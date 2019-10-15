package marianspos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class MenuDialogController implements Initializable {

    @FXML
    private TextField quantity_tf;
    
    @FXML 
    private Label menuName_tf, status_tf , description_tf, price_tf;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
