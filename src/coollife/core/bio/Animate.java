package coollife.core.bio;

import coollife.core.geometry.Area;

import java.util.Map;
import java.util.Set;

public interface Animate {
	
	public boolean isAlive();
	public Set<? extends Animate> evolve( Map<Integer, Set<? extends Animate>> history );
	public double[] getPosition();
	public Area getReach();

}
