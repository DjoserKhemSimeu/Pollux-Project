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
	
// attributs de direction
	private static final boolean DROITE=true;
	private static final boolean GAUCHE=false;
	
	RegulatedMotor l1; // roue gauche
	RegulatedMotor r1; // roue droite
	private RegulatedMotor pince; // pince
	private double angle; // direction
	

// ce constructeur permet d'initialiser nos attributs d'instance, notamment d'expliciter le lien entre les ports, les attributs et méthodes associées à ceux-ci.
	public Actionneurs (Port A,Port B,Port D) {
		l1 = new EV3LargeRegulatedMotor(A);
		r1= new EV3LargeRegulatedMotor(B);
		pince= new EV3LargeRegulatedMotor(D);
		pince.setSpeed(1200);
		//l1.setSpeed(1000);
		//r1.setSpeed(1000);
		l1.synchronizeWith(new RegulatedMotor[] {r1});
		l1.startSynchronization();
		angle=0;
	}
	public Actionneurs() {
		// TODO Auto-generated constructor stub
	}
	// méthode faisant tourner le moteur vers la gauche jusqu'à l'angle limite de valeur t ;
	public void rotateG(int t) {
		l1.endSynchronization();
		l1.rotate(t,true);
		l1.startSynchronization();
		
	}
	
	// méthode faisant tourner le moteur vers la droite jusqu'à l'angle limite de valeur t ;
	public void rotateR(int t) {
		l1.endSynchronization();
		r1.rotateTo(t,true); 
		r1.waitComplete();
		r1.stop();
		l1.startSynchronization();
		
	}
	
	// méthode qui synchronise les deux roues afin qu'elles avancent au même rythme, le temps t passé en paramètre;
	public  void avance(int t) {
		l1.startSynchronization();
			l1.forward();
			r1.forward();
			l1.endSynchronization();
			Delay.msDelay(t);
			
	}
	
	// méthode qui synchronise les deux roues afin qu'elles avancent au même rythme, sans limite de temps;
	public  void avance() {
		l1.startSynchronization();
		l1.forward();
		r1.forward();
		l1.endSynchronization();		
}
	// méthode qui synchronise les deux roues afin qu'elles reculent au même rythme, sans limite de temps;
	public void recule() {
		l1.startSynchronization();
		l1.backward();
		r1.backward();
		l1.endSynchronization();
	}
	
	// méthode qui arrête les deux roues puis ouvre les pinces afin que Pollux se libère du palet qu'il a dans ses pinces
	public void lacherPallet() {
		stop();
		pince.rotate(3*QuartT);
		recule();
		Delay.msDelay(100);
		pince.rotate(-3*QuartT);
		r1.stop();
		tournerR(true,2);
		
	}
	// return true si un des moteurs(roue) est en mouvement
	public boolean isMoving() {
		return(l1.isMoving()||r1.isMoving());
	}
	
	
	public double getAngle() {
		return angle;
	}
	
	public void tournerTo(int différence) {
		l1.endSynchronization();
		int i=0;
		if(différence>0) {
			while(différence<i) {
				tournerScan(true);
				i--;
				
			}
		}else {
			while(différence>i) {
				tournerScan(false);
				i++;
			}
		}
		l1.startSynchronization();
	}
	
	public void tournerScan(boolean dir) {
		l1.endSynchronization();
		if(dir==DROITE) {
			r1.rotate(-3,true);
			l1.rotate(3, true);
			addAngle(1,DROITE);
		}else if(dir==GAUCHE) {
			l1.rotate(-3, true);
			r1.rotate(3, true);
			addAngle(1,GAUCHE);
		}
		l1.startSynchronization();
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
	
	public void addAngle(int deg,boolean dir) {
		angle%=360;
		if(dir==DROITE) {
			angle= angle +deg;
		}else {
			if(angle==0) {
				angle=360-deg;
			}else if(angle<deg) {
				angle=360-(deg-angle);
			}
			else {
			angle= angle -deg;
			}
		}
	}
	
	public void tourner(boolean dir,double nbQuartT) {
		l1.endSynchronization();
		
		if(dir==DROITE) {
			r1.rotate((int)(-QuartT*nbQuartT),true);
			l1.rotate((int)(QuartT*nbQuartT),true);
			addAngle((int)(90*nbQuartT),DROITE);
		}else if(dir==GAUCHE) {
			l1.rotate((int)(-QuartT*nbQuartT),true);
			r1.rotate((int)(QuartT*nbQuartT),true);
			addAngle((int)(90*nbQuartT),GAUCHE);
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
		//a.tournerTo(90);
		//Delay.msDelay(10000);
		int i=0;
		while (i<90) {
			a.tournerScan(false);
			i++;
		}
		Delay.msDelay(5000);
		
	
		
	

	}
}
