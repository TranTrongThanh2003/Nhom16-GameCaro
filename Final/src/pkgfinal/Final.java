package pkgfinal;

import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

public class Final {   
    
    public static JFrame f;
    JButton[][] bt;
    static boolean flat = false;
    boolean winner;
    JButton send;
    Timer thoigian;
    String temp="",strNhan = "";
    Integer second, minute;
    JLabel demthoigian;
    JTextArea content;
    JTextField nhap,enterchat;
    JPanel p;
    int xx, yy, x, y;
    int[][] matran;
    int[][] matrandanh;
    
    // Server Socket
    ServerSocket serversocket;
    Socket socket;
    OutputStream os;// ....
    InputStream is;// ......
    ObjectOutputStream oos;// .........
    ObjectInputStream ois;// 
    
    //MenuBar
    MenuBar menubar;
    
    public Final() {
        f = new JFrame();
        f.setTitle("Game Caro");
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("caro.jpg"));
        // Tạo JLabel để chứa hình ảnh nền
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("bg.jpg")));
        background.setBounds(0, 0, 750, 500); // Đảm bảo đúng kích thước của cửa sổ

        // Đặt JLabel làm nền cho JFrame
        f.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                Image bgImage = new ImageIcon(getClass().getResource("bg.jpg")).getImage();
                g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        });
        
        f.getContentPane().setLayout(null); // Giữ nguyên layout hiện tại

        f.setIconImage(imgIcon.getImage());
        f.setSize(750, 500);
        x = 25;
        y = 25;
        f.getContentPane().setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setResizable(false);
        
        //khoi tao ban co
        matran = new int[x][y];
        matrandanh = new int[x][y];
        menubar = new MenuBar();

        //panel chứa các button
        p = new JPanel();  
        p.setBounds(10, 10, 400, 425);
        p.setLayout(new GridLayout(x, y));
        f.add(p);
        
        // tạo menubar cho frame
        f.setMenuBar(menubar);
        Menu game = new Menu("Game");
        menubar.add(game);
        Menu help = new Menu("Help");
        menubar.add(help);
        MenuItem helpItem = new MenuItem("Help");
        help.add(helpItem);
        MenuItem about = new MenuItem("About ..");
        help.add(about);
        help.addSeparator();
        MenuItem newItem = new MenuItem("New Game");
        game.add(newItem);
        MenuItem exit = new MenuItem("Exit");
        game.add(exit);
        game.addSeparator();
        //xử lý chức năng của các item trong menubar
        newItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        newgame();
                        try {
                                oos.writeObject("newgame,123");
                        } catch (IOException ie) {
                                //
                        }
                }

        });
        exit.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                }
        });
        about.addActionListener(new ActionListener()
        {
            @Override
                public void actionPerformed(ActionEvent e) {
                    //Object[] options = {"OK"};
                 JOptionPane.showConfirmDialog(f,
                                "Trần Trọng Thành_2151150058\nNhữ Ngọc Thiện_2151150059\nTrần Anh Tuấn_2151150068\nNguyễn Minh Phương_2151150055", "Nhóm 16",
                                JOptionPane.CLOSED_OPTION); 
                }
        });
        help.addActionListener(new ActionListener()
        {
            @Override
                public void actionPerformed(ActionEvent e) {
                    //Object[] options = {"OK"};
                 JOptionPane.showConfirmDialog(f,
                                "Luật chơi rất đơn giản bạn chỉ cần 5 ô liên tiếp nhau\n"
                                        + "Theo hàng ngang hoặc dọc hoặc chéo là bạn đã thắng", "Luật Chơi",
                                JOptionPane.CLOSED_OPTION); 
                }
        });

        // Thêm hình ảnh caro.jpg
        JLabel caroImage = new JLabel();
        ImageIcon icon = new ImageIcon("D:\\LT Mạng\\Caro Game\\Final\\src\\pkgfinal\\caro.jpg");

        // Điều chỉnh kích thước hình ảnh cho phù hợp
        Image img = icon.getImage().getScaledInstance(280, 130, Image.SCALE_SMOOTH);
        caroImage.setIcon(new ImageIcon(img));

        // Đặt vị trí ngang bằng với đỉnh của bàn cờ và bên phải
        caroImage.setBounds(430, 10, 280, 130);
        f.add(caroImage);

        //label đếm thời gian chơi
        demthoigian = new JLabel("⏰ Thời Gian:");
        demthoigian.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 14));
        demthoigian.setForeground(Color.WHITE);
        f.add(demthoigian);
        demthoigian.setBounds(430, 190, 300, 50);
        second = 0;
        minute = 0;

        //Timer
        thoigian = new Timer(1000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                        String temp = minute.toString();
                        String temp1 = second.toString();
                        if (temp.length() == 1) {
                                temp = "0" + temp;
                        }
                        if (temp1.length() == 1) {
                                temp1 = "0" + temp1;
                        }
                        /*if (second == 59) {
                                demthoigian.setText("Thời Gian:" + temp + ":" + temp1);
                                minute++;
                                second = 0;
                        } else {
                                demthoigian.setText("Thời Gian:" + temp + ":" + temp1);
                                second++;
                        }*/
                        
                        if (second == 10) {
                                try {
                                        oos.writeObject("checkwin,123");
                                } catch (IOException ex) 
                                              {
                                }
                                Object[] options = { "Dong y", "Huy bo" };
                                int m = JOptionPane.showConfirmDialog(f,
                                                "Bạn thua rồi. Gỡ lại chứ nhỉ?", "Thông báo",
                                                JOptionPane.YES_NO_OPTION);
                                if (m == JOptionPane.YES_OPTION) {
                                        second = 0;
                                        minute = 0;
                                        setVisiblePanel(p);
                                        newgame();
                                        try {
                                                oos.writeObject("newgame,123");
                                        } catch (IOException ie) {
                                                //
                                        }
                                } else if (m == JOptionPane.NO_OPTION) {
                                        thoigian.stop();
                                }
                        } else {
                                demthoigian.setText("⏰ Thời Gian:" + temp + ":" + temp1);
                                second++;
                        }

                }

        });
        //khung chat giữa client và server
        Font fo = new Font("Arial",Font.BOLD,15);
        content = new JTextArea();
        content.setFont(fo);
        content.setBackground(Color.decode("#FFE4C4"));
        
        content.setEditable(false);
        JScrollPane sp = new JScrollPane(content);
        sp.setBounds(430,230,300,150);
        send = new JButton("Gửi"); //tạo button gửi chat đi
        
        send.setBounds(640, 400, 70, 30);
        // Thay thế phần xử lý JTextField hiện tại bằng đoạn code này
