package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import interfaces.AbstractComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import vgu.consumer.ConsumerFactory;
import vgu.control.Control;
import vgu.generator.GeneratorFactory;
/**
 * The class controls InputData.fxml which is used to get data input from users for the simulation
 * @author Viet Nguyen - 9990
 *
 */
public class InputController implements Initializable{

	private String Con_Name, Gen_Name;
	private int num_Gen, num_Con;
	private double Con_MaxPower, Con_MinPower, Con_MaxChange, Con_MinChange,
					Gen_MaxPower, Gen_MinPower, Gen_MaxChange, Gen_MinChange,
					total_GenPower, initial_Power, total_ConPower, _deviation;
	ArrayList<AbstractComponent> consumers;
	ArrayList<AbstractComponent> generators;
	Control control = new Control();
	TableView table = new TableView();
    @FXML
    private TextField ConMinPower;

    @FXML
    private Button singleGenEnter;

    @FXML
    private TextField initialPower;

    @FXML
    private TextField numCon;

    @FXML
    private Button multiConEnter;

    @FXML
    private TextField deviation;

    @FXML
    private TextField totalGenPower;

    @FXML
    private TextField GenMinPower;

    @FXML
    private TextField totalConPower;

    @FXML
    private Button multiGenEnter;

    @FXML
    private TextField ConMinChange;

    @FXML
    private TextField ConMaxPower;

    @FXML
    private TextField numGen;

    @FXML
    private TextField ConName;

    @FXML
    private Button singleConEnter;

    @FXML
    private TextField ConMaxChange;

    @FXML
    private TextField GenMaxPower;

    @FXML
    private TextField GenMaxChange;

    @FXML
    private TextField GenMinChange;

    @FXML
    private TextField GenName;
    
    @FXML
    private Label singleConLabel;

    @FXML
    private Label singleGenLabel;
    
    @FXML
    private Label multiGenLabel;
    
    @FXML
    private Label multiConLabel;
    
    @FXML private Label totalConsumer;
    @FXML private Label totalGenerator;
    //start simulation
    @FXML private Button startButton;

