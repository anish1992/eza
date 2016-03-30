//display histogram
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.*;

import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;


public class CreateHistogram extends JFrame implements ActionListener
{
	int[] counts = new int[10];
	//private JTextField jtf = new JTextField(20);
	private Histogram display = new Histogram();
	private JButton jbtShowHistogram = new JButton("Show Histogram");

	public CreateHistogram() {
		JPanel p = new JPanel();
		//p.setLayout(new BorderLayout());
		//p.add(new JLabel("Text File"), "West");
		//p.add(this.jbtShowHistogram, "East");


		//p.add(this.jtf, "Center");

		this.display.setBorder(new LineBorder(Color.black, 1));

		setLayout(new BorderLayout());
		add(p, "South");
		add(this.display, "Center");

		try
		{
			Scanner input = new Scanner(new File(this.jtf.getText().trim()));

			if (input.hasNext()) {
				String line = input.nextLine();

				StringTokenizer tokens = new StringTokenizer(line, " ");
		    	String[] splited = new String[tokens.countTokens()];
		    	int index = 0;
			    while(tokens.hasMoreTokens()){
			        this.counts[index] = Integer.parseInt(tokens.nextToken());
			        ++index;
			    }
			}

			this.display.showHistogram(this.counts);
		}	catch (FileNotFoundException ex) {
			System.out.println("File not found: " + this.jtf.getText().trim());
		}

		//this.jbtShowHistogram.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e)
	{

		
		
	}

	public static void main(String[] args) {
		CreateHistogram frame = new CreateHistogram();
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(3);
		frame.setTitle("Display Histogram");
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
