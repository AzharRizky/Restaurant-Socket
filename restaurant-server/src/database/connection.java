package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {
    public static Connection connection;

    public static void setConnection(){
        try {
            //setting connection to databse
            String url = "jdbc:mysql://127.0.0.1:3306/plt?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String user = "root";
            String pass = "";
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = DriverManager.getConnection(url, user, pass);
        }
        catch (SQLException t){
            t.printStackTrace();
        }
    }
}

