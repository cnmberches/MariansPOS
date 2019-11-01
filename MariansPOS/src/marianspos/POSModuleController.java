package marianspos;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class POSModuleController implements Initializable {
    private final String[] columns = {"Category", "Name", "Description", "Price", "Special","Servings", "Status"};
    private ObservableList<ObservableList> tbl_data;
    private ObservableList<ObservableList> orders_data;

    @FXML
    private TextField quantity_tf,search_tf;
    
    @FXML
    private Label name_lbl, description_lbl, status_lbl, price_lbl,cashierName_lbl, userID_lbl;
    
    @FXML
    private Button decrease_btn, increase_btn, confirm_order, sinangagSpecial_btn, barkadaDeals_btn, extraOrders_btn, riceMeals_btn,
            specialties_btn, pancitBilao_btn, vegetables_btn, sandwich_btn, pulutan_btn, dessert_btn, merienda_btn, soup_btn, main_btn,
            all_btn, drinks_btn;
    
    @FXML
    private TableView menu_tbl;
    
    @FXML
    private TableView orders_tbl;

    @FXML
    public void clickItem(MouseEvent event) throws IOException
    {
        if(menu_tbl.getSelectionModel().selectedIndexProperty().intValue() != -1)
        {
            Global.isForAddMenu = false;
            Global.menuClickedItems = menu_tbl.getSelectionModel().selectedItemProperty()
                    .get().toString().replace('[', ' ').replace(']', ' ').split(", ");
            for(int i = 0; i < Global.menuClickedItems.length ; i++)
            {
                Global.menuClickedItems[i] = Global.menuClickedItems[i].trim();
            }

            name_lbl.setText(Global.menuClickedItems[2]);
            description_lbl.setText(Global.menuClickedItems[4]);
            price_lbl.setText(Global.menuClickedItems[3]);
            status_lbl.setText(Global.menuClickedItems[6]);
        }
    }
    
    @FXML
    public void voidItem(MouseEvent event) throws IOException
    {
        if(!orders_data.isEmpty())
        {
            openModule("VoidModule.fxml", Modality.APPLICATION_MODAL, "Void");
            if(Global.isVoid)
            {
                ObservableList<ObservableList> selectedOrder, allOrders;
                allOrders = orders_tbl.getItems();
                selectedOrder = orders_tbl.getSelectionModel().getSelectedItems();

                selectedOrder.forEach(allOrders::remove);
                Global.isVoid = false;
            }
        }
    }

    @FXML
    private void add(ActionEvent e)
    {
        String quantity = quantity_tf.getText();
        ObservableList<String> row = FXCollections.observableArrayList();
        row.add(name_lbl.getText());
        row.add(price_lbl.getText());
        row.add(quantity);
        int cost = Integer.valueOf(quantity) * Integer.valueOf(price_lbl.getText());
        row.add(String.valueOf(cost));
        if(cost > 0)
        {
            orders_tbl.getItems().add(row);
        }
        quantity_tf.setText("1");
        name_lbl.setText("Menu Name");
        price_lbl.setText("0");
        description_lbl.setText("Menu Description");
        Global.orders = orders_tbl.getItems();
    }
    
    @FXML
    private void increase(ActionEvent e)
    {
        int quantity = Integer.valueOf(quantity_tf.getText()) + 1;
        quantity_tf.setText(String.valueOf(quantity));
    }
    
    @FXML
    private void confirm(ActionEvent e) throws IOException
    {
        ObservableList orders = orders_tbl.getItems();
        int totalCosts = 0;
        for(int i = 0; i< orders.size();i++)
        {
            String [] temp = orders.get(i).toString().replace('[', ' ').replace(']', ' ').split(", ");
            totalCosts += Integer.valueOf(temp[temp.length-1].replaceAll(" ", ""));
        }
        if(totalCosts > 0)
        {
            Global.totalCost = totalCosts;
            openModule("MenuDialog.fxml", Modality.APPLICATION_MODAL,"Confirm");
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR, "There are no orders!" , ButtonType.OK);
            alert.setTitle("");
            //the show and wait functions waits the user to click between the buttons ok cancel
            alert.showAndWait();
        }
    }
    
    @FXML
    private void decrease(ActionEvent e)
    {
        int quantity = Integer.valueOf(quantity_tf.getText());
        if(quantity <= 1)
        {
            quantity_tf.setText(String.valueOf(quantity));
        }
        else
        {
            quantity--;
            quantity_tf.setText(String.valueOf(quantity));
        }
    }
    
    @FXML
    private void buttonLoadOrder(ActionEvent e)
    {
        if(e.getSource().equals(sinangagSpecial_btn))
        {
            loadMenu(sinangagSpecial_btn.getText());
        }
        else if(e.getSource().equals(barkadaDeals_btn))
        {
            loadMenu(barkadaDeals_btn.getText());
        }
        else if(e.getSource().equals(main_btn))
        {
            loadMenu(main_btn.getText());
        }
        else if(e.getSource().equals(extraOrders_btn))
        {
            loadMenu(extraOrders_btn.getText());
        }
        else if(e.getSource().equals(riceMeals_btn))
        {
            loadMenu(riceMeals_btn.getText());
        }
        else if(e.getSource().equals(specialties_btn))
        {
            loadMenu(specialties_btn.getText());
        }
        else if(e.getSource().equals(pancitBilao_btn))
        {
            loadMenu(pancitBilao_btn.getText());
        }
        else if(e.getSource().equals(vegetables_btn))
        {
            loadMenu(vegetables_btn.getText());
        }
        else if(e.getSource().equals(sandwich_btn))
        {
            loadMenu(sandwich_btn.getText());
        }
        else if(e.getSource().equals(pulutan_btn))
        {
            loadMenu(pulutan_btn.getText());
        }
        else if(e.getSource().equals(dessert_btn))
        {
            loadMenu(dessert_btn.getText());
        }
        else if(e.getSource().equals(merienda_btn))
        {
            loadMenu(merienda_btn.getText());
        }
        else if(e.getSource().equals(soup_btn))
        {
            loadMenu(soup_btn.getText());
        }
        else if(e.getSource().equals(drinks_btn))
        {
            loadMenu(drinks_btn.getText());
        }
        else if(e.getSource().equals(all_btn))
        {
            loadMenu("*");
        }
               
    }
    
    @FXML
    private void search(KeyEvent e)
    {
        searchMenu(search_tf.getText());  
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        cashierName_lbl.setText(Global.name);
        userID_lbl.setText(Global.account_id);
        loadMenu("*");
        String[] column_names = {"Name", "Price", "Quantity", "Cost"};
        for (int i = 0; i < column_names.length; i++) {
            final int j = i;
            TableColumn col = new TableColumn(column_names[i]);
            col.setStyle(" -fx-font-family: 'Roboto'; -fx-font-size: 14px;");
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
            {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
                {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });

            orders_tbl.getColumns().addAll(col);
        }
        orders_tbl.setItems(orders_data);
    }
    
    private void loadMenu(String categoryName)
    {
        DBConnector con = new DBConnector();
        try
        {
            tbl_data = FXCollections.observableArrayList();
            tbl_data.clear();
            menu_tbl.getColumns().clear();
            menu_tbl.getItems().clear();
            orders_data = FXCollections.observableArrayList();
            
            String SQL2;
            if(!categoryName.equalsIgnoreCase("*"))
            {
                SQL2 = "select category_name, menus_name, menus_description, menus_price ,menus_special, "
                + "menus_persons, menu_status from menus_tbl A inner join category_tbl S on A.category_id = S.category_id where"
                + " category_name = '" + categoryName + "'";
            }
            else
            {
                SQL2 = "select category_name, menus_name, menus_description, menus_price ,menus_special, "
                + "menus_persons, menu_status from menus_tbl A inner join category_tbl S on A.category_id = S.category_id";
            }
             
            //ResultSet
            ResultSet rs2 = con.getConnection().createStatement().executeQuery(SQL2);
            for (int i = 0; i < rs2.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(columns[i]);
                col.setStyle(" -fx-font-family: 'Roboto'; -fx-font-size: 14px;");
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
                {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
                    {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                menu_tbl.getColumns().addAll(col);
            }

            while (rs2.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs2.getString(i));
                }
                tbl_data.add(row);
            }
            menu_tbl.setItems(tbl_data);
            con.getConnection().close();
        }
        catch(SQLException e)
        {
        }
    }
    
     private void searchMenu(String wordToSearch)
    {
        DBConnector con = new DBConnector();
        try
        {
            tbl_data = FXCollections.observableArrayList();
            tbl_data.clear();
            menu_tbl.getColumns().clear();
            menu_tbl.getItems().clear();
            orders_data = FXCollections.observableArrayList();
            
            String SQL2 = "select category_name, menus_name, menus_description, menus_price ,menus_special, "
                + "menus_persons, menu_status from menus_tbl A inner join category_tbl S on A.category_id = S.category_id "
                    + "WHERE menus_name LIKE '%" + wordToSearch + "%'";
             
            //ResultSet
            ResultSet rs2 = con.getConnection().createStatement().executeQuery(SQL2);
            for (int i = 0; i < rs2.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(columns[i]);
                col.setStyle(" -fx-font-family: 'Roboto'; -fx-font-size: 14px;");
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
                {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
                    {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                menu_tbl.getColumns().addAll(col);
            }

            while (rs2.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs2.getString(i));
                }
                tbl_data.add(row);
            }
            menu_tbl.setItems(tbl_data);
            con.getConnection().close();
        }
        catch(SQLException e)
        {
        }
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
        stage.showAndWait();
    }
}
