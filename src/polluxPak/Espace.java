package polluxPak;


import lejos.utility.Delay;
// Création de la classe représentant le terrain d'exploration de l'aggent
public class Espace {
	// Attribut de la classe
	private boolean [][]tab ; 
	//tab est un tableau booleen en 2 dimension, la booleen true du tableau représente la zone ou se trouve l'agent
	private Zones [][] espaces;
	// espaces est un tableau de Zones (cf: interface Zones) en 2 dimension qui représente les différentes zones du terrains
	private Agents pollux;
	// pollux est l'agent qui se deplace sur le terrain

	public Espace() {
		// TODO Auto-generated constructor stub
	}
	/*Le constructeur Espace(int,int,Agents) intialise les attributs e la classe:
	 * tab est initialisé en un tableau de booleen 5x5 remplis de la valeur false
	 * Les deux int en paramètre indique l'indice dans tab de la case de départ qui sera initialisé à true
	 * pollux est initiliser avec l'objet Agents 
	 * appel de la methode initiliaze (cf:methode initilize()) afin de terminer l'initialisation
	 * 
	 */
	public Espace(int debx,int deby, Agents a) {
		tab= new boolean [][]
				{{false,false,false,false,false}
				,{false,false,false,false,false}
				,{false,false,false,false,false}
				,{false,false,false,false,false}
				,{false,false,false,false,false}};
				tab[debx][deby]=true;
				pollux=a;
				initiliaze();

	}
	/*La methode initialize() instancie des objet de type Zones (cf:interface Zones ,class Case,class Ligne,class Intersection)
	 * ces objet de type Zones vont etre rangées dans le tableau de Zones espaces 
	 */
	private void initiliaze() {
		//instancition des Case
		Case case1=new Case (new String []{"null","red","white","blue"},1);
		Case case2=new Case (new String []{"red","yellow","white","blue"},2);
		Case case3=new Case (new String []{"yellow","null","white","blue"},3);
		Case case4=new Case (new String []{"null","red","blue","green"},4);
		Case case5=new Case (new String []{"red","yellow","blue","green"},5);
		Case case6=new Case (new String []{"yellow","null","blue","green"},6);
		Case case7=new Case (new String []{"null","red","green","white"},7);
		Case case8=new Case (new String []{"red","yellow","green","white"},8);
		Case case9=new Case (new String []{"yellow","null","green","white"},9);



		//instanciation des Lignes
		double inf=Double.POSITIVE_INFINITY;
		Ligne lr1=new Ligne ("red",new Case [] {case1,case2},new double[] {0.3,0.9},new double[] {2.1,inf});
		Ligne lr2=new Ligne ("red",new Case [] {case4,case5},new double[] {0.9,2.1},new double[] {0.9,2.1});
		Ligne lr3=new Ligne ("red",new Case [] {case7,case8},new double[] {2.1,2.7},new double[] {0.3,0.9});
		Ligne ly1=new Ligne ("yellow",new Case[] {case2,case3},new double[] {0.3,0.9},new double[] {2.1,inf});
		Ligne ly2=new Ligne ("yellow",new Case[] {case5,case6},new double[] {0.9,2.1},new double[] {0.9,2.1});
		Ligne ly3=new Ligne ("yellow",new Case[] {case8,case9},new double[] {2.1,2.7},new double[] {0.3,0.9});
		Ligne lb1=new Ligne ("blue",new Case[] {case1,case4},new double[] {0.0,0.5},new double[] {1.5,2.0});
		Ligne lb2=new Ligne ("blue",new Case[] {case2,case5},new double[] {0.5,1.5},new double[] {0.5,1.5});
		Ligne lb3=new Ligne ("blue",new Case[] {case3,case6},new double[] {1.5,2.0},new double[] {0.0,0.5});
		Ligne lv1=new Ligne ("green",new Case[] {case4,case7},new double[] {0.0,0.5},new double[] {1.5,2.0});
		Ligne lv2=new Ligne ("green",new Case[] {case5,case8},new double[] {0.5,1.5},new double[] {0.5,1.5});
		Ligne lv3=new Ligne ("green",new Case[] {case6,case9},new double[] {1.5,2.0},new double[] {0.0,0.5});

		// instanciation des Intersections
		Intersections  i1=new Intersections (new Ligne[] {lb1,lb2,lr1,lr2},false,10);
		Intersections  i2=new Intersections (new Ligne[] {lb2,lb3,ly1,ly2},false,20);
		Intersections  i3=new Intersections (new Ligne[] {lv1,lv2,lr2,lr3},false,30);
		Intersections  i4=new Intersections (new Ligne[] {lv2,lv3,ly2,ly3},false,40);

		//intialisation du tableau espaces avec les objets Zones
		espaces=new Zones[][] 
				{{case1,lr1,case2,ly1,case3}
				,{lb1,  i1,  lb2, i2,  lb3}
				,{case4,lr2,case5,ly2,case6}
				,{lv1,  i3,  lv2, i4,  lv3}
				,{case7,lr3,case8,ly3,case9}};

	}

