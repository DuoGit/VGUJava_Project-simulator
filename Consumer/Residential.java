package vgu.consumer;

public class Residential extends ConsumerFactory{
	// all types of residential activities consuming power
	private double  appliances,
					lighting,
					otherUses;
	
	// order counter
	private static int i;
	// constructor
	public Residential(String name, double maxPower, double minPower, double maxChange, double minChange) {
		super(name+i, maxPower, minPower, maxChange, minChange);
		setFlag(0);
		++i;
	}
	// reset order counter i
	public static void resetOrderCounter() {
		i = 1;
	}
	
	// set methods
	public void setAppliances(double x) {
		appliances = x;
	}
	public void setLighting(double x) {
		lighting = x;
	}
	public void setOtherUses(double x) {
		otherUses = x;
	}
	
	// get methods
	public double getAppliances() {
		return appliances;
	}
	public double getLighting() {
		return lighting;
	}
	public double getOtherUses() {
		return otherUses;
	}
}