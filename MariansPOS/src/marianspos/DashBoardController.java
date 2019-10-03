/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marianspos;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Timer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author user
 */
public class DashBoardController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label date_lbl, time_lbl;
    
    @FXML
    private void POS_Btn(ActionEvent event) throws IOException {
        //To open the POS Module
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("POSModule.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Point Of Sales System");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(new Scene(root1));  
        stage.show();
        MariansPOS.stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
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
    
}
