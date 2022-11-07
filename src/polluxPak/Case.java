package polluxPak;


//classe représentant les cases composant la zone de jeu en case délimitées par les bandes de couleur du terrain
public class Case implements Zones {
	private String [] bord; //tableau de chaîne de caractères représentant les quatres bords de chaque Case
	private int numCase; //entier représentant le numéro de la case allant de 1 à 16 
	//{bordL,bordR,bordH,bordB}
	
	//constructeur initialisant les attributs d'instance
	public Case (String [] bord, int numCase) {
		this.bord=bord;
		this.numCase=numCase;
	}

	public Case() {
		// TODO Auto-generated constructor stub
	}
	
	//méthode retournant le bord de la case sur lequel se trouve le capteur de couleur
	public String getBord(int cote) {
		// 0=gauche 1=droite 2=haut 3=bas
		if (cote<4 && cote>=0) {
		 return bord[cote];
		}else {
			return null;
		}
	}
	
	//méthode renvoyant le numéro de case courant
	public int getNum() {
		return numCase;
	}

}
