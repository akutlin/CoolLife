package coollife.core.math;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;

public class BoundaryConditionProblem {

	public final static double NOT_DETERMINED = Double.MAX_VALUE;
	
	public static double[] convertToInitialConditianProblem( FirstOrderDifferentialEquations eq, double[] left, double[] right) {
		
		checkSanity( eq, left, right);
		
		EulerIntegrator integrator = new EulerIntegrator(1000);
		
		//Default basis in solution space
		int eqDim = eq.getDimension();
		double[][] basis = new double[ eqDim ][ eqDim ];
		for ( int i = 0; i < eqDim; i++) basis[i][i] = 1;
		
		//Basis for the given problem
		for ( int i = 0; i < eqDim; i++) {
			integrator.integrate( eq, 0, basis[i], 1, basis[i]);
		}
		
		double[] coeff = new double[eqDim];
		double[] newRight = right.clone();
		
		int rightDim = 0;
		for ( int i = 0; i < eqDim; i++) {
			if ( left[i] == NOT_DETERMINED ) {
				coeff[i] = NOT_DETERMINED;
				rightDim++;
			} else {
				coeff[i] = left[i];
				for ( int j = 0; j < eqDim; j++) {
					newRight[j] -= newRight[j] == NOT_DETERMINED ? 0 : basis[i][j] * coeff[i];
				}
			}
		}
		
		double[][] A = new double[rightDim][rightDim];
		double[] b = new double[rightDim];
		for ( int i = 0, j = 0; i < eqDim ; i++) {
			if ( newRight[i] != NOT_DETERMINED ) {
				b[j] = newRight[i];
				for ( int s = 0, t = 0; s < eqDim ; s++) {
					if ( left[s] == NOT_DETERMINED ) {
						A[t][j] = basis[s][i];
						t++;
					}
				}
				j++;
			}
		}
		
		RealVector vector =  MatrixUtils.createRealVector( b );
		RealLinearOperator matrix = new Array2DRowRealMatrix( A );
		LinearSolver solver = new LinearSolver(1000);
		double[] x = solver.solve(matrix, vector).toArray();
		
		for ( int i = 0, j = 0; i < eqDim; i++) {
			if ( coeff[i] == NOT_DETERMINED ) {
				coeff[i] = x[j];
				j++;
			}
		}
		
		double[] realInitialConditions = new double[eqDim];
		for ( int i = 0; i < eqDim; i++ ) {
			for ( int j = 0; j < eqDim; j++ ) {
				realInitialConditions[i] += basis[j][i] * coeff[j];
			}
		}
		return realInitialConditions;
	}
	
	private static void checkSanity( FirstOrderDifferentialEquations eq, double[] left, double right[] ) {
		if ( eq == null || left == null || right == null ) {
			throw new NullPointerException();
		} 
		
		int eqDim = eq.getDimension();
		
		if ( left.length != eqDim ) {
			throw new IllegalArgumentException();
		} else if ( right.length != eqDim ) {
			throw new IllegalArgumentException();
		}
		
		int counter = 0;
		for ( double el : left )
			if (el == NOT_DETERMINED ) 
				counter++;
		for ( double el : right )
			if (el == NOT_DETERMINED ) 
				counter++;
		
		if ( counter != eqDim ) {
			throw new IllegalArgumentException();
		}
	}

}
