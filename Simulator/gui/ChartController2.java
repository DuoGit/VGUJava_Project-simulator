package gui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import vgu.consumer.ConsumerFactory;
import vgu.control.Control;
import vgu.generator.GeneratorFactory;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import interfaces.AbstractComponent;
/**
 * Class controls LineChart2.fxml which controls the main simulation in each iteration - controller to xml file LineChart2.fxml
 * @author Viet Nguyen - 9990
 *
 */
public class ChartController2 implements Initializable{
	//Initial Values
	XYChart.Series<String,Number> generatorGUI;
	XYChart.Series<String,Number> consumerGUI;
	XYChart.Series<String,Number> frequencyGUI;
	ArrayList<String> infoList = new ArrayList<String>();
	ArrayList<String> report = new ArrayList<String>();
	ArrayList<String> reportCon = new ArrayList<String>();
 	String iter;	
	Control control = new Control();
	ArrayList<AbstractComponent> consumer;
	ArrayList<AbstractComponent> generator;
	double graphInfo[][] = new double[12][3];
	int itr=-1, reportNumber=1;
	int gloBlackOut = 0, gloOverload = 0;  
    @FXML private Label displayStat; 
    @FXML private Button printButton;
    @FXML private CategoryAxis iteration;
    @FXML private CategoryAxis frequencyIteration;
    @FXML private NumberAxis totalFrequency;
    @FXML private NumberAxis totalPower;
    
    @FXML
    private LineChart<String,Number> lineChart;

    @FXML
    private LineChart<String,Number> lineChart2;

    @FXML
    private TextField myTextField;
    
    @FXML 
    private Label consumerInfo;
    
    @FXML
    private Label generatorInfo;
    
