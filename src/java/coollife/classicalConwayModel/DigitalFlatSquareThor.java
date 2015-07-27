package coollife.classicalConwayModel;

import coollife.core.geometry.Topology;

public class DigitalFlatSquareThor extends Topology {
	
	private final int xMax, yMax;
	
	public int[] getSize() {
		return new int[]{ xMax, yMax };
	}
	
	public DigitalFlatSquareThor( int xMax, int yMax) {
		super(2);
		checkSanity( new double[]{xMax, yMax} );
		this.xMax = xMax;
		this.yMax = yMax;
	}
	
	private static void checkSanity( double[] r ) {
		if ( r == null ) {
			throw new NullPointerException();
		} else if ( r.length != dim ) {
			throw new IllegalArgumentException();
		}
		
		for ( int i = 0; i < dim; i++) {
			if ( r[i] < 0) {
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * This method is not necessary for this class
	 */
	@Override
	public double[][][] getChristoffelSymbolOfTheFirstKind(double[] point) {
		return new double[dim][dim][dim];
	}

	/**
	 * This method is not necessary for this class
	 */
	@Override
	public double[][] getMetrics(double[] point) {
		return new double[][] {
				{1,0}, 
				{0,1}
		};
	}
	
	@Override
	protected double getSeed(double[] p) {
		return 1;
	}
	
	/**
	 * @param r coordinates in Descartes system
	 */
	@Override
	public void transform(double[] r) {
		defaultCheckSanity( r , dim );
		while (r[0] < 0) r[0] += xMax;
		r[0] = r[0] == xMax ? r[0] : Math.round( r[0] % xMax );
		while (r[1] < 0) r[1] += yMax;
		r[1] = r[1] == yMax ? r[1] : Math.round( r[1] % yMax );
	}
	
	@Override
	public double getDistance( double[] p1, double p2[]) {
		p1[0] = p1[0] == xMax ? 0 : p1[0];
		p1[1] = p1[1] == yMax ? 0 : p1[1];
		p2[0] = p2[0] == xMax ? 0 : p2[0];
		p2[1] = p2[1] == yMax ? 0 : p2[1];

		transform(p1);
		transform(p2);
		double[] delta = new double[]{ Math.abs( p2[0] - p1[0] ), Math.abs( p2[1] - p1[1] )};
		return delta[1] > delta[0] ? delta[1] : delta[0];
	}
	
	@Override
	public double getPathLength( double[] state, double t1, double t2) {
		defaultCheckSanity( state, 4);
		double[] p = new double[] { state[0], state[1] };
		transform(p);
		double v = Math.abs(state[2]) > Math.abs(state[3]) ? state[2] : state[3];
		return Math.round( Math.abs( v * (t2 - t1)) );
	}

}
