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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class POSModuleController implements Initializable {
    private String[] columns = {"ID", "Category", "Menu Name", "Price", "Description","Special", "Status", "Servings"};

    private ObservableList<ObservableList> tbl_data;
    
    @FXML
    private TableView menu_tbl;
    
    @FXML
    public void clickItem(MouseEvent event) throws IOException
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            Global.isForAddMenu = false;
            Global.inventoryClickedItems = menu_tbl.getSelectionModel().selectedItemProperty()
                    .get().toString().replace('[', ' ').replace(']', ' ').split(", ");
            for(int i = 0; i < Global.inventoryClickedItems.length ; i++)
            {
                Global.inventoryClickedItems[i] = Global.inventoryClickedItems[i].trim();
            }
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
            stage.setTitle("Edit");
            //this function makes the window not resizable
            stage.setResizable(false);
            //this makes sure that size is equal to the size of window based on the code
            stage.sizeToScene();
            //this puts the fxml file design in the window
            stage.setScene(new Scene(root1));  
            //this makes the window viewable to the user
            stage.show();
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
        }
        catch(SQLException e)
        {
        }
    }    
    
}
