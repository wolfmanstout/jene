package jene.samples.surface3d;

import javax.vecmath.Point3d;

import jene.BasicMutation;
import jene.God;
import jene.Mutation;
import jene.Pixel;
import jene.vectornodes.AbsNodeFactory;
import jene.vectornodes.AddNodeFactory;
import jene.vectornodes.BlendXNodeFactory;
import jene.vectornodes.BlendYNodeFactory;
import jene.vectornodes.ConstantNodeFactory;
import jene.vectornodes.CubeNodeFactory;
import jene.vectornodes.MultiplyNodeFactory;
import jene.vectornodes.SineNodeFactory;
import jene.vectornodes.WrappedDoubleFactory;
import jene.vectornodes.XNodeFactory;
import jene.vectornodes.YNodeFactory;


/**
 *  
 * @author James Stout
 *
 */
 
 public class S3DMain {

	private static final int NUM_COLUMNS = 4;
	private static final int NUM_ROWS = 2;
	private static final int POPULATION_SIZE = NUM_COLUMNS * NUM_ROWS;
	
	public static void main(String[] args) {
		
		SurfaceEvaluator evaluator = new SurfaceEvaluator(NUM_COLUMNS, NUM_ROWS);
		God<UV, Point3d> god = new God<UV, Point3d>(POPULATION_SIZE, evaluator, S3DOrganismFactory.INSTANCE);
		
		god.addFactory(new WrappedDoubleFactory<Pixel>(MultiplyNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(AddNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(new XNodeFactory(1)));
		god.addFactory(new WrappedDoubleFactory<Pixel>(new YNodeFactory(1)));
//		god.addFactory(new WrappedDoubleFactory<Pixel>(AndNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(SineNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(new ConstantNodeFactory(1)));
		god.addFactory(new WrappedDoubleFactory<Pixel>(AbsNodeFactory.INSTANCE));
		god.addFactory(new WrappedDoubleFactory<Pixel>(BlendXNodeFactory.INSTANCE), .25);
		god.addFactory(new WrappedDoubleFactory<Pixel>(BlendYNodeFactory.INSTANCE), .25);
		god.addFactory(new WrappedDoubleFactory<Pixel>(CubeNodeFactory.INSTANCE));
		//god.addFactory(new WrappedDoubleFactory<Pixel>(ExpNodeFactory.INSTANCE));

		
		for (Mutation mut : BasicMutation.values()) {
			god.addMutation(mut);
		}
		
		god.run();
		
	}
	
}
