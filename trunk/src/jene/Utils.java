package jene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  
 * @author James Stout
 *
 */
 
 public class Utils {
	
	public static Random RANDOM = new SeededRandom();

	public static void bw2DSeed(){
	RANDOM = new SeededRandom(2193251949344314761l);
}

	public static void c2DSeed(){
		
//	RANDOM = new SeededRandom(-2815489074454869111l);
	RANDOM = new SeededRandom(3087108671977889168l);


}	
	
	public static  void surfaceSeed(){		
	RANDOM = new SeededRandom(-5515710731324423241l);
}	
	
	
	



	
	public static int countNodes(Node<?,?> root) {
		int count = 0;
		for (Node<?,?> child : root.children()) {
			count += countNodes(child);
		}
		return 1 + count;
	}
	
	public static int getMaxDepth(Node<?,?> root) {
		if (root.children().size() == 0) {
			return 0;
		} else {
			int maxChildDepth = 0;
			for (Node<?,?> child : root.children()) {
				int childDepth = getMaxDepth(child);
				if (childDepth > maxChildDepth) {
					maxChildDepth = childDepth;
				}
			}
			return 1 + maxChildDepth;
		}
	}
	
	public static <I,O> Node<I,O> createSubtree(FactoryManager manager, Class<I> inputType, Class<O> outputType, int depth) {
		
		// get a random factory using rejection sampling
		NodeFactory<I, O> factory;
		do {
			factory = manager.factoryWithType(inputType, outputType);
		} while (rejectSubtree(factory.childrenTypes().size(), 0, manager.getMaxChildren(), depth));
		
		// construct children
		List<Node<I, ?>> children = new ArrayList<Node<I,?>>();
		for (Class<?> childType : factory.childrenTypes()) {
			children.add(createSubtree(manager, inputType, childType, depth+1));
		}
		
		// construct subtree
		return factory.newInstance(children);
	}
	
	public static boolean rejectSubtree(int numberOfChildren, int minChildren, int maxChildren, int depth) {
		// TODO adjust probability
		double sharpness = .7;
		double criticalDepth = 2.5;
		double maxProbability = (depth < criticalDepth) ? Math.exp(sharpness * maxChildren * (criticalDepth - depth))
								: Math.exp(sharpness * minChildren * (criticalDepth - depth));
		return Utils.RANDOM.nextDouble() * maxProbability
				> Math.exp(sharpness * numberOfChildren * (criticalDepth - depth));
	}
	
	public static double[] listToArray(List<Double> list){
		double[] output = new double[list.size()];
		for(int i = 0; i<output.length; i++){
			output[i] = list.get(i);			
		}
		return output;
		
	}
	
		
		
}
	

