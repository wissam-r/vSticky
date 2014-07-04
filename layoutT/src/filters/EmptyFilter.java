package filters;

import org.opencv.core.Mat;

public class EmptyFilter implements Filter {

	@Override
	public void apply(Mat src, Mat dst) {
	}

}