	/*
	 * la methode changementEsp() mets à jours la position de pollux sur le terrain
	 * via ses capteurs (cf: attribut capteurs class Agents)
	 */
	public boolean changementEsp() {
	
		Zones z=getZone();	// z est la zone actuellement true sur tab (cf: getZone())
		if(z instanceof Ligne) {
			//appelle de changementLigne() si z est une Ligne (cf:changementLigne())
			return changementLigne();
		}
		if(z instanceof Case) {
			//appelle de changementCase() si z est une Case (cf:changementCase())
			return changementCase();
		}

		return false;
	}
	/*la methode changementLigne() retourne un booleen, si le changement a été effectuer retourne true et false sinon
	 * elle s'effectue si la position courante est une Ligne et permet de modifier la position  de pollux en fonction
	 * de la couleur capter et de la distance percue 
	 */

	private boolean changementLigne() {
		// si la zone courante n'est pas une ligne return flase
		if(getZone() instanceof Ligne) {
			
			// création de la Ligne courante
			Ligne courante=(Ligne) getZone();
			if(pollux.passeLigne()) {
				if(pollux.passeLigneR().equals("black")) {
					return false;
				}
				//pollux.passeLigneR retourne une String retournant la nouvelle couleur  perçue
				// prise en charge du cas ou la nouvelle couleur perçue est none c'est à dire 
				//que la nouvellle zone est une case
				if(pollux.passeLigneR().equals("none")) {

					/*
					 * Les ligne rouge et jaune sont les ligne verticales sur le plan du terrain,
					 * ainsi nous utilisons la methodes pollux.getAngle() pour determiné si la case d'arriver 
					 * est à gauche ou a droite de la ligne courante.
					 */
					if(courante.getColor().equals("yellow")||courante.getColor().equals("red")){

						// s'assurer des valeurs de angle
						// si l'angle est entre 360 et 180 la case est à droite sinon gauche
						if(pollux.getAngle()<360 && pollux.getAngle()>180) {
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]][actuel[1]+1]=true;
							return true;
						}else{
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]][actuel[1]-1]=true;
							return true;
						}
						/*
						 * Les ligne verte et bleu sont les lignes horizontale du plan nous
						 * faison donc la meme chose que pour les lignes verticales
						 * en changent les valeurs des bornes pour veifier si la nouvelle
						 * case est au dessus ou en dessous de la ligne courante
						 */
					}else {

						// s'assurer des valeurs de angle
						if(pollux.getAngle()<90 && pollux.getAngle()>270) {
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]+1][actuel[1]]=true;
							return true;
						}else{
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]-1][actuel[1]]=true;
							return true;
						}
					}
					/*
					 * Si la couleur capter par le capteur n'est pas none ni blanc ni noir alors il s'agit d'une 
					 * intersection il faut modifier d'un pas de 2* la pos de pollux afin qu'il reste sur la même ligne 
					 * de couleur en modifiant sa position
					 * 
					 * *les coordonnées des objets intercection ne sont pas des positions accecible pour pollux
					 * le but de ces coordonnée est de reprédsenter la position des pallet et de les rendre accecible
					 * via une methode pollux.deplacementVise()
					 */
				}else if(!(pollux.passeLigneR().equals("white")||pollux.passeLigneR().equals("black"))) {

					//même principe que pour changer changer la position vers une case
					//ormis que le pas de déplacement est de 2
					if(courante.getColor().equals("red")||courante.getColor().equals("yellow")) {
						if (pollux.getAngle()==0) {
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]-2][actuel[1]]=true;
							return true;
						}//par du principe que 180
						else {
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]+2][actuel[1]]=true;
							return true;

						}
					}//part du principe que bleu ou vert
					else {
						if (pollux.getAngle()==90) {
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]][actuel[1]-2]=true;
							return true;
						}//par du principe que 180
						else {
							int[]actuel=getIJ();
							tab[actuel[0]][actuel[1]]=false;
							tab[actuel[0]][actuel[1]+2]=true;
							return true;

						}
					}
				}


			}
			/*
			 * si aucune nouvelle couleur est détecter par le capteur nous faisons appel à la methode 
			 * majLigne() pour mettre a jour la pos de pollux sur la même ligne
			 */
			if(majLigne()) {
				return true;
			}
		}
		//retourne false si la zone n'est pas une ligne
		return false;

	}
	/*
	 * La methode majLigne permet de modifier la position de pollux sur une ligne de couleur uni
	 * si aucune nouvelle couleur a été détectée grace à la distance perçue par le capteur ultrason
	 * ainsi que les attribut de Ligne minMaxH et minMaxB qui représente la distance maximale et minale
	 * qui peut etre perçue en haut et en bas ou a gauche et à droite, cette methode utilise éganlement 
	 * l'angle de l'Agent
	 */
	private boolean majLigne() {

		if(getZone()instanceof Ligne) {
			Ligne courante= (Ligne)getZone();//defini la Ligne courante

			double dist=pollux.getDistance();// defini la distance

			// rappel: les lignes  jaune et rouge sont verticale
			if(courante.getColor().equals("yellow")||courante.getColor().equals("red")) {
				//si l'angle est de 0 utiliser minMaxH pour comparer les distances 
				//rappel: nous ne souhaitons pas que la positions soit une intersection
				// donc un pas de 2 est nécéssaire pour modifier la position
				if(pollux.getAngle()==0) {
					if(dist<courante.getMinMaxH()[0]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]-2][actuel[1]]=true;
						return true;
					}else if(dist>courante.getMinMaxH()[1]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]+2][actuel[1]]=true;
						return true;

					}
					//si l'angle est de 180 utiliser minMaxB pour comparer les distances
				}else if(pollux.getAngle()==180) {
					if(dist<courante.getMinMaxB()[0]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]+2][actuel[1]]=true;
						return true;
					}else if(dist>courante.getMinMaxB()[1]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]-2][actuel[1]]=true;
						return true;

					}

				}
				/*
				 * Dans le cas des lignes horizontales minMaxH représente la gauche 
				 * et minMaxB représente la droite. Etant donné qu'il s'agit de lignes 
				 * horizontale, nous utilisons 90 et 270 comme angle de référence
				 */
			}else {
				if(pollux.getAngle()==90) {
					if(dist<courante.getMinMaxH()[0]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]][actuel[1]-2]=true;
						return true;
					}else if(dist>courante.getMinMaxH()[1]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]][actuel[1]+2]=true;
						return true;

					}
				}else if(pollux.getAngle()==270) {
					if(dist<courante.getMinMaxB()[0]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]][actuel[1]+2]=true;
						return true;
					}else if(dist>courante.getMinMaxB()[1]) {
						int[]actuel=getIJ();
						tab[actuel[0]][actuel[1]]=false;
						tab[actuel[0]][actuel[1]-2]=true;
						return true;

					}

				}

			}
		}
		return false;
	}





	/*
	 * La methode changementCase() utiliser si la position courante est une case 
	 * cette methode utilise un delay afin de determiner si le déplacement est un 
	 * passage de ligne ou un suivi de ligne
	 */
	private boolean changementCase() {

		// si aucune ligne n'as été passé pas de déplacement
		if(!pollux.passeLigne()) {

			return false;
		}else {
			String newCol=pollux.passeLigneR();//recupère la couleur détecter
			//il faut gérer l'asynchronisme
			Delay.msDelay(100);//delay de 100 millisecondes

			/*
			 * Si le après le delay newCol est toujour la même couleur capté
			 * il s'agit d'un suivi de ligne sinon un pasage
			 */
			if(newCol.equals(pollux.getColor())) {
				int i=0;
				Case c= (Case) getZone();
				while(i<4) {
					if(c.getBord(i).equals(newCol)) {
						if(i==0) {
							tab[getIJ()[0]][getIJ()[1]]=false;
							tab[getIJ()[0]][getIJ()[1]-1]=true;
							return true;
						}
						if(i==1) {
							tab[getIJ()[0]][getIJ()[1]]=false;
							tab[getIJ()[0]][getIJ()[1]+1]=true;
							return true;
						}
						if(i==2) {
							tab[getIJ()[0]][getIJ()[1]]=false;
							tab[getIJ()[0]-1][getIJ()[1]]=true;
							return true;
						}
						if(i==3) {
							tab[getIJ()[0]][getIJ()[1]]=false;
							tab[getIJ()[0]+1][getIJ()[1]]=true;
							return true;
						}
					}
					i++;
				}
			}else if(pollux.getColor().equals("none")) {
				int i=0;

				while(i<tab.length) {
					int j=0;
					while(j<tab[i].length) {
						if(tab[i][j]==true) {
							int idx=0;
							while(idx<4) {
								if(espaces[i][j].getBord(idx).equals(newCol)) {
									if(idx==0) {
										tab[i][j]=false;
										tab[i][j-2]=true;
										return true;
									}
									if(idx==1) {
										tab[i][j]=false;
										tab[i][j+2]=true;
										return true;
									}
									if(idx==2) {
										tab[i][j]=false;
										tab[i-2][j]=true;
										return true;
									}
									if(idx==3) {
										tab[i][j]=false;
										tab[i+2][j]=true;
										return true;
									}
								}
								idx++;
							}

						}
						j++;
					}
					i++;
				}
				return false;
			}
			return false;
		}
	}
	public Zones getZone() {
		int i=0;
		while(i<tab.length) {
			int j=0;
			while(j<tab[i].length) {
				if (tab[i][j]==true) {
					return espaces[i][j];
				}
				j++;
			}
			i++;
		}
		return null;
	}
	public int[] getIJ() {
		int i=0;
		while(i<tab.length) {
			int j=0;
			while(j<tab[i].length) {
				if (tab[i][j]==true) {
					return new int [] {i,j};
				}
				j++;
			}
			i++;
		}
		return null;
	}

	public int getNumCase() {
		int i=0;
		while(i<tab.length) {
			int j=0;
			while(j<tab[i].length) {
				if (tab[i][j]==true) {
					return espaces[i][j].getNum();
				}
				j++;
			}
			i++;
		}
		return -1;
	}
}


