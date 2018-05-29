import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;


public class SearchForGarbage  extends java.lang.Object implements Behavior {
	
	public boolean suppressed = false;
	
	private DifferentialPilot pilot;
	private Navigator nav;
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
		
		//Rotates to look for trash and counting down to switch location
		while (!suppressed && rotation < 72)
		{
			pilot.rotate(5);
			rotation = rotation + 1; 
			Thread.yield();
		}
		
		//Switches location based on where the robot is now
		while (!suppressed && rotation == 72)
		{
			rotation = 0;
			if (moved == 0)
			{
				nav.goTo(0, 300);
				while(nav.isMoving()) Thread.yield();
				moved = 1;
			}
			else if (moved == 1)
			{
				nav.goTo(0, -300);
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
	
	//When suppressed it will reset rotation and movement to start anew when it returns
	public void suppress() {
		rotation = 0;
		moved = 0;
		suppressed = true;
	}
}
