Project Structure:  
src  
--> main  
------> java  
----------> rideScheduler  
--------------> Scheduler.java  
--------------> SchedulerAlgorithm.java  
------> resources  
----------> secrets.properties

User Manual:  
(Execute on command in Terminal at root project directory)  
./gradlew run

Once the server is up and running, navigate to: http://localhost:8181 to start the program.

You will be asked the following questions:  
1. How many times do you need to request Uber today?  
2. For each time, you will be asked to enter an origin address and a destination address.  

Navigate to see results of your ride requests.

Things to Note:
- All addresses must be well formatted in order for the GeoCode to correctly retrieve the latitude and longitude coordinates.
- We adapted the servlet-sample from the Uber Rides API to suit our needs. All credits have been given to Uber by preserving their header in the relevant files (Server.java, UberServlet.java, OAuth2CallbackServlet.java.)
