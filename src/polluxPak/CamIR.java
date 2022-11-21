package polluxPak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import lejos.hardware.Button;

public class CamIR {
	private int port;
	private DatagramSocket dsocket;
	private DatagramPacket packet;
	private byte[] buffer;
	private Position posPollux;
	private Position[] posItems;
	//private StringBuffer elementsCourants;
	
	public CamIR(int x, int y) throws IOException { 
		this.port = 8888; // 
		buffer = new byte[2048]; 
		//buffer qui permet de lire les datagrammes
		dsocket = new DatagramSocket(port);
		//packet qui rçoit les les données et les insère dans buffer
		packet = new DatagramPacket(buffer, buffer.length); 
		posPollux = new Position(x,y);
		posItems = getCoord();
		
		//elementsCourants = new StringBuffer();
	}
	
	//à implémenter dans un while pour capter les coordonnées en continu
	// retourne un tableau de la forme suivante : [x1;y1;x2;y2...xn;yn]
	// pour n palets
	public Position[] getCoord() throws IOException { 
		
		//attente de réception de datagramm
        dsocket.receive(packet);
        
        //le buffer est converti en String
        String msg = new String(buffer, 0, packet.getLength());
        
        // le tableau elements contient à l'indice i les infos suivantes :
        // objet i; int abscisse; int ordonne
        // les éléments sont triés en ordre croissant d'abscisse.
        String[] elements = msg.split("\n");
        
        //renvoit le nom de l'adresse IP qui envoie les packets, puis msg
        //System.out.println(packet.getAddress().getHostName());

        //System.out.println(msg);
        
        //tableau de positions qui reçoit abscisse et ordonné de chaque objet detecté
        Position[] absOrd = new Position[elements.length];
 
        //boucle qui remplit absOrd 
        for(int i = 0; i<elements.length; i++) {
        	String[] coord = elements[i].split(";");
        	absOrd[i] = new Position(Integer.parseInt(coord[1]),Integer.parseInt(coord[2]));
        }     
        return absOrd;
	}
	
	public Position[] getItemsImmobiles(Position[] newItems) {//utilisation d'un contains ?
		Position[] res = new Position[11];
		int l = 0;
		for(int i = 0; i<newItems.length;i++) {
			for(int j=0; j<posItems.length;j++) {
				if(newItems[i].equals(posItems[j])) {
					res[l] = newItems[i];
					l++;
				}
				
			}
			
		}
		return res;
	}
	
	
	
	public Position getPaletProche(Position[] palets) {
		int min = posPollux.getDistance(palets[0]);
		Position posMin = palets[0];
		for(int i =0; i<palets.length; i++) {
			if(min>posPollux.getDistance(palets[i]))
				posMin = palets[i]; 
		}
		return posMin;
	}
	
	
	// actualise la position de Pollux
	// prend l'élément le plus proche de la dernière position de pollux connue
	public void getPollux() throws IOException {
		Position[] pos = this.getCoord();
        
		int diffAbs = pos[0].getAbs()-posPollux.getAbs();
		int diffOrd = pos[0].getOrd()-posPollux.getOrd();
		
		//distance minimale, qui sera modifié si une plus petite est trouvée
		int min = (int) Math.sqrt((diffAbs*diffAbs)+(diffOrd*diffOrd));
		int idx = 0;
		for(int i =1; i<pos.length;i = i+1) {
			diffAbs = pos[i].getAbs()-posPollux.getAbs();
			diffOrd = pos[i].getOrd()-posPollux.getOrd();
			if(min > Math.sqrt((diffAbs*diffAbs)+(diffOrd*diffOrd)))
					idx = i;
		}
		posPollux = new Position(pos[idx].getAbs(),pos[idx].getOrd());
	}
	
	public static void main(String args[]) throws IOException {
		CamIR camera = new CamIR(20,500);
		int j = 0;
		
		while(j<10) {
			Position[] pos = camera.getCoord();
			delay(5000);
			Position[] palets = camera.getItemsImmobiles(pos);
			for (int i =0; i<pos.length; i++) {
				System.out.println("position palet" + i + ":" + palets[i]);
			}
			camera.getPollux();
			//System.out.println("position de pollux :" + camera.posPollux);
			j++;
		}
	}

	private static void delay(int i) {
		// TODO Auto-generated method stub
		
	}
}
