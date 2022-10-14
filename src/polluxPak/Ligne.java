package polluxPak;

public class Ligne implements Zones {
	private String color;
	private Case[]voisines;
	private double[]minMaxH;
	private double[]minMaxB;
	

	public Ligne() {
		// TODO Auto-generated constructor stub
	}
	public Ligne (String c, Case[]v, double []mmh, double[]mmb) {
	color=c;
	voisines=v;
	minMaxH=mmh;
	minMaxB=mmb;
	}
	public String getBord(int idx) {
		return "none";
	}

	public int getNum() {
		return (voisines[0].getNum()*10)+voisines[1].getNum();
	}
	public String getColor() {
		// TODO Auto-generated method stub
		return color;
	}
	public double[] getMinMaxH() {
		return minMaxH;
	}
	public double[] getMinMaxB() {
		return minMaxB;
	}


}
