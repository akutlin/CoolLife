package coollife.core.geometry;

public interface Topology {
	
	public int getDimension();
	public double getDistance( double[] p1, double[] p2 );
	public void translate( double[] p0, double[] v0, double length );
	public void transform(double[] p);
	
}
