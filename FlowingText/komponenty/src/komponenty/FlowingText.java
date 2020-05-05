package komponenty;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Component extending JPanel for scrolling the text
 * 
 * @author Lewandowski Adam, Wojnicz Adam, Jarosz Michał
 * @version 1.0
 */

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
    private Color Color_point_on;
    private Color Color_point_off;
    private Color Color_net;
    private Color Color_frame;
    private FontType ft;
    private String fnt;
    private FontStyle fntst;
    private int fontstyle;
    private boolean changehappened;
    private int[][] pointsMatrix;
    private int matrixColumns;
    private PropertyChangeSupport propertyChangeSupport;  
    private int frameWidthThin = 2;
    private int frameWidthNormal = 6;
    private int frameWidthBold = 10;
    private int panelRowsLOW = 25;
    private int panelRowsMEDIUM = 40;
    private int panelRowsHIGH = 55;
    
    /**
     * Constructor
     */
    public FlowingText(){
        propertyChangeSupport = new PropertyChangeSupport(this);
        resolution = FlowingTextResolution.MEDIUM;
        isNetShown = showNet.No;
        pointSh = pointShape.Circle;
        panelRows = panelRowsMEDIUM;
        panelColumns = 2*panelRowsMEDIUM;
        cellSize = 8;
        horizontalTab = 0;
        verticalTab = 0;
        firstViewedColumn = panelColumns;
        speed = 75;//0 - minimum, 100 - maximum 
        ft = FontType.Georgia;
        fnt = "Georgia";
        text = "Test Text";
        fntst = FontStyle.ItalicBold;
        fontstyle = Font.BOLD+Font.ITALIC;
        changehappened = true;
        fs = frameStyle.Normal;
        frameWidth = frameWidthNormal;
        Color_background = UIManager.getColor ( "Panel.background" );
        Color_point_on = Color.RED;
        Color_point_off = new Color(255,204,204);
        Color_net = Color.BLACK;
        Color_frame = Color.RED;
        Thread thread = new Thread(() -> {
            do
            {
                IncfirstViewedColumn();
                try {
                    if (speed<0) speed = 0;
                    if (speed>100) speed = 100;
                    Thread.sleep(-9*speed+910);
                }
                    catch (InterruptedException e){
                }
            } 
            while (true);
        });
        thread.start(); 
        setPreferredSize(new Dimension(cellSize*panelColumns+2*frameWidth, cellSize*panelRows+2*frameWidth)); 
    }

    /**
     * 
     * @return returns if net is shown (Yes / No)
     */
    public showNet get_showNet(){return isNetShown;}
    
    /**
     * 
     * @param show allows to decide to show net (Yes / No)
     */
    public void set_showNet(showNet show){isNetShown = show; repaint();}
    
    /**
     * 
     * @return returns if net is shown (True / False)
     */
    public boolean isNetshown(){
        if (isNetShown==showNet.Yes)
            return true;
        else 
            return false;
    }

    /** 
     * 
     * @return returns position of pointer to column current shown as first.
     */
    private int getfirstViewedColumn(){return firstViewedColumn;}
    private void setfirstViewedColumn(int i){
        firstViewedColumn = i;
        repaint();
    }

    /**
     * 
     * @return  returns current speed (in range from 0 to 100)
     */
    public int get_speed(){return speed;}
    
    /**
     * 
     * @param s sets speed of flow, 0 - delay 1 sec, 100 - 0.1  sec. All values between 0 and 100 are allowed.
     */
    public void set_speed(int s){speed = s;}
 
    /**
     * 
     * @return returns current shape of point (FullPoint, Circle, Square, Triangle, Diamond)
     */
    public pointShape get_pointShape(){return pointSh;}
    
    /**
     * 
     * @param sh sets point shape, possible values: FullPoint, Circle, Square, Triangle, Diamond
     */
    public void set_pointShape(pointShape sh){pointSh = sh;}
 
    /**
     * 
     * @return returns color of background
     */
    public Color get_Color_background(){return Color_background;}

    /**
     * 
     * @param col sets color of background
     */
    public void set_Color_background(Color col){Color_background = col;}
 
    /**
     * 
     * @return returns color of turnerd on point
     */
    public Color get_Color_point_on(){return Color_point_on;}
    
    /**
     * 
     * @param col sets color of turner on point
     */
    public void set_Color_point_on(Color col){Color_point_on = col;}
 
    /**
     * 
     * @return returns color of turned off point
     */
    public Color get_Color_point_off(){return Color_point_off;}
    
    /**
     * 
     * @param col sets color of turned off point
     */
    public void set_Color_point_off(Color col){Color_point_off = col;}
 
    /**
     * 
     * @return returns color of net
     */
    public Color get_Color_net(){return Color_net;}
    
    /**
     * 
     * @param col sets color of net
     */
    public void set_Color_net(Color col){Color_net = col;}
 
    /**
     * 
     * @return returns color of frame
     */
    public Color get_Color_frame(){return Color_frame;}
    
    /**
     * 
     * @param col sets color of frame
     */
    public void set_Color_frame(Color col){Color_frame = col;}
 
    /**
     * 
     * @return returns string actually flowing
     */
    public String get_Text(){return text;}
    
    /**
     * 
     * @param t sets text to show
     */
    public void set_Text(String t){
        if (this.text != t) {
            changehappened = true;
            String oldText = this.text;
            this.text = t;
            repaint();
            propertyChangeSupport.firePropertyChange("Text", oldText, text);
        }
    }
    
    /**
     * 
     * @return returns type of font of flowing text
     */
    public FontType get_FontType() {return ft;}
    
    /**
     * 
     * @param f sets font of flowing text, possible values: Arial, Courier, Georgia, Serif
     */
    public void set_FontType(FontType f) {
        if (this.ft != f) {
            changehappened = true;
            FontType oldFontType = this.ft;
            this.ft = f;
            switch (ft){
                case Arial:
                    fnt = "Arial";
                    break;
                case Courier:
                    fnt = "Courier";
                    break;
                case Georgia:
                    fnt = "Georgia";
                    break;
                case Test:
                    fnt = "Elephant";
                    break;
                case Serif:
                    fnt = "Serif";
                    break;
                default:
                    fnt = "Georgia";
                    break;
            }
            repaint();
            propertyChangeSupport.firePropertyChange("FontType", oldFontType, ft);
        }
    }
 
    /**
     * 
     * @return returns current style of frame
     */
    public frameStyle get_frameStyle(){ return fs; }
    
    /**
     * 
     * @param f sets style of frame, possible values: Thin, Normal, Bold
     */
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
    
    /**
     * 
     * @return returns resolution of component
     */
    public FlowingTextResolution get_FlowingTextResolution(){ return resolution; }
    
    /**
     * 
     * @param res sets resolution of flowing text, possible values: LOW, MEDIUM, HIGH
     */
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

    /**
     * 
     * @return returns style of font (plain, bold, italic)
     */
    public FontStyle get_FontStyle(){ return fntst; }
    
    /**
     * 
     * @param f sets style of flowing text, possible values: Plain, Bold, Italic, ItalicBold
     */
    public void set_FontStyle(FontStyle f){
        if (this.fntst != f) {
            changehappened = true;
            FontStyle oldFontStyle = this.fntst;
            this.fntst = f;
            switch (f) {
                case Plain:
                    fontstyle = Font.PLAIN;
                    break;
                case Bold:
                    fontstyle = Font.BOLD;
                    break;
                case Italic:
                    fontstyle = Font.ITALIC;
                    break;
                case ItalicBold:
                    fontstyle = Font.BOLD+Font.ITALIC;
                    break;
                default:
                    fontstyle = Font.PLAIN;
                    break;
            }
            repaint();
            propertyChangeSupport.firePropertyChange("FontStyle", oldFontStyle, this.fntst);
        }
    }
    
    /**
     * Method used to calculate dimension of raster table after events could change it.
     */
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

    /**
     * Method used to paint (or not) net (depends of settings)
     * @param g object of Graphics
     */
    private void PaintNet(Graphics g){
        if (isNetShown==showNet.Yes){
            g.setColor(Color_net);
                for (int i=0; i<=panelRows;i++)
                g.drawLine(frameWidth+horizontalTab,frameWidth+verticalTab+i*cellSize, frameWidth+horizontalTab+panelColumns*cellSize, frameWidth+verticalTab+i*cellSize);
            for (int i=0; i<=panelColumns;i++)
                g.drawLine(frameWidth+horizontalTab+i*cellSize,frameWidth+verticalTab, frameWidth+horizontalTab+i*cellSize, frameWidth+verticalTab+panelRows*cellSize);
        }
    }
    
    /**
     * 
     * @param g object of Graphics
     * @param row coordinate of row to paint point
     * @param col coordinate of column to paint point
     * @param color color of point
     */
    private void paintPoint(Graphics g, int row, int col, Color color){
        if ((row>=0)&&(row<panelRows)&&(col>=0)&&(col<panelColumns)){
            g.setColor(color);
            switch (pointSh) {
                case Circle:
                    g.fillOval(frameWidth+horizontalTab + col * cellSize + 1, frameWidth+verticalTab + row * cellSize + 1,cellSize, cellSize);
                    break;
                case FullPoint:
                    g.fillRect(frameWidth+horizontalTab+col*cellSize, frameWidth+verticalTab+row*cellSize, cellSize, cellSize);
                    break;
                case Square:
                    g.fillRect(frameWidth+horizontalTab+col*cellSize + 1, frameWidth+verticalTab+row*cellSize + 1, cellSize - 1, cellSize - 1);
                    break;
                case Triangle:
                    int[] xPoints = {frameWidth+horizontalTab+col*cellSize + 1,frameWidth+horizontalTab+(int)((0.5+col)*cellSize),frameWidth+horizontalTab+(col+1)*cellSize - 1};
                    int[] yPoints = {frameWidth+verticalTab+(row+1)*cellSize - 1,frameWidth+verticalTab+row*cellSize + 1,frameWidth+verticalTab+(row+1)*cellSize - 1};
                    g.fillPolygon(xPoints, yPoints, 3);
                    break;
                case Diamond:
                    int[] xPointsD = {frameWidth+horizontalTab+col*cellSize + 1,frameWidth+horizontalTab+(int)((0.5+col)*cellSize),frameWidth+horizontalTab+(col+1)*cellSize - 1, frameWidth+horizontalTab+(int)((0.5+col)*cellSize)};
                    int[] yPointsD = {frameWidth+verticalTab+(int)((row+0.5)*cellSize),frameWidth+verticalTab+row*cellSize + 1, frameWidth+verticalTab+(int)((row+0.5)*cellSize), frameWidth+verticalTab+(row+1)*cellSize - 1};
                    g.fillPolygon(xPointsD, yPointsD, 4);
                    break;
            }
        }
    } 
    
    /**
     * Method fills matrix to scroll with points showing text
     */
    private void fillPointsMatrix(){
        if (changehappened) {
            BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            Font font = new Font(fnt, fontstyle, 1000);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int width = fm.stringWidth(text);
            int height = fm.getHeight();
            g2d.dispose();

            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
            g2d.setFont(font);
            fm = g2d.getFontMetrics();
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, 0, fm.getAscent());
            g2d.dispose();

            int[][] tmp = new int[width][height];
            for (int i=0;i<width;i++)
                for (int j=0;j<height;j++)
                    tmp[i][j] = img.getRGB(i, j);
            int textColumns = (int)Math.round(panelRows*width/height);
            matrixColumns = 2*panelColumns+textColumns;
            pointsMatrix = new int[panelRows][matrixColumns];
            for (int i=0;i<width;i++)
                for (int j=0;j<height;j++){
                    int xnew = panelColumns + i*(textColumns-1)/(width-1);
                    int ynew = j*(panelRows-1)/(height-1);
                    int p = tmp[i][j];
                    int a = (p>>24) & 0xff;
                    int r = (p>>16) & 0xff;
                    int g = (p>>8) & 0xff;
                    int b = p & 0xff;
                    if ((a==0)&&(r==0)&&(g==0)&&(b==0))
                        pointsMatrix[ynew][xnew]++;
                    else
                        pointsMatrix[ynew][xnew]--;
                }
            for (int i=0;i<panelRows;i++)
                for (int j=panelColumns;j<panelColumns+textColumns;j++)
                    if (pointsMatrix[i][j]>0)
                        pointsMatrix[i][j] = 0;
                    else
                        pointsMatrix[i][j] = 1;
            changehappened = false;
        }
    }
    
    /**
     * Method increases first column of matrix is currently shown. When reaches end of matrix, starts from begin.
     */
    public void IncfirstViewedColumn(){
        if (firstViewedColumn<matrixColumns-panelColumns) firstViewedColumn++;
        else firstViewedColumn = 0;
        repaint();
    }

    /**
     * Method paints component
     * @param g object of Graphics
     */
    public synchronized void paint(Graphics g) {

        calculateSize();
        fillPointsMatrix();
        g.setColor(Color_frame);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color_background);
        g.fillRect(frameWidth, frameWidth, getWidth()-2*frameWidth, getHeight()-2*frameWidth);
        for (int i = 0; i<panelRows; i++)
            for (int j = 0; j<panelColumns; j++)
                paintPoint(g,i,j,
                        (pointsMatrix[i][firstViewedColumn+j] == 1) ? Color_point_on:Color_point_off);
        PaintNet(g);
    }
    
}
