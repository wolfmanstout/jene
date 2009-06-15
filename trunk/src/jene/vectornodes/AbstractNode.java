package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class AbstractNode implements Node<Pixel, double[]> {

	protected int _rank;
	
	protected AbstractNode() {
	}

	protected AbstractNode(List<Node<Pixel, ?>> children) {
		_rank = 0;
		if (!children.isEmpty()) {
			for (Node<Pixel, ?> child : children) {
				_rank = Math.max(_rank, child.rank());
			}
			_rank++;
		}
		
		// TODO validation doesn't work because factory is not yet set
/*		// validate types of children
		if (children.size() != childrenTypes().size()) {
			throw new IllegalArgumentException("Incorrect number of children");
		}

		Iterator<Class<?>> typeIter = childrenTypes().iterator();
		for (Node<Pixel, ?> child : children) {
			if (child.returnType() != typeIter.next()) {
				throw new IllegalArgumentException("Child has wrong type");
			}
		}*/
	}
	
	/**
	 * Always returns false.  Override if the node supports shuffling.  
	 */
	public boolean canShuffle() {
		return false;
	}
	
	/**
	 * Returns the original node.  Override if the node supports shuffling.  
	 */
	public Node<Pixel, double[]> shuffle() {
		return this;
	}
	
	/**
	 * Simply creates a new node using the factory. Override if the node supports shuffling.  
	 */
	public Node<Pixel, double[]> copyWithChildren(List<Node<Pixel, ?>> children) {
		return getFactory().newInstance(children);
	}
	
	/**
	 * Does nothing.  Override if the node supports shuffling.  
	 */
	public void record() {
		// does nothing
	}
	
	public int rank() {
		return _rank;
	}
	
	public Class<Pixel> inputType() {
		return getFactory().inputType();
	}

	public Class<double[]> returnType() {
		return getFactory().returnType();

	}

	public List<Class<?>> childrenTypes() {
		return getFactory().childrenTypes();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(nodeName());
		for (Node<Pixel, ?> child : children()) {
			sb.append(" ");
			sb.append(child);
		}
		sb.append(")");
		return sb.toString();
	}

	protected abstract String nodeName();

}
