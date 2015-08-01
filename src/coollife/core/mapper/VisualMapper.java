package coollife.core.mapper;

import java.util.List;
import java.util.Set;

import coollife.core.bio.Animate;
import firststep.Canvas;
import firststep.DoubleXY;

public interface VisualMapper {
	
	public void drawAtlas(Canvas cnv, int winWidth, int winHeight);
	public void drawBiosphere(Canvas cnv, int winWidth, int winHeight, Set<Animate> pool);
	
	public List<double[]> preparePosition(DoubleXY pos);
	public List<DoubleXY> preparePoint(double[] p);
	
}
