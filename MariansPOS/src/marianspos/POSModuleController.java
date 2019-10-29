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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class POSModuleController implements Initializable {
    private final String[] columns = {"ID", "Category", "Menu Name", "Price", "Description","Special", "Status", "Servings"};

    private ObservableList<ObservableList> tbl_data;
    private ObservableList<ObservableList> orders_data;

    @FXML
    private TextField quantity_tf;
    
    @FXML
    private Label name_lbl, description_lbl, status_lbl, price_lbl;
    
    @FXML
    private Button decrease_btn, increase_btn, confirm_order;
    
    @FXML
    private TableView menu_tbl;
    
    @FXML
    private TableView orders_tbl;

    @FXML
    public void clickItem(MouseEvent event) throws IOException
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
        orders_tbl.getItems().add(row);
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
        Global.totalCost = totalCosts;
        //this function is for opening a new window where its parameter include the fxml file in string, 
        //how the window will open (dialog or not),and its title 
        //fxml loader is used to get the fxml file wherein it has the codes for the design of the window
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuDialog.fxml"));
        //this parent root is for loading all the codes for design
        Parent root1 = (Parent) fxmlLoader.load();
        //this stage is for creating the window
        Stage stage = new Stage();
        //this function is for how the window will open (window or dialog)
        stage.initModality(Modality.APPLICATION_MODAL);
        //this sets the title seen on the upper left of the window
        stage.setTitle("Confirm");
        //this function makes the window not resizable
        stage.setResizable(false);
        //this makes sure that size is equal to the size of window based on the code
        stage.sizeToScene();
        //this puts the fxml file design in the window
        stage.setScene(new Scene(root1));  
        //this makes the window viewable to the user
        stage.show();
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConnector con = new DBConnector();
        try
        {
            String SQL1 = "SELECT category_name from category_tbl";
            //ResultSet
            ResultSet rs1 = con.getConnection().createStatement().executeQuery(SQL1);
            orders_data = FXCollections.observableArrayList();
            
            tbl_data = FXCollections.observableArrayList();
            
            String SQL2 = "SELECT * from menus_tbl";
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

            while(rs1.next())
            {
                Global.category_names.add(rs1.getString(1));
            }

            while (rs2.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    if(i == 2)
                    {
                        row.add(Global.category_names.get(rs2.getInt(i)));
                    }
                    else
                    {
                        row.add(rs2.getString(i));
                    }
                }
                tbl_data.add(row);
            }
            menu_tbl.setItems(tbl_data);
            
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
        catch(SQLException e)
        {
        }
    }

}
