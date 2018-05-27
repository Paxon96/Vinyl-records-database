package BazaDanych;

import java.sql.Connection;
import java.sql.DriverManager;



public class MsSqlConnection  {

    public Connection dbConnect(){
        try {
            String db_connect_string = "jdbc:sqlserver://Bartek-Komputer;databasename=BazaPłyt";
            String db_userid = "Bartek";
            String db_password = "123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection(db_connect_string,
                    db_userid, db_password);
            System.out.println("connected");

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Błąd połączenia!");
        }
        return null;
    }

}
