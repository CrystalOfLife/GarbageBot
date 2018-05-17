import lejos.robotics.Color;
import lejos.hardware.Audio;
import lejos.hardware.Sound;
import lejos.hardware.Sounds;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.ColorAdapter;
import lejos.robotics.ColorIdentifier;
import lejos.robotics.RangeFinderAdapter;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.*;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class DeliverGarbage implements Behavior {

	private RangeFinderAdapter range;
	private SampleProvider colorProvider;
	private float[] colorSample;
	EV3ColorSensor colorSensor;
	int currentDetectedColor;

	private MovePilot pilot;
	private Navigator nav;
	private Pose pose;

	private boolean suppressed = false;

	public DeliverGarbage(MovePilot pilot, EV3ColorSensor colorSensor) {
		this.colorSensor = colorSensor;
		colorProvider = colorSensor.getColorIDMode();
		colorSample = new float[colorProvider.sampleSize()];
		
		this.pilot = pilot;
		nav = new Navigator(pilot);
	}

	
	public boolean takeControl() {

		return currentDetectedColor == Color.RED;
	}
	

	public void suppress() {
		suppressed = true;
	}
	
	public void action() {
		suppressed = false;
		
		while(Button.ESCAPE.isUp())
		{
			currentDetectedColor = colorSensor.getColorID();
			switch (currentDetectedColor) 
			{
				case Color.RED:
					colorSensor.setFloodlight(Color.RED);
					pilot.forward();
					break;
				case Color.GREEN:
					colorSensor.setFloodlight(Color.GREEN);
					break;
				case Color.BLUE:
					colorSensor.setFloodlight(Color.BLUE);
					pilot.rotate(360);
					break;
				default:
					colorSensor.setFloodlight(Color.NONE);
					break;	
			}
		}
		colorSensor.close();
		//nav.clearPath();
	
		
		
		
		
		
		//pilot.forward();
		//nav.addWaypoint(0, 0);
		//pilot.rotateLeft();
		
		while( !suppressed )
		{
			Thread.yield();
		}
		
		pilot.stop();

	}

}
