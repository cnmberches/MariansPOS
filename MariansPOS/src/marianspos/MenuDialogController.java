package marianspos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;


public class MenuDialogController implements Initializable {     
    @FXML
    private Button cancel_btn, add_btn;
    
    @FXML
    private ToggleButton discount_tb; 
    
    @FXML
    private RadioButton senior_rb, pwd_rb;
    
    @FXML
    private void cancel(ActionEvent e)
    {
        //this function is use to get the source file of the action event
        final Node source = (Node) e.getSource();
        //this gets the sctive stage or window of the file
        final Stage stage = (Stage) source.getScene().getWindow();
        //this is for closing the window
        stage.close();
    }
    
    @FXML
    private void discount_toggle(ActionEvent e)
    {
        if(discount_tb.isSelected())
        {
            senior_rb.setDisable(false);
            pwd_rb.setDisable(false);
        }
        else
        {
            senior_rb.setDisable(true);
            pwd_rb.setDisable(true);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }    
    
}
