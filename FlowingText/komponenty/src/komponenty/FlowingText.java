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
    private frameStyle fs;
    private int frameWidth;
    private Color Color_background;
    private Color Color_point;
    private Color Color_net;
    private Color Color_frame;
    private boolean changehappened;
    private int[][] pointsMatrix;
    private PropertyChangeSupport propertyChangeSupport;  

    private int frameWidthThin = 2;
    private int frameWidthNormal = 6;
    private int frameWidthBold = 10;
    private int panelRowsLOW = 8;
    private int panelRowsMEDIUM = 12;
    private int panelRowsHIGH = 16;
    
    
    public FlowingText(){
        propertyChangeSupport = new PropertyChangeSupport(this);
        resolution = FlowingTextResolution.LOW;
        isNetShown = showNet.Yes;
        pointSh = pointShape.Circle;
        panelRows = panelRowsLOW;
        panelColumns = 2*panelRowsLOW;
        cellSize = 8;
        horizontalTab = 0;
        verticalTab = 0;
        firstViewedColumn = panelColumns;
        speed = 50;//0 - minimum, 100 - maximum 
        text = "Żółć gęślą jaźń";
        changehappened = true;
        fs = frameStyle.Normal;
        frameWidth = frameWidthNormal;
        Color_background = Color.LIGHT_GRAY;
        Color_point = Color.RED;
        Color_net = Color.BLUE;
        Color_frame = Color.BLACK;
        Thread thread = new Thread(() -> {
            do
            {
                IncfirstViewedColumn();
                try {
                    if (speed<0) speed = 0;
                    if (speed>100) speed = 100;
                    Thread.sleep(-9*speed+1000);
                }
                    catch (InterruptedException e){
                }
            } 
            while (true);
        });
        thread.start(); 
        setPreferredSize(new Dimension(cellSize*panelColumns+2*frameWidth, cellSize*panelRows+2*frameWidth)); 
    }

    public showNet get_showNet(){return isNetShown;}
    public void set_showNet(showNet show){isNetShown = show; repaint();}
    public boolean isNetshown(){
        if (isNetShown==showNet.Yes)
            return true;
        else 
            return false;
    }

    public int getfirstViewedColumn(){return firstViewedColumn;}
    public void setfirstViewedColumn(int i){
        firstViewedColumn = i;
        repaint();
    }

    public int get_speed(){return speed;}
    public void set_speed(int s){speed = s;}
 
    public pointShape get_pointShape(){return pointSh;}
    public void set_pointShape(pointShape sh){pointSh = sh;}
 
    public Color get_Color_background(){return Color_background;}
    public void set_Color_background(Color col){Color_background = col;}
 
    public Color get_Color_point(){return Color_point;}
    public void set_Color_point(Color col){Color_point = col;}
 
    public Color get_Color_net(){return Color_net;}
    public void set_Color_net(Color col){Color_net = col;}
 
    public Color get_Color_frame(){return Color_frame;}
    public void set_Color_frame(Color col){Color_frame = col;}
 
    public FlowingTextResolution get_FlowingTextResolution(){ return resolution; }
    public void set_FlowingTextResolution(FlowingTextResolution res){
        if (resolution != res) {
            changehappened = true;
            FlowingTextResolution oldFlowingTextResolution= resolution;
            resolution = res;
            switch (res) {
                case LOW:
                    panelRows = panelRowsLOW;
                    break;
                case MEDIUM:
                    panelRows = panelRowsMEDIUM;
                    break;
                case HIGH:
                    panelRows = panelRowsHIGH;
                    break;
            }
            repaint();
            propertyChangeSupport.firePropertyChange("Resolution", oldFlowingTextResolution, resolution);
        }
    }

    public frameStyle get_frameStyle(){ return fs; }
    public void set_frameStyle(frameStyle f){
        if (this.fs != f) {
            changehappened = true;
            frameStyle oldframeStyle = this.fs;
            this.fs = f;
            switch (f) {
                case Thin:
                    frameWidth = frameWidthThin;
                    break;
                case Normal:
                    frameWidth = frameWidthNormal;
                    break;
                case Bold:
                    frameWidth = frameWidthBold;
                    break;
            }
            repaint();
            propertyChangeSupport.firePropertyChange("FrameWidth", oldframeStyle, this.fs);
        }
    }
    
    private void calculateSize(){
        try {
            cellSize = (this.getHeight()-2*frameWidth-1) / panelRows;
            int pc = (this.getWidth()-2*frameWidth-1) / cellSize;
            if (panelColumns!=pc)
                changehappened = true;
            panelColumns = pc;
            horizontalTab = (this.getWidth()-2*frameWidth-1-panelColumns*cellSize)/2;
            verticalTab = (this.getHeight()-2*frameWidth-1-panelRows*cellSize)/2;
        }
        catch (java.lang.ArithmeticException e){
        }
    }

    private void PaintNet(Graphics g){
        if (isNetShown==showNet.Yes){
            g.setColor(Color_net);
                for (int i=0; i<=panelRows;i++)
                g.drawLine(frameWidth+horizontalTab,frameWidth+verticalTab+i*cellSize, frameWidth+horizontalTab+panelColumns*cellSize, frameWidth+verticalTab+i*cellSize);
            for (int i=0; i<=panelColumns;i++)
                g.drawLine(frameWidth+horizontalTab+i*cellSize,frameWidth+verticalTab, frameWidth+horizontalTab+i*cellSize, frameWidth+verticalTab+panelRows*cellSize);
        }
    }
    
    private void paintPoint(Graphics g, int row, int col){
        if ((row>=0)&&(row<panelRows)&&(col>=0)&&(col<panelColumns)){
            g.setColor(Color_point);
            switch (pointSh) {
                case Circle:
                    g.fillOval(frameWidth+horizontalTab + col * cellSize + 1, frameWidth+verticalTab + row * cellSize + 1,cellSize-2, cellSize-2);
                    break;
                case Square:
                    g.fillRect(frameWidth+horizontalTab+col*cellSize + 2, frameWidth+verticalTab+row*cellSize + 2, cellSize - 3, cellSize - 3);
                    break;
                case Triangle:
                    int[] xPoints = {frameWidth+horizontalTab+col*cellSize + 1,frameWidth+horizontalTab+(int)((0.5+col)*cellSize),frameWidth+horizontalTab+(col+1)*cellSize - 1};
                    int[] yPoints = {frameWidth+verticalTab+(row+1)*cellSize - 1,frameWidth+verticalTab+row*cellSize + 1,frameWidth+verticalTab+(row+1)*cellSize - 1};
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
        g.setColor(Color_frame);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color_background);
        g.fillRect(frameWidth, frameWidth, getWidth()-2*frameWidth, getHeight()-2*frameWidth);
        PaintNet(g);
        for (int i = 0; i<panelRows; i++)
            for (int j = 0; j<panelColumns; j++)
                try {
                    if (pointsMatrix[i][firstViewedColumn+j] == 1)
                        paintPoint(g,i,j);
                }
                catch(java.lang.Exception e){
                }
    }
    
}
