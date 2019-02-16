package vgu.consumer;
import interfaces.AbstractComponent;
import java.util.ArrayList;
import java.util.Random;
/**
 * class ConsumerFactory creates consumers
 * 
 * @author Hoang Thai Duong	- 11052
 * @author Tra Ngoc Nguyen	- 10248
 *
 */
public class ConsumerFactory extends AbstractComponent {
	// array to store percentage of power consumption in each iteration
	static double[] iterationArray = new double[] { .5, .2, .15, .45, .75, .60, .55, .40, .45, .65, .95, .75 };
	// counter variable of iterationArray
	private int iteration = -1;
	// current using power of each consumer
	private double power;
	// types of consumers are 2
	private static final int type = 2;
	// a marker of consumer's type
	private int flag = type;
	// state of each consumer
	private boolean state = false;
	
	/**
	 * Constructor
	 * 
	 * @param name		name of consumer
	 * @param maxPower	highest power consumer can reach
	 * @param minPower	lowest power consumer can reach
	 * @param maxChange	maximum amount to change power
	 * @param minChange	minimum amount to change power
	 */
	public ConsumerFactory(String name, double maxPower, double minPower, double maxChange, double minChange) {
		this.name = name;
		setMinChange(minChange);
		setMaxChange(maxChange);
		setMinPower(minPower);
		setMaxPower(maxPower);
		power = minPower;
		next();
	}
	/**
	 * 
	 * @param iterationArray	array of iteration needs to be stored
	 */
	public static void setRunBehaviour(double[] iterationArray) {
		ConsumerFactory.iterationArray = iterationArray;
	}
	/**
	 * 
	 * @return	the exact running iteration
	 */
	public int getIteration() {
		return iteration;
	}
	public void setFlag(int x) {
		flag = x;
	}
	public int getFlag() {
		return flag;
	}
	public String getStatus() {
		return ((state==false && getPower()==0) ? "Off" : "On");
	}
	public String getName() {
		return name;
	}
	/**
	 * create a single consumer
	 * @param name		name of consumer
	 * @param maxPower	highest power consumer can reach
	 * @param minPower	lowest power consumer can reach
	 * @param maxChange	maximum amount to change power
	 * @param minChange	minimum amount to change power
	 * @return consumer of type AbstractComponent
	 */
	public static AbstractComponent generate(String name, double maxPower, double minPower, double maxChange, double minChange) {
		return new ConsumerFactory(name, maxPower, minPower, maxChange, minChange);
	}
	/**
	 * create a list of multiple consumers
	 * @param quan		quantity of consumers
	 * @param avgPower	average power of each consumer
	 * @param deviation	standard deviation
	 * @return a list of consumers of type AbstractComponent
	 */
	public static ArrayList<AbstractComponent> generate(int quan, int avgPower,double deviation) {
		ArrayList<AbstractComponent> consumers = new ArrayList<AbstractComponent>();
		/*
		 * each time a list is created
		 * a number of consumers are categorized into either Residential or Industrial
		 * resetOrderCounter() is to reset the static counter in its class 
		 * to make sure that order of consumers are consecutive despite randomizing 
		 */
		Residential.resetOrderCounter();
		Industrial.resetOrderCounter();
		for (int i = 1; i <= quan; ++i) {
			double power = new Random().nextGaussian()*deviation + avgPower;
			/*
			 * type:represents for 2 types of consumers which are
			 * 0:	residential consumer
			 * 1:	industrial consumer
			 */
			// random the type of consumer
			int flag = new Random().nextInt(type);
			if (flag == 0) {
				//consumers.add(new Residential("Residential area ", avgPower, avgPower*.15, avgPower*.7, avgPower*.1));
				consumers.add(new Residential("Residential area ", power, Math.ceil(power*.2), power*.6, power*.3));
			} else if (flag == 1){
				//consumers.add(new Industrial("Industrial park ", avgPower, avgPower*.15, avgPower*.7, avgPower*.1));
				consumers.add(new Industrial("Industrial park ", power, Math.ceil(power*.2), power*.6, power*.3));
			}
		}
		return consumers;
	}

	@Override
	public double getPower() {
		return power;
	}

	@Override
	public void setPower(double power) {
		if (power == -1) {
			state = false;
			this.power = 0;
		}else if (this.power != power) {
			if (Math.abs(this.power - power) > getMaxChange()) {
				if (power > this.power) {
					this.power += getMaxChange();
				} else {
					this.power -= getMaxChange();
				}
			} else if (Math.abs(this.power - power) < getMinChange()) {
				if (power > this.power) {
					this.power += getMinChange();
				} else {
					this.power -= getMinChange();
				}
			} else {
				this.power = power;
			}

			if (this.power > getMaxPower()) {
				this.power = getMaxPower();
			} else if (this.power < getMinPower()) {
				this.power = getMinPower();
			}
		}
	}

	@Override
	/**
	 * set new power for consumers
	 * based on the percentage of consumption in each iteration 
	 */
	public void next() {
		++iteration;
		if(getStatus().equals("On")) {
			if (Math.random() < ConsumerFactory.iterationArray[iteration]) {
				state = true;
				setPower(getMaxPower());
			} else {
				state = false;
				setPower(getMinPower());
			}
		}
	}

	@Override
	public double getCost() {
		return power;
	}

}