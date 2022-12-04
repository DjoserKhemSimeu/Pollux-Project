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
	
	/*public int modulo() {
		return Math.sqrt(x*x+y*y);
	}
	
	public int arg() {
		return Math.atan(y/x);
	}
	*/
	
}
