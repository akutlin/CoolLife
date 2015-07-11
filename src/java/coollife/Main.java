package coollife;

public class Main {

	private static float fps = 25.0f;
	
	public static void main(String... args) {
		
		@SuppressWarnings("unused")
		LifeMainWindow mainWindow = new LifeMainWindow();
		
		LifeMainWindow.loop(fps);
	}

}
