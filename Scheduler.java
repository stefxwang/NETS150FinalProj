import java.util.Scanner;

public class Scheduler {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("How many times do you need to call an Uber/Lyft today");
		
		int numTimes = sc.nextInt();
		
		for (int i = 0; i < numTimes - 1; i++) {
			//keeping track of the first time so we can call these all easily because we can tell the i
			System.out.println("input the first time you want to start looking");
			int[] firstTimeArr = new int[numTimes];
			int firstTime = sc.nextInt();
			firstTimeArr[i] = firstTime;
			
			//keeping track of the end time 
			System.out.println("input the second time you want to stop looking");
			int [] secondTimeArr = new int[numTimes];
			int secondTime = sc.nextInt();
			secondTimeArr[i] = secondTime;
		}
		sc.close();
	}
}
