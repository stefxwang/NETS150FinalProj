package rideScheduler;

import java.awt.geom.Point2D;

public interface ServerInterface {
	public double getPriceEstimate(Point2D.Float start, Point2D.Float end);
	public boolean requestRide();
}
