package vgu.control;
import interfaces.AbstractComponent;
import interfaces.IControl;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 
 * @author Nguyen Hai Duc	- 9701
 * @author Hoang Thai Duong	- 11052
 *
 */
public class Control implements IControl {
	ArrayList<AbstractComponent> generators = new ArrayList<AbstractComponent>();
	ArrayList<AbstractComponent> consumers = new ArrayList<AbstractComponent>();
	ArrayList<AbstractComponent> industrial = new ArrayList<AbstractComponent>();
	ArrayList<AbstractComponent> residential = new ArrayList<AbstractComponent>();
	
	int overload = 0;
	int blackout = 0;
	double price = 1;
	double balance = 0;
	
	ArrayList<AbstractComponent> backups = new ArrayList<AbstractComponent>();
	boolean backup = false;
	
	@Override
	public void addGenerator(AbstractComponent generator) {
		generators.add(generator);
	}
	
	@Override
	public void removeGenerator(AbstractComponent generator) {
		generators.remove(generator);
	}

	@Override
	public List<AbstractComponent> getGenerators() {
		return generators;
	}

	@Override
	public void removeConsumer(AbstractComponent consumer) {
		int flag = ((vgu.consumer.ConsumerFactory)consumer).getFlag();
		if (flag == 0) {
			residential.remove(consumer);
		} else if (flag == 1) {
			industrial.remove(consumer);
		} 
		consumers.remove(consumer);
	}

	@Override
	public List<AbstractComponent> getConsumers() {
		return consumers;
	}
	
	public List<AbstractComponent> getIndustries(){
		return industrial;
	}
	
	public List<AbstractComponent> getResidents(){
		return residential;
	}
	@Override
	public double getTotalDemand() {
		double demand = 0;
		if(consumers.size() > 0) {
			for (AbstractComponent c : consumers) {
				demand += c.getPower();
	    	}
		}
	    return demand;
	}

	@Override
	public double getTotalSupply() {
		double supply = 0;
		if(generators.size() > 0) {
			for (AbstractComponent g : generators) {
				supply += g.getPower();
			}
			if (backup) {
				for (AbstractComponent b : backups) {
					supply += b.getPower();
				}
			}
		}
	    return supply;
	}

	@Override
	public double getFrequency() {
		if (getTotalDemand() == 0 && getTotalSupply() == 0){
			return 50;
		}
		else {
			double diff = (getTotalDemand() - getTotalSupply())/
	    		Math.max(getTotalDemand(), getTotalSupply());
			return 50.0D - diff*10;
		}
	}

	@Override
	public double getCost() {
		double cost = 0;
		for (AbstractComponent g : generators) {
			cost += g.getCost();
		}
		if (backup) {
			for (AbstractComponent b : backups) {
				cost += (b.getCost() + 0.25*b.getPower());
			}
		}
	    return cost;
	}

	public void setPrice(double x) {
		if ( x > 0) {
			price = x;
		}
	}
	public double getPrice() {
		return price;
	}
	
