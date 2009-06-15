package jene;

/**
 *  
 * @author James Stout
 *
 */
 
 public class NodeContext {

	private final int _depth;

	public NodeContext(int depth) {
		_depth = depth;
	}
	
	public int depth() {
		return _depth;
	}
}
