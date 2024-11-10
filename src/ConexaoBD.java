import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    public static Connection getConnection(String url, String user, String bd, String password) throws SQLException{
        URL = "jdbc:sqlserver://"+url+":1433;"+"databaseName="+ bd +";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        USER = user;
        PASSWORD = password;
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}