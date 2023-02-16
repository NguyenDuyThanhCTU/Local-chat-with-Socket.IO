/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER;

import MYSQLSERVER.MySQLServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class Server {
    
    private Object lock;
    private ServerSocket server;
    private Socket socket;
    public static ArrayList<Handler> clients;
    private Account account;
    private MySQLServer database;
    
    public Server() throws IOException, ClassNotFoundException, SQLException{
        
        try {
            //Tao lock dung de synchronize 
            lock = new Object();

            clients = new ArrayList();

            //Tao ServerSocket voi cong 9999
            server = new ServerSocket(9999);

            database = new MySQLServer();
        
            while(true) {
                
                System.out.println("Bắt đầu");
                
                //Doi socket yeu cau tu phia client
                socket = server.accept();                

                System.out.println("Kết nối thành công");

                //Tao DataOutputStream de gui yeu cau di tu server
                DataOutputStream out =  new DataOutputStream(socket.getOutputStream());

                //Tao DataInputStream nhan cac yeu cau tu client
                DataInputStream in = new DataInputStream(socket.getInputStream());

                String request = in.readUTF();

                //Xu ly yeu cau dang ky tai khoan
                if(request.equals("Sign up")){

                    //Nhan ten dang nhap va password tu client
                    String username = in.readUTF();
                    String password = in.readUTF();
                    
                    //Tao mot accout de kiem tra va luu account
                    account = new Account(username, password);

                    if(account.CheckAccount()){
                        out.writeUTF("acc da ton tai");
                        out.flush();
                    }
                    else{

                        //Luu Accout cua clients nay
                        account.SaveAccount();

                        //Thong bao xuong Clients
                        out.writeUTF("signup thanh cong");
                        out.flush();

                        //Update Friend

                    }
                }
                else if(request.equals("Log in")) {
                    
                    //Nhan ten dang nhap va mat khau tu client
                    String username = in.readUTF();
                    String password = in.readUTF();
                    
                    System.out.println(password);
                    
                    //Tao Account de kiem tra tai khoan
                    account = new Account(username, password);
                    
                    if(account.CheckAccount()){
                        //Lay ma user cua account nay
                        int mauser = account.MaAccount();
                        
                        System.out.println(mauser);
                        
                        //Lay thong tin cua user tu database
                        database.Connection();
                        
                        String executeStr = "Select * from Users where MaUser = " + mauser + ";";
                        ResultSet rs = database.executeQuery(executeStr);
                        while(rs.next()){
                            if(CheckOnl(rs.getString("NameUser"))){
                                out.writeUTF("ton tai");
                                out.flush();
                            }
                            else{
                                //Kiem tra password cua account
                                if(account.CheckPwd()){
                                    //Tao Handler moi de giai quyet cac yeu cau cua user
                                    Handler newHandler = new Handler(socket, rs.getString("NameUser"), rs.getString("Avatar"), true, lock);
                                    clients.add(newHandler);
                                    
                                    //Thong bao dang nhap thanh cong cho user
                                    out.writeUTF("login thanh cong");
                                    out.flush();                                    
                                    
                                    //Gui profile tu database ve user
                                    out.writeUTF(rs.getString("MaUser"));
                                    out.flush();
                                    out.writeUTF(rs.getString("NameUser"));
                                    out.flush();
                                    if(rs.getBoolean("GioiTinh")){
                                        out.writeUTF("Nam");
                                        out.flush();
                                    }
                                    else{
                                        out.writeUTF("Nữ");
                                        out.flush();
                                    }
                                    out.writeUTF(rs.getString("NgaySinh"));
                                    out.flush();
                                    out.writeUTF(rs.getString("Avatar"));
                                    out.flush();   
                                    
                                    
                                    
                                    //Tao Thread de giao tiep voi user nay
                                    Thread t = new Thread(newHandler);
                                    t.start();
                                    
                                    
                                }
                                else{
                                    out.writeUTF("pass khong dung");
                                    out.flush();
                                }
                            }
                        }
                       
                    }
                    else{
                        out.writeUTF("login that bai");
                        out.flush();
                    }
                }
                
                
                System.out.println("Hết Vòng Lặp");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        finally{       
            if(server != null){
                server.close();
            }
        }           
    }        
    
    public Boolean CheckOnl(String name) {
        try {
            for(Handler client: clients) {
                if(client.getName().equals(name))
                    return true;
            }
        }catch(Exception e){
            return false; 
        }
        return false; 
    }    
    public void UpdateFriendOnl() {

    }  
}