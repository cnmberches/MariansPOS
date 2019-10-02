/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marianspos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RegisterController implements Initializable {
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Button exit_btn;
    
    @FXML
    private void exit(ActionEvent e) throws IOException 
    {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        MariansPOS.stage.show();
    }
    
    @FXML
    private void onMousePressed(MouseEvent event) throws IOException
    {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    
    @FXML
    private void onMouseDragged(MouseEvent event) throws IOException
    {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
}
