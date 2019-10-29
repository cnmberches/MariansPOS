package marianspos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class MenuDialogController implements Initializable {   
    private int total;
    @FXML
    private Button cancel_btn, add_btn;
    
    @FXML
    private ToggleButton discount_tb; 
    
    @FXML
    private RadioButton senior_rb, pwd_rb;
    
    @FXML
    private Label discount_lbl, tax_lbl, total_lbl, grandTotal_lbl, change_lbl;
    
    @FXML
    private TextField amountTendered_tf;
    
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
    private void radioButtons(ActionEvent e)
    {
        if(pwd_rb.isSelected() || senior_rb.isSelected())
        {
            float discount = total * .20f;
            discount_lbl.setText(String.valueOf(discount));
            grandTotal_lbl.setText(String.valueOf(total-discount));
        }
        else
        {
            discount_lbl.setText("00.00");
            grandTotal_lbl.setText(String.valueOf(total));
        }
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
            pwd_rb.setSelected(false);
            senior_rb.setSelected(false);
            discount_lbl.setText("00.00");
            grandTotal_lbl.setText(String.valueOf(total));
        }
    }
    
    @FXML
    private void amountKeyType(KeyEvent e)
    {
        if(!amountTendered_tf.getText().isEmpty())
        {
            double amountTendered = Double.valueOf(amountTendered_tf.getText());
            double change = amountTendered - Double.valueOf(grandTotal_lbl.getText());
            change_lbl.setText(String.valueOf(change));
        }    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        total = Global.totalCost;
        float tax = Math.round((total  * .12)* 100) / 100.0f;
        
        total_lbl.setText(String.valueOf(total));
        grandTotal_lbl.setText(String.valueOf(total));
        tax_lbl.setText(String.valueOf(tax));
        amountTendered_tf.setText("0");
        
        amountTendered_tf.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if(!newValue.matches("\\d*")) {
                    amountTendered_tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