    @FXML
    private Label generalInfo;
    //buttons
    @FXML private Button nextIteration; //next iteration process
    @FXML private Button remGen; //remove generators
    @FXML private Button remCon; //remove consumers
    @FXML private Button setOffButton; //set off consumers
    @FXML private Button scenOne; //generate scenario 1
    @FXML private Button sceneTwo; //generate scenario 2
    @FXML private Button backButton; //back to InputData.fxml
    //text fields
    @FXML private TextField iRemGen; //remove generators field
    @FXML private TextField iRemCon; //remove consumers field
    @FXML private TextField iSetOff; //set consumers off  
    //labels
    @FXML private Label errMsg; //display error 
    @FXML private Label currentItr; //display current iteration 
    @FXML private Label activeCon; //display active consumers
    @FXML private Label showIteration; //display current iteration
    @FXML private Label showFrequency; //display current total frequency
    @FXML private Label showDemand; //display current total demand
    @FXML private Label showSupply; //display current total supply
    @FXML private Label showBalance; //display current balance
    @FXML private Label showCost; //display cost after 12 iterations
    @FXML private Label showProfit; //display profit after 12 iterations
    @FXML private Label numGen; //display number of generators
    @FXML private Label numCon; //display number of consumers
    @FXML private Label showStatus; //display current status of iteration
    @FXML private Label showOverload; //display overloads
    @FXML private Label showBlackOut; //display blackouts
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//report.add("Iteration\tTotal Consumer\tTotal Generator\tTotal Demand\tTotal Supply\tFrequency\tBalance ");
		clearIterationData();
    }

    /**
     * Method to simulate the next iteration with given data. This method continues updating line chart graph 
     * and data of Consumers and Generators with total demand, total supply and total frequency of each iteration.
     * The method will stop simulate if iteration is 12.  
     * @param event triggered if clicking nextIteration button
     * @throws IOException 
     */
    @FXML
    void nextStep(ActionEvent event) throws IOException{
		PopUp popUp = new PopUp();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUp.fxml"));

		Parent root = (Parent) loader.load();
    	if(control.getConsumers().isEmpty()&&control.getGenerators().isEmpty()&&itr==-1) {
    		popUp = loader.getController();
    		popUp.setPopUp("No data to simulate!");
    		popUp.setText();
    		Stage stage = new Stage();
    		stage.setTitle("Error");
    		stage.setScene(new Scene(root));
    		stage.show();
    	}else {
	    	if(itr>=11) {
	    		popUp = loader.getController();
	    		popUp.setPopUp("Simulation is already finished!");
	    		popUp.setText();
	    		Stage stage = new Stage();
	    		stage.setTitle("Error");
	    		stage.setScene(new Scene(root));
	    		stage.show();
	    	}else{
	        	ConsumerFactory.setRunBehaviour(new double[]{.5,.2,.15,.45,.75,.60,.55,.40,.45,.65,.95,.75});
	    		lineChart.getData().clear();
	    		lineChart2.getData().clear();
	    		clearIterationData();

		    	generatorGUI = new XYChart.Series<String,Number>();
		    	consumerGUI = new XYChart.Series<String,Number>();
		    	frequencyGUI = new XYChart.Series<String,Number>();
		    	generatorGUI.setName("Generator");
		    	consumerGUI.setName("Consumer");
		    	frequencyGUI.setName("Frequency"); 
	    		for (AbstractComponent c : consumer) {
	    			c.next();
	    		}			
	    		control.nextIteration();
	    		gloBlackOut = control.getBlackOut();
	    		gloOverload = control.getOverLoad();
	    		if(gloBlackOut == 3 || gloOverload == 3) {
	    			System.out.println(control.getConsumers().size());
	    		}
		    	graphInfo[itr+1][0] = control.getTotalDemand();
		    	graphInfo[itr+1][1] = control.getTotalSupply();
		    	graphInfo[itr+1][2] = control.getFrequency();

		        //infoList.add("Consumers: "+ consumer.size() + "\nGenerators: " + generator.size());
	            //infoList.add("Consumers: "+ control.getConsumers().size() + "\nGenerators: " + control.getGenerators().size()); 
	            for(int i = 0; i < itr+2; i++) {
	    	    	consumerGUI.getData().add(new XYChart.Data(iter.valueOf(i+1),graphInfo[i][0]));
	    	    	generatorGUI.getData().add(new XYChart.Data(iter.valueOf(i+1),graphInfo[i][1]));
	    	        frequencyGUI.getData().add(new XYChart.Data(iter.valueOf(i+1),graphInfo[i][2]));           	
	            }
	            /*
	        	for(final XYChart.Data<Integer,Double> data: generatorGUI.getData()) {
	        		data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED,new EventHandler<MouseEvent>() {

	    				@Override
	    				public void handle(MouseEvent arg0) {
	    					String result = "Total Supply: " + data.getYValue() + "\nFrequency: " + control.getFrequency() + "\nCurrent iteration" + data.getXValue();
	    					int iteration = Integer.parseInt(data.getXValue());
	    					frequencyLabel.setText(result);
	    					generalInfo.setText(infoList.get(iteration-1));
	    				}

	        		});
	       
	        	}*/
	    		showIterationData();
		    	lineChart.getData().addAll(generatorGUI,consumerGUI);
		    	lineChart2.getData().addAll(frequencyGUI);
		    	itr++;	
    		}
    	}
    }  
	/**
	 * Method to remove generators during iterations
	 * @param event triggered if clicking remGen button
	 */
    @FXML
    void removeGenerator(ActionEvent event) {
    	errMsg.setText(null);
    	try {
    		if (iRemGen.getText().isEmpty()) {
    			errMsg.setText("Please input data!");
    		} else {
    			int number = Integer.parseInt(iRemGen.getText());
    			if(number<=control.getGenerators().size()) {
	    			removeGenerator(number);
	    			iRemGen.clear();
	    			showGenAndCon();
    			}else {
    				errMsg.setText("Value is larger than size!");
    				iRemGen.clear();
    			}
    		}
    	} catch (Exception e) {
    		errMsg.setText("Input must be Integer!");
    	}
    }
