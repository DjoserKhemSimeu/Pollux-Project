package polluxPak;




import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.ListIterator;



import lejos.hardware.port.Port;

import lejos.utility.Delay;


public class Agents {

	/*
	 * La classe Agent coordonne les différentes classes du code en les implémentant
	 * sous forme d'attribut.
	 */

	// l'attribut capteurs de la classe Capteurs qui utilise les capteurs du robot
	Capteurs capteurs;

	// l'attribut moteurs de la classe Actionneurs qui utilise les moteurs du robot
	Actionneurs moteurs;

	// L'attribut colorT qui représente la couleur courante perçue par le capteur de couleur
	public String colorT;


	// l'attribut espace de la classe espace qui représente le terrain sur lequel évolue l'agent
	public Espace e;


	/*
	 * Le constructeur de la classe prend en parametre les différent port du robot afin de faire aux
	 * constructeurs de la classe Capteurs et Actionneurs. Il prend également en parametre des entier x
	 * et y afin de définir la case dans laquelle demmarre pollux 
	 */
	public Agents(Port A, Port B, Port D, Port s1, Port s3,Port s4,int x,int y) throws IOException {
		// appel au constructeur de la classe Capteurs
		capteurs= new Capteurs (s1,s3,s4);

		// apppel au constructeur de la classe Actionneurs
		moteurs= new Actionneurs (A,B,D, true);

		//appel au constructeur de la classe Espace
		e= new Espace(x,y,this);

		//la premiere couleur percue par pollux est definie comme none mais vas etre mise a jour au premier
		// appel a la methode getColor
		colorT="none";


	}

	// la methode getAngle retourne l'angle actuel de pollux via la classe Actionneurs
	public double getAngle(){
		return moteurs.getAngle();
	}

	// la methode  getDistance retourne la distance actuelle perçue par pollux via la classe Capteurs
	public double getDistance() {
		return capteurs.getDistance();
	}

	/*
	 * la methode détectionPallet retourne true si un pallet est perçue ou false sinon, un pallet est détecté si 
	 * le capteurs de distance percoit une distance inférieur a 40 cm puis ,après un delais de 300 ms,  le meme capteur
	 * percoit une distance supèrieure à la précédente distance perçue, le parametre booleen b défini si le pallet doit etre 
	 * attrapé ou non aprés l'avoir perçue.
	 */
	public boolean detectionPallet(boolean b) {
		// pollux avance
		moteurs.avance();

		// appel a la méthode évitePallet afin de prendre en compte  la présence d'un robot  et de mettre en place la procedure
		// d'évitement.
		eviteRobot();


		// condition: si la distance captée est inférieure a 40 cm
		if(capteurs.getDistance()<0.4) {

			// le double dist enregistre la distance captée
			double dist=capteurs.getDistance();


			// Delais
			Delay.msDelay(300);

			//condition: si la distance enregistrée est inférieure a la nouvelle distance perçue
			if(capteurs.getDistance()>dist)
			{

				// condition: si le paramètre b = true
				if(b) {


					// procèdure pour attraper le pallet: le robot ralentis, les pince souvre pui se ferme et le robot reprend 
					//sa vitesse
					moteurs.speed(300);
					moteurs.ouvrirPince();

					moteurs.fermerPince();
					moteurs.speed(650);
				}
				// retourne true car un pallet a été percue
				return true;
				
				//si la distance percue est inferieure a 40 cm mais qu'il n'y a pas dist<la nouvelle distance percue
			}else {
				
				// tourne a droite tant que la distance est inférieure a 20cm
				while(capteurs.getDistance()<0.2) {
					moteurs.fermerPince();
					moteurs.tourner(true,1);
				}
				// pallet pas percue
				return false;

			}



		}
		// retourne false car pallet pas percue
		return false;
	}
	
	

	// methode qui retourne true si une ligne de couleur a été passé
	public boolean passeLigne() {

		//Appel a la méthode passeLigneR qui retourne la nouvelle couleur percue
		//condition: si la nouvelle couleurs percue est égale a l'attribut colorT
		if(passeLigneR()==null) {
			return false;
		}else {
			
			// les lignes noires ne sont pas pris en compte
			if(passeLigneR().equals("black")) {
				return false;
			}
			return true;
		}
	}
	// methode passeLigneR qui retourne la nouvelle couleur percue et null si pas de nouvelle couleur percue
	public String passeLigneR() {
		if(capteurs.getColor().equals(colorT)) {
			return null;
		}else {
			return capteurs.getColor();
		}
	}
	// methode get color qui retourne la couleur percue par le capteur de couleur
	public String getColor() {
		return capteurs.getColor();
	}
	
