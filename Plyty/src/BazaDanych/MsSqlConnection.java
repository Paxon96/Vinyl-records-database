package BazaDanych;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MsSqlConnection  {

    public Connection dbConnect() throws Exception{
        try {

            String db_connect_string = "bazaplyty.database.windows.net";//jdbc:sqlserver://Bartek-Komputer;databasename=BazaPłyt";
            String database = "BazaPłyty";
            String db_userid = "Bartek";
            String db_password = "";
            String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", db_connect_string, database, db_userid, db_password);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection(url);

            /*String db_connect_string = "jdbc:sqlserver://KOMP;databasename=BazaPłyt";
            String db_userid = "Bartek";
            String db_password = "123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection(db_connect_string,
                    db_userid, db_password);*/

            System.out.println("connected");

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Błąd połączenia!");
            throw new Exception();
        }
    }

}
/*
           String db_connect_string = "bazaplyty.database.windows.net";//jdbc:sqlserver://Bartek-Komputer;databasename=BazaPłyt";
            String database = "BazaPłyty";
            String db_userid = "Bartek";
            String db_password = "Baza_plyty200396";
            String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;", db_connect_string, database, db_userid, db_password);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            Connection conn = DriverManager.getConnection(url);
 */