    @FXML private Button clearButton;
    //table view
    @FXML
    private TableView<AbstractComponent> tableView;
    @FXML
    private TableColumn<AbstractComponent, Double> minChange;
    @FXML
    private TableColumn<AbstractComponent, Double> minPower;
    @FXML
    private TableColumn<AbstractComponent, Double> maxChange;
    @FXML
    private TableColumn<AbstractComponent, String> name;
    @FXML
    private TableColumn<AbstractComponent, Double> maxPower;
    @FXML
    private TableColumn<AbstractComponent, String> status;
    @FXML
    private TableColumn<AbstractComponent, Double> power;
    @FXML private Label simulation;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	name.setCellValueFactory(new PropertyValueFactory<AbstractComponent,String>("Name"));
    	power.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("Power"));
    	maxPower.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MaxPower"));
    	minPower.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MinPower"));
    	maxChange.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MaxChange"));
    	minChange.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MinChange"));    	
    	status.setCellValueFactory(new PropertyValueFactory<AbstractComponent,String>("Status")); 
    }
    //<columnResizePolicy><TableView fx:constant="CONSTRAINED_RESIZE_POLICY" /></columnResizePolicy>
    /**
     *Method displays data on table view format
     */
    void showList() {
    	tableView.getItems().clear();
        ObservableList<AbstractComponent> list = getUserList();
        tableView.getItems().addAll(list); 	
        //String number = ""+control.getConsumers().size();
        totalConsumer.setText(Integer.toString(control.getConsumers().size()));
       // number = ""+control.getGenerators().size();
        totalGenerator.setText(Integer.toString(control.getGenerators().size()));    	
    }
    /**
     * Method returns list for table view
     * @return observable list of consumers and generators
     */
    private ObservableList<AbstractComponent> getUserList(){
    	ArrayList<AbstractComponent> myList = new ArrayList<AbstractComponent>();
    	for(AbstractComponent i: control.getConsumers()) {
    		myList.add(i);
    	}
    	for(AbstractComponent i: control.getGenerators()) {
    		myList.add(i);
    	}
    	ObservableList<AbstractComponent> list = FXCollections.observableArrayList(myList);
        return list;    	
    }
    /**
     * Methods remove all data from lists
     * @param event is triggered if clicking Clear Data button
     */
    @FXML void clearData(ActionEvent event) {
    	control.getConsumers().clear();
    	control.getGenerators().clear();   
    	showList();
    }
    /**
     * Method adds one consumer
     * @param event is triggered if submitting data with "Enter" button in Consumer/Single tab
     */
    @FXML
    void singleConEnter(ActionEvent event) {
    	singleConLabel.setText(null);
    	try {
    		if (ConName.getText().trim().isEmpty()) {
        		throw new Exception("empty name");
        	} else {
        		Con_Name = (String)ConName.getText();
        	}
	    	Con_MaxPower = Double.parseDouble(ConMaxPower.getText());
	    	Con_MinPower = Double.parseDouble(ConMinPower.getText());
	    	Con_MaxChange = Double.parseDouble(ConMaxChange.getText());
	    	Con_MinChange = Double.parseDouble(ConMinChange.getText());
	    	if(Con_MaxPower<0 || Con_MinPower<0 || Con_MaxChange<0 || Con_MinChange<0) {
	    		singleConLabel.setText("Invalid Input");
	    	}else {
	    		clearTextfield(ConName);
		    	clearTextfield(ConMaxPower);
		    	clearTextfield(ConMinPower);
		    	clearTextfield(ConMaxChange);
		    	clearTextfield(ConMinChange);
	    		simulation.setText(null);
		    	control.addConsumer(new ConsumerFactory(Con_Name,Con_MaxPower,Con_MinPower,Con_MaxChange,Con_MinChange));
		    	showList();
    		}	
    	} catch (Exception e) {
    		System.out.println(e);
    		singleConLabel.setText("Invalid input");
    	}
    }

    /**
     * Method adds a certain number of consumers
     * @param event is triggered if submitting data with "Enter" button in Consumer/Multiple tab
     */
    @FXML
    void multiConEnter(ActionEvent event) {
    	multiConLabel.setText(null);
    	try {
    		_deviation = Double.parseDouble(deviation.getText());
    		num_Con = Integer.parseInt(numCon.getText());
        	total_ConPower = Double.parseDouble(totalConPower.getText());
        	if(num_Con<0) {
        		multiConLabel.setText("Invalid Input");
        	}else {
	        	if(_deviation >= total_ConPower || _deviation < 0) {
	        		multiConLabel.setText("deviation < 0 or > power");
	        	}else {
		        	clearTextfield(numCon);
		        	clearTextfield(totalConPower);
		        	clearTextfield(deviation);
		    		consumers = ConsumerFactory.generate(num_Con, (int)total_ConPower, (int)_deviation);		
		    		for(AbstractComponent a : consumers) {			
		    			control.addConsumer(a);
		    		}
		    		simulation.setText(null);
		    		showList();
	    		}
        	}
    	} catch (Exception e) {
    		System.out.println(e);
    		multiConLabel.setText("Invalid input");
    	}
    	
    }

    /**
     * Method adds a generator
     * @param event is triggered if submitting data with "Enter" button in Generator/Single tab
     */
    @FXML
    void singleGenEnter(ActionEvent event) {
    	singleGenLabel.setText(null);
    	try {
    		if (GenName.getText().isEmpty()) {
    			throw new Exception("empty name");
    		} else {
    			Gen_Name = GenName.getText();
    		}
    		Gen_MaxPower = Double.parseDouble(GenMaxPower.getText());
        	Gen_MinPower = Double.parseDouble(GenMinPower.getText());
        	Gen_MaxChange = Double.parseDouble(GenMaxChange.getText());
        	Gen_MinChange = Double.parseDouble(GenMinChange.getText());
        	if(Gen_MaxPower<0 || Gen_MinPower<0 || Gen_MaxChange<0 || Gen_MinChange<0) {
        		singleGenLabel.setText("Invalid Input");
        	}else {
	        	clearTextfield(GenName);
	        	clearTextfield(GenMaxPower);
	        	clearTextfield(GenMinPower);
	        	clearTextfield(GenMaxChange);
	        	clearTextfield(GenMinChange);
	    		simulation.setText(null);
	        	control.addGenerator(new GeneratorFactory(Gen_Name,Gen_MaxPower,Gen_MinPower,Gen_MaxChange,Gen_MinChange));
	        	showList();
        	}
    	} catch (Exception e) {
    		System.out.println(e);
    		singleGenLabel.setText("Invalid input");
    	}
    }
    /**
     * Method adds a certain number of generators
     * @param event is triggered if submitting data with "Enter" button in Generator/Multiple tab
     */
    @FXML
    void multiGenEnter(ActionEvent event) {
    	multiGenLabel.setText(null);
    	try {
    		num_Gen = Integer.parseInt(numGen.getText());
        	total_GenPower = Double.parseDouble(totalGenPower.getText());
        	initial_Power = Double.parseDouble(initialPower.getText());
        	if(total_GenPower < 0 || initial_Power < 0) {
        		multiGenLabel.setText("Invalid Input");
        	}else {
        		if(total_GenPower < initial_Power) {
        			multiGenLabel.setText("Initial Power must be smaller than Total Power!");
        		}else {
		        	clearTextfield(numGen);
		        	clearTextfield(totalGenPower);
		        	clearTextfield(initialPower);
		    		generators = GeneratorFactory.generate(num_Gen,(int)total_GenPower,(int)initial_Power);		
		    		for(AbstractComponent a : generators) {			
		    			control.addGenerator(a);
		    		}
		    		simulation.setText(null);
		    		showList();
        		}
        	}
    	} catch (Exception e) {
    		System.out.println(e);
    		multiGenLabel.setText("Invalid input");
    	}
    }
    /**
     * Method opens Simulation window (LineChart.fxml)
     * @param event is triggered if submitting data with "Enter" button in Generator/Multiple tab
     */
    @FXML
    void startSimulation(ActionEvent event) {
    	if(control.getConsumers().isEmpty() && control.getGenerators().isEmpty()) {
    		simulation.setText("No data input");
    	}else {
            Stage stageStart = (Stage) startButton.getScene().getWindow();
            stageStart.close(); 
	    	try {   	

	    		FXMLLoader loader = new FXMLLoader(getClass().getResource("LineChart2.fxml"));
	    		Parent root = (Parent) loader.load();
	    		ChartController2 chartController = new ChartController2();
	    		chartController = loader.getController();
	    		chartController.addConsumerList(getConsumerList());
	    		chartController.addGeneratorList(getGeneratorList());
	    		chartController.drawGraph();
	    		Stage stage = new Stage();
	    		stage.setScene(new Scene(root));
	    		stage.setTitle("Simulator");
	    		stage.show();
	    	}catch(IOException e) {
	    		e.printStackTrace();
	    	}
    	}
    }    
    /**
     * Method generates data based on requirement of 2 scenarios (100 consumers & 15 generators)
     * @param event is triggered if clicking Generate Scenario button
     */    
    @FXML 
    void generateScenario(ActionEvent event) {
    	ArrayList<AbstractComponent> consumer, generator;
    	consumer = ConsumerFactory.generate(100, 100, 0);
    	generator = GeneratorFactory.generate(15, 11000, 5000);
    	//run the first iteration
    	for(AbstractComponent a : consumer) {			
    		control.addConsumer(a);
    	}
    	for(AbstractComponent a : generator) {			
    		control.addGenerator(a);
    	}	
    	showList();
    }
    void clearTextfield(TextField tf) {
    	tf.clear();
    }
    
    /**
     * 
     * @return consumers list
     */
    public ArrayList<AbstractComponent> getConsumerList(){
    	ArrayList<AbstractComponent> myList = new ArrayList<AbstractComponent>();
    	for(AbstractComponent i: control.getConsumers()) {
    		myList.add(i);
    	}
    	return myList;
    }
    /**
     * 
     * @return generators list
     */
    public ArrayList<AbstractComponent> getGeneratorList(){
    	ArrayList<AbstractComponent> myList = new ArrayList<AbstractComponent>();
    	for(AbstractComponent i: control.getGenerators()) {
    		myList.add(i);
    	}
    	return myList;
    }
    

}