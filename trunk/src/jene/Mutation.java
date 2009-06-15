package jene;

/**
 *  
 * @author James Stout
 *
 */
 
 public interface Mutation {
	
	<I, O> Node<I,O> mutate(Node<I,O> node, NodeContext context, FactoryManager manager);
	
}
