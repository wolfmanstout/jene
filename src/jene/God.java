package jene;

import java.util.ArrayList;
import java.util.List;

import jene.EvaluatorResults.Couple;


/**
 *  
 * @author James Stout
 *
 */
 
 public class God<I,O> {
	
	private List<Organism<I,O>> _population;
	private int _populationSize;
	private final Evaluator<I,O> _evaluator;
	private final OrganismFactory<I,O> _orgFactory;
	
	private final FactoryManager _manager;
	private final Mutator _mutator;
	
	
	public God(int populationSize, Evaluator<I,O> evaluator, OrganismFactory<I,O> orgFactory) {
		_population = new ArrayList<Organism<I,O>>();
		_populationSize = populationSize;
		_evaluator = evaluator;
		_orgFactory = orgFactory;
		
		_manager = new FactoryManager();
		_mutator = new Mutator(_manager);
	}
	
	public void addFactory(NodeFactory<?,?> factory) {
		_manager.registerFactory(factory);
	}
	
	public void addFactory(NodeFactory<?,?> factory, double initialWeight) {
		_manager.registerFactory(factory, initialWeight);
	}
	
	public void addMutation(Mutation mutation) {
		_mutator.addMutation(mutation);
	}
	
	private void recordOrganism(Organism<?,?> org) {
		for (Node<?,?> node : org.nodes()) {
			_manager.recordNodes(node);
		}
		_mutator.recordMutations(org.mutations());
	}
	
	public void run() {
		// Spawn initial trees
		for (int i=0; i<_populationSize; i++) {
			_population.add(_orgFactory.newOrganism(_manager));
		}
		
		while (true) {
			// evaluate current generation
			System.out.println("Evaluating population");
			EvaluatorResults<Organism<I,O>> results = _evaluator.evaluate(_population);
			
			// select survivors
			System.out.println("Selecting survivors");
			List<Organism<I,O>> survivors = new ArrayList<Organism<I,O>>();
			survivors.addAll(results.asexual());
			for (Couple<Organism<I,O>> couple : results.sexual()) {
				if (!survivors.contains(couple.org1())) survivors.add(couple.org1());
				if (!survivors.contains(couple.org2())) survivors.add(couple.org2());
			}
			for (Organism<I,O> org : survivors) {
				recordOrganism(org);
			}
			
			// add survivors unchanged
			_population.clear();
			for (Organism<I,O> org : survivors) {
				_population.add(org);
			}
			
			// do asexual mutations
			for (Organism<I,O> org : results.asexual()) {
				System.out.println("Mutating asexually ...");
				if (_population.size() < _populationSize) {
					_population.add(org.mutateAsexually(_mutator));
				}
			}
			
			// do sexual mutations
			for (Couple<Organism<I,O>> couple : results.sexual()) {
				System.out.println("Mutating sexually ...");
				if (_population.size() < _populationSize) {
					_population.add(couple.org1().mutateSexually(couple.org2(), _mutator));
				}
				if (_population.size() < _populationSize) {
					_population.add(couple.org2().mutateSexually(couple.org1(), _mutator));
				}
			}
			
			while (_population.size() < _populationSize) {
				_population.add(_orgFactory.newOrganism(_manager));
			}
			
			/*while (_population.size() < _populationSize) {
				int idx = 0;
//				int idx = Utils.RANDOM.nextInt(remainingSingles.size() + remainingCouples.size());
				if (idx < remainingSingles.size()) {
					// asexually mutate
					System.out.println("Mutating asexually ...");
					Organism<I,O> org = remainingSingles.get(idx);
					remainingSingles.remove(idx);
					_population.add(org.mutateAsexually(_mutator));
				} else {
					// sexually mutate
					System.out.println("Mutating sexually ...");
					Couple<Organism<I, O>> couple = remainingCouples.get(idx - remainingSingles.size());
					remainingCouples.remove(idx - remainingSingles.size());
					_population.add(couple.org1().mutateSexually(couple.org2(), _mutator));
					if (_population.size() < _populationSize) {
						_population.add(couple.org2().mutateSexually(couple.org1(), _mutator));
					}
				}
				
				// if we are exhausted, reload lists
				if (remainingSingles.isEmpty() && remainingCouples.isEmpty()) {
					remainingSingles.addAll(results.asexual());
					remainingCouples.addAll(results.sexual());
				}
			}*/
		}
	}
	
}
