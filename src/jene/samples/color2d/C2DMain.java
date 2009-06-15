package jene.samples.color2d;

import jene.BasicMutation;
import jene.Evaluator;
import jene.God;
import jene.Mutation;
import jene.Pixel;
import jene.vectornodes.AbsNodeFactory;
import jene.vectornodes.AddNodeFactory;
import jene.vectornodes.AndNodeFactory;
import jene.vectornodes.ConstantNodeFactory;
import jene.vectornodes.HsvToRgbNodeFactory;
import jene.vectornodes.MaxNodeFactory;
import jene.vectornodes.MinNodeFactory;
import jene.vectornodes.MultiplyNodeFactory;
import jene.vectornodes.NoiseNodeFactory;
import jene.vectornodes.ToonNodeFactory;
import jene.vectornodes.WrappedVector3Factory;
import jene.vectornodes.XNodeFactory;
import jene.vectornodes.XORNodeFactory;
import jene.vectornodes.YNodeFactory;

/**
 *  
 * @author James Stout
 *
 */
 
 public class C2DMain {

	private static final int NUM_COLUMNS = 4;
	private static final int NUM_ROWS = 2;
	private static final int POPULATION_SIZE = NUM_COLUMNS * NUM_ROWS;
	private static final int NUM_SURVIVORS = 4;
	
	public static void main(String[] args) {
		
		int numColors = 3;
		Evaluator<Pixel, double[]> evaluator = new C2DEvaluator(NUM_ROWS, NUM_COLUMNS, POPULATION_SIZE - NUM_SURVIVORS, numColors);
		God<Pixel, double[]> god = new God<Pixel, double[]>(POPULATION_SIZE, evaluator, C2DOrganismFactory.INSTANCE);

		
		god.addFactory(new WrappedVector3Factory<Pixel>(new XNodeFactory(numColors)));
		god.addFactory(new WrappedVector3Factory<Pixel>(new YNodeFactory(numColors)));
		god.addFactory(new WrappedVector3Factory<Pixel>(new ConstantNodeFactory(numColors)));

		god.addFactory(new WrappedVector3Factory<Pixel>(MaxNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(MinNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(MultiplyNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(AddNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(AndNodeFactory.INSTANCE), 0.5);
		god.addFactory(new WrappedVector3Factory<Pixel>(XORNodeFactory.INSTANCE), 0.5);
		god.addFactory(new WrappedVector3Factory<Pixel>(ToonNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(AbsNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(NoiseNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(HsvToRgbNodeFactory.INSTANCE));
		
		for (Mutation mut : BasicMutation.values()) {
			god.addMutation(mut);
		}
		
		god.run();
		
	}
	
}
