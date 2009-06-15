package jene.samples.bw2d;

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
 
 public class BW2DOrganismFactory implements OrganismFactory<Pixel, double[]> {

	public static BW2DOrganismFactory INSTANCE = new BW2DOrganismFactory();
	private BW2DOrganismFactory() {}
	
	public Organism<Pixel, double[]> newOrganism(FactoryManager manager) {
		return new BW2DOrganism(Utils.createSubtree(manager, Pixel.class, Double.class, 0));
	}

}
