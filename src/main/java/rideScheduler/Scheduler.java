package rideScheduler;

import java.util.Scanner;

public class Scheduler {
	public static int[] firstTimeArr;
	public static int[] secondTimeArr;
	public static int maxPrice; 
	

	public static void main(String[] args) {
		//example of how to get coordinates from a well-formatted address
		System.out.println(Geocode.getCoordinates("1401 John F Kennedy Blvd, Philadelphia, PA").toString());
		Scanner sc = new Scanner(System.in);
		
		System.out.println("How many times do you need to call an Uber/Lyft today");
		
		int numTimes = sc.nextInt();
		
		for (int i = 0; i < numTimes; i++) {
			//keeping track of the first time so we can call these all easily because we can tell the i
			System.out.println("input the first time you want to start looking");
			firstTimeArr = new int[numTimes];
			int firstTime = sc.nextInt();
			firstTimeArr[i] = firstTime;
			
			//keeping track of the end time 
			System.out.println("input the second time you want to stop looking");
			secondTimeArr = new int[numTimes];
			int secondTime = sc.nextInt();
			secondTimeArr[i] = secondTime;
			
			
			System.out.println("What is the max price drop you are waiting for");
			maxPrice = sc.nextInt();
			
			
			//keep making calls to the api until the times are up
			//add the prices returned to the input[] algorithm
			//We should call scheduler algorithm here with the price inputs 
		}
		sc.close();
		
	}
}
