package jene.samples.surface3d;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import jene.AbstractOrganism;
import jene.Mutation;
import jene.Mutator;
import jene.Node;
import jene.Organism;
import jene.Pixel;
import jene.Utils;


/**
 *  
 * @author James Stout
 *
 */
 
 public class S3DOrganism extends AbstractOrganism<UV, Point3d> {

	static double VALUE = .7;
	static double COLOR_DRIFT = .1;
	
	private List<Node<Pixel,Double>> _roots;
	private double[] _color;
	
	S3DOrganism(List<Node<Pixel, Double>> roots, double[] color) {
		_roots = new ArrayList<Node<Pixel,Double>>(roots);
		_color = color;
	}
	
	public float[] color() {
		int[] color = RgbHsv.HSVtoRGB((float) _color[0],(float) _color[1], (float) _color[2]);
		return new float[] {color[0]/256.0f, color[1]/256.0f, color[2]/256.0f};
	}

	public Point3d evaluate(UV input) {
		return new Point3d(_roots.get(0).evaluate(new Pixel(input.u, input.v)),
				_roots.get(1).evaluate(new Pixel(input.u, input.v)),
				_roots.get(2).evaluate(new Pixel(input.u, input.v)));
	}

	public List<? extends Node<?, ?>> nodes() {
		return _roots;
	}

	public S3DOrganism mutateAsexually(Mutator mutator) {
		List<Mutation> mutations = new ArrayList<Mutation>();
		List<Node<Pixel,Double>> newRoots = new ArrayList<Node<Pixel,Double>>();
		for (Node<Pixel, Double> root : _roots) {
			newRoots.add(mutator.mutateNodeTree(root, mutations));
		}
		
		// drift color
		double[] newColor = new double[] {_color[0], _color[1], _color[2]};
		newColor[0] = newColor[0] +  COLOR_DRIFT * Utils.RANDOM.nextGaussian() + 1.;
		newColor[0] = newColor[0] - Math.floor(newColor[0]);
		
		//newColor [0] = Math.min(1.0, Math.max(0.0,newColor[0] +  COLOR_DRIFT * Utils.RANDOM.nextGaussian()));
		newColor[1] = Math.min(1.0, Math.max(0.5, newColor[1] +  COLOR_DRIFT * Utils.RANDOM.nextGaussian()));
		return new S3DOrganism(newRoots, newColor);
	}

	public S3DOrganism mutateSexually(
			Organism<UV, Point3d> mate, Mutator mutator) {
		S3DOrganism myMate = (S3DOrganism) mate;
		List<Node<Pixel, Double>> mateNodes = myMate._roots;
		List<Node<Pixel, Double>> newNodes = new ArrayList<Node<Pixel,Double>>();
		int i=0;
		for (Node<Pixel, Double> mateNode : mateNodes) {
			newNodes.add(mutator.mateNodeTrees(_roots.get(i++), mateNode));
		}
		
		int[] color1 = RgbHsv.HSVtoRGB((float) _color[0],(float) _color[1], (float) _color[2]);
		int[] color2 = RgbHsv.HSVtoRGB((float) myMate._color[0],(float) myMate._color[1], (float) myMate._color[2]);
		int[] newColor = new int[] {(color1[0]+color2[0])/2, (color1[1]+color2[1])/2, (color1[2]+color2[2])/2};
		float[] newHsvf = RgbHsv.RGBtoHSV(newColor[0], newColor[1], newColor[2], null);
		double[] newHsv = new double[] {newHsvf[0], newHsvf[1], VALUE};
		return new S3DOrganism(newNodes, newHsv);
	}
}