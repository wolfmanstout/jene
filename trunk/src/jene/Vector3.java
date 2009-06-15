package jene;

/**
 *  
 * @author James Stout
 *
 */
 
 public class Vector3 {
	
	private double[] _data;
	
	public Vector3(double v0, double v1, double v2) {
		_data = new double[3];
		_data[0] = v0;
		_data[1] = v1;
		_data[2] = v2;
	}
	
	public double[] data() {
		return _data;
	}
}
