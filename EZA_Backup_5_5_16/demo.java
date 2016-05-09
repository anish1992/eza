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

public class demo extends JFrame {

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
	private int time_on_hour = 8;
	private int time_on_minute = 0;
	private String time_on_ampm = "AM";
	private int time_off_hour = 10;
	private int time_off_minute = 0;
	private String time_off_ampm = "PM";

	private Boolean is_set;
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
					demo frame = new demo();
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
			BufferedReader reader = new BufferedReader(new FileReader("./images/data.txt"));
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
			BufferedReader reader = new BufferedReader(new FileReader("./images/data.txt"));
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
	public demo() {

		super("EZ Aquarium");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setResizable(false);
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(0, 0, 800, 480);
		getContentPane().setLayout(null);

		JPanel Case = new JPanel();
		Case.setBackground(Color.GRAY);
		Case.setBounds(0, 0, 800, 480);
		getContentPane().add(Case);
		Case.setLayout(null);

		final JPanel Deck = new JPanel();
		Deck.setBounds(1, 1, 797, 455);
		Case.add(Deck);
		Deck.setLayout(new CardLayout(0, 0));

		/*
		 * data and object for Histogram
		 */
		final float[] temper_counts = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		final Histogram temper_display = new Histogram();
		final float[] ph_counts = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		final Histogram ph_display = new Histogram();

		/*
		 * adding Welcome_panel panel to Deck of card layout Everything under
		 * this will be in this panel
		 */
		JPanel Welcome_panel = new JPanel();
		Welcome_panel
				.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
		Deck.add(Welcome_panel, "name_1459752272475044000");

		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("./images/welcome.png"));
		background.setBounds(-123, -84, 1024, 600);
		Welcome_panel.add(background);

		/*
		 * adding Setup_panel panel to Deck of card layout Everything under this
		 * will be in this panel
		 */
		final JPanel Setup_panel = new JPanel();
		Setup_panel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
		Deck.add(Setup_panel, "name_1459752478502634000");

		final JLabel back_to_main = new JLabel();
		back_to_main.setFont(new Font("SansSerif", Font.BOLD, 20));
		back_to_main.setBounds(25, 25, 62, 50);
		back_to_main.setIcon(new ImageIcon("./images/Actions-go-previous-icon.png"));
		Setup_panel.add(back_to_main);
		back_to_main.setVisible(false);

		JLabel jLabel4 = new JLabel();
		jLabel4.setFont(new Font("SansSerif", Font.BOLD, 22));
		jLabel4.setForeground(new java.awt.Color(204, 204, 204));
		jLabel4.setText("SET TANK SIZE");
		Setup_panel.add(jLabel4);
		jLabel4.setBounds(25, 160, 337, 50);

		final JComboBox jComboBox1 = new JComboBox();
		jComboBox1.setFont(new java.awt.Font("SansSerif", 1, 14));
		jComboBox1.setForeground(new java.awt.Color(51, 51, 51));
		// jComboBox1.addItem(new DefaultComboBoxModel(content.toArray()));//
		// add from data base
		for (int i = 0; i < num_fish; i++) {
			jComboBox1.addItem(content.get(i));
		}
		Setup_panel.add(jComboBox1);
		jComboBox1.setBounds(400, 95, 250, 28);

		final JLabel tank_size_tens = new JLabel("1");
		tank_size_tens.setHorizontalAlignment(SwingConstants.CENTER);
		tank_size_tens.setForeground(Color.LIGHT_GRAY);
		tank_size_tens.setFont(new Font("SansSerif", Font.BOLD, 40));
		// tank_size.setHorizontalAlignment(JTextField.CENTER);
		Setup_panel.add(tank_size_tens);
		tank_size_tens.setBounds(431, 157, 35, 50);

		final JLabel tank_size_ones = new JLabel("0");
		tank_size_ones.setHorizontalAlignment(SwingConstants.CENTER);
		tank_size_ones.setForeground(Color.LIGHT_GRAY);
		tank_size_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
		tank_size_ones.setBounds(461, 157, 35, 50);
		Setup_panel.add(tank_size_ones);

		final JLabel tank_size_hundred = new JLabel("0");
		tank_size_hundred.setHorizontalAlignment(SwingConstants.TRAILING);
		tank_size_hundred.setForeground(Color.LIGHT_GRAY);
		tank_size_hundred.setFont(new Font("SansSerif", Font.BOLD, 40));
		tank_size_hundred.setBounds(377, 155, 51, 50);

		Setup_panel.add(tank_size_hundred);

		JLabel lblNewLabel_3 = new JLabel("GALLONS");
		lblNewLabel_3.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_3.setFont(new Font("SansSerif", Font.BOLD, 25));
		lblNewLabel_3.setBounds(531, 151, 137, 67);
		Setup_panel.add(lblNewLabel_3);

		final JLabel light_on_time_hour = new JLabel();
		light_on_time_hour.setForeground(Color.LIGHT_GRAY);
		light_on_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
		light_on_time_hour.setText("8");
		light_on_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
		Setup_panel.add(light_on_time_hour);
		light_on_time_hour.setBounds(384, 240, 62, 50);

		final JLabel on_am_pm = new JLabel();
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

		final JLabel light_on_time_minute = new JLabel();
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
		jLabel6.setText("TIME FOR LIGHT OFF");
		Setup_panel.add(jLabel6);
		jLabel6.setBounds(25, 320, 357, 50);

		JLabel jLabel5 = new JLabel();
		jLabel5.setFont(new Font("SansSerif", Font.BOLD, 22));
		jLabel5.setForeground(new java.awt.Color(204, 204, 204));
		jLabel5.setText("TIME FOR LIGHT ON");
		Setup_panel.add(jLabel5);
		jLabel5.setBounds(25, 240, 357, 50);

		final JLabel light_off_time_hour = new JLabel();
		light_off_time_hour.setForeground(Color.LIGHT_GRAY);
		light_off_time_hour.setText("10");
		light_off_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
		light_off_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
		Setup_panel.add(light_off_time_hour);
		light_off_time_hour.setBounds(384, 322, 62, 50);

		final JLabel light_off_time_minute = new JLabel();
		light_off_time_minute.setText("00");
		light_off_time_minute.setForeground(Color.LIGHT_GRAY);
		light_off_time_minute.setHorizontalAlignment(SwingConstants.CENTER);
		light_off_time_minute.setBounds(461, 321, 62, 50);
		light_off_time_minute.setFont(new Font("SansSerif", Font.BOLD, 40));
		Setup_panel.add(light_off_time_minute);

		final JLabel off_am_pm = new JLabel();
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
		off_am_pm.setText("PM");
		off_am_pm.setBounds(568, 322, 82, 50);
		off_am_pm.setFont(new Font("SansSerif", Font.BOLD, 40));
		Setup_panel.add(off_am_pm);

