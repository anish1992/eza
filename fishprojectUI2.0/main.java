import java.awt.EventQueue;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;

import java.io.*;
import java.awt.CardLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

public class main extends JFrame {

    private String[] info = { "null", "null", "null" };
    private String[] combo_hour = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    private String[] combo_minute = { "00", "10", "20", "30", "40", "50", "60" };
    private String[] am_pm = { "AM", "PM" };
    private String[] tens = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    private String[] ones = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    /**
     * Creates new form setup
     */
    // private int num_fish;
    private String selected;
    private String light_on;
    private String light_off;
    private String tank;

    /*
     * Variable set from main()
     */
    static ArrayList<String> content = new ArrayList<String>();
    static private int num_fish;

    /*
     * Variable set from selected type of fish
     */
    static private float ideal_ph;
    static private float ideal_temp;

    /*
     * Variables set from setup page.
     */
    private int time_on_hour = 0;
    private int time_on_minute = 0;
    private int time_off_hour = 0;
    private int time_off_minute = 0;

    Thread Engine_On;

    /**
     * COUNTER
     */
    private int first_set = 0;
    // for data 
    float temp, ph;
    boolean in_dash = false;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        read_file();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    main frame = new main();
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void read_temp_ph(String keyword) {
        int selected_fish_index = 0;
        int current_fish_index = 0;
        for (int i = 0; i < num_fish; i++) {
            if (content.get(i).equals(keyword)) {
                selected_fish_index = i;
            }
        }
        // System.out.println(""+selected_fish_index);

        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/data.txt"));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                if (current_fish_index == selected_fish_index) {
                    String[] line = currentLine.split(" ");
                    ideal_temp = Float.parseFloat(line[2]);
                    ideal_ph = Float.parseFloat(line[3]);
                    break; // FISH TYPE FOUND
                }
                current_fish_index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(""+ideal_temp+" "+ ideal_ph);

    }

    public static void read_file() {
        num_fish = 0;
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/data.txt"));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] line = currentLine.split(" ");
                line[1] = line[1].replace("_", " ");
                line[1] = line[1].toUpperCase();
                content.add(line[1]);
                num_fish++;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public main() {

        super("EZ Aquarium");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 800, 480);
        getContentPane().setLayout(null);

        JPanel Case = new JPanel();
        Case.setBackground(Color.GRAY);
        Case.setBounds(0, 0, 800, 480);
        getContentPane().add(Case);
        Case.setLayout(null);

        JPanel Deck = new JPanel();
        Deck.setBounds(1, 1, 797, 455);
        Case.add(Deck);
        Deck.setLayout(new CardLayout(0, 0));
        
        /*  
         * data and object for Histogram   
         */
        float[] temper_counts = {0,0,0,0,0,0,0,0,0,0};
        Histogram temper_display = new Histogram();
        float[] ph_counts = {0,0,0,0,0,0,0,0,0,0};
        Histogram ph_display = new Histogram();
        
        
        /*
         * adding Welcome_panel panel to Deck of card layout Everything under
         * this will be in this panel
         */
        JPanel Welcome_panel = new JPanel();
        Welcome_panel
        .setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
        Deck.add(Welcome_panel, "name_1459752272475044000");

        JLabel background = new JLabel();
        background.setIcon(new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/welcome.png"));
        background.setBounds(-123, -84, 1024, 600);
        Welcome_panel.add(background);

        /*
         * adding Setup_panel panel to Deck of card layout Everything under this
         * will be in this panel
         */
        JPanel Setup_panel = new JPanel();
        Setup_panel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
        Deck.add(Setup_panel, "name_1459752478502634000");

        JLabel back_to_main = new JLabel();
        back_to_main.setFont(new Font("SansSerif", Font.BOLD, 20));
        back_to_main.setBounds(25, 25, 62, 50);
        back_to_main.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Actions-go-previous-icon.png"));
        Setup_panel.add(back_to_main);
        back_to_main.setVisible(false);

        JLabel jLabel4 = new JLabel();
        jLabel4.setFont(new Font("SansSerif", Font.BOLD, 22));
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("SET TANK SIZE");
        Setup_panel.add(jLabel4);
        jLabel4.setBounds(25, 160, 337, 50);

        JComboBox jComboBox1 = new JComboBox();
        jComboBox1.setFont(new java.awt.Font("SansSerif", 1, 26));
        jComboBox1.setForeground(new java.awt.Color(51, 51, 51));
        // jComboBox1.addItem(new DefaultComboBoxModel(content.toArray()));//
        // add from data base
        for (int i = 0; i < num_fish; i++) {
            jComboBox1.addItem(content.get(i));
        }
        Setup_panel.add(jComboBox1);
        jComboBox1.setBounds(400, 95, 250, 28);

        JLabel tank_size_tens = new JLabel("1");
        tank_size_tens.setHorizontalAlignment(SwingConstants.CENTER);
        tank_size_tens.setForeground(Color.LIGHT_GRAY);
        tank_size_tens.setFont(new Font("SansSerif", Font.BOLD, 40));
        // tank_size.setHorizontalAlignment(JTextField.CENTER);
        Setup_panel.add(tank_size_tens);
        tank_size_tens.setBounds(431, 157, 35, 50);

        JLabel tank_size_ones = new JLabel("0");
        tank_size_ones.setHorizontalAlignment(SwingConstants.CENTER);
        tank_size_ones.setForeground(Color.LIGHT_GRAY);
        tank_size_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
        tank_size_ones.setBounds(461, 157, 35, 50);
        Setup_panel.add(tank_size_ones);

        JLabel tank_size_hundred = new JLabel("0");
        tank_size_hundred.setHorizontalAlignment(SwingConstants.CENTER);
        tank_size_hundred.setForeground(Color.LIGHT_GRAY);
        tank_size_hundred.setFont(new Font("SansSerif", Font.BOLD, 40));
        tank_size_hundred.setBounds(400, 157, 35, 50);

        Setup_panel.add(tank_size_hundred);

        JLabel lblNewLabel_3 = new JLabel("GALLONS");
        lblNewLabel_3.setForeground(Color.LIGHT_GRAY);
        lblNewLabel_3.setFont(new Font("SansSerif", Font.BOLD, 25));
        lblNewLabel_3.setBounds(531, 151, 137, 67);
        Setup_panel.add(lblNewLabel_3);

        JLabel light_on_time_hour = new JLabel();
        light_on_time_hour.setForeground(Color.LIGHT_GRAY);
        light_on_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
        light_on_time_hour.setText("12");
        light_on_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
        // light_on_time_hour.setHorizontalAlignment(JTextField.CENTER);
        Setup_panel.add(light_on_time_hour);
        light_on_time_hour.setBounds(384, 240, 62, 50);

        JLabel on_am_pm = new JLabel();
        on_am_pm.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String current = on_am_pm.getText();
                switch (current) {
                case "AM":
                    on_am_pm.setText("PM");
                    break;
                case "PM":
                    on_am_pm.setText("AM");
                    break;
                }
            }
        });
        on_am_pm.setForeground(Color.LIGHT_GRAY);
        on_am_pm.setHorizontalAlignment(SwingConstants.CENTER);
        on_am_pm.setText("AM");
        on_am_pm.setFont(new Font("SansSerif", Font.BOLD, 40));
        on_am_pm.setBounds(568, 240, 82, 50);
        Setup_panel.add(on_am_pm);

        JLabel light_on_time_minute = new JLabel();
        light_on_time_minute.setForeground(Color.LIGHT_GRAY);
        light_on_time_minute.setHorizontalAlignment(SwingConstants.CENTER);
        light_on_time_minute.setText("00");
        light_on_time_minute.setFont(new Font("SansSerif", Font.BOLD, 40));
        light_on_time_minute.setBounds(461, 240, 62, 50);
        Setup_panel.add(light_on_time_minute);

        JLabel setting_label = new JLabel();
        setting_label.setBackground(new java.awt.Color(255, 255, 255));
        setting_label.setFont(new Font("SansSerif", Font.BOLD, 40));
        setting_label.setForeground(new java.awt.Color(204, 204, 204));
        // jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        setting_label.setText("SETTINGS");
        Setup_panel.add(setting_label);
        setting_label.setBounds(300, 15, 236, 60);

        JLabel fish_select = new JLabel();
        fish_select.setFont(new Font("SansSerif", Font.BOLD, 22));
        fish_select.setForeground(new java.awt.Color(204, 204, 204));
        fish_select.setText("PLEASE SELECT A FISH TYPE");
        Setup_panel.add(fish_select);
        fish_select.setBounds(25, 85, 412, 50);

        JLabel jLabel6 = new JLabel();
        jLabel6.setFont(new Font("SansSerif", Font.BOLD, 22));
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("SPECIFIC TIME FOR LIGHT OFF");
        Setup_panel.add(jLabel6);
        jLabel6.setBounds(25, 320, 357, 50);

        JLabel jLabel5 = new JLabel();
        jLabel5.setFont(new Font("SansSerif", Font.BOLD, 22));
        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("SPECIFIC TIME FOR LIGHT ON");
        Setup_panel.add(jLabel5);
        jLabel5.setBounds(25, 240, 357, 50);

        JLabel light_off_time_hour = new JLabel();
        light_off_time_hour.setForeground(Color.LIGHT_GRAY);
        light_off_time_hour.setText("6");
        light_off_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
        light_off_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
        Setup_panel.add(light_off_time_hour);
        light_off_time_hour.setBounds(384, 322, 62, 50);

        JLabel light_off_time_minute = new JLabel();
        light_off_time_minute.setText("00");
        light_off_time_minute.setForeground(Color.LIGHT_GRAY);
        light_off_time_minute.setHorizontalAlignment(SwingConstants.CENTER);
        light_off_time_minute.setBounds(461, 321, 62, 50);
        light_off_time_minute.setFont(new Font("SansSerif", Font.BOLD, 40));
        Setup_panel.add(light_off_time_minute);

        JLabel off_am_pm = new JLabel();
        off_am_pm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = off_am_pm.getText();
                switch (current) {
                case "AM":
                    off_am_pm.setText("PM");
                    break;
                case "PM":
                    off_am_pm.setText("AM");
                    break;
                }
            }
        });
        off_am_pm.setForeground(Color.LIGHT_GRAY);
        off_am_pm.setHorizontalAlignment(SwingConstants.CENTER);
        off_am_pm.setText("AM");
        off_am_pm.setBounds(568, 321, 82, 50);
        off_am_pm.setFont(new Font("SansSerif", Font.BOLD, 40));
        Setup_panel.add(off_am_pm);

        JLabel After_Setup = new JLabel();
        After_Setup.setFont(new java.awt.Font("SansSerif", 1, 26));
        After_Setup.setIcon(
                new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Actions-go-next-icon.png"));
        // action listener at the bottom
        Setup_panel.add(After_Setup);
        After_Setup.setBounds(700, 380, 100, 60);

        JLabel jButton2 = new JLabel();
        jButton2.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Actions-edit-clear-icon.png"));
        jButton2.setFont(new java.awt.Font("SansSerif", 1, 26));
        jButton2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                jComboBox1.setSelectedIndex(0);
                tank_size_hundred.setText("0");
                tank_size_tens.setText("1");
                tank_size_ones.setText("0");
                light_on_time_hour.setText("12");
                light_on_time_minute.setText("00");
                on_am_pm.setText("AM");
                light_off_time_hour.setText("6");
                light_off_time_minute.setText("00");
                off_am_pm.setText("AM");

            }
        });
        Setup_panel.add(jButton2);
        jButton2.setBounds(25, 380, 200, 50);

        JLabel label = new JLabel(":");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("SansSerif", Font.BOLD, 35));
        label.setBounds(431, 240, 31, 50);
        Setup_panel.add(label);

        JLabel lblNewLabel = new JLabel(":");
        lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 35));
        lblNewLabel.setForeground(Color.LIGHT_GRAY);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(431, 320, 31, 50);
        Setup_panel.add(lblNewLabel);

        JLabel custom_setting = new JLabel("CUSTOMIZE");
        custom_setting.setFont(new Font("SansSerif", Font.BOLD, 26));
        custom_setting.setBounds(600, 38, 200, 28);
        custom_setting.setForeground(Color.LIGHT_GRAY);
        Setup_panel.add(custom_setting);

        JLabel tank_hundred_up = new JLabel("");
        tank_hundred_up.setVerticalAlignment(SwingConstants.TOP);
        tank_hundred_up.setHorizontalAlignment(SwingConstants.CENTER);
        tank_hundred_up.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        tank_hundred_up.setBounds(400, 140, 35, 40);
        tank_hundred_up.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(tank_size_hundred.getText().toString());
                if (current == 9) {
                    current = 0;
                } else {
                    current++;
                }
                tank_size_hundred.setText("" + current);
            }
        });

        JLabel label_2 = new JLabel("");
        label_2.setVerticalAlignment(SwingConstants.TOP);
        label_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                int current = Integer.parseInt(tank_size_tens.getText().toString());
                if (current == 9) {
                    current = 0;
                } else {
                    current++;
                }
                tank_size_tens.setText("" + current);
            }
        });
        label_2.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_2.setHorizontalAlignment(SwingConstants.CENTER);
        label_2.setBounds(431, 140, 35, 40);
        Setup_panel.add(label_2);

        JLabel label_3 = new JLabel("");
        label_3.setVerticalAlignment(SwingConstants.TOP);
        label_3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                int current = Integer.parseInt(tank_size_ones.getText().toString());
                if (current == 9) {
                    current = 0;
                } else {
                    current++;
                }
                tank_size_ones.setText("" + current);
            }
        });
        label_3.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_3.setHorizontalAlignment(SwingConstants.CENTER);
        label_3.setBounds(461, 140, 35, 40);
        Setup_panel.add(label_3);

        JLabel label_4 = new JLabel("");
        label_4.setVerticalAlignment(SwingConstants.BOTTOM);
        label_4.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(tank_size_tens.getText().toString());
                if (current == 0) {
                    current = 9;
                } else {
                    current--;
                }
                tank_size_tens.setText("" + current);
            }
        });
        label_4.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setBounds(431, 177, 35, 40);
        Setup_panel.add(label_4);

        JLabel label_5 = new JLabel("");
        label_5.setVerticalAlignment(SwingConstants.BOTTOM);
        label_5.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(tank_size_ones.getText().toString());
                if (current == 0) {
                    current = 9;
                } else {
                    current--;
                }
                tank_size_ones.setText("" + current);
            }
        });
        label_5.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        label_5.setBounds(461, 178, 35, 40);
        Setup_panel.add(label_5);

        JLabel label_6 = new JLabel("");
        label_6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(light_on_time_hour.getText().toString());
                if (current == 12) {
                    current = 1;
                } else {
                    current++;
                }
                light_on_time_hour.setText("" + current);
            }
        });
        label_6.setVerticalAlignment(SwingConstants.TOP);
        label_6.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_6.setHorizontalAlignment(SwingConstants.CENTER);
        label_6.setBounds(400, 225, 35, 40);
        Setup_panel.add(label_6);

        JLabel label_7 = new JLabel("");
        label_7.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(light_off_time_hour.getText().toString());
                if (current == 12) {
                    current = 1;
                } else {
                    current++;
                }
                light_off_time_hour.setText("" + current);
            }
        });
        label_7.setVerticalAlignment(SwingConstants.TOP);
        label_7.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_7.setHorizontalAlignment(SwingConstants.CENTER);
        label_7.setBounds(400, 307, 35, 40);
        Setup_panel.add(label_7);

        JLabel label_8 = new JLabel("");
        label_8.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                String current = off_am_pm.getText();
                switch (current) {
                case "AM":
                    off_am_pm.setText("PM");
                    break;
                case "PM":
                    off_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_8.setVerticalAlignment(SwingConstants.TOP);
        label_8.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_8.setHorizontalAlignment(SwingConstants.CENTER);
        label_8.setBounds(584, 225, 52, 40);
        Setup_panel.add(label_8);

        JLabel label_9 = new JLabel("");
        label_9.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = off_am_pm.getText();
                switch (current) {
                case "AM":
                    off_am_pm.setText("PM");
                    break;
                case "PM":
                    off_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_9.setVerticalAlignment(SwingConstants.TOP);
        label_9.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_9.setHorizontalAlignment(SwingConstants.CENTER);
        label_9.setBounds(584, 307, 52, 40);
        Setup_panel.add(label_9);

        JLabel label_10 = new JLabel("");
        label_10.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = light_on_time_minute.getText();
                switch (current) {
                case "00":
                    light_on_time_minute.setText("10");
                    break;
                case "10":
                    light_on_time_minute.setText("20");
                    break;
                case "20":
                    light_on_time_minute.setText("30");
                    break;
                case "30":
                    light_on_time_minute.setText("40");
                    break;
                case "40":
                    light_on_time_minute.setText("50");
                    break;
                case "50":
                    light_on_time_minute.setText("60");
                    break;
                case "60":
                    light_on_time_minute.setText("00");
                    break;
                }
            }
        });
        label_10.setVerticalAlignment(SwingConstants.TOP);
        label_10.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_10.setHorizontalAlignment(SwingConstants.CENTER);
        label_10.setBounds(461, 225, 62, 40);
        Setup_panel.add(label_10);

        JLabel label_11 = new JLabel("");
        label_11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = light_off_time_minute.getText();
                switch (current) {
                case "00":
                    light_off_time_minute.setText("10");
                    break;
                case "10":
                    light_off_time_minute.setText("20");
                    break;
                case "20":
                    light_off_time_minute.setText("30");
                    break;
                case "30":
                    light_off_time_minute.setText("40");
                    break;
                case "40":
                    light_off_time_minute.setText("50");
                    break;
                case "50":
                    light_off_time_minute.setText("60");
                    break;
                case "60":
                    light_off_time_minute.setText("00");
                    break;
                }
            }
        });
        label_11.setVerticalAlignment(SwingConstants.TOP);
        label_11.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_11.setHorizontalAlignment(SwingConstants.CENTER);
        label_11.setBounds(461, 307, 62, 40);
        Setup_panel.add(label_11);

        JLabel label_12 = new JLabel("");
        label_12.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(light_on_time_hour.getText().toString());
                if (current == 1) {
                    current = 12;
                } else {
                    current--;
                }
                light_on_time_hour.setText("" + current);
            }
        });
        label_12.setVerticalAlignment(SwingConstants.BOTTOM);
        label_12.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_12.setHorizontalAlignment(SwingConstants.CENTER);
        label_12.setBounds(400, 265, 35, 40);
        Setup_panel.add(label_12);

        JLabel label_13 = new JLabel("");
        label_13.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = light_on_time_minute.getText();
                switch (current) {
                case "00":
                    light_on_time_minute.setText("60");
                    break;
                case "10":
                    light_on_time_minute.setText("00");
                    break;
                case "20":
                    light_on_time_minute.setText("10");
                    break;
                case "30":
                    light_on_time_minute.setText("20");
                    break;
                case "40":
                    light_on_time_minute.setText("30");
                    break;
                case "50":
                    light_on_time_minute.setText("40");
                    break;
                case "60":
                    light_on_time_minute.setText("50");
                    break;
                }
            }
        });
        label_13.setVerticalAlignment(SwingConstants.BOTTOM);
        label_13.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_13.setHorizontalAlignment(SwingConstants.CENTER);
        label_13.setBounds(461, 265, 62, 40);
        Setup_panel.add(label_13);

        JLabel label_14 = new JLabel("");
        label_14.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = off_am_pm.getText();
                switch (current) {
                case "AM":
                    off_am_pm.setText("PM");
                    break;
                case "PM":
                    off_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_14.setVerticalAlignment(SwingConstants.BOTTOM);
        label_14.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_14.setHorizontalAlignment(SwingConstants.CENTER);
        label_14.setBounds(584, 265, 52, 40);
        Setup_panel.add(label_14);

        JLabel label_15 = new JLabel("");
        label_15.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(light_off_time_hour.getText().toString());
                if (current == 1) {
                    current = 12;
                } else {
                    current--;
                }
                light_off_time_hour.setText("" + current);
            }
        });
        label_15.setVerticalAlignment(SwingConstants.BOTTOM);
        label_15.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_15.setHorizontalAlignment(SwingConstants.CENTER);
        label_15.setBounds(400, 346, 35, 40);
        Setup_panel.add(label_15);

        JLabel label_16 = new JLabel("");
        label_16.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = light_off_time_minute.getText();
                switch (current) {
                case "00":
                    light_off_time_minute.setText("60");
                    break;
                case "10":
                    light_off_time_minute.setText("00");
                    break;
                case "20":
                    light_off_time_minute.setText("10");
                    break;
                case "30":
                    light_off_time_minute.setText("20");
                    break;
                case "40":
                    light_off_time_minute.setText("30");
                    break;
                case "50":
                    light_off_time_minute.setText("40");
                    break;
                case "60":
                    light_off_time_minute.setText("50");
                    break;
                }
            }
        });
        label_16.setVerticalAlignment(SwingConstants.BOTTOM);
        label_16.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_16.setHorizontalAlignment(SwingConstants.CENTER);
        label_16.setBounds(461, 346, 62, 40);
        Setup_panel.add(label_16);

        JLabel label_17 = new JLabel("");
        label_17.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = off_am_pm.getText();
                switch (current) {
                case "AM":
                    off_am_pm.setText("PM");
                    break;
                case "PM":
                    off_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_17.setVerticalAlignment(SwingConstants.BOTTOM);
        label_17.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_17.setHorizontalAlignment(SwingConstants.CENTER);
        label_17.setBounds(584, 346, 52, 40);
        Setup_panel.add(label_17);
        Setup_panel.add(tank_hundred_up);

        JLabel tank_hundred_down = new JLabel("");
        tank_hundred_down.setVerticalAlignment(SwingConstants.BOTTOM);
        tank_hundred_down.setHorizontalAlignment(SwingConstants.CENTER);
        tank_hundred_down.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        tank_hundred_down.setBounds(400, 177, 35, 40);
        tank_hundred_down.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(tank_size_hundred.getText().toString());
                if (current == 0) {
                    current = 9;
                } else {
                    current--;
                }
                tank_size_hundred.setText("" + current);
            }
        });
        Setup_panel.add(tank_hundred_down);

        /*
         * New panel for custom setting
         */
        JPanel Custom = new JPanel();
        Custom.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
        // Dash.setBackground(Color.GREEN);
        Deck.add(Custom, "name_1459752488724759011");
        Custom.setLayout(null);

        JLabel custom_title = new JLabel("MANUAL SETUP");
        custom_title.setForeground(Color.LIGHT_GRAY);
        custom_title.setFont(new Font("SansSerif", Font.BOLD, 40));
        custom_title.setBounds(243, 15, 355, 60);
        Custom.add(custom_title);

        JLabel custom_clear = new JLabel();
        custom_clear.setFont(new java.awt.Font("SansSerif", 1, 26));
        custom_clear.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Actions-edit-clear-icon.png"));
        Custom.add(custom_clear);
        custom_clear.setBounds(25, 382, 200, 50);

        JLabel After_custom_setup = new JLabel();
        After_custom_setup.setFont(new java.awt.Font("SansSerif", 1, 26));
        After_custom_setup.setIcon(
                new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Actions-go-next-icon.png"));
        // action listener at the bottom
        Custom.add(After_custom_setup);
        After_custom_setup.setBounds(700, 382, 100, 60);

        JLabel back_to_setting = new JLabel();
        back_to_setting.setFont(new Font("SansSerif", Font.BOLD, 20));
        back_to_setting.setBounds(25, 25, 62, 50);
        back_to_setting.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Actions-go-previous-icon.png"));
        back_to_setting.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // removing panel
                Deck.removeAll();
                Deck.repaint();
                Deck.revalidate();

                // adding panel
                Deck.add(Setup_panel);
                Deck.repaint();
                Deck.revalidate();

            }
        });
        Custom.add(back_to_setting);

        JLabel temp_ones = new JLabel("0");
        temp_ones.setForeground(Color.LIGHT_GRAY);
        temp_ones.setHorizontalAlignment(SwingConstants.CENTER);
        temp_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
        temp_ones.setBounds(431, 86, 35, 50);
        Custom.add(temp_ones);

        JLabel temp_tens = new JLabel("0");
        temp_tens.setHorizontalAlignment(SwingConstants.CENTER);
        temp_tens.setForeground(Color.LIGHT_GRAY);
        temp_tens.setFont(new Font("SansSerif", Font.BOLD, 40));
        temp_tens.setBounds(400, 86, 35, 50);
        Custom.add(temp_tens);

        JLabel temp_deci = new JLabel("0");
        temp_deci.setForeground(Color.LIGHT_GRAY);
        temp_deci.setHorizontalAlignment(SwingConstants.CENTER);
        temp_deci.setFont(new Font("SansSerif", Font.BOLD, 40));
        temp_deci.setBounds(461, 86, 35, 50);
        Custom.add(temp_deci);

        JLabel lblNewLabel_5 = new JLabel(".");
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_5.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblNewLabel_5.setForeground(new Color(192, 192, 192));
        lblNewLabel_5.setBounds(269, 97, 34, 30);
        Custom.add(lblNewLabel_5);

        JLabel lblNewLabel_7 = new JLabel("<html> &#8457;</html>");
        lblNewLabel_7.setForeground(Color.LIGHT_GRAY);
        lblNewLabel_7.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_7.setBounds(511, 86, 50, 50);
        Custom.add(lblNewLabel_7);

        JLabel ph_ones = new JLabel("0");
        ph_ones.setForeground(Color.LIGHT_GRAY);
        ph_ones.setHorizontalAlignment(SwingConstants.TRAILING);
        ph_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
        ph_ones.setBounds(220, 86, 55, 50);
        Custom.add(ph_ones);

        JLabel ph_oneth = new JLabel("0");
        ph_oneth.setForeground(Color.LIGHT_GRAY);
        ph_oneth.setHorizontalAlignment(SwingConstants.CENTER);
        ph_oneth.setFont(new Font("SansSerif", Font.BOLD, 40));
        ph_oneth.setBounds(292, 86, 35, 50);
        Custom.add(ph_oneth);

        JLabel ph_tenth = new JLabel("0");
        ph_tenth.setForeground(Color.LIGHT_GRAY);
        ph_tenth.setHorizontalAlignment(SwingConstants.CENTER);
        ph_tenth.setFont(new Font("SansSerif", Font.BOLD, 40));
        ph_tenth.setBounds(326, 86, 35, 50);
        Custom.add(ph_tenth);

        JLabel label_1 = new JLabel(".");
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setForeground(Color.LIGHT_GRAY);
        label_1.setFont(new Font("SansSerif", Font.BOLD, 36));
        label_1.setBounds(445, 97, 34, 30);
        Custom.add(label_1);

        JLabel jLabelx = new JLabel();
        jLabelx.setFont(new Font("SansSerif", Font.BOLD, 22));
        jLabelx.setForeground(new java.awt.Color(204, 204, 204));
        jLabelx.setText("SET TANK SIZE");
        Custom.add(jLabelx);
        jLabelx.setBounds(25, 160, 282, 50);

        JLabel custom_tank_size_tens = new JLabel("1");
        custom_tank_size_tens.setForeground(Color.LIGHT_GRAY);
        custom_tank_size_tens.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel custom_tank_size_ones = new JLabel("0");
        custom_tank_size_ones.setForeground(Color.LIGHT_GRAY);
        custom_tank_size_ones.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel custom_tank_size_hundred = new JLabel("0");
        custom_tank_size_hundred.setForeground(Color.LIGHT_GRAY);
        custom_tank_size_hundred.setHorizontalAlignment(SwingConstants.CENTER);

        custom_tank_size_hundred.setFont(new Font("SansSerif", Font.BOLD, 40));
        custom_tank_size_hundred.setBounds(400, 157, 35, 50);
        Custom.add(custom_tank_size_hundred);

        custom_tank_size_tens.setFont(new Font("SansSerif", Font.BOLD, 40));
        // tank_size.setHorizontalAlignment(JTextField.CENTER);
        Custom.add(custom_tank_size_tens);
        custom_tank_size_tens.setBounds(431, 157, 35, 50);

        custom_tank_size_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
        custom_tank_size_ones.setBounds(461, 157, 35, 50);
        Custom.add(custom_tank_size_ones);

        JLabel lblNewLabel_31 = new JLabel("GALLONS");
        lblNewLabel_31.setForeground(Color.LIGHT_GRAY);
        lblNewLabel_31.setFont(new Font("SansSerif", Font.BOLD, 25));
        lblNewLabel_31.setBounds(531, 151, 137, 67);
        Custom.add(lblNewLabel_31);

        JLabel jLabel61 = new JLabel();
        jLabel61.setFont(new Font("SansSerif", Font.BOLD, 22));
        jLabel61.setForeground(new java.awt.Color(204, 204, 204));
        jLabel61.setText("SPECIFIC TIME FOR LIGHT OFF");
        Custom.add(jLabel61);
        jLabel61.setBounds(25, 320, 445, 50);

        JLabel jLabel51 = new JLabel();
        jLabel51.setFont(new Font("SansSerif", Font.BOLD, 22));
        jLabel51.setForeground(new java.awt.Color(204, 204, 204));
        jLabel51.setText("SPECIFIC TIME FOR LIGHT ON");
        Custom.add(jLabel51);
        jLabel51.setBounds(25, 240, 355, 50);

        JLabel custom_light_off_time_hour = new JLabel("6");
        custom_light_off_time_hour.setForeground(Color.LIGHT_GRAY);
        custom_light_off_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
        custom_light_off_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
        Custom.add(custom_light_off_time_hour);
        custom_light_off_time_hour.setBounds(384, 322, 62, 50);

        JLabel custom_light_off_time_minute = new JLabel("00");
        custom_light_off_time_minute.setForeground(Color.LIGHT_GRAY);
        custom_light_off_time_minute.setHorizontalAlignment(SwingConstants.CENTER);
        custom_light_off_time_minute.setBounds(461, 321, 62, 50);
        custom_light_off_time_minute.setFont(new Font("SansSerif", Font.BOLD, 40));
        Custom.add(custom_light_off_time_minute);

        JLabel custom_off_am_pm = new JLabel("AM");
        custom_off_am_pm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_off_am_pm.getText();
                switch (current) {
                case "AM":
                    custom_off_am_pm.setText("PM");
                    break;
                case "PM":
                    custom_off_am_pm.setText("AM");
                    break;
                }
            }
        });
        custom_off_am_pm.setHorizontalAlignment(SwingConstants.CENTER);
        custom_off_am_pm.setForeground(Color.LIGHT_GRAY);
        custom_off_am_pm.setBounds(568, 321, 82, 50);
        custom_off_am_pm.setFont(new Font("SansSerif", Font.BOLD, 40));
        Custom.add(custom_off_am_pm);

        JLabel custom_light_on_time_hour = new JLabel("12");
        custom_light_on_time_hour.setForeground(Color.LIGHT_GRAY);
        custom_light_on_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
        custom_light_on_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
        // light_on_time_hour.setHorizontalAlignment(JTextField.CENTER);
        Custom.add(custom_light_on_time_hour);
        custom_light_on_time_hour.setBounds(384, 240, 62, 50);

        JLabel custom_on_am_pm = new JLabel("AM");
        custom_on_am_pm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_on_am_pm.getText();
                switch (current) {
                case "AM":
                    custom_on_am_pm.setText("PM");
                    break;
                case "PM":
                    custom_on_am_pm.setText("AM");
                    break;
                }
            }
        });
        custom_on_am_pm.setHorizontalAlignment(SwingConstants.CENTER);
        custom_on_am_pm.setForeground(Color.LIGHT_GRAY);
        custom_on_am_pm.setFont(new Font("SansSerif", Font.BOLD, 40));
        custom_on_am_pm.setBounds(568, 240, 82, 50);
        Custom.add(custom_on_am_pm);

        JLabel custom_light_on_time_minute = new JLabel("00");
        custom_light_on_time_minute.setForeground(Color.LIGHT_GRAY);
        custom_light_on_time_minute.setHorizontalAlignment(SwingConstants.CENTER);
        custom_light_on_time_minute.setFont(new Font("SansSerif", Font.BOLD, 40));
        custom_light_on_time_minute.setBounds(461, 240, 62, 50);
        Custom.add(custom_light_on_time_minute);

        JLabel label1 = new JLabel(":");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setForeground(Color.LIGHT_GRAY);
        label1.setFont(new Font("SansSerif", Font.BOLD, 35));
        label1.setBounds(431, 240, 31, 50);
        Custom.add(label1);

        JLabel label_18 = new JLabel("");
        label_18.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_tank_size_hundred.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current++;
                }
                custom_tank_size_hundred.setText(""+current);
            }
        });
        label_18.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_18.setVerticalAlignment(SwingConstants.TOP);
        label_18.setHorizontalAlignment(SwingConstants.CENTER);
        label_18.setBounds(400, 140, 35, 40);
        Custom.add(label_18);

        JLabel label_19 = new JLabel("");
        label_19.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_tank_size_tens.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current++;
                }
                custom_tank_size_tens.setText(""+current);
            }
        });
        label_19.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_19.setVerticalAlignment(SwingConstants.TOP);
        label_19.setHorizontalAlignment(SwingConstants.CENTER);
        label_19.setBounds(431, 140, 35, 43);
        Custom.add(label_19);

        JLabel label_20 = new JLabel("");
        label_20.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_tank_size_ones.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current++;
                }
                custom_tank_size_ones.setText(""+current);
            }
        });
        label_20.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_20.setVerticalAlignment(SwingConstants.TOP);
        label_20.setHorizontalAlignment(SwingConstants.CENTER);
        label_20.setBounds(461, 140, 35, 43);
        Custom.add(label_20);

        JLabel label_21 = new JLabel("");
        label_21.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(ph_ones.getText().toString());
                if(current == 14){
                    current = 0;
                }else{
                    current++;
                }
                ph_ones.setText(""+current);
            }
        });
        label_21.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_21.setVerticalAlignment(SwingConstants.TOP);
        label_21.setHorizontalAlignment(SwingConstants.CENTER);
        label_21.setBounds(247, 71, 35, 43);
        Custom.add(label_21);

        JLabel label_22 = new JLabel("");
        label_22.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
                int current = Integer.parseInt(ph_oneth.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current ++;
                }
                ph_oneth.setText(""+current);
            }
            
        });
        label_22.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_22.setVerticalAlignment(SwingConstants.TOP);
        label_22.setHorizontalAlignment(SwingConstants.CENTER);
        label_22.setBounds(292, 71, 35, 40);
        Custom.add(label_22);

        JLabel label_23 = new JLabel("");
        label_23.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(ph_tenth.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current ++;
                }
                ph_tenth.setText(""+current);
            
            }
        });
        label_23.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_23.setVerticalAlignment(SwingConstants.TOP);
        label_23.setHorizontalAlignment(SwingConstants.CENTER);
        label_23.setBounds(326, 71, 35, 40);
        Custom.add(label_23);

        JLabel label_24 = new JLabel("");
        label_24.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(temp_tens.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current ++;
                }
                temp_tens.setText(""+current);
            
            }
        });
        label_24.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_24.setVerticalAlignment(SwingConstants.TOP);
        label_24.setHorizontalAlignment(SwingConstants.CENTER);
        label_24.setBounds(400, 71, 35, 40);
        Custom.add(label_24);

        JLabel label_25 = new JLabel("");
        label_25.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(temp_ones.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current ++;
                }
                temp_ones.setText(""+current);
            
            }
        });
        label_25.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_25.setVerticalAlignment(SwingConstants.TOP);
        label_25.setHorizontalAlignment(SwingConstants.CENTER);
        label_25.setBounds(431, 71, 35, 40);
        Custom.add(label_25);

        JLabel label_26 = new JLabel("");
        label_26.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(temp_deci.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current ++;
                }
                temp_deci.setText(""+current);
            
            }
        });
        label_26.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_26.setVerticalAlignment(SwingConstants.TOP);
        label_26.setHorizontalAlignment(SwingConstants.CENTER);
        label_26.setBounds(461, 71, 35, 40);
        Custom.add(label_26);

        JLabel label_27 = new JLabel("");
        label_27.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_light_on_time_hour.getText().toString());
                if(current == 12){
                    current = 1;
                }else{
                    current ++;
                }
                custom_light_on_time_hour.setText(""+current);
            }
        });
        label_27.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_27.setVerticalAlignment(SwingConstants.TOP);
        label_27.setHorizontalAlignment(SwingConstants.CENTER);
        label_27.setBounds(400, 225, 35, 40);
        Custom.add(label_27);

        JLabel label_28 = new JLabel("");
        label_28.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_light_on_time_minute.getText();
                switch (current) {
                case "00":
                    custom_light_on_time_minute.setText("10");
                    break;
                case "10":
                    custom_light_on_time_minute.setText("20");
                    break;
                case "20":
                    custom_light_on_time_minute.setText("30");
                    break;
                case "30":
                    custom_light_on_time_minute.setText("40");
                    break;
                case "40":
                    custom_light_on_time_minute.setText("50");
                    break;
                case "50":
                    custom_light_on_time_minute.setText("60");
                    break;
                case "60":
                    custom_light_on_time_minute.setText("00");
                    break;
                }
            }
        });
        label_28.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_28.setVerticalAlignment(SwingConstants.TOP);
        label_28.setHorizontalAlignment(SwingConstants.CENTER);
        label_28.setBounds(461, 225, 62, 40);
        Custom.add(label_28);

        JLabel label_29 = new JLabel("");
        label_29.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_on_am_pm.getText();
                switch (current) {
                case "AM":
                    custom_on_am_pm.setText("PM");
                    break;
                case "PM":
                    custom_on_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_29.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_29.setVerticalAlignment(SwingConstants.TOP);
        label_29.setHorizontalAlignment(SwingConstants.CENTER);
        label_29.setBounds(584, 225, 52, 40);
        Custom.add(label_29);

        JLabel label_30 = new JLabel("");
        label_30.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_light_off_time_hour.getText().toString());
                if(current == 12){
                    current = 0;
                }else{
                    current ++;
                }
                custom_light_off_time_hour.setText(""+current);
            }
        });
        label_30.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_30.setVerticalAlignment(SwingConstants.TOP);
        label_30.setHorizontalAlignment(SwingConstants.CENTER);
        label_30.setBounds(400, 307, 35, 40);
        Custom.add(label_30);

        JLabel label_31 = new JLabel("");
        label_31.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_light_off_time_minute.getText();
                switch (current) {
                case "00":
                    custom_light_off_time_minute.setText("10");
                    break;
                case "10":
                    custom_light_off_time_minute.setText("20");
                    break;
                case "20":
                    custom_light_off_time_minute.setText("30");
                    break;
                case "30":
                    custom_light_off_time_minute.setText("40");
                    break;
                case "40":
                    custom_light_off_time_minute.setText("50");
                    break;
                case "50":
                    custom_light_off_time_minute.setText("60");
                    break;
                case "60":
                    custom_light_off_time_minute.setText("00");
                    break;
                }
            }
        });
        label_31.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_31.setVerticalAlignment(SwingConstants.TOP);
        label_31.setHorizontalAlignment(SwingConstants.CENTER);
        label_31.setBounds(461, 307, 62, 40);
        Custom.add(label_31);

        JLabel label_32 = new JLabel("");
        label_32.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_off_am_pm.getText();
                switch (current) {
                case "AM":
                    custom_off_am_pm.setText("PM");
                    break;
                case "PM":
                    custom_off_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_32.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon.png"));
        label_32.setVerticalAlignment(SwingConstants.TOP);
        label_32.setHorizontalAlignment(SwingConstants.CENTER);
        label_32.setBounds(584, 307, 52, 40);
        Custom.add(label_32);

        JLabel label_33 = new JLabel("");
        label_33.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(ph_ones.getText().toString());
                if(current == 0){
                    current = 14;
                }else{
                    current--;
                }
                ph_ones.setText(""+current);
            }
        });
        label_33.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_33.setVerticalAlignment(SwingConstants.BOTTOM);
        label_33.setHorizontalAlignment(SwingConstants.CENTER);
        label_33.setBounds(248, 109, 35, 36);
        Custom.add(label_33);

        JLabel label_34 = new JLabel("");
        label_34.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(ph_tenth.getText().toString());
                if(current == 9){
                    current = 0;
                }else{
                    current ++;
                }
                ph_tenth.setText(""+current);
            
            }
        });
        label_34.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_34.setVerticalAlignment(SwingConstants.BOTTOM);
        label_34.setHorizontalAlignment(SwingConstants.CENTER);
        label_34.setBounds(326, 109, 35, 36);
        Custom.add(label_34);

        JLabel label_35 = new JLabel("");
        label_35.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(ph_oneth.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                ph_oneth.setText(""+current);
            
            }
        });
        label_35.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_35.setVerticalAlignment(SwingConstants.BOTTOM);
        label_35.setHorizontalAlignment(SwingConstants.CENTER);
        label_35.setBounds(292, 109, 35, 36);
        Custom.add(label_35);

        JLabel label_36 = new JLabel("");
        label_36.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(temp_tens.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                temp_tens.setText(""+current);
            }
        });
        label_36.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_36.setVerticalAlignment(SwingConstants.BOTTOM);
        label_36.setHorizontalAlignment(SwingConstants.CENTER);
        label_36.setBounds(400, 109, 35, 36);
        Custom.add(label_36);

        JLabel label_37 = new JLabel("");
        label_37.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(temp_ones.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                temp_ones.setText(""+current);
            }
        });
        label_37.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_37.setVerticalAlignment(SwingConstants.BOTTOM);
        label_37.setHorizontalAlignment(SwingConstants.CENTER);
        label_37.setBounds(431, 109, 35, 36);
        Custom.add(label_37);

        JLabel label_38 = new JLabel("");
        label_38.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(temp_deci.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                temp_deci.setText(""+current);
            }
        });
        label_38.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_38.setVerticalAlignment(SwingConstants.BOTTOM);
        label_38.setHorizontalAlignment(SwingConstants.CENTER);
        label_38.setBounds(461, 109, 35, 36);
        Custom.add(label_38);

        JLabel label_39 = new JLabel("");
        label_39.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_tank_size_hundred.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                custom_tank_size_hundred.setText(""+current);
            }
        });
        label_39.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_39.setVerticalAlignment(SwingConstants.BOTTOM);
        label_39.setHorizontalAlignment(SwingConstants.CENTER);
        label_39.setBounds(400, 177, 35, 40);
        Custom.add(label_39);

        JLabel label_40 = new JLabel("");
        label_40.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_tank_size_tens.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                custom_tank_size_tens.setText(""+current);
            }
        });
        label_40.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_40.setVerticalAlignment(SwingConstants.BOTTOM);
        label_40.setHorizontalAlignment(SwingConstants.CENTER);
        label_40.setBounds(431, 177, 35, 40);
        Custom.add(label_40);

        JLabel label_41 = new JLabel("");
        label_41.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_tank_size_ones.getText().toString());
                if(current == 0){
                    current = 9;
                }else{
                    current --;
                }
                custom_tank_size_ones.setText(""+current);
            }
        });
        label_41.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_41.setVerticalAlignment(SwingConstants.BOTTOM);
        label_41.setHorizontalAlignment(SwingConstants.CENTER);
        label_41.setBounds(461, 178, 35, 40);
        Custom.add(label_41);

        JLabel label_42 = new JLabel("");
        label_42.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_light_on_time_hour.getText().toString());
                if(current == 1){
                    current = 12;
                }else{
                    current --;
                }
                custom_light_on_time_hour.setText(""+current);
            }
        });
        label_42.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_42.setVerticalAlignment(SwingConstants.BOTTOM);
        label_42.setHorizontalAlignment(SwingConstants.CENTER);
        label_42.setBounds(400, 265, 35, 40);
        Custom.add(label_42);

        JLabel label_43 = new JLabel("");
        label_43.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_light_on_time_minute.getText().toString();
                switch (current) {
                case "00":
                    custom_light_on_time_minute.setText("60");
                    break;
                case "10":
                    custom_light_on_time_minute.setText("00");
                    break;
                case "20":
                    custom_light_on_time_minute.setText("10");
                    break;
                case "30":
                    custom_light_on_time_minute.setText("20");
                    break;
                case "40":
                    custom_light_on_time_minute.setText("30");
                    break;
                case "50":
                    custom_light_on_time_minute.setText("40");
                    break;
                case "60":
                    custom_light_on_time_minute.setText("50");
                    break;
                }
            }
        });
        label_43.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_43.setVerticalAlignment(SwingConstants.BOTTOM);
        label_43.setHorizontalAlignment(SwingConstants.CENTER);
        label_43.setBounds(461, 265, 62, 40);
        Custom.add(label_43);

        JLabel label_44 = new JLabel("");
        label_44.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_on_am_pm.getText();
                switch (current) {
                case "AM":
                    custom_on_am_pm.setText("PM");
                    break;
                case "PM":
                    custom_on_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_44.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_44.setVerticalAlignment(SwingConstants.BOTTOM);
        label_44.setHorizontalAlignment(SwingConstants.CENTER);
        label_44.setBounds(584, 265, 52, 40);
        Custom.add(label_44);

        JLabel label_45 = new JLabel("");
        label_45.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int current = Integer.parseInt(custom_light_off_time_hour.getText().toString());
                if(current == 1){
                    current = 12;
                }else{
                    current --;
                }
                custom_light_off_time_hour.setText(""+current);
            }
        });
        label_45.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_45.setVerticalAlignment(SwingConstants.BOTTOM);
        label_45.setHorizontalAlignment(SwingConstants.CENTER);
        label_45.setBounds(400, 346, 35, 40);
        Custom.add(label_45);

        JLabel label_46 = new JLabel("");
        label_46.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_light_off_time_minute.getText();
                switch (current) {
                case "00":
                    custom_light_off_time_minute.setText("60");
                    break;
                case "10":
                    custom_light_off_time_minute.setText("00");
                    break;
                case "20":
                    custom_light_off_time_minute.setText("10");
                    break;
                case "30":
                    custom_light_off_time_minute.setText("20");
                    break;
                case "40":
                    custom_light_off_time_minute.setText("30");
                    break;
                case "50":
                    custom_light_off_time_minute.setText("40");
                    break;
                case "60":
                    custom_light_off_time_minute.setText("50");
                    break;
                }
            }
        });
        label_46.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_46.setVerticalAlignment(SwingConstants.BOTTOM);
        label_46.setHorizontalAlignment(SwingConstants.CENTER);
        label_46.setBounds(461, 346, 62, 40);
        Custom.add(label_46);

        JLabel label_47 = new JLabel("");
        label_47.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                String current = custom_off_am_pm.getText();
                switch (current) {
                case "AM":
                    custom_off_am_pm.setText("PM");
                    break;
                case "PM":
                    custom_off_am_pm.setText("AM");
                    break;
                }
            }
        });
        label_47.setIcon(new ImageIcon(
                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/Action-arrow-blue-up-icon - Copy.png"));
        label_47.setVerticalAlignment(SwingConstants.BOTTOM);
        label_47.setHorizontalAlignment(SwingConstants.CENTER);
        label_47.setBounds(584, 346, 52, 40);
        Custom.add(label_47);

        JLabel lblNewLabel1 = new JLabel(":");
        lblNewLabel1.setFont(new Font("SansSerif", Font.BOLD, 35));
        lblNewLabel1.setForeground(Color.LIGHT_GRAY);
        lblNewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel1.setBounds(431, 320, 31, 50);
        Custom.add(lblNewLabel1);
        
        JLabel lblNewLabel_2 = new JLabel("pH:");
        lblNewLabel_2.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblNewLabel_2.setForeground(Color.LIGHT_GRAY);
        lblNewLabel_2.setBounds(175, 97, 72, 41);
        Custom.add(lblNewLabel_2);

        JLabel lblNewLabel_1 = new JLabel("SPECIFIC");
        lblNewLabel_1.setForeground(Color.LIGHT_GRAY);
        lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNewLabel_1.setBounds(25, 85, 253, 50);
        Custom.add(lblNewLabel_1);
        
        JLabel lblNewLabel_4 = new JLabel("&");
        lblNewLabel_4.setForeground(Color.LIGHT_GRAY);
        lblNewLabel_4.setFont(new Font("SansSerif", Font.BOLD, 25));
        lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_4.setBounds(357, 86, 46, 41);
        Custom.add(lblNewLabel_4);

        JLabel custom_back_image = new JLabel();
        custom_back_image.setBounds(0, 0, 800, 600);
        custom_back_image
        .setIcon(new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/darkblue.jpg"));
        Custom.add(custom_back_image);

        custom_clear.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                /*
                 * NEED WORK
                 */
                temp_tens.setText("0");
                temp_ones.setText("0");
                temp_deci.setText("0");
                ph_ones.setText("0");
                ph_oneth.setText("0");
                ph_tenth.setText("0");
                custom_tank_size_hundred.setText("0");
                custom_tank_size_tens.setText("1");
                custom_tank_size_ones.setText("0");
                custom_light_on_time_hour.setText("12");
                custom_light_on_time_minute.setText("00");
                custom_on_am_pm.setText("AM");
                custom_light_off_time_hour.setText("6");
                custom_light_off_time_minute.setText("00");
                custom_off_am_pm.setText("AM");
            }
        });

        /*
         * adding dash panel to Deck of card layout Everything under this will
         * be in this panel
         */
        JPanel Dash = new JPanel();
        Dash.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
        // Dash.setBackground(Color.GREEN);
        Deck.add(Dash, "name_1459752488724759000");
        Welcome_panel.setLayout(null);

        JLabel on_image = new JLabel();
        on_image.setIcon(new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/bulb-icon.png"));
        Dash.add(on_image);
        on_image.setBounds(715, 10, 74, 50);

        javax.swing.JToggleButton jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton1.setFont(new java.awt.Font("SansSerif", 1, 20));
        jToggleButton1.setText("ON/OFF");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // GPIO 16 will be use for light control
                try {
                    if (jToggleButton1.isSelected()) {
                        jToggleButton1.setText("ON/OFF");
                        on_image.setIcon(new ImageIcon(
                                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/light-bulb-icon.png"));
                        Runtime.getRuntime().exec("sudo ./setGPIO 26 0");

                    } else {
                        jToggleButton1.setText("ON/OFF");
                        on_image.setIcon(new ImageIcon(
                                "/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/bulb-icon.png"));
                        Runtime.getRuntime().exec("sudo ./setGPIO 26 1");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Dash.add(jToggleButton1);
        jToggleButton1.setBounds(563, 10, 142, 50);

        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/logo_trans.PNG"));
        Dash.add(logo);
        logo.setBounds(10, 10, 100, 90);

        JLabel light_on_when = new JLabel();
        light_on_when.setFont(new java.awt.Font("SansSerif", 1, 14));
        light_on_when.setForeground(new java.awt.Color(204, 204, 204));
        light_on_when.setText("LIGHT ON: ");
        Dash.add(light_on_when);
        light_on_when.setBounds(563, 60, 170, 20);

        JLabel light_off_when = new JLabel();
        light_off_when.setFont(new java.awt.Font("SansSerif", 1, 14));
        light_off_when.setForeground(new java.awt.Color(204, 204, 204));
        light_off_when.setText("LIGHT OFF: ");
        Dash.add(light_off_when);
        light_off_when.setBounds(563, 75, 170, 19);

        JLabel ph_label = new JLabel();
        ph_label.setFont(new Font("SansSerif", Font.BOLD, 22));
        ph_label.setForeground(new java.awt.Color(204, 204, 204));
        ph_label.setText("Current pH ");
        Dash.add(ph_label);
        ph_label.setBounds(403, 82, 280, 60);

        JLabel Go_to_setings = new JLabel();
        Go_to_setings.setIcon(
                new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/applications_system.png"));
        Go_to_setings.setOpaque(false);
        // Go_to_setings.setContentAreaFilled(false);
        // Go_to_setings.setBorderPainted(false);
        Dash.add(Go_to_setings);
        Go_to_setings.setBounds(700, 366, 100, 60);

        JLabel temp_label = new JLabel();
        temp_label.setFont(new Font("SansSerif", Font.BOLD, 23));
        temp_label.setForeground(new java.awt.Color(204, 204, 204));
        temp_label.setText("Current Temperature");
        Dash.add(temp_label);
        temp_label.setBounds(10, 82, 342, 60);

        JLabel c_time = new JLabel();
        c_time.setFont(new java.awt.Font("SansSerif", 1, 18));
        c_time.setForeground(new java.awt.Color(204, 204, 204));
        c_time.setText("TIME:");
        // c_time.setText("TIME: "+ LocalDateTime.now().getHour()+ " : " +
        // LocalDateTime.now().getMinute());
        Dash.add(c_time);
        c_time.setBounds(290, 10, 60, 60);

        JLabel hr_time = new JLabel("" + LocalDateTime.now().getHour());
        hr_time.setForeground(Color.LIGHT_GRAY);
        hr_time.setBounds(345, 10, 33, 60);
        hr_time.setFont(new java.awt.Font("SansSerif", 1, 18));
        hr_time.setHorizontalAlignment(JLabel.TRAILING);
        Dash.add(hr_time);

        JLabel semi = new JLabel(":");
        semi.setForeground(Color.LIGHT_GRAY);
        semi.setBounds(388, 10, 10, 60);
        semi.setFont(new java.awt.Font("SansSerif", 1, 18));
        semi.setHorizontalAlignment(JLabel.CENTER);
        Dash.add(semi);

        JLabel min_time = new JLabel("" + LocalDateTime.now().getMinute());
        min_time.setForeground(Color.LIGHT_GRAY);
        min_time.setBounds(393, 10, 33, 60);
        min_time.setFont(new java.awt.Font("SansSerif", 1, 18));
        min_time.setHorizontalAlignment(JLabel.TRAILING);
        Dash.add(min_time);
        
        JLabel APM_time = new JLabel("AM");
        APM_time.setForeground(Color.LIGHT_GRAY);
        APM_time.setBounds(430, 10, 33, 60);
        APM_time.setFont(new java.awt.Font("SansSerif", 1, 18));
        APM_time.setHorizontalAlignment(JLabel.TRAILING);
        Dash.add(APM_time);

        JLabel current_temp = new JLabel();
        current_temp.setFont(new Font("SansSerif", Font.BOLD, 90));
        current_temp.setForeground(new java.awt.Color(204, 204, 204));
        current_temp.setText("<html>60.1 &#8457;</html>");
        Dash.add(current_temp);
        current_temp.setBounds(10, 82, 383, 157);

        JLabel current_ph = new JLabel();
        current_ph.setBackground(new java.awt.Color(0, 0, 0));
        current_ph.setFont(new Font("SansSerif", Font.BOLD, 90));
        current_ph.setForeground(new java.awt.Color(204, 204, 204));
        current_ph.setText("10.1");
        Dash.add(current_ph);
        current_ph.setBounds(407, 82, 365, 157);

        JLabel set_temp = new JLabel();
        set_temp.setFont(new Font("SansSerif", Font.BOLD, 15));
        set_temp.setForeground(new java.awt.Color(204, 204, 204));
        set_temp.setText("SET TEMPERATURE: ");
        Dash.add(set_temp);
        set_temp.setBounds(10, 380, 183, 24);

        JLabel set_ph = new JLabel();
        set_ph.setFont(new Font("SansSerif", Font.BOLD, 15));
        set_ph.setForeground(new java.awt.Color(204, 204, 204));
        set_ph.setText("SET pH: ");
        Dash.add(set_ph);
        set_ph.setBounds(10, 402, 183, 24);

        JLabel temp_set_to = new JLabel();
        temp_set_to.setFont(new Font("SansSerif", Font.BOLD, 15));
        temp_set_to.setForeground(new java.awt.Color(204, 204, 204));
        temp_set_to.setBounds(190, 380, 100, 24);
        Dash.add(temp_set_to);

        JLabel ph_set_to = new JLabel();
        ph_set_to.setFont(new Font("SansSerif", Font.BOLD, 15));
        ph_set_to.setForeground(new java.awt.Color(204, 204, 204));
        ph_set_to.setBounds(190, 402, 100, 24);
        Dash.add(ph_set_to);
        
        JLabel tank_set_to = new JLabel("TANK: ");
        tank_set_to.setForeground(Color.LIGHT_GRAY);
        tank_set_to.setFont(new Font("SansSerif", Font.BOLD, 14));
        tank_set_to.setBounds(563, 91, 170, 19);
        Dash.add(tank_set_to);
        
        /*
         * COMPONENT FOR ADDING THE GRAPHS
         */
        JPanel temper_chat = new JPanel();
        temper_chat.setBounds(11, 215, 383, 158);
        temper_chat.setBackground(new Color(84,84,84,100));
        Dash.add(temper_chat);
        temper_chat.add(temper_display);

        JPanel ph_chat = new JPanel();
        ph_chat.setBounds(400, 215, 383, 158);
        ph_chat.setBackground(new Color(84,84,84,100));
        Dash.add(ph_chat);
        ph_chat.add(ph_display);
        
        JLabel back_image = new JLabel();
        back_image.setForeground(Color.LIGHT_GRAY);
        back_image.setIcon(new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/darkblue.jpg"));
        back_image.setText("");
        Dash.add(back_image);
        back_image.setBounds(0, 0, 800, 455);

        /**/

        /*
         * everything below are action listeners actual button will be above
         */
        // --> to setup panel
        Go_to_setings.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent m) {
                in_dash = false; // don't remove if removed display will glitch

                temp_tens.setText("0");
                temp_ones.setText("0");
                temp_deci.setText("0");
                ph_ones.setText("0");
                ph_oneth.setText("0");
                ph_tenth.setText("0");
                custom_tank_size_hundred.setText("0");
                custom_tank_size_tens.setText("1");
                custom_tank_size_ones.setText("0");
                custom_light_on_time_hour.setText("1");
                custom_light_on_time_minute.setText("00");
                custom_light_off_time_hour.setText("1");
                custom_light_off_time_minute.setText("00");
                custom_on_am_pm.setText("AM");
                custom_off_am_pm.setText("AM");
                jComboBox1.setSelectedIndex(0);
                tank_size_hundred.setText("0");
                tank_size_tens.setText("1");
                tank_size_ones.setText("0");
                light_on_time_hour.setText("1");
                light_on_time_minute.setText("00");
                on_am_pm.setText("AM");
                light_off_time_hour.setText("1");
                light_off_time_minute.setText("00");
                off_am_pm.setText("AM");

                // removing panel
                Deck.removeAll();
                Deck.repaint();
                Deck.revalidate();

                // adding panel
                Deck.add(Setup_panel);
                Deck.repaint();
                Deck.revalidate();

            }
        });

        custom_setting.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {

                // removing panel
                Deck.removeAll();
                Deck.repaint();
                Deck.revalidate();

                // adding panel
                Deck.add(Custom);
                Deck.repaint();
                Deck.revalidate();

            }
        });
        After_custom_setup.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                String current = custom_tank_size_hundred.getText() + custom_tank_size_tens.getText()
                        + custom_tank_size_ones.getText();
                int current_size = Integer.parseInt(current);
                if (current_size < 9) {
                    JOptionPane.showMessageDialog(null, "TANK SIZE MUST BE GREATER THAN 10", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                } else if (temp_ones.getText().toString() == "0" && temp_tens.getText().toString() == "0"
                        && temp_deci.getText().toString() == "0") {
                    JOptionPane.showMessageDialog(null, "TEMPERATURE CANNOT BE ZERO", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                } else if (ph_ones.getText().toString() == "0" && ph_oneth.getText().toString() == "0"
                        && ph_tenth.getText().toString() == "0") {
                    JOptionPane.showMessageDialog(null, "PH CANNOT BE ZERO", "ERROR", JOptionPane.ERROR_MESSAGE);

                } else {
                    // read_temp_ph(jComboBox1.getSelectedItem().toString());
                    int time1 = 0, time2 = 0;
                    if (custom_on_am_pm.getText().toString() == "PM") {
                        time1 = Integer.parseInt(custom_light_on_time_hour.getText().toString()) + 12;
                    } else {
                        time1 = Integer.parseInt(custom_light_on_time_hour.getText().toString());
                    }
                    if (custom_off_am_pm.getText().toString() == "PM") {
                        time2 = Integer.parseInt(custom_light_on_time_hour.getText().toString()) + 12;
                    } else {
                        time2 = Integer.parseInt(custom_light_on_time_hour.getText().toString());
                    }

                    light_on = time1 + ":" + custom_light_on_time_minute.getText().toString();
                    light_off = time2 + ":" + custom_light_off_time_minute.getText().toString();

                    tank = "" + current_size;
                    tank_set_to.setText("TANK: "+tank);
                    /*
                     * setting settings
                     * 
                     */
                    String light_off_text = "LIGHT OFF: " + light_off;
                    String light_on_text = "LIGHT ON:   " + light_on;
                    String temprature_text = "<html>60.1 &#8457;</html>";
                    String ph_text = "10.1";

                    time_on_hour = Integer.parseInt(custom_light_on_time_hour.getText().toString());
                    time_on_minute = Integer.parseInt((custom_light_on_time_minute.getText().toString()));
                    
                    light_off_when.setText(light_off_text);
                    light_on_when.setText(light_on_text);
                    String set_temp = temp_tens.getText().toString() + temp_ones.getText().toString() + "."
                            + temp_deci.getText().toString();
                    String set_ph = ph_ones.getText().toString() + "." + ph_oneth.getText().toString()
                            + ph_tenth.getText().toString();
                    temp_set_to.setText("" + set_temp);
                    ph_set_to.setText("" + set_ph);

                    back_to_main.setVisible(true);
                    in_dash = true; // don't remove if removed display will glitch

                    // removing panel
                    Deck.removeAll();
                    Deck.repaint();
                    Deck.revalidate();

                    // adding panel
                    Deck.add(Dash);
                    Deck.repaint();
                    Deck.revalidate();

                    Engine_On.start();
                }
            }
        });
        Dash.setLayout(null);

        // --> to dash
        After_Setup.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                /*
                 * WORK
                 */
                String currentTankSize = tank_size_hundred.getText() + tank_size_tens.getText()
                        + tank_size_ones.getText();
                if (Integer.parseInt(currentTankSize) < 9) {
                    JOptionPane.showMessageDialog(null, "TANK SIZE MUST BE GREATER THAN 10", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    read_temp_ph(jComboBox1.getSelectedItem().toString());
                    int time1 = 0, time2 = 0;

                    if (on_am_pm.getText().toString() == "PM") {
                        time1 = Integer.parseInt(light_on_time_hour.getText().toString()) + 12;
                    } else {
                        time1 = Integer.parseInt(light_on_time_hour.getText().toString());
                    }
                    if (off_am_pm.getText() == "PM") {
                        time2 = Integer.parseInt(light_on_time_hour.getText().toString()) + 12;
                    } else {
                        time2 = Integer.parseInt(light_on_time_hour.getText().toString());
                    }

                    light_on = time1 + ":" + light_on_time_minute.getText().toString();
                    light_off = time2 + ":" + light_off_time_minute.getText().toString();
                    tank = ""+ Integer.parseInt(currentTankSize);
                    
                    time_on_hour = Integer.parseInt(light_on_time_hour.getText().toString());
                    time_on_minute = Integer.parseInt((light_on_time_minute.getText().toString()));
                    tank_set_to.setText("TANK: " + tank);
                    /*
                     * setting settings
                     * 
                     */
                    String light_off_text = "LIGHT OFF: " + light_off;
                    String light_on_text = "LIGHT ON:   " + light_on;
                    String temperature_text = "<html>&#8457;</html>";
                    String ph_text = "10.1";

                    light_off_when.setText(light_off_text);
                    light_on_when.setText(light_on_text);
                    temp_set_to.setText("" + ideal_temp);
                    ph_set_to.setText("" + ideal_ph);

                    back_to_main.setVisible(true);
                    in_dash = true; // don't remove if removed display will glitch

                    // removing panel
                    Deck.removeAll();
                    Deck.repaint();
                    Deck.revalidate();

                    // adding panel
                    Deck.add(Dash);
                    Deck.repaint();
                    Deck.revalidate();

                    Engine_On.start();

                }
            }
        });
        back_to_main.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                in_dash = true; // don't remove if removed display will glitch

                // removing panel
                Deck.removeAll();
                Deck.repaint();
                Deck.revalidate();

                // adding panel
                Deck.add(Dash);
                Deck.repaint();
                Deck.revalidate();

            }
        });
        Setup_panel.setLayout(null);

        JLabel Setup_background = new JLabel();
        Setup_background
                .setIcon(new ImageIcon("/Users/anishkumaramangalam/Documents/workspace/pane/src/pane/images/darkblue.jpg"));
        Setup_panel.add(Setup_background);
        Setup_background.setBounds(0, 0, 800, 480);

        /*
         * everything below are threads all will move up
         */
        Thread System = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    temper_display.showHistogram(temper_counts, "Last ten temperature reads", 1, 0);
                    temper_display.setBackground(new Color(213,134,145,0));
                    ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 0); 
                    ph_display.setBackground(new Color(213,134,145,0));
                    
                    Thread.sleep(1000);
                    String text ="";
                    String line;
                    Process process_read2;
                    
                    int i = 0;
                    // removing panel
                    Deck.removeAll();
                    Deck.repaint();
                    Deck.revalidate();

                    // adding panel
                    Deck.add(Setup_panel);
                    Deck.repaint();
                    Deck.revalidate();

                    while (true) {
                        try {
                            process_read2 = Runtime.getRuntime().exec("./temp");
                            BufferedReader in = new BufferedReader(new InputStreamReader(process_read2.getInputStream()));
                            temp = Float.parseFloat(in.readLine());
                            process_read2 = Runtime.getRuntime().exec("python ph.py");
                            in = new BufferedReader(new InputStreamReader(process_read2.getInputStream()));
                            ph = Float.parseFloat(in.readLine());
                        }   catch (Exception e) {

                            e.printStackTrace();
                        }
                        
                        Thread.sleep(3000);
                        if(LocalDateTime.now().getHour() < 12)	{
                        	hr_time.setText("" + LocalDateTime.now().getHour());
                        	APM_time.setText("AM");
                        } else	{
                        	hr_time.setText("" + (LocalDateTime.now().getHour()-12));
                        	APM_time.setText("PM");
                        }
                        
                        if (LocalDateTime.now().getMinute() < 10)   {
                            min_time.setText("0" + LocalDateTime.now().getMinute());
                        }   else    {
                            min_time.setText("" + LocalDateTime.now().getMinute());
                        }

                        for (i = 9; i >= 0; i--) { 
                            if (i == 0) {
                                temper_counts[i] = temp;
                                break;
                            }   else    {
                                temper_counts[i] = temper_counts[i-1];
                            }
                        }

                        text = "<html>"+(float)((int)(temp*10))/10+"&#8457;</html>";
                        current_temp.setText(text);

                        for (i = 9; i >= 0; i--) {
                            if (i == 0) {
                                ph_counts[i] = ph;
                                break;
                            }   else    {
                                ph_counts[i] = ph_counts[i-1];
                            }
                        }
                        text = "<html>"+(float)((int)(ph*10))/10+"</html>";
                        current_ph.setText(text);

                        /*
                         *  histogram update 
                         */
                         temper_display.showHistogram(temper_counts, "Last ten temperature reads", 1, 0);
                         temper_display.setBackground(new Color(213,134,145,0));
                         ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 0); 
                         ph_display.setBackground(new Color(213,134,145,0));

                         if (in_dash) { // makes shore transparency works 

                             // removing panel
                             Deck.removeAll();
                             //Deck.repaint();
                             Deck.revalidate();

                             // adding panel
                             Deck.add(Dash);
                             Deck.repaint();
                             Deck.revalidate();
                         }
                    }

                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        });

        /*
         * ENGINE START
         */
        Engine_On = new Thread(new Runnable() {
            @Override
            public void run() {
                Process process_read;
                float average_temp = 0;
                float average_ph = 0;
                int ml_time = 100; // time in milliseconds
                int i = 0;
                while (true) {

                    /*
                     * FINDING THE CURRENT READINGS AND CALCULATING THE AVERAGE
                     * READINGS
                     */
                    float current_temp = 0;
                    float current_ph = 0;

                    try {
                        process_read = Runtime.getRuntime().exec("./temp");

                        BufferedReader in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));
                        String line;

                        if ((line = in.readLine()) != null) {
                            current_temp = Float.parseFloat(line);
                            //current_temp = (float) ((float) current_temp * 1.800 + 32.0);
                            /*
    `                        * tempratuer tap Warning do not remove
                             * if removed reading will not update
                             */
							//temp = current_temp; // 
                        }

                        /*
                         * HEAT ELEMENT
                         */
                        while (!((current_temp > (float) ideal_temp - 2) && (current_temp < (float) ideal_temp + 2))) {
                            if (current_temp > ideal_temp) {

                                break;
                            } else {
                                Runtime.getRuntime().exec("sudo ./setGPIO 19 0"); 
                                // ASSUMING GPIO PIN 1 is for heater
                                Thread.sleep(1000); // THREAD WAIT FOR 1s TO
                                                    // TAKE THE NEXT READING

                                process_read = Runtime.getRuntime().exec("./temp");

                                in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));
                                if ((line = in.readLine()) != null) {
                                    current_temp = Float.parseFloat(line);
                                    current_temp = (float) ((float) current_temp * 1.800 + 32.0);
                                    /*
            `                        * tempratuer tap Warning do not remove
                                     * if removed reading will not update
                                     */
                                    temp = current_temp; // 
                                }
                            }
                        }
                        Runtime.getRuntime().exec("sudo ./setGPIO 19 1"); 
                        // TURN OFF HEATER

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // OPENING ph.py TO READ THE PH
                    try {
                        process_read = Runtime.getRuntime().exec("python ph.py");

                        BufferedReader in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));
                        String line;

                        if ((line = in.readLine()) != null) {
                            current_ph = Float.parseFloat(line);
                            current_ph = (float) (Math.round(current_ph * 100.0) / 100.0);
                            // ROUNDING THE READING TO BE 2 DECIMAL PLACE, SO READINGS ARE CONSISTENT
                            /*
            `                * tempratuer tap Warning do not remove
                             * if removed reading will not update
                             */
                            //ph = current_ph; // 
                        }

                        /*
                         * DISPENSER
                         */
                        while (!((current_ph > (float) ideal_ph - 0.01) && (current_ph < (float) ideal_ph + 0.01))) {
                            if (current_ph > ideal_ph) {
                                Runtime.getRuntime().exec("sudo ./setGPIO 6 1"); 
                                // SET GPIO PIN 6 TO LOW INCASE IT WAS SET HIGH
                                Runtime.getRuntime().exec("sudo ./setGPIO 13 0"); 
                                // ASSUMING GPIO PIN 26 for DISPENSER A

                                Thread.sleep(ml_time * Integer.parseInt(tank));
                                Runtime.getRuntime().exec("sudo ./setGPIO 13 1"); 
                                //TURN OFF DISPENSER A
                                process_read = Runtime.getRuntime().exec("python ph.py");
								
                                in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));

                                if ((line = in.readLine()) != null) {
                                    current_ph = Float.parseFloat(line);
                                    current_ph = (float) (Math.round(current_ph * 100.0) / 100.0); 
                                    // ROUNDING THE READING TO BE 2 DECIMAL PLACE, SO READINGS ARE CONSISTENT
                                    /*
            `                        * tempratuer tap Warning do not remove
                                     * if removed reading will not update
                                     */
                                    //ph = current_ph; // 
                                }
                            } else {
                                Runtime.getRuntime().exec("sudo ./setGPIO 13 1"); 
                                // SET GPIO PIN 26 TO LOW INCASE IT WAS SET HIGH
                                Runtime.getRuntime().exec("sudo ./setGPIO 6 0"); 
                                // ASSUMING GPIO PIN 25 for DISPENSER B

                                Thread.sleep(ml_time * Integer.parseInt(tank));
                                Runtime.getRuntime().exec("sudo ./setGPIO 6 1"); 
                                //TURN OFF DISPENSER B
                                process_read = Runtime.getRuntime().exec("pyton ph.py");

                                in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));

                                if ((line = in.readLine()) != null) {
                                    current_ph = Float.parseFloat(line);
                                    current_ph = (float) (Math.round(current_ph * 100.0) / 100.0); 
                                    // ROUNDING THE READING TO BE 2 DECIMAL PLACE, SO READINGS ARE CONSISTENT
                                    /*
            `                        * tempratuer tap Warning do not remove
                                     * if removed reading will not update
                                     */
                                    //ph = current_ph; // 
                                }
                            }
                        }
                        Runtime.getRuntime().exec("sudo ./setGPIO 13 1");
                        Runtime.getRuntime().exec("sudo ./setGPIO 6 1");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*
                     * LIGHT ELEMENT
                     */
                    if (LocalDateTime.now().getHour() == time_on_hour
                            && LocalDateTime.now().getMinute() == time_on_minute) {
                        /*
                         * SET LIGHT ON
                         */
                        try {
                            Runtime.getRuntime().exec("sudo ./setGPIO 19 0"); 
                            // ASSSUMING GPIO PIN 14 USE FOR LIGHT ELEMENT
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (LocalDateTime.now().getHour() == time_off_hour
                            && LocalDateTime.now().getMinute() == time_off_minute) {
                        /*
                         * SET LIGHT OFF
                         */
                        try {
                            Runtime.getRuntime().exec("sudo ./setGPIO 19 1"); 
                            // ASSSUMING GPIO PIN 14 USE FOR LIGHT ELEMENT
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        // END//

        System.start();
    }
}
