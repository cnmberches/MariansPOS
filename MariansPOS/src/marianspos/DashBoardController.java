package marianspos;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashBoardController implements Initializable {

    @FXML
    private Label date_lbl, time_lbl, name_lbl;
    
    @FXML
    private void salesReport(ActionEvent event) throws IOException {
        openModule("SalesReportModule.fxml", Modality.APPLICATION_MODAL, "Sales Report");
    }
    
    @FXML
    private void inventoryReport(ActionEvent event) throws IOException {
        openModule("InventoryModule.fxml", Modality.APPLICATION_MODAL, "Inventory");
    }
    
    @FXML
    private void accountReport(ActionEvent event) throws IOException {
        openModule("AccountManagementModule.fxml", Modality.APPLICATION_MODAL, "Account Manager");
    }
    
    @FXML
    private void logOut(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to log out?" , ButtonType.NO, ButtonType.YES);
        alert.setTitle("Log out user?");
        //the show and wait functions waits the user to click between the buttons ok cancel
        alert.showAndWait();
        if(alert.getResult().equals(ButtonType.YES))
        {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            MariansPOS.stage.show();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        name_lbl.setText("Name: " + Global.name);
        //To display live date and time 
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
            Calendar now = Calendar.getInstance();
            date_lbl.setText(new SimpleDateFormat("MMM-dd-yyyy").format(now.getTime()));
            time_lbl.setText(new SimpleDateFormat("hh:mm:ss a").format(now.getTime()));
        }),
             new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();  
    }    
    
    void openModule(String fxmlFile, Modality modal, String title) throws IOException
    {
        //this function is for opening a new window where its parameter include the fxml file in string, 
        //how the window will open (dialog or not),and its title 
        //fxml loader is used to get the fxml file wherein it has the codes for the design of the window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        //this parent root is for loading all the codes for design
        Parent root1 = (Parent) fxmlLoader.load();
        //this stage is for creating the window
        Stage stage = new Stage();
        //this function is for how the window will open (window or dialog)
        stage.initModality(modal);
        //this sets the title seen on the upper left of the window
        stage.setTitle(title);
        //this function makes the window not resizable
        stage.setResizable(false);
        //this makes sure that size is equal to the size of window based on the code
        stage.sizeToScene();
        //this puts the fxml file design in the window
        stage.setScene(new Scene(root1));  
        //this makes the window viewable to the user
        stage.show();
        if(modal.equals(Modality.WINDOW_MODAL))
        {
            //this if statement is to check if the window is showned not as a dialog
            //if it is WINDOW_MODAL, the main menu or log in module will close from the screen
            MariansPOS.stage.close();
        }
    }
  
}
