package jene;

import java.util.List;


/**
 *  
 * @author James Stout
 *
 */
 
 public interface NodeFactory<I, O> {
	
	/**
	 * Creates a fresh instance of the desired node with a specified set of children.  
	 * Uses a random set of coefficients corresponding to its histogram.  
	 * 
	 * @param children should correspond to childrenTypes
	 * @return the node with children attached
	 */
	Node<I,O> newInstance(List<Node<I,?>> children);
	
	Class<I> inputType();
	Class<O> returnType();
	List<Class<?>> childrenTypes();

}
