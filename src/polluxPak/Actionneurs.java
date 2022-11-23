package polluxPak;




import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;


public class Actionneurs {
	public static final int QuartT =200;
	private static final int DQuartT =397;

	//attributs de direction
	private static final boolean DROITE=true;
	private static final boolean GAUCHE=false;

	RegulatedMotor l1; // roue gauche
	RegulatedMotor r1; // roue droite
	RegulatedMotor pince; // pince
	private double angle; // direction
	private boolean cote;
	public static boolean SOUTH=true;
	public static boolean NORTH=false;
	

// ce constructeur permet d'initialiser nos attributs d'instance, notamment d'expliciter le lien entre les ports, les attributs et méthodes associées à ceux-ci.
	public Actionneurs (Port A,Port B,Port D,boolean cote) {
		l1 = new EV3LargeRegulatedMotor(A);
		r1= new EV3LargeRegulatedMotor(B);
		pince= new EV3LargeRegulatedMotor(D);
		pince.setSpeed(3000);
		speed(650);
		l1.synchronizeWith(new RegulatedMotor[] {r1});
		l1.startSynchronization();
		angle=0;
		this.cote=cote;
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
	public void lacherPallet(int d) {
		stop();
		pince.rotate(6*QuartT);
		startS();
		l1.rotate(-100,true);
		r1.rotate(-100,true);
		endS();
		pince.rotate(-6*QuartT);
		r1.stop();
		tournerR(false,d);
		addAngle(d*90,false);
	}
	
	// return true si un des moteurs(roue) est en mouvement
	public boolean isMoving() {
		return(l1.isMoving()||r1.isMoving());
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void tournerTo(double différence) {
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
		startS();
		l1.stop();
		r1.stop();
		endS();
	

	}
	public void actionPince() {
		pince.setSpeed(1400);
		pince.rotate(6*QuartT);
		pince.rotate(-6*QuartT);
	}
	public void addAngle(int deg,boolean dir) {

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
		angle%=360;
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
			addAngle(nbQuartT*90,DROITE);

		}else if(dir==GAUCHE) {
			l1.stop();
			r1.rotate(-DQuartT*nbQuartT);
			addAngle(nbQuartT*90,GAUCHE);
			
		}
		l1.startSynchronization();
	}
	public void endS() {
		l1.endSynchronization();
	}
	public void startS() {
		l1.startSynchronization();
	}
	public void speed(int s) {
		l1.setSpeed(s);
		r1.setSpeed(s);
	}
	public void recule(int d) {
		startS();
		l1.rotate(-d);
		r1.rotate(-d);
		endS();
	}
	public void flt() {
		startS();
		l1.flt();
		r1.flt();
		endS();
	}
	


	public static void main (String[]args) {
		Actionneurs a=new Actionneurs(MotorPort.A,MotorPort.B,MotorPort.D,true);
		//a.tournerTo(90);
		a.pince.rotate(-720);
		System.out.print(a.getAngle());
		Delay.msDelay(4000);
		
	
		
		
		
	
		
	

	}
}
