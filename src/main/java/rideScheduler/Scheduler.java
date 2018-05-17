package rideScheduler;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scheduler {
	public static int[] firstTimeArr;
	public static int[] secondTimeArr;
	private Scanner sc;

	public Scheduler() {
		sc = new Scanner(System.in);
	}

	public List<Point2D.Float> getUserInput() {
		//example of how to get coordinates from a well-formatted address
		//System.out.println(Geocode.getCoordinates("1401 John F Kennedy Blvd, Philadelphia, PA").toString());
		
		List<Point2D.Float> locations = new ArrayList<>();

		System.out.println("How many times do you need to call an Uber/Lyft today");
		
		int numTimes = sc.nextInt();
		String origin = "";
		String destination = "";		

		for (int i = 0; i < numTimes; i++) {
			//keeping track of the first time so we can call these all easily because we can tell the i
			System.out.println("input the first time you want to start looking\n" + "Format 24 hour time as 1605 for 4:05pm for example");
			firstTimeArr = new int[numTimes];
			int firstTime = sc.nextInt();
			firstTimeArr[i] = firstTime;
			
			sc.nextLine();

			//keeping track of the end time 
			System.out.println("input the second time you want to stop looking\n" + "Format 24 hour time as 1605 for 4:05pm for example");
			secondTimeArr = new int[numTimes];
			int secondTime = sc.nextInt();
			secondTimeArr[i] = secondTime;
			
			sc.nextLine();

			//All address inputs must be well-formatted
			System.out.println("Input your origin address: ");
			origin = sc.nextLine();
		
			//sc.nextLine();
			
			System.out.println("Input your destination address: ");
			destination = sc.nextLine();			

			locations.add(Geocode.getCoordinates(origin));
			locations.add(Geocode.getCoordinates(destination));
		}
		sc.close();
		return locations;
	}

}
