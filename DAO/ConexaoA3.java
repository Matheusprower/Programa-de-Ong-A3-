package DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class ConexaoA3 {
    
    public Connection conectaDB() {
        Connection conn = null;
        
        try {
            String url = "jdbc:mysql://localhost:3306/a3psc?user=root&password=testea3psc123!";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro na classe de conexão - " + e.getMessage());
        }
        return conn;
    }
}
