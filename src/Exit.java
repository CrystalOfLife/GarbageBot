import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;


public class Exit implements Behavior{

	private boolean suppressed = false;
	
	//Takes controlled when exit is pressed
	public boolean takeControl() {
		return Button.ESCAPE.isDown();
	}

	//exits the system
	public void action() {
		System.exit(0);
	}
	

	public void suppress() {
		
	}

}
