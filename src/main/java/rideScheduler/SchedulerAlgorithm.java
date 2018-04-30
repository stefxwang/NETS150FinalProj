package rideScheduler;

public class SchedulerAlgorithm {

	int[] input;
	
	public SchedulerAlgorithm(int[] inputs) {

		input = inputs;
		int prevPrice = 0;
		for (int price: input) {
			if (prevPrice != 0) {
				if (price - prevPrice > Scheduler.maxPrice) {
					//make a call to api
				}
			}
			prevPrice = price;
		}
	}
	
}
