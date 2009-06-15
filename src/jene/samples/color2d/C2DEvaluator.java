package jene.samples.color2d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import jene.Evaluator;
import jene.EvaluatorResults;
import jene.Organism;
import jene.Pixel;
import jene.EvaluatorResults.Couple;


/**
 * @author Andrew Loomis
 * @author James Stout
 *
 */
 
 public class C2DEvaluator implements Evaluator<Pixel, double[]> {

	private static final int IMAGE_SPACING = 25;
	
	private int _imgWidth, _imgHeight;
	private int _rows, _cols;
	
	private JFrame _frame;
	private JPanel _mainPanel;
	private JPanel _phenotypePanel;
	private JLabel _infoPane;
	private JButton _nextGenButton;
	private GridLayout _gridLayout;
	
	private int _numColors;
	
	private List<Organism<Pixel, double[]>> _singles;
	private List<Couple<Organism<Pixel, double[]>>> _couples;
	private BW2DExpressionTreeImage _match;
	private int _numCouples;
	
	private boolean _ready;
	
	public C2DEvaluator(int rows, int cols, int numToChoose, int numcolors) {

		 _singles = new ArrayList<Organism<Pixel,double[]>>();
		 _couples = new ArrayList<Couple<Organism<Pixel,double[]>>>();
		 _match = null;
		 _numCouples = 0;
		
		_numColors = numcolors;
		_imgWidth = 256;
		_imgHeight = 256;
		
		_rows = rows;
		_cols = cols;
		
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	private void createAndShowGUI() {
		_frame = new JFrame("Jene - 2D Images");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//_frame.setPreferredSize(new Dimension(600, 600));
		
		_mainPanel = new JPanel();
		_frame.setContentPane(_mainPanel);
		
		_phenotypePanel = new JPanel();
		_phenotypePanel.setBackground(Color.black);
		
		_gridLayout = new GridLayout(_rows, _cols);
		_gridLayout.setHgap(IMAGE_SPACING);
		_gridLayout.setVgap(IMAGE_SPACING);
		_phenotypePanel.setLayout(_gridLayout);
		
		
		_infoPane = new JLabel("Genotype");
//		_infoPane = new JTextPane();
//		_infoPane.setEditable(false);
//		_infoPane.setBackground(Color.black);
//		MutableAttributeSet aSet = new SimpleAttributeSet();
//		StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_CENTER);
//		StyleConstants.setForeground(aSet, Color.white);
//		_infoPane.setParagraphAttributes(aSet, true);
//		_infoPane.setText("Genotype");
		
		_frame.setLayout(new BorderLayout());
		_nextGenButton = new NextGenerationButton();
		_frame.add(_nextGenButton, BorderLayout.SOUTH);
		_frame.add(_phenotypePanel, BorderLayout.CENTER);
		_frame.add(_infoPane, BorderLayout.NORTH);
		
		_frame.pack();
		_frame.setVisible(true);

	}

	/* Determines the fitness of each expression tree in the current 
	 * population.  The fitness is calculated either by user input 
	 * or by some predefined heuristic.
	 */
	public <E extends Organism<Pixel, double[]>> EvaluatorResults<E> evaluate(List<E> trees) {
		
		System.out.println("Evaluate");
		
		_singles.clear();
		_couples.clear();
		// TODO lock
		_ready = false;
		//_nextGenButton.setEnabled(false);
		_phenotypePanel.removeAll();
		
		final BW2DExpressionTreeImage[] treeImages = new BW2DExpressionTreeImage[trees.size()];
		Thread[] threadArray = new Thread[trees.size()];
		
		int i = 0;

		for(final Organism<Pixel, double[]> tree: trees) {
			
			final int threadID = i;
			Runnable orgRunnable = new Runnable() {
				public void run() {
							
					synchronized(treeImages) {
						treeImages[threadID] = new BW2DExpressionTreeImage(tree);
					}
					_infoPane.setText("Rendered Image " + threadID + "...");
				}
			};
			
			threadArray[i] = new Thread(orgRunnable);
			threadArray[i].start();
			
			i++;
		}
		
		for(int threadIndex = 0; threadIndex<threadArray.length; threadIndex++) {
			try {
				threadArray[threadIndex].join();
				_phenotypePanel.add(treeImages[threadIndex]);		

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		_phenotypePanel.revalidate();
		
		_frame.pack();

		// wait for user to choose
		synchronized (this) {
			while (!_ready) {
				try {
					wait();
				} catch (InterruptedException ex) {
					return null;
				}
			}
		}
		
		
		System.out.println("Evaluation complete.");
		
		// construct results
		@SuppressWarnings("unchecked")
		EvaluatorResults<E> results = 
			new EvaluatorResults<E>(new ArrayList<E>((List<E>)_singles), 
									new ArrayList<Couple<E>>((List<Couple<E>>)(List)_couples)); 
		return results;
	}
	
	public class BW2DExpressionTreeImage extends JPanel implements MouseListener {
		
		private static final long serialVersionUID = 2191911781727142895L;

		private Organism<Pixel, double[]> _expressionTree;
		private String _genotype;
		private BufferedImage _phenotype;
		private int _coupleNum;
		private boolean _single, _couple;
		
		public BW2DExpressionTreeImage(Organism<Pixel, double[]> expressionTree) {
			this.setBackground(Color.black);
			
			_coupleNum = 0;
			_single = _couple = false;
			
			_expressionTree = expressionTree;
			_genotype = _expressionTree.nodes().toString();
			_phenotype = ImageGenerator.generate(_imgWidth, _imgHeight, _expressionTree, _numColors);
			
			this.addMouseListener(this);
			this.setMinimumSize(new Dimension(_imgWidth, _imgHeight));
			this.setPreferredSize(new Dimension(_imgWidth, _imgHeight));
		}

		public void setCoupleNum(int num) {
			_coupleNum = num;
		}
		
		public void setSingle(boolean single) {
			_single = single;
		}
		
		public void setCouple(boolean couple) {
			_couple = couple;
		}
		
		public boolean isSingle() {
			return _single;
		}
		
		public boolean isCouple() {
			return _couple;
		}
		
		public int getCoupleNum() {
			return _coupleNum;
		}
		
		public Organism<Pixel, double[]> getOrganism() {
			return _expressionTree;
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int x = (this.getWidth()-_phenotype.getWidth())/2;
			int y = (this.getHeight()-_phenotype.getHeight())/2;
			g.drawImage(_phenotype, x, y, null);
		}

		public void mouseClicked(MouseEvent e) {
			//couple
			if(e.isShiftDown()) {
				//remove couple
				if(_couple) {
					for(Couple<Organism<Pixel, double[]>> couple: _couples) {
						if(couple.org1().equals(_expressionTree) ||
						   couple.org2().equals(_expressionTree)) {
							_couples.remove(couple);
							
							for(Component comp: _phenotypePanel.getComponents()) {
								BW2DExpressionTreeImage orgPanel = (BW2DExpressionTreeImage)comp;
								if(orgPanel.getOrganism().equals(couple.org1()) ||
								   orgPanel.getOrganism().equals(couple.org2())) {
									if(orgPanel.isSingle()) {
										orgPanel.setBorder(BorderFactory.createLineBorder(Color.white, 5));
									}
									else {
										orgPanel.setBorder(BorderFactory.createEmptyBorder());
									}
									orgPanel.setCouple(false);
								}
							}
							break;
						}
					}
				}
				//add couple
				else {
					if(_match == null) {
						_match = this;
						
						TitledBorder border;
						if(this.isSingle()) {
							border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 5), "Select Mate");
							border.setTitleColor(Color.white);
						}
						else {
							border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 5), "Select Mate");
							border.setTitleColor(Color.gray);
						}
						
						this.setBorder(border);
						
					}
					else if (_match != this){
						_numCouples++;
						this.setCoupleNum(_numCouples);
						_match.setCoupleNum(_numCouples);
						
						TitledBorder border1, border2;
						if(this.isSingle()) {
							border1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 5), "Couple " + Integer.toString(this.getCoupleNum()));
							border1.setTitleColor(Color.white);
						}
						else {
							border1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 5), "Couple " + Integer.toString(this.getCoupleNum()));
							border1.setTitleColor(Color.gray);
						}
						if(_match.isSingle()) {
							border2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 5), "Couple " + Integer.toString(_match.getCoupleNum()));
							border2.setTitleColor(Color.white);
						}
						else {
							border2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 5), "Couple " + Integer.toString(_match.getCoupleNum()));
							border2.setTitleColor(Color.gray);
						}
						
						this.setBorder(border1);
						_match.setBorder(border2);
						
						this.setCouple(true);
						_match.setCouple(true);
						
						_couples.add(new Couple<Organism<Pixel, double[]>>(_expressionTree, 
								_match.getOrganism()));
						_match = null;
					}
				}
			}
			//single
			else {
				//remove single
				if(_single) {
					_singles.remove(_expressionTree);
					_single = false;
					
					Border border;
					if(_couple) {
						border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 5), "Couple " + Integer.toString(this.getCoupleNum()));
						((TitledBorder)border).setTitleColor(Color.gray);
					}
					else if(_match == this) {
						border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 5), "Select Mate");
						((TitledBorder)border).setTitleColor(Color.gray);
					}
					else {
						border = BorderFactory.createEmptyBorder();
					}

					this.setBorder(border);
				}
				//add single
				else {
					_singles.add(_expressionTree);
					_single = true;
					
					Border border;
					if(_couple) {
						border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 5), "Couple " + Integer.toString(this.getCoupleNum()));
						((TitledBorder)border).setTitleColor(Color.white);
					}
					else if (_match == this) {
						border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 5), "Select Mate");
						((TitledBorder)border).setTitleColor(Color.white);
					}
					else {
						border = BorderFactory.createLineBorder(Color.white, 5);
					}
					this.setBorder(border);
				}
			}
			
			
			/*if (_chosenTrees.contains(_expressionTree)) {
				_chosenTrees.remove(_expressionTree);
				setBorder(BorderFactory.createEmptyBorder());
				_nextGenButton.setEnabled(false);
			} else if (_chosenTrees.size() < _numToChoose) {
				_chosenTrees.add(_expressionTree);
				setBorder(BorderFactory.createLineBorder(Color.blue, 5));
				if (_chosenTrees.size() == _numToChoose) {
					_nextGenButton.setEnabled(true);
				}
			}*/
		}

		public void mouseEntered(MouseEvent e) {
			_infoPane.setText(_genotype);
		}

		public void mouseExited(MouseEvent e) {	
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
	}
	
	public class NextGenerationButton extends JButton implements ActionListener {
		private static final long serialVersionUID = -3336135227489753281L;

		public NextGenerationButton() {
			super("Next Generation");
			this.addActionListener(this);
			//setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent e) {
			//if(_chosenTrees.size() == _numToChoose) {
				_ready = true;
				synchronized(C2DEvaluator.this) {
					C2DEvaluator.this.notifyAll();
				}
				System.out.println("Next Generation");
			//}
			//else {
			//	System.out.println("Must Choose 2 Images");
			//}
		}
	}
	
	public static class ImageGenerator {
		public static BufferedImage generate(int width, int height, Organism<Pixel, double[]> tree, int numcolors) {
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			double[] minVal = new double[numcolors];
			double[] maxVal =  new double[numcolors];
			
			for(int i = 0; i<numcolors; i++){
				minVal[i] = Double.MAX_VALUE;
				maxVal[i] = Double.MIN_VALUE;
			}
			
			
			for(int i = 0; i < width; ++i) {
				for(int j = 0; j < height; ++j) {
					double[] val = tree.evaluate(new Pixel(2.0*i/(double)width - 1, 2.0*j/(double)height - 1));

					for(int color = 0; color<numcolors; color++){
						minVal[color] = Math.min(minVal[color], val[color]);
						maxVal[color] = Math.max(maxVal[color], val[color]);
					}
				}
			}
			
			
			double[] valRange = new double[numcolors];
			for(int color = 0; color<numcolors; color++){
				valRange[color] = maxVal[color]-minVal[color];
			}
			
			for(int i = 0; i < width; ++i) {
				for(int j = 0; j < height; ++j) {
					double[] val = tree.evaluate(new Pixel(2.0*i/(double)width - 1, 2.0*j/(double)height - 1));
					int[] readjustedColor = new int[numcolors];

					for(int color = 0; color<numcolors; color++){
						if (numcolors == 1) {
							// full contrast
							readjustedColor[color]= (int)(255*(val[color]-minVal[color])/valRange[color]);
						} else {
							// reduced contrast
							readjustedColor[color]= (int)(191*(val[color]-minVal[color])/valRange[color] + 32);
						}
						if(readjustedColor[color] < 0 || readjustedColor[color] > 255) {
							System.out.println("BW out of bounds: " + readjustedColor);
							readjustedColor[color] = Math.max(Math.min(readjustedColor[color], 255), 0);
						}
					}
					
//					readjustedColor = hsv2rgb(readjustedColor);
//					int brightness = gray[0];
//					int brightness = ( gray[0] + gray[1] + gray[2])/3;
					if (numcolors == 1) {
						image.setRGB(i, j, readjustedColor[0]^(readjustedColor[0]<<8)^(readjustedColor[0]<<16)^0xFF000000);
					} else {
						image.setRGB(i, j, readjustedColor[0]^(readjustedColor[1]<<8)^(readjustedColor[2]<<16)^0xFF000000);
					}
//					image.setRGB(i, j, brightness^(brightness<<8)^(brightness<<16)^0xFF000000);

				}
			}
			return image;
		}
	}
	
	public static int[] hsv2rgb(int[] hsv) {
		float h = hsv[0] / 256.0f * 6.0f;
		float s = hsv[1] / 256.0f * 1.0f;
		float v = hsv[2] / 256.0f * 1.0f;
		
		float[] frgb = HSVtoRGB(h, s, v);
		int[] irgb = new int[3];
		for (int i=0; i<3; i++) {
			irgb[i] = (int) (frgb[i] * 256.0f);
		}
		return irgb;
	}
	
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

}
