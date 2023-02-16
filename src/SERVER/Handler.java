/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author ADMIN
 */
public class Handler implements Runnable {
    
    private Object lock;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String name;
    private String avatar;
    private Boolean online;
    
    public Handler(Socket socket, String name, String avatar, Boolean online, Object lock) throws IOException {
        this.socket = socket;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
        this.lock = lock;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getAvatar() {
        return this.avatar;
    }
    
    public Boolean getOnline() {
        return this.online;
    }
    
    public void closeSocket() {
        if(socket != null){
            try {
                socket.close();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public DataOutputStream getOut() {
        return this.out;
    }
    
    @Override
    public void run() {
        
        while(true) {
            
            try {
                String request = null;
                
                //Doc yeu caiu tu user
                request = in.readUTF();
                
                //Yeu cau dang xuat tu user
                if(request.equals("Log out")){
                    //Thong bao user da dang xuat
                    out.writeUTF("da dang xuat");
                    out.flush();
  
                    //Chuyen trang thai offline va xoa khoi clients
                    this.online  = false;
                    for(Handler client: Server.clients){
                        if(client.getName().equals(name))
                            Server.clients.remove(Server.clients.indexOf(client));
                        break;
                    }
                    
                    socket.close();
                    
                }
                
                //Xu ly yeu cau gui tin nhan van ban
                else if(request.equals("ChatWithFriend")) {
                    
                    //Nhan tin nhan tu user
                    String message = null;
                    message = in.readUTF();

                    //Tin nhan dang van ban
                    if(message.equals("Text")){
                        
                        //Nhan ten Friend va tin nhan
                        String nameFriend = in.readUTF();
                        String content = in.readUTF();
                                               
                        for(Handler client: Server.clients){
                            if(client.getName().equals(nameFriend)){
                                synchronized(lock){
                                    client.getOut().writeUTF("Text");
                                    client.getOut().writeUTF(this.avatar);
                                    client.getOut().writeUTF(content);
                                    client.getOut().writeUTF(this.name);
                                    client.getOut().flush();
                                    break;
                                }
                            }
                        }
                    }
                    
                    //Tin nhan dang emojis
                    else if(message.equals("Emojis")){
                        
                        //Nhan ten Friend va tin nhan
                        String nameFriend = in.readUTF();
                        String content = in.readUTF();
                        
                        for(Handler client: Server.clients){
                            if(client.getName().equals(nameFriend)){
                                synchronized(lock){
                                    client.getOut().writeUTF("Emojis");
                                    client.getOut().writeUTF(this.avatar);
                                    client.getOut().writeUTF(content);
                                    client.getOut().writeUTF(this.name);
                                    client.getOut().flush();
                                    break;
                                }
                            }
                        }                         
                    }
                    
                    //Tin nhan dang file
                    else if(message.equals("File")){
                        
                        // Đọc các header của tin nhắn gửi file
                            String receiver = in.readUTF();
                            String filename = in.readUTF();
                            int size = Integer.parseInt(in.readUTF());
                            int bufferSize = 2048;
                            byte[] buffer = new byte[bufferSize];
					
                            for (Handler client: Server.clients) {
				if (client.getName().equals(receiver)) {
                                    synchronized (lock) {
					client.getOut().writeUTF("File");
					client.getOut().writeUTF(this.avatar);
					client.getOut().writeUTF(filename);
					client.getOut().writeUTF(String.valueOf(size));
					while (size > 0) {
					// Gửi lần lượt từng buffer cho người nhận cho đến khi hết file
					in.read(buffer, 0, Math.min(size, bufferSize));
					client.getOut().write(buffer, 0, Math.min(size, bufferSize));
					size -= bufferSize;
                                    }
                                    client.getOut().flush();
                                    break;
				}
                            }
			}                        
                    }
                }
                else if(request.equals("ChatWithGroup")){
                    
                    //Nhan tin nhan tu user
                    String message = null;
                    message = in.readUTF();

                    //Tin nhan dang van ban
                    if(message.equals("Text")){
                        
                        
                        int sl = in.readInt();
                        String[] users = new String[sl];
                        for(int i = 0; i < sl; i++){
                            users[i] = in.readUTF();
                        }
                        String content = in.readUTF();
                        
                        for(String user: users){
                            for(Handler client: Server.clients){
                                if(client.getName().equals(user)){
                                    synchronized(lock){
                                        client.getOut().writeUTF("Text");
                                        client.getOut().writeUTF(this.avatar);
                                        client.getOut().writeUTF(content);
                                        client.getOut().writeUTF("Send ALL My Friend");
                                        client.getOut().flush();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    //Tin nhan dang emojis
                    else if(message.equals("Emojis")){
                        
                        
                        int sl = in.readInt();
                        String[] users = new String[sl];
                        for(int i = 0; i < sl; i++){
                            users[i] = in.readUTF();
                        }
                        String content = in.readUTF();

                        for(String user: users){
                            for(Handler client: Server.clients){
                                if(client.getName().equals(user)){
                                    synchronized(lock){
                                        client.getOut().writeUTF("Emojis");
                                        client.getOut().writeUTF(this.avatar);
                                        client.getOut().writeUTF(content);
                                        client.getOut().writeUTF("Send ALL My Friend");
                                        client.getOut().flush();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    //Tin nhan dang file
                    else if(message.equals("File")){
                        
                        //Nhan ten Friend va tin nhan
                        String nameFriend = in.readUTF();
                        String content = in.readUTF();
                        
                        for(Handler client: Server.clients){
                            if(client.getName().equals(nameFriend)){
                                synchronized(lock){
                                    
                                    break;
                                }
                            }
                        }                         
                    }
                }
                  
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
    }
    
    public int checkOnline(String nameFriend) {
        for(Handler client: Server.clients){
            if(client.equals(nameFriend))
                return 1;
        }
        return 0;
    }
    
}
