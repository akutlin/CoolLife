package coollife.core.geometry;

public interface Area {
	
	public Area substruct( Area a );
	public Area intersect( Area a );
	public Area associate( Area a );
	public boolean isEmpty();
}
