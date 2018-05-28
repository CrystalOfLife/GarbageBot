import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.SampleProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;


public class SearchForGarbage  extends java.lang.Object implements Behavior {
	
	public boolean suppressed = false;
	
	private DifferentialPilot pilot;
	private Navigator nav;
	private Pose pose;
	private OdometryPoseProvider poseprovider;
	private int rotation = 0;
	private int moved = 0;
	
	public SearchForGarbage(DifferentialPilot pilot){
		
		this.pilot = pilot;
		nav = new Navigator(pilot);
		poseprovider = new OdometryPoseProvider(pilot);
		nav.setPoseProvider(poseprovider);
	}
	
	public boolean takeControl() {
		return true;
	}
	
	public void action() {
		suppressed = false;
		
		while (!suppressed && rotation < 72)
		{
			pilot.rotate(5);
			rotation = rotation + 1; 
			Thread.yield();
		}
		
		while (!suppressed && rotation == 72)
		{
			rotation = 0;
			if (moved == 0)
			{
				nav.goTo(0, 200);
				while(nav.isMoving()) Thread.yield();
				moved = 1;
			}
			else if (moved == 1)
			{
				nav.goTo(0, -200);
				while(nav.isMoving()) Thread.yield();
				moved = 2;
			}
			else if (moved == 2)
			{
				nav.goTo(0, 0);
				while(nav.isMoving()) Thread.yield();
				moved = 0;
			}
		}
	}
	
	public void suppress() {
		rotation = 0;
		moved = 0;
		suppressed = true;
	}
}
