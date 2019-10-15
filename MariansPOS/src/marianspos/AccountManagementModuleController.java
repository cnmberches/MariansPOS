package marianspos;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class AccountManagementModuleController implements Initializable {
    private String[] columns = {"ID", "Name", "Username", "Role", "Date Hired"};

    private ObservableList<ObservableList> tbl_data;
    
    @FXML
    private TableView acc_tbl;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DBConnector con = new DBConnector();
        try
        {
            tbl_data = FXCollections.observableArrayList();
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
    
}
