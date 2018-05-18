import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;


public class SearchForGarbage  extends java.lang.Object implements Behavior {
	
	public boolean suppressed = false;
	
	private MovePilot pilot;
	private Navigator nav;
	private Pose pose;
	
	public SearchForGarbage(MovePilot pilot){
		
		this.pilot = pilot;
		nav = new Navigator(pilot);
	}
	
	public boolean takeControl() {
		return true;
	}
	
	public void action() {
		suppressed = false;
		pilot.setAngularSpeed(100);
		
		while (!suppressed)
		{
			pilot.rotate(5);
			Thread.yield();
		}
	}
	
	public void suppress() {
		// TODO Auto-generated method stub
		suppressed = true;
	}
}
