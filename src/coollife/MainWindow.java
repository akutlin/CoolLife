package coollife;

import java.util.ArrayList;
import java.util.List;

import coollife.classicalConwayModel.ClassicalConwayAnimal;
import coollife.core.bio.Biosphere;
import coollife.core.mapper.VisualMapper;
import firststep.Canvas;
import firststep.Color;
import firststep.Image;
import firststep.Paint;
import firststep.Window;
import firststep.Framebuffer;


public class MainWindow extends Window {
	
	private static final String APPNAME = "Cool Life";
	
	private boolean isPaused;
	private boolean isDown;
    private VisualMapper mapper;
    private Biosphere bio;

	public MainWindow( VisualMapper mapper, Biosphere bio ) {
		super (APPNAME, 600, 400, new Color(0.2f, 0.2f, 0.2f, 1.0f));
		this.mapper = mapper;
		this.bio = bio;
	}
	
	@Override
	public void key(Key key, int scancode, KeyState state, Modifiers modifiers) {
		if (key == Key.ESCAPE && state == KeyState.PRESS) {
			close();
		} else if (key == Key.SPACE && state == KeyState.PRESS) {
			isPaused = !isPaused;
	    }
	}
	
	@Override
	public void mouseButton(MouseButton button, MouseButtonState state, Modifiers modifiers) {
	    if (button == MouseButton.LEFT && state == MouseButtonState.PRESS)
	    	isDown = true;
	    if (button == MouseButton.LEFT && state == MouseButtonState.RELEASE) {
	    	isDown = false;
	    	points = new ArrayList<>();
	    }
	}
	
	private void updateTitle() {
	    if (isPaused)
	    	setTitle(APPNAME + " [ Pause ]: " + bio.getTime() );
	    else
	    	setTitle(APPNAME + ": " + bio.getTime() );
	}
	
	int winWidth = 0;
	int winHeight = 0;
	Framebuffer fontFB;
	Paint fbPaint;
	List<double[]> points = new ArrayList<>();
	
	@Override
	protected void frame(Canvas cnv) {
		
		boolean changed = false;
		
		changed = winWidth != getWidth() || winHeight != getHeight() ? true : false;
		
		winWidth = getWidth();
		winHeight = getHeight();
 		
 		if ( changed ) {
 			fontFB = cnv.createFramebuffer(winWidth, winHeight, Image.Flags.of(Image.Flag.REPEATX, Image.Flag.REPEATY));
 			fontFB.beginDrawing(1.0f);
 			mapper.drawAtlas(cnv, winWidth, winHeight);
 			fontFB.endDrawing();
 			fbPaint = cnv.imagePattern(0, 0, winWidth, winHeight, 0.f, fontFB.getImage(), 1.5f);
 		}
 		
		Framebuffer mainFb = cnv.getMainFramebuffer(); 
		mainFb.beginDrawing(1.0f);
		
		cnv.beginPath();
		cnv.rect(0, 0, winWidth, winHeight);
		cnv.fillPaint(fbPaint);
		cnv.fill();
		
		mapper.drawBiosphere(cnv, winWidth, winHeight, bio.getAnimatePool());
		if ( !isPaused )
			bio.turn();
		if ( isDown ) {
			List<double[]> newPoints = mapper.preparePosition( getCursorPos() );
			boolean equals = true;
			for ( double[] p1 : newPoints ) {
				boolean local = false;
				for ( double[] p2 : points ) {
					if ( p1[0] == p2[0] && p1[1] == p2[1] ) {
						local = true; break;
					}
				}
				if (local == false) {
					equals = false; break;
				}
			}
			if (!equals) {
				for ( double[] p : newPoints )
					bio.updateAnimates( new ClassicalConwayAnimal( p[0], p[1] ) );
			}
			points = newPoints;
		}
        updateTitle();
		mainFb.endDrawing();
	}
	
}