import javax.imageio.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class src_nemo2 {

    private JFrame frame;
    private JLabel text;
    private JLabel temper;
    private JLabel PH;
    private JLabel PHs;
    private JLabel light;
    private JLabel temps;
    private JPanel Dash;
    private JPanel temper_chat;
    private JPanel ph_chat;
    private boolean read = true;
    
    String texts, onoff = "on";
    String[] info = { "null", "null", "null" };
    String temperR, pH;
    Scanner scan = new Scanner(System.in);
    InputStream stdinT, stdinP, stdInfo1, stdInfo3;
    Process processT, processP, processInfo1, processInfo3;
    Thread temperrature_read, ph_read, lights;
    JTabbedPane tabbedPane;
    private JButton light_btn;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    src_nemo2 window = new src_nemo2();
                    window.frame.setVisible(true);

                    //window.frame
                    window.frame.setBackground(new Color(57,115,157));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public src_nemo2() {
        
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame();
        frame.setBounds(100, 100, 800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        float[] temper_counts = {0,0,0,0,0,0,0,0,0,0};
        Histogram temper_display = new Histogram();
        float[] ph_counts = {0,0,0,0,0,0,0,0,0,0};
        Histogram ph_display = new Histogram();
        
        temperrature_read = new Thread(new Runnable() {
            
            @Override
            public void run() {
                int i = 0;
                while(true) {
                  
                    try {
                        processT = Runtime.getRuntime().exec("./temp");
                        stdinT = processT.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stdinT));
                            
                        if((temperR = reader.readLine ()) != null) {
                            for (i = 9; i >= 0; i--) {
                                if (i == 0) {
                                    float ftemp = Float.parseFloat(temperR);
                                    int itemp = (int)(ftemp*10);
                                    temper_counts[i] = ((float)itemp)/10;
                                    break;
                                }   else    {
                                    temper_counts[i] = temper_counts[i-1];
                                }
                            }
                            String text = "<html><font size=\"3\">&#32;Current temperrature :<br> &#32;</font><font size=\"8\">"+temperR+"</font></html>";
                            temper.setText(text);
                            System.out.println("\u0020temperrature : "+temperR);
                        }   else    {
                            temper.setText("\u0020"+"system error");
                        }
                        Thread.sleep(3000);
                            
                    }catch (Exception er)  {
                        System.out.println(er+" at file response");
                        temper.setText("\u0020"+"system error");
                    }
                }
            }
            
        });
        
        /*  pH sensor   */
        ph_read = new Thread(new Runnable() {
            
            @Override
            public void run() {
                int i = 0;
                while(true) {

                    try {
                        processP = Runtime.getRuntime().exec("python ph.py");
                        stdinP = processP.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stdinP));
                            
                        if((pH = reader.readLine ()) != null) {

                            for (i = 9; i >= 0; i--) {
                                if (i == 0) {
                                    float ftemp = Float.parseFloat(pH);
                                    int itemp = (int)(ftemp*10);
                                    ph_counts[i] = ((float)itemp)/10;
                                    break;
                                }   else    {
                                    ph_counts[i] = ph_counts[i-1];
                                }
                            }
                            String text = "<html><font size=\"3\">&#32;Current pH :<br> &#32;</font><font size=\"8\">"+pH+"</font></html>";
                            PH.setText(text);
                            System.out.println("\u0020pH : "+pH);
                        }   else    {
                            PH.setText("\u0020"+"system error");
                        }
                        Thread.sleep(3000);
                            
                    }catch (Exception er)  {
                        System.out.println(er+" at file response");
                        PH.setText("system error");
                    }   
                }
            }
            
        });

        /*  lights   */
        lights = new Thread(new Runnable() {
            
            @Override
            public void run() {
                
                while(true) {
                    
                    try {
                            
                        if (onoff.equals("off")) {

                            onoff = "on";
                                
                        }   else if(onoff.equals("on"))   {

                            onoff = "off";
                                
                        }
                            
                        if (onoff.equals("on")) {
                            temper_display.showHistogram(temper_counts, "Last ten temperrature reads", 1, 1);
                            ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 1);
                            light.setBackground(new Color(204,204,0));
                            String temp = "<html><font color='black'>&#32;&#32;&#32;&#32;lights :"
                                        + "<br> &#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;"+onoff+"</font></html>";
                            Dash.setBackground(new Color(51,0,0));
                            text.setBackground(Color.lightGray);
                            temper.setBackground(Color.lightGray);
                            PH.setBackground(Color.lightGray);
                            light_btn.setBackground(Color.lightGray);
                            ph_chat.setBackground(Color.lightGray);
                            temper_chat.setBackground(Color.lightGray);
                            light.setText(temp);
                        }   else    {
                            temper_display.showHistogram(temper_counts, "Last ten temperrature reads", 1, 0);
                            ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 0);
                            String temp = "<html><font color='white'>&#32;&#32;&#32;&#32;lights :<br> &#32;"+onoff+"</font></html>";
                            Dash.setBackground(new Color(57,115,157));
                            light.setBackground(Color.RED);
                            text.setBackground(Color.WHITE);
                            temper.setBackground(Color.WHITE);
                            PH.setBackground(Color.WHITE);
                            light_btn.setBackground(Color.WHITE);
                            ph_chat.setBackground(Color.WHITE);
                            temper_chat.setBackground(Color.WHITE);
                            light.setText(temp);
                        }

                        Thread.sleep(3000);
                            
                    }catch (Exception er)  {
                        System.out.println(er+" at file response");
                        light.setText("system error");
                    }
                }
            }
            
        });

        /* sets the fish info */
        try {
            processInfo1 = Runtime.getRuntime().exec("./a Avg_temp gold_fish");
            stdInfo1 = processInfo1.getInputStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(stdInfo1));
            
            processInfo3 = Runtime.getRuntime().exec("./a Avg_PH gold_fish");
            stdInfo3 = processInfo3.getInputStream();
            BufferedReader reader3 = new BufferedReader(new InputStreamReader(stdInfo3));

            info[0] = "gold fish";
            info[1] = reader1.readLine();
            info[2] = reader3.readLine();

        }catch (Exception er)  {
            System.out.println(er+" at file response");
        }

        AbstractBorder brdr = new TextBubbleBorder(new Color(57,115,157),1,10,0);
        AbstractBorder brdarR = new TextBubbleBorder(Color.BLACK,1,16,10);
        AbstractBorder brdarL = new TextBubbleBorder(Color.BLACK,1,16,10,false);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        Dash = new JPanel();
        tabbedPane.addTab("Dashboard", null, Dash, null);
        Dash.setBackground(new Color(57,115,157));

        //Icon warnIcon = new ImageIcon("/logo.png");
        JLabel logo = new JLabel("");
        logo.setBounds(25, 15, 111, 75);
        logo.setBorder(new TextBubbleBorder(Color.BLACK,3,5,0));
        logo.setOpaque(true);
        Image img = new ImageIcon(this.getClass().getResource("/logo.png")).getImage();
        logo.setIcon(new ImageIcon(img));

        /* loads UI */
        text = new JLabel();
        text.setBounds(170, 15, 200, 75);
        text.setBorder(brdr);
        text.setOpaque(true);
        texts = "<html>&#32;Fish : "+info[0]+"<br><br>&#32;Set temperature : "+info[1]+"\u2103<br>&#32;Set pH : "+info[2]+"</html>";
        text.setText(texts);
        text.setBackground(Color.WHITE);
        text.setFont(new Font("Arial", Font.BOLD, 15));
        
        temper = new JLabel();
        temper.setBounds(22, 120, 170, 75);
        temper.setBorder(brdarR);
        temper.setOpaque(true);
        temper.setText("null");
        temper.setBackground(Color.WHITE);
        temper.setFont(new Font("Arial", Font.BOLD, 25));
       
        light = new JLabel();
        light.setBounds(408, 15, 225, 75);
        light.setOpaque(true);
        light.setBorder(brdr);
        light.setText("null");
        light.setBackground(Color.WHITE);
        light.setFont(new Font("Arial", Font.BOLD, 25));
        
        PH = new JLabel();
        PH.setBounds(410, 120, 125, 75);
        PH.setBorder(brdarR);
        PH.setOpaque(true);
        PH.setText("null");
        PH.setBackground(Color.WHITE);
        PH.setFont(new Font("Arial", Font.BOLD, 25));
        Dash.setLayout(null);
        
        light_btn = new JButton("lights ON / OFF");
        light_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (onoff.equals("off")) {

                    onoff = "on";
                    light.setBackground(new Color(204,204,0));
                    String temp = "<html><font color='black'>&#32;&#32;&#32;&#32;lights :"
                            + "<br> &#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;"+onoff+"</font></html>";
                    Dash.setBackground(new Color(51,0,0));
                    text.setBackground(Color.lightGray);
                    temper.setBackground(Color.lightGray);
                    PH.setBackground(Color.lightGray);
                    light_btn.setBackground(Color.lightGray);
                    ph_chat.setBackground(Color.lightGray);
                    temper_chat.setBackground(Color.lightGray);
                    light.setText(temp);
                    temper_display.showHistogram(temper_counts, "Last ten temperrature reads", 1, 1);
                    ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 1);
                    
                }   else if(onoff.equals("on"))   {

                    onoff = "off";
                    String temp = "<html><font color='white'>&#32;&#32;&#32;&#32;lights :<br> &#32;"+onoff+"</font></html>";
                    Dash.setBackground(new Color(57,115,157));
                    light.setBackground(Color.RED);
                    text.setBackground(Color.WHITE);
                    temper.setBackground(Color.WHITE);
                    PH.setBackground(Color.WHITE);
                    light_btn.setBackground(Color.WHITE);
                    ph_chat.setBackground(Color.WHITE);
                    temper_chat.setBackground(Color.WHITE);
                    light.setText(temp); 
                    temper_display.showHistogram(temper_counts, "Last ten temperrature reads", 1, 0);
                    ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 0);           
                }
            }
        });
        light_btn.setBounds(638, 13, 123, 77);

        Dash.add(light_btn);
        Dash.add(light);
        Dash.add(text);
        Dash.add(temper);
        Dash.add(PH);
        Dash.add(logo);

        temper_chat = new JPanel();
        temper_chat.setBounds(22, 195, 350, 120);
        temper_chat.setBorder(brdr);
        Dash.add(temper_chat);
        temper_chat.add(temper_display);
        
        ph_chat = new JPanel();
        ph_chat.setBounds(408, 195, 350, 120);
        ph_chat.setBorder(brdr);
        Dash.add(ph_chat);
        ph_chat.add(ph_display);

        JPanel change = new JPanel();
        tabbedPane.addTab("change fish", null, change, null);
        change.setLayout(null);
        change.setBackground(new Color(57,115,157));
                
        JPanel setting = new JPanel();
        tabbedPane.addTab("settings", null, setting, null);
        setting.setLayout(null);
        setting.setBackground(new Color(57,115,157));
        
        JPanel about = new JPanel();
        tabbedPane.addTab("About Us", null, about, null);
        about.setLayout(null);
        about.setBackground(new Color(57,115,157));
                
        JFormattedTextField formattedTextField = new JFormattedTextField();
        formattedTextField.setBounds(161, 33, 92, 127);
        about.add(formattedTextField);
        
        temperrature_read.start();
        ph_read.start();
        lights.start();
    }
}
