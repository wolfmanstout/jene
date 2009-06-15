package jene.vectornodes;

import java.util.List;

import jene.Node;
import jene.Pixel;


/**
 *  
 * @author James Stout
 *
 */
 
 public class HsvToRgbNode extends Node1Arg<double[]> {

	protected HsvToRgbNode(List<Node<Pixel, ?>> children) {
		super(children);
	}

	public HsvToRgbNode(Node<Pixel, double[]> arg1) {
		super(arg1);
	}

	public double[] evaluate(Pixel input) {
		double[] a = _arg1.evaluate(input);
		return hsv2rgb(a);
	}
	
	public static double[] hsv2rgb(double[] hsv) {
		float h = (float) ((hsv[0] + 1.0) / 2.0 * 6.0);
		float s = (float) ((hsv[1] + 1.0) / 2.0 * 1.0);
		float v = (float) ((hsv[2] + 1.0) / 2.0 * 1.0);
		
		float[] frgb = HSVtoRGB(h, s, v);
		double[] drgb = new double[3];
		for (int i=0; i<3; i++) {
			drgb[i] = frgb[i] * 2.0 - 1.0;
		}
		return drgb;
	}
	
	/**
	 * Thanks to David Flanagan for this code.  
	 */
	public static float[] HSVtoRGB(float h, float s, float v)
	{
		// H is given on [0->6] or -1. S and V are given on [0->1]. 
		// RGB are each returned on [0->1]. 
		float m, n, f;
		int i; 

		float[] hsv = new float[3];
		float[] rgb = new float[3];

		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;

		if(hsv[0] == -1) {rgb[0] = rgb[1] = rgb[2] = hsv[2];
							return rgb;  }
		i = (int)(Math.floor(hsv[0]));
		f = hsv[0] - i;
		if(i%2 == 0) f = 1 - f; // if i is even 
		m = hsv[2] * (1 - hsv[1]); 
		n = hsv[2] * (1 - hsv[1] * f); 
		switch (i) { 
		case 6: 
		case 0: rgb[0]=hsv[2]; rgb[1]=n; rgb[2]=m; break;
		case 1: rgb[0]=n; rgb[1]=hsv[2]; rgb[2]=m; break;
		case 2: rgb[0]=m; rgb[1]=hsv[2]; rgb[2]=n; break;
		case 3: rgb[0]=m; rgb[1]=n; rgb[2]=hsv[2]; break;
		case 4: rgb[0]=n; rgb[1]=m; rgb[2]=hsv[2]; break;
		case 5: rgb[0]=hsv[2]; rgb[1]=m; rgb[2]=n; break;
		} 

		return rgb;
		
	}

	public HsvToRgbNodeFactory getFactory() {
		return HsvToRgbNodeFactory.INSTANCE;
	}

	@Override
	protected String nodeName() {
		return "hsv2rgb";
	}

}
