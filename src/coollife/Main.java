package coollife;

import coollife.classicalConwayModel.ClassicalConwayAnimal;
import coollife.classicalConwayModel.DigitalFlatSquareThor;
import coollife.classicalConwayModel.DigitalFlatSquareThorMapper;
import coollife.core.bio.Biosphere;
import coollife.core.mapper.VisualMapper;

public class Main {

	private static float fps = 5.0f;
	
	public static void main(String... args) {
		
		DigitalFlatSquareThor tor = new DigitalFlatSquareThor( 60, 40 );
		ClassicalConwayAnimal.setTopology(tor);
		
		Biosphere sph = new Biosphere( 
				new ClassicalConwayAnimal( new double[] { 50, 30 } ),
				new ClassicalConwayAnimal( new double[] { 51, 31 } ),
				new ClassicalConwayAnimal( new double[] { 49, 31 } ),
				new ClassicalConwayAnimal( new double[] { 51, 32 } ),
				new ClassicalConwayAnimal( new double[] { 49, 32 } ),
				new ClassicalConwayAnimal( new double[] { 50, 33 } ),
				new ClassicalConwayAnimal( new double[] { 52, 33 } )
				);
		
		VisualMapper conway = new DigitalFlatSquareThorMapper( tor, sph );

		
		@SuppressWarnings("unused")
		LifeMainWindow mainWindow = new LifeMainWindow( conway );
		
		LifeMainWindow.loop(fps);
	}

}
