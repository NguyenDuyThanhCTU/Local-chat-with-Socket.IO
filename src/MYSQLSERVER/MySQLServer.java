/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MYSQLSERVER;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author ADMIN
 */
public class MySQLServer {
    
//    private Connection conn;
//    private Statement s;
//    private ResultSet rs;
//    
//    public void Connection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        String dbUrl = "jdbc:sqlserver://localhost:1433;databasename=ChatBox;user=sa;password=9WTbVqk#TE";
//        conn = DriverManager.getConnection(dbUrl);
//    }
//    
//    public ResultSet executeQuery(String executeStr) throws SQLException{
//        s = conn.createStatement();
//        rs = s.executeQuery(executeStr);
//        
//        return rs;
//    }
//    
//    public void executeNonQuery(String executeStr) throws SQLException{
//        s = conn.createStatement();
//        s.execute(executeStr);
//    }
//    
//    public void Close() throws SQLException{
//        
//        conn.close();
//        
//    }
    
    private Connection conn;
    
    public MySQLServer(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=ChatBox;user=sa;password=9WTbVqk#TE");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new MySQLServer();
    }
    

}
