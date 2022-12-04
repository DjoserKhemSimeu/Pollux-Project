package polluxPak;




import java.io.IOException;

import lejos.hardware.port.Port;

import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.PublishFilter;

public class Capteurs {
	/*
	 * La classe capteur représente les capteurs de polux ( touch, couleur, ultrason) et les implémente sous forme d'attribut
	 */
	
	
	//  le capteur ultrasonic
	private EV3UltrasonicSensor uss;
	
	// Sample Provider nécéssaire a l'utilsation du capteur ultrason
	private SampleProvider usp;
	
	// tableau de float ou sera stocké les valeurs recu par l'ultrason
	private float[]sampleUltra;
	
	//le capteur de toucher
	private EV3TouchSensor touch;
	
	// Sample Provider nécéssaire a l'utilsation du capteur de toucher
	private SampleProvider tsp;
	
	// tableau de float ou sera stocké les valeurs recu par le touchsensor
	private float[]sampleTouch;
	
	
	//attribut d'instance représentant la frequence utiliser en parametre du constructeur
	// de la classe PublishFilter
	public static float frequence=1;
	
	//instance de la Class TestColor représentant le capteur de couleur
	TestColor color;
	
	
	
	// Initialisation des attribut d'instance en fonction des port des senseur sur le robot(s1,s2,s3)
	public Capteurs (Port s1,Port s3,Port s4) throws IOException {
		
		//appele au constructeur de la classe TestColor
		color=new TestColor (s4);
		
		//appelle au consteur de la classe EV3UltrasonicSensor
		uss=new EV3UltrasonicSensor(s1);
		
		//appelle au consteur de la classe EV3TouchSensor
		touch = new EV3TouchSensor(s3);
		
		//initialisation des sample avec le constructeur de la classe PublishFilter
		usp=new PublishFilter(uss.getDistanceMode(),"Ultrasonic readings", frequence);
		tsp=new PublishFilter(touch.getTouchMode(),"Touch readings", frequence);
		
		// initilisation des tableau permettant de récupérer l'information de toucher et de distance
		sampleUltra=new float [usp.sampleSize()];
		sampleTouch=new float [tsp.sampleSize()];
		
	}
	

	public Capteurs() {
		// TODO Auto-generated constructor stub
	}
	
	//methode get color qui renvoie une string représentant la couleur actuelle captée
	public String getColor() {
		return color.getColor();
	}
	
	// methode close qui ferme les capteurs
	public void close() {
		uss.close();
		touch.close();
	}


	//methode getDistance qui renvoi un double représentant la distance percue
	public double getDistance() {
		usp.fetchSample(sampleUltra,0);
		return sampleUltra[0];
	}
	// methode getTouch renvoyant un booleen qui indique si le capteur de toucher percoit quelque chose ou non
	public boolean getTouch() {
		tsp.fetchSample(sampleTouch,0);
		if(sampleTouch[0]==1) {
			return true;
		}else {
			return false;
		}
	}

}
