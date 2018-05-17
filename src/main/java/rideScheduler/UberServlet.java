/*
 * Copyright (c) 2016 Uber Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package rideScheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.uber.sdk.core.auth.OAuth2Credentials;
import com.uber.sdk.core.client.CredentialsSession;
import com.uber.sdk.core.client.SessionConfiguration;
import com.uber.sdk.rides.client.services.RidesService;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.UserProfile;
import com.uber.sdk.rides.client.model.Ride;
import com.uber.sdk.rides.client.model.RideEstimate;
import com.uber.sdk.rides.client.model.RideRequestParameters;
import com.uber.sdk.rides.client.model.PriceEstimate;
import com.uber.sdk.rides.client.model.PriceEstimatesResponse;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import com.google.gson.*;
import java.awt.geom.Point2D;
import retrofit2.Response;

/**
 * Demonstrates how to authenticate the user and load their profile via a servlet.
 */
public class UberServlet extends HttpServlet {

    private OAuth2Credentials oAuth2Credentials;
    private Credential credential;
    private RidesService uberRidesService;
    private static final int MAX_WAIT_TIME = 10000; //in milliseconds
    private static final int SLEEP_TIME = 3000; //in milliseconds

    /**
     * Clear the in memory credential and Uber API service once a call has ended.
     */
    @Override
    public void destroy() {
        uberRidesService = null;
        credential = null;

        super.destroy();
    }

    /**
     * Before each request, fetch an OAuth2 credential for the user or redirect them to the
     * OAuth2 login page instead.
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        SessionConfiguration config = Server.createSessionConfiguration();
        if (oAuth2Credentials == null) {
            oAuth2Credentials = Server.createOAuth2Credentials(config);
        }

        // Load the user from their user ID (derived from the request).
        HttpSession httpSession = req.getSession(true);
        if (httpSession.getAttribute(Server.USER_SESSION_ID) == null) {
            httpSession.setAttribute(Server.USER_SESSION_ID, new Random().nextLong());
        }
        credential = oAuth2Credentials.loadCredential(httpSession.getAttribute(Server.USER_SESSION_ID).toString());

        if (credential != null && credential.getAccessToken() != null) {

            if (uberRidesService == null) {

                CredentialsSession session = new CredentialsSession(config, credential);

//                Set up the Uber API Service once the user is authenticated.
                UberRidesApi api = UberRidesApi.with(session).build();
                uberRidesService = api.createService();
            }

            super.service(req, resp);
        } else {
            resp.sendRedirect(oAuth2Credentials.getAuthorizationUrl());
        }
    }

    
    protected Response<Ride> runAlgorithm(int n, float startLat, float startLong, float endLat, float endLong) throws IOException, InterruptedException {
	double minPrice = Double.MAX_VALUE;
	double currPrice = -1;
	RideRequestParameters rideRequestParameters = new RideRequestParameters.Builder()
	    .setPickupCoordinates(startLat, startLong)
	    .setDropoffCoordinates(endLat, endLong)
	    .build();
	Response<Ride> ride = null;
	String fareId = "";

	System.out.println("--Algorithm running-----");

	for (int i = 0; i < (int) (Math.log(n)); i++) {
	    System.out.println("Getting benchmark: " + i);
	    Response<RideEstimate> response = uberRidesService.estimateRide(rideRequestParameters).execute();
	    currPrice = response.body().getFare().getValue().doubleValue();
	    System.out.println(currPrice);
	    if (currPrice < minPrice) {
		minPrice = currPrice;
	    }
	    System.out.println("Sleeping for 3 seconds before getting price again");
	    Thread.sleep(SLEEP_TIME);
	}

	for (int i = 0; i < MAX_WAIT_TIME; i+= SLEEP_TIME) {
	    System.out.println("----Finding best price..." + i);
	    Response<RideEstimate> response = uberRidesService.estimateRide(rideRequestParameters).execute();
	    currPrice = response.body().getFare().getValue().doubleValue();
	    System.out.println(currPrice);
	    if (currPrice < minPrice) {
		fareId = response.body().getFare().getFareId();
		System.out.println(fareId);
		rideRequestParameters = new RideRequestParameters.Builder()
	    	    .setPickupCoordinates(startLat, startLong)
	    	    .setDropoffCoordinates(endLat, endLong)
		    .setFareId(fareId)
	    	    .build();
		ride = uberRidesService.requestRide(rideRequestParameters).execute();
		//System.out.println("-----Testing...." + ride.body().getRideId());
		if (ride == null) {
	    	    System.out.println("There was trouble requesting a ride.");
		} else {
	    	    System.out.println("Ride request successful.");
		}
		System.out.println("-----Printing ride body:");
		//System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(ride));
	 	System.out.println("Getting ride ID");
		//This will result in a NullPointerException due to Internal Server Errors from the Uber Rides API.
		//We're not sure how to resolve this but this was intended to request a ride for the user.
		System.out.println(ride.body().getRideId());
		return ride;
	    }
	    System.out.println("Sleeping for 3 seconds before getting price again");
	    Thread.sleep(SLEEP_TIME);
	}
	System.out.println("----max wait time reached, requesting ride now");
	Response<RideEstimate> response = uberRidesService.estimateRide(rideRequestParameters).execute();
	fareId = response.body().getFare().getFareId();
	System.out.println(fareId);
	rideRequestParameters = new RideRequestParameters.Builder()
	    .setPickupCoordinates(startLat, startLong)
	    .setDropoffCoordinates(endLat, endLong)
	    .setFareId(fareId)
	    .build();
	//rideRequestParameters = rideRequestBuilder.setFareId(fareId).build();
	ride = uberRidesService.requestRide(rideRequestParameters).execute();
	if (ride == null) {
	    System.out.println("There was trouble requesting a ride.");
	} else {
	    System.out.println("Ride request successful.");
	}
	System.out.println("Printing ride body-----");
	System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(ride));
	System.out.println("-----Testing...." + ride.body().getRideId());
	System.out.println("Getting ride ID");
	//This will result in a NullPointerException due to Internal Server Errors from the Uber Rides API.
	//We're not sure how to resolve this but this was intended to request a ride for the user.
	System.out.println(ride.body().getRideId());
	System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(ride));
	return ride;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Fetch the user's profile.
        UserProfile userProfile = uberRidesService.getUserProfile().execute().body();
	Response<Ride> ride = null;
/*
	//This is for testing purposes.
	Point2D.Float p1 = Geocode.getCoordinates("1401 John F Kennedy Blvd, Philadelphia, PA");
	Point2D.Float p2 = Geocode.getCoordinates("101 S 39th St, Philadelphia, PA");
	System.out.println("------------Finished getting coordinates------------");
	try {
		ride = runAlgorithm(100, (float) p1.getX(), (float) p1.getY(), (float) p2.getX(), (float) p2.getY());
		System.out.println(ride.body().getRideId());
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
*/

	Scheduler scheduler = new Scheduler();
	List<Point2D.Float> locations = scheduler.getUserInput();

	for (int i = 0; i < locations.size() - 1; i+=2) {
		Point2D.Float orig = locations.get(i);
		Point2D.Float dest = locations.get(i + 1);
		try {
			ride = runAlgorithm(locations.size(), (float) orig.getX(), (float) orig.getY(), (float) dest.getX(), (float) dest.getY());
			//sb.append(ride.body().getRideId() + "\n");
			System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(ride));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().printf("Logged in as %s%n", userProfile.getEmail());
    }
}
