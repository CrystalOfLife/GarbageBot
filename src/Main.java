import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Main {

	public static void main(String[] args) {
		Wheel wheel1 = WheeledChassis.modelWheel(Motor.B, 55).offset(53);
		Wheel wheel2 = WheeledChassis.modelWheel(Motor.C, 55).offset(-53);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheel1, wheel2 },
				WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot movePilot = new MovePilot(chassis);
		
		
		Wheel rightEngine = WheeledChassis.modelWheel(Motor.D, 55).offset(65);
		Wheel leftEngine = WheeledChassis.modelWheel(Motor.A, 55).offset(-65);
		Chassis grabberChassis = new WheeledChassis(new Wheel[] { rightEngine, leftEngine },
				WheeledChassis.TYPE_DIFFERENTIAL);
		MovePilot grabber = new MovePilot(grabberChassis);
		
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
		movePilot = new MovePilot(chassis);
		Behavior b1 = new SearchForGarbage(movePilot);
		Behavior b2 = new DeliverGarbage(movePilot, colorSensor, grabber);
		Behavior b3 = new Exit();
		Behavior[] bArray = { b1, b2, b3 };
		Arbitrator arby = new Arbitrator(bArray);
		arby.go();
	}
	

}
