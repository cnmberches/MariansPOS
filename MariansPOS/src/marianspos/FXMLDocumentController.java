/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marianspos;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    //Initialize value of position
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private TextField username_textField, password_textField;
    
    @FXML
    private void exit(ActionEvent e) throws IOException 
    {
        //Close the module
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void minimize(ActionEvent e) throws IOException 
    {
        //Minimize the module
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.setIconified(true);;
    }
    
    @FXML
    private void register(ActionEvent event) throws IOException
    {
        //Open register module
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Register");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(new Scene(root1));  
        stage.show();
        new DBConnector().getConnection();
    }
    
    @FXML
    private void logIn(ActionEvent event) throws IOException
    {
        //Open logIn Module
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setScene(new Scene(root1));  
        stage.show();
        MariansPOS.stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }    
    
    void logIn(){
        String username = username_textField.getText(), password = password_textField.getText();
        final String queryCheck = "SELECT * from accounts_tbl WHERE username = '" + username + "'";
        try
        {
            DBConnector db = new DBConnector();
            final PreparedStatement ps = db.getConnection().prepareStatement(queryCheck);
            final ResultSet resultSet = ps.executeQuery();
            if(resultSet.next())
            {
                
            }
        }
        catch(Exception e)
        {
        }
    }
}
