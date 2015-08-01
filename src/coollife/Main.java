package coollife;

import coollife.classicalConwayModel.ClassicalConwayAnimal;
import coollife.classicalConwayModel.DigitalFlatSquareThor;
import coollife.classicalConwayModel.DigitalFlatSquareThorMapper;
import coollife.core.bio.Biosphere;
import coollife.core.mapper.VisualMapper;

public class Main {

	private static float fps = 25.0f;
	
	@SuppressWarnings("unused")
	public static void main(String... args) {
		
		DigitalFlatSquareThor tor = new DigitalFlatSquareThor( 60, 40 );
		ClassicalConwayAnimal.setTopology(tor);
		Biosphere sph = new Biosphere();
		VisualMapper conway = new DigitalFlatSquareThorMapper( tor, sph );
		
		MainWindow mainWindow = new MainWindow( conway );
		MainWindow.loop(fps);
	}

}
