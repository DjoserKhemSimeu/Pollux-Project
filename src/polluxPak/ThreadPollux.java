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

	public void run(){
		while(!Button.ENTER.isDown()) {
			switch(courant.getNum()){
			case 1: System.out.println("1");
			pollux.moteurs.avance();



			while (!pollux.detectionPallet()) {

			}

			pollux.moteurs.stop();
			pollux.moteurs.tourner(false, 1);
			Delay.msDelay(500);
			pollux.moteurs.avance();
			while(pollux.getDistance()>0.15) {

			}
			pollux.moteurs.stop();
			pollux.moteurs.tourner(true,1);
			Delay.msDelay(500);
			pollux.moteurs.avance();
			while(pollux.capteurs.getColor()!= "white"){
			}
			pollux.moteurs.stop();
			pollux.moteurs.lacherPallet(2);
			courant = Etat.B1;


			case 2:
				System.out.println("2");
				/*pollux.moteurs.recule();
			Delay.msDelay(500);
			pollux.moteurs.stop();
			pollux.moteurs.tournerR(true,1);
			Delay.msDelay(1000);
				 */pollux.moteurs.avance();
				 while(pollux.getColor()=="none") {
					 System.out.print ("f");
				 }
				 Delay.msDelay(500);
				 pollux.moteurs.stop();
				 pollux.moteurs.tourner(false,1);
				 Delay.msDelay(500);
				 pollux.moteurs.avance();
				 while(!pollux.detectionPallet()) {



				 }
				 pollux.moteurs.stop();

				 pollux.moteurs.tourner(false,1);
				 Delay.msDelay(500);
				 pollux.moteurs.avance();
				 while(pollux.getColor()!="white") {
				 }

				 pollux.moteurs.stop();
				 pollux.moteurs.pince.rotate(6*pollux.moteurs.QuartT);
				 pollux.moteurs.startS();
				 pollux.moteurs.l1.rotate(-200,true);
				 pollux.moteurs.r1.rotate(-200,true);
				 pollux.moteurs.endS();
				 pollux.moteurs.	pince.rotate(-6*pollux.moteurs.QuartT);
				 pollux.moteurs.r1.stop();
				 pollux.moteurs.tournerR(true,1);
				 pollux.moteurs.addAngle(90,true);

				 pollux.moteurs.lacherPallet(1);

				 courant=Etat.BUT;





			case 3:

				System.out.println("3");
				double dist = pollux.getDistance();
				boolean b=true;
				while (b==true) {
					try {
						double d;
						if(dist<0.3) {
							pollux.moteurs.tourner(true,1);
							d=pollux.scan(1,true);

						}else if(dist>0.3 && dist<1.7) {
							d=pollux.scan(2,true);
						}else {
							d=pollux.scan(1,true);
						}
						if(d==-1) {
							b=false;
						}
						Delay.msDelay(5000);
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


