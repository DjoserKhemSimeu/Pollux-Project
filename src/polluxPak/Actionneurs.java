package polluxPak;





import lejos.hardware.motor.EV3LargeRegulatedMotor;

import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;



public class Actionneur {
	/*
	 * La classe Actionneur contient l'ensemble de méthode qui permettent d'utiliser les moteurs:
	 * pince, roue gauche et roue droite. 
	 */


	//attributs d'instance définissant la degre de de rotation des roues pour obtenir un angle de 90°

	//en avant
	public  static final int QuartT =190;

	//en arriere
	public static final int DQuartT =380;

	//attributs d'instance de direction
	private static final boolean DROITE=true;
	private static final boolean GAUCHE=false;

	RegulatedMotor l1; // roue gauche
	RegulatedMotor r1; // roue droite
	RegulatedMotor pince; // pince
	private double angle; // direction

	// attributs d'instance définissant le sud et le nord du terrain
	public static boolean SOUTH=true;
	public static boolean NORTH=false;


	// ce constructeur permet d'initialiser nos attributs d'instance, notamment 
	//d'expliciter le lien entre les ports, les attributs et méthodes associées à ceux-ci.
	public Actionneur (Port A,Port B,Port D,boolean cote) {

		// initialisation des des moteurs avec les port respectifs du robot(ABD)
		l1 = new EV3LargeRegulatedMotor(A);
		r1= new EV3LargeRegulatedMotor(B);
		pince= new EV3LargeRegulatedMotor(D);



		//intialisation de la vitessse des roues a 650 via la method speed
		speed(650);

		// synchronization des roues via la methode synchronizeWith de la classe EV3LargeRegulatedMotor
		l1.synchronizeWith(new RegulatedMotor[] {r1});
		l1.startSynchronization();

		// definition de l'angle initiale a 0°
		angle=0;

	}
	public Actionneur() {
		// TODO Auto-generated constructor stub
	}






	// méthode qui synchronise les deux roues afin qu'elles avancent au même rythme, 
	//sans limite de temps qui s'arrete lorsque la methode stop est appelée
	public  void avance() {
		l1.startSynchronization();
		l1.forward();
		r1.forward();
		l1.endSynchronization();

	}
	// méthode qui synchronise les deux roues afin qu'elles reculent au même rythme,
	//sans limite de temps qui s'arrete lorsque la methode stop est appelé.
	public void recule() {
		l1.startSynchronization();
		l1.backward();
		r1.backward();
		l1.endSynchronization();
	}

	// méthode qui arrête les deux roues puis ouvre les pinces et recule
	//afin que Pollux se libère du palet qu'il a dans ses pinces 
	// l'entier d représente le nombre de Quart de tour a effectuer après
	// avoir lacher le palet, l'angle est en suite ajouté a l'attribut d'instance via la methode
	//addAngle
	public void lacherPalet(int d) {
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

	// return true si une des roue est en mouvement
	public boolean isMoving() {
		return(l1.isMoving()||r1.isMoving());
	}

	// methode qui retourne angle actuel de pollux
	public double getAngle() {
		return angle;
	}


	// methode qui arrete les deux roues
	public void stop() {
		startS();
		l1.stop();
		r1.stop();
		endS();


	}
	// methode qui ouvre les pinces
	public void ouvrirPince() {
		pince.setSpeed(1400);
		pince.rotate(6*QuartT);
	}


	//methode qui ferme les pinces
	public void fermerPince(){
		pince.setSpeed(1400);
		pince.rotate(-6*QuartT);
	}

	//methode qui incrémente de deg l'attribut angle en fonction de dir (DROITE ou GAUCHE)
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

	//methode qui fait tourner pollux d'un nombre de quart de tour donné
	//en fonction d'une direction (DROITE ou GAUCHE)
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

	//methode qui fait tourner pollux en arriere d'un nombre de quart de tour donné
	//et en fonction d'une direction donnée
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
	// methode qui désynchronize les roues
	public void endS() {
		l1.endSynchronization();
	}
	// methode qui synchronize les roues
	public void startS() {
		l1.startSynchronization();
	}


	//methode qui set la vitesse des roues
	public void speed(int s) {
		l1.setSpeed(s);
		r1.setSpeed(s);
	}











}
