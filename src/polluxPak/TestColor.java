package polluxPak;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;


public class TestColor {
	Port port;
	EV3ColorSensor color;
	SampleProvider sampleColor;
	float[]red;
	float[]green;
	float[]blue;
	float[]yellow;
	float[]black;
	float[]white;
	float[]none;
	
	public TestColor (Port port) {
		 port = LocalEV3.get().getPort("S4");
		color = new EV3ColorSensor(port);
		sampleColor= new MeanFilter(color.getRGBMode(), 1);
		color.setFloodlight(Color.WHITE);
		initialization();
	}
	public void initialization() {

		
		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate none", 0,5);
		Button.ENTER.waitForPressAndRelease();
		none = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(none, 0);
		LCD.clear(4);
		LCD.clear(5);
		
		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate white", 0,5);
		Button.ENTER.waitForPressAndRelease();
		white = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(white, 0);
		LCD.clear(4);
		LCD.clear(5);
		
		
		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate blue", 0,5);
		Button.ENTER.waitForPressAndRelease();
		blue = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(blue, 0);
		LCD.clear(4);
		LCD.clear(5);
		
		
		
		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate red", 0,5);
		Button.ENTER.waitForPressAndRelease();
		red = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(red, 0);
		LCD.clear(4);
		LCD.clear(5);

		
		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate green", 0,5);
		Button.ENTER.waitForPressAndRelease();
		green = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(green, 0);
		LCD.clear(4);
		LCD.clear(5);
		
		
		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate yellow", 0,5);
		Button.ENTER.waitForPressAndRelease();
		yellow = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(yellow, 0);
		LCD.clear(4);
		LCD.clear(5);


		LCD.drawString("Press enter to", 0,4);
		LCD.drawString("calibrate black", 0,5);
		Button.ENTER.waitForPressAndRelease();
		black = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(black, 0);
		LCD.clear(4);
		LCD.clear(5);
		
	}
	
	public String getColor() {
		float[] sample = new float[sampleColor.sampleSize()];
		sampleColor.fetchSample(sample, 0);
		double minscal = Double.MAX_VALUE;
		double scalaire = TestColor.scalaire(sample, blue);
		String color="";

		
		if (scalaire < minscal) {
			minscal = scalaire;
			color="blue";
		}
		
		scalaire = TestColor.scalaire(sample, none);
		
		if (scalaire < minscal) {
			minscal = scalaire;
			color="none";
		}
		
		scalaire = TestColor.scalaire(sample, white);
		
		if (scalaire < minscal) {
			minscal = scalaire;
			color="white";
		}
		scalaire = TestColor.scalaire(sample, red);

		if (scalaire < minscal) {
			minscal = scalaire;
			color="red";
		}
		
		scalaire = TestColor.scalaire(sample, yellow);
		
		if (scalaire < minscal) {
			minscal = scalaire;
			color= "yellow";
		}
		
		scalaire = TestColor.scalaire(sample, green);
		
		if (scalaire < minscal) {
			minscal = scalaire;
			color="green";
		}
		
		scalaire = TestColor.scalaire(sample, black);
		
		if (scalaire < minscal) {
			minscal = scalaire;
			color="black";
		}
		return color;
		
		
		}
	
	


	
	public static double scalaire(float[] v1, float[] v2) {
		return Math.sqrt (Math.pow(v1[0] - v2[0], 2.0) +
				Math.pow(v1[1] - v2[1], 2.0) +
				Math.pow(v1[2] - v2[2], 2.0));
	}

}