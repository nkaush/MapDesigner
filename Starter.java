import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
public class Starter{
    public static void main(String[] args){
        /* 
         * ***See bottom of this class for initial planning/brainstorm***
         * 
         * ***Handles the case where if you click on an existing station, the line goes to that station***
         *      -it does not create a new station
         */
        JFrame f = new JFrame("Subway Map");
        int frameWidth = 800;
        int frameHeight = 800;
        f.setSize(frameWidth, frameHeight);
        f.getContentPane().setBackground(Color.WHITE);
        //instantiate and draw the buttons used to select which train line to add to
        LineManager three = new LineManager(10, 150, 100, 60, Color.BLUE, "Line 3");
        f.add(three);
        f.setVisible(true);
        LineManager two = new LineManager(10, 80, 100, 60, Color.GREEN, "Line 2");
        f.add(two);
        f.setVisible(true);
        LineManager one = new LineManager(10, 10, 100, 60, Color.RED, "Line 1");
        f.add(one);
        f.setVisible(true);
        LineManager viewing = new LineManager(10, 220, 100, 30, Color.GRAY, "View Map");      
        f.add(viewing);
        f.setVisible(true);
        LineManager saving = new LineManager(10, 260, 100, 30, Color.GRAY, "Export");      
        f.add(saving);
        f.setVisible(true);
        //store all the line managers in an array for easier access in "for each" loops
        LineManager[] current = new LineManager[]{one, one, two, three}; 
        //store the location of the last point added so it can be moved if user wants
        //also store the larger circle to indicate the last added point
        StationComp[] editable = new StationComp[2];
        editable[0] = new StationComp(0, 0, "", true);
        f.add(editable[0]);
        f.setVisible(true);
        //hide the larger circle at the moment since there has not been a point added yet
        editable[0].setVisible(false);
        //set the 1st line button to be highlighted & selected at the start by default
        one.select(true);
        //color the "view map" button
        viewing.select(true);
        viewing.view(true);   
        viewing.arrow(true);
        class myMouseListener implements MouseListener{
            public void mouseClicked(MouseEvent e){}
            public void mousePressed(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
            public void mouseReleased(MouseEvent e){
                double x = e.getX();
                double y = e.getY()-20;
                //create a rectangle to act as a buffer so the user can't add stations under the buttons
                Rectangle2D bufferRect = new Rectangle2D.Double(0, 0, 120, 260); 
                if(viewing.getRect().contains(x, y)){
                    //if the user clicks the view button
                    //color all the buttons of the lines
                    //and hide the edit buttons
                    for(LineManager lM : current){
                        lM.select(true);
                        lM.view(true);                    
                    }
                    //and hide the "view map" button
                    viewing.hide(true);
                    editable[0].setVisible(false);
                    viewing.arrow(false);
                }
                else if(!viewing.getViewState()){ //as long as the user is NOT in "view mode"
                    if(LineManager.buttonContains(current, x, y)){//check if the user clicked a button to switch lines                       
                        //return the line corresponding to the button clicked
                        LineManager clicked = LineManager.whichButtonClicked(current, x, y);                       
                        if(clicked.getEditButtonRect().contains(x, y)){ 
                            //if the user clicked the "edit" button
                            //prompt the user to change the name and color of the line
                            clicked.editLine();
                        }
                        else{
                            //otherwise, highlight/select the button clicked on
                            current[0].select(false); //change the currently selected line to unselected 
                            clicked.select(true); 
                            current[0] = clicked;
                            int size = clicked.getStations().size();
                            if(size>0){
                                //move the indicator for last station to that last added station for the line selected
                                StationComp last = clicked.getStations().get(size-1);
                                editable[0].goTo(last);
                                editable[0].setVisible(true);
                                editable[1] = last;
                            }
                            else{
                                //if there are no stations for a line, remove the last added station indicator
                                editable[0].setVisible(false);
                                editable[1] = editable[0];
                            }
                        }
                    }                   
                    else if(!bufferRect.contains(x, y)){ 
                        //if the user did not click near where the buttons to switch lines are located in the JFrame
                        //select the point that is on a different line if said point exists
                        StationComp sc = LineManager.testPoint(current, x, y);                    
                        if(sc.getPoint().getX()==x && sc.getPoint().getY()==y){
                            //if no existing point is similar, create a new station  
                            sc = new StationComp(sc.getPoint().getX(), sc.getPoint().getY(), null, false);
                            f.add(sc, 0);
                            f.setVisible(true);                           
                        }
                        editable[0].goTo(sc);
                        editable[0].setVisible(true);
                        editable[1] = sc;
                        //add the point to the line currently selected
                        current[0].add(sc);
                    }
                }                                             
                for(LineManager lM : current){
                    //remove all lines from the screen in case color changed so that the lines can be repainted
                    for(LineComp l : lM.getLines()){
                        f.remove(l);
                    }
                    lM.clearLines();
                    //draw a physical line between each station on the train line
                    for(int i = 0; i < lM.getStations().size()-1; i++){
                        LineComp lC = new LineComp(lM.getStations().get(i), lM.getStations().get(i+1), lM.getColor());
                        lM.add(lC); //add the line to the line Manager
                        f.add(lC); //add the line to the frame
                        f.setVisible(true);
                    }
                }                
            }
        }
        class myKeyListener implements KeyListener{
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE && viewing.getViewState()){
                    //if the user wishes to exit view mode
                    for(LineManager lM : current){
                        //deselect all buttons
                        lM.select(false);
                        lM.view(false);                    
                    }
                    viewing.hide(false); //show the view map button
                    viewing.arrow(true);
                    current[0].select(true); //highlight the currently selected line button
                    boolean test = false;
                    int s = 1;
                    //determine whether to show the last point indicator 
                    while(s<4 && !test){ 
                        int len = current[s].getStations().size();
                        if(len>0){
                            test = true;
                        }
                        s++;
                    }
                    editable[0].setVisible(test);
                }
                //move the selected point depending on which arrow keys are pressed
                //and move the selector highlighting the point as well
                if(e.getKeyCode()==KeyEvent.VK_UP && !viewing.getViewState()){                   
                    editable[1].goTo(editable[1].xCoord(), editable[1].yCoord()-10);
                    editable[0].goTo(editable[1]);                  
                }
                if(e.getKeyCode()==KeyEvent.VK_DOWN && !viewing.getViewState()){                   
                    editable[1].goTo(editable[1].xCoord(), editable[1].yCoord()+10);
                    editable[0].goTo(editable[1]);                
                }
                if(e.getKeyCode()==KeyEvent.VK_LEFT && !viewing.getViewState()){                   
                    editable[1].goTo(editable[1].xCoord()-10, editable[1].yCoord());
                    editable[0].goTo(editable[1]);                  
                }
                if(e.getKeyCode()==KeyEvent.VK_RIGHT && !viewing.getViewState()){                   
                    editable[1].goTo(editable[1].xCoord()+10, editable[1].yCoord());
                    editable[0].goTo(editable[1]);                 
                }
            }
            public void keyReleased(KeyEvent e){}
            public void keyTyped(KeyEvent e){}
        }
        //add mouse and key listeners to the JFrame
        f.addMouseListener(new myMouseListener()); 
        f.addKeyListener(new myKeyListener());
    }
}
/* Subway Map Project Initial Planning
 * 
 * Starter class 
 * -add a mouse listener to select points/select lines to add
 * -add a key listener to move the last added station?
 * -instantiate all stations, buttons, lines
 * 
 * Station Class
 * -paint the circle
 * -add label
 * 
 * Line Class
 * -add lines between each station in the main method 
 * 
 * Line Manager
 * -stores the color of the line
 * -arraylist of stations in the line
 * -arraylist to store all the track lines part of the line
 * -paint the button used to select the current line
 * -store rectangle to check if button is clicked to change lines
 * -add algorithm to check if this line contains a point in a similar location to where clicked
 */