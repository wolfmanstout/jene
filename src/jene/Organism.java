package jene;

import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public interface Organism<I,O> {
	
	O evaluate(I input);
	
	Organism<I,O> mutateAsexually(Mutator mutator);
	Organism<I, O> mutateSexually(Organism<I,O> mate, Mutator mutator);
	
	List<? extends Node<?,?>> nodes();
	
	List<Mutation> mutations();
	
}
