package polluxPak;

public class Thread {
	private Agents pollux;
	private Etat courant;

	public Thread() {
		// TODO Auto-generated constructor stub
	}
	public Thread( Agents a){
		pollux = a; 
		courant = Etat.P0;
	}

	public void run(){
		switch(courant.getNum()){
		case 1: 
			pollux.moteurs.avance();
			if(pollux.capteurs.getTouch())
				pollux.moteurs.actionPince();
			pollux.moteurs.tourner(false, 2);
			while(pollux.capteurs.getDistance()>0.2){
				pollux.moteurs.avance();
			}
			pollux.moteurs.tourner(true, 2);
			while(pollux.capteurs.getColor()!= "white"){
				pollux.moteurs.avance();
			}
			pollux.moteurs.lacherPallet();
			courant = Etat.B1;


		case 2:
			pollux.getEspace().getZone();
			int idx=pollux.getEspace().getIJ()[0];
			pollux.moteurs.tournerR(true,180);
			pollux.moteurs.l1.startSynchronization();
			while(pollux.getColor()=="none") {
				pollux.moteurs.l1.forward();
				pollux.moteurs.r1.forward();
			}
			pollux.moteurs.r1.stop();
			pollux.moteurs.l1.stop();
			pollux.moteurs.l1.endSynchronization();
			pollux.moteurs.tourner(false,1);
			pollux.moteurs.l1.startSynchronization();
			pollux.moteurs.l1.forward();
			pollux.moteurs.r1.forward();
			while(pollux.getDistance()>0.2) {
				if(pollux.detectionPallet()) {
					pollux.moteurs.actionPince();
					break;
				}
				
			}
			pollux.moteurs.r1.stop();
			pollux.moteurs.l1.stop();
			
			pollux.moteurs.l1.endSynchronization();
			
			pollux.moteurs.tourner(false,1);
			pollux.moteurs.l1.startSynchronization();
			pollux.moteurs.l1.forward();
			pollux.moteurs.r1.forward();
			while(pollux.getColor()!="white") {
			}
			pollux.moteurs.r1.stop();
			pollux.moteurs.l1.stop();
			pollux.moteurs.l1.endSynchronization();
			pollux.moteurs.actionPince();
			
			courant=Etat.BUT;
			
			



		case 3:
			
			if(pollux.scan()==-1) {
				courant=Etat.PALET0;
			}else {
				courant=Etat.PALET_A;
			}
			
			

		case 4:
			
			

		case 5:



		case 6:
			pollux.scanf(360);

		case 7:
			
			int but=0;
			pollux.moteurs.tournerR(true,180);
			pollux.moteurs.l1.startSynchronization();
			while(pollux.getColor()=="none") {
				pollux.moteurs.l1.forward();
				pollux.moteurs.r1.forward();
			}
			pollux.moteurs.r1.stop();
			pollux.moteurs.l1.stop();
			pollux.moteurs.l1.endSynchronization();
			int i=pollux.getEspace().getIJ()[0];
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
			
			while(pollux.getDistance()>1.5) {
				
			}
			pollux.moteurs.r1.stop();
			pollux.moteurs.l1.stop();
			
			pollux.moteurs.l1.endSynchronization();
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


