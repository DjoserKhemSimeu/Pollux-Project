package polluxPak;

import java.io.IOException;

import lejos.hardware.Button;
import lejos.utility.Delay;

public class ThreadPollux {
	private Agents pollux;
	private Etat courant;

	public ThreadPollux() {
		// TODO Auto-generated constructor stub
	}
	public ThreadPollux( Agents a){
		pollux = a; 
		courant = Etat.P0;
		pollux.moteurs.pince.setSpeed(2000);
		pollux.moteurs.speed(550);
	}
	
	/*
	 * Methode run qui vas représenter le diagramme d'état de pollux
	 */

	public void run(){
		while(!Button.ENTER.isDown()) {
			
		
			 // representation du diagramme par un switch case
			switch(courant.getNum()){
	
			
			// Etat initiale P0
			case 1: System.out.println("1");
			/*
			 * Il avance jsqua dtecter un pallet, tourner vers la gauche 
			 * jusqua etre a 15 cm du mur, tourner a droite et avancer
			 * jusqua la ligne blanche et se retourner
			 */
			
			pollux.moteurs.avance();
			while (!pollux.detectionPallet()) {

			}

			pollux.moteurs.stop();
			pollux.moteurs.tourner(false, 1);
			Delay.msDelay(500);
			pollux.moteurs.avance();
			while(pollux.getDistance()>0.2) {

			}
			pollux.moteurs.stop();
			pollux.moteurs.tourner(true,1);
			Delay.msDelay(500);
			pollux.moteurs.avance();
			while(pollux.capteurs.getColor()!= "white"){
			}
			pollux.moteurs.stop();
			pollux.moteurs.lacherPallet(1);
			pollux.moteurs.avance();
			while(pollux.getColor()=="none") {
				
			}
			Delay.msDelay(300);
			pollux.moteurs.stop();
			pollux.moteurs.tourner(true,1);
			Delay.msDelay(2000);
			courant = Etat.B1;
			

			
			

			// Etat Premier but mis B1
			case 2:
				
				/* Pollux vas avancer en detectant les pallet jusqua atteindre la premiere ligne de 
				 *couleur pour tourner a gauche et attraper le premier pallet qui'il detecte 
				 *pour le deposer dans l'enbut et se tourner a 90°
				 */
				System.out.println("2");
				 int goal=0;
				 while(pollux.getColor()!="blue"){
					if(pollux.detectionPallet()) {
						goal++;
						pollux.moteurs.tourner(true,2);
					 }
					Delay.msDelay(500);
				 }
				 if(goal==0) {
					 Delay.msDelay(300);
					 pollux.moteurs.stop();
					 pollux.moteurs.tourner(false,1);
					 Delay.msDelay(500);
					 pollux.moteurs.avance();
					
					 pollux.moteurs.stop();

					 pollux.moteurs.tourner(false,1);
				 }
				 Delay.msDelay(500);
				 pollux.moteurs.avance();
				 while(pollux.capteurs.getColor()!="white") {
				 }
				 pollux.moteurs.stop();
				 pollux.moteurs.lacherPallet(1);

				 courant=Etat.BUT;




				 

				 //Etat but ou pollux vas utiliser le scan
			case 3:
				/*
				 * pollux vas capter la distance du mur en face de lui ainsi il saura
				 * ou il se trouve , en fonction de sa position un scan est lancer:
				 * si il est a droite du terrain il vas lancer un scan de 90 vers la droite
				 * si il est a gauche du terrain il vas d'abord faire un 90° a droite pour ensuite faire 
				 * un sacn de 90° vers la droite
				 * si il est au milieux il fait un scan de 180° vers la droite
				 */

				System.out.println("3");
				double dist = pollux.getDistance();
				boolean b=true;
				while (b==true) {
					try {
						double d;
						pollux.scan(2,true);
						while(pollux.moteurs.isMoving());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				courant=Etat.VERIFICATION;





			case 4:

				courant=Etat.VERIFICATION;



			case 5:
				System.out.println("5");

				//pollux.moteurs.tourner(false,((double)pollux.moteurs.getAngle()-180)/360);
				pollux.moteurs.avance();
				while (pollux.getDistance()>1.5) {

				}
				pollux.moteurs.stop();
				courant=Etat.VERIFICATION;



			case 6:
				System.out.println("6");
				try {
					pollux.scan(4,true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			case 7:
				System.out.println("7");
				int but=0;
				pollux.moteurs.tournerR(true,2);
				pollux.moteurs.avance();
				while(pollux.getColor()=="none") {
				}
				pollux.moteurs.stop();
				int i=pollux.getEspace().getIJ()[0];
				switch(i%3) {
				case 0:
					pollux.moteurs.tourner(true,1);

				case 1:
					pollux.moteurs.tourner(false,1);
				case 2:
					pollux.moteurs.tourner(false,1);
				}
				pollux.moteurs.avance();
				while(pollux.getDistance()>0.2 ) {
					if( pollux.detectionPallet()) {
						but++;
						pollux.moteurs.stop();
						switch(i%3) {
						case 0:
							pollux.moteurs.tourner(true,1);

						case 1:
							pollux.moteurs.tourner(false,1);
						case 2:
							pollux.moteurs.tourner(false,1);
						}
						pollux.moteurs.avance();
						while(pollux.getColor()!="white") {

						}
						pollux.moteurs.stop();
						break;
					}

				}
				if(but>0) {
					break;
				}
				pollux.moteurs.stop();
				switch(i%3) {
				case 0:
					pollux.moteurs.tourner(false,1);

				case 1:
					pollux.moteurs.tourner(true,1);
				case 2:
					pollux.moteurs.tourner(true,1);
				}

				pollux.moteurs.avance();

				while(pollux.getDistance()>1.5) {

				}
				pollux.moteurs.stop();
				switch(i%3) {
				case 0:
					pollux.moteurs.tourner(false,1);

				case 1:
					pollux.moteurs.tourner(true,1);
				case 2:
					pollux.moteurs.tourner(true,1);
				}
				pollux.moteurs.l1.startSynchronization();
				pollux.moteurs.l1.forward();
				pollux.moteurs.r1.forward();

				while(pollux.getDistance()>0.2 ) {
					if( pollux.detectionPallet()) {
						but++;
						pollux.moteurs.r1.stop();
						pollux.moteurs.l1.stop();
						pollux.moteurs.l1.endSynchronization();
						switch(i%3) {
						case 0:
							pollux.moteurs.tourner(true,1);

						case 1:
							pollux.moteurs.tourner(false,1);
						case 2:
							pollux.moteurs.tourner(false,1);
						}
						pollux.moteurs.l1.startSynchronization();
						pollux.moteurs.l1.forward();
						pollux.moteurs.r1.forward();
						while(pollux.getColor()!="white") {

						}
						pollux.moteurs.r1.stop();
						pollux.moteurs.l1.stop();
						pollux.moteurs.l1.endSynchronization();
						break;
					}

				}
				if(but>0) {
					break;
				}
				pollux.moteurs.r1.stop();
				pollux.moteurs.l1.stop();

				pollux.moteurs.l1.endSynchronization();
				switch(i%3) {
				case 0:
					pollux.moteurs.tourner(true,1);

				case 1:
					pollux.moteurs.tourner(false,1);
				case 2:
					pollux.moteurs.tourner(false,1);
				}

				pollux.moteurs.l1.startSynchronization();
				pollux.moteurs.l1.forward();
				pollux.moteurs.r1.forward();
				pollux.moteurs.l1.startSynchronization();
				pollux.moteurs.l1.forward();
				pollux.moteurs.r1.forward();

				while(pollux.getDistance()>1.5) {

				}
				pollux.moteurs.r1.stop();
				pollux.moteurs.l1.stop();

				pollux.moteurs.l1.endSynchronization();
				switch(i%3) {
				case 0:
					pollux.moteurs.tourner(true,1);

				case 1:
					pollux.moteurs.tourner(false,1);
				case 2:
					pollux.moteurs.tourner(false,1);
				}
				pollux.moteurs.l1.startSynchronization();
				pollux.moteurs.l1.forward();
				pollux.moteurs.r1.forward();

				while(pollux.getDistance()>0.2 ) {
					if( pollux.detectionPallet()) {
						but++;
						pollux.moteurs.r1.stop();
						pollux.moteurs.l1.stop();
						pollux.moteurs.l1.endSynchronization();
						switch(i%3) {
						case 0:
							pollux.moteurs.tourner(true,1);

						case 1:
							pollux.moteurs.tourner(false,1);
						case 2:
							pollux.moteurs.tourner(false,1);
						}
						pollux.moteurs.l1.startSynchronization();
						pollux.moteurs.l1.forward();
						pollux.moteurs.r1.forward();
						while(pollux.getColor()!="white") {

						}
						pollux.moteurs.r1.stop();
						pollux.moteurs.l1.stop();
						pollux.moteurs.l1.endSynchronization();
						break;
					}

				}
				if(but>0) {
					break;
				}
				pollux.moteurs.r1.stop();
				pollux.moteurs.l1.stop();

				pollux.moteurs.l1.endSynchronization();
				courant=Etat.FINPARCOUR;


			case 8:


			case 9:
			}
		}
	}
}


