package coollife.core.geometry;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;

import coollife.core.math.BoundaryConditionProblem;
import coollife.core.math.GeodesicEquation;


public abstract class Topology {
	
	protected static int dim;
	
	public abstract double[][][] getChristoffelSymbolOfTheFirstKind( double[] p );
	public abstract double[][] getMetrics( double[] p );
	public abstract void transform( double[] p );
		
	public Topology(int dim) {
		if ( dim < 0 ) {
			throw new IllegalArgumentException();
		}
		Topology.dim = dim;
	}
	
	public final int getDimension() {
		return dim;				
	}
	
	public double getDistance( double[] p1, double[] p2 ) {
		
		defaultCheckSanity(p1, dim );
		defaultCheckSanity(p2, dim );
		
		transform(p1);
		transform(p2);
		
		GeodesicEquation eq = new GeodesicEquation( this );
		EulerIntegrator integrator = new EulerIntegrator(1000);
		
		int eqDim = eq.getDimension();
		double[] left = new double[eqDim];
		double[] right = new double[eqDim];
		for (int i = 0; i < eqDim; i++) {
			if ( i < dim ) {
				left[i] = p1[i];
				right[i] = p2[i];
			} else {
				left[i] = right[i] = BoundaryConditionProblem.NOT_DETERMINED;
			}
		}
		
		double[] realInitialConditions = BoundaryConditionProblem.convertToInitialConditianProblem(eq, left, right);
				
		UnivariateFunction path = new UnivariateFunction() {
			
			@Override
			public double value(double x) {
				double[] p = cutPoint( integrator.singleStep(eq, 0, realInitialConditions, x) );
				double[][] metrics = getMetrics( p );
				RealMatrix A = MatrixUtils.createRealMatrix(metrics);
				RealVector R = MatrixUtils.createRealVector( p );
				double toRet = A.operate(R).dotProduct(R);
				toRet = Math.sqrt(toRet);
				return toRet;
		    }
			
			private double[] cutPoint( double[] p ) {
				defaultCheckSanity(p, 2 * dim);
				double[] toRet = new double[ dim ];
				for ( int i = 0; i < dim; i++) toRet[i] = p[i + dim]; 
				return toRet;
			}
		};
		
		SimpsonIntegrator integral = new SimpsonIntegrator();
		double lenth = integral.integrate(1000, path, 0, 1);
		return lenth;
				
	}
	
	protected static void defaultCheckSanity( double[] coordinates , int dim) {
		if ( coordinates == null ) {
			throw new NullPointerException();
		} else if ( coordinates.length != dim ) {
			throw new IllegalArgumentException();
		}
	}
}
