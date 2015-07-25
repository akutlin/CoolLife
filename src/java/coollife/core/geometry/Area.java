package coollife.core.geometry;

public interface Area {
	
	Area substruct( Area a );
	Area intersection( Area a );
	boolean isEmpty();

}
