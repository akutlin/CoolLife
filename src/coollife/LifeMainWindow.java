package coollife;

import coollife.classicalConwayModel.DigitalFlatSquareThor;
import coollife.classicalConwayModel.DigitalFlatSquareThorMapper;
import firststep.Canvas;
import firststep.Color;
import firststep.DoubleXY;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.Framebuffer;


public class LifeMainWindow extends Window {
	
	private static final String APPNAME = "Cool Life";
	
	private int mouseI, mouseJ;
	private boolean isPaused;
	private boolean isDown;
	private boolean clickedColor;
	
	float k = 0, x0 = 0, y0 = 0;

	private Cells field;
    private DigitalFlatSquareThor thor;
    private DigitalFlatSquareThorMapper mapper;

	public LifeMainWindow() {
		super (APPNAME, 600, 400, new Color(0.2f, 0.2f, 0.2f, 1.0f));
		field = new Cells(60, 40);
		thor = new DigitalFlatSquareThor(field.getWidth(), field.getHeight());
		mapper = new DigitalFlatSquareThorMapper( thor );
	}
	
	@Override
	public void key(Key key, int scancode, KeyState state, Modifiers modifiers) {
		if (key == Key.ESCAPE && state == KeyState.PRESS) {
			close();
		} else if (key == Key.SPACE && state == KeyState.PRESS) {
			isPaused = !isPaused;
	        updateTitle();
	    }
	}
	
	@Override
	public void mouseButton(MouseButton button, MouseButtonState state, Modifiers modifiers) {
	    if (button == MouseButton.LEFT && state == MouseButtonState.PRESS)
	    {
	    	isDown = true;
	        if (!field.getCell(mouseI, mouseJ))
	        {
	        	field.setCell(mouseI, mouseJ, true);
	        	clickedColor = true;
	        }
	        else
	        {
	        	field.setCell(mouseI, mouseJ, false);
	        	clickedColor = false;
	        }
	    }
	    
	    if (button == MouseButton.LEFT && state == MouseButtonState.RELEASE) {
	    	isDown = false;
	    }
	}
	
	@Override
	public void cursorPos(double x, double y) {
		if (isDown) {
        	field.setCell(mouseI, mouseJ, clickedColor);
		}
	}
	
	
	private float calcScale(int winWidth, int winHeight)
	{
	    float wpw = (float)field.getWidth() / winWidth;
	    float hph = (float)field.getHeight() / winHeight;
	    float k = 1;
	    if (wpw > hph)
	    {
	        k = 1.0f / wpw;
	    }
	    else
	    {
	        k = 1.0f / hph;
	    }
	    return k;
	}

	private void cellUnderMouse(int winWidth, int winHeight)
	{
	    DoubleXY mPos = getCursorPos();

	    float k = calcScale(winWidth, winHeight);
	    float x0 = winWidth / 2 - (field.getWidth() * k / 2);
	    float y0 = winHeight / 2 - (field.getHeight() * k / 2);
	    
	    mouseI = (int) ((mPos.getX() - x0) / k);
	    mouseJ = (int) ((mPos.getY() - y0) / k);
	}
	
	private void updateTitle()
	{
	    if (isPaused)
	    {
	    	setTitle(APPNAME + " [ Pause ]");
	    }
	    else
	    {
	    	setTitle(APPNAME);
	    }
	}
	
	private long frameIndex = 0;

	Framebuffer fontFB;
	Paint fbPaint;
	
	@Override
	protected void frame(Canvas cnv) {
		
		boolean changed = false;
		
 		changed = k != (k = calcScale(getWidth(), getHeight()));
 		changed = x0 != (x0 = getWidth() / 2 - (field.getWidth() * k / 2) ) ? true : changed;
 		changed = y0 != (y0 = getHeight() / 2 - (field.getHeight() * k / 2) ) ? true : changed;
 		
 		if ( changed ) {
 			fontFB = cnv.createFramebuffer(getWidth(), getHeight(), Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
 			fontFB.beginDrawing(1.0f);
 			mapper.drawAtlas(cnv, getWidth(), getHeight());
 			fontFB.endDrawing();
 			fbPaint = cnv.imagePattern(0, 0, getWidth(), getHeight(), 0.f, fontFB.getImage(), 1.0f);
 		}
 		
		Framebuffer mainFb = cnv.getMainFramebuffer(); 
		mainFb.beginDrawing(1.0f);
		
		cnv.beginPath();
		cnv.rect(0, 0, getWidth(), getHeight());
		cnv.fillPaint(fbPaint);
		cnv.fill();

	    // Painting light cells
        cnv.beginPath();
        cnv.fillColor(new Color(192, 192, 192));
	    for (int i = 0; i < field.getWidth(); i++)
	    for (int j = 0; j < field.getHeight(); j++)
	    {
	        if (field.getCell(i, j))
	        {
		        cnv.rect(x0 + i * k, y0 + j * k, k, k);
	        }
	    }
        cnv.fill();
	    
	    if (mouseI >= 0 && mouseI < field.getWidth() && mouseJ >= 0 && mouseJ < field.getHeight())
	    {
	        cnv.fillColor(new Color(255, 255, 255, 64));
	        cnv.beginPath();
	        cnv.rect(x0 + mouseI * k, y0 + mouseJ * k, k, k);
	        cnv.fill();
	    }

		if (!isPaused && frameIndex % 2 == 0) {
			field = LifeEngine.turn(field);
		}
		frameIndex ++;
		cellUnderMouse(getWidth(), getHeight());
		
		mainFb.endDrawing();
	}
	
}