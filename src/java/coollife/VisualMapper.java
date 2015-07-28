package coollife;

import coollife.core.geometry.Topology;
import firststep.Canvas;
import firststep.Color;
import firststep.DoubleXY;

public abstract class VisualMapper {
		
	protected Topology tp; 
	
	protected abstract void mapIt( double[] p, DoubleXY pos );
	public abstract void drawAtlas(Canvas cnv, int winWidth, int winHeight);
	
	public VisualMapper( Topology tp ) {
		this.tp = tp;
	}
	
	protected void drawGeodesic( Canvas cnv, Color c, double[] p, double[] v, double length, double step ) {
		
		if ( cnv == null || c == null || p == null || v == null) {
			throw new NullPointerException();
		}
		
		if ( length < 0 || step < 0 ) {
			throw new IllegalArgumentException();
		}
		
		DoubleXY p0 = new DoubleXY(0, 0);
		mapIt(p, p0);
		cnv.beginPath();
		cnv.strokeColor(c);
		cnv.moveTo( (float)p0.getX(), (float)p0.getY() );
		for( double current = 0; current < length; current += step ) {
			tp.translate(p, v, step);
			mapIt(p, p0);
			cnv.lineTo((float)p0.getX(), (float)p0.getY());
		}
		
		cnv.stroke();
	}

}
