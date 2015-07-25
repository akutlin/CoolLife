package coollife.core.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class TopologyTest {

	@Test
	public void getDistanceTest() {
		
		int dim = 3;
		
		Topology euqlideanSpace = new Topology(dim) {

			@Override
			public double[][][] getChristoffelSymbolOfTheFirstKind(
					double[] point) {
				return new double[dim][dim][dim];
			}

			@Override
			public double[][] getMetrics(double[] point) {
				double[][] m = new double[dim][dim];
				for ( int i = 0; i < dim; i++) m[i][i] = 0;
				return m;
			}

			@Override
			public double[] transform(double[] r) {
				return r;
			}	
		};
		
		double[] p1 = new double[dim];
		double[] p2 = new double[dim];
		for ( int i = 0; i < dim; i++) p2[i] = 1; 
		
		assertEquals( Math.sqrt(dim), euqlideanSpace.getDistance(p1, p2), 0.001 );
			
	}

}
