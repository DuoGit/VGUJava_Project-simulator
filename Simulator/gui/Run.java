package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class to start GUI application 
 * @author Nguyen The Viet - 9990
 *
 */
public class Run extends Application {
  
 
   @Override
   public void start(Stage primaryStage) {
       try {
           Parent root = FXMLLoader.load(getClass()
                   .getResource("/gui/InputData.fxml"));
 
           primaryStage.setTitle("Simulator-Input Data");
           primaryStage.setScene(new Scene(root));
           primaryStage.show();
        
       } catch(Exception e) {
           e.printStackTrace();
       }
   }
  
   public static void main(String[] args) {
       launch(args);
   }
  
}