package marianspos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddMenuDialogController implements Initializable {
    @FXML
    private ComboBox category_cb;
    
    @FXML 
    private TextField name_tf, price_tf;
    
    @FXML
    private TextArea description_ta;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for(int i = 0; i < Global.col_names.size(); i++)
        {
            options.add(Global.col_names.get(i));
        }
        category_cb.setItems(options);
    }    
    
}
