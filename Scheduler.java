package rideScheduler;

import java.util.Scanner;

public class Scheduler {
	public static int[] firstTimeArr;
	public static int[] secondTimeArr;
	

	public static void main(String[] args) {
		//example of how to get coordinates from a well-formatted address
		System.out.println(Geocode.getCoordinates("1600 Amphitheatre Pkwy, Mountain View, CA").toString());
		Scanner sc = new Scanner(System.in);
		
		System.out.println("How many times do you need to call an Uber/Lyft today");
		
		int numTimes = sc.nextInt();
		
		for (int i = 0; i < numTimes; i++) {
			//keeping track of the first time so we can call these all easily because we can tell the i
			System.out.println("input the first time you want to start looking\n" + "Format 24 hour time as 1605 for 4:05pm for example");
			firstTimeArr = new int[numTimes];
			int firstTime = sc.nextInt();
			firstTimeArr[i] = firstTime;
			
			//keeping track of the end time 
			System.out.println("input the second time you want to stop looking\n" + "Format 24 hour time as 1605 for 4:05pm for example");
			secondTimeArr = new int[numTimes];
			int secondTime = sc.nextInt();
			secondTimeArr[i] = secondTime;

			//keep making calls to the api until the times are up
			//add the prices returned to the input[] algorithm
			//We should call scheduler algorithm here with the price inputs 
		}
		sc.close();
		
	}
}
