package coollife;

import coollife.core.geometry.Topology;
import firststep.Canvas;
import firststep.Color;
import firststep.DoubleXY;

public abstract class VisualMapper {
		
	protected final Topology tp;
	
	public abstract void drawAtlas(Canvas cnv, int winWidth, int winHeight);
	
	public VisualMapper( Topology tp ) {
		this.tp = tp;
	}
	
	protected abstract class Map {
		
		protected DoubleXY relativePos, relativeSize;
		
		public Map(DoubleXY relativePos, DoubleXY relativeSize) {
			this.relativePos = relativePos;
			this.relativeSize = relativeSize;
		}
		
		protected abstract void it( double[] p, DoubleXY pos ) throws MapMismatchException;
		public abstract void drawMap();
		
		protected void drawGeodesic( Canvas cnv, Color c, double[] p, double[] v, double length, double step) {
			
			if ( cnv == null || c == null || p == null || v == null) {
				throw new NullPointerException();
			}
			
			if ( length < 0 || step < 0 ) {
				throw new IllegalArgumentException();
			}
			
			DoubleXY p0 = new DoubleXY(0, 0);
			it(p, p0);
			cnv.beginPath();
			cnv.strokeColor(c);
			cnv.moveTo( (float)p0.getX(), (float)p0.getY() );
			for( double current = 0; current < length; current += step ) {
				tp.translate(p, v, step);
				it(p, p0);
				cnv.lineTo((float)p0.getX(), (float)p0.getY());
			}
			
			cnv.stroke();
		}
		
		protected class MapMismatchException extends RuntimeException {
			public MapMismatchException() {
				super();
			}
			private static final long serialVersionUID = 8612909242053861560L;
		}
	}

}
