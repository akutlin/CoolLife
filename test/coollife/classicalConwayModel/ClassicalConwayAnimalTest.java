package coollife.classicalConwayModel;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import coollife.core.bio.Animate;

public class ClassicalConwayAnimalTest {
	
	@BeforeClass
	public static void setTopology() {
		ClassicalConwayAnimal.setTopology( new DigitalFlatSquareThor( 100, 100 ) );
	}
	
	@Test
	public void singleAloneDeath() {
		
		ClassicalConwayAnimal a = new ClassicalConwayAnimal( new double[] { 50, 25 } );
		Set<ClassicalConwayAnimal> set = new HashSet<>(); 
		set.add( a );
		Map<Integer, Set<? extends Animate>> map = new HashMap<>();
		map.put(0, set);
		Set<? extends Animate> newBorns = a.evolve(map);
		
		assertFalse( a.isAlive() );
		assertTrue( newBorns.isEmpty() );

	}
	
	@Test
	public void multipleAloneDeaths() {
		
		ClassicalConwayAnimal[] arr = new ClassicalConwayAnimal[19];
		Set<ClassicalConwayAnimal> set = new HashSet<>(); 

		for ( int i = 0; i < 19; i++ ) {
			arr[i] = new ClassicalConwayAnimal( new double[] { 5 * i, 5 * i } );
			set.add( arr[i] );
		}
		
		Map<Integer, Set<? extends Animate>> map = new HashMap<>();
		map.put(0, set);
		
		for ( int i = 0; i < 19; i++ ) {
			assertTrue( arr[i].evolve(map).isEmpty() );
			assertFalse( arr[i].isAlive() );
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void multipleCollectiveDeaths() {
		
		Set<ClassicalConwayAnimal> set = new HashSet<>(); 

		for ( int i = 0; i < 4; i++ ) 
			for ( int j = 0; j < 4; j++ )
				set.add( new ClassicalConwayAnimal( new double[] { 50 + i, 50 + j } ) );
		
		Map<Integer, Set<? extends Animate>> map = new HashMap<>();
		map.put(0, set);
		
		for ( int i = 0; i < 4; i++ ) {
			Set<ClassicalConwayAnimal> next = new HashSet<>();
			for ( Animate a : map.get(i) ) {
				next.addAll( (Collection<? extends ClassicalConwayAnimal>) a.evolve(map) );
				if ( a.isAlive() )
					next.add((ClassicalConwayAnimal) a);
			}
			map.put( i + 1, next);
		}
		
		assertFalse( map.get(1).isEmpty() );
		assertFalse( map.get(2).isEmpty() );
		assertFalse( map.get(3).isEmpty() );
		
		assertTrue( map.get(4).isEmpty() );
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void topologyTest() {
		Set<ClassicalConwayAnimal> set = new HashSet<>();
		
		ClassicalConwayAnimal a1 = new ClassicalConwayAnimal(0,0);
		ClassicalConwayAnimal a2 = new ClassicalConwayAnimal(0,99);
		ClassicalConwayAnimal a3 = new ClassicalConwayAnimal(99,0);
		ClassicalConwayAnimal a4 = new ClassicalConwayAnimal(99,99);

		set.add( a1 );
		set.add( a2 );
		set.add( a3 );
		set.add( a4 );
		
		Map<Integer, Set<? extends Animate>> map = new HashMap<>();
		map.put(0, set);
		
		for ( int i = 0; i < 10; i++ ) {
			Set<ClassicalConwayAnimal> next = new HashSet<>();
			for ( Animate a : map.get(i) ) {
				next.addAll( (Collection<? extends ClassicalConwayAnimal>) a.evolve(map) );
				if ( a.isAlive() )
					next.add((ClassicalConwayAnimal) a);
			}
			
			assertTrue( next.containsAll(set) );
			assertEquals( 4, next.size() );
			
			map.put( i + 1, next);
		}
		
	}
	
}
