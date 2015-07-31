package coollife.core.geometry;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;

import coollife.core.math.BoundaryConditionProblem;
import coollife.core.math.GeodesicEquation;


public abstract class AbstractTopology implements Topology {
	
	protected static int dim;
	
	private GeodesicEquation eq;
	private EulerIntegrator evaluator;
	private SimpsonIntegrator integrator;
	private PathFunction path;
	
	public abstract double[][][] getChristoffelSymbolOfTheFirstKind( double[] p );
	public abstract double[][] getMetrics( double[] p );
	public abstract void transform( double[] p );
	public abstract double getSeed( double[] p );
			
	public AbstractTopology(int dim) {
		if ( dim < 0 ) {
			throw new IllegalArgumentException();
		}
		AbstractTopology.dim = dim;
		eq = new GeodesicEquation( this );
		evaluator = new EulerIntegrator(1000);
		integrator = new SimpsonIntegrator();
		path = new PathFunction();
	}
	
	@Override
	public final int getDimension() {
		return dim;				
	}
	
	@Override
	public double getDistance( double[] p1, double[] p2 ) {
		
		defaultCheckSanity(p1, dim );
		defaultCheckSanity(p2, dim );
		
		transform(p1);
		transform(p2);
				
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
		return getPathLength( realInitialConditions, 0, 1);
				
	}
	
	protected double getPathLength( double[] state, double t1, double t2 ) {
		path.setState(state);
		return integrator.integrate(1000, path, t1, t2);
	}
	
	@Override
	public void translate( double[] p0, double[] v0, double length ) {
		
		defaultCheckSanity(p0, dim);
		defaultCheckSanity(v0, dim);
		transform(p0);
		
		int eqDim = eq.getDimension();
		double[] initialState = new double[ eqDim ];
		for ( int i = 0; i < eqDim; i++ ) {
			initialState[i] = i < dim ? p0[i] : v0[i - dim];
		}

		double[] state = initialState.clone();
		path.setState(state);
		double seed = getSeed(p0);
		seed = Double.isInfinite(seed) ? 1 : seed;
		double step = seed  * seed / getPathLength( state, 0, seed);
		
		double[] stateDot = new double[eqDim];
		double[] stateDotDot = new double[eqDim];
		eq.computeDerivatives( 0, state, stateDotDot);
		eq.computeDerivatives( step, state, stateDot);
		for ( int i = 0; i < eqDim; i++) {
			state[i] += step * stateDot[i];
		}
		
		double v, f, x = step, l = 0;
		
		while ( l < length ) {
			
			v = 0; f = 0;
			for ( int i = 0; i < eqDim; i++) {
				stateDotDot[i] = (stateDot[i] - stateDotDot[i]);
				v += stateDotDot[i] * stateDotDot[i];
				f += stateDot[i] * stateDot[i];
			}
			v = Math.sqrt(v) / step;
			f = Math.sqrt(f);
			if ( f == 0) throw new RuntimeException(); 
			step = v == 0 ? step : f/v;
			
			l += path.value(x) * step;
			x += step;
			eq.computeDerivatives(x, state, stateDot);
			for ( int i = 0; i < eqDim; i++) {
				state[i] += step * stateDot[i];
			}
			
		}
		
		l = getPathLength( initialState, 0, x);
		double shift = length - l;
		
		l = getPathLength( state, shift, 0);
		if ( l != 0 ) {
			step = shift * shift / l;
			eq.computeDerivatives( -step, state, stateDot);
			for ( int i = 0; i < eqDim; i++) {
				state[i] -= step * stateDot[i];
			}
		}
		
		for ( int i = 0; i < dim; i++) {
			p0[i] = state[i];
			v0[i] = state[dim + i];
		}
		
		transform(p0);
	}
	
	private class PathFunction implements UnivariateFunction {
		
		private double[] state = new double[ eq.getDimension() ];
		
		@Override
		public double value(double x) {
			double[] p = evaluator.singleStep(eq, 0, state, x);
			double[][] metrics = getMetrics( getPoint(p) );
			RealMatrix A = MatrixUtils.createRealMatrix(metrics);
			RealVector R = MatrixUtils.createRealVector( getVelocity(p) );
			double toRet = A.operate(R).dotProduct(R);
			toRet = Math.sqrt(toRet);
			return toRet;
	    }
		
		public void setState( double[] state ) {
			this.state = state;
		}
		
		public double[] getPoint( double[] p ) {
			defaultCheckSanity(p, 2 * dim);
			double[] toRet = new double[ dim ];
			for ( int i = 0; i < dim; i++) toRet[i] = p[i]; 
			return toRet;
		}
		
		public double[] getVelocity( double[] p ) {
			defaultCheckSanity(p, 2 * dim);
			double[] toRet = new double[ dim ];
			for ( int i = 0; i < dim; i++) toRet[i] = p[i + dim]; 
			return toRet;
		}
	}
	
	protected static void defaultCheckSanity( double[] coordinates , int dim) {
		if ( coordinates == null ) {
			throw new NullPointerException();
		} else if ( coordinates.length != dim ) {
			throw new IllegalArgumentException();
		}
	}
}
