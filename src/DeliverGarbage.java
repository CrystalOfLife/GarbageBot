import org.freedesktop.dbus.test.profile.Log;

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
	private int currentDetectedColor;

	private MovePilot pilot;
	private Navigator nav;
	private Pose pose;

	private boolean suppressed = false;

	public DeliverGarbage(MovePilot pilot, EV3ColorSensor colorSensor) {
		this.colorSensor = colorSensor;
		colorProvider = colorSensor.getColorIDMode();
		colorSample = new float[colorProvider.sampleSize()];
		currentDetectedColor = colorSensor.getColorID();
		colorSensor.setFloodlight(Color.WHITE);
		
		this.pilot = pilot;
		nav = new Navigator(pilot);
	}

	
	public boolean takeControl() {
		
		currentDetectedColor = colorSensor.getColorID();
		System.out.println(currentDetectedColor);
		return currentDetectedColor != Color.NONE;
		//return true;
		
	}
	

	public void suppress() {
		suppressed = true;
	}
	
	public void action() {
		suppressed = false;
		while(true)
		{
			currentDetectedColor = colorSensor.getColorID();
			switch (currentDetectedColor) 
			{
			case Color.BLUE:
				colorSensor.setFloodlight(Color.BLUE);
				pilot.rotate(360);
				Sound.setVolume(100);
				Sound.playTone(2, 500);
				break;
				case Color.RED:
					colorSensor.setFloodlight(Color.RED);
					pilot.forward();
					break;
				case Color.GREEN:
					colorSensor.setFloodlight(Color.GREEN);
					break;
				default:
					colorSensor.setFloodlight(Color.WHITE);
					break;	
			}
		}
		//colorSensor.close();
		
		//while( !suppressed && pilot.isMoving())
		//{
		//	Thread.yield();
		//}

		//pilot.stop();
	}

}
