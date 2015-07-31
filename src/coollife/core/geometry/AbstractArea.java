package coollife.core.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractArea implements Area, Cloneable {

	@Override
	public abstract Area substruct(Area a);
	
	@Override
	public abstract Area intersect(Area a);

	@Override
	public abstract boolean isEmpty();
	
	public static Map<Integer, Collection<Area>> rank( Collection<? extends Area> set ) {
		
		Map<Integer, Collection<Area>> pool = new HashMap<>();
		goDeeper( set, pool, 1 );
		
		return pool;
	}
	
	private static void goDeeper( Collection<? extends Area> input, Map<Integer, Collection<Area>> pool, int depth ) {
		
		if ( input.isEmpty() ) return;
		
		Area area = null;
		
		for (Area a : input) {
			if ( a != null )
				if ( !a.isEmpty() ) {
					area = a; 
					break;
				}
		}
		
		if ( area == null) return;
		
		ArrayList<Area> inners = new ArrayList<>();
		ArrayList<Area> interceptions = new ArrayList<>();
		ArrayList<Area> outers = new ArrayList<>();
		
		Area inner = area;
		for ( Area a : input ) {
			if ( area != a ) {
				Area interception = area.intersect(a);
				interceptions.add( interception );
				outers.add( a.substruct(interception) );
				inner = inner.substruct(interception);
			}
		}
		
		goDeeper(interceptions, pool, depth + 1);
		goDeeper(outers, pool, depth);
		
		if ( !inner.isEmpty() ) inners.add(inner);
		
		if ( pool.containsKey(depth) ) {
			pool.get(depth).addAll(inners);
		} else {
			pool.put( depth, inners );
		}
				
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Area) {
			return this.substruct(((Area)obj)).isEmpty() && ((Area)obj).substruct(this).isEmpty();
		}
		return false;
	}
	
	@Override
	public Object clone() {
		return this.intersect(this);
	}

}
