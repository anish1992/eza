import java.awt.AlphaComposite;
import javax.swing.*;
import java.awt.*;

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

         int x = 25;
        
         // Draw a horizontal base line
         g.setColor(Color.black);
         g.drawLine(15, height - 30, width - 10, height - 30);
         for (int i = 0; i < count.length; i++) {
             
             // Find the bar height
             int barHeight = (int)(((double)count[i] / (double)maxCount) * (height - 50));

             //color setting
             if (color == 0)    {
                g.setColor(new Color(0,204,0));
             } else if(color == 1){
                g.setColor(new Color(0,0,255));
             }
             // Display a bar (i.e., rectangle)
             g.fillRect(x, height - 30 - barHeight, individualWidth+25,barHeight);
             g.setColor(Color.black);
             
             // Move x for displaying the next character
             x += interval;
         }

         g.setColor(Color.LIGHT_GRAY);
         g.setFont(new Font("SansSerif", Font.PLAIN, 20)); 
         strWidth = g.getFontMetrics().stringWidth(label);
         g.drawString(label, (374-strWidth)/2 , height-8);
     }

     @Override
     public Dimension getPreferredSize() {
        return new Dimension(374, 148);
     }
 }