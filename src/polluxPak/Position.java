package polluxPak;

public class Position {
	private int x; 
	private int y; 
	
	public Position(int abs, int ord) {
		x=abs;
		y=ord;
	}
	
	public void setAbs(int abs) {
		x=abs;
	}
	
	public void setOrd(int ord) {
		y=ord;
	}
	public int getAbs() {
		return x;
	}
	
	public int getOrd() {
		return y;
	}
	
	public int modulo() {
		return (int) Math.sqrt(x*x+y*y);
	}
	
	public int arg() {
		return (int) Math.atan(y/x);
	}
	
	public String toString() {
		return x + ";" + y;
	}
	public int getDistance(Position pos) {
		int distx = x-pos.x;
		int disty = y-pos.y;
		return (int) Math.sqrt(distx*distx + disty*disty);
	}
	public boolean equals(Position pos) {
		if(this.x==pos.x && this.y==pos.y)
			return true;
		return false;
	}
}
