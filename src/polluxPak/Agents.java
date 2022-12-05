package polluxPak;




import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.LinkedList;
import java.util.ListIterator;



import lejos.hardware.port.Port;

import lejos.utility.Delay;


public class Agent {

	/*
	 * La classe Agent coordonne les différentes classes du code en les implémentant
	 * sous forme d'attribut.
	 */

	// l'attribut capteur de la classe Capteur qui utilise les capteurs du robot
	Capteur capteur;

	// l'attribut moteur de la classe Actionneur qui utilise les moteurs du robot
	Actionneur moteur;

	// L'attribut colorT qui représente la couleur courante perçue par le capteur de couleur
	public String colorT;


	// l'attribut espace de la classe espace qui représente le terrain sur lequel évolue l'agent
	public Espace e;


	/*
	 * Le constructeur de la classe prend en paramètres les différents ports du robot afin de faire appel aux
	 * constructeurs de la classe Capteur et Actionneur. Il prend également en paramètres des entiers x
	 * et y afin de définir la case dans laquelle demmarre pollux 
	 */
	public Agent(Port A, Port B, Port D, Port s1, Port s3,Port s4,int x,int y) throws IOException {
		// appel au constructeur de la classe Capteur
		capteur= new Capteur (s1,s3,s4);

		// apppel au constructeur de la classe Actionneurs
		moteur= new Actionneur (A,B,D, true);

		//appel au constructeur de la classe Espace
		e= new Espace(x,y,this);

		//la premiere couleur percue par pollux est definie comme none mais va etre mise à jour au premier
		// appel a la methode getColor
		colorT="none";


	}

	// la methode getAngle retourne l'angle actuel de pollux via la classe Actionneur
	public double getAngle(){
		return moteur.getAngle();
	}

	// la methode  getDistance retourne la distance actuelle perçue par pollux via la classe Capteur
	public double getDistance() {
		return capteur.getDistance();
	}

	/*
	 * la methode détectionPalet retourne true si un palet est perçue ou false sinon, un palet est détecté si 
	 * le capteur de distance perçoit une distance inférieur à 40 cm puis ,après un delais de 300 ms,  le même capteur
	 * percoit une distance supérieure à la précédente distance perçue, le parametre booleen b défini si le palet doit être 
	 * attrapé ou non aprés l'avoir perçue.
	 */
	public boolean detectionPalet(boolean b) {
		// pollux avance
		moteur.avance();

		// appel a la méthode évitePalet afin de prendre en compte  la présence d'un robot  et de mettre en place la procedure
		// d'évitement.
		eviteRobot();


		// condition: si la distance captée est inférieure a 40 cm
		if(capteur.getDistance()<0.4) {

			// le double dist enregistre la distance captée
			double dist=capteur.getDistance();


			// Delais
			Delay.msDelay(300);

			//condition: si la distance enregistrée est inférieure a la nouvelle distance perçue
			if(capteur.getDistance()>dist)
			{

				// condition: si le paramètre b = true
				if(b) {


					// procèdure pour attraper le pallet: le robot ralentis, les pince souvre pui se ferme et le robot reprend 
					//sa vitesse
					moteur.speed(300);
					moteur.ouvrirPince();

					moteur.fermerPince();
					moteur.speed(650);
				}
				// retourne true car un palet a été perçu
				return true;
				
				//si la distance percue est inferieure à 40 cm mais qu'il n'y a pas dist<la nouvelle distance percue
			}else {
				
				// tourne à droite tant que la distance est inférieure à 20cm
				while(capteur.getDistance()<0.2) {
					moteur.fermerPince();
					moteur.tourner(true,1);
				}
				// palet pas percue
				return false;

			}



		}
		// retourne false car palet pas percue
		return false;
	}
	
	

	// methode qui retourne true si une ligne de couleur a été passé
	public boolean passeLigne() {

		//Appel a la méthode passeLigneR qui retourne la nouvelle couleur percue
		//condition: si la nouvelle couleur percue est égale à l'attribut colorT
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
		if(capteur.getColor().equals(colorT)) {
			return null;
		}else {
			return capteur.getColor();
		}
	}
	// methode get color qui retourne la couleur percue par le capteur de couleur
	public String getColor() {
		return capteur.getColor();
	}
	
