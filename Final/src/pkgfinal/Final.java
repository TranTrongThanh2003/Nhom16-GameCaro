package pkgfinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Final {   
    
    public static JFrame f;
    MenuBar menubar;
    
    public Final() {
        // Tạo cửa sổ chính
        f = new JFrame();
        f.setTitle("Game Caro");
        f.setSize(750, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        
        // Tạo menubar
        menubar = new MenuBar();
        
        // Tạo menu Game
        Menu game = new Menu("Game");
        MenuItem newItem = new MenuItem("New Game");
        MenuItem exit = new MenuItem("Exit");
        game.add(newItem);
        game.addSeparator();
        game.add(exit);
        
        // Tạo menu Help
        Menu help = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        help.add(helpItem);
        help.add(about);
        
        // Thêm menu vào menubar
        menubar.add(game);
        menubar.add(help);
        
        // Gán menubar vào frame
        f.setMenuBar(menubar);
        
        // Hiển thị cửa sổ
        f.setVisible(true);
    }
    
    public static void main(String[] args) {
        new Final();
    }
}