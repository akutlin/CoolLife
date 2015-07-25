package coollife.core.math;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

import coollife.core.geometry.Topology;

public class GeodesicEquation implements FirstOrderDifferentialEquations {

	private final int equationDim; 
	private Topology space;
	
	public GeodesicEquation( Topology space ){
		equationDim = 2 * space.getDimension();
		this.space = space;
	}
	
	@Override
	public final int getDimension() {
		return equationDim;				
	}

	@Override
	public final void computeDerivatives(double t, double[] y, double[] yDot)
			throws MaxCountExceededException, DimensionMismatchException {
		
		checkSanity( y, equationDim );
		checkSanity( yDot, equationDim );
		
		int spaceDim = equationDim / 2;
		
		double[] p = new double[spaceDim];
		for ( int i = 0; i < spaceDim; i++) p[i] = y[i]; 
		
		for ( int k = 0; k < spaceDim; k++ ) {
			yDot[k] = y[k + spaceDim];
			
			double a = 0;
			for ( int i = 0; i < spaceDim; i++ ) {
				double b = 0;
				for ( int j = 0; j < spaceDim; j++ ) {
					b += space.getChristoffelSymbolOfTheFirstKind( p )[k][i][j] * y[j + spaceDim];
				}
				a += b * y[i + spaceDim];
			}
			
			yDot[k + spaceDim] = -a;
		}
	}
		
	private static void checkSanity( double[] coordinates , int dim) {
		if ( coordinates == null ) {
			throw new NullPointerException();
		} else if ( coordinates.length != dim ) {
			throw new IllegalArgumentException();
		}
	}
}
