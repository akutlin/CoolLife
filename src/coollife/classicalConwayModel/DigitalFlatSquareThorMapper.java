package coollife.classicalConwayModel;

import coollife.VisualMapper;
import firststep.Canvas;
import firststep.Color;
import firststep.DoubleXY;

public class DigitalFlatSquareThorMapper extends VisualMapper {
	
	private final int[] size;
	private float k, x0, y0;
	
	public DigitalFlatSquareThorMapper( DigitalFlatSquareThor tp ) {
		super(tp);
		size = tp.getSize();
	}
	
	private void fillFrameSize(int winWidth, int winHeight) {
		float wpw = (float)winWidth /size[0];
		float hph = (float)winHeight /size[1];
		if (wpw < hph) {
			k = wpw;
		} else {
			k = hph;
		}
		x0 = winWidth / 2 - (size[0] * k / 2);
	    y0 = winHeight / 2 - (size[1] * k / 2);
	}
	
	@Override
	protected void mapIt(double[] p, DoubleXY pos) {
		tp.transform(p);
		pos.setX(x0 + k * p[0]);
		pos.setY(y0 + k * p[1]);
	}

	@Override
	public void drawAtlas(Canvas cnv, int winWidth, int winHeight) {
		
		fillFrameSize( winWidth, winHeight );
	    
	    // Painting dark font
        cnv.beginPath();
        cnv.fillColor(new Color(0, 0, 0));
        cnv.rect(x0, y0, k * size[0], k * size[1]);
        cnv.fill();
		
        // Painting geodesic lines
        double[] p = new double[2];
        double[] vx = new double[2];
        double[] vy = new double[2];
        Color c = new Color(64, 64, 64, 255);
		for ( int i = 1; i < size[0]; i++ ) {
			p[0] = i; p[1] = 0;
			vy[0] = 0; vy[1] = 1;
			drawGeodesic(cnv, c, p , vy , size[1], 1);
		}
		for ( int j = 1; j < size[1]; j++) {
			p[0] = 0; p[1] = j;
			vx[0] = 1; vx[1] = 0;
			drawGeodesic(cnv, c, p , vx , size[0], 1);
		}
		
	}
}
