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
	public void getDistance() {
		DigitalFlatSquareThor testObject = new DigitalFlatSquareThor( 100, 200 );
		assertEquals( 100 ,testObject.getDistance(
				new double[] { 50.3, 50.9 }, 
				new double[] { 125, -50 }) , 0.0000000000001);
	}

}
