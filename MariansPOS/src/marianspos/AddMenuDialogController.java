package marianspos;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class AddMenuDialogController implements Initializable {
    private final DBConnector con = new DBConnector();
    String category, special_menu, available;
    boolean special = false;
    
    @FXML
    private ToggleButton available_tb, special_tb; 
    
    @FXML
    private ComboBox category_cb;
    
    @FXML 
    private TextField name_tf, price_tf, persons_tf;
    
    @FXML
    private Button add_btn;
    
    @FXML
    private TextArea description_ta;
    
    @FXML
    private void special_btn_action(ActionEvent e)
    {
        if(special)
        {
            special = false;
            persons_tf.setDisable(true);
            persons_tf.setText("1");
        }
        else
        {
            special = true;
            persons_tf.setDisable(false);
        }
    }
    
    @FXML
    private void add(ActionEvent e)
    {
        special_menu = "No";
        available = "Not Available";
        if(special_tb.isSelected())
        {
            special_menu = "Yes";
        }
        
        if(available_tb.isSelected())
        {
            available = "Available";
        }
        
        category = category_cb.getEditor().getText();
        if(Global.isForAddMenu)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Add this menu?" , ButtonType.CANCEL, ButtonType.OK);
            alert.setTitle("Add Menu");
            //the show and wait functions waits the user to click between the buttons ok cancel
            alert.showAndWait();
            
            if(alert.getResult().equals(ButtonType.OK))
            {
                if(Global.category_names.contains(category))
                {
                    insert_row();
                }
                else
                {
                    Global.category_names.add(category);
                    try
                    {
                        PreparedStatement ps = con.getConnection().prepareStatement(
                                "INSERT INTO Category_tbl (category_name) VALUES (?)");
                        ps.setInt(1, Global.category_names.indexOf(category)+1);
                        ps.execute();
                        insert_row();
                    }
                    catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Menu Added", ButtonType.OK);
                alert1.setTitle("Added");
                //the show and wait functions waits the user to click between the buttons ok cancel
                alert1.showAndWait();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update this menu?" , ButtonType.CANCEL, ButtonType.OK);
            alert.setTitle("Update Menu");
            //the show and wait functions waits the user to click between the buttons ok cancel
            alert.showAndWait();
            if(alert.getResult().equals(ButtonType.OK))
            {
                try
                {
                    PreparedStatement ps = con.getConnection().prepareStatement(
                            "UPDATE MENUS_TBL SET category_id = ?, menus_name = ?, "
                        + "menus_price = ?, menus_description = ?, menus_special = ?, "
                        + "menu_status = ?, menus_persons = ? where menus_id = ?");
                    ps.setInt(1, Global.category_names.indexOf(category)+1);
                    ps.setString(2, name_tf.getText());
                    ps.setString(3, price_tf.getText());
                    ps.setString(4, description_ta.getText());
                    ps.setString(5, special_menu);
                    ps.setString(6, available);
                    ps.setInt(7, Integer.parseInt(persons_tf.getText()));
                    ps.setInt(8, Integer.parseInt(Global.inventoryClickedItems[0]));
                    ps.execute();
                    con.getConnection().close();
                    
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION, "Menu Updated", ButtonType.OK);
                    alert1.setTitle("Updated");
                    //the show and wait functions waits the user to click between the buttons ok cancel
                    alert1.showAndWait();
                }
                catch(NumberFormatException | SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void insert_row()
    {
        try
        {
            PreparedStatement ps = con.getConnection().prepareStatement(
                    "INSERT INTO MENUS_TBL (category_id, menus_name, "
                + "menus_price, menus_description, menus_special, "
                + "menu_status, menus_persons) VALUES (?, ?, ?, ?, ?, ? ,?)");
            ps.setInt(1, Global.category_names.indexOf(category)+1);
            ps.setString(2, name_tf.getText().replaceAll("//s", ""));
            ps.setInt(3, Integer.parseInt(price_tf.getText()));
            ps.setString(4, description_ta.getText());
            ps.setString(5, special_menu);
            ps.setString(6, available);
            ps.setInt(7, Integer.parseInt(persons_tf.getText()));
            ps.execute();
            con.getConnection().close();
        }
        catch(NumberFormatException | SQLException ex)
        {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for(int i = 0; i < Global.category_names.size(); i++)
        {
            options.add(Global.category_names.get(i));
        }
        category_cb.setItems(options);
        
        if(!Global.isForAddMenu)
        {
            category_cb.getEditor().setText(Global.inventoryClickedItems[1]);
            name_tf.setText(Global.inventoryClickedItems[2]);
            price_tf.setText(Global.inventoryClickedItems[3]);
            description_ta.setText(Global.inventoryClickedItems[4]);
            if(Global.inventoryClickedItems[5].equals(("Yes")))
            {
                special_tb.fire();
            }
            
            if(Global.inventoryClickedItems[6].equals("Available"))
            {
                available_tb.fire();
            }
            
            persons_tf.setText(Global.inventoryClickedItems[7]);
            
            add_btn.setText("Update");
        }
    }    
    
}
