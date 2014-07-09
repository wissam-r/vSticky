package filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.os.Process;
import android.util.Log;


public class DetectionFilterThreading implements TrackFilter {
public final static String TAG = "detectionFilter"; 
	Filter subFilter;
	
	public DetectionFilterThreading(Mat detected, Mat move){
		this(new EmptyFilter(), detected, move);
	}
	
	public DetectionFilterThreading(Filter father, Mat detected, Mat move){
		this.subFilter = father;
		this.imageDetected = detected;
		this.move = move;
		mFeatureDetector = FeatureDetector.create(FeatureDetector.ORB);
		mDescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
		mDescriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
	}	
	
	private final MatOfKeyPoint mSceneKeypoints = new MatOfKeyPoint();
	private final Mat mSceneDescriptors = new Mat();
	private final List<Mat> mSceneCorners = new ArrayList<Mat>();//(4, 1,	CvType.CV_32FC2);
	private final Mat mGraySrc = new Mat();
	private final List<MatOfDMatch> mMatches = new ArrayList<MatOfDMatch>();
	FeatureDetector mFeatureDetector;
	DescriptorExtractor mDescriptorExtractor;
	DescriptorMatcher mDescriptorMatcher;
	
	
	//private List<Integer> power = new ArrayList<Integer>();
	private final int maxCurrent = 8;
	//private final int maxDetectoin = 5;
	Mat imageDetected; 
	Mat move;
	//private Point[] detectedPos;
	
	//private List<Byte> ids= new LinkedList<Byte>();
	
	private List<Integer> Current = new LinkedList<Integer>();
	private Queue<Integer> queue = new  LinkedList<Integer>();
	
	private List<Target> targets = new ArrayList<Target>();
	private List<Worker> works = new ArrayList<Worker>();
	public void addReference(Mat rgb,int id){	
		synchronized (Current) {
			int index = targets.size();
			Target target = new Target(rgb.clone(), id, imageDetected);
			Imgproc.cvtColor(target.image, target.image, Imgproc.COLOR_RGBA2GRAY);
			subFilter.apply(target.image, target.image);
			target.corners = new Mat(4, 1, CvType.CV_32FC2);
			target.corners.put(0, 0, new double[] {0.0, 0.0});
			target.corners.put(1, 0, new double[] {target.image.cols(), 0.0});
			target.corners.put(2, 0, new double[] {target.image.cols(), target.image.rows()});
			target.corners.put(3, 0, new double[] {0.0, target.image.rows()});
			target.keyPoints = new MatOfKeyPoint();
			mFeatureDetector.detect(target.image, target.keyPoints);
			target.descriptor = new Mat();
			mDescriptorExtractor.compute(target.image, target.keyPoints, target.descriptor);
			target.alpha = 0.6;
			targets.add(target);
			
			mSceneCorners.add(new Mat(4, 1, CvType.CV_32FC2));
			mMatches.add(new MatOfDMatch());
			
			works.add(new Worker(this, mSceneKeypoints, mSceneDescriptors, mSceneCorners.get(index), target, null));
			
			if(targets.size()<maxCurrent)
			{
				Current.add(index);
				target.setActive(true);
				Log.d(TAG+"s", "put in current "+index);
			}else{
				queue.add(index);
				target.setActive(false);
				Log.d(TAG+"s", "put in queue "+index);
			}
		}
	}
	
