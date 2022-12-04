package polluxPak;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

public class Competition {

	public Competition() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[]args) throws IOException {
		Agents robot= new Agents (MotorPort.A,MotorPort.B,MotorPort.D,SensorPort.S1,SensorPort.S3,SensorPort.S4,0,1);
		
		ThreadPollux pollux=new ThreadPollux(robot);
		
	while(true) {
		if(Button.ENTER.isDown()) {

		pollux.run();
		}
	}
			
	}

}
