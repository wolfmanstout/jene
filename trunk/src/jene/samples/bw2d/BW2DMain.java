package jene.samples.bw2d;

import jene.BasicMutation;
import jene.Evaluator;
import jene.God;
import jene.Mutation;
import jene.Pixel;
import jene.samples.color2d.C2DEvaluator;
import jene.vectornodes.AddNodeFactory;
import jene.vectornodes.AndNodeFactory;
import jene.vectornodes.ConstantNodeFactory;
import jene.vectornodes.MultiplyNodeFactory;
import jene.vectornodes.NoiseNodeFactory;
import jene.vectornodes.SineNodeFactory;
import jene.vectornodes.WrappedDoubleFactory;
import jene.vectornodes.XNodeFactory;
import jene.vectornodes.XORNodeFactory;
import jene.vectornodes.YNodeFactory;

/**
 *  
 * @author James Stout
 *
 */
 
 public class BW2DMain {

	private static final int NUM_COLUMNS = 4;
	private static final int NUM_ROWS = 2;
	private static final int POPULATION_SIZE = NUM_COLUMNS * NUM_ROWS;
	private static final int NUM_SURVIVORS = 4;
	
	public static void main(String[] args) {
		
		
//		Utils.su
		int numColors = 1;
		Evaluator<Pixel, double[]> evaluator = new C2DEvaluator(NUM_ROWS, NUM_COLUMNS, POPULATION_SIZE - NUM_SURVIVORS, numColors);
		God<Pixel, double[]> god = new God<Pixel, double[]>(POPULATION_SIZE, evaluator, BW2DOrganismFactory.INSTANCE);
		
		god.addFactory(new WrappedDoubleFactory<Pixel>(MultiplyNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(AddNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(new XNodeFactory(numColors)));
		god.addFactory(new WrappedDoubleFactory<Pixel>(new YNodeFactory(numColors)));
		god.addFactory(new WrappedDoubleFactory<Pixel>(AndNodeFactory.INSTANCE), 0.5);
		god.addFactory(new WrappedDoubleFactory<Pixel>(NoiseNodeFactory.INSTANCE), 0.5);
		god.addFactory(new WrappedDoubleFactory<Pixel>(SineNodeFactory.INSTANCE), 0.5);
		god.addFactory(new WrappedDoubleFactory<Pixel>(new ConstantNodeFactory(numColors)));
		god.addFactory(new WrappedDoubleFactory<Pixel>(XORNodeFactory.INSTANCE), 0.5);

		
		for (Mutation mut : BasicMutation.values()) {
			god.addMutation(mut);
		}
		
		god.run();
		
	}
	
}
