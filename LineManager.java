import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JColorChooser;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.util.ArrayList;
public class LineManager extends JComponent{
    static int stationCounter = 3;
    Rectangle2D r;
    Rectangle2D editButton;
    int x, y, width, height, counter; 
    Color mainColor;
    String displayText;
    boolean selected = false;
    boolean viewing = false;
    boolean hide = false;
    boolean arrowReminder = false;
    ArrayList<StationComp> stations = new ArrayList<StationComp>();
    ArrayList<LineComp> lines = new ArrayList<LineComp>();
    public LineManager(int xPos, int yPos, int w, int h, Color c, String t){
        counter = stationCounter;
        stationCounter--;
        x = xPos;
        y = yPos;
        width = w;
        height = h;
        mainColor = c;
        displayText = t;
        r = new Rectangle2D.Double(x, y, width, height);
        editButton = new Rectangle2D.Double(x+(width-30), y+(height-20), 30, 20);
    }
    public void paintComponent(Graphics g){       
        //draws the button to select this line
        //cast the graphics to Graphics2D
        Graphics2D g2 = (Graphics2D) g;
        if(!hide){
            //draw the line selection button depending on whether it is selected or not
            if(selected){
                g2.setColor(mainColor);
                g2.fill(r);
                g2.setColor(Color.BLACK);
            }
            else{
                g2.setColor(Color.WHITE);
                g2.fill(r);
                g2.setColor(mainColor);
            }
            //label the line selection button
            g2.setStroke(new BasicStroke(2));
            g2.drawString(displayText, (x + width/2) - 43, y + (height/2) + 5);
            g2.draw(r);            
            //draw the edit button
            if(!viewing){
                g2.setColor(Color.WHITE);
                g2.fill(editButton);
                g2.setColor(Color.BLACK);
                g2.draw(editButton);
                g2.drawString("Edit", x+(width-30)+3, y+(height-5));  
            }
        }
        else{
            //if the program is in view map mode, then display this text
            g2.setColor(Color.BLACK);
            g2.drawString("Press esc to exit View Mode", 310, 12);
        }
        if(arrowReminder){
            //display the reminder to use arrow keys
            g2.setColor(Color.BLACK);
            g2.drawString("Use the arrow keys to move the selected (last added) point", 220, 12);
        }
    }
    public Rectangle2D getRect(){
        return r;
    }   
    public Rectangle2D getEditButtonRect(){
        return editButton;
    }
    public void select(boolean s){
        //color the button if selected
        selected = s;
        repaint();
    }
    public void view(boolean s){
        //remind to use esc to escape view mode
        viewing = s;
        repaint();
    }
    public void hide(boolean s){
        //hide the button if in view mode
        hide = s;
        repaint();
    }
    public void arrow(boolean s){
        //remind the user to use arrow keys to move stations
        arrowReminder = s;
        repaint();
    }
    public boolean getViewState(){
        //returns whether the button is in viewing mode and cannot responds to clicks
        return hide;
    }
    public void editLine(){
        displayText = JOptionPane.showInputDialog(null, "Rename " + displayText + ":", "Rename Line", JOptionPane.PLAIN_MESSAGE);
        mainColor = JColorChooser.showDialog(null, "Choose a Color", mainColor);
        repaint();
    }
    public void add(StationComp sc){
        stations.add(sc);
    }
    public void add(LineComp l){
        lines.add(l);
    }
    public ArrayList<StationComp> getStations(){
        return stations;
    }
    public ArrayList<LineComp> getLines(){
        return lines;
    }
    public void clearLines(){
        lines.clear();
    }
    public Color getColor(){
        return mainColor;
    }
    public String[][] report(){
        String[][] reps = new String[stations.size()][7];
        String[] lineInfo;
        for(int i = 0; i < lines.size(); i++){
            reps[i][0] = Integer.toString(counter);
            lineInfo = lines.get(i).report();
            for(int j = 0; i < 6; i++){
                reps[i][j+1] = lineInfo[j];
            }
        }
        return reps;
    }
    public static StationComp testPoint(LineManager[] lines, double x, double y){
        //code to test if a line contains a station at the location that the user clicked and return the station 
        Rectangle2D r = new Rectangle2D.Double(0, 0, 0, 0);
        StationComp sc = new StationComp(x, y, "temporary", false);
        int proximitySensitivity = 24;
        for(int s = 0; s < lines.length; s++){
            for(int i = 0; i < lines[s].getStations().size(); i++){
                r.setRect(lines[s].getStations().get(i).getPoint().getX()-(proximitySensitivity/2), lines[s].getStations().get(i).getPoint().getY()-(proximitySensitivity/2), proximitySensitivity, proximitySensitivity);
                if(r.contains(x, y)){
                     sc = lines[s].getStations().get(i);
                }
            }
        }
        return sc;
    }  
    public static boolean buttonContains(LineManager[] lines, double x, double y){
        //code to determine whether a button was clicked using boolean operations
        boolean res = false;
        for(LineManager lM : lines){
            res = res || lM.getRect().contains(x, y);
        }
        return res;
    }
    public static LineManager whichButtonClicked(LineManager[] lines, double x, double y){
        //code to determine which button of some buttons in an array was clicked
        LineManager res = lines[0];
        for(LineManager lM : lines){
            if(lM.getRect().contains(x, y)){
                res = lM;
            }
        }        
        return res;
    }
}