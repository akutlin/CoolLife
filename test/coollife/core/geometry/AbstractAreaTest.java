package coollife.core.geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

public class AbstractAreaTest {
	
	private class Area1D extends AbstractArea {

		protected HashSet<Integer> area;
		
		@SuppressWarnings("unchecked")
		public Area1D( HashSet<Integer> arr ) {
			area = (HashSet<Integer>) arr.clone();
		}
		
		public Area1D( int[] arr ) {
			area = new HashSet<Integer>();
			for ( int i = 0; i < arr.length; i++) area.add(arr[i]); 
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Area substruct(Area a) {
			HashSet<Integer> newArea = (HashSet<Integer>) area.clone();
			for (int i : ((Area1D)a).area) {
				for (int j : area) {
					if ( i == j ) {
						newArea.remove(i);
					}
				}
			}
			return new Area1D( newArea );
		}

		@Override
		public Area intersect(Area a) {
			HashSet<Integer> newArea = new HashSet<>();
			for (int i : ((Area1D)a).area) {
				for (int j : area) {
					if ( i == j ) {
						newArea.add(i);
					}
				}
			}
			return new Area1D( newArea );
		}

		@SuppressWarnings("unchecked")
		@Override
		public Area associate(Area a) {
			HashSet<Integer> newArea = (HashSet<Integer>) area.clone();
			for (int i : ((Area1D)a).area) 
						newArea.add(i);
			return new Area1D( newArea );
		}

		@Override
		public boolean isEmpty() {
			return area.isEmpty();
		}
		
		@Override
		public String toString() {
			StringBuilder strBldr = new StringBuilder("[");
			for ( int i : area ) {
				strBldr.append(i + ",");
			}
			strBldr.deleteCharAt(strBldr.length() - 1);
			strBldr.append("]");
			return strBldr.toString();
		}
		
		
		@Override
		public int hashCode() {
			return Arrays.hashCode( area.toArray());
		}
		
	}
	
	@Test
	public void isEmpty() {
		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {1,2});
		Area a3 = new Area1D( new int[] {});
		
		assertFalse( a1.isEmpty() );
		assertFalse( a2.isEmpty() );
		assertTrue( a3.isEmpty());

	}
	
	@Test
	public void equals() {
		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {1});
		Area a3 = new Area1D( new int[] {2});
		Area a4 = new Area1D( new int[] {});
		Area a5 = new Area1D( new int[] {});
		
