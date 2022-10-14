package polluxPak;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


public class Agents {
	private Capteurs capteurs;
	private Actionneurs moteurs;
	public String colorT;
	public Espace e;

	public Agents() {
		// TODO Auto-generated constructor stub
	}
	public Agents(Port A, Port B, Port D, Port s1, Port s3,Port s4,int x,int y) throws IOException {
		capteurs= new Capteurs (s1,s3,s4);
		moteurs= new Actionneurs (A,B,D);
		e= new Espace(x,y,this);
		colorT="none";
		
	}
	public double getAngle(){
		return moteurs.getAngle();
	}
	public double getDistance() {
		return capteurs.getDistance();
	}
	public boolean detectionPallet() {
		//moteurs.avance();
		if(capteurs.getDistance()<0.4) {
			double dist=capteurs.getDistance();
			Delay.msDelay(300);
			if(capteurs.getDistance()>dist)
			{
				moteurs.actionPince();
				return true;
			}else {
				while(capteurs.getDistance()<0.2) {
					moteurs.tourner(true,1);
			}
				return false;

			}

	
		}
		return false;
	}

	public boolean tournerLigne(String c, int i) {
		if(capteurs.getColor().equals(c)) {
			return false;
		}else {
			if(i%2==1) {
				moteurs.tourner(true,1);
			}else {
				moteurs.tourner(false,1);
			}
			return true;
		}
		
	}
	public boolean passeLigne() {

		if(passeLigneR()==null) {
			return false;
		}else {
			if(passeLigneR().equals("black")) {
				return false;
			}
			return true;
		}
	}
	public String passeLigneR() {
		if(capteurs.getColor().equals(colorT)) {
			return null;
		}else {
			return capteurs.getColor();
		}
	}
	public String getColor() {
		return capteurs.getColor();
	}
	public void majColor() {
	}
	public int scan() {
		
		ArrayList<Double>distances=new ArrayList<Double>();
		moteurs.l1.rotate(360,true);
		while(moteurs.l1.isMoving()) {
			
			distances.add(getDistance());
			System.out.println(getDistance());
		
		}
		System.out.println(distances.size());
		
		return distances.size();
		//ArrayList<Double> discontinuitées	=new ArrayList<Double>();
		
		
		
		}
	public void action() {
		boolean b=true;
		int s;
		//e.getNumCase();
		while(!Button.ENTER.isDown()) {
			colorT=capteurs.getColor();
			if (b) {
			 s=scan();
			b=false;
			}
			
			
		
			LCD.clear(3);
			LCD.clear(4);
			LCD.clear(5);
			LCD.clear(6);
			LCD.drawString("Distance : "+capteurs.getDistance(), 0,3);
			LCD.drawString("couleur : "+colorT, 0,4);
			LCD.drawString("case : ", 0,5);
			LCD.drawString("angle : "+getAngle(), 0,6);
			
			
			
			
			//capteurs.maj();
		//moteurs.avance();
			Delay.msDelay(100);
			///e.changementEsp();
			//detectionPallet();
			

		}

		capteurs.close();
		moteurs.stop();
		moteurs.l1.close();
		moteurs.r1.close();
		
	}
	public static void main (String[]args) throws IOException {
		Agents robot= new Agents (MotorPort.A,MotorPort.B,MotorPort.D,SensorPort.S1,SensorPort.S3,SensorPort.S4,0,1);
		robot.scan();
		Delay.msDelay(10000);
		
		
		
		
		
		
	}

}