enterchat = new JTextField(" Chat trò chuyện...");
enterchat.setFont(fo);
enterchat.setBounds(430, 400, 200, 30);
enterchat.setBackground(Color.white);
enterchat.setForeground(Color.GRAY); // Màu chữ xám cho placeholder

// Xử lý placeholder
enterchat.addFocusListener(new FocusListener() {
    @Override
    public void focusGained(FocusEvent e) {
        if (enterchat.getText().equals(" Chat trò chuyện...")) {
            enterchat.setText("");
            enterchat.setForeground(Color.BLACK); // Đổi màu khi bắt đầu nhập
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (enterchat.getText().trim().isEmpty()) {
            enterchat.setText(" Chat trò chuyện...");
            enterchat.setForeground(Color.GRAY); // Khôi phục placeholder nếu trống
        }
    }
});

// Xử lý khi người dùng gõ phím
enterchat.addKeyListener(new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent e) {
        if (enterchat.getText().equals(" Chat trò chuyện...")) {
            enterchat.setText("");
            enterchat.setForeground(Color.BLACK);
        }
    }
});

        f.add(enterchat);
        f.add(send);
        f.add(sp);
        f.setVisible(true);
        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource().equals(send))
        {
            try
            {
                
                temp+="Tôi: " + enterchat.getText() + "\n";
                content.setText(temp);
                oos.writeObject("chat," + enterchat.getText());
                enterchat.setText("");
                //temp = "";
                enterchat.requestFocus();
                content.setVisible(false);
                content.setVisible(true);
                
            }
            catch (Exception r)
            {
                r.printStackTrace();
            }
        }
            }
        });
        
        //button caro 
        bt = new JButton[x][y];
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                final int a = i, b =j;
                bt[a][b] = new JButton();
                bt[a][b].setBackground(Color.LIGHT_GRAY);
                bt[a][b].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        flat = true;// server đã click
                        thoigian.start();
                        second = 0;
                        minute = 0;
                        matrandanh[a][b] = 1;
                        // bt[a][b].setEnabled(false);                        
                        //bt[a][b].setIcon(new ImageIcon(getClass().getResource("o.png")));
                            
                        bt[a][b].setText("O");
                        bt[a][b].setBackground(new Color(200, 230, 255));
                        bt[a][b].putClientProperty("html.disable", Boolean.TRUE);  // Quan trọng!
                        bt[a][b].setFocusPainted(false);  // Tắt viền focus
                        bt[a][b].setMargin(new Insets(0, 0, 0, 0));  // Xóa margin
                        
                        bt[a][b].setForeground(new Color(18,28,254));  
                        bt[a][b].setFont(new Font("Arial", Font.BOLD, 12));  // Font đậm, size 12px
                        bt[a][b].setHorizontalAlignment(SwingConstants.CENTER);  // Canh giữa
                        bt[a][b].setVerticalAlignment(SwingConstants.CENTER);  // Canh giữa
                        try{
                            oos.writeObject("caro," + a + "," + b);
                            setEnableButton(false);
                        }
                        catch(Exception ie)
                        {
                            ie.printStackTrace();
                        }
                        thoigian.stop();
                  }

                });                
                p.add(bt[a][b]);
                p.setVisible(false);
                p.setVisible(true);
            }
        }
        
       // Thêm nút Start vào giao diện
