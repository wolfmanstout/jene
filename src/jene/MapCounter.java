package jene;

import java.util.HashMap;
import java.util.Map;

/**
 *  
 * @author James Stout
 *
 */
 
 public class MapCounter<K> {

	private final Map<K, Integer> _map = new HashMap<K, Integer>();
	private int _totalCount;
	
	public void incrementCount(K key) {
		_totalCount++;
		if (!_map.containsKey(key)) {
			_map.put(key, 1);
		} else {
			_map.put(key, _map.get(key) + 1);
		}
	}
	
	public int getCount(K key) {
		Integer count = _map.get(key);
		return (count == null) ? 0 : count;
	}
	
	public int getTotalCount() {
		return _totalCount;
	}
	
	public boolean rejectKey(K key) {
		return Utils.RANDOM.nextDouble() * _totalCount > getCount(key);
	}
}
