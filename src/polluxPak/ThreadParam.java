package polluxPak;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;


public class ThreadParam extends Thread {
	Agents pollux;

	public ThreadParam() {
		// TODO Auto-generated constructor stub
	}
	public ThreadParam(Agents pollux) {
		this.pollux=pollux;
	}
	public void run() {
		while(!Button.ENTER.isDown()) {
			String colorT=pollux.majColor();
			
			LCD.clear(3);
			LCD.clear(4);
			LCD.clear(5);
			LCD.clear(6);
			LCD.drawString("Distance : "+pollux.getDistance(), 0,3);
			LCD.drawString("couleur : "+colorT, 0,4);
			LCD.drawString("case : "+pollux.MajPos(), 0,5);
			LCD.drawString("angle : "+pollux.getAngle(), 0,6);
	

		}
	}
	

}
