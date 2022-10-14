package polluxPak;




import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;


public class Actionneurs {
	public static final int QuartT =200;
	private static final int DQuartT =200;

	private static final boolean DROITE=true;
	private static final boolean GAUCHE=false;

	RegulatedMotor l1;
	RegulatedMotor r1;
	private RegulatedMotor pince;
	private double angle;
	


	public Actionneurs (Port A,Port B,Port D) {
		l1 = new EV3LargeRegulatedMotor(A);
		r1= new EV3LargeRegulatedMotor(B);
		pince= new EV3LargeRegulatedMotor(D);
		pince.setSpeed(1200);
		//l1.setSpeed(1000);
		//r1.setSpeed(1000);
		l1.synchronizeWith(new RegulatedMotor[] {r1});
		l1.startSynchronization();
		angle=180;
	}
	public Actionneurs() {
		// TODO Auto-generated constructor stub
	}
	public void rotateG(int t) {
		l1.endSynchronization();
		l1.rotate(t,true);
		l1.startSynchronization();
		
	}
	public void rotateR(int t) {
		l1.endSynchronization();
		r1.rotateTo(t,true);
		r1.waitComplete();
		r1.stop();
		l1.startSynchronization();
		
	}
	public  void avance(int t) {
		l1.startSynchronization();
			l1.forward();
			r1.forward();
			l1.endSynchronization();
			Delay.msDelay(t);
			
	}
	public  void avance() {
		l1.startSynchronization();
		l1.forward();
		r1.forward();
		l1.endSynchronization();
		
}
	public void recule() {
		l1.startSynchronization();
		l1.backward();
		r1.backward();
		l1.endSynchronization();
	}
	public void lacherPallet() {
		stop();
		pince.rotate(3*QuartT);
		recule();
		Delay.msDelay(100);
		pince.rotate(-3*QuartT);
		r1.stop();
		tournerR(true,2);
		
	}
	public boolean isMoving() {
		return(l1.isMoving()||r1.isMoving());
	}
	public double getAngle() {
		return angle;
	}
	public void stop() {
		l1.startSynchronization();
		l1.stop();
		r1.stop();

		l1.endSynchronization();
	}
	public void actionPince() {
		pince.rotate(3*QuartT);
		pince.rotate(-3*QuartT);
	}
	public void tourner(boolean dir,double nbQuartT) {
		l1.endSynchronization();
		angle%=360;
		if(dir==DROITE) {
			r1.rotate((int)(-QuartT*nbQuartT),true);
			l1.rotate((int)(QuartT*nbQuartT),true);
			l1.waitComplete();
			l1.stop(true);
			r1.stop(true);
			angle= angle +90*nbQuartT;
		}else if(dir==GAUCHE) {
			l1.rotate((int)(-QuartT*nbQuartT),true);
			r1.rotate((int)(QuartT*nbQuartT),true);
			if(angle==0) {
				angle=360-90*nbQuartT;
			}else {
			angle= angle -90*nbQuartT;
			}
		}
		l1.startSynchronization();
	}
	public void tournerR(boolean dir,int nbQuartT) {
		l1.endSynchronization();
		if(dir==DROITE) {
			r1.stop();
			l1.rotate(-DQuartT*nbQuartT);
			angle= angle +QuartT*nbQuartT;
		}else if(dir==GAUCHE) {
			l1.stop();
			r1.rotate(-DQuartT*nbQuartT);
			if(angle==0) {
				angle=360-90*nbQuartT;
			}else {
			angle= angle -90*nbQuartT;
			}
		}
		l1.startSynchronization();
	}


	public static void main (String[]args) {
		Actionneurs a=new Actionneurs(MotorPort.A,MotorPort.B,MotorPort.D);
		ExecutorService es=Executors.newSingleThreadExecutor();
	
	
			a.tourner(true,360);
			
	
		
		while(a.r1.isMoving()) {
			System.out.println(a.r1.getTachoCount());
		}
		System.out.println("a");
		
		
	

	}
}