JButton startButton = new JButton("Start Server");
f.add(startButton);
startButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serversocket = new ServerSocket(1234);
                    System.out.println("Dang doi client...");
                    socket = serversocket.accept();
                    System.out.println("Client da ket noi thanh cong!");
                    os = socket.getOutputStream();
                    is = socket.getInputStream();
                    oos = new ObjectOutputStream(os);
                    ois = new ObjectInputStream(is);
                    
                    while (true) {
                        String stream = ois.readObject().toString();
                        String[] data = stream.split(",");
                        if (data[0].equals("chat")) {
                            temp += "Khách: " + data[1] + '\n';
                            content.setText(temp);
                        } else if (data[0].equals("caro")) {
                            thoigian.start();
                            second = 0;
                            minute = 0;
                            caro(data[1], data[2]);
                            setEnableButton(true);

                            if (winner == false)
                                setEnableButton(true);
                        } else if (data[0].equals("newgame")) {
                            newgame();
                            second = 0;
                            minute = 0;
                        } else if (data[0].equals("checkwin")) {
                            thoigian.stop();
                        }
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }).start(); // Khởi động server trong thread mới để không làm chậm giao diện
    }
});

// Điều chỉnh vị trí nút "Start Server"
startButton.setBounds(430, 160, 150, 30);  // Vị trí dưới caro.jpg và trên label "Thời gian"


 //finally {
              //      socket.close();
              //      serversocket.close();
            //}
        
    }
    
    public void newgame() {
            for (int i = 0; i < x; i++)
            {
                    for (int j = 0; j < y; j++) {
                            bt[i][j].setBackground(Color.LIGHT_GRAY);
                            bt[i][j].setText("");
                            matran[i][j] = 0;
                            matrandanh[i][j] = 0;
                    }
            }
            setEnableButton(true);
            second = 0;
            minute = 0;
            thoigian.stop();
}
    
    public void setVisiblePanel(JPanel pHienthi) {
            f.add(pHienthi);
            pHienthi.setVisible(true);
            pHienthi.updateUI();// ......
	
       }
    
    public void setEnableButton(boolean b) {
            for (int i = 0; i < x; i++)
            {
                    for (int j = 0; j < y; j++) {
                            if (matrandanh[i][j] == 0)
                                    bt[i][j].setEnabled(b);
                    }
            }
    }
    
    //Xây dựng thuật toán tính toán thắng thua
    public int checkHang() {
        int win = 0;
        
        for (int i = 0; i < x; i++) { // Duyệt từng hàng
            int hang = 0; // Reset bộ đếm mỗi hàng
            for (int j = 0; j < y; j++) { // Duyệt từng ô trong hàng
                if (matran[i][j] == 1) {
                    hang++;
                    if (hang > 4) {
                        return 1; // Tìm thấy 5 quân liên tiếp, trả về 1 ngay lập tức
                    }
                } else {
                    hang = 0; // Nếu gặp ô trống, reset bộ đếm
                }
            }
        }
        return win;
    }
    

    public int checkCot() {
        int win = 0;
        
        for (int j = 0; j < y; j++) { // Duyệt từng cột
            int cot = 0; // Reset bộ đếm mỗi cột
            for (int i = 0; i < x; i++) { // Duyệt từng ô trong cột
                if (matran[i][j] == 1) {
                    cot++;
                    if (cot > 4) {
                        return 1; // Tìm thấy 5 quân liên tiếp, trả về 1 ngay lập tức
                    }
                } else {
                    cot = 0; // Nếu gặp ô trống, reset bộ đếm
                }
            }
        }
        return win;
    }
    

    public int checkCheoPhai() {
        int win = 0;
        
        // Duyệt qua từng phần tử của bàn cờ
        for (int i = x - 1; i >= 0; i--) { // Duyệt từ hàng dưới lên
            for (int j = 0; j < y; j++) { // Duyệt từng ô trong hàng
                int cheop = 0; // Reset bộ đếm tại mỗi ô
                int row = i, col = j;
    
                // Kiểm tra đường chéo phải (từ trái xuống phải)
                while (row >= 0 && col < y) {
                    if (matran[row][col] == 1) {
                        cheop++;
                        if (cheop > 4) {
                            return 1; // Tìm thấy 5 quân liên tiếp, trả về 1 ngay lập tức
                        }
                    } else {
                        break; // Dừng kiểm tra nếu gặp ô trống
                    }
                    row--; // Di chuyển lên trên (giảm hàng)
                    col++; // Di chuyển sang phải (tăng cột)
                }
            }
        }
        return win;
    }
    

    public int checkCheoTrai() {
        // Kiểm tra từ biên dưới và biên phải
        for (int i = x - 1; i >= 0; i--) { // Biên dưới
            if (checkDiagonalLeft(i, y - 1)) return 1;
        }
        for (int j = y - 2; j >= 0; j--) { // Biên phải (trừ góc đã xét)
            if (checkDiagonalLeft(x - 1, j)) return 1;
        }
        return 0;
    }
    
    // Hàm kiểm tra đường chéo trái từ điểm xuất phát
    private boolean checkDiagonalLeft(int startX, int startY) {
        int cheot = 0;
        int row = startX, col = startY;
    
        while (row >= 0 && col >= 0) {
            if (matran[row][col] == 1) {
                cheot++;
                if (cheot > 4) return true;
            } else {
                cheot = 0;
            }
            row--;  // Di chuyển lên trên
            col--;  // Di chuyển sang trái
        }
        return false;
    }
    
    //Viết hàm caro
    public void caro(String x, String y)
    {
        xx = Integer.parseInt(x);
        yy = Integer.parseInt(y);
        // đánh dấu vị trí đánh
        matran[xx][yy] = 1;
        matrandanh[xx][yy] = 1;
        bt[xx][yy].setEnabled(false);
        // bt[xx][yy].setIcon(new ImageIcon("x.png"));
        // bt[xx][yy].setBackground(Color.BLACK);
        bt[xx][yy].setText("X");
        bt[xx][yy].putClientProperty("html.disable", Boolean.TRUE);  // Tắt HTML
        bt[xx][yy].setFocusPainted(false);  // Tắt viền focus
        bt[xx][yy].setMargin(new Insets(0, 0, 0, 0));  // Xóa margin


        bt[xx][yy].setForeground(Color.RED);  // Màu đỏ (có thể dùng new Color(255, 0, 0))
        bt[xx][yy].setFont(new Font("Arial", Font.BOLD, 12));  // Font đậm, size 12px
        bt[xx][yy].setHorizontalAlignment(SwingConstants.CENTER);  // Canh giữa
        bt[xx][yy].setVerticalAlignment(SwingConstants.CENTER);  // Canh giữa
        bt[xx][yy].setBackground(new Color(236,236,236));

        // 
        // Kiem tra thang hay chua
        // System.out.println("CheckH:" + checkHang());
        // System.out.println("CheckC:" + checkCot());
        // System.out.println("CheckCp:" + checkCheoPhai());
        // System.out.println("CheckCt:" + checkCheoTrai());
        winner = (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1 || checkCheoTrai() == 1);
        if (checkHang() == 1 || checkCot() == 1 || checkCheoPhai() == 1
                        || checkCheoTrai() == 1) {
                setEnableButton(false);
                thoigian.stop();
                try {
                        oos.writeObject("checkwin,123");
                } catch (IOException ex) 
                                              {
                }
                // Object[] options = { "Dong y", "Huy bo" };
                int m = JOptionPane.showConfirmDialog(f,
                                "Bạn thua rồi.Gỡ lại chứ nhỉ?", "Thong bao",
                                JOptionPane.YES_NO_OPTION);
                if (m == JOptionPane.YES_OPTION) {
                        second = 0;
                        minute = 0;
                        setVisiblePanel(p);
                        newgame();
                        try {
                                oos.writeObject("newgame,123");
                        } catch (IOException ie) {
                                //
                        }
                } else if (m == JOptionPane.NO_OPTION) {
                        thoigian.stop();
                        System.exit(0);
                }
        }
        
    }
    
    public static void main(String[] args) {
        new Final();
    }
    
}
