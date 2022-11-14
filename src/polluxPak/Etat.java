package polluxPak;

public class Etat {
	private String nom;
	public static Etat P0=new Etat ("Etat Initiale");
	public static Etat B1=new Etat ("Premier But Mis");
	public static Etat BUT=new Etat ("But Mis");
	public static Etat PALET_A=new Etat ("Scan reussi");
	public static Etat PALET0=new Etat ("Echec scan");
	public static Etat VERIFICATI0N=new Etat("verification");
	public static Etat SECOUR1=new Etat ("Methode de Secour1");
	public static Etat FINPARCOUR=new Etat ("Fin Secour1");
	public static Etat SECOUR2=new Etat ("Methode de Secour2");


	private Etat() {
		// TODO Auto-generated constructor stub
	}
	private Etat( String nom) {
		this.nom=nom;
	}
	public String toString() {
		return nom;
	}

}
