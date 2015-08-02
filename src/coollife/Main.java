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
		
		ClassicalConwayAnimal[] arr = new ClassicalConwayAnimal[60];
		for ( int i = 0; i < 60; i++) 
			arr[i] = new ClassicalConwayAnimal(i,20);
		
		Biosphere sph = new Biosphere(arr);
		VisualMapper conway = new DigitalFlatSquareThorMapper( tor );
		
		MainWindow mainWindow = new MainWindow( conway, sph );
		MainWindow.loop(fps);
	}

}
