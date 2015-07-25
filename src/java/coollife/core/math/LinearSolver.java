package coollife.core.math;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.IterativeLinearSolver;
import org.apache.commons.math3.linear.NonSquareOperatorException;
import org.apache.commons.math3.linear.RealLinearOperator;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.IterationManager;

public class LinearSolver extends IterativeLinearSolver {

	public LinearSolver(int maxIterations) {
		super(maxIterations);
		// TODO Auto-generated constructor stub
	}

	public LinearSolver(IterationManager manager) throws NullArgumentException {
		super(manager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public RealVector solveInPlace(RealLinearOperator a, RealVector b,
			RealVector x0) throws NullArgumentException,
			NonSquareOperatorException, DimensionMismatchException,
			MaxCountExceededException {
		// TODO Auto-generated method stub
		return null;
	}

}