	public void addReference(final Context context,	final int referenceImageResourceID,int id) throws IOException{
		Mat rgb = Utils.loadResource(context, referenceImageResourceID, Highgui.CV_LOAD_IMAGE_COLOR);
		Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_BGR2RGBA);
		addReference(rgb,id);
	}
	
	public void addReference(final String path,int id) {
		Mat rgb = Highgui.imread(path);
		Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_BGR2RGBA);
		addReference(rgb,id);
	}


	private void setDetectables(){
		for(int k=0;k<Current.size();k++){
			Integer i = Current.get(k);
			//if(power.get(i) <= -1*maxDetectoin){
			if(targets.get(i).getPower() <= -1*Target.maxDetectoin){
				Log.d(TAG+"s", "remove from current "+i);
				queue.add(i);
				Current.remove(i);
				//power.set(i, 0);
				targets.get(i).setActive(false);
			}
		}
		for(int i=Current.size();i<maxCurrent && queue.size()>0;i++){
			targets.get(queue.peek()).setActive(true);
			Current.add(queue.poll());
		}
		String l = "";
		for(int i:Current){
			l+= i +", ";
		}
		Log.d(TAG+"s", "in current "+l);
		 l = "";
		for(int i:queue){
			l+= i +", ";
		}
		Log.d(TAG+"s", "in queque "+l);
	}
	
	private boolean anyDetectable(){
		return Current.size()>0;
	}
	
	public void apply(final Mat src, final Mat dst) {		
		setDetectables();
		
		if(!src.size().equals(dst.size()))
			src.copyTo(dst);
		
		if(!anyDetectable()){
			return;
		}
		if(src.type()!=CvType.CV_8UC1)
			Imgproc.cvtColor(src, mGraySrc, Imgproc.COLOR_RGBA2GRAY);
		else
			src.copyTo(mGraySrc);
		subFilter.apply(mGraySrc, mGraySrc);
		mFeatureDetector.detect(mGraySrc, mSceneKeypoints);
		mDescriptorExtractor.compute(mGraySrc, mSceneKeypoints,	mSceneDescriptors);
		Thread[] thworks = new Thread[Current.size()];
		for(int k=0;k<Current.size();k++){
			int i = Current.get(k);
			this.works.get(i).setDst(dst);
			thworks[k] = new Thread(this.works.get(i));
			thworks[k].start();
		}
		for (int i = 0; i < thworks.length; i++) {
            try {
            	thworks[i].join();
            } catch (InterruptedException ie) {
                System.err.println("join " + i + " failed: " + ie);
            }
        }
	}
	
	@Override
	public Point[] getTrackedCenters() {
		Point[] points = new Point[Current.size()];
		for(int k = 0;k<Current.size();k++){
			int i = Current.get(k);
			if(!targets.get(i).found) continue;
			points[k] = targets.get(i).getPosition();
		}
		return points;
	}

	@Override
	public int getTracked(Point at) {
		for(int i: Current){
			Target target = targets.get(i);
			if(!target.found) continue;
			Point p = target.getPosition();
			if(at.inside(new Rect((int)(p.x-target.usedSize.width/2),
								(int)(p.y-target.usedSize.height/2),
								(int)(target.usedSize.width),
								(int)(target.usedSize.height)))){
				return target.id;
			}
		}
		return -1;
	}

}

class Target{
	public static int maxDetectoin = 5;
	
	public Mat image;
	public int id;
	
	public MatOfKeyPoint keyPoints;
	public Mat descriptor;
	public Mat corners;
	
	private boolean active;
	public boolean isActive(){ return active; }
	public void setActive(boolean active){
		if(this.active == active) return;
		this.active = active;
		if(!active){
			power = 0;
			detectedPos = null;
		}
	}
	
	public boolean found;
	
	private int power;
	public Integer getPower(){
		if(isActive()) return power;
		return null;
	}
	public void increasePower(){
		if(!isActive() || power == maxDetectoin) return;
		++power;
		Log.d(DetectionFilterThreading.TAG,"photo "+id+ " power up "+power);
	}
	public void decreasePower(){
		if(!isActive() || power == -maxDetectoin) return;
		--power;
		Log.d(DetectionFilterThreading.TAG,"photo "+id+ " power down "+power);
	}
	
	public Mat detectedImage;
	public Size usedSize;
	public Mat getDetectedImage(int height){
		usedSize = new Size(detectedImage.width()/detectedImage.height()*height, height);
		Mat m = new Mat();
		Imgproc.resize(detectedImage, m, usedSize);
		return m;
	}
	public double alpha;
	
	private Point detectedPos;
	public Point getPosition(){
		if(isActive()) return detectedPos;
		return null;
	}
	public void setPosition(Point pos){
		if(!isActive()) return;
		this.detectedPos = pos;
	}
	
	
	public Target(Mat image,int id,Mat detectedImage){
		this.image = image;
		this.id = id;
		setActive(false);
		this.detectedImage = detectedImage;
		usedSize = detectedImage.size();
		alpha = 1;
		found = false;
	}
}

class Worker implements Runnable{
	
	private final String TAG;
	
	DetectionFilterThreading master;
	MatOfKeyPoint mSceneKeypoints;
	Mat mSceneDescriptors;
	Mat mSceneCorners;
	Target target;
	Mat dst;
	
	public Worker(DetectionFilterThreading master,MatOfKeyPoint mSceneKeypoints,Mat mSceneDescriptors,Mat mSceneCorners, Target target,Mat dst){
		this.master = master;
		this.mSceneKeypoints = mSceneKeypoints;
		this.mSceneDescriptors = mSceneDescriptors;
		this.mSceneCorners = mSceneCorners;
		this.target = target;
		this.dst = dst;
		
		this.TAG = DetectionFilterThreading.TAG+"-w:"+target.id;
	}
	
	public void setDst(Mat dst){
		this.dst = dst;
	}
	
