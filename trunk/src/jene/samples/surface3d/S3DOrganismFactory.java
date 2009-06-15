package jene.samples.surface3d;

import java.util.Arrays;

import javax.vecmath.Point3d;

import jene.FactoryManager;
import jene.Organism;
import jene.OrganismFactory;
import jene.Pixel;
import jene.Utils;


/**
 *  
 * @author James Stout
 *
 */
 
 public class S3DOrganismFactory implements OrganismFactory<UV, Point3d> {

	public static S3DOrganismFactory INSTANCE = new S3DOrganismFactory();
	private S3DOrganismFactory() {}
	
	@SuppressWarnings("unchecked")
	public Organism<UV, Point3d> newOrganism(FactoryManager manager) {
		double[] color = new double[] {Utils.RANDOM.nextDouble(), Utils.RANDOM.nextDouble() * .5 + .5, S3DOrganism.VALUE};
		return new S3DOrganism(Arrays.asList(Utils.createSubtree(manager, Pixel.class, Double.class, 0),
				Utils.createSubtree(manager, Pixel.class, Double.class, 0),
				Utils.createSubtree(manager, Pixel.class, Double.class, 0)), color);
	}

}
