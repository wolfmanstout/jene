package jene;

/**
 *  
 * @author James Stout
 *
 */
 
 public interface OrganismFactory<I,O> {

	/**
	 * Randomly generates a new organism
	 * 
	 * @param manager the manager used to create nodes
	 */
	Organism<I,O> newOrganism(FactoryManager manager);
	
	
}
