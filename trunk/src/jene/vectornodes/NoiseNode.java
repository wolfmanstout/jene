package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class NoiseNode extends Node2Arg<double[], double[]> {

	private double _z;
	
	protected NoiseNode(List<Node<Pixel, ?>> children, double z) {
		super(children);
		_z = z;
	}

	public NoiseNode(Node<Pixel, double[]> arg1, Node<Pixel, double[]> arg2, double z) {
		super(arg1, arg2);
		_z = z;
	}

	public double[] evaluate(Pixel input) {
		double freqMod = 4.0;
		double[] a = _arg1.evaluate(input);
		double[] b = _arg2.evaluate(input);
		for(int i = 0; i<a.length; i++){
			a[i] = PerlinNoise.noise((a[i]+1.0)/2.0 * freqMod, (b[i]+1.0)/2.0 * freqMod, _z);
		}
		return a;
	}

	public NoiseNodeFactory getFactory() {
		return NoiseNodeFactory.INSTANCE;
	}
	
	@Override
	public Node<Pixel, double[]> copyWithChildren(List<Node<Pixel, ?>> children) {
		return new NoiseNode(children, _z);
	}

	@Override
	protected String nodeName() {
		return "noise";
	}

}
