package coollife;

import java.util.Map;
import java.util.Set;

public interface Animate {
	
	public boolean isAlive();
	public Animate evolve( Map<Integer, Set<Animate>> history );

}
