package coollife.core.bio;

import java.util.Arrays;

import coollife.core.geometry.Topology;

public abstract class AbstractAnimate implements Animate {

	protected boolean alive = true;
	protected double[] position;
	protected static Topology tp;
	
	public static void setTopology(Topology t) {
		if ( tp == null ) {
			tp = t;
		} else 
			throw new UnsupportedOperationException("Can't reset topology");
	}
	
	public AbstractAnimate(double[] p) {
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
	public double[] getPosition() {
		return position;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractAnimate) {
			return 0 == tp.getDistance( getPosition(), ((AbstractAnimate) obj).getPosition() );
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(position);
	}
	
	@Override
	public String toString() {
		return "@(" + position[0] + ", " + position[1] + ")";
	}

}
