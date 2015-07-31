package coollife;

import coollife.classicalConwayModel.DigitalFlatSquareThor;
import coollife.classicalConwayModel.DigitalFlatSquareThorMapper;
import coollife.core.mapper.VisualMapper;
import firststep.Canvas;
import firststep.Color;
import firststep.DoubleXY;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.Framebuffer;


public class LifeMainWindow extends Window {
	
	private static final String APPNAME = "Cool Life";
	
//	private int mouseI, mouseJ;
	private boolean isPaused;
	private boolean isDown;
//	private boolean clickedColor;
	int winWidth = 0;
	int winHeight = 0;
	
	float k = 0, x0 = 0, y0 = 0;

    private VisualMapper mapper;

	public LifeMainWindow( VisualMapper mapper ) {
		super (APPNAME, 600, 400, new Color(0.2f, 0.2f, 0.2f, 1.0f));
		this.mapper = mapper;
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
//	        if (!field.getCell(mouseI, mouseJ))
//	        {
//	        	field.setCell(mouseI, mouseJ, true);
//	        	clickedColor = true;
//	        }
//	        else
//	        {
//	        	field.setCell(mouseI, mouseJ, false);
//	        	clickedColor = false;
//	        }
	    }
	    
	    if (button == MouseButton.LEFT && state == MouseButtonState.RELEASE) {
	    	isDown = false;
	    }
	}
	
	@Override
	public void cursorPos(double x, double y) {
		if (isDown) {
//        	field.setCell(mouseI, mouseJ, clickedColor);
		}
	}
	
	
//	private float calcScale(int winWidth, int winHeight)
//	{
//	    float wpw = (float)field.getWidth() / winWidth;
//	    float hph = (float)field.getHeight() / winHeight;
//	    float k = 1;
//	    if (wpw > hph)
//	    {
//	        k = 1.0f / wpw;
//	    }
//	    else
//	    {
//	        k = 1.0f / hph;
//	    }
//	    return k;
//	}

//	private void cellUnderMouse(int winWidth, int winHeight)
//	{
//	    DoubleXY mPos = getCursorPos();
//
//	    float k = calcScale(winWidth, winHeight);
//	    float x0 = winWidth / 2 - (field.getWidth() * k / 2);
//	    float y0 = winHeight / 2 - (field.getHeight() * k / 2);
//	    
//	    mouseI = (int) ((mPos.getX() - x0) / k);
//	    mouseJ = (int) ((mPos.getY() - y0) / k);
//	}
	
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
	
//	private long frameIndex = 0;

	Framebuffer fontFB;
	Paint fbPaint;
	
	@Override
	protected void frame(Canvas cnv) {
		
		boolean changed = false;
		
// 		changed = k != (k = calcScale(winWidth, getHeight()));
// 		changed = x0 != (x0 = getWidth() / 2 - (field.getWidth() * k / 2) ) ? true : changed;
// 		changed = y0 != (y0 = getHeight() / 2 - (field.getHeight() * k / 2) ) ? true : changed;
		
		changed = winWidth != getWidth() || winHeight != getHeight() ? true : false;
		
		int winWidth = getWidth();
		int winHeight = getHeight();
 		
 		if ( changed ) {
 			fontFB = cnv.createFramebuffer(winWidth, winHeight, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
 			fontFB.beginDrawing(1.0f);
 			mapper.drawAtlas(cnv, winWidth, winHeight);
 			fontFB.endDrawing();
 			fbPaint = cnv.imagePattern(0, 0, winWidth, winHeight, 0.f, fontFB.getImage(), 1.0f);
 		}
 		
		Framebuffer mainFb = cnv.getMainFramebuffer(); 
		mainFb.beginDrawing(1.0f);
		
		cnv.beginPath();
		cnv.rect(0, 0, winWidth, winHeight);
		cnv.fillPaint(fbPaint);
		cnv.fill();
		
		mapper.drawBiosphere(cnv, winWidth, winHeight);
		mapper.getBiosphere().turn();
	    
//	    if (mouseI >= 0 && mouseI < field.getWidth() && mouseJ >= 0 && mouseJ < field.getHeight())
//	    {
//	        cnv.fillColor(new Color(255, 255, 255, 64));
//	        cnv.beginPath();
//	        cnv.rect(x0 + mouseI * k, y0 + mouseJ * k, k, k);
//	        cnv.fill();
//	    }
//
//		if (!isPaused && frameIndex % 2 == 0) {
//			field = LifeEngine.turn(field);
//		}
//		frameIndex ++;
//		cellUnderMouse(getWidth(), getHeight());
		
		mainFb.endDrawing();
	}
	
}