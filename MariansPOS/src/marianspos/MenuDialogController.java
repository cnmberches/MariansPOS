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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class MenuDialogController implements Initializable {  
    public TableView order_tbl;
    public ObservableList<ObservableList> data;

    @FXML
    private TextField quantity_tf;
    
    @FXML
    private Button cancel_btn, add_btn;
    
    @FXML 
    private Label menuName_tf, status_tf , description_tf, price_tf;
    
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
    private void add(ActionEvent e)
    {
        String cost = String.valueOf(Integer.parseInt(Global.menuClickedItems[3]) * Integer.parseInt(quantity_tf.getText()));
        ObservableList<String> row = FXCollections.observableArrayList();
        row.add(Global.menuClickedItems[0]);
        row.add(Global.menuClickedItems[2]);
        row.add(quantity_tf.getText());
        row.add(cost);
        data.add(row);
        System.out.print(data.toString());
        order_tbl.getItems().clear();
        order_tbl.setItems(data);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuName_tf.setText(Global.menuClickedItems[2]);
        description_tf.setText(Global.menuClickedItems[4]);
        price_tf.setText(Global.menuClickedItems[3]);
        status_tf.setText(Global.menuClickedItems[6]);
    }    
    
}
