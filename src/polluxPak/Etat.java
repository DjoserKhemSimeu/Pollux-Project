package polluxPak;

public class Etat {
	
	/*
	 * La classe ETAT représentent les états possible de pollux définie dans le diagramme d'etat de pollux
	 * certain n'on pas été utiliser dans le code suit a des probleme de derniere minute et par manque d'organisation:
	 * les etat reelement utiliser sont : P0 et BUT pour obtenir le premier pallet en dur et répeter le scan plusieur fois
	 */
	
	// Attribut nom représente le nom de l'etat
	private String nom;
	
	//l'attribut num représente le numero de l'etat
	private int num;
	
	
	/*
	 * Pour représenter les etat nous avons choisi d'utiliser des attribut de classe , etant donner que le constructeur
	 * est private les etat ne peuvent etre défini qu'au sein de la classe elle meme
	 */
	public static Etat P0=new Etat ("Etat Initiale",1);
	public static Etat B1=new Etat ("Premier But Mis",2);
	public static Etat BUT=new Etat ("But Mis",3);
	public static Etat PALET_A=new Etat ("Scan reussi",4);
	public static Etat PALET0=new Etat ("Echec scan",5);
	public static Etat VERIFICATION=new Etat("verification",6);
	public static Etat SECOUR1=new Etat ("Methode de Secour1",7);
	public static Etat FINPARCOUR=new Etat ("Fin Secour1",8);
	public static Etat SECOUR2=new Etat ("Methode de Secour2",9);


	private Etat() {
		// TODO Auto-generated constructor stub
	}
	
	// Constructeur private qui initialise les attribut d'instance
	private Etat( String nom, int num) {
		this.nom=nom;
		this.num=num;
	}
	//Accesseurs
	public int getNum() {
		return num;
	}
	public String toString() {
		return nom;
	}

}
