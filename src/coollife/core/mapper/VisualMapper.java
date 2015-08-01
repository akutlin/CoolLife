package coollife.core.mapper;

import coollife.core.bio.Biosphere;
import firststep.Canvas;
import firststep.DoubleXY;

public interface VisualMapper {
	
	public Biosphere getBiosphere();
	public void drawAtlas(Canvas cnv, int winWidth, int winHeight);
	public void drawBiosphere(Canvas cnv, int winWidth, int winHeight);
	public void preparePosition(DoubleXY pos, double[] p);
	public void updatePosition( DoubleXY pos );
	
}
