package marianspos;

import com.sun.javafx.print.Units;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MenuDialogController implements Initializable {   
    private int total;
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Button cancel_btn, add_btn;
    
    @FXML
    private ToggleButton discount_tb; 
    
    @FXML
    private ToggleGroup status_tg;
    
    @FXML
    private RadioButton senior_rb, pwd_rb, dineIn_rb, takeOut_rb;
    
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
        //cheks if the discount button is selected
        if(pwd_rb.isSelected() || senior_rb.isSelected())
        {
            float discount = total * .20f;
            //this willl set the value of discount
            discount_lbl.setText(String.valueOf(discount));
            //automatically minus the discount to total
            grandTotal_lbl.setText(String.valueOf(total-discount));
        }
        else
        {
            //set the discount to 0 and grand total to the original value
            discount_lbl.setText("00.00");
            grandTotal_lbl.setText(String.valueOf(total));
        }
    }
    
    @FXML
    private void order(ActionEvent e)
    {
        if(!amountTendered_tf.getText().isEmpty() && Integer.parseInt(amountTendered_tf.getText()) != 0 && Double.parseDouble(change_lbl.getText()) > 0)
        {
            Calendar now = Calendar.getInstance();
            DBConnector db = new DBConnector();
            String status = "";
            if(status_tg.getSelectedToggle().equals(dineIn_rb))
            {
                status = "Dine In";
            }
            else
            {
                status = "Take Out";
            }
            try
            {
                TextArea receiptLayout = new TextArea();
                receiptLayout.setPrefWidth(450);
                receiptLayout.setStyle("-fx-font-size: 6; -fx-font-family: monospace;");
                receiptLayout.appendText("Marian's Pares, Bulalo, atbp.\n");
                receiptLayout.appendText("========================================================\n");
                //first is to get a connection and create a statement
                Statement st = db.getConnection().createStatement();
                //this query is for inserting the values name, username, password, role, and date_hired
                //it uses "?" in the values for preparedstatement
                String sql = "INSERT INTO transactions_tbl(accounts_id, orders, date_ordered, transactions_status) "
                            + "VALUES(?, ?, ?, ?)";


                ObservableList <String> transaction_id = FXCollections.observableArrayList();
                String orders = "";

                for(ObservableList<ObservableList> order: Global.orders)
                {
                    String arrOrder[] = order.subList(0,4).toString().replace('[', ' ').replace(']', ' ').split(", ");
                    orders = order.subList(0,4).toString().replace('[', ' ').replace(']', ' ').replaceAll(",", "")
                            + status + "\n";
                    receiptLayout.appendText(String.format("%-30s\n", arrOrder[0].trim()));
                    receiptLayout.appendText(String.format("%-5s*%-10s|%-6s|\n", arrOrder[1].trim() + ".00", arrOrder[2].trim() ,arrOrder[3].trim()+ ".00"));
                }
                receiptLayout.appendText("\n");
                receiptLayout.appendText(String.format("%30s:%-6s\n", "Tax" ,tax_lbl.getText()));
                receiptLayout.appendText(String.format("%30s:%-6s\n", "Discount" ,discount_lbl.getText()));
                receiptLayout.appendText(String.format("%30s:%-6s\n", "Total Cost" ,grandTotal_lbl.getText()));
                receiptLayout.appendText(String.format("%30s:%-6s\n", "Cash" ,amountTendered_tf.getText()));
                receiptLayout.appendText(String.format("%30s:%-6s\n", "Change" ,change_lbl.getText()));

                //first is to get the connection then prepare the statement query
                //name.price,quantity,cost
                PreparedStatement ps = db.getConnection().prepareStatement(sql);
                //this inserts the data by index and its corresponding value
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(now.getTime());
                ps.setString(1, Global.account_id);
                ps.setString(2, orders);
                ps.setString(3, date);
                ps.setInt(4, 0);
                //this function is for commanding the system to do the query which inserts a new row/data in database
                ps.executeUpdate();

                ResultSet rs2 = db.getConnection().createStatement().executeQuery("SELECT transactions_id FROM transactions_tbl");

                int id = 0;
                while(rs2.next())
                {
                    id = rs2.getInt("transactions_id");
                }
                db.getConnection().close();

                transaction_id.add(String.valueOf(id) + "-" + date);
                POSSecondModuleController.preparing_data.add(transaction_id);

                //this function is use to get the source file of the action event
                final Node source = (Node) e.getSource();
                //this gets the sctive stage or window of the file
                final Stage stage = (Stage) source.getScene().getWindow();
                anchorPane.getChildren().add(receiptLayout);
                printOperation(receiptLayout);

                //this is for closing the window
                stage.close();

            } catch (SQLException ex) {
                //this prints the error message if it encounters problem
                ex.printStackTrace();
            }  
        }
        
    }
    
    static void printOperation(TextArea textDocument) {
        Text extractedText = new Text(textDocument.textProperty().get());
        extractedText.prefWidth(450);
        extractedText.setStyle("-fx-font-size: 6; -fx-font-family: monospace;");

        // use pane to place the text
        StackPane container = new StackPane(extractedText);
        container.setAlignment(Pos.TOP_LEFT);

        try
        {
            Constructor<Paper> c = Paper.class.getDeclaredConstructor(String.class,
                                         double.class, double.class, Units.class);
            c.setAccessible(true);
            Paper photo = c.newInstance("55x210", 55, 210, Units.MM);
            
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            Printer printer = javafx.print.Printer.getDefaultPrinter();
            PageLayout pageLayout = printer.createPageLayout(photo,  PageOrientation.PORTRAIT, 0.0f, 0.0f, 8.0f, 8.0f);
            printerJob.getJobSettings().setPageLayout(pageLayout);
            if (printerJob != null) {

                if (printerJob.printPage(container)) {
                    printerJob.endJob();
                } else {
                    System.out.println("Failed to print");
                }
            } else {
                System.out.println("Canceled");
            }
        }
        catch(Exception e)
        {
            
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
            discount_lbl.setText("0");
            grandTotal_lbl.setText(String.valueOf(total));
        }
    }
    
    @FXML
    private void amountKeyType(KeyEvent e)
    {
        if(!amountTendered_tf.getText().isEmpty())
        {
            double amountTendered = Double.valueOf(amountTendered_tf.getText());
            double change = Math.round((amountTendered - Double.valueOf(grandTotal_lbl.getText()))* 100) / 100.0f;
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
