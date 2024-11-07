import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    public ConexaoBD(String url, String user, String pass){
        URL = "jdbc:sqlserver://"+url+":1433;"+"databaseName="+ user +";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        USER = user;
        PASSWORD = pass;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}