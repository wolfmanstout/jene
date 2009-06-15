package jene.samples.surface3d;

import javax.vecmath.Point3d;

import jene.BasicMutation;
import jene.God;
import jene.Mutation;
import jene.Pixel;
import jene.vectornodes.AddNodeFactory;
import jene.vectornodes.ConstantNodeFactory;
import jene.vectornodes.MultiplyNodeFactory;
import jene.vectornodes.SineNodeFactory;
import jene.vectornodes.WrappedVector3Factory;
import jene.vectornodes.XNodeFactory;
import jene.vectornodes.YNodeFactory;


/**
 *  
 * @author James Stout
 *
 */
 
 public class S3DAltMain {

	private static final int NUM_COLUMNS = 4;
	private static final int NUM_ROWS = 2;
	private static final int POPULATION_SIZE = NUM_COLUMNS * NUM_ROWS;
	
	public static void main(String[] args) {
		
		SurfaceEvaluator evaluator = new SurfaceEvaluator(NUM_COLUMNS, NUM_ROWS);
		God<UV, Point3d> god = new God<UV, Point3d>(POPULATION_SIZE, evaluator, S3DAltOrganismFactory.INSTANCE);
		
		god.addFactory(new WrappedVector3Factory<Pixel>(MultiplyNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(AddNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(new XNodeFactory(3)));
		god.addFactory(new WrappedVector3Factory<Pixel>(new YNodeFactory(3)));
//		god.addFactory(new WrappedVector3Factory<Pixel>(AndNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(SineNodeFactory.INSTANCE));
		god.addFactory(new WrappedVector3Factory<Pixel>(new ConstantNodeFactory(3)));
		
		for (Mutation mut : BasicMutation.values()) {
			god.addMutation(mut);
		}
		
		god.run();
		
	}
	
}
