package polluxPak;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

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
	public static HashMap<Integer,Double>chercheDis(ArrayList<Double> dist){
		HashMap<Integer,Double> res=new HashMap<Integer,Double>();
		double delta=0.15;
		int i=1;
		double prec=dist.get(0);
		while (i<dist.size()) {
			if(Math.abs(dist.get(i)-prec)>delta) {
				res.put(i*90/dist.size(),dist.get(i));
			}
			prec=dist.get(i);
			i++;
			
		}
		return res;
	}
	public void scanf() {
		ArrayList<Double> dist=new ArrayList<Double>();
		moteurs.l1.endSynchronization();
		moteurs.l1.setSpeed(35);
		moteurs.r1.setSpeed(35);
		moteurs.l1.rotate(Actionneurs.QuartT,true);
		moteurs.r1.rotate(-Actionneurs.QuartT,true);
		while(moteurs.l1.isMoving()) {
			dist.add(getDistance());
			
		}
		moteurs.addAngle(90,true);
		int size=dist.size();
		double delta=0.25;
		ArrayList<Double>disDist=new ArrayList<Double>();
		ArrayList<Integer>disAngle=new ArrayList<Integer>();
		int i=1;
		
		while(i<size) {
			double now=dist.get(i);
			double prec=dist.get(i-1);
			if(Math.abs(now-prec)>delta) {
				int agl=i*90/size;
				disAngle.add(agl);
				disDist.add(now);
				System.out.println("#################");
				System.out.println("idx: "+i+" agl :"+size );
				System.out.println("ecart: "+Math.abs(now-prec));
			}
			i++;
		}
		System.out.println("_________________");
		for(Integer s: disAngle) {
			System.out.print(s+" ");
		}
	/*	ArrayList<Integer> pallet=new ArrayList<Integer>(disAngle.size()/2);
		int compt=0;
		while (compt+1<disAngle.size()) {
			int deb=disAngle.get(compt);
			int fin= disAngle.get(compt+1);
			pallet.add((deb+fin)/2);
			compt++;
		}*/
		for(Integer s: disAngle) {
			System.out.print(s+" ");
		}
		/*System.out.println();
		for(Integer r: pallet) {
			System.out.print(r+" ");
		}*/
		
	
		

		
	}

	public int scan() {

		ArrayList<Double>distances=new ArrayList<Double>();
		moteurs.l1.endSynchronization();
		moteurs.l1.setSpeed(50);
		moteurs.r1.setSpeed(50);

		moteurs.l1.rotate(Actionneurs.QuartT,true);
		moteurs.r1.rotate(-Actionneurs.QuartT,true);



		while(moteurs.isMoving()) {
			

			distances.add(getDistance());

			System.out.println(getDistance());



		}
		moteurs.addAngle(90,true);
		moteurs.l1.startSynchronization();

		double size=distances.size()-30;

		double delta=0.05;
		ArrayList<Double> distDis=new ArrayList<Double>();
		ArrayList <Integer>angleDis=new ArrayList<Integer>();
		Iterator <Double>it=distances.iterator();
		int i=1;

		double prec= distances.get(0);
		while(i<size) {
			double now=distances.get(i);



			if(Math.abs(prec-now)>delta) {
				System.out.println(size);
				System.out.println("###########################");
				int goal=(int)(i*90.0/size);
				System.out.println(i);
				distDis.add(now);
				angleDis.add(goal);
			}
			prec=now;
			i++;
		}

		for(Integer in: angleDis) {
			System.out.print(in+" ");
		}



		if(distDis.size()>0) {
			double min= Double.MAX_VALUE;
			int idx=0;
			for(Double d: distDis ) {
				if(d<min) {
					min=d;
					idx=distDis.indexOf(d);
				}




			}
			double angle= angleDis.get(idx);
			int diff=(int)(moteurs.getAngle()-angle);
			/*moteurs.l1.backward();
			moteurs.r1.forward();
			int rep=distances.size()-1;
			while(moteurs.l1.isMoving()) {
				if (rep==angle) {
					moteurs.l1.stop();
					moteurs.r1.stop();
				}
			}*/
			return diff;
		}


		return -1;
		//ArrayList<Double> discontinuitées	=new ArrayList<Double>();



	}
	public void chercheAngle() {
		int diff=scan();
		if(diff>=0) {
			int i=0;
			while(i<diff) {
				moteurs.tournerScan(false);
				i++;
			}
			Delay.msDelay(5000);
		}
	}
	public void action() {

		while(!Button.ENTER.isDown()) {
			colorT=capteurs.getColor();




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
		robot.scanf();
		while(!Button.ENTER.isDown()) {

		}






	}

}