	@Override
	public void run() {
		Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
		
		if(dst == null) return;
		MatOfDMatch matches = new MatOfDMatch();
		master.mDescriptorMatcher.match(mSceneDescriptors, target.descriptor, matches);
		DMatch[] matchesList = matches.toArray();
		if (matchesList.length < 4) {
			Log.d(TAG, "fail: matchesList.length < 4 ");
			mSceneCorners.create(0, 0, CvType.CV_32FC2);
			return;
		}
		KeyPoint[] referenceKeypointsList;
		referenceKeypointsList =	target.keyPoints.toArray();
		KeyPoint[] sceneKeypointsList =	mSceneKeypoints.toArray();
		double minDist = Double.MAX_VALUE;
		for(DMatch match : matchesList) {
			if (match.distance < minDist) {
				minDist = match.distance;
			}
		}
		if (minDist > 40.0) {
			// The target is completely lost.
			// Discard any previously found corners.
			Log.d(TAG, "fail: minDist > 40.0 ");
			mSceneCorners.create(0, 0, CvType.CV_32FC2);
			return;
		} else if (minDist < 25.0) {
			// Identify "good" keypoints based on match distance.
			ArrayList<Point> goodReferencePointsList = new ArrayList<Point>();
			ArrayList<Point> goodScenePointsList = new ArrayList<Point>();
			double maxGoodMatchDist = 1.75 * minDist;
			for(DMatch match : matchesList) {
				if (match.distance < maxGoodMatchDist) {
					goodReferencePointsList.add(referenceKeypointsList[match.trainIdx].pt);
					goodScenePointsList.add(sceneKeypointsList[match.queryIdx].pt);
				}
			}
			if (goodReferencePointsList.size() >= 4 &&
				goodScenePointsList.size() >= 4) {
				
				MatOfPoint2f goodReferencePoints = new MatOfPoint2f();
				goodReferencePoints.fromList(goodReferencePointsList);
				MatOfPoint2f goodScenePoints = new MatOfPoint2f();
				goodScenePoints.fromList(goodScenePointsList);
				Mat homography = Calib3d.findHomography(goodReferencePoints, goodScenePoints);
				Mat mCandidateSceneCorners = new Mat(4, 1, CvType.CV_32FC2);
				MatOfPoint mIntSceneCorners = new MatOfPoint();
				Core.perspectiveTransform(target.corners,mCandidateSceneCorners, homography);
				mCandidateSceneCorners.convertTo(mIntSceneCorners, CvType.CV_32S);
				if (Imgproc.isContourConvex(mIntSceneCorners)) {
					mCandidateSceneCorners.copyTo(mSceneCorners);
				}
			}else{
				// There are too few good points to find the homography.
			}
			
		}else{
			// The target is lost but maybe it is still close.
			// Keep any previously found corners.
		}
		
		if (mSceneCorners.height() < 4){
			target.decreasePower();
			target.found = false;
			Log.d(TAG, "fail: mSceneCorners.height() < 4 ");
			return;
		}
		target.setPosition(getCenter(mSceneCorners));
		if(!target.getPosition().inside(new Rect(-dst.cols()/2, -dst.rows()/2, 2*dst.cols(), 2*dst.rows()))){
			target.decreasePower();
			target.found = false;
			Log.d(TAG, "fail: !target.getPosition().inside(new Rect(-dst.cols()/2, -dst.rows()/2, 3/2*dst.cols(), 3/2*dst.rows())) ");
			Log.i(TAG, "position: "+target.getPosition());
			return;
		}
		target.increasePower();
		target.found = true;		
		
		int maxDimension = Math.min(dst.width(), dst.height()) / 4;
		Mat dstROI ,temp = target.getDetectedImage(maxDimension);	
		if((int)target.getPosition().y <= temp.rows()/2){
			//above screen
			Core.flip(master.move.t(), temp, 0);
			dstROI = dst.submat(0,temp.rows(),dst.cols()/2-temp.cols()/2,dst.cols()/2+temp.cols()/2+temp.cols()%2);
		}else if((int)target.getPosition().y >= dst.rows()-temp.rows()/2){
			//down screen
			Core.flip(master.move.t(), temp, 1);
			dstROI = dst.submat(dst.rows() - temp.rows(),dst.rows(),dst.cols()/2-temp.cols()/2,dst.cols()/2+temp.cols()/2+temp.cols()%2);
		}else if((int)target.getPosition().x <= temp.cols()/2){
			//on left of screen
			Core.flip(master.move, temp, -1);
			dstROI = dst.submat(dst.rows()/2-temp.rows()/2,dst.rows()/2+temp.rows()/2+temp.rows()%2, 0, temp.cols());
		}else if((int)target.getPosition().x >= dst.cols()-temp.cols()/2){
			//on right of screen
			temp = master.move;
			dstROI = dst.submat(dst.rows()/2-temp.rows()/2,dst.rows()/2+temp.rows()/2+temp.rows()%2, dst.cols()-temp.cols(), dst.cols());
		}else{				
			dstROI = dst.submat((int)target.getPosition().y-temp.rows()/2,
								(int)target.getPosition().y+temp.rows()/2,
								(int)target.getPosition().x-temp.cols()/2,
								(int)target.getPosition().x+temp.cols()/2);
		}
		Core.addWeighted(dstROI, 1-target.alpha, temp, target.alpha, 0, dstROI);		
		Log.d(TAG, "success");
	}
	
	private static Point getCenter(Mat corners){
		Point c = new Point(0, 0);
		for(int i=0;i<4;i++){
			Point temp = new Point(corners.get(i, 0));
			c.x+=temp.x;
			c.y+=temp.y;
		}
		c.x/=4;
		c.y/=4;
		return c;
	}
}

