package filters;

import org.opencv.core.Point;


public interface TrackFilter extends Filter{
	Point[] getTrackedCenters();
	int getTracked(Point at);
}
