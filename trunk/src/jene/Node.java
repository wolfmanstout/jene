package jene;

import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public interface Node<I,O> {
	
	O evaluate(I input);
	List<Node<I,?>> children();
	
	NodeFactory<I,O> getFactory();
	
	/** 
	 * Constructs a copy of the node with a different set of children.  Uses the existing coefficients of the node.  
	 * 
	 * @param children should correspond to childrenTypes()
	 * @return the new node with children attached
	 */
	Node<I,O> copyWithChildren(List<Node<I,?>> children);
	
	/**
	 * Creates a new node that is a copy of this node, with drifted coefficients.  Returns the original node if canShuffle() is false.  
	 * 
	 * @return the drifted node
	 */
	Node<I,O> shuffle();
	
	/**
	 * @return true if the node has coefficients to shuffle
	 */
	boolean canShuffle();

	// Convenience methods
	int rank();
	/**
	 * Tells this node's factory that it has been selected.  The factory may use this information later when creating new instances.   
	 */
	void record();
	Class<I> inputType();
	Class<O> returnType();
	List<Class<?>> childrenTypes();
}
