package marianspos;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class POSSecondModuleController implements Initializable {
    private ObservableList<ObservableList> serving_data = FXCollections.observableArrayList();
    public static ObservableList<ObservableList> preparing_data = FXCollections.observableArrayList();
    int clickCounter = 0;
    int clickCounter1 = 0;
    
    @FXML
    private TableView preparing_tbl, serving_tbl;
    
    @FXML
    public void serveAdd(MouseEvent event) throws IOException
    {
        clickCounter++;
        if(clickCounter++ >2)
        {
            if(preparing_tbl.getSelectionModel().selectedIndexProperty().intValue() != -1)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Serve this?" , ButtonType.CANCEL, ButtonType.OK);
                alert.setTitle("");
                //the show and wait functions waits the user to click between the buttons ok cancel
                alert.showAndWait();

                if(alert.getResult() == ButtonType.OK)
                {
                    String itemClick = preparing_tbl.getItems().subList(0, 1).toString().replace('[', ' ').replace(']', ' ');
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(itemClick);
                    serving_tbl.getItems().add(row);

                    ObservableList<ObservableList> selectedOrder, allOrders;
                    allOrders = preparing_tbl.getItems();
                    selectedOrder = preparing_tbl.getSelectionModel().getSelectedItems();

                    selectedOrder.forEach(allOrders::remove);
                }
            }
            clickCounter = 0;
        }
    }
    
    @FXML
    public void update(MouseEvent event) throws IOException
    {
        clickCounter1++;
        if(clickCounter1++ > 2)
        {
            if(serving_tbl.getSelectionModel().selectedIndexProperty().intValue() != -1)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove this?" , ButtonType.CANCEL, ButtonType.OK);
                alert.setTitle("");
                //the show and wait functions waits the user to click between the buttons ok cancel
                alert.showAndWait();

                if(alert.getResult() == ButtonType.OK)
                {
                    String []itemClick = serving_tbl.getItems().subList(0, 1).toString().replace('[', ' ').replace(']', ' ').split("-");
                    try
                    {
                        DBConnector db = new DBConnector();
                        //first is to get a connection and create a statement
                        Statement st = db.getConnection().createStatement();
                        //this query is for inserting the values name, username, password, role, and date_hired
                        //it uses "?" in the values for preparedstatement
                        String sql = "UPDATE transactions_tbl SET transactions_status = 1 WHERE transactions_id = ?";

                        //prepared statement is used instead of statement to prevent sql injection
                        //first is to get the connection then prepaere the statement query
                        //name.price,quantity,cost
                        PreparedStatement ps = db.getConnection().prepareStatement(sql);
                        //this inserts the data by index and its corresponding value
                        ps.setInt(1, Integer.parseInt(itemClick[0].trim()));
                        //this function is for commanding the system to do the query which inserts a new row/data in database
                        ps.executeUpdate();

                        db.getConnection().close();

                    } catch (SQLException ex) {
                        //this prints the error message if it encounters problem
                        ex.printStackTrace();
                    }
                    ObservableList<ObservableList> selectedOrder, allOrders;
                    allOrders = serving_tbl.getItems();
                    selectedOrder = serving_tbl.getSelectionModel().getSelectedItems();

                    selectedOrder.forEach(allOrders::remove);
                }
            }
            clickCounter1 = 0;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn col = new TableColumn("Transaction Number");
        col.setStyle(" -fx-font-family: 'Roboto'; -fx-font-size: 14px;");
        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
        {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
            {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        preparing_tbl.getColumns().addAll(col);
 
        TableColumn col2 = new TableColumn("Transaction Number");
        col2.setStyle(" -fx-font-family: 'Roboto'; -fx-font-size: 14px;");
        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>()
        {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param)
            {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        serving_tbl.getColumns().addAll(col2);
        serving_tbl.setItems(serving_data);
        preparing_tbl.setItems(preparing_data);
    }    
    
}
