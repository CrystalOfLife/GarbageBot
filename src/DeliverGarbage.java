import org.freedesktop.dbus.test.profile.Log;

import cx.ath.matthew.debug.Debug;
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
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
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

	private MovePilot grabber;
	private DifferentialPilot pilot;
	private Navigator nav;
	private Pose pose;
	private OdometryPoseProvider poseprovider;

	private boolean suppressed = false;

	public DeliverGarbage(DifferentialPilot pilot, EV3ColorSensor colorSensor, MovePilot grabber) {
		this.colorSensor = colorSensor;
		colorProvider = colorSensor.getColorIDMode();
		colorSample = new float[colorProvider.sampleSize()];
		currentDetectedColor = colorSensor.getColorID();
		colorSensor.setFloodlight(Color.BLUE);
		
		this.pilot = pilot;
		this.grabber = grabber;
		nav = new Navigator(pilot);
		poseprovider = new OdometryPoseProvider(pilot);
		nav.setPoseProvider(poseprovider);
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
					grab();
					nav.goTo(750,-200);
					deliver();
					break;
				case Color.RED:
					grab();
					nav.goTo(750,0);
					deliver();
					break;
				case Color.YELLOW:
					grab();
					nav.goTo(750,200);
					deliver();
					break;
				case Color.GREEN:
					grab();
					nav.goTo(750,-400);
					deliver();
					break;
				case Color.BLACK:
					grab();
					nav.goTo(750,400);
					deliver();
					break;
				default:
					colorSensor.setFloodlight(Color.WHITE);
					suppress();
					break;	
			}
		}
	}
	
	public void grab()
	{
		colorSensor.setFloodlight(Color.RED);
		currentDetectedColor = Color.NONE;
		grabber.rotate(-70);
	}
	
	public void deliver()
	{
		while(nav.isMoving()) Thread.yield();
		grabber.rotate(70);
		nav.getMoveController().travel(-200);
		while(nav.isMoving()) Thread.yield();
		nav.goTo(0, 0);
		while(nav.isMoving()) Thread.yield();
		currentDetectedColor = colorSensor.getColorID();
	}
}
