/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marianspos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable
{
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private TextField username_TextField;
    
    @FXML
    private Button exit_btn, min_btn;
    
    @FXML
    private void exit(ActionEvent e) throws IOException 
    {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void minimize(ActionEvent e) throws IOException 
    {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);;
    }
    
    @FXML
    private void register(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(root1));  
        stage.show();
        MariansPOS.stage.close();
    }
    
    @FXML
    private void logIn(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(new Scene(root1));  
        stage.show();
        MariansPOS.stage.close();
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
    public void initialize(URL url, ResourceBundle rb)
    {
    }    
}
