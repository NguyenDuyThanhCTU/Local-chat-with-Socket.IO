/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER;

import MYSQLSERVER.MySQLServer;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author ADMIN
 */
public class Account {
    
    private String username;
    private String password;
    private MySQLServer database;
    
    public Account(String username, String password){
        this.username = username;
        this.password = password;
        this.database = new MySQLServer();
    }
    
    public Boolean CheckAccount() throws ClassNotFoundException, SQLException{
        
        System.out.println(username);
        
        
        try{

            database.Connection();
        
            String executeStr = "Select * from Account;";
            ResultSet rs = database.executeQuery(executeStr);
            while(rs.next()){
                if(rs.getString("UserName").equals(username)){
                    database.Close();
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        database.Close();
        return false;
        
    }
    
    public Boolean CheckPwd() {
        
        try {
            database.Connection();
            String executeStr = "Select PassWord from Account where UserName = '" + username + "'";
            ResultSet rs = database.executeQuery(executeStr);
            while(rs.next()){
                if(rs.getString("PassWord").equals(password)){
                    database.Close();
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void SaveAccount() throws ClassNotFoundException, SQLException {
        
        int mauser = 0;
        
        try{
            database.Connection();
        
            String executeStr1 = "Select count(MaUser) from Account;";
            ResultSet rs = database.executeQuery(executeStr1);
            while(rs.next()){
                mauser = rs.getInt(1);     
            }

            mauser++;

            String executeStr2 = "Insert into Account values (" + mauser
                    + ", '" + username + "', '" + password + "');";
            database.executeNonQuery(executeStr2);

            database.Close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        
        
    }
    
    public int MaAccount() throws ClassNotFoundException, SQLException {
        
        int mauser = 0;
        
        try {
            database.Connection();
            
            String executeStr = "Select MaUser from Account where UserName = '" + username + "';" ;
            ResultSet rs = database.executeQuery(executeStr);
            while(rs.next()) {
                mauser = rs.getInt("MaUser");
            }
            
            database.Close();
            
        }catch(Exception e) {
            e.printStackTrace();
        }

        return mauser;
    }
    
}
