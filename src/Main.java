import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static void main(String[] args) {
		
		//Setting our pilot for moving up
		DifferentialPilot pilot = new DifferentialPilot(55, 106, Motor.B, Motor.C);
		
		//Setting the pilot for our grabber up
		Wheel rightEngine = WheeledChassis.modelWheel(Motor.D, 55).offset(65);
		Wheel leftEngine = WheeledChassis.modelWheel(Motor.A, 55).offset(-65);
		Chassis grabberChassis = new WheeledChassis(new Wheel[] { rightEngine, leftEngine },
				WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot grabber = new MovePilot(grabberChassis);
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
		
		//The hierarchy 
		Behavior b1 = new SearchForGarbage(pilot);
		Behavior b2 = new DeliverGarbage(pilot, colorSensor, grabber);
		Behavior b3 = new Exit();
		Behavior[] bArray = { b1, b2, b3 };
		Arbitrator arby = new Arbitrator(bArray);
		arby.go();
	}
	

}
