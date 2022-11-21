package polluxPak;



import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
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
		// 
		capteurs= new Capteurs (s1,s3,s4);
		moteurs= new Actionneurs (A,B,D, true);
		e= new Espace(x,y,this);
		colorT="none";
		moteurs.speed(550);

	}
	public double getAngle(){
		return moteurs.getAngle();
	}
	public double getDistance() {
		return capteurs.getDistance();
	}
	public boolean detectionPallet() {
		moteurs.avance();
		if(capteurs.getDistance()<0.4) {
			double dist=capteurs.getDistance();
			Delay.msDelay(300);
			if(capteurs.getDistance()>dist)
			{
				moteurs.speed(450);
				moteurs.actionPince();
				moteurs.stop();
				moteurs.speed(550);
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

	
	public double moyArrays(Collection<Double>t) {
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

	/*Méthode scan(int n) qui prend en parametre un nombre de quart de tour a effectuer
	 * 
	 * Thechnique utiliser: prendre un echantillon de 5 valeurs ,
	 * l'échantillon est une fenetre glissante, pour chaque nouvelle distance la plus ancienne est suppr
	 * et la nouvelle est stocker, calculer max-min de l'échantillon
	 * stocker cette valeurs dans une liste res. Parcourir la liste res afin de trouver des valeurs > delta
	 * stocker ces valeur dans un dictionnaire de relation indice dans la liste-distance percue
	 * selectioner dans le dictionnaire la indice dans la list a la distance la plus faible et la retourner 
	 * 
	 * Problème rencontrer:  lorsque aucun pallet est sur le terrain il detecte quand meme une discontinuitée
	 * et lka discontinuitée percu lorsrqu'il y'a un palet n'es pas précise
	 */






	public double scan(int n) throws IOException {

		// création du tableau des distances qui stocks les distances percues
		ArrayList<Double>distances=new ArrayList<Double>();
		// initilisation des moteurs afin debuter la rotation 
		moteurs.endS();
		moteurs.l1.setSpeed(100);
		moteurs.r1.setSpeed(100);

		// debut de la rotation en faisant appel a Actionneurs.QuartT(90°)
		moteurs.l1.rotate(n*Actionneurs.QuartT,true);
		moteurs.r1.rotate(-n*Actionneurs.QuartT,true);

		// Création d'une ListeTriee qui contiendra l'échantillon sous forme triee
		ListeTriee l=new ListeTriee();

		//Creation de la fentre glissante sur forme d'une linkedList
		LinkedList<Double>fenetre=new LinkedList<Double>();


		//intialization de des deux liste avec les 5 premiere valeur
		for(int compt=0;compt<2;compt++) {
			l.add(getDistance());
			fenetre.addLast(getDistance());
		}

		// Création de la list qui vas contenir les valeur max-min
		LinkedList<Double> rep=new LinkedList<Double>();
		rep.addLast(l.getLast()-l.getFirst());
		System.out.println(rep.size() +" = "+(l.getLast()-l.getFirst()));

		// ajout de la premiere distance percue a distances
		distances.add(getDistance());

		//netoyage de la liste triée et suppression de la premiere distance percue
		l.clear();
		fenetre.removeFirst();

		// boucle de mouvement qui s'arrete à la fin de la rotation de pollux
		while(moteurs.isMoving()) {
			


			double f=getDistance();

			//prise en charge des distance infini = 2.5
			if(f!=Double.POSITIVE_INFINITY) {
				fenetre.addLast(f);

			}else {
				fenetre.addLast(2.5);
			}

			// ajout de tout les element de fentre dans l pour les trier
			l.addAll(fenetre);


			// ajout de min - max dans rep et suppression du premier element de fenetre
			rep.addLast(l.getLast()-l.getFirst());
			System.out.println(rep.size() +" = "+(l.getLast()-l.getFirst()));
			distances.add(fenetre.getLast());
			l.clear();
			fenetre.removeFirst();
			

		}
		
		// Creation du dictionnaire de dis continuite (relation indice dans la liste-distance percue)
		HashMap <Integer,Double> dis= new HashMap<Integer,Double>();
		ListIterator<Double> it=rep.listIterator();
		int i=0;
		Double c;
		double delta=0.2;
		while (i<10) {
			it.next();
			i++;
		}
		while(it.hasNext()) {
			c=it.next();
			if(c>delta) {
				int i2=i;
				System.out.print(i+"*");
				boolean pass=false;
				while (it.hasNext()&&pass==false) {
					c=it.next();
					if(c>delta) {
						dis.put((i+i2)/2,distances.get(i));
						System.out.print(i);
						pass=true;
					}
					i++;
				}
				System.out.println();
			}
			i++;
		}


		moteurs.addAngle(n*90,true);
		double agl=0;
		double min=Double.MAX_VALUE;
		for(Integer k: dis.keySet()) {
			if(min>dis.get(k)) {
				agl=k;
				min=dis.get(k);
				System.out.print(rep.get(k)+" "+"("+k+") ");
			}
		}
		agl=(agl*(n*90))/rep.size();

		System.out.println("=");
		System.out.println(dis.keySet());
		System.out.println(agl);
		double diff= getAngle()-agl;
		
			moteurs.tourner(false,diff/90);
			Delay.msDelay(5000);
			moteurs.speed(550);
			moteurs.startS();
			moteurs.avance();
			while(!detectionPallet()) {
				
			}
			moteurs.stop();
			moteurs.endS();
		
		return agl;




	}

	public void chercheAngle(double n) throws IOException {
		double diff= getAngle()-n;


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
		robot.scan(1);
		Delay.msDelay(10000);







	}

}