		JLabel After_Setup = new JLabel();
		After_Setup.setFont(new java.awt.Font("SansSerif", 1, 26));
		After_Setup.setIcon(new ImageIcon("./images/Actions-go-next-icon.png"));
		// action listener at the bottom
		Setup_panel.add(After_Setup);
		After_Setup.setBounds(700, 382, 100, 60);

		JLabel jButton2 = new JLabel();
		jButton2.setIcon(new ImageIcon("./images/Actions-edit-clear-icon.png"));
		jButton2.setFont(new java.awt.Font("SansSerif", 1, 26));
		jButton2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				jComboBox1.setSelectedIndex(0);
				tank_size_hundred.setText("0");
				tank_size_tens.setText("1");
				tank_size_ones.setText("0");
				light_on_time_hour.setText("8");
				light_on_time_minute.setText("00");
				on_am_pm.setText("AM");
				light_off_time_hour.setText("10");
				light_off_time_minute.setText("00");
				off_am_pm.setText("PM");

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
		tank_hundred_up.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		tank_hundred_up.setBounds(400, 140, 35, 40);
		tank_hundred_up.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(tank_size_hundred.getText().toString());
				if (current == 10) {
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
		label_2.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_3.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_4.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		label_5.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		label_6.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_7.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setBounds(400, 307, 35, 40);
		Setup_panel.add(label_7);

		JLabel label_8 = new JLabel("");
		label_8.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
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
		label_8.setVerticalAlignment(SwingConstants.TOP);
		label_8.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_9.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
					light_on_time_minute.setText("00");
					break;
				}
			}
		});
		label_10.setVerticalAlignment(SwingConstants.TOP);
		label_10.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
					light_off_time_minute.setText("00");
					break;
				}
			}
		});
		label_11.setVerticalAlignment(SwingConstants.TOP);
		label_11.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_12.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_12.setHorizontalAlignment(SwingConstants.CENTER);
		label_12.setBounds(400, 265, 35, 40);
		Setup_panel.add(label_12);

		JLabel label_13 = new JLabel("");
		label_13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String current = light_on_time_minute.getText();
				switch (current) {
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
				case "00":
					light_on_time_minute.setText("50");
					break;
				}
			}
		});
		label_13.setVerticalAlignment(SwingConstants.BOTTOM);
		label_13.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_13.setHorizontalAlignment(SwingConstants.CENTER);
		label_13.setBounds(461, 265, 62, 40);
		Setup_panel.add(label_13);

		JLabel label_14 = new JLabel("");
		label_14.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
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
		label_14.setVerticalAlignment(SwingConstants.BOTTOM);
		label_14.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		label_15.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_15.setHorizontalAlignment(SwingConstants.CENTER);
		label_15.setBounds(400, 346, 35, 40);
		Setup_panel.add(label_15);

		JLabel label_16 = new JLabel("");
		label_16.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				String current = light_off_time_minute.getText();
				switch (current) {
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
				case "00":
					light_off_time_minute.setText("50");
					break;
				}
			}
		});
		label_16.setVerticalAlignment(SwingConstants.BOTTOM);
		label_16.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		label_17.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_17.setHorizontalAlignment(SwingConstants.CENTER);
		label_17.setBounds(584, 346, 52, 40);
		Setup_panel.add(label_17);
		Setup_panel.add(tank_hundred_up);

		JLabel tank_hundred_down = new JLabel("");
		tank_hundred_down.setVerticalAlignment(SwingConstants.BOTTOM);
		tank_hundred_down.setHorizontalAlignment(SwingConstants.CENTER);
		tank_hundred_down.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		tank_hundred_down.setBounds(400, 177, 35, 40);
		tank_hundred_down.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(tank_size_hundred.getText().toString());
				if (current == 0) {
					current = 10;
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
		final JPanel Custom = new JPanel();
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
		custom_clear.setIcon(new ImageIcon("./images/Actions-edit-clear-icon.png"));
		Custom.add(custom_clear);
		custom_clear.setBounds(25, 380, 200, 50);

		JLabel After_custom_setup = new JLabel();
		After_custom_setup.setFont(new java.awt.Font("SansSerif", 1, 26));
		After_custom_setup.setIcon(new ImageIcon("./images/Actions-go-next-icon.png"));
		// action listener at the bottom
		Custom.add(After_custom_setup);
		After_custom_setup.setBounds(700, 382, 100, 60);

		JLabel back_to_setting = new JLabel();
		back_to_setting.setFont(new Font("SansSerif", Font.BOLD, 20));
		back_to_setting.setBounds(25, 25, 62, 50);
		back_to_setting.setIcon(new ImageIcon("./images/Actions-go-previous-icon.png"));
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

		final JLabel temp_ones = new JLabel("0");
		temp_ones.setForeground(Color.LIGHT_GRAY);
		temp_ones.setHorizontalAlignment(SwingConstants.CENTER);
		temp_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
		temp_ones.setBounds(531, 86, 35, 50);
		Custom.add(temp_ones);

		final JLabel temp_tens = new JLabel("0");
		temp_tens.setHorizontalAlignment(SwingConstants.TRAILING);
		temp_tens.setForeground(Color.LIGHT_GRAY);
		temp_tens.setFont(new Font("SansSerif", Font.BOLD, 40));
		temp_tens.setBounds(461, 86, 62, 50);
		Custom.add(temp_tens);

		final JLabel temp_deci = new JLabel("0");
		temp_deci.setForeground(Color.LIGHT_GRAY);
		temp_deci.setHorizontalAlignment(SwingConstants.CENTER);
		temp_deci.setFont(new Font("SansSerif", Font.BOLD, 40));
		temp_deci.setBounds(565, 86, 35, 50);
		Custom.add(temp_deci);

		JLabel lblNewLabel_5 = new JLabel(".");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("SansSerif", Font.BOLD, 36));
		lblNewLabel_5.setForeground(new Color(192, 192, 192));
		lblNewLabel_5.setBounds(107, 97, 34, 30);
		Custom.add(lblNewLabel_5);

		JLabel lblNewLabel_7 = new JLabel("<html> &#8457;</html>");
		lblNewLabel_7.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_7.setFont(new Font("SansSerif", Font.BOLD, 40));
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(600, 80, 50, 50);
		Custom.add(lblNewLabel_7);

		final JLabel ph_ones = new JLabel("0");
		ph_ones.setForeground(Color.LIGHT_GRAY);
		ph_ones.setHorizontalAlignment(SwingConstants.TRAILING);
		ph_ones.setFont(new Font("SansSerif", Font.BOLD, 40));
		ph_ones.setBounds(55, 86, 62, 50);
		Custom.add(ph_ones);

		final JLabel ph_oneth = new JLabel("0");
		ph_oneth.setForeground(Color.LIGHT_GRAY);
		ph_oneth.setHorizontalAlignment(SwingConstants.CENTER);
		ph_oneth.setFont(new Font("SansSerif", Font.BOLD, 40));
		ph_oneth.setBounds(127, 86, 35, 50);
		Custom.add(ph_oneth);

		final JLabel ph_tenth = new JLabel("0");
		ph_tenth.setForeground(Color.LIGHT_GRAY);
		ph_tenth.setHorizontalAlignment(SwingConstants.CENTER);
		ph_tenth.setFont(new Font("SansSerif", Font.BOLD, 40));
		ph_tenth.setBounds(154, 86, 35, 50);
		Custom.add(ph_tenth);
		///////////////////////////////////////////////////////

		JLabel label_1 = new JLabel(".");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.LIGHT_GRAY);
		label_1.setFont(new Font("SansSerif", Font.BOLD, 36));
		label_1.setBounds(550, 97, 34, 30);
		Custom.add(label_1);

		JLabel jLabelx = new JLabel();
		jLabelx.setFont(new Font("SansSerif", Font.BOLD, 22));
		jLabelx.setForeground(new java.awt.Color(204, 204, 204));
		jLabelx.setText("SET TANK SIZE");
		Custom.add(jLabelx);
		jLabelx.setBounds(25, 160, 282, 50);

		final JLabel custom_tank_size_tens = new JLabel("1");
		custom_tank_size_tens.setForeground(Color.LIGHT_GRAY);
		custom_tank_size_tens.setHorizontalAlignment(SwingConstants.CENTER);
		final JLabel custom_tank_size_ones = new JLabel("0");
		custom_tank_size_ones.setForeground(Color.LIGHT_GRAY);
		custom_tank_size_ones.setHorizontalAlignment(SwingConstants.CENTER);
		final JLabel custom_tank_size_hundred = new JLabel("0");
		custom_tank_size_hundred.setForeground(Color.LIGHT_GRAY);
		custom_tank_size_hundred.setHorizontalAlignment(SwingConstants.TRAILING);

		custom_tank_size_hundred.setFont(new Font("SansSerif", Font.BOLD, 40));
		custom_tank_size_hundred.setBounds(377, 155, 51, 50);
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
		jLabel61.setText("TIME FOR LIGHT OFF");
		Custom.add(jLabel61);
		jLabel61.setBounds(25, 320, 445, 50);

		JLabel jLabel51 = new JLabel();
		jLabel51.setFont(new Font("SansSerif", Font.BOLD, 22));
		jLabel51.setForeground(new java.awt.Color(204, 204, 204));
		jLabel51.setText("TIME FOR LIGHT ON");
		Custom.add(jLabel51);
		jLabel51.setBounds(25, 240, 355, 50);

		final JLabel custom_light_off_time_hour = new JLabel("10");
		custom_light_off_time_hour.setForeground(Color.LIGHT_GRAY);
		custom_light_off_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
		custom_light_off_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
		Custom.add(custom_light_off_time_hour);
		custom_light_off_time_hour.setBounds(384, 322, 62, 50);

		final JLabel custom_light_off_time_minute = new JLabel("00");
		custom_light_off_time_minute.setForeground(Color.LIGHT_GRAY);
		custom_light_off_time_minute.setHorizontalAlignment(SwingConstants.CENTER);
		custom_light_off_time_minute.setBounds(461, 321, 62, 50);
		custom_light_off_time_minute.setFont(new Font("SansSerif", Font.BOLD, 40));
		Custom.add(custom_light_off_time_minute);

		final JLabel custom_off_am_pm = new JLabel("PM");
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
		custom_off_am_pm.setBounds(568, 322, 82, 50);
		custom_off_am_pm.setFont(new Font("SansSerif", Font.BOLD, 40));
		Custom.add(custom_off_am_pm);

		final JLabel custom_light_on_time_hour = new JLabel("8");
		custom_light_on_time_hour.setForeground(Color.LIGHT_GRAY);
		custom_light_on_time_hour.setHorizontalAlignment(SwingConstants.CENTER);
		custom_light_on_time_hour.setFont(new Font("SansSerif", Font.BOLD, 40));
		// light_on_time_hour.setHorizontalAlignment(JTextField.CENTER);
		Custom.add(custom_light_on_time_hour);
		custom_light_on_time_hour.setBounds(384, 240, 62, 50);

		final JLabel custom_on_am_pm = new JLabel("AM");
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

		final JLabel custom_light_on_time_minute = new JLabel("00");
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
				if (current == 10) {
					current = 0;
				} else {
					current++;
				}
				custom_tank_size_hundred.setText("" + current);
			}
		});
		label_18.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		label_18.setVerticalAlignment(SwingConstants.TOP);
		label_18.setHorizontalAlignment(SwingConstants.CENTER);
		label_18.setBounds(400, 140, 35, 40);
		Custom.add(label_18);

		JLabel label_19 = new JLabel("");
		label_19.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_tank_size_tens.getText().toString());
				if (current == 9) {
					current = 0;
				} else {
					current++;
				}
				custom_tank_size_tens.setText("" + current);
			}
		});
		label_19.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		label_19.setVerticalAlignment(SwingConstants.TOP);
		label_19.setHorizontalAlignment(SwingConstants.CENTER);
		label_19.setBounds(431, 140, 35, 43);
		Custom.add(label_19);

		JLabel label_20 = new JLabel("");
		label_20.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_tank_size_ones.getText().toString());
				if (current == 9) {
					current = 0;
				} else {
					current++;
				}
				custom_tank_size_ones.setText("" + current);
			}
		});
		label_20.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		label_20.setVerticalAlignment(SwingConstants.TOP);
		label_20.setHorizontalAlignment(SwingConstants.CENTER);
		label_20.setBounds(461, 140, 35, 43);
		Custom.add(label_20);

		JLabel label_21 = new JLabel("");
		label_21.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(ph_ones.getText().toString());
				if (current == 14) {
					current = 0;
				} else {
					current++;
				}
				ph_ones.setText("" + current);
			}
		});
		label_21.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon.png"));
		label_21.setVerticalAlignment(SwingConstants.TOP);
		label_21.setHorizontalAlignment(SwingConstants.CENTER);
		label_21.setBounds(89, 73, 35, 40);
		Custom.add(label_21);

		JLabel label_22 = new JLabel("");
		label_22.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				int current = Integer.parseInt(ph_oneth.getText().toString());
				if (current == 9) {
					current = 0;
				} else {
					current++;
				}
				ph_oneth.setText("" + current);
			}

		});
		label_22.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon.png"));
		label_22.setVerticalAlignment(SwingConstants.TOP);
		label_22.setHorizontalAlignment(SwingConstants.CENTER);
		label_22.setBounds(127, 73, 35, 40);
		Custom.add(label_22);

		JLabel label_23 = new JLabel("");
		label_23.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(ph_tenth.getText().toString());
				if (current == 9) {
					current = 0;
				} else {
					current++;
				}
				ph_tenth.setText("" + current);

			}
		});
		label_23.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon.png"));
		label_23.setVerticalAlignment(SwingConstants.TOP);
		label_23.setHorizontalAlignment(SwingConstants.CENTER);
		label_23.setBounds(154, 73, 35, 40);
		Custom.add(label_23);

		JLabel label_24 = new JLabel("");
		label_24.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(temp_tens.getText().toString());
				if (current == 11) {
					current = 0;
				} else {
					current++;
				}
				temp_tens.setText("" + current);

			}
		});
		label_24.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon.png"));
		label_24.setVerticalAlignment(SwingConstants.TOP);
		label_24.setHorizontalAlignment(SwingConstants.CENTER);
		label_24.setBounds(495, 73, 35, 40);
		Custom.add(label_24);

		JLabel label_25 = new JLabel("");
		label_25.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(temp_ones.getText().toString());
				if (current == 9) {
					current = 0;
				} else {
					current++;
				}
				temp_ones.setText("" + current);

			}
		});
		label_25.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon.png"));
		label_25.setVerticalAlignment(SwingConstants.TOP);
		label_25.setHorizontalAlignment(SwingConstants.CENTER);
		label_25.setBounds(530, 73, 35, 40);
		Custom.add(label_25);

		JLabel label_26 = new JLabel("");
		label_26.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(temp_deci.getText().toString());
				if (current == 9) {
					current = 0;
				} else {
					current++;
				}
				temp_deci.setText("" + current);

			}
		});
		label_26.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon.png"));
		label_26.setVerticalAlignment(SwingConstants.TOP);
		label_26.setHorizontalAlignment(SwingConstants.CENTER);
		label_26.setBounds(565, 73, 35, 40);
		Custom.add(label_26);
		///////////////////////////////////////////////////////////////////////

		JLabel label_27 = new JLabel("");
		label_27.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_light_on_time_hour.getText().toString());
				if (current == 12) {
					current = 1;
				} else {
					current++;
				}
				custom_light_on_time_hour.setText("" + current);
			}
		});
		label_27.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
					custom_light_on_time_minute.setText("00");
					break;
				}
			}
		});
		label_28.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_29.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		label_29.setVerticalAlignment(SwingConstants.TOP);
		label_29.setHorizontalAlignment(SwingConstants.CENTER);
		label_29.setBounds(584, 225, 52, 40);
		Custom.add(label_29);

		JLabel label_30 = new JLabel("");
		label_30.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_light_off_time_hour.getText().toString());
				if (current == 12) {
					current = 0;
				} else {
					current++;
				}
				custom_light_off_time_hour.setText("" + current);
			}
		});
		label_30.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
					custom_light_off_time_minute.setText("00");
					break;
				}
			}
		});
		label_31.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
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
		label_32.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon.png"));
		label_32.setVerticalAlignment(SwingConstants.TOP);
		label_32.setHorizontalAlignment(SwingConstants.CENTER);
		label_32.setBounds(584, 307, 52, 40);
		Custom.add(label_32);

		JLabel label_33 = new JLabel("");
		label_33.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(ph_ones.getText().toString());
				if (current == 0) {
					current = 14;
				} else {
					current--;
				}
				ph_ones.setText("" + current);
			}
		});
		label_33.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon - Copy.png"));
		label_33.setVerticalAlignment(SwingConstants.BOTTOM);
		label_33.setHorizontalAlignment(SwingConstants.CENTER);
		label_33.setBounds(89, 109, 35, 36);
		Custom.add(label_33);

		JLabel label_34 = new JLabel("");
		label_34.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(ph_tenth.getText().toString());
				if (current == 0) {
					current = 9;
				} else {
					current--;
				}
				ph_tenth.setText("" + current);

			}
		});
		label_34.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon - Copy.png"));
		label_34.setVerticalAlignment(SwingConstants.BOTTOM);
		label_34.setHorizontalAlignment(SwingConstants.CENTER);
		label_34.setBounds(154, 109, 35, 36);
		Custom.add(label_34);

		JLabel label_35 = new JLabel("");
		label_35.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(ph_oneth.getText().toString());
				if (current == 0) {
					current = 9;
				} else {
					current--;
				}
				ph_oneth.setText("" + current);

			}
		});
		label_35.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon - Copy.png"));
		label_35.setVerticalAlignment(SwingConstants.BOTTOM);
		label_35.setHorizontalAlignment(SwingConstants.CENTER);
		label_35.setBounds(127, 109, 35, 36);
		Custom.add(label_35);

		JLabel label_36 = new JLabel("");
		label_36.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(temp_tens.getText().toString());
				if (current == 0) {
					current = 11;
				} else {
					current--;
				}
				temp_tens.setText("" + current);
			}
		});
		label_36.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon - Copy.png"));
		label_36.setVerticalAlignment(SwingConstants.BOTTOM);
		label_36.setHorizontalAlignment(SwingConstants.CENTER);
		label_36.setBounds(495, 109, 35, 36);
		Custom.add(label_36);

		JLabel label_37 = new JLabel("");
		label_37.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(temp_ones.getText().toString());
				if (current == 0) {
					current = 9;
				} else {
					current--;
				}
				temp_ones.setText("" + current);
			}
		});
		label_37.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon - Copy.png"));
		label_37.setVerticalAlignment(SwingConstants.BOTTOM);
		label_37.setHorizontalAlignment(SwingConstants.CENTER);
		label_37.setBounds(530, 109, 35, 36);
		Custom.add(label_37);

		JLabel label_38 = new JLabel("");
		label_38.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(temp_deci.getText().toString());
				if (current == 0) {
					current = 9;
				} else {
					current--;
				}
				temp_deci.setText("" + current);
			}
		});
		label_38.setIcon(new ImageIcon(
				"./images/Action-arrow-blue-up-icon - Copy.png"));
		label_38.setVerticalAlignment(SwingConstants.BOTTOM);
		label_38.setHorizontalAlignment(SwingConstants.CENTER);
		label_38.setBounds(565, 109, 35, 36);
		Custom.add(label_38);
		///////////////////////////////////////////////////////////////////////////

		JLabel label_39 = new JLabel("");
		label_39.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_tank_size_hundred.getText().toString());
				if (current == 0) {
					current = 10;
				} else {
					current--;
				}
				custom_tank_size_hundred.setText("" + current);
			}
		});
		label_39.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_39.setVerticalAlignment(SwingConstants.BOTTOM);
		label_39.setHorizontalAlignment(SwingConstants.CENTER);
		label_39.setBounds(400, 177, 35, 40);
		Custom.add(label_39);

		JLabel label_40 = new JLabel("");
		label_40.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_tank_size_tens.getText().toString());
				if (current == 0) {
					current = 9;
				} else {
					current--;
				}
				custom_tank_size_tens.setText("" + current);
			}
		});
		label_40.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_40.setVerticalAlignment(SwingConstants.BOTTOM);
		label_40.setHorizontalAlignment(SwingConstants.CENTER);
		label_40.setBounds(431, 177, 35, 40);
		Custom.add(label_40);

		JLabel label_41 = new JLabel("");
		label_41.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_tank_size_ones.getText().toString());
				if (current == 0) {
					current = 9;
				} else {
					current--;
				}
				custom_tank_size_ones.setText("" + current);
			}
		});
		label_41.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_41.setVerticalAlignment(SwingConstants.BOTTOM);
		label_41.setHorizontalAlignment(SwingConstants.CENTER);
		label_41.setBounds(461, 177, 35, 40);
		Custom.add(label_41);

		JLabel label_42 = new JLabel("");
		label_42.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_light_on_time_hour.getText().toString());
				if (current == 1) {
					current = 12;
				} else {
					current--;
				}
				custom_light_on_time_hour.setText("" + current);
			}
		});
		label_42.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
				case "00":
					custom_light_on_time_minute.setText("50");
					break;
				}
			}
		});
		label_43.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		label_44.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
		label_44.setVerticalAlignment(SwingConstants.BOTTOM);
		label_44.setHorizontalAlignment(SwingConstants.CENTER);
		label_44.setBounds(584, 265, 52, 40);
		Custom.add(label_44);

		JLabel label_45 = new JLabel("");
		label_45.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int current = Integer.parseInt(custom_light_off_time_hour.getText().toString());
				if (current == 1) {
					current = 12;
				} else {
					current--;
				}
				custom_light_off_time_hour.setText("" + current);
			}
		});
		label_45.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
				case "00":
					custom_light_off_time_minute.setText("50");
					break;
				}
			}
		});
		label_46.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		label_47.setIcon(new ImageIcon("./images/Action-arrow-blue-up-icon - Copy.png"));
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
		lblNewLabel_2.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblNewLabel_2.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_2.setBounds(25, 96, 72, 41);
		Custom.add(lblNewLabel_2);

		JLabel lblNewLabel_4 = new JLabel("TEMP:");
		lblNewLabel_4.setForeground(Color.LIGHT_GRAY);
		lblNewLabel_4.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblNewLabel_4.setBounds(384, 96, 191, 41);
		Custom.add(lblNewLabel_4);

		JLabel custom_back_image = new JLabel();
		custom_back_image.setBounds(0, 0, 800, 600);
		custom_back_image.setIcon(new ImageIcon("./images/darkblue.jpg"));
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
				custom_light_on_time_hour.setText("8");
				custom_light_on_time_minute.setText("00");
				custom_on_am_pm.setText("AM");
				custom_light_off_time_hour.setText("10");
				custom_light_off_time_minute.setText("00");
				custom_off_am_pm.setText("PM");
			}
		});

		/*
		 * adding dash panel to Deck of card layout Everything under this will
		 * be in this panel
		 */
		final JPanel Dash = new JPanel();
		Dash.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE));
		// Dash.setBackground(Color.GREEN);
		Deck.add(Dash, "name_1459752488724759000");
		Welcome_panel.setLayout(null);

		final JLabel on_image = new JLabel();
		on_image.setIcon(new ImageIcon("./images/bulb-icon.png"));
		Dash.add(on_image);
		on_image.setBounds(715, 10, 74, 50);
		

		final javax.swing.JToggleButton jToggleButton1 = new javax.swing.JToggleButton();
		jToggleButton1.setFont(new java.awt.Font("SansSerif", 1, 20));
		jToggleButton1.setText("ON/OFF");
		jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// GPIO 26 will be use for light control
				try {
					if (jToggleButton1.isSelected()) {
						jToggleButton1.setText("ON/OFF");
						on_image.setIcon(new ImageIcon("./images/light-bulb-icon.png"));
						Thread.sleep(100);
						Runtime.getRuntime().exec("sudo ./setGPIO 26 0");
						System.out.println("down Lights ON");

					} else {
						jToggleButton1.setText("ON/OFF");
						on_image.setIcon(new ImageIcon("./images/bulb-icon.png"));
						Thread.sleep(100);
						Runtime.getRuntime().exec("sudo ./setGPIO 26 1");
						System.out.println("down Lights OFF");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		Dash.add(jToggleButton1);
		jToggleButton1.setBounds(563, 10, 142, 50);

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon("./images/logo_trans.png"));
		Dash.add(logo);
		logo.setBounds(10, 10, 100, 90);

		final JLabel light_on_when = new JLabel();
		light_on_when.setFont(new java.awt.Font("SansSerif", 1, 14));
		light_on_when.setForeground(new java.awt.Color(204, 204, 204));
		light_on_when.setText("LIGHT ON: ");
		Dash.add(light_on_when);
		light_on_when.setBounds(563, 60, 202, 20);

		final JLabel light_off_when = new JLabel();
		light_off_when.setFont(new java.awt.Font("SansSerif", 1, 14));
		light_off_when.setForeground(new java.awt.Color(204, 204, 204));
		light_off_when.setText("LIGHT OFF: ");
		Dash.add(light_off_when);
		light_off_when.setBounds(563, 75, 202, 19);

		JLabel ph_label = new JLabel();
		ph_label.setFont(new Font("SansSerif", Font.BOLD, 21));
		ph_label.setForeground(new java.awt.Color(204, 204, 204));
		ph_label.setText("Current pH ");
		Dash.add(ph_label);
		ph_label.setBounds(403, 82, 280, 60);

		JLabel Go_to_setings = new JLabel();
		Go_to_setings.setIcon(new ImageIcon("./images/applications_system.png"));
		Go_to_setings.setOpaque(false);
		// Go_to_setings.setContentAreaFilled(false);
		// Go_to_setings.setBorderPainted(false);
		Dash.add(Go_to_setings);
		Go_to_setings.setBounds(700, 382, 100, 60);

		JLabel temp_label = new JLabel();
		temp_label.setFont(new Font("SansSerif", Font.BOLD, 21));
		temp_label.setForeground(new java.awt.Color(204, 204, 204));
		temp_label.setText("Current Temperature");
		Dash.add(temp_label);
		temp_label.setBounds(10, 82, 280, 60);

		JLabel c_time = new JLabel();
		c_time.setFont(new java.awt.Font("SansSerif", 1, 18));
		c_time.setForeground(new java.awt.Color(204, 204, 204));
		c_time.setText("TIME:");
		// c_time.setText("TIME: "+ LocalDateTime.now().getHour()+ " : " +
		// LocalDateTime.now().getMinute());
		Dash.add(c_time);
		c_time.setBounds(290, 10, 60, 60);

		final JLabel hr_time = new JLabel("" + LocalDateTime.now().getHour());
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

		final JLabel min_time = new JLabel("" + LocalDateTime.now().getMinute());
		min_time.setForeground(Color.LIGHT_GRAY);
		min_time.setBounds(393, 10, 33, 60);
		min_time.setFont(new java.awt.Font("SansSerif", 1, 18));
		min_time.setHorizontalAlignment(JLabel.TRAILING);
		Dash.add(min_time);

		final JLabel APM_time = new JLabel("AM");
		APM_time.setForeground(Color.LIGHT_GRAY);
		APM_time.setBounds(430, 10, 33, 60);
		APM_time.setFont(new java.awt.Font("SansSerif", 1, 18));
		APM_time.setHorizontalAlignment(JLabel.TRAILING);
		Dash.add(APM_time);

		final JLabel current_temp = new JLabel();
		current_temp.setFont(new Font("SansSerif", Font.BOLD, 90));
		current_temp.setForeground(new java.awt.Color(204, 204, 204));
		current_temp.setText("<html>60.1 &#8457;</html>");
		Dash.add(current_temp);
		current_temp.setBounds(10, 82, 383, 157);

		final JLabel current_ph = new JLabel();
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

		final JLabel temp_set_to = new JLabel();
		temp_set_to.setFont(new Font("SansSerif", Font.BOLD, 15));
		temp_set_to.setForeground(new java.awt.Color(204, 204, 204));
		temp_set_to.setBounds(190, 380, 100, 24);
		Dash.add(temp_set_to);

		final JLabel ph_set_to = new JLabel();
		ph_set_to.setFont(new Font("SansSerif", Font.BOLD, 15));
		ph_set_to.setForeground(new java.awt.Color(204, 204, 204));
		ph_set_to.setBounds(190, 402, 100, 24);
		Dash.add(ph_set_to);

		final JLabel tank_set_to = new JLabel("TANK: ");
		tank_set_to.setForeground(Color.LIGHT_GRAY);
		tank_set_to.setFont(new Font("SansSerif", Font.BOLD, 14));
		tank_set_to.setBounds(563, 91, 170, 19);
		Dash.add(tank_set_to);

		/*
		 * COMPONENT FOR ADDING THE GRAPHS
		 */
		JPanel temper_chat = new JPanel();
		temper_chat.setBounds(11, 215, 383, 158);
		temper_chat.setBackground(new Color(84, 84, 84, 100));
		Dash.add(temper_chat);
		temper_chat.add(temper_display);

		JPanel ph_chat = new JPanel();
		ph_chat.setBounds(400, 215, 383, 158);
		ph_chat.setBackground(new Color(84, 84, 84, 100));
		Dash.add(ph_chat);
		ph_chat.add(ph_display);

		JLabel back_image = new JLabel();
		back_image.setForeground(Color.LIGHT_GRAY);
		back_image.setIcon(new ImageIcon("./images/darkblue.jpg"));
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

				//RESET ALL VARIABLES TO DEFAULT
				temp_tens.setText("0");
				temp_ones.setText("0");
				temp_deci.setText("0");
				ph_ones.setText("0");
				ph_oneth.setText("0");
				ph_tenth.setText("0");
				custom_tank_size_hundred.setText("0");
				custom_tank_size_tens.setText("1");
				custom_tank_size_ones.setText("0");
				custom_light_on_time_hour.setText("8");
				custom_light_on_time_minute.setText("00");
				custom_light_off_time_hour.setText("10");
				custom_light_off_time_minute.setText("00");
				custom_on_am_pm.setText("AM");
				custom_off_am_pm.setText("PM");
				jComboBox1.setSelectedIndex(0);
				tank_size_hundred.setText("0");
				tank_size_tens.setText("1");
				tank_size_ones.setText("0");
				light_on_time_hour.setText("8");
				light_on_time_minute.setText("00");
				on_am_pm.setText("AM");
				light_off_time_hour.setText("10");
				light_off_time_minute.setText("00");
				off_am_pm.setText("PM");

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
				// CHECK IF ALL REQUIRED PARAMETERS ARE FILLED
				if (current_size < 10) {
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
					// Parse time on/off into string
					light_on = custom_light_on_time_hour.getText().toString() + " : "
							+ custom_light_on_time_minute.getText().toString() + " "
							+ custom_on_am_pm.getText().toString();
					light_off = custom_light_off_time_hour.getText().toString() + " : "
							+ custom_light_off_time_minute.getText().toString() + " "
							+ custom_off_am_pm.getText().toString();

					tank = "" + current_size;
					tank_set_to.setText("TANK: " + tank);
					/*
					 * setting settings
					 * 
					 */
					//String light_off_text = "LIGHT OFF: " + light_off;
					String light_off_text = "";
					//String light_on_text = "LIGHT ON:   " + light_on;
					String light_on_text = "THIS IS DEMO";

					light_off_when.setText(light_off_text);
					light_on_when.setText(light_on_text);
					
					//STORE VALUE TO ON/OFF TIME
					time_on_hour = Integer.parseInt(custom_light_on_time_hour.getText().toString());
					time_on_minute = Integer.parseInt(custom_light_on_time_hour.getText().toString());
					time_on_ampm = custom_on_am_pm.getText().toString();
					
					time_off_hour = Integer.parseInt(custom_light_off_time_hour.getText().toString());
					time_off_minute = Integer.parseInt(custom_light_off_time_minute.getText().toString());
					time_off_ampm = custom_off_am_pm.getText().toString();
					

					// Make sure leading zeros will not occur in the string.
					String set_temp;
					if (Integer.parseInt(temp_tens.getText().toString()) == 0) {
						set_temp = temp_ones.getText().toString() + "." + temp_deci.getText().toString();
					} else {
						set_temp = temp_tens.getText().toString() + temp_ones.getText().toString() + "."
								+ temp_deci.getText().toString();
					}
					temp_set_to.setText("" + set_temp);
					//SET CUSTOM IDEAL TEMPERATURE
					ideal_temp = Float.parseFloat(set_temp);
					// SET pH.
					String set_ph = ph_ones.getText().toString() + "." + ph_oneth.getText().toString()
							+ ph_tenth.getText().toString();
					ph_set_to.setText("" + set_ph);
					//SET CUSTOM IDEAL pH
					ideal_ph = Float.parseFloat(set_ph);
					back_to_main.setVisible(true);
					in_dash = true; // don't remove if removed display will
									// glitch

					// removing panel
					Deck.removeAll();
					Deck.repaint();
					Deck.revalidate();

					// adding panel
					Deck.add(Dash);
					Deck.repaint();
					Deck.revalidate();

					is_set = true;
					// THREAD WILL ONLY START IF first_set == 0
					if (first_set == 0) {
						first_set++;
						Engine_On.start();
					}

				}
			}
		});
		Dash.setLayout(null);

		// --> to dash
		After_Setup.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				// Parse text of each components into string
				String currentTankSize = tank_size_hundred.getText() + tank_size_tens.getText()
						+ tank_size_ones.getText();

				// validate tank size > 10
				if (Integer.parseInt(currentTankSize) < 10) {
					JOptionPane.showMessageDialog(null, "TANK SIZE MUST BE GREATER THAN 10", "ERROR",
							JOptionPane.ERROR_MESSAGE);
				} else {
					// Read from file to retrieve ph and temperature.
					read_temp_ph(jComboBox1.getSelectedItem().toString());

					// set time for light on and off
					light_on = light_on_time_hour.getText().toString() + " : "
							+ light_on_time_minute.getText().toString() + " " + on_am_pm.getText().toString();
					light_off = light_off_time_hour.getText().toString() + " : "
							+ light_off_time_minute.getText().toString() + " " + off_am_pm.getText().toString();

					tank = "" + Integer.parseInt(currentTankSize);
					// Set tank size
					tank_set_to.setText("TANK: " + tank);

					// Store light on/off
					time_on_hour = Integer.parseInt(light_on_time_hour.getText().toString());
					time_on_minute = Integer.parseInt(light_on_time_minute.getText().toString());
					time_on_ampm = on_am_pm.getText().toString();

					time_off_hour = Integer.parseInt(light_off_time_hour.getText().toString());
					time_off_minute = Integer.parseInt(light_off_time_minute.getText().toString());
					time_off_ampm = off_am_pm.getText().toString();

					/*
					 * setting settings
					 * 
					 */
					//String light_off_text = "LIGHT OFF: " + light_off;
					String light_off_text = "THIS IS A DEMO";
					//String light_on_text = "LIGHT ON:   " + light_on;
					String light_on_text = " ";

					light_off_when.setText(light_off_text);
					light_on_when.setText(light_on_text);
					temp_set_to.setText("" + ideal_temp);
					ph_set_to.setText("" + ideal_ph);

					back_to_main.setVisible(true);
					in_dash = true; // don't remove if removed display will
									// glitch

					// removing panel
					Deck.removeAll();
					Deck.repaint();
					Deck.revalidate();

					// adding panel
					Deck.add(Dash);
					Deck.repaint();
					Deck.revalidate();

					is_set = true;
					// START THREAD ONLY IF FIRST SET == 0
					if (first_set == 0) {
						first_set++;
						Engine_On.start();
					}
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
		Setup_background.setIcon(new ImageIcon("./images/darkblue.jpg"));
		Setup_panel.add(Setup_background);
		Setup_background.setBounds(0, 0, 800, 480);

		/*
		 * everything below are threads all will move up
		 */
		Thread gui = new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					temper_display.showHistogram(temper_counts, "Last ten temperature reads", 1, 0);
					temper_display.setBackground(new Color(213, 134, 145, 0));
					ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 0);
					ph_display.setBackground(new Color(213, 134, 145, 0));

					Thread.sleep(1000);
					String text = "";

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

						Thread.sleep(1000);
						if (LocalDateTime.now().getHour() < 12) {
							hr_time.setText("" + LocalDateTime.now().getHour());
							APM_time.setText("AM");
						} else if (LocalDateTime.now().getHour() == 12) {
							hr_time.setText("" + LocalDateTime.now().getHour());
							APM_time.setText("PM");
						} else {
							hr_time.setText("" + (LocalDateTime.now().getHour() - 12));
							APM_time.setText("PM");
						}

						if (LocalDateTime.now().getMinute() < 10) {
							min_time.setText("0" + LocalDateTime.now().getMinute());
						} else {
							min_time.setText("" + LocalDateTime.now().getMinute());
						}

						for (i = 9; i >= 0; i--) {
							if (i == 0) {
								temper_counts[i] = temp;
								break;
							} else {
								temper_counts[i] = temper_counts[i - 1];
							}
						}

						text = "<html>" + (float) ((int) (temp * 10)) / 10 + "&#8457;</html>";
						current_temp.setText(text);

						for (i = 9; i >= 0; i--) {
							if (i == 0) {
								ph_counts[i] = ph;
								break;
							} else {
								ph_counts[i] = ph_counts[i - 1];
							}
						}
						text = "<html>" + (float) ((int) (ph * 10)) / 10 + "</html>";
						current_ph.setText(text);

						/*
						 * histogram update
						 */
						temper_display.showHistogram(temper_counts, "Last ten temperature reads", 1, 0);
						temper_display.setBackground(new Color(213, 134, 145, 0));
						ph_display.showHistogram(ph_counts, "Last ten pH reads", 0, 0);
						ph_display.setBackground(new Color(213, 134, 145, 0));

						if (in_dash) { // makes shore transparency works

							// removing panel
							Deck.removeAll();
							//Deck.repaint();
							//Deck.revalidate();

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
				double ml_time = 1000.0; // time in milliseconds
				int i = 0;
				int hold_time = 0;
				int hold_hour = 0;
				double ph_wait_time = 0.0;
				double ml_gal = 0.1;
				boolean hold = false;
				// INDICATOR THAT LIGHT HAS BEEN TURN ON FOR THE FIRST TIME
				// FALSE IMPLIES LIGHT HAS NOT BEEN AUTOMATICALLY TURN ON
				// TRUE IMPLIES LIGHT HAS BEEN AUTOMATICALLY TURN ON
				boolean light = false; 
				/*
				 * FINDING THE CURRENT READINGS
				 * 
				 */
				float current_temp = 0;
				float current_ph = 0;
				float prev_temp = 0;

				int current, c_hour;
				current = LocalDateTime.now().getMinute();
				c_hour = LocalDateTime.now().getHour();
				while (true) {

					//System.out.println("IDEAL TEMP: " + ideal_temp + "\tIDEAL pH: " + ideal_ph);
					/*
					 * OPEN TO READ TEMPEARTURE
					 */
					try {
						process_read = Runtime.getRuntime().exec("./temp");

						BufferedReader in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));
						String line;

						if ((line = in.readLine()) != null) {
							current_temp = Float.parseFloat(line);

							// ASSIGN CURRENT READING TO PREVIOUS WHEN THE PROGRAM START THE FIRST TIME
							if(i == 0){
								i++;
								// CASE WHERE FIRST READING BLOWS UP
								while(current_temp > 150){
									in = new BufferedReader(new InputStreamReader(process_read.getInputStream()));
									line = in.readLine();
									current_temp = Float.parseFloat(line);
								}
								prev_temp = current_temp;
							}

							// CHECK IF THE READING BLOWS UP
							if (prev_temp - 10.0 <= current_temp && current_temp <= prev_temp + 10.0) {
								// IF CURRENT TEMPERATURE LESS THAN PREVIOUS
								// TEMPERATURE + 10, READING SATISFIED
								prev_temp = current_temp;
							} else {
								// IF CURRENT TEMPERATURE GREATER THAN PREVIOUS
								// TEMPERATRUE + 10, READING DISCARD
								current_temp = prev_temp;
							}
							/*
							 * ` * tempratuer tap Warning do not remove if
							 * removed reading will not update
							 */
							temp = current_temp; //
						}

						/*
						 * HEAT ELEMENT
						 */
						if ((current_temp > (float) ideal_temp - 0.5)) { // && (current_temp < (float) ideal_temp + 2)) {
							//TURN OFF HEATER
							Runtime.getRuntime().exec("sudo ./setGPIO 19 1");
						} else {
							Runtime.getRuntime().exec("sudo ./setGPIO 19 0");						
						}
						Thread.sleep(100);
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
							// no reason to do this current_ph = (float) (Math.round(current_ph * 10.0) / 10.0);
							// ROUNDING THE READING TO BE 2 DECIMAL PLACE, SO
							// READINGS ARE CONSISTENT
							/*
							 * ` * tempratuer tap Warning do not remove if
							 * removed reading will not update
							 */
							ph = current_ph; //
						}

						/*
						 * DISPENSER
						 */
						//
						//Calculate wait time for the dispenser. Casting from type float.
						ph_wait_time =(long)(ml_time * ml_gal * Integer.parseInt(tank));
						
						/*
						 * DISPENSER_A == pH up 
						 * DISPENSER_B == pH down
						 */
						
						// 
						if ((current_ph <= (float) (ideal_ph - 0.249))) {
							if (hold == false) {
								if ((current_ph <= (float)(ideal_ph - 0.50))) {
									// MAKE SURE DIPENSER_B IS OFF
									// there is no reason it should be on
									//Runtime.getRuntime().exec("sudo ./setGPIO 13 0");
									// TURN ON DISPENSER_A
									Thread.sleep(100); 
									Runtime.getRuntime().exec("sudo ./setGPIO 6 1");
									// WAIT FOR ph_wait_time
									Thread.sleep((long) ph_wait_time);
									// TURN OFF DISPENSER_A
									Runtime.getRuntime().exec("sudo ./setGPIO 6 0");
									// HOLD SET TRUE
									System.out.println("Up full");
									hold_time = LocalDateTime.now().getMinute();
									hold_hour = LocalDateTime.now().getHour();
									hold = true;
								} else {
									// MAKE SURE DIPENSER_B IS OFF
									// there is no reason it should be on
									//Runtime.getRuntime().exec("sudo ./setGPIO 13 0");
									// TURN ON DISPENSER_A
									Thread.sleep(100);
									Runtime.getRuntime().exec("sudo ./setGPIO 6 1");
									// WAIT FOR ph_wait_time
									// Getting closer, half dosage
									Thread.sleep((long) (ph_wait_time*0.5));
									// TURN OFF DISPENSER_A
									Runtime.getRuntime().exec("sudo ./setGPIO 6 0");
									// HOLD SET TRUE
									
									System.out.println("Up half");
									hold_time = LocalDateTime.now().getMinute();
									hold_hour = LocalDateTime.now().getHour();
									hold = true;
								}
							}
						} else if ((current_ph >= (float) (ideal_ph + 0.199))) {
							if (hold == false) {
								if ((current_ph >= (float) (ideal_ph + 0.4))) { 
								    // MAKE SURE DISPENSER_A IS OFF
									// there is no reason it should be on
									//Runtime.getRuntime().exec("sudo ./setGPIO 6 0");
									// TURN ON DISPENSER_B
									Thread.sleep(100);
									Runtime.getRuntime().exec("sudo ./setGPIO 13 1");
									// WAIT 
									Thread.sleep((long)ph_wait_time);
									// TURN OFF DISPENSER_B
									Runtime.getRuntime().exec("sudo ./setGPIO 13 0");
									// HOLD SET TRUE
									System.out.println("down full");
									hold_time = LocalDateTime.now().getMinute();
									hold_hour = LocalDateTime.now().getHour();
									hold = true;
								}else { 
									// MAKE SURE DISPENSER_A IS OFF
									// there is no reason it should be on
									//Runtime.getRuntime().exec("sudo ./setGPIO 6 0");
									// TURN ON DISPENSER_B
									// Almost there dispense half the dosage
									Thread.sleep(100);
									Runtime.getRuntime().exec("sudo ./setGPIO 13 1");
									// WAIT 
									// Getting close, 1/2 dosage
									Thread.sleep((long)(ph_wait_time*0.5)); 
									// TURN OFF DISPENSER_B
									Runtime.getRuntime().exec("sudo ./setGPIO 13 0");
									// HOLD SET TRUE
									System.out.println("down half");
									hold_time = LocalDateTime.now().getMinute();
									hold_hour = LocalDateTime.now().getHour();
									hold = true;
								}
							}
						} else {
							// TURN BOTH MOTORS OFF
							Thread.sleep(100);
							Runtime.getRuntime().exec("sudo ./setGPIO 6 0");
							Thread.sleep(1500);
							Runtime.getRuntime().exec("sudo ./setGPIO 13 0");
						}
						//MAKE SURE BOTH DISPENSERS TURN OFF
						Thread.sleep(100);
						Runtime.getRuntime().exec("sudo ./setGPIO 6 0");
						Thread.sleep(1500);
						Runtime.getRuntime().exec("sudo ./setGPIO 13 0");

						if (hold == true) {
							// RESET HOLD IF CURRENT TIME => HOLD + 2;
							// AT LEAST 2 MINUTE
							switch (hold_time) {
							// Two minutes is enough
							//case 57:
								//if (LocalDateTime.now().getMinute() >= 0 && LocalDateTime.now().getHour() != hold_hour) {
									//hold = false;
								//}
								//break;
							//case 58:
								//if (LocalDateTime.now().getMinute() >= 0 && LocalDateTime.now().getHour() != hold_hour) {
									//hold = false;
								//}
								//break;
							case 59:
								if (LocalDateTime.now().getMinute() >=0 && LocalDateTime.now().getHour() != hold_hour) {
									hold = false;
								}
								break;
							default:
								if (LocalDateTime.now().getMinute() >= hold_time + 1) {
									hold = false;
								}
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					/*
					 * DEMO
					 * 
					 */
					switch(current){
					//case 58:
						//if(LocalDateTime.now().getMinute() >= 0 && LocalDateTime.now().getHour() != c_hour){
							//jToggleButton1.doClick();
							//current = LocalDateTime.now().getMinute();
							//c_hour = LocalDateTime.now().getHour();
						//}
						//break;
					case 59:
						if(LocalDateTime.now().getMinute() >= 0 && LocalDateTime.now().getHour() != c_hour){
							jToggleButton1.doClick();
							current = LocalDateTime.now().getMinute();
							c_hour = LocalDateTime.now().getHour();
						}
						break;
					default:
						if(LocalDateTime.now().getMinute() >= current + 1){
							jToggleButton1.doClick();
							current = LocalDateTime.now().getMinute();
							c_hour = LocalDateTime.now().getHour();
						}
						break;
					}
				}
			}
		});
		// END//

		gui.start();
	}
}
