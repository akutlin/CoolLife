package coollife;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Biosphere {
	
	private Set<Animate> animatePool;
	private final Map<Integer, Set<Animate>> history = new HashMap<>();
	
	private void addAnimate( Animate a ) {
		synchronized(animatePool) {
			animatePool.add(a);
		}
	}
	
	private void updateHistory(Set<Animate> pool) {
		synchronized(history) {
			int count = history.size();
			history.put(count, pool);
		}
	}

	public Biosphere( Animate... animate ) {
		animatePool = new HashSet<>();
		for (Animate a : animate) {
			addAnimate(a);
		}
		updateHistory(animatePool);
	}
	
	public synchronized void turn() {
		animatePool = new HashSet<>();
		synchronized(animatePool) {
			for ( Animate e : history.get(history.size()) ) {
				Animate newborn = e.evolve(Collections.unmodifiableMap(history));
				if (newborn != null) animatePool.add(newborn);
				if (e.isAlive()) animatePool.add(e);
			}
		}
		updateHistory(animatePool);
	}
	
	public Set<Animate> getAnimatePool() {
		synchronized(animatePool) {
			return Collections.unmodifiableSet(animatePool);
		}
	}

}
