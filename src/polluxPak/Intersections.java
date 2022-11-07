package polluxPak;

//classe représentant les intersections des bandes de couleur dans la zone.
public class Intersections implements Zones{
	private Ligne [] intersect; //tableau de ligne comprenant l'ensemble des intersections de la zone
	private boolean horiOnVert; //
	private int num; //numéro de l'intersection


	public Intersections() {
		// TODO Auto-generated constructor stub
	}
	//constructeur initialisant les attributs d'instance
	public Intersections(Ligne[]tab,boolean b,int n) {
		intersect=tab;
		horiOnVert=b;
		num=n;
	}
	
	public boolean getHoriOnVert() {
		return horiOnVert;
	}
	
	//méthode renvoyant la couleur du bord de l'indice de l'intersection passée en paramètre
	public String getBord(int idx) {
		return  intersect[idx].getColor();
	}
	
	//méthode renvoyant le numéro de l'intersection courante
	public int getNum() {
		return num;
	}

}
