/*
 * Author's name and email: Michael
 * Program description: This class is used by Menu class to either take a photo 
 *  which will be used for ocr.analyze or show a video feed with awt CanvasFrame.
 * Latest version: 5:27 PM, 5/3/2019. Now the program has multiple classes.
 * Older versions: 2:43 PM, 4/25/2019.
 *	 Didnt record older versions.
 */

import org.bytedeco.javacv.*;
import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

final public class Camera implements Runnable
{
	CanvasFrame canvas = new CanvasFrame("Web Cam");
	
	// Objects used in run and takeAPhoto methods.
	FrameGrabber grabber = new OpenCVFrameGrabber(0);
	OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	IplImage img;

	public Camera()
	{
		canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
	}

	public void run()
	{
		try
		{
			/* When application starts, this method is called automatically from 
			main method of Menu class. So grabber is started before we start taking 
			photos. */
			grabber.start();
			while (true)
			{
				Frame frame = grabber.grab();

				//img = converter.convert(frame);

				canvas.showImage(frame); //converter.convert(img));
				
				//Thread.sleep(41);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void takeAPhoto()
	{
		try
		{
			//grabber.start(); // Causes error. So i moved 3 declarations to field
			
			Frame frame = grabber.grab();

			img = converter.convert(frame);

			cvSaveImage("id.jpg", img);

			//canvas.showImage(converter.convert(img));

			//Thread.sleep(1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
