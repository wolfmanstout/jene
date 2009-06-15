package jene.samples.surface3d;

import javax.vecmath.Point3d;

import jene.FactoryManager;
import jene.Organism;
import jene.OrganismFactory;
import jene.Pixel;
import jene.Utils;
import jene.Vector3;


/**
 *  
 * @author James Stout
 *
 */
 
 public class S3DAltOrganismFactory implements OrganismFactory<UV, Point3d> {

	public static S3DAltOrganismFactory INSTANCE = new S3DAltOrganismFactory();
	private S3DAltOrganismFactory() {}
	
	public Organism<UV, Point3d> newOrganism(FactoryManager manager) {
		return new S3DAltOrganism(Utils.createSubtree(manager, Pixel.class, Vector3.class, 0));
	}

}
