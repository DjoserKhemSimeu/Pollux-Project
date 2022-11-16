package polluxPak;

public class ThreadPollux extends Thread{
	private Agents pollux;
	private Etat courant;

	public ThreadPollux() {
		// TODO Auto-generated constructor stub
	}
	public ThreadPollux( Agents a){
		pollux = a; 
		courant = Etat.P0;
	}

	public void run(){
		switch(courant.getNum()){
		case 1: 
			pollux.moteurs.avance();
			while (pollux.getDistance()>0.1) {
			if(pollux.detectionPallet()) {
				pollux.moteurs.stop();
			pollux.moteurs.tourner(false, 1);
			pollux.moteurs.avance();
			}
			}
			pollux.moteurs.stop();
			pollux.moteurs.tourner(true,1);
			pollux.moteurs.avance();
			while(pollux.capteurs.getColor()!= "white"){
			}
			pollux.moteurs.stop();
			pollux.moteurs.lacherPallet();
			courant = Etat.B1;


		case 2:
			pollux.moteurs.recule(100);
			pollux.moteurs.tournerR(true,2);
			pollux.moteurs.avance();
			while(pollux.getColor()=="none") {
			}
			pollux.moteurs.stop();
			pollux.moteurs.tourner(false,1);
			pollux.moteurs.avance();
			while(pollux.getDistance()>0.2) {
				if(pollux.detectionPallet()) {
					pollux.moteurs.actionPince();
					break;
				}
				
			}
			pollux.moteurs.stop();
			
			pollux.moteurs.tourner(false,1);
			pollux.moteurs.avance();
			while(pollux.getColor()!="white") {
			}
			pollux.moteurs.stop();
			pollux.moteurs.actionPince();
			
			courant=Etat.BUT;
			
			



		case 3:
			pollux.moteurs.recule(100);
			pollux.moteurs.tournerR(true,2);
			
			if(pollux.scan()==-1) {
				courant=Etat.PALET0;
			}else {
				courant=Etat.PALET_A;
			}
			
			

		case 4:
			pollux.moteurs.avance();
			int but4=0;
			while(pollux.getDistance()>0.15)
			{
				if(pollux.detectionPallet()) {
					
					but4++;
					pollux.moteurs.stop();
					pollux.moteurs.tourner(false,(360-(double)pollux.moteurs.getAngle())/360);
					pollux.moteurs.avance();
					while(pollux.getColor()!="white") {
						
					}
					pollux.moteurs.stop();
					break;
				}
				
			}
			if(but4>0) {
				pollux.moteurs.lacherPallet();
				courant=Etat.BUT;
				break;
			}
			pollux.moteurs.tourner(false,(360-(double)pollux.moteurs.getAngle())/360);
			pollux.moteurs.avance();
			while(pollux.getColor()!="white") {
				
			}
			pollux.moteurs.stop();
			courant=Etat.VERIFICATION;
			
			

		case 5:
			pollux.moteurs.tourner(false,((double)pollux.moteurs.getAngle()-180)/360);
			pollux.moteurs.avance();
			while (pollux.getDistance()>1.5) {
				
			}
			pollux.moteurs.stop();
			courant=Etat.VERIFICATION;



		case 6:
			pollux.scanf(360);

		case 7:
			
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


