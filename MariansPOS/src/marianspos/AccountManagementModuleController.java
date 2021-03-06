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
        //this function is for editing an account when the user clicks the cell
        //the condition checks first if the clicked cell is not the header
        if(acc_tbl.getSelectionModel().selectedIndexProperty().intValue() != -1)
        {
            //the global variable is for the condition in opening the register module, we will be
            //setting it to false because we will just edit the account
            Global.isForAddAccount = false;
            //this global array variable is for the items clicked.
            //this will get the id, name, username, password, role, and the date hired.
            //from the table, it will get a text formatted like [id, name, username, password, role, date hired]
            //It will first replace all the brackets with space and split it using ', '
            //then it will be saved as an array
            Global.accMenuClickedItems = acc_tbl.getSelectionModel().selectedItemProperty()
                    .get().toString().replace('[', ' ').replace(']', ' ').split(", ");
            for(int i = 0; i < Global.accMenuClickedItems.length ; i++)
            {
                //this function is to trim or remove all the spaces at the start and end
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
        //first is to connect to the database
        DBConnector con = new DBConnector();
        try
        {
            //instantiate or create a obsevable list
            tbl_data = FXCollections.observableArrayList();
            //it will clear first the items in the observable list
            tbl_data.clear();
            //remove all columns and items
            acc_tbl.getColumns().clear();
            acc_tbl.getItems().clear();
            //this string is for getting all the accounts saved in the database
            String SQL2 = "SELECT accounts_id, name, username, role, date_hired from accounts_tbl";
            //ResultSet for executing the query
            ResultSet rs2 = con.getConnection().createStatement().executeQuery(SQL2);
            //this will create columns
            for (int i = 0; i < rs2.getMetaData().getColumnCount(); i++)
            {
                final int j = i;
                //create a column
                TableColumn col = new TableColumn(columns[i]);
                col.setStyle(" -fx-font-family: 'Roboto'; -fx-font-size: 14px;");
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
                {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
                    {
                        //this is for the table header of the table
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                //add the column created
                acc_tbl.getColumns().addAll(col);
            }
            
            //this function is to get all the data from teh database
            while (rs2.next()) {
                //Iterate Row
                //create a observable list row.
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                    //Iterate Column and add the data to observable list
                    row.add(rs2.getString(i));
                }
                //add the created observable list to the data
                tbl_data.add(row);
            }
            //this is for putting all the data to the table
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
    }
}
