package polluxPak;

public class Etat {
	
	/*
	 * La classe Etat représente les états possibles de pollux définis dans le diagramme à états de pollux
	 * certains n'ont pas été utilisés dans le code suite à des problèmes de dernière minute et par manque d'organisation:
	 * les états réelement utilisés sont : P0 et BUT pour obtenir le premier palet en dur et répeter le scan plusieurs fois
	 */
	
	// L'attribut nom représente le nom de l'état
	private String nom;
	
	//L'attribut num représente le numero de l'état
	private int num;
	
	
	/*
	 * Pour représenter les états nous avons choisi d'utiliser des attributs de classe, étant donné que le constructeur
	 * est private les états ne peuvent être définis qu'au sein de la classe elle même
	 */
	public static Etat P0=new Etat ("Etat Initial",1);
	public static Etat B1=new Etat ("Premier But Mis",2);
	public static Etat BUT=new Etat ("But Mis",3);
	public static Etat PALET_A=new Etat ("Scan reussi",4);
	public static Etat PALET0=new Etat ("Echec scan",5);
	public static Etat VERIFICATION=new Etat("verification",6);
	public static Etat SECOURS1=new Etat ("Methode de Secours1",7);
	public static Etat FINPARCOURS=new Etat ("Fin Secours1",8);
	public static Etat SECOURS2=new Etat ("Methode de Secours2",9);


	private Etat() {
		// TODO Auto-generated constructor stub
	}
	
	// Constructeur private qui initialise les attributs d'instance
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
