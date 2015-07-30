package coollife.classicalConwayModel;

import static org.junit.Assert.*;

import org.junit.Test;

public class DigitalFlatSquareThorTest {
	
	@Test(expected = IllegalArgumentException.class)
	public void IllegalArgumentExceptionThrowing() {
		new DigitalFlatSquareThor( -1, 0 );
	}
	
	@Test
	public void getSize() {
		DigitalFlatSquareThor testObject = new DigitalFlatSquareThor( 100, 200 );
		assertEquals( 100, testObject.getSize()[0] );
		assertEquals( 200, testObject.getSize()[1] );
	}
	
	@Test
	public void transform() {
		DigitalFlatSquareThor testObject = new DigitalFlatSquareThor( 100, 200 );
		
		double[] p = new double[] { 36,78 };
		testObject.transform(p);
		assertEquals( 36, p[0], 0);
		assertEquals( 78, p[1], 0);
		
		p = new double[] { 36, 278 };
		testObject.transform(p);
		assertEquals( 36, p[0], 0);
		assertEquals( 78, p[1], 0);
		
		p = new double[] { 136, 278 };
		testObject.transform(p);
		assertEquals( 36, p[0], 0);
		assertEquals( 78, p[1], 0);
		
		p = new double[] { -164, -122 };
		testObject.transform(p);
		assertEquals( 36, p[0], 0);
		assertEquals( 78, p[1], 0);
		
		p = new double[] { -164 - 1000, 278 };
		testObject.transform(p);
		assertEquals( 36, p[0], 0);
		assertEquals( 78, p[1], 0);
		
		p = new double[] { 36.4, 77.5 };
		testObject.transform(p);
		assertEquals( 36, p[0], 0);
		assertEquals( 78, p[1], 0);
		
		p = new double[] { 0, 200 };
		testObject.transform(p);
		assertEquals( 0, p[0], 0);
		assertEquals( 0, p[1], 0);
	}
	
	@Test
	public void getDistance() {
		DigitalFlatSquareThor testObject = new DigitalFlatSquareThor( 100, 200 );
		
		assertEquals( 100 ,testObject.getDistance(
				new double[] { 50.3, 49.9 }, 
				new double[] { 125, -50 }) , 0.0000000000001);
		
		assertEquals( 0 ,testObject.getDistance(
				new double[] { 0, 0 }, 
				new double[] { 100, 200 }) , 0.0000000000001);
	}
	
	@Test
	public void translate() {
		DigitalFlatSquareThor testObject = new DigitalFlatSquareThor( 100, 200 );
		double[] p = new double[] { 104, 37 };
		double[] v = new double[] { -1, -5 };
		testObject.translate(p, v, 73);
		assertEquals( 89, p[0], 0.001);
		assertEquals( 164, p[1], 0.001);
		assertEquals( -1, v[0], 0.001);
		assertEquals( -5, v[1], 0.001);
	}

}