/**
 * Method to remove generators during iterations
 * @param event triggered if clicking remCon button
 */
    @FXML
    void removeConsumer(ActionEvent event) {
    	errMsg.setText(null);
    	try {
    		if (iRemCon.getText().isEmpty()) {
    			errMsg.setText("Please input data!");
    		} else {
    			int number = Integer.parseInt(iRemCon.getText());
    			if(number<=control.getConsumers().size()) {
	    			removeConsumer(number);
	    			iRemCon.clear();
	    			showGenAndCon();
	    			activeCon.setText(Integer.toString(getActiveConsumer()));
    			}else {
    				errMsg.setText("Value is larger than size!");
    				iRemCon.clear();
    			}
    		}
    	} catch (Exception e) {
    		errMsg.setText("Input must be Integer!");
    	}    	
    }
  
    /**
     * This method will clear all the data after 12 iteration and print out all detailed information of each 
     * iteration into a text file.
     * @param event triggered if clicking Print button
     * @throws IOException
     */
    @FXML
    void printData(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUp.fxml"));
		Parent root = (Parent) loader.load();
    	PopUp popUpController = loader.getController();
    	if(itr<11) {
    		popUpController.setPopUp("Simulation not finished!");
    		popUpController.setText();
    		Stage stage = new Stage();
    		stage.setScene(new Scene(root));
    		stage.show();
    	}else {
    		popUpController.setPopUp("Data printed, clearing data..");
    		popUpController.setText();
    		Stage stage = new Stage();
    		stage.setScene(new Scene(root));
    		stage.show();    
    		writeReport();
	    	lineChart.getData().clear();
	    	lineChart2.getData().clear();
	    	ArrayList<String> infoList = new ArrayList<String>();
	    	control.getConsumers().clear();
	    	control.getGenerators().clear();
	    	double graphInfo[][] = new double[12][3];
	    	clearIterationData();
	    	itr=-1;	
    	}
    }

    @FXML
    void displayInfo(ActionEvent event) {

    }
    

	/**
	 * This method turns off the simulation window and gets back to the input window
	 * @param event triggered if clicking Back button
	 */
    @FXML
    void backToInput(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();    	
    	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InputData.fxml"));
			Parent root = (Parent) loader.load();
	        InputController inputController = new InputController();
    		Stage Inputstage = new Stage();
    		Inputstage.setScene(new Scene(root));
    		Inputstage.show();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    }

    /**
     * This method receive and integer number from text field isSetOff, and use it to randomly turn off
     * a certain amount of consumers inside the control.
     * @param event triggered if clicking setOff button
     * 
     */
    @FXML
    void setConsumerOff(ActionEvent event) {
    	errMsg.setText(null);
    	try {
    			if(control.getConsumers().size()==0) {
    				errMsg.setText("Consumer list is empty!");
    			}else {
	    			int size = Integer.parseInt(iSetOff.getText());
	    			if(size<0 || size > getActiveConsumer()) {
	    				errMsg.setText("Must be in range of activeCon");
	    			}else {
		    			setStatusConsumer(size);
		    			showIterationData();
		    			iSetOff.clear();
	    			}
    			}
    		}catch(Exception e) {
    		e.printStackTrace();
    		errMsg.setText("Invalid Input");
    	}
    	
    }
    /**
     * This methods show the full list of consumers and generators inside the control (name, power, status,
     * min-max power, min-max change power)
     * @param event triggered if clicking Show Full List button
     */
    @FXML
    void showFullList(ActionEvent event) {
    	try {
    		if(control.getConsumers().isEmpty() && control.getGenerators().isEmpty()) {
	    		PopUp popUp = new PopUp();
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUp.fxml"));
	    		Parent root = (Parent) loader.load();
	    		popUp = loader.getController();
	    		popUp.setPopUp("No data to show!");
	    		popUp.setText();
	    		Stage stage = new Stage();
	    		stage.setTitle("Error");
	    		stage.setScene(new Scene(root));
	    		stage.show();
    		}else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("InitialTable.fxml"));
				Parent root = (Parent) loader.load();
		    	ShowTable show = new ShowTable();
		    	show = loader.getController();
				show.retrieveData(control);
				show.viewData();
	    		Stage stage = new Stage();
	    		stage.setScene(new Scene(root));
	    		stage.show();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * This method call a PieChart controller to display detailed data of power uses of 2 types of consumers,
     * namely Industrial and Residential
     * @param event triggered if clicking Show Consumers button
     */
    @FXML
    public void showConsumers(ActionEvent event) {
    	try {
    		if(control.getConsumers().isEmpty()) {
	    		PopUp popUp = new PopUp();
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("PopUp.fxml"));
	    		Parent root = (Parent) loader.load();
	    		popUp = loader.getController();
	    		popUp.setPopUp("No data to show!");
	    		popUp.setText();
	    		Stage stage = new Stage();
	    		stage.setTitle("Error");
	    		stage.setScene(new Scene(root));
	    		stage.show();
    		}else {
	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("PieChart.fxml"));
				Parent root = (Parent) loader.load();
		    	PieChartController pieChart = new PieChartController();
		    	pieChart = loader.getController();
				pieChart.retrieveControl(control);
	    		Stage stage = new Stage();
	    		stage.setScene(new Scene(root));
	    		stage.show();    		
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    /**
     * This methods show number of generators and consumers out of numCon and numGen labels
     */
    void showGenAndCon() {
    	numCon.setText(Integer.toString(control.getConsumers().size()));
    	numGen.setText(Integer.toString(control.getGenerators().size()));
    }
    /**
     * This methods returns specific data of simulation in 1 iteration and gets data ready for printing out
     * to the text file
     */
    void showIterationData() {
    	report.add("Iteration: " + (itr+2));
    	report.add("Total Consumer: " +control.getConsumers().size());
    	report.add("Total Generator: " +control.getGenerators().size());
    	report.add("Frequency: " + control.getFrequency());
    	report.add("Demand: " + control.getTotalDemand());
    	report.add("Supply: " +control.getTotalSupply());
    	report.add("Balance: " +control.getBalance());
    	report.add("--------------");
    	
    	reportCon.add("Iteration: " + (itr+2));
    	reportCon.add("Total Factories: " + control.getIndustries().size());
    	reportCon.add("Machine drive power: " +control.getTotalMachinDrive());
    	reportCon.add("Maintenance power: " + control.getTotalMaintenance());
    	reportCon.add("Other processes power: " + control.getTotalOtherProcesses());
    	reportCon.add("------------------");
    	reportCon.add("Total Residents: " + control.getResidents().size());
    	reportCon.add("Appliances power: " +control.getTotalAppliances());
    	reportCon.add("Lighting power: " + control.getTotalLighting());
    	reportCon.add("Other devices power: " + control.getTotalOtherUses());
    	reportCon.add("***************");
    	
    	showIteration.setText(Integer.toString(itr+2));
    	showFrequency.setText(Double.toString(control.getFrequency()));
    	showDemand.setText(Double.toString(control.getTotalDemand()));
    	showSupply.setText(Double.toString(control.getTotalSupply()));
    	showBalance.setText(Double.toString(control.getBalance()));
    	activeCon.setText(Integer.toString(getActiveConsumer()));
    	showOverload.setText(Integer.toString(gloOverload));
    	showBlackOut.setText(Integer.toString(gloBlackOut));
    	if(control.getFrequency()>51) {
    		showStatus.setText("Overload");
    	}else {
    		if(control.getFrequency()<49) {
    			showStatus.setText("Blackout");
    		}else {
    			if(control.getFrequency()==50 && (control.getConsumers().size()==0 || control.getGenerators().size()==0)){
    				showStatus.setText("Defect");
    			}else {
    				showStatus.setText("Normal");
    			}
    			
    		}
    	}
    	showGenAndCon();
    	if(itr+2==12) {
    		showCost.setText(Double.toString(control.getCost()));
    		showProfit.setText(Double.toString(control.getProfit()));
    		report.add("Total Cost: "+ control.getCost());
    		report.add("Total Profit: "+ control.getProfit());
    	}
    }
    /**
     * This method clears all text fields in the simulation gui
     */
    void clearIterationData() {
    	showIteration.setText(null);
    	showFrequency.setText(null);
    	showDemand.setText(null);
    	showSupply.setText(null);
    	showBalance.setText(null);
    	showCost.setText(null);
    	showProfit.setText(null);
    	numCon.setText(null);
    	numGen.setText(null);
    	activeCon.setText(null);
    	showBlackOut.setText(null);
    	showOverload.setText(null);
    	showStatus.setText(null);
    	errMsg.setText(null);
    }
    /**
     * 
     * @return current iteration
     */
    public int getIteration() {
    	return itr;
    }

   /**
    * 
    * @param myList add consumer list into control
    */
    public void addConsumerList(ArrayList<AbstractComponent> myList) {
    	for(AbstractComponent i: myList) {
    		control.addConsumer(i);
    		
    	}
    	consumer = myList;
    	//System.out.println(consumer.size());

    }
    /**
     * 
     * @param myList add generator list into control
     */
    public void addGeneratorList(ArrayList<AbstractComponent> myList) {
    	for(AbstractComponent i: myList) {
    		control.addGenerator(i);
    	}
    	generator = myList;
    }
    /**
     * 
     * @param i remove random i consumers
     */
    public void removeConsumer(int i) {
    	for(int j=0;j<i;j++) {
    		control.getConsumers().remove(0);
    	}
    }
    /**
     * 
     * @param i remove random i generators
     */
    public void removeGenerator(int i) {
    	for(int j=0;j<i;j++) {
    		control.getGenerators().remove(0);
    	}
    }
    /**
     * This method is used to run the first iteration of the simulation after retrieving data from
     * InputController.java (drawing Line Chart, storing data)
     */
    public void drawGraph() {
    	ConsumerFactory.setRunBehaviour(new double[]{.5,.2,.15,.45,.75,.60,.55,.40,.45,.65,.95,.75});
    	generatorGUI = new XYChart.Series<String,Number>();
    	consumerGUI = new XYChart.Series<String,Number>();
    	frequencyGUI = new XYChart.Series<String,Number>();
		gloBlackOut = control.getBlackOut();
		gloOverload = control.getOverLoad();
		control.setActivityPower();
    	generatorGUI.setName("Generator");
    	consumerGUI.setName("Consumer");
    	frequencyGUI.setName("Frequency");       	
    	consumerGUI.getData().add(new XYChart.Data("1",control.getTotalDemand()));
    	generatorGUI.getData().add(new XYChart.Data("1",control.getTotalSupply()));
        frequencyGUI.getData().add(new XYChart.Data("1",control.getFrequency()));
    	graphInfo[0][0] = control.getTotalDemand();
    	graphInfo[0][1] = control.getTotalSupply();
    	graphInfo[0][2] = control.getFrequency();
    	lineChart.getData().addAll(generatorGUI,consumerGUI);
    	lineChart2.getData().addAll(frequencyGUI);
    	System.out.println(control.getBalance());
    	showIterationData();
    	showGenAndCon();
     	itr++;
    }
    /**
     * This method is used to generate random UNIQUE numbers given in a certain range
     * @param times number of random UNIQUE numbers needed
     * @param size upper bound for randomizing
     * @return list of random UNIQUE numbers
     */
	public int[] randomize(int times, int size) {
		int[] result = new int[times];
		int add;
		HashSet<Integer> used = new HashSet<Integer>();
		for(int i=0; i<times; i++) {
			add = (int)(Math.random()*size);
			while(used.contains(add)||control.getConsumers().get(add).getPower()==0) {
				add = (int) (Math.random()*size);
			}
			used.add(add);
			result[i] = add;
		}
		
		return result; 
	}
    /**
     * This method uses the randomize(times,size) to turn off a number of random consumers
     * @param amount number of consumers needed to be turn off
     */
    public void setStatusConsumer(int amount) {
    	try {
	    	int randomNumbers[] = randomize(amount, control.getConsumers().size());
	    
	    	for(int i=0; i<amount; i++) {
	    		System.out.println(randomNumbers[i]);
	    	    control.getConsumers().get(randomNumbers[i]).setPower(-1);
    		
	    	}
    	}catch(Exception e) {
    		errMsg.setText("Invalid input");
    	}

    }
    /**
     * 
     * @return total active consumers in current iteration
     */
    public int getActiveConsumer() {
    	int number = 0, j=0;
    	ArrayList<ConsumerFactory> con = new ArrayList<ConsumerFactory>();
    	for(AbstractComponent i: control.getConsumers()) {
    		con.add((ConsumerFactory)i);
    	}
    	for(AbstractComponent i: con) {
    		if(con.get(j++).getStatus().equals("On")) {
    			number++;
    		}
    	}
    	return number;
    }
    /**
     * This method generates a report in text format, which stores detailed information of each iteration
     * after simulation. The file is created with real local date and time
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void writeReport() throws FileNotFoundException, UnsupportedEncodingException {
    	LocalDate reportData = java.time.LocalDate.now();  
    	LocalTime time = java.time.LocalTime.now();
    	PrintWriter writer = new PrintWriter("Power Consumption" + reportData +".txt", "UTF-8");
    	PrintWriter writerCon = new PrintWriter("Consumers" +reportData+".txt","UTF-8");
    	
    	for(String i: report) {
    		writer.println(i);
    	}
    	
    	for(String i: reportCon) {
    		writerCon.println(i);
    	}
    	writer.println("-----------------\n Generated at "+ reportData + " at " + time);
    	writerCon.println("Generated at "+ reportData + " at " + time);
    	writer.close();
    	writerCon.close();
    }
    
    /*
    @FXML
    void generateScene1(ActionEvent event) {
    	if(itr==-1) {
	    	ConsumerFactory.setRunBehaviour(new double[]{.5,.2,.15,.45,.75,.60,.55,.40,.45,.65,.95,.75});
	    	//initialize data for line chart
	    	generatorGUI = new XYChart.Series<String,Number>();
	    	consumerGUI = new XYChart.Series<String,Number>();
	    	frequencyGUI = new XYChart.Series<String,Number>();
	    	generatorGUI.setName("Generator");
	    	consumerGUI.setName("Consumer");
	    	frequencyGUI.setName("Frequency");    	
	    	//intialize input
	

	    	consumer = ConsumerFactory.generate(100, 100, 0);
	    	generator = GeneratorFactory.generate(15, 11000, 5000);
	    	//run the first iteration
	    	for(AbstractComponent a : consumer) {			
	    		control.addConsumer(a);
	    	}
	    	for(AbstractComponent a : generator) {			
	    		control.addGenerator(a);
	    	}	
	    	//show iteration data
	    	showIterationData();
	    	//storing first iteration's data
	    	graphInfo[0][0] = control.getTotalDemand();
	    	graphInfo[0][1] = control.getTotalSupply();
	    	graphInfo[0][2] = control.getFrequency();
	        infoList.add("Consumers: "+ consumer.size() + "\nGenerators: " + generator.size());
	    	consumerGUI.getData().add(new XYChart.Data("1",control.getTotalDemand()));
	    	generatorGUI.getData().add(new XYChart.Data("1",control.getTotalSupply()));
	        frequencyGUI.getData().add(new XYChart.Data("1",control.getFrequency()));
	    	lineChart.getData().addAll(generatorGUI,consumerGUI);
	    	lineChart2.getData().addAll(frequencyGUI);
	    	
    	}
    	itr++;
    }


    @FXML
    void generateScene2(ActionEvent event) {
    	if(itr==-1) {
	    	ConsumerFactory.setRunBehaviour(new double[]{.5,.2,.15,.45,.75,.60,.55,.40,.45,.65,.95,.75});
	    	//initialize data for line chart
	    	generatorGUI = new XYChart.Series<String,Number>();
	    	consumerGUI = new XYChart.Series<String,Number>();
	    	frequencyGUI = new XYChart.Series<String,Number>();
	    	generatorGUI.setName("Generator");
	    	consumerGUI.setName("Consumer");
	    	frequencyGUI.setName("Frequency");    	
	    	//intialize input
	    	consumer = ConsumerFactory.generate(100, 100, 0);
	    	generator = GeneratorFactory.generate(15, 11000, 5000);
	    	//run the first iteration
	    	for(AbstractComponent a : consumer) {			
	    		control.addConsumer(a);
	    	}
	    	for(AbstractComponent a : generator) {			
	    		control.addGenerator(a);
	    	}	
	    	//show iteration data
	    	showIterationData();
	    	//storing first iteration's data
	    	graphInfo[0][0] = control.getTotalDemand();
	    	graphInfo[0][1] = control.getTotalSupply();
	    	graphInfo[0][2] = control.getFrequency();
	        infoList.add("Consumers: "+ consumer.size() + "\nGenerators: " + generator.size());
	    	consumerGUI.getData().add(new XYChart.Data("1",control.getTotalDemand()));
	    	generatorGUI.getData().add(new XYChart.Data("1",control.getTotalSupply()));
	        frequencyGUI.getData().add(new XYChart.Data("1",control.getFrequency()));
	    	lineChart.getData().addAll(generatorGUI,consumerGUI);
	    	lineChart2.getData().addAll(frequencyGUI);
			for(int i = 0; i < 11; i++) {
				if(i==2) {
					generator.remove(0);
					generator.remove(0);
				}
			}
	    	
    	}
    	itr++;
    }
    */
    
}

