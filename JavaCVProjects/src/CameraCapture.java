import java.awt.event.InputEvent;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.VideoInputFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;

public class CameraCapture implements Runnable{
	
    final int INTERVAL=100;///you may use interval
    IplImage image;
    CanvasFrame canvas = new CanvasFrame("Web Cam");
    public CameraCapture() {
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void run() {
        FrameGrabber grabber = new VideoInputFrameGrabber(0); // 1 for next camera
        int i=0;
        try {
            grabber.start();
            IplImage img;
            while (i < 10) {
                img = grabber.grab();
                if (img != null) {
                    //cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise
                	opencv_highgui.cvLoadImage("img");
                    opencv_highgui.cvSaveImage("output.jpg", img);
                    // show image on window
                    
                    int s1 = InputEvent.BUTTON1_MASK;
                    
                    if( true ){
                    	int s2;
                    	
                    }
                    
                    int s2 = 1;
                    
                    Object s = new Integer(90);
                    System.out.println((int)s);
                    
                    canvas.showImage(img);  
                }; 
                Thread.sleep(INTERVAL);
            }
        } catch (Exception e) {
        	System.out.println(e);
        }
    }
    
    public static void main(String[] args) {
        CameraCapture cc = new CameraCapture();
        Thread th = new Thread(cc);
        th.start();
    }

}
