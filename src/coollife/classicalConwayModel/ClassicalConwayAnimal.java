package coollife.classicalConwayModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import coollife.core.bio.Animate;
import coollife.core.bio.AbstractAnimate;
import coollife.core.geometry.AbstractArea;
import coollife.core.geometry.Area;

public class ClassicalConwayAnimal extends AbstractAnimate {

	public ClassicalConwayAnimal(double... p) {
		super(p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<? extends Animate> evolve(Map<Integer, Set<? extends Animate>> history) {
		
		Set<? extends Animate> current = history.get(history.size() - 1);
		Collection<Area> newLifeAreas = new HashSet<>();
		newLifeAreas.add( this.getReach() );
		
		int lifeCounter = 0;
		for (Animate a : current) {
			if ( a != this ) {
				double d = tp.getDistance( position, a.getPosition() );
				if ( d <= 2 ) {
					newLifeAreas.add( a.getReach() );
					if ( d <= 1) {
						lifeCounter++;
					}
				}
			}
		}
		
		if ( lifeCounter != 2 && lifeCounter != 3) alive = false;
		
		Map<Integer, Collection<Area>> globalPool = AbstractArea.rank(newLifeAreas);
		newLifeAreas = globalPool.get(3);
		
		HashSet<Animate> newBorns = new HashSet<>();
		if ( newLifeAreas != null ) {
			for ( Area a : newLifeAreas) {
				newBorns.addAll( ((Region) a).makeLife() );
			}
		}
		
		for (Animate a : (HashSet<Animate>) newBorns.clone() )
			if ( current.contains(a) || 
					tp.getDistance( position, a.getPosition() ) > 1 )
				newBorns.remove(a);
		
		return newBorns;
	}

	@Override
	public Area getReach() {
		return new Region();
	}
	
	private class Region extends AbstractArea {
		
		private final HashSet<double[]> region;
		
		public Region() {
			double[] pos = getPosition();
			HashSet<double[]> region = new HashSet<>();
			for ( int i = -1; i < 2; i++)
				for ( int j = -1; j < 2; j++) {
					double[] p = new double[]{ pos[0] + i, pos[1] + j};
					tp.transform(p);
					region.add( p );
				}
			this.region = region;
		}
		
		private Region( HashSet<double[]> region ) {
			this.region = region;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Area substruct(Area a) {
			if ( a instanceof Region ) {
				HashSet<double[]> region = (HashSet<double[]>) this.region.clone();
				for ( double[] p1 : this.region )
					for ( double[] p2 : ((Region)a).region )
						if ( p1[0] == p2[0] && p1[1] == p2[1])
							region.remove(p1);
				return new Region(region);
			}
			throw new IllegalArgumentException();
		}

		@Override
		public Area intersect(Area a) {
			if ( a instanceof Region ) {
				HashSet<double[]> region = new HashSet<>();
				for ( double[] p1 : this.region ) {
					for ( double[] p2 : ((Region)a).region )
						if ( p1[0] == p2[0] && p1[1] == p2[1])
							region.add(p1);
				}
				return new Region(region);
			}
			throw new IllegalArgumentException();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Area associate(Area a) {
			if ( a instanceof Region ) {
				HashSet<double[]> region = (HashSet<double[]>) this.region.clone();
				for ( double[] p : ((Region)a).region )
						region.add(p);
				return new Region(region);
			}
			throw new IllegalArgumentException();
		}

		@Override
		public boolean isEmpty() {
			return region.isEmpty();
		}
		
		public Set<Animate> makeLife() {
			HashSet<Animate> set = new HashSet<Animate>();
			for ( double[] p : region ) {
				set.add( new ClassicalConwayAnimal( p ) );
			}
			return set;
		}
		
		@Override
		public String toString() {
			StringBuilder ret = new StringBuilder("{ ");
			for ( double[] p : region ) {
				ret.append("(");
				ret.append(p[0]);
				ret.append(",");
				ret.append(p[1]);
				ret.append(") ");
			}
			ret.append( "}" );
			return ret.toString();
		}
		
		@Override
		public int hashCode() {
			int hash = 0;
			for ( double[] p : region )
				hash += Arrays.hashCode(p);
			return hash;
		}		
	}
	
}
