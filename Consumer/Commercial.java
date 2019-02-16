package vgu.consumer;
public class Commercial extends ConsumerFactory{
	// all types of power consumption
	private double  appliances,		// 16% appliances and electronics
					HVAC,			// 26% heating, ventilation, air conditioning
					lighting,		// 20%
					waterHeating,	//  2%
					otherUses;		// 36%
	
	// counter of order
	private static int i;
	
	// constructor
	Commercial(String name, double maxPower, double minPower, double maxChange, double minChange) {
		super(name+i, maxPower, minPower, maxChange, minChange);
		setFlag(2);
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
	public void setHVAC(double x) {
		HVAC = x;
	}
	public void setLighting(double x) {
		lighting = x;
	}
	public void setWaterHeating(double x) {
		waterHeating = x;
	}
	public void setOtherUses(double x) {
		otherUses = x;
	}
	
	// get methods
	public double getAppliances() {
		return appliances;
	}
	public double getHVAC() {
		return HVAC;
	}
	public double getLighting() {
		return lighting;
	}
	public double getWaterHeating() {
		return waterHeating;
	}
	public double getOtherUses() {
		return otherUses;
	}
}
