package coollife.core.geometry;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class TopologyTest {

	@Test
	public void getDistanceInEuqlideanSpace() {
		
		int maxDim = 6;
		
		for (int dim = 1; dim < maxDim; dim++) {
			
			Topology euqlideanSpace = new Topology(dim) {

				@Override
				public double[][][] getChristoffelSymbolOfTheFirstKind(
						double[] point) {
					return new double[dim][dim][dim];
				}

				@Override
				public double[][] getMetrics(double[] point) {
					double[][] m = new double[dim][dim];
					for ( int i = 0; i < dim; i++) m[i][i] = 1;
					return m;
				}

				@Override
				public void transform(double[] r) {}	
			};
			
			double[] p1 = new double[dim];
			double[] p2 = new double[dim];
			for ( int i = 0; i < dim; i++) p2[i] = 1; 
			
			assertEquals( Math.sqrt(dim), euqlideanSpace.getDistance(p1, p2), 0.001 );
		}
	}
	
	@Test
	public void getDistanceOnSphere2D() {
		
		int dim = 2;
		double radius = 1;
		
		Topology sphere2D = new Topology(dim) {
			
			//Coordinates: fi and teta
			
			double r = radius;

			@Override
			public double[][][] getChristoffelSymbolOfTheFirstKind(
					double[] point) {
				transform(point);
				return new double[][][] {
						{ {0 , 0}, {0 , 0}}, 
						{ {0 , 0}, {0 , 2 * Math.pow(r, dim) * Math.sin( point[1] )} }
				};
			}

			@Override
			public double[][] getMetrics(double[] point) {
				transform(point);
				return new double[][] {
						{ Math.pow(r, dim), 			      0 				},
						{ 		0, 		Math.pow( r * Math.sin(point[1]), dim) }
				};
			}

			@Override
			public void transform(double[] r) {
				defaultCheckSanity(r, dim);
				while (r[0] < 0) r[0] += 2 * Math.PI;
				r[0] = r[0] % (2 * Math.PI);
				while (r[1] < 0) r[1] += Math.PI;
				r[1] = r[1] % Math.PI;
			}	
		};
		
		double[] p1 = new double[] { 0, Math.PI / 2};
		double[] p2 = new double[] { Math.PI / 2, Math.PI / 2};;		
		assertEquals( Math.PI * radius / 2, sphere2D.getDistance(p1, p2), 0.001 );
	}
	
	@Test
	@Ignore
	public void getDistanceOnSphere2DSpecialPoint() {
		
		int dim = 2;
		double radius = 1;
		
		Topology sphere2D = new Topology(dim) {
			
			//Coordinates: fi and teta
			
			double r = radius;

			@Override
			public double[][][] getChristoffelSymbolOfTheFirstKind(
					double[] point) {
				transform(point);
				return new double[][][] {
						{ {0 , 0}, {0 , 0}}, 
						{ {0 , 0}, {0 , 2 * Math.pow(r, dim) * Math.sin( point[1] )} }
				};
			}

			@Override
			public double[][] getMetrics(double[] point) {
				transform(point);
				return new double[][] {
						{ Math.pow(r, dim), 			      0 				},
						{ 		0, 		Math.pow( r * Math.sin(point[1]), dim) }
				};
			}

			@Override
			public void transform(double[] r) {
				defaultCheckSanity(r, dim);
				while (r[0] < 0) r[0] += 2 * Math.PI;
				r[0] = r[0] % (2 * Math.PI);
				while (r[1] < 0) r[1] += Math.PI;
				r[1] = r[1] % Math.PI;
			}	
		};
		
		double[] p1 = new double[] { 0, 0 };
		double[] p2 = new double[] { Math.PI / 2, Math.PI / 2};;		
		assertEquals( Math.PI * radius / 2, sphere2D.getDistance(p1, p2), 0.001 );
	}

}
