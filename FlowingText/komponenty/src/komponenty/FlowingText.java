package komponenty;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;

public class FlowingText extends JPanel {
    
    private FlowingTextResolution resolution;
    private showNet isNetShown;
    private pointShape pointSh;
    private int panelRows;
    private int panelColumns;
    private int cellSize;
    private int horizontalTab;
    private int verticalTab;
    private String text;
    private int firstViewedColumn;
    private int speed;
    private Color Color_background;
    private Color Color_point;
    private Color Color_net;
    private boolean changehappened;
    private int[][] pointsMatrix;
    private PropertyChangeSupport propertyChangeSupport;  
    
    
    public FlowingText(){
        propertyChangeSupport = new PropertyChangeSupport(this);
        resolution = FlowingTextResolution.LOW;
        isNetShown = showNet.Yes;
        pointSh = pointShape.Circle;
        panelRows = 8;
        panelColumns = 14;
        cellSize = 10;
        horizontalTab = 0;
        verticalTab = 0;
        firstViewedColumn = panelColumns;
        speed = 505;
        text = " ";
        changehappened = false;
        Color_background = Color.LIGHT_GRAY;
        Color_point = Color.RED;
        Color_net = Color.BLUE;
        Thread thread = new Thread(() -> {
            do
            {
                IncfirstViewedColumn();
                try {
                    Thread.sleep(speed);
                }
                    catch (InterruptedException e){
                }
            } 
            while (true);
        });
        thread.start(); 
        setPreferredSize(new Dimension(cellSize*panelColumns, cellSize*panelRows)); 
    }

    public showNet getshowNet(){return isNetShown;}
    public void setshowNet(showNet show){isNetShown = show; repaint();}

    public int getfirstViewedColumn(){return firstViewedColumn;}
    public void setfirstViewedColumn(int i){
        firstViewedColumn = i;
        repaint();
    }

    public int getspeed(){return speed;}
    public void setspeed(int s){speed = s;}
 
    public pointShape getpointShape(){return pointSh;}
    public void setpointShape(pointShape sh){pointSh = sh;}
 
    public Color getColor_background(){return Color_background;}
    public void setColor_background(Color col){Color_background = col;}
 
    public Color getColor_point(){return Color_point;}
    public void setColor_point(Color col){Color_point = col;}
 
    public Color getColor_net(){return Color_net;}
    public void setColor_net(Color col){Color_net = col;}
 
    private void calculateSize(){
        try {
            cellSize = (this.getHeight()-1) / panelRows;
            int pc = (this.getWidth()-1) / cellSize;
            if (panelColumns!=pc)
                changehappened = true;
            panelColumns = pc;
            horizontalTab = (this.getWidth()-1-panelColumns*cellSize)/2;
            verticalTab = (this.getHeight()-1-panelRows*cellSize)/2;
        }
        catch (java.lang.ArithmeticException e){
        }
    }

    public FlowingTextResolution getFlowingTextResolution(){ return resolution; }

    public void setFlowingTextResolution(FlowingTextResolution res){
        if (resolution != res) {
            changehappened = true;
            FlowingTextResolution oldFlowingTextResolution= resolution;
            resolution = res;
            switch (res) {
                case LOW:
                    panelRows = 8;
                    break;
                case MEDIUM:
                    panelRows = 12;
                    break;
                case HIGH:
                    panelRows = 16;
                    break;
            }
            repaint();
            propertyChangeSupport.firePropertyChange("Resolution", oldFlowingTextResolution, resolution);
        }
    }
    
    private void PaintNet(Graphics g){
        if (isNetShown==showNet.Yes){
            g.setColor(Color_net);
                for (int i=0; i<=panelRows;i++)
                g.drawLine(horizontalTab,verticalTab+i*cellSize, horizontalTab+panelColumns*cellSize, verticalTab+i*cellSize);
            for (int i=0; i<=panelColumns;i++)
                g.drawLine(horizontalTab+i*cellSize,verticalTab, horizontalTab+i*cellSize, verticalTab+panelRows*cellSize);
        }
    }
    
    private void paintPoint(Graphics g, int row, int col){
        if ((row>=0)&&(row<panelRows)&&(col>=0)&&(col<panelColumns)){
            g.setColor(Color_point);
            switch (pointSh) {
                case Circle:
                    g.fillOval(horizontalTab + col * cellSize + 1, verticalTab + row * cellSize + 1,cellSize-2, cellSize-2);
                    break;
                case Square:
                    g.fillRect(horizontalTab+col*cellSize + 2, verticalTab+row*cellSize + 2, cellSize - 3, cellSize - 3);
                    break;
                case Triangle:
                    int[] xPoints = {horizontalTab+col*cellSize + 1,horizontalTab+(int)((0.5+col)*cellSize),horizontalTab+(col+1)*cellSize - 1};
                    int[] yPoints = {verticalTab+(row+1)*cellSize - 1,verticalTab+row*cellSize + 1,verticalTab+(row+1)*cellSize - 1};
                    g.fillPolygon(xPoints, yPoints, 3);
                    break;
            }
        }
    } 
    
    private void fillPointsMatrix(){
        if (changehappened) {
            pointsMatrix = new int[panelRows][3*panelColumns];
            for (int i=0; i<panelRows;i++)
                for (int j=0; j<3*panelColumns;j++){
                    if ((j>=panelColumns) && (j<2*panelColumns))
                        pointsMatrix[i][j] = (i+j) % 4;
                    else
                        pointsMatrix[i][j] = 0;
                }
            changehappened = false;
            firstViewedColumn = panelColumns;
        }
    }
    
    public void IncfirstViewedColumn(){
        if (firstViewedColumn<2*panelColumns) firstViewedColumn++;
        else firstViewedColumn = 0;
        repaint();
    }

    public synchronized void paint(Graphics g) {
        calculateSize();
        fillPointsMatrix();
        g.setColor(Color_background);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        PaintNet(g);
        for (int i = 0; i<panelRows; i++)
            for (int j = 0; j<panelColumns; j++)
                try {
                    if (pointsMatrix[i][firstViewedColumn+j] == 1)
                        paintPoint(g,i,j);
//                    System.out.println("i="+i+", j="+j+", mac="+pointsMatrix[i][j]);
                }
                catch(java.lang.Exception e){
                }
    }
    
}