	// methode majColor qui met à jour l'attribut colorT
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
			moteur.stop();
			moteur.endS();
			moteur.tourner(true, 1);
			moteur.startS();
			moteur.avance();
			Delay.msDelay(500);
			moteur.stop();
			moteur.endS();
			moteur.tourner(false, 1);
			moteur.startS();
			moteur.avance();

		}
	}

	/*Méthode scan(int n) qui prend en parametre un nombre de quart de tour à effectuer
	 * 
	 * Technique utiliser: prendre un echantillon de 5 valeurs ,
	 * l'échantillon est une fenetre glissante, pour chaque nouvelle distance la plus ancienne est suppr
	 * et la nouvelle est stockée, calculer max-min de l'échantillon
	 * stocker cette valeurs dans une liste res. Parcourir la liste res afin de trouver des valeurs > delta
	 * stocker ces valeurs dans un dictionnaire de relation indice dans la liste-distance percue
	 * selectioner dans le dictionnaire l'indice dans la list à la distance la plus faible et la retourner 
	 * 
	 * Problème rencontrer:  lorsque aucun pallet est sur le terrain il detecte quand même une discontinuitée
	 * et la discontinuitée percu lorsqu'il y'a un palet n'est pas précise
	 */






	public double scan(int n, boolean dir) throws IOException {

		// création du tableau des distances qui stocks les distances percues
		ArrayList<Double>distances=new ArrayList<Double>();
		// initilisation des moteurs afin debuter la rotation 
		moteur.endS();
		moteur.l1.setSpeed(100);
		moteur.r1.setSpeed(100);

		// debut de la rotation en faisant appel a Actionneur.QuartT(90°)
		if(dir) {
			moteur.l1.rotate(n*Actionneur.QuartT,true);
			moteur.r1.rotate(-n*Actionneur.QuartT,true);
		}else {
			moteur.l1.rotate(-n*Actionneur.QuartT,true);
			moteur.r1.rotate(n*Actionneur.QuartT,true);
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


			// ajout de min - max dans rep et suppression du premier élement de la fenêtre
			rep.addLast(l.getLast()-l.getFirst());
			System.out.println(rep.size() +" = "+(l.getLast()-l.getFirst()));
			distances.add(fenetre.getLast());
			l.clear();
			fenetre.removeFirst();


		}

		// Creation du dictionnaire de discontinuite (relation indice dans la liste-distance percue)
		HashMap <Integer,Double> dis= new HashMap<Integer,Double>();
		ListIterator<Double> it=rep.listIterator();
		int i=0;
		Double c;
		// definition du delta a 20 cm
		double delta=0.2;
		
		
		// durant les observation nous avons remarqué que les 10 premieres valeurs n'étaient pas fiables
		// nous avons donc décidé de ne pas les prendre en compte
		while (i<10) {
			it.next();
			i++;
		}
		
		// parcours
		while(it.hasNext()) {
			c=it.next();
			/*
			 * on capte la premiere discontinuité pour l'associer a une seconde (debut et fin du pallet
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


		moteur.addAngle(n*90,true);
		
		// recuperation de la plus petite distance parmis les palets
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

		// pollux ouvre la pince et se retourne vers le palet
		double diff= n*90-agl;
		moteur.ouvrirPince();

		if(dir) {

			moteur.tourner(false,diff/90);
		}else {
			moteur.tourner(true,diff/90);
		}
		Delay.msDelay(8000);
		moteurs.speed(850);
		
		// pollux avance jusquà detecter un palet
		moteur.startS();
		moteur.avance();
		while(!detectionPalet(false)) {
			eviteRobot();

		}
		Delay.msDelay(500);
		moteur.stop();
		moteur.endS();
		moteur.fermerPince();



		/// pollux se retourne vers l'enbut
		if(dir) {
			moteur.tourner(true,1+(diff/90));
		}else {
			moteur.tourner(false,1+(diff/90));
		}
		Delay.msDelay(2000);
		
		// avance jusqu'à capter une ligne blache puis dépose le palet
		moteur.startS();
		moteur.avance();
		while(capteur.getColor()!= "white"){
		}
		moteur.stop();
		//lache le palet et se retourne de 90°
		moteur.lacherPalet(1);

		return agl;




	}

	

	/*
	 * methode majPos que l'on souhaitait utiliser pour mettre à jour en temps reel la position de pollux sur l'espace mais
	 * plusieurs erreurs donc pas utilisé
	 */
	public String MajPos() {
		e.changementCase();
		return String.valueOf(e.getNumCase());
	}
	
}

