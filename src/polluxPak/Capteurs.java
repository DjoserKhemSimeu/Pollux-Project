package polluxPak;




import java.io.IOException;

import lejos.hardware.port.Port;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;

public class Capteurs {
	private EV3UltrasonicSensor uss;
	private SampleProvider usp;
	private float[]sampleUltra;
	private EV3TouchSensor touch;
	private SampleProvider tsp;
	private float[]sampleTouch;
	public static float frequence=1;
	TestColor color;
	
	public Capteurs (Port s1,Port s3,Port s4) throws IOException {
		color=new TestColor (s4);
		uss=new EV3UltrasonicSensor(s1);
		touch = new EV3TouchSensor(s3);
		usp=new PublishFilter(uss.getDistanceMode(),"Ultrasonic readings", frequence);
		tsp=new PublishFilter(touch.getTouchMode(),"Touch readings", frequence);
		sampleUltra=new float [usp.sampleSize()];
		sampleTouch=new float [tsp.sampleSize()];
		
	}
	

	public Capteurs() {
		// TODO Auto-generated constructor stub
	}
	public void maj() {
		usp.fetchSample(sampleUltra,0);
		tsp.fetchSample(sampleTouch,0);
	}
	public String getColor() {
		return color.getColor();
	}
	public void close() {
		uss.close();
		touch.close();
	}


	
	public double getDistance() {
		usp.fetchSample(sampleUltra,0);
		return sampleUltra[0];
	}
	
	public boolean getTouch() {
		tsp.fetchSample(sampleTouch,0);
		if(sampleTouch[0]==1) {
			return true;
		}else {
			return false;
		}
	}

}
