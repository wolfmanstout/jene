package jene.samples.color2d;

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
 
 public class C2DOrganismFactory implements OrganismFactory<Pixel, double[]> {

	public static C2DOrganismFactory INSTANCE = new C2DOrganismFactory();
	private C2DOrganismFactory() {}
	
	public Organism<Pixel, double[]> newOrganism(FactoryManager manager) {
		return new C2DOrganism(Utils.createSubtree(manager, Pixel.class, Vector3.class, 0));
	}

}
