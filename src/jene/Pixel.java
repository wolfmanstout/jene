package jene;

/**
 *  
 * @author James Stout
 *
 */
 
 public class Pixel {
	
	private final double _x;
	private final double _y;
	
	public Pixel(double x, double y) {
		_x = x;
		_y = y;
	}
	
	public double x() {
		return _x;
	}
	
	public double y() {
		return _y;
	}
}
