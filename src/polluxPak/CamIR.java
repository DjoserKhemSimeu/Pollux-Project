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
	private int[] posPollux;
	private StringBuffer elementsCourants;
	
	public CamIR(int x, int y) throws SocketException { 
		this.port = 8888; // 
		buffer = new byte[2048]; 
		//buffer qui permet de lire les datagrammes
		dsocket = new DatagramSocket(port);
		//packet qui rçoit les les données et les insère dans buffer
		packet = new DatagramPacket(buffer, buffer.length); 
		posPollux = new int[] {x,y};
		elementsCourants = new StringBuffer();
	}
	
	//à implémenter dans un while pour capter les coordonnées en continu
	// retourne un tableau de la forme suivante : [x1;y1;x2;y2...xn;yn]
	// pour n palets
	public int[] getCoord() throws IOException { 
		
		//attente de réception de datagramm
        dsocket.receive(packet);
        
        //le buffer est converti en String
        String msg = new String(buffer, 0, packet.getLength());
        
        // le tableau elements contient à l'indice i les infos suivantes :
        // objet i; int abscisse; int ordonne
        // les éléments sont triés en ordre croissant d'abscisse.
        String[] elements = msg.split("\n");
        
        //renvoit le nom de l'adresse IP qui envoie les packets, puis msg
        System.out.println(packet.getAddress().getHostName() + ": " + msg);
        
        //tableau d'entiers qui reçoit abscisse et ordonné de chaque objet detecté
        int [] absOrd = new int[elements.length * 2];
        int compteur
        //boucle qui remplit absOrd 
        for(int i = 0; i<elements.length; i++) {
        	String[] coord = elements[i].split(";");
        	absOrd[2*i] = Integer.parseInt(coord[1]);
        	absOrd[2*i+1] = Integer.parseInt(coord[2]);
        }     
        return absOrd;
	}
	
	// actualise la position de Pollux
	// prend l'élément le plus proche de la dernière position de pollux connue
	public void getPollux() throws IOException {
		int[] pos = this.getCoord();
		int diffAbs = pos[0]-posPollux[0];
		int diffOrd = pos[1]-posPollux[1];
		
		//distance minimale, qui sera modifié si une plus petite est trouvée
		int min = Math.sqrt((diffAbs*diffAbs)+(diffOrd*diffOrd))
		int idx = 0
		for(int i =2; i<pos.length;i = i+2) {
			diffAbs = pos[i]-posPollux[0];
			diffOrd = pos[i+1]-posPollux[1];
			if(min > Math.sqrt((diffAbs*diffAbs)+(diffOrd*diffOrd)))
					idx = i;
		}
		posPollux = int[] {pos[idx];pos[idx+1]};
	}
