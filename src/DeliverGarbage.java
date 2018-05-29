import lejos.robotics.Color;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.*;

public class DeliverGarbage implements Behavior {

	EV3ColorSensor colorSensor;
	private int currentDetectedColor;

	private MovePilot grabber;
	private Navigator nav;
	private OdometryPoseProvider poseprovider;

	private boolean suppressed = false;

	public DeliverGarbage(DifferentialPilot pilot, EV3ColorSensor colorSensor, MovePilot grabber) {
		this.colorSensor = colorSensor;
		colorSensor.getColorIDMode();
		currentDetectedColor = colorSensor.getColorID();
		colorSensor.setFloodlight(Color.BLUE);
		
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
					nav.goTo(550,-600);
					deliver();
					break;
				case Color.RED:
					grab();
					nav.goTo(750,0);
					deliver();
					break;
				case Color.YELLOW:
					grab();
					nav.goTo(650,-300);
					deliver();
					break;
				case Color.GREEN:
					grab();
					nav.goTo(650,300);
					deliver();
					break;
				case Color.BLACK:
					grab();
					nav.goTo(550,600);
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
