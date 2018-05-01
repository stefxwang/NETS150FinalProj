package rideScheduler;

public class SchedulerAlgorithm {
	
	// private fields
	private int[] allPrices;
	private int benchmark = 0;
	private int POSITIVE_INFINITY;
	private int targetPrice = 0;
	
	public int algorithm (int[] inputs) {
		allPrices = inputs;
		
		// Loop through first ln(n) and find the min price. This is the benchmark.
		for (int i = 0; i < (int) (Math.log(allPrices.length)); i++) {
			int smallest = 0;
			if (allPrices[i] < POSITIVE_INFINITY) {
				smallest = allPrices[i];
			}
			if (allPrices[i] < smallest) {
				smallest = allPrices[i];
				benchmark = smallest;
			}
		}
		
		// Loop through all prices AFTER the ones we rejected. 
		// Find the next price lower than the benchmark.
		for (int j = (int) (Math.log(allPrices.length)); j < allPrices.length; j++) {
			// Benchmark should never equal 0 if inputs are correct.
			if (benchmark != 0) {
				if (allPrices[j] < benchmark) {
					// Make a call to the API
				}
			}
			targetPrice = allPrices[j];
		}
		// This should be the price we're looking for. 
		return targetPrice;
	}
}
