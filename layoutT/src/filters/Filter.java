package filters;

import org.opencv.core.Mat;

public interface Filter {
	void apply(final Mat src, final Mat dst);
}