	// methode majColor qui mets a jour l'attribut colorT
	public String majColor() {
		colorT=getColor();
		return colorT;

	}
	// methode get espace qui retourne l'attribut espace
	public Espace getEspace() {
		return e;
	}
	
	
	// methode éviteRobot qui vas faire esquiver le robot par la droite
	public void eviteRobot() {
		if(getDistance()<0.10) {
			moteurs.stop();
			moteurs.endS();
			moteurs.tourner(true, 1);
			moteurs.startS();
			moteurs.avance();
			Delay.msDelay(500);
			moteurs.stop();
			moteurs.endS();
			moteurs.tourner(false, 1);
			moteurs.startS();
			moteurs.avance();

		}
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






	public double scan(int n, boolean dir) throws IOException {

		// création du tableau des distances qui stocks les distances percues
		ArrayList<Double>distances=new ArrayList<Double>();
		// initilisation des moteurs afin debuter la rotation 
		moteurs.endS();
		moteurs.l1.setSpeed(100);
		moteurs.r1.setSpeed(100);

		// debut de la rotation en faisant appel a Actionneurs.QuartT(90°)
		if(dir) {
			moteurs.l1.rotate(n*Actionneurs.QuartT,true);
			moteurs.r1.rotate(-n*Actionneurs.QuartT,true);
		}else {
			moteurs.l1.rotate(-n*Actionneurs.QuartT,true);
			moteurs.r1.rotate(n*Actionneurs.QuartT,true);
		}

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
				fenetre.addLast(fenetre.getLast());
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
		// definition du delta a 20 cm
		double delta=0.2;
		
		
		// durant les observation nous avons remarquée que les 10 premiere valeurs n'était pas fiable
		// nous avons donc décidé de ne pas les prendre en compte
		while (i<10) {
			it.next();
			i++;
		}
		
		// parcour
		while(it.hasNext()) {
			c=it.next();
			/*
			 * on capte la premiere discontinuité pour l'associer a une seconde (debut ey fin du pallet
			 */
			if(c>delta) {
				int i2=i;
				for(int copt=0;copt<10;copt++) {
					it.next();
				}
				i+=10;
				System.out.print(i2+"*");
				boolean pass=false;
				while (it.hasNext()&&pass==false&& i-i2<40) {
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
		
		// recuperation de la plus petite distance parmis les pallets
		double agl=0;
		double min=Double.MAX_VALUE;
		for(Integer k: dis.keySet()) {
			if(min>dis.get(k)) {
				agl=k;
				min=dis.get(k);
			}
		}
		agl=(agl*(n*90))/rep.size();
		if(agl==0) {
			return -1;
		}

		// pollux ouvre la pince et se retourne vers le pallet
		double diff= n*90-agl;
		moteurs.ouvrirPince();

		if(dir) {

			moteurs.tourner(false,diff/90);
		}else {
			moteurs.tourner(true,diff/90);
		}
		Delay.msDelay(8000);
		moteurs.speed(850);
		
		// pollux avance jusquà detecter un pallet
		moteurs.startS();
		moteurs.avance();
		while(!detectionPallet(false)) {
			eviteRobot();

		}
		Delay.msDelay(500);
		moteurs.stop();
		moteurs.endS();
		moteurs.fermerPince();



		/// pollux se retourne vers l'enbut
		if(dir) {
			moteurs.tourner(true,1+(diff/90));
		}else {
			moteurs.tourner(false,1+(diff/90));
		}
		Delay.msDelay(2000);
		
		// avance jusqua capter une ligne blache pui dépose le palet
		moteurs.startS();
		moteurs.avance();
		while(capteurs.getColor()!= "white"){
		}
		moteurs.stop();
		//lache le pallet et se retourne de 90°
		moteurs.lacherPallet(1);

		return agl;




	}

	

	/*
	 * methode majPos que l'on souhaitait utiliser pour mettre a jour en temps reel la position de pollux sur l'espace mais
	 * plusieur erreur donc pas utiliser
	 */
	public String MajPos() {
		e.changementCase();
		return String.valueOf(e.getNumCase());
	}
	
}

