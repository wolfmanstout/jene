package jene;

import jene.samples.bw2d.BW2DMain;
import jene.samples.color2d.C2DMain;
import jene.samples.surface3d.S3DMain;

/**
 *  
 * @author James Stout
 *
 */
 
 public class Main {

	public static void main(String[] args) {
		boolean seed = false;
		if (args.length >= 2 && args[1].equals("s")) {
			seed = true;
		}
		
		if (args.length >= 1 && args[0].equals("bw2d")) {
			if (seed) Utils.bw2DSeed();
			BW2DMain.main(new String[] {});
		} else if (args.length >= 1 && args[0].equals("c2d")) {
			if (seed) Utils.c2DSeed();
			C2DMain.main(new String[] {});
		} else {
			if (seed) Utils.surfaceSeed();
			S3DMain.main(new String[] {});
		}
	}
	
}
