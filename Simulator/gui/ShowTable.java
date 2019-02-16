package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import interfaces.AbstractComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import vgu.consumer.ConsumerFactory;
import vgu.control.Control;
import vgu.generator.GeneratorFactory;
/**
 * Class controls InitialTable.fxml which displays data of consumers and generators on a table
 * @author Nguyen The Viet - 9990
 *
 */
public class ShowTable implements Initializable{
	public ArrayList<AbstractComponent> list = new ArrayList<AbstractComponent>();
	Control control = new Control();
	public ArrayList<AbstractComponent> myList = new ArrayList<AbstractComponent>();
	ArrayList<String> report = new ArrayList<String>();
	int itr;
    @FXML
    private Button button;
    
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	name.setCellValueFactory(new PropertyValueFactory<AbstractComponent,String>("Name"));
    	power.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("Power"));
    	maxPower.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MaxPower"));
    	minPower.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MinPower"));
    	maxChange.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MaxChange"));
    	minChange.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MinChange"));    	
    	status.setCellValueFactory(new PropertyValueFactory<AbstractComponent,String>("Status"));    	
    	//viewData();
    }
    //<columnResizePolicy><TableView fx:constant="CONSTRAINED_RESIZE_POLICY" /></columnResizePolicy>
    /**
     * Method displays data on table
     */
    @FXML 
    public void viewData() {	
    	tableView.getItems().clear();
        ObservableList<AbstractComponent> list = getUserList();
        tableView.getItems().addAll(list); 	

    }
    /**
     * Method get consumer and generator lists
     * @return observable list of consumers and generators
     */
    private ObservableList<AbstractComponent> getUserList(){

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
     * 
     * @param control returns control
     */
    public void retrieveData(Control control) {
    	this.control = control;
    }
    public int getIteration() {
    	return itr;
    }
    

/**    
    private TableColumn<AbstractComponent, String> tableName;
    final ObservableList<AbstractComponent> data = FXCollections.observableArrayList(
    	new ConsumerFactory("Consumer1",100,10,200,20),
    	new ConsumerFactory("Consumer2",200,30,400,10),
    	new GeneratorFactory("Generator1",100,200,30,10)
    );
    ObservableList<AbstractComponent> getList(){
    	ObservableList<AbstractComponent> myList = FXCollections.observableArrayList(list);
    	return myList;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	tableName.setCellValueFactory(new PropertyValueFactory<AbstractComponent,String>("Name"));
    	tableMaxPower.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MaxPower"));
    	tableMinPower.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MinPower"));
    	tableMaxChange.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MaxChange"));
    	tableMinChange.setCellValueFactory(new PropertyValueFactory<AbstractComponent,Double>("MinChange"));
    	//tableDataView.setItems(data);
    }
    
 
    @FXML
    void addData(ActionEvent event) {
    	tableDataView.getItems().clear();
    	list.add(new ConsumerFactory("Consumer",100,100,1,1));
    	list.add(new GeneratorFactory("Totototoe",200,20,200,20));
    	ObservableList<AbstractComponent> displayList = getList();
    	tableDataView.getItems().addAll(displayList);
    }
*/
}