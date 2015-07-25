package coollife.core.geometry;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;

import coollife.core.math.GeodesicEquation;
import coollife.core.math.LinearSolver;


public abstract class Topology {
	
	protected static int dim;
	
	public abstract double[][][] getChristoffelSymbolOfTheFirstKind( double[] point );
	public abstract double[][] getMetrics( double[] point );
	public abstract double[] transform( double[] r);
		
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
		
		checkSanity(p1, dim );
		checkSanity(p2, dim );
		
		p1 = transform(p1);
		p2 = transform(p2);
		
		GeodesicEquation eq = new GeodesicEquation( this );
		EulerIntegrator integrator = new EulerIntegrator(1000);
		int eqDim = eq.getDimension();
		
		double[][] Basis = new double[ eqDim ][ eqDim ];
		for ( int i = 0; i < eqDim; i++) Basis[i][i] = 1;
		double[][] basis = Basis.clone();
		
		for ( int i = 0; i < eqDim; i++) {
			integrator.integrate( eq, 0, basis[i], 1, basis[i]);
		}
		
		double[] coeff = new double[eqDim];
		double[] finish = new double[dim];
		for ( int i = 0; i < dim; i++) {
			finish[i] = p2[i];
			coeff[i] = p1[i];
			for ( int j = 0; j < dim; j++) {
				finish[j] -= basis[i][j] * p1[i];
			}
		}	
		
		double[][] dest = new double[dim][dim];
		MatrixUtils.createRealMatrix(basis).copySubMatrix( 0, dim - 1, dim, eqDim - 1, dest);
		RealVector vector =  MatrixUtils.createRealVector(finish);
		RealLinearOperator matrix = new Array2DRowRealMatrix( dest );
		LinearSolver solver = new LinearSolver(1000);
		
		double[] last = solver.solve(matrix, vector).toArray();
		
		for ( int i = 0; i < dim; i++) {
			coeff[i + dim] = last[i];
		}
		
		double[] realInitialConditions = new double[eqDim];
		basis = Basis.clone();
		
		for ( int i = 0; i < eqDim; i++) {
			for ( int j = 0; j < eqDim; j++) {
				realInitialConditions[j] -= basis[i][j] * coeff[i];
			}
		}
		
		SimpsonIntegrator integral = new SimpsonIntegrator();
		LengthFunction func = new LengthFunction( integrator, realInitialConditions, eq );
		double lenth = integral.integrate(1000, func, 0, 1);
		return lenth;
				
	}
	
	private class LengthFunction implements UnivariateFunction {
	 
		private RungeKuttaIntegrator integrator;
		private double[] initialConditions;
		private GeodesicEquation eq;
		
		public LengthFunction(RungeKuttaIntegrator integrator, double[] initialConditions, GeodesicEquation eq) {
			checkSanity( initialConditions, eq.getDimension() );
			this.integrator = integrator;
			this.initialConditions = initialConditions;
			this.eq = eq;
		}
		
		public double value(double x) {
			double[] p = integrator.singleStep(eq, 0, initialConditions, x);
			double[][] metrics = getMetrics( p );
			RealMatrix A = MatrixUtils.createRealMatrix(metrics);
			RealVector R = MatrixUtils.createRealVector( p );
			double toRet = A.operate(R).dotProduct(R);
			toRet = Math.sqrt(toRet);
			return toRet;
	    }
	}
	
	protected static void checkSanity( double[] coordinates , int dim) {
		if ( coordinates == null ) {
			throw new NullPointerException();
		} else if ( coordinates.length != dim ) {
			throw new IllegalArgumentException();
		}
	}
}
