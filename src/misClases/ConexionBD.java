
package misClases;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ConexionBD {
    public static java.sql.Connection conectar() {
        try {
            String url = "jdbc:mysql://localhost:3306/adivinaquien";
            String user = "root";
            String password = "";
            java.sql.Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
