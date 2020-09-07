import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
public class StationComp extends JComponent{
    int diameter = 10;
    double x, y;
    String name;
    Point2D point;
    Ellipse2D circle;
    boolean last, visible;    
    public StationComp(double xPos, double yPos, String n, boolean l){
        x = xPos;
        y = yPos;
        last = l;
        point = new Point2D.Double(x, y);
        visible = true;
        if(last){
            diameter+=8;
        }      
        if(n!=null){//used if a temporary point is created - the point will not be displayed
            //it is only for determining whther the user clicked an existing station
            name = n;
        }
        else{
            name = JOptionPane.showInputDialog("Please name this station.");
            while(!(name!=null)&&!name.equals("")){
                name = JOptionPane.showInputDialog("Please name this station.");
            }
        }        
    }
    public void paintComponent(Graphics g){
        if(visible){ //draw the point if it is set as visible
            Graphics2D g2 = (Graphics2D) g;
            circle = new Ellipse2D.Double(x-(diameter/2), y-(diameter/2), diameter, diameter);
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLACK);
            g2.draw(circle);
            g2.setColor(Color.WHITE);
            g2.fill(circle);
            g2.setColor(Color.BLACK);
            g2.drawString(name, (float)x + 10, (float)y + 10);
        }
    }
    public Point2D getPoint(){
        return point;
    }
    public void setVisible(boolean v){
        visible = v;
        repaint();
    }
    public void goTo(double newx, double newy){
        //moves the station to a new location if arrow keys are pressed
        x = newx;
        y = newy;
        point.setLocation(x, y);
        repaint();
    }
    public void goTo(StationComp s){
        //moves the station to a new location if arrow keys are pressed
        //this method is overloaded just to shorten the code a little bit in the starter method
        //it is easier to say go to a station than go to the x and y points of a station
        x = s.xCoord();
        y = s.yCoord();
        point.setLocation(s.getPoint());
        repaint();
    }
    public double xCoord(){
        return x;
    }
    public double yCoord(){
        return y;
    }
}