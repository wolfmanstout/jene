package jene;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 * @author James Stout
 *
 */
 
 public abstract class AbstractOrganism<I,O> implements Organism<I, O> {

	private List<Mutation> _mutations = new ArrayList<Mutation>();

	public List<Mutation> mutations() {
		return _mutations;
	}

}
