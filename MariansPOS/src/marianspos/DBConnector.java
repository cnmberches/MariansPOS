package marianspos;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector
{
    Connection conn = null;
    
    public Connection getConnection()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/marianspos","root", "");
            System.out.print("Database is connected !");
            conn.close();
        }
        catch(ClassNotFoundException | SQLException e)
        {
        }       
        return conn;
    }
}
