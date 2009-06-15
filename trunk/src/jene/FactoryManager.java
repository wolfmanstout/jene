package jene;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *  
 * @author James Stout
 *
 */
 
 public class FactoryManager {
	
	private final Map<Class<?>, Map<Class<?>, List<NodeFactory<?,?>>>> _factoryMap = new HashMap<Class<?>, Map<Class<?>,List<NodeFactory<?,?>>>>();
	private int _maxChildren = 0;
	private double _maxWeight = 1.0;
	
	// counts instances of nodes to influence probability
	private Map<NodeFactory<?,?>, Double> _nodeWeights = new HashMap<NodeFactory<?,?>, Double>();
	private MapCounter<NodeFactory<?,?>> _nodeCounter = new MapCounter<NodeFactory<?,?>>();
	
	public <O> void registerFactory(NodeFactory<?, O> factory, double initialWeight) {
		// register factory with map
		if (!_factoryMap.containsKey(factory.inputType())) {
			_factoryMap.put(factory.inputType(), new HashMap<Class<?>, List<NodeFactory<?,?>>>());
		}
		Map<Class<?>, List<NodeFactory<?,?>>> outputMap = _factoryMap.get(factory.inputType());
		if (!outputMap.containsKey(factory.returnType())) {
			outputMap.put(factory.returnType(), new ArrayList<NodeFactory<?,?>>());
		}
		outputMap.get(factory.returnType()).add(factory);
		
		// add to weights map
		_nodeWeights.put(factory, initialWeight);
		
		// update useful info
		_maxWeight = Math.max(_maxWeight, initialWeight);
		_maxChildren = Math.max(_maxChildren, factory.childrenTypes().size());
	}
	
	public <O> void registerFactory(NodeFactory<?, O> factory) {
		registerFactory(factory, 1.0);
	}
	
	public int getMaxChildren() {
		return _maxChildren;
	}
	
	public void recordNodes(Node<?, ?> node) {
		_nodeCounter.incrementCount(node.getFactory());
		node.record();
		for (Node<?,?> child : node.children()) {
			recordNodes(child);
		}
	}
	
	
/*	public <O> NodeFactory<I,O> factoryWithSignature(Class<O> returnType, List<Class<?>> signature){
		//Doesn't work right now, is being worked on
		
		// choose a random factory based on weights
		List<NodeFactory<I,?>> factories = _factoryMap.get(returnType);		
		double totalWeight = 0;
		for (NodeFactory<I,?> factory : factories) {
			totalWeight += factory.getWeight();
		}
		
		double r = Utils.RANDOM.nextDouble() * totalWeight;
		double accumWeight = 0;
		for (NodeFactory<I,?> factory : factories) {
			accumWeight += factory.getWeight();
			if (r < accumWeight) {
				@SuppressWarnings("unchecked")
				NodeFactory<I,O> chosen = (NodeFactory<I, O>) factory;
				return chosen;
			}
		}
		
		// should not get here
		throw new AssertionError("No factory was chosen");			
	}*/

	@SuppressWarnings("unchecked")
	private <I,O> NodeFactory<I,O> weightedFactoryWithType(Class<I> inputType, Class<O> returnType) {
		// choose a random factory based on weights
		Map<Class<?>, List<NodeFactory<?,?>>> outputMap = _factoryMap.get(inputType);
		if (outputMap == null) {
			throw new IllegalArgumentException("No Factories of Type Found");
		}
		List<NodeFactory<?,?>> factories = outputMap.get(returnType);	
		if(factories == null){
			throw new IllegalArgumentException("No Factories of Type Found");
		}
		
		NodeFactory<I,O> factory;
		do {
			int fIdx = Utils.RANDOM.nextInt(factories.size());
			factory = (NodeFactory<I, O>) factories.get(fIdx);
		} while (Utils.RANDOM.nextDouble() * _maxWeight > _nodeWeights.get(factory));
		
		return factory;
	}
	
	public <I,O> NodeFactory<I,O> factoryWithType(Class<I> inputType, Class<O> returnType) {
		NodeFactory<I,O> factory;
		if (Utils.RANDOM.nextDouble() < .5) {
			factory = weightedFactoryWithType(inputType, returnType);
		} else {
			do {
				factory = weightedFactoryWithType(inputType, returnType);
				// TODO adjust probability
			} while (_nodeCounter.rejectKey(factory));
		}
		
		return factory;
	}
	
}
