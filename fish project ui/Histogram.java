
import javax.swing.*;
import java.awt.*;
//import java.awt.geom.*;

 public class Histogram extends JPanel{
     // Count the occurrences of 26 letters
     private float[] count;
     private String label = "this is a label test";
     private int strWidth;
     private int color; // 0 for green or 1 for blue
     private int highest = 0;
     private int night = 0;

     /** Set the count and display histogram */
     public void showHistogram(float[] count, String label, int color, int night) {
         this.count = count;
         this.label = label;
         this.color = color;
         this.night = night;
         repaint();
     }

     @Override /** Paint the histogram */
     protected void paintComponent(Graphics g) {
         if (count == null) return; // No display if count is null

         super.paintComponent(g);



         // Find the panel size and bar width and interval dynamically
         int width = getWidth();
         int height = getHeight();
         int interval = (width - 40) / count.length;
         int individualWidth = (int)(((width - 40) / 24) * 0.60);

         // Find the maximum count. The maximum count has the highest bar
         float maxCount = 0;
         for (int i = 0; i < count.length; i++) {
             if (maxCount < count[i])
                maxCount = count[i];
         }

         if (night == 1)    { // night shift
            g.setColor(Color.lightGray);
        }  else if (night == 0) {
            g.setColor(Color.WHITE);
        } 
         
         g.fillRect(0, 0, 345, 94);
         // x is the start position for the first bar in the histogram
         int x = 25;

         //Graphics2D g2d = (Graphics2D) g;
         //AffineTransform defaultAt = g2d.getTransform();
         //AffineTransform at = new AffineTransform();
         //at.setToRotation(-Math.PI / 2.0);
        
         // Draw a horizontal base line
         g.setColor(Color.black);
         g.drawLine(15, height - 15, width - 10, height - 15);
         for (int i = 0; i < count.length; i++) {
             if(highest<count[i])   {
                highest = (int)count[i];
             }
             // Find the bar height
             int barHeight = (int)(((double)count[i] / (double)maxCount) * (height - 35));

             g.drawString("Peek : "+highest, 10 , 10);

             //color setting
             if (color == 0)    {
                g.setColor(new Color(0,204,0));
             } else if(color == 1){
                g.setColor(new Color(0,0,255));
             }
             // Display a bar (i.e., rectangle)
             g.fillRect(x, height - 15 - barHeight, individualWidth+20,barHeight);
             g.setColor(Color.black);
             
             // Display a hight of the line under the base line
             //g.drawString( count[i] + "", x, height - 30);
             
             // Display a label for the graf
             strWidth = g.getFontMetrics().stringWidth(label);
             g.drawString(label, (350-strWidth)/2 , height-2);
             // Move x for displaying the next character
             x += interval;
         }
         //g2d.setTransform(at);
         //g2d.drawString("Peek : "+highest, (int)(0.7*(-height)) , 12);
         //g2d.setTransform(defaultAt);
     }

     @Override
     public Dimension getPreferredSize() {
        return new Dimension(345, 94);
     }
 }