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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable
{
 
    @FXML
    private TextField username_textField, password_textField;
    
    @FXML
    private void register(ActionEvent event) throws IOException
    {
        //Open register module
        openModule("Register.fxml", Modality.APPLICATION_MODAL, "Register");
    }
    
    @FXML
    private void logIn(ActionEvent event) throws IOException
    {
        String username = username_textField.getText(), password = password_textField.getText();
        final String queryCheck = "SELECT * from accounts_tbl WHERE username = '"+ username +"'";
        try
        {
            DBConnector db = new DBConnector();
            final PreparedStatement ps = db.getConnection().prepareStatement(queryCheck);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next())
            {
                if(username.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password")))
                {
                    Global.name = resultSet.getString("name");
                    Global.role = resultSet.getString("role");
                    Global.account_id = resultSet.getString("accounts_id");
                    Global.username = resultSet.getString("username");
                    
                    if("admin".equals(Global.role))
                    {
                        openModule("DashBoard.fxml", Modality.WINDOW_MODAL, "Dashboard");
                    }
                    else
                    {
                        openModule("POSModule.fxml", Modality.WINDOW_MODAL, "Point of Sales");
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please make sure that the username and password is correct" ,ButtonType.OK);
                    alert.setHeaderText("Username/password is incorrect");
                    alert.setTitle("");
                    alert.showAndWait();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please make sure that the username is correct" ,ButtonType.OK);
                alert.setHeaderText("User account doesn't exist");
                alert.setTitle("");
                alert.showAndWait();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
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
