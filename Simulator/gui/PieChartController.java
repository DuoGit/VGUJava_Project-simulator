package gui;

import vgu.consumer.*;
import vgu.control.Control;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
/**
 * Class controls PieChart.fxml file which displays the data of consumers into pie charts
 * @author Nguyen The Viet - 9990
 *
 */
public class PieChartController implements Initializable {
	@FXML Label caption;
    @FXML private PieChart factoryChart;
    @FXML private PieChart houseChart;
    @FXML private Label factoryLabel;
    @FXML private Label householdLabel;
   Control control = new Control();
    /**
     * 
     * @return industries list
     */
    private ObservableList<Data> getIndustries() {
    	ObservableList<Data> inList = FXCollections.observableArrayList();
        inList.add(new PieChart.Data("Machine Drive",control.getTotalMachinDrive()));
        inList.add(new PieChart.Data("Maintenance",control.getTotalMaintenance()));
        inList.add(new PieChart.Data("Other Processes",control.getTotalOtherProcesses()));
    	return inList;
    
    }
    /**
     * 
     * @return residents list
     */
    private ObservableList<Data> getResidents() {
    	ObservableList<Data> reList = FXCollections.observableArrayList();
        reList.add(new PieChart.Data("Appliances",control.getTotalAppliances()));
        reList.add(new PieChart.Data("Lighting",control.getTotalLighting()));
        reList.add(new PieChart.Data("Other devices",control.getTotalOtherUses()));
    	return reList;
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		caption.setText(null);
	} 
	/**
	 * 
	 * @param control returns control 
	 */
	public void retrieveControl(Control control) {
		this.control = control;
		factoryChart.setData(getIndustries());
		houseChart.setData(getResidents());
		factoryLabel.setTextFill(Color.RED);
		householdLabel.setTextFill(Color.RED);
		factoryLabel.setStyle("-fx-font: 40 arial;");
		householdLabel.setStyle("-fx-font: 40 arial;");
		factoryLabel.setText("Total factories: "+ String.valueOf(control.getIndustries().size()));
		householdLabel.setText("Total houses: "+String.valueOf(control.getResidents().size()));
		noteOnData();
	}
	/**
	 * Display data on each node
	 */
	public void noteOnData() {
		caption.toFront();
	    caption.setTextFill(Color.BLACK);
	    caption.setStyle("-fx-font: 26 arial;");
	    for (final PieChart.Data data1 : factoryChart.getData()) {
            data1.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setText(String.valueOf(data1.getPieValue()));
                }
            });
	    }
	    
	    for (final PieChart.Data data2 : houseChart.getData()) {
			caption.toFront();
		    caption.setTextFill(Color.BLACK);
		    caption.setStyle("-fx-font: 26 arial;");
            data2.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    caption.setText(String.valueOf(data2.getPieValue()));
                }
            });
	    }
	}

	
	
}
