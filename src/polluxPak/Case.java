package polluxPak;



public class Case implements Zones {
	private String [] bord;
	private int numCase;
	//{bordL,bordR,bordH,bordB}
	
	public Case (String [] bord, int numCase) {
		this.bord=bord;
		this.numCase=numCase;
	}

	public Case() {
		// TODO Auto-generated constructor stub
	}
	public String getBord(int cote) {
		// 0=gauche 1=droite 2=haut 3=bas
		if (cote<4 && cote>=0) {
		 return bord[cote];
		}else {
			return null;
		}
	}
	public int getNum() {
		return numCase;
	}

}
