package vgu.generator;
import interfaces.AbstractComponent;
import java.util.ArrayList;
/**
 * class GeneratorFactory generates generators and calculates cost for generating power
 * @author Hoang Thai Duong	- 11052
 *
 */
public class GeneratorFactory extends AbstractComponent {
	// current generating power of each generator
	private double power;
	/**
	 * Constructor
	 * @param name		name of each generator
	 * @param maxPower	highest power each generator generates
	 * @param minPower	lowest power each generator generates
	 * @param maxChange	highest amount each generator can change its current power
	 * @param minChange lowest amount each generator can change its current power
	 */
	public GeneratorFactory(String name, double maxPower, double minPower, double maxChange, double minChange) {
		this.name = name;
		setMaxPower(maxPower);
		setMinPower(minPower);
		setMaxChange(maxChange);
		setMinChange(minChange);
		power = minPower;
	}
	
	@Override
	public void next() {};
	public String getStatus() {
		return "Generator";
	}
	public String getName() {
		return name;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		double maxPower = getMaxPower();
		double minPower = getMinPower();
		double maxChange = getMaxChange();
		double minChange = getMinChange();

		if (power >= minPower && power <= maxPower) {
			// calculate the difference between new power and original power
			double diffPower = (this.power > power) ? this.power - power : power - this.power;
			// new power and original power are the same
			if (diffPower == 0) {
				return;
			}

			// new power and original power are different
			// check if diffPower is in the range of minChange and maxChange
			if (diffPower > maxChange) {
				if (this.power > power) {
					this.power -= maxChange;
				} else {
					this.power += maxChange;
				}
			} else if (diffPower < minChange) {
				if (this.power > power) {
					this.power -= minChange;
				} else {
					this.power += minChange;
				}
			} else {
				this.power = power;
				return;
			}

			// check if newly assigned power is not in the range of minPower and maxPower
			if (this.power > maxPower) {
				this.power = maxPower;
			} else if (this.power < minPower) {
				this.power = minPower;
			}
		}
	}
	// calculate the cost for producing a specific amount of power of each generator
	public double getCost() {
		double  coefficient = .5,
				maxPower = getMaxPower(),
				maxChange = getMaxChange(),
				minChange = getMinChange();

		coefficient += maxPower < 2500 ? .25 : 0;
		coefficient += maxChange > .5 * maxPower ? .25 : 0;
		coefficient += minChange < .5 * maxPower ? .25 : 0;

		return coefficient * getPower();
	}

	/**
	 * generate a single generator
	 * @param name		name of each generator
	 * @param maxPower	highest power each generator generates
	 * @param minPower	lowest power each generator generates
	 * @param maxChange	highest amount each generator can change its current power
	 * @param minChange lowest amount each generator can change its current power
	 * @return a single generator of type AbstractComponent
	 */
	public static AbstractComponent generate(String name, double maxPower, double minPower, double maxChange, double minChange) {
		return new GeneratorFactory(name, maxPower, minPower, maxChange, minChange);
	}

	/**
	 * generate a list of multiple generators 
	 * @param numGen		number of generators
	 * @param totalMaxPower	sum of total max power of all generators in the list
	 * @param initialPower	amount of power for all generators in the first iteration
	 * @return a list of multiple generators of type AbstractComponent
	 */
	public static ArrayList<AbstractComponent> generate(int numGen, double totalMaxPower, double initialPower) {
		ArrayList<AbstractComponent> generator = new ArrayList();
		// calculate the max power each generator has
		double avgMaxPower = totalMaxPower / numGen;
		for (int i = 0; i < numGen; ++i) {
			//generator.add(new GeneratorFactory("Generator_" + i, avgMaxPower, avgMaxPower *3/11, avgMaxPower *5/11, avgMaxPower *.4));
			generator.add(new GeneratorFactory("Generator_" + i, avgMaxPower, avgMaxPower *2.6/11, avgMaxPower *6.5/11, avgMaxPower *.3));
			/*
			 *  set max power for the generator i
			 *  if the initial power is still higher than the max power 
			 */
			if (initialPower >= Math.floor(avgMaxPower)) {
				generator.get(i).setPower(avgMaxPower);
				initialPower -= avgMaxPower;
			}
		}
		return generator;
	}
}