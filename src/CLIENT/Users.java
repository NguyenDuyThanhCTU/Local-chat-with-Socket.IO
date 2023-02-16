/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CLIENT;

import MYSQLSERVER.MySQLServer;
import java.sql.SQLException;
import java.sql.ResultSet;
/**
 *
 * @author ADMIN
 */
public class Users {
    
    private String name;
    private String avatar;
    private int gioitinh;
    private String ngaysinh;
    private MySQLServer database;
    
    public Users(String name, int gioitinh, String ngaysinh, String avatar) {
        this.name = name;
        this.avatar = avatar;
        this.gioitinh = gioitinh;
        this.ngaysinh = ngaysinh;
        database = new MySQLServer();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAvatar() {
        return this.avatar;
    }
    
    public int getGioiTinh() {
        return this.gioitinh;
    }
    
    public String getNgaySinh() {
        return this.ngaysinh;
    }
    
    public int getMaUser() {
        
        int mauser = 0;
        
        try {
            
            database.Connection();
            
            String executeStr = "Select MaUser from Users where NameUser = '" + name + "';";
            ResultSet rs1 = database.executeQuery(executeStr);
            while(rs1.next()){
                mauser = rs1.getInt(1);
            }
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return mauser;
    }
    
    public void SaveUser() throws ClassNotFoundException, SQLException{
        
        int mauser = 0;
        try {
            
            database.Connection();
            
            String executeStr1 = "Select count(MaUser) from Users;";
            ResultSet rs1 = database.executeQuery(executeStr1);
            while(rs1.next()){
                mauser = rs1.getInt(1);
            }
            
            mauser++;
            
            String executeStr2 = "Insert into Users values (" + mauser +
                            ", '" + name + "', " + gioitinh + ", '" +
                    ngaysinh + "', '" + avatar + "');";
            database.executeNonQuery(executeStr2);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void UpdateUser(String trupdate, String giatri, int vitri) throws ClassNotFoundException, SQLException{
        
        int giatriupdate;
        String executeStr;
        
        try {
            
            database.Connection();
            if(trupdate.equals("GioiTinh") || trupdate.equals("MaUser")){
                giatriupdate = Integer.parseInt(giatri);
                executeStr = "Update Users set " + trupdate +
                            " = " + giatriupdate + " where MaUser = " + vitri + ";";
            }else{
                executeStr = "Update Users set " + trupdate +
                            " = " + giatri + " where MaUser = " + vitri + ";";
            }
            
            
            database.executeNonQuery(executeStr);
            
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
