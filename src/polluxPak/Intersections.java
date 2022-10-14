package polluxPak;

public class Intersections implements Zones{
	private Ligne [] intersect;
	private boolean horiOnVert;
	private int num;


	public Intersections() {
		// TODO Auto-generated constructor stub
	}
	public Intersections(Ligne[]tab,boolean b,int n) {
		intersect=tab;
		horiOnVert=b;
		num=n;
	}
	public boolean getHoriOnVert() {
		return horiOnVert;
	}
	public String getBord(int idx) {
		return  intersect[idx].getColor();
	}
	public int getNum() {
		return num;
	}

}
