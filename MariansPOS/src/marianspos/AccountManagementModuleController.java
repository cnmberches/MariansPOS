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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AccountManagementModuleController implements Initializable {
    private String[] columns = {"ID", "Name", "Username", "Role", "Date Hired"};

    private ObservableList<ObservableList> tbl_data;
    
    @FXML
    private TableView acc_tbl;
    
    @FXML
    private void addAccount(ActionEvent event) throws IOException
    {
        //Open register module
        openModule("Register.fxml", Modality.APPLICATION_MODAL, "Register");
    }
           
     @FXML
    public void clickItem(MouseEvent event) throws IOException
    {
        if(acc_tbl.getSelectionModel().selectedIndexProperty().intValue() != -1)
        {
            Global.isForAddAccount = false;
            Global.accMenuClickedItems = acc_tbl.getSelectionModel().selectedItemProperty()
                    .get().toString().replace('[', ' ').replace(']', ' ').split(", ");
            for(int i = 0; i < Global.accMenuClickedItems.length ; i++)
            {
                Global.accMenuClickedItems[i] = Global.accMenuClickedItems[i].trim();
            }
            //Open register module
            openModule("Register.fxml", Modality.APPLICATION_MODAL, "Register");
            setItems();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setItems();
    }    
    
    private void setItems()
    {
        DBConnector con = new DBConnector();
        try
        {
            tbl_data = FXCollections.observableArrayList();
            tbl_data = FXCollections.observableArrayList();
            tbl_data.clear();
            acc_tbl.getColumns().clear();
            acc_tbl.getItems().clear();
            String SQL2 = "SELECT accounts_id, name, username, role, date_hired from accounts_tbl";
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
                acc_tbl.getColumns().addAll(col);
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
            acc_tbl.setItems(tbl_data);
            acc_tbl.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY);
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
        stage.show();
        if(modal.equals(Modality.WINDOW_MODAL))
        {
            //this if statement is to check if the window is showned not as a dialog
            //if it is WINDOW_MODAL, the main menu or log in module will close from the screen
            MariansPOS.stage.close();
        }
    }
}
