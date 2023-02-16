/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EditImage;

/**
 *
 * @author ADMIN
 */

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class XuLyAnh {
    
    public  static ImageIcon Edit_Img(String filename, JLabel lbl){
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            int x = lbl.getSize().width;
            int y =lbl.getSize().height;
            int ix =image.getWidth();
            int iy =image.getHeight();

            int dx=0;
            int dy=0;
            if(x /y > ix /iy){
                dy=y;
                dx=dy*ix /iy;
            }else{
                dx=x;
                dy=dx*iy/ix;
            }
            ImageIcon icon = new ImageIcon(image.getScaledInstance(dx, dy, BufferedImage.SCALE_SMOOTH));

            return icon;
        }catch(Exception e) {
            System.out.println("Khong dc duoc path");
            e.printStackTrace();
        }
        
        return null;
    }
    
}
