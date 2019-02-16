import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import interfaces.AbstractComponent;
import vgu.consumer.ConsumerFactory;
import vgu.control.Control;
import vgu.generator.GeneratorFactory;

class TestScenarios {

	ArrayList<AbstractComponent> consumer;
	ArrayList<AbstractComponent> generator;
	@BeforeAll
	static void initialize() {
		// assign the array of iteration and running to array run in ConsumerFactory
		ConsumerFactory.setRunBehaviour(new double[]{.5,.2,.15,.45,.75,.60,.55,.40,.45,.65,.95,.75});
	}
	
	@Test
	void testScenario1() {
		// instantiate a control object		
		Control control = new Control();
		// declare an array list consumer of type AbstractComponent
		
		// let ConsumerFactory generates a list of 100 consumers each with 100W => avgPower = 100 & std = 0
		consumer = ConsumerFactory.generate(100,	// amount: number of consumers
											100,	// avgPower: average power of amount consumers
											0);		// std of power
		// all consumers are REGISTERED in the control object
		for(AbstractComponent a : consumer) {			
			control.addConsumer(a);
		}
		
		// declare an array list generator of type AbstractComponent
		ArrayList<AbstractComponent> generator;
		// let GeneratorFactory generates a list of 15 generators with total power of 11000
		generator = GeneratorFactory.generate(15,		// amount: number of values
											  11000,	// totalPower: sum
											  5000);	// startPower
		// all generators are REGISTERED in the control object
		for(AbstractComponent a : generator) {			
			// add GeneratorFactory to the list generators in control
			control.addGenerator(a);
		}
				
		double totalCost=0;
		double totalProfit=0;
		// 12 iterations
		for(int i = 0; i < 11; i++) {
			totalProfit+=control.getProfit();	// in control
			totalCost+=control.getCost();
			
			System.out.println("Frequency: "+control.getFrequency()+"Hz Demand: "+control.getTotalDemand()+"W  Supply: "+control.getTotalSupply()+"W");
			
			for (AbstractComponent c : consumer) {
				c.next();	// reset Power for consumers in the next iteration
			}
			control.nextIteration();	// reset Power for generators in next iteration
		}
		System.out.println("Daily Profit:"+totalProfit);
		System.out.println("Daily Cost:"+totalCost);

	}
	
	/**
	- The consumers show a consumption according to table 1. 
	- Due to an outage 10% of the supply will unregister at a random iteration between 0 and 5
	- The frequency should stabilize after 5 more iterations
	 */
	@Test
	void testScenario2() {
	Control control = new Control();
		
		ArrayList<AbstractComponent> consumer;		
		consumer = ConsumerFactory.generate(100, 100, 0);
		
		for(AbstractComponent a : consumer) {			
			control.addConsumer(a);
		}
		ArrayList<AbstractComponent> generator;
		generator = GeneratorFactory.generate(6, 10000, 5000);
		for(AbstractComponent a : generator) {			
			control.addGenerator(a);
		}
				
		double totalCost=0;
		double totalProfit=0;
		for(int i = 0; i < 11; i++) {
			if(i==2) {
				generator.remove(0);
				generator.remove(0);
			}
			totalProfit+=control.getProfit();
			totalCost+=control.getCost();
			
			System.out.println(" Frequency: "+control.getFrequency()+"Hz Demand: "+control.getTotalDemand()+"W  Supply: "+control.getTotalSupply()+"W");
			for (AbstractComponent c : consumer) {
				c.next();
			}			
			control.nextIteration();
		}
		System.out.println("Daily Profit:"+totalProfit);
		System.out.println("Daily Cost:"+totalCost);
	}
}