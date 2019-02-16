package vgu.consumer;

public class Industrial extends ConsumerFactory{
	// all manufacturing activities consuming power
	private double  machineDrive,
					maintenance,
					otherProcesses;
	// counter of order
	private static int i;
	
	// constructor
	public Industrial(String name, double maxPower, double minPower, double maxChange, double minChange) {
		super(name+i, maxPower, minPower, maxChange, minChange);
		setFlag(1);
		++i;
	}
	
	// reset order counter i
	public static void resetOrderCounter() {
		i = 1;
	}
	// set methods
	public void setMachineDrive(double x) {
		machineDrive = x;
	}
	public void setMaintenance(double x) {
		maintenance = x;
	}
	public void setOtherProcesses(double x) {
		otherProcesses = x;
	}
	
	// get methods
	public double getMachineDrive() {
		return machineDrive;
	}
	public double getMaintenace() {
		return maintenance;
	}
	public double getOtherProcesses() {
		return otherProcesses;
	}
	
}
