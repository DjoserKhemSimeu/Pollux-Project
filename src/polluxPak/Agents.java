package polluxPak;



import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


public class Agents {
	Capteurs capteurs;
	Actionneurs moteurs;
	public String colorT;
	public Espace e;

	public Agents() {
		// TODO Auto-generated constructor stub
	}
	public Agents(Port A, Port B, Port D, Port s1, Port s3,Port s4,int x,int y) throws IOException {
		capteurs= new Capteurs (s1,s3,s4);
		moteurs= new Actionneurs (A,B,D, true);
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
	public String majColor() {
		colorT=getColor();
		return colorT;
		
	}
	public static HashMap<Integer,Double>chercheDis(ArrayList<Double> dist){
		HashMap<Integer,Double> res=new HashMap<Integer,Double>();
		double delta=0.25;
		int i=1;
		double prec=dist.get(0);
		while (i<dist.size()) {
			if(Math.abs(dist.get(i)-prec)>delta) {
				res.put(i*90/dist.size(),Math.abs(dist.get(i)-prec));
			}
			prec=dist.get(i);
			i++;

		}
		return res;
	}

	public double tabMoy(double[]d) {
		double sum=0;
		for(int i=0;i<d.length;i++) {
			sum+=d[i];
		}
		return sum/d.length;
	}

	private ArrayList<Double> tourScan(int deg, boolean dir){
		ArrayList<Double>distances=new ArrayList<Double>();
		moteurs.l1.endSynchronization();
		moteurs.l1.setSpeed(30);
		moteurs.r1.setSpeed(30);

		moteurs.l1.rotate((deg/360)*Actionneurs.QuartT,true);
		moteurs.r1.rotate(-(deg/360)*Actionneurs.QuartT,true);



		while(moteurs.isMoving()) {
			double []moy=new double[10];
			for(int i=0;i<moy.length;i++) {
				double f=getDistance();
				if(f!=Double.POSITIVE_INFINITY) {
					moy[i]= f;

				}else {
					moy[i]=2.5;
				}
			}
			double m=tabMoy(moy);
			int cpt=0;
			ArrayList<Double> rep=new ArrayList<Double>();
			while (cpt<moy.length) {
				if(Math.abs(moy[cpt]-m)<0.1) {
					rep.add(moy[cpt]);
				}
				cpt++;
			}
			double[] cast=new double[rep.size()];
			int i=0;
			for (Double d: rep) {
				cast[i]=d;
				i++;
			}



			System.out.println(tabMoy(cast));

			distances.add(tabMoy(cast));




		}
		moteurs.addAngle(deg,true);
		moteurs.l1.startSynchronization();
		return distances;

	}
	public double moyArrays(ArrayList<Double>t) {
		double res=0;
		for(Double d:t) {
			res+=d;
		}
		return res/t.size();
	}
	public double moy2Arrays(ArrayList<Double>t) {
		double res=0;
		for(Double d:t) {
			res+=d*d;
		}
		return res/t.size();
	}
	public double ecartType(ArrayList<Double>t) {
		double res=0;
		double m=moyArrays(t);
		for(Double d:t) {
			double r=d-m;
			res+=Math.pow(r,2);
		}
		return Math.sqrt(res/t.size());
	}
	public ArrayList<Double> distDiff(){
		ArrayList<Double>distances=new ArrayList<Double>();
		moteurs.l1.endSynchronization();
		moteurs.l1.setSpeed(30);
		moteurs.r1.setSpeed(30);

		moteurs.l1.rotate(Actionneurs.QuartT,true);
		moteurs.r1.rotate(-Actionneurs.QuartT,true);



		while(moteurs.isMoving()) {
			double []moy=new double[10];
			for(int id=0;id<moy.length;id++) {
				double f=getDistance();
				if(f!=Double.POSITIVE_INFINITY) {
					moy[id]= f;

				}else {
					moy[id]=2.5;
				}
				double m=tabMoy(moy);
				int cpt=0;
				ArrayList<Double> rep=new ArrayList<Double>();
				while (cpt<moy.length) {
					if(Math.abs(moy[cpt]-m)<0.1) {
						rep.add(moy[cpt]);
					}
					cpt++;
				}
				double[] cast=new double[rep.size()];
				int i=0;
				for (Double d: rep) {
					cast[i]=d;
					i++;
				}



				System.out.println(tabMoy(cast));

				distances.add(tabMoy(cast));


			}

		}
		ArrayList<Double>res=new ArrayList<Double>();
		for(int i=0;i+1<distances.size();i++) {
			res.add(Math.abs(distances.get(i)-distances.get(i+1)));
		}


		return res;


	}
	public Map<Integer,Double> cleanDis(Map<Integer,Double> dis){
		ArrayList<Integer> agl=new ArrayList<Integer>();
		ArrayList<Integer> agl2=new ArrayList<Integer>();
		for(Integer c:dis.keySet()) {
			agl.add (c);
			agl2.add(c);
		}
		ArrayList<Integer>bin=new ArrayList<Integer>();
		Set<Integer>memo=new HashSet<Integer>();
		for(Integer i:agl) {
			if(agl2.isEmpty()) {
				break;
			}
			for(Integer j:agl2) {
				if(Math.abs(i-j)<10&&i!=j && !memo.contains(j)) {
					memo.add(i);
				
					memo.add(j);
					bin.add(j);
					System.out.println("oui");
				}
			}
		}
		for(Integer iv:bin) {
			dis.remove(iv);
		}
		
		return dis;
	}
	public Espace getEspace() {
		return e;
	}
	public void scanf(int agl) {
		moteurs.endS();
		ArrayList <Double>distances=new ArrayList<Double>();
		moteurs.speed(50);
		moteurs.l1.rotate(2*Actionneurs.QuartT,true);
		moteurs.r1.rotate(-2*Actionneurs.QuartT,true);

		while(moteurs.isMoving()) {
			double []moy=new double[10];
			for(int i=0;i<moy.length;i++) {
				double f=getDistance();
				if(f!=Double.POSITIVE_INFINITY) {
					moy[i]= f;

				}else {
					moy[i]=2.5;
				}
			}
			double m=tabMoy(moy);
			int cpt=0;
			ArrayList<Double> rep=new ArrayList<Double>();
			while (cpt<moy.length) {
				if(Math.abs(moy[cpt]-m)<0.1) {
					rep.add(moy[cpt]);
				}
				cpt++;
			}
			double[] cast;
			if(moy.length==0) {
				cast=new double[] {m};
			}else {
				cast=new double[moy.length];
			int i=0;
			for (Double d: rep) {
				cast[i]=d;
				i++;
			}
			}



			System.out.println(tabMoy(cast));

			distances.add(tabMoy(cast));

		}
		moteurs.startS();
		ArrayList<Double>diff=new ArrayList<Double>();
		for(int i=0;i+1<distances.size();i++) {
			diff.add(Math.abs(distances.get(i)-distances.get(i+1)));
		}
		double moyenDiff=moyArrays(diff);
		double eDiff=ecartType(diff);
		double delta=0.2;
		Map <Integer,Double> dict=new HashMap<Integer,Double>();
		for(int compt=0;compt<diff.size();compt++) {
			double v=diff.get(compt);
			if(v>moyenDiff+eDiff+delta) {
				dict.put(compt*agl/diff.size(),distances.get(compt));
			}	
		}
		dict=cleanDis(dict);
		System.out.println(dict.keySet());
		
	}








	public int scan() {

		ArrayList<Double>distances=new ArrayList<Double>();
		moteurs.l1.endSynchronization();
		moteurs.l1.setSpeed(30);
		moteurs.r1.setSpeed(30);

		moteurs.l1.rotate(Actionneurs.QuartT,true);
		moteurs.r1.rotate(-Actionneurs.QuartT,true);




		while(moteurs.isMoving()) {
			double []moy=new double[10];
			for(int i=0;i<moy.length;i++) {
				double f=getDistance();
				if(f!=Double.POSITIVE_INFINITY) {
					moy[i]= f;

				}else {
					moy[i]=2.5;
				}
			}
			double m=tabMoy(moy);
			int cpt=0;
			ArrayList<Double> rep=new ArrayList<Double>();
			while (cpt<moy.length) {
				if(Math.abs(moy[cpt]-m)<0.1) {
					rep.add(moy[cpt]);
				}
				cpt++;
			}
			double[] cast=new double[rep.size()];
			int i=0;
			for (Double d: rep) {
				cast[i]=d;
				i++;
			}



			System.out.println(tabMoy(cast));

			distances.add(tabMoy(cast));




		}
		moteurs.addAngle(90,true);
		moteurs.l1.startSynchronization();

		double size=distances.size();

		double delta=0.15;
		ArrayList<Double> distDis=new ArrayList<Double>();
		ArrayList <Integer>angleDis=new ArrayList<Integer>();

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


		int compt=0;
		ArrayList<Integer> res=new ArrayList<Integer>();
		while(compt+1<angleDis.size()) {
			res.add((angleDis.get(compt)+angleDis.get(compt+1))/2);
			compt=compt+2;

		}

		for(Integer in: res) {
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
	public String MajPos() {
		e.changementCase();
		return String.valueOf(e.getNumCase());
	}
	public static void main (String[]args) throws IOException {
		Agents robot= new Agents (MotorPort.A,MotorPort.B,MotorPort.D,SensorPort.S1,SensorPort.S3,SensorPort.S4,0,1);
		robot.scanf(180);
		while(!Button.ENTER.isDown()) {

		}






	}

}

