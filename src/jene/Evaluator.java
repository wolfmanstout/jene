package jene;

import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public interface Evaluator<I,O> {
	<E extends Organism<I,O>> EvaluatorResults<E> evaluate(List<E> trees);
}
