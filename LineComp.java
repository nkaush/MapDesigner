import javax.swing.JComponent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
public class LineComp extends JComponent{
    Point2D p1, p2;
    StationComp station1, station2;
    int width = 6;
    Color color;
    public LineComp(StationComp sc1, StationComp sc2, Color c){
        station1 = sc1;
        station2 = sc2;
        p1 = sc1.getPoint();
        p2 = sc2.getPoint();
        color = c;
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Line2D l = new Line2D.Double(p1, p2);       
        g2.setStroke(new BasicStroke(width));
        g2.setColor(color);
        g2.draw(l);        
    }
    public String[] report(){
        return new String[]{station1.name, Double.toString(station1.x), Double.toString(station1.y),
                            station2.name, Double.toString(station2.x), Double.toString(station2.y)};
    }
}