		assertEquals(a1, a2);
		assertEquals(a4, a5);
		assertNotEquals( a2, a3);
		assertNotEquals( a1, a4);

	}
	
	@Test
	public void cloneTest() {
		AbstractArea a1 = new Area1D( new int[] {2,3,4});
		AbstractArea a2 = new Area1D( new int[] {1,2,3});
		AbstractArea a3 = new Area1D( new int[] {2,3});
		AbstractArea a4 = new Area1D( new int[] {4,5});
		AbstractArea a5 = new Area1D( new int[] {});
		
		assertEquals( a1, a1.clone() );
		assertEquals( a2, a2.clone() );
		assertEquals( a3, a3.clone() );
		assertEquals( a3, a3.clone() );
		assertEquals( a4, a4.clone() );
		assertEquals( a5, a5.clone() );

	}
	
	@Test 
	public void hashCodeTest() {
		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {1});
		Area a3 = new Area1D( new int[] {2});
		Area a4 = new Area1D( new int[] {});
		Area a5 = new Area1D( new int[] {});
		
		assertEquals( a1.hashCode(), a2.hashCode() );
		assertEquals( a4.hashCode(), a5.hashCode() );
		assertNotEquals( a2.hashCode(), a3.hashCode());
		assertNotEquals( a1.hashCode(), a4.hashCode());
	}
	
	@Test
	public void substract() {
		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {1,2,3});
		Area a3 = new Area1D( new int[] {2,3});
		Area a4 = new Area1D( new int[] {});
		Area a5 = new Area1D( new int[] {});
		
		assertEquals( a1, a2.substruct(a3) );
		assertEquals( a4, a3.substruct(a2) );
		assertEquals( a2, a2.substruct(a4) );
		assertEquals( a4, a3.substruct(a3) );
		assertEquals( a4, a5.substruct(a4) );
		
	}
	
	@Test
	public void intersect() {
		Area a1 = new Area1D( new int[] {2,3,4});
		Area a2 = new Area1D( new int[] {1,2,3});
		Area a3 = new Area1D( new int[] {2,3});
		Area a4 = new Area1D( new int[] {4,5});
		Area a5 = new Area1D( new int[] {});
		
		assertEquals( a3, a2.intersect(a3) );
		assertEquals( a3, a2.intersect(a1) );
		assertEquals( a5, a3.intersect(a4) );
		assertEquals( a1, a1.intersect(a1) );
		assertEquals( a5, a5.intersect(a1) );
		
	}
	
	@Test
	public void associate() {
		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {2});
		Area a3 = new Area1D( new int[] {3});
		Area a4 = new Area1D( new int[] {1,2,3});
		Area a5 = new Area1D( new int[] {});
		
		assertEquals( a4, a4.associate(a4) );
		assertEquals( a3, a3.associate(a3) );
		assertEquals( a4, a1.associate(a2).associate(a3) );
		assertEquals( a2, a2.associate(a5) );
		assertEquals( a5, a5.associate(a5) );
		
	}
	
	@Test
	public void simpleRank1() {
		HashSet<Area> set = new HashSet<>();

		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {2});
		
		set.add( a1 );
		set.add( a2 );
		
		Map<Integer, Collection<Area>> map= AbstractArea.rank(set);
		
		assertTrue( map.get(1).contains(a1) );
		assertTrue( map.get(1).contains(a2) );

	}
	
	@Test
	public void simpleRank2() {
		ArrayList <Area> set = new ArrayList <>();

		Area a1 = new Area1D( new int[] {1});
		Area a2 = new Area1D( new int[] {1});
		Area a3 = new Area1D( new int[] {1});
		
		set.add( a1 );
		set.add( a2 );
		set.add( a3 );
		
		Map<Integer, Collection<Area>> map= AbstractArea.rank(set);
		
		assertTrue( map.get(3).contains(a1) );
		assertTrue( map.get(3).contains(a2) );
		assertTrue( map.get(3).contains(a3) );

	}
	

	@Test
	public void rank() {
		HashSet<Area> set = new HashSet<>();

		set.add( new Area1D( new int[] {5}) );
		set.add( new Area1D( new int[] {4,5}) );
		set.add( new Area1D( new int[] {5,3,4}) );
		set.add( new Area1D( new int[] {5,4,2,3}) );
		set.add( new Area1D( new int[] {1,3,2,4,5}) );
		
		Map<Integer, Collection<Area>> map= AbstractArea.rank(set);
				
		assertTrue( map.get(4).contains( new Area1D( new int[] {4}) ) );
		assertTrue( map.get(5).contains( new Area1D( new int[] {5}) ) );
		assertFalse( map.get(2).contains( new Area1D( new int[] {5}) ) );
		assertFalse( map.get(3).contains( new Area1D( new int[] {4})) );
	}
	
	@Test
	public void rankStressTest() {
		ArrayList<Area> set = new ArrayList<>();
		int areaCount = 100;
		int maxPointCount = 100;
		int[] etalon = new int[maxPointCount];
		for ( int i = 0; i < areaCount; i++ ) {
			int currentPointCount = (int) (Math.random() * maxPointCount);
			HashSet<Integer> arr = new HashSet<>();
			while ( arr.size() != currentPointCount ) {
				int tmp = (int) (Math.random() * maxPointCount);
				if ( !arr.contains(tmp) ) {
					arr.add(tmp);
					etalon[tmp]++;
				}
			}
			set.add( new Area1D(arr) );
		}
		
		Map<Integer, Collection<Area>> map= AbstractArea.rank(set);
		
		for ( int i : map.keySet() ) {
			HashSet<Integer> arr = new HashSet<>();
			for ( int j = 0; j < maxPointCount; j++ )
				if ( etalon[j] == i )
					arr.add(j);
			Area testArea = new Area1D( new int[]{} );
			for ( Area a : map.get(i) ) {
				testArea = testArea.associate(a);
			}
			assertEquals( new Area1D( arr ), testArea );
		}
	}

}
