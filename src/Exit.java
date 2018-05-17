import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;


public class Exit implements Behavior{

	private boolean suppressed = false;
	
	public boolean takeControl() {
		return Button.ESCAPE.isDown();
	}

	public void action() {
		System.exit(0);
	}
	

	public void suppress() {
		
	}

}
