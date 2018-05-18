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

	private MovePilot pilot, grabber;
	private Navigator nav;
	private Pose pose;

	private boolean suppressed = false;

	public DeliverGarbage(MovePilot pilot, EV3ColorSensor colorSensor, MovePilot grabber) {
		this.colorSensor = colorSensor;
		colorProvider = colorSensor.getColorIDMode();
		colorSample = new float[colorProvider.sampleSize()];
		currentDetectedColor = colorSensor.getColorID();
		colorSensor.setFloodlight(Color.WHITE);
		
		this.pilot = pilot;
		this.grabber = grabber;
		nav = new Navigator(pilot);
	}

	
	public boolean takeControl() {
		
		currentDetectedColor = colorSensor.getColorID();
		return currentDetectedColor != Color.NONE;
	}
	

	public void suppress() {
		suppressed = true;
	}
	
	public void action() {
		suppressed = false;
		while(!suppressed)
		{
			currentDetectedColor = colorSensor.getColorID();
			switch (currentDetectedColor) 
			{
				case Color.BLUE:
					colorSensor.setFloodlight(Color.BLUE);
					pilot.rotate(360);
					Sound.setVolume(100);
					Sound.playTone(2, 500);
					nav.goTo(150, 200);
					break;
				case Color.RED:
					colorSensor.setFloodlight(Color.RED);
					currentDetectedColor = Color.NONE;
					grabber.rotate(-52);
					nav.goTo(750,-750);
					while(nav.isMoving()) Thread.yield();
					grabber.rotate(52);
					pilot.travel(-200, true);
					nav.goTo(0,0);
					while(nav.isMoving()) Thread.yield();
					currentDetectedColor = colorSensor.getColorID();
					break;
				case Color.GREEN:
					colorSensor.setFloodlight(Color.GREEN);
					break;
				default:
					colorSensor.setFloodlight(Color.WHITE);
					suppress();
					break;	
			}
		}
	}
}