	@Override
	public double getProfit() {
		double profit = price*getTotalDemand();
	    return profit;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void checkBalance(double amount) {
		if(amount < 0) {
			double x = Math.abs(amount)/getTotalSupply();
			double y = Math.round(x * 100d) / 100d;
			if (y < x) {
				y += 0.01;
			}
			setPrice(price + y);
		}
	}

	@Override
	public void nextIteration() {
		//
		backup = false;
		//
		double changeAmount = 0;
	    double diff = getTotalSupply() - getTotalDemand();
	    double sign = diff/Math.abs(diff);
	    for (AbstractComponent g : generators) {
	    	changeAmount = changeAmount(diff,g)*sign;
			g.setPower(g.getPower() - changeAmount);
			diff -= changeAmount;
	    }
	    
	    double frequency = getFrequency();
	    checkFrequency(frequency);
	    /**
	    //
	    if (getFrequency() < 48) {
	    	//System.out.println("Fre= " + getFrequency() + " .Sup= " + getTotalSupply());
	    	backup = true;
	    	double bchangeAmount = 0;
	    	double bdiff = getTotalSupply() - getTotalDemand();
	    	double bsign = bdiff/Math.abs(bdiff);
	    	for (AbstractComponent b : backups) {
	    		changeAmount = changeAmount(bdiff,b)*bsign;
				b.setPower(b.getPower() - bchangeAmount);
				bdiff -= bchangeAmount;
	    	}
	    	//System.out.println("New Fre= " + getFrequency() + " .New Sup= " + getTotalSupply());
	    }
	    //
	    */
	    checkBalance(getProfit() - getCost());
	    balance += (getProfit() - getCost());
	    setActivityPower();
	}

	private double changeAmount(double diff, AbstractComponent g) {
		double availableChange = 0;
		
		if ( diff > 0 ) {
			availableChange = Math.min(g.getMaxChange(), g.getPower() - g.getMinPower());
		}
		else if ( diff < 0 ) {
			availableChange = Math.min(g.getMaxChange(), g.getMaxPower() - g.getPower());
		}
		if ( availableChange < g.getMinChange()) {
			availableChange = 0;
		}
		else if ( Math.abs(diff) < availableChange) {
			availableChange = Math.max(Math.abs(diff), g.getMinChange());
		}
		return availableChange;
	}
	private void checkFrequency(double frequency) {
		if (frequency > 51) {
    		int x = (int)Math.ceil(generators.size()*0.1);
    		for (int i = 0; i < x; i++) {
    			generators.remove(0);
    		}
    		blackout = 0;
    		overload += 1;
    		//System.out.println("Overload!");
    	}
	    else if (frequency < 49) {
    		int x = (int)Math.ceil(consumers.size()*0.15);
    		for (int i = 0; i < x; i++) {
    			consumers.remove(0);
    		}
    		blackout += 1;
    		overload = 0;
    		//System.out.println("Blackout!");
    	}
	    else {
	    	blackout = 0;
	    	overload = 0;
	    }

	    if ((overload == 3) || (blackout == 3)) {
		    consumers.clear();
		    generators.clear();
		    //System.out.println("Defect!");
		}
	}
	public void addBackups(AbstractComponent generator) {
		backups.add(generator);
	}
	public void removeBackups(AbstractComponent generator) {
		backups.remove(generator);
	}
	public List<AbstractComponent> getBackups() {
		return backups;
	}

	public int getBlackOut() {
		return this.blackout;
	}
	
	public int getOverLoad() {
		return this.overload;
	}

	public double getTotalMachinDrive() {
		double sum = 0.0;
		for (AbstractComponent ac : industrial) {
			sum += ((vgu.consumer.Industrial)ac).getMachineDrive();
		}
		return sum;
	}
	public double getTotalMaintenance() {
		double sum = 0.0;
		for (AbstractComponent ac : industrial) {
			sum += ((vgu.consumer.Industrial)ac).getMaintenace();
		}
		return sum;
	}
	public double getTotalOtherProcesses() {
		double sum = 0.0;
		for (AbstractComponent ac : industrial) {
			sum += ((vgu.consumer.Industrial)ac).getOtherProcesses();
		}
		return sum;
	}
	public double getTotalAppliances() {
		double sum = 0.0;
		for (AbstractComponent ac : residential) {
			sum += ((vgu.consumer.Residential)ac).getAppliances();
		}
		return sum;
	}
	public double getTotalLighting() {
		double sum = 0.0;
		for (AbstractComponent ac : residential) {
			sum += ((vgu.consumer.Residential)ac).getLighting();
		}
		return sum;
	}
	public double getTotalOtherUses() {
		double sum = 0.0;
		for (AbstractComponent ac : residential) {
			sum += ((vgu.consumer.Residential)ac).getOtherUses();
		}
		return sum;
	}
	
	public void addConsumer(AbstractComponent consumer) {
		consumers.add(consumer);
		// categorize consumers
		int flag = ((vgu.consumer.ConsumerFactory)consumer).getFlag();
		if (flag == 0) {
			residential.add(consumer);
		} else if (flag == 1){
			industrial.add(consumer);
		}
	}
// each consumer has different activities and activity's consumption
	public void setActivityPower() {
			if(!consumers.isEmpty()) {	
				int iteration = ((vgu.consumer.ConsumerFactory)consumers.get(0)).getIteration();
				if (iteration>=-1 && iteration<3) {
					setIndustrial(5, 10, 40, 50);
					setResidential(10, 15, 30, 40);
				} else if (iteration>=3 && iteration<=8) {
					setIndustrial(50, 80, 5, 10);
					setResidential(35, 40, 35, 40);
				} else {
					setIndustrial(10, 15, 45, 55);
					setResidential(25, 35, 25, 35);
				}
			}else {
				industrial.clear();
				residential.clear();
			}
	}
	
	private void setIndustrial(double l1, double r1, double l2, double r2) {
		//System.out.println("here1");
		if (industrial.size() != 0) {
			//System.out.println("here1a");
			for (AbstractComponent ac : industrial) {
				double power = ((vgu.consumer.ConsumerFactory)ac).getPower();
				double onePercent = power/100;
				// calculate percentages for each activity
				double machine = (new Random().nextDouble()*(r1-l1)+l1)*onePercent;
				double maintenance = (new Random().nextDouble()*(r2-l2)+l2)*onePercent;
				double others = power - (machine+maintenance);
				((vgu.consumer.Industrial)ac).setMachineDrive(machine);
				((vgu.consumer.Industrial)ac).setMaintenance(maintenance);
				((vgu.consumer.Industrial)ac).setOtherProcesses(others);
			}
		}
	}
	
	private void setResidential(double l1, double r1, double l2, double r2) {
		//System.out.println("here2");
		if (residential.size() != 0) {
			//System.out.println("here2a");
			for (AbstractComponent ac : residential) {
				double power = ((vgu.consumer.ConsumerFactory)ac).getPower();
				double onePercent = power/100;
				// calculate percentages for each activity
				double appliances = (new Random().nextDouble()*(r1-l1)+l1)*onePercent;
				double lighting = (new Random().nextDouble()*(r2-l2)+l2)*onePercent;
				double others = power - (appliances+lighting);
				((vgu.consumer.Residential)ac).setAppliances(appliances);
				((vgu.consumer.Residential)ac).setLighting(lighting);
				((vgu.consumer.Residential)ac).setOtherUses(others);
			}
		}
	}
}