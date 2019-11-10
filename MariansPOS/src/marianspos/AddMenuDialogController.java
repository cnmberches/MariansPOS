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
        //this function will check if the user wants the menu to be special.
        //it allows to have more persons per menu
        if(special)
        {
            //this will make the boolean special false if it is true.
            //it will not allow to change the quantity or number of servings per menu
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
        //this will first create a string for the menu if it is special and available
        special_menu = "No";
        available = "Not Available";
        if(special_tb.isSelected())
        {
            //if special is selected then store yes
            special_menu = "Yes";
        }
        
        if(available_tb.isSelected())
        {
            //if the user clicked available, then store available
            available = "Available";
        }
        
        //this will get the text from the combo box of catefories
        category = category_cb.getEditor().getText();
        
        if(Global.isForAddMenu)
        {
            //if the user opens the menu for adding account do the following
            //ask or confirm the user to add the menu
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Add this menu?" , ButtonType.CANCEL, ButtonType.OK);
            alert.setTitle("Add Menu");
            //the show and wait functions waits the user to click between the buttons ok cancel
            alert.showAndWait();
            
            if(alert.getResult().equals(ButtonType.OK))
            {
                //this will check if the catefory names has the category
                if(Global.category_names.contains(category))
                {
                    insert_row();
                }
                else
                {
                    //if not, it will add the category to the database
                    Global.category_names.add(category);
                    try
                    {
                        //get the connection and prepare the database
                        PreparedStatement ps = con.getConnection().prepareStatement(
                                "INSERT INTO Category_tbl (category_name) VALUES (?)");
                        //input the index of category
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
            //this is for the user if it will check if the user wanted to edit or update the account
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Update this menu?" , ButtonType.CANCEL, ButtonType.OK);
            alert.setTitle("Update Menu");
            //the show and wait functions waits the user to click between the buttons ok cancel
            alert.showAndWait();
            if(alert.getResult().equals(ButtonType.OK))
            {
                try
                {
                    //prepare the database
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
                    //execute or start aupdating the database
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
        //this is for the inserting the data to the menus database
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
    public void initialize(URL url, ResourceBundle rb)
    {
        //at the start of the program, we will put or prepare the data of the table menus
        ObservableList<String> options = FXCollections.observableArrayList();
        //this function is for getting the categories and displaying it on the combo box
        for(int i = 0; i < Global.category_names.size(); i++)
        {
            options.add(Global.category_names.get(i));
        }
        category_cb.setItems(options);
        
        if(!Global.isForAddMenu)
        {
            //this will set the text to the items that were get from the accounts table
            category_cb.getEditor().setText(Global.inventoryClickedItems[1]);
            name_tf.setText(Global.inventoryClickedItems[2]);
            price_tf.setText(Global.inventoryClickedItems[3]);
            description_ta.setText(Global.inventoryClickedItems[4]);
            if(Global.inventoryClickedItems[5].equals(("Yes")))
            {
                //if the menu is special, set the special toggle button selected
                special_tb.fire();
            }
            
            if(Global.inventoryClickedItems[6].equals("Available"))
            {
                //if the menu is available, set the available toggle button selected
                available_tb.fire();
            }
            //set the quantity of servings per menu
            persons_tf.setText(Global.inventoryClickedItems[7]);
            //change the text of button to update
            add_btn.setText("Update");
        }
    }    
}
