package coollife.classicalConwayModel;

import java.util.Map;
import java.util.Set;

import coollife.Animate;
import coollife.core.geometry.Topology;

public class ConwayAnimal implements Animate {

	private boolean alive = true;
	private double[] position;
	private static Topology tp;
	
	public static void setTopology(Topology t) {
		if ( tp == null ) {
			tp = t;
		} else 
			throw new UnsupportedOperationException("Can't reset topology");
	}
	
	public ConwayAnimal(double[] p) {
		if (tp != null) {
			tp.transform(p);
			position = p;
		} else 
			throw new UnsupportedOperationException("Can't create animal without specified topology");
	}

	@Override
	public boolean isAlive() {
		return alive;
	}

	@Override
	public Animate evolve(Map<Integer, Set<Animate>> history) {
		//TODO: Implement it!!!
		Set<Animate> current = history.get(history.size());
		return null;
	}

}
