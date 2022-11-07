package polluxPak;




import java.io.IOException;

import lejos.hardware.port.Port;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;

public class Capteurs {
	private EV3UltrasonicSensor uss; //creation d'un objet uss venant de la classe UltrasonicSensor
	private SampleProvider usp; //création de l'instance récupérant les données du capteur Ultrason implentées dans le tab sample provider 
	private float[]sampleUltra; //création de tab de flottant permettant de stocker les données récupérées par le capteur Ultrason
	private EV3TouchSensor touch; //creation d'un objet touch venant de la classe TouchSensor
	private SampleProvider tsp; //création de l'instance récupérant les données du capteur de touché implentées dans le tab sample provider 
	private float[]sampleTouch; //création de tab de flottant permettant de stocker les données récupérées par le capteur de touché
	public static float frequence=1; //attribut de fréquence standart de captage de donnée
	TestColor color; //creation d'un objet color venant de la classe TestColor
	
	// constructeur, initialise les attributs d'instance
	public Capteurs (Port s1,Port s3,Port s4) throws IOException {
		color=new TestColor (s4); //initialisation de color, appelle le constructur qui prend en paramètre s4
		uss=new EV3UltrasonicSensor(s1); //initialisation de uss, appelle le constructeur qui prend en paramètre s1
		touch = new EV3TouchSensor(s3); //initialisation de touch, appelle le constructeur qui prend en paramètre s3
		usp=new PublishFilter(uss.getDistanceMode(),"Ultrasonic readings", frequence); //sample provider, récupère les données du capteur Ultrason
		tsp=new PublishFilter(touch.getTouchMode(),"Touch readings", frequence); //sample provider, récupère les données du capteur de touché
		sampleUltra=new float [usp.sampleSize()]; //initialisation du tab de flottant des données du capteur Ultrason
		sampleTouch=new float [tsp.sampleSize()]; //initialisation du tab de flottant des données de capteur de touché
		
	}
	

	public Capteurs() {
		// TODO Auto-generated constructor stub
	}
	
	// permet de mettre à jour les capteurs
	public void maj() {
		usp.fetchSample(sampleUltra,0);
		tsp.fetchSample(sampleTouch,0);
	}
	
	// renvoi la couleur captée par le detecteur de couleur
	public String getColor() {
		return color.getColor(); 
	}
	
	// nettoyage des capteurs après utilisation
	public void close() {
		uss.close();
		touch.close();
	}

	// renvoi la distance captée par le detecteur ultrason
	public double getDistance() {
		usp.fetchSample(sampleUltra,0);
		return sampleUltra[0];
	}
	
	// renvoi true si le capteur de touché touche un palet
	public boolean getTouch() {
		tsp.fetchSample(sampleTouch,0);
		if(sampleTouch[0]==1) {
			return true;
		}else {
			return false;
		}
	}
}
