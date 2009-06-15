package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class BlendYNode extends Node2Arg<double[], double[]> {

	protected BlendYNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public BlendYNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2) {
		super(arg1, arg2);
	}

	public double[] evaluate(Pixel input) {
		double blendCoef = (input.y() + 1.0) / 2.0;
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = (blendCoef * a[i] + (1-blendCoef) * b[i]);
		}
		return a;
	}

	public BlendYNodeFactory getFactory() {
		return BlendYNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "blendy";
	}

}
