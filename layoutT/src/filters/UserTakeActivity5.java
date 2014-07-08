package filters;

import java.io.IOException;
import java.util.ArrayList;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.example.layoutt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

public class UserTakeActivity5 extends Activity implements CvCameraViewListener2,OnTouchListener{
	private static final String  TAG = "UserTake::Activity-5";

	private CameraBridgeVeiwCustom mOpenCvCameraView;
	//private DetectionFilter filter;
	private DetectionFilterThreading filter;
	
	private ArrayList<Integer> ids;
	private final String IDS = "IDS";
	private ArrayList<String> paths;
	private final String PATHS = "PATHS"; 

	//private Mat lastMat;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG, "OpenCV loaded successfully");
				try {
					Mat rgb,move;
					rgb = Utils.loadResource(UserTakeActivity5.this, android.R.drawable.ic_menu_add, Highgui.CV_LOAD_IMAGE_COLOR);
					Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_BGR2RGBA);
					move = Utils.loadResource(UserTakeActivity5.this,R.drawable.ic_action_search, Highgui.CV_LOAD_IMAGE_COLOR);
					Imgproc.cvtColor(move, move, Imgproc.COLOR_BGR2RGBA);
					//filter = new DetectionFilter(rgb, move);
					filter = new DetectionFilterThreading(rgb, move);
					//filter = new DetectionFilterThreading(new HistogramEqualizer(), rgb, move);
					//filter = new DetectionFilterThreading(new GaussianBlurFilter(new Size(3, 3)), rgb, move);
				} catch (IOException e) {
					e.printStackTrace();
					finish();
				}
				if(getIntent().getBooleanExtra("data", false))
					new Thread(new Runnable() {
	
						@Override
						public void run() {
							if(ids.size()!= paths.size()) throw new IllegalArgumentException("ids.length!= paths.size()");
							for(int i = 0;i<ids.size();i++){
								String path = paths.get(i);
								int id = ids.get(i);
								filter.addReference(path, id);
							}
						}
					}).start();


				/* Now enable camera view to start receiving frames */
				mOpenCvCameraView.setOnTouchListener(UserTakeActivity5.this);
				mOpenCvCameraView.enableView();

			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
			//lastMat = new Mat();
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if(savedInstanceState == null){
	        ids = getIntent().getIntegerArrayListExtra("ids");
	        paths = getIntent().getStringArrayListExtra("images");
        }else{
        	ids = savedInstanceState.getIntegerArrayList(IDS);
        	paths = savedInstanceState.getStringArrayList(PATHS);
        }
		
		Log.d(TAG, "Creating and setting view");
		mOpenCvCameraView = (CameraBridgeVeiwCustom) new CameraBridgeVeiwCustom(this, -1);
		setContentView(mOpenCvCameraView);
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.enableFpsMeter();

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putStringArrayList(PATHS, paths);
		outState.putIntegerArrayList(IDS, ids);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause()
	{
		Log.i(TAG, "pausing");
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	@Override
	public void onResume()
	{
		Log.i(TAG, "resuming");
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
	}

	public void onDestroy() {
		Log.i(TAG, "destroying");
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}


	@Override
	public void onCameraViewStarted(int width, int height) {
		Log.i(TAG, "frameSize = "+width+"|"+height);
	}

	@Override
	public void onCameraViewStopped() {

	}



	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		//		inputFrame.rgba().assignTo(lastMat);
		//		Mat res = new Mat();
		//		filter.apply(lastMat, res);
		Mat res = inputFrame.rgba();
		filter.apply(inputFrame.gray(), res);

		return res;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.d(TAG, "motion event : "+event.getActionMasked());
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			int id = filter.getTracked(new Point(event.getX(), event.getY()));
			Toast.makeText(this,  "you want note "+id, Toast.LENGTH_SHORT).show();
			Intent data = new Intent();
			data.putExtra("id", id);
			setResult(RESULT_OK, data);
			finish();
			break;
		default:
			break;
		}

		return false;
	}

}
