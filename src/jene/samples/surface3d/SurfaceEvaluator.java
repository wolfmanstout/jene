package jene.samples.surface3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import javax.vecmath.Point3d;

import jene.Evaluator;
import jene.EvaluatorResults;
import jene.Organism;
import jene.EvaluatorResults.Couple;


/**
 *  
 * @author Andrew Loomis
 *
 */
 
 public class SurfaceEvaluator implements Evaluator<UV, Point3d>{

	private final int SPACING = 25;
	
	private JFrame _appFrame;
	private JLabel _infoPane;
	private JPanel _phenotypePanel;
	private JButton _nextGenButton;
	
	private ArrayList<Organism<UV, Point3d>> _singles;
	private ArrayList<Couple<Organism<UV, Point3d>>> _couples;
	private int _numCouples;
	private OrganismPanel _match;
	
	private int _width;
	private int _height;
	
	public SurfaceEvaluator(int width, int height) {
		
		_width = width;
		_height = height;
		
		_singles = new ArrayList<Organism<UV, Point3d>>();
		_couples = new ArrayList<Couple<Organism<UV, Point3d>>>();
		_numCouples = 0;
		_match = null;
		
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
		_appFrame = new JFrame("Jene - 3D Surfaces");
		_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		_infoPane = new JLabel("Genotype");
//		_infoPane = new JTextPane();
//		_infoPane.setEditable(false);
//		_infoPane.setBackground(Color.black);
//		MutableAttributeSet aSet = new SimpleAttributeSet();
//		StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_CENTER);
//		StyleConstants.setForeground(aSet, Color.white);
//		_infoPane.setParagraphAttributes(aSet, true);
//		_infoPane.setText("Genotype");
		
		_phenotypePanel = new GradientPanel(Color.black, Color.black);
		
		GridLayout gridLayout = new GridLayout(_height, _width);
		gridLayout.setHgap(SPACING);
		gridLayout.setVgap(SPACING);
		_phenotypePanel.setLayout(gridLayout);
		_phenotypePanel.setBorder(BorderFactory.createEmptyBorder(SPACING,SPACING,SPACING,SPACING));
		
		_nextGenButton = new NextGenButton();
		_nextGenButton.setEnabled(false);
		
		_appFrame.setLayout(new BorderLayout());
		_appFrame.add(_infoPane, BorderLayout.NORTH);
		_appFrame.add(_phenotypePanel, BorderLayout.CENTER);
		_appFrame.add(_nextGenButton, BorderLayout.SOUTH);
		
		_appFrame.pack();
		_appFrame.setVisible(true);
	}
	
	public <E extends Organism<UV, Point3d>> EvaluatorResults<E> evaluate(List<E> organisms) {
		
		// Clear lists
		_singles.clear();
		_couples.clear();
		
		// Cleanup java 3d
		for(Component comp: _phenotypePanel.getComponents()) {
			OrganismPanel orgPanel = (OrganismPanel)comp;
			orgPanel.cleanup();
		}
		
		// Create surfaces from the surface organisms and add them to the panel
		_phenotypePanel.removeAll();
		_infoPane.setText("Evaluating Surfaces...");
		
		final OrganismPanel[] orgPanels = new OrganismPanel[organisms.size()];
		Thread[] threadArray = new Thread[organisms.size()];
		
		int i = 0;
		for(final Organism<UV, Point3d> organism: organisms) {
			
			
			final int threadID = i;
			Runnable orgRunnable = new Runnable() {
				public void run() {
					OrganismPanel orgPanel = new OrganismPanel(organism, 256, 256);
					synchronized(orgPanels) {
						orgPanels[threadID] = orgPanel; 
					}
				}
			};
			
			threadArray[i] = new Thread(orgRunnable);
			threadArray[i].start();
			
			i++;
		}
		
		for(Thread thread: threadArray) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(OrganismPanel orgPanel: orgPanels) {
			_phenotypePanel.add(orgPanel);
		}
		
		_phenotypePanel.revalidate();
		_appFrame.pack();
		
		_infoPane.setText("Select singles and pairs for next generation.");
		
		_nextGenButton.setEnabled(true);
		
		//Wait for user to choose the required number of phenotypes for the
		//next generation
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		_nextGenButton.setEnabled(false);
		
		// Construct results
		@SuppressWarnings("unchecked")
		EvaluatorResults<E> results = 
			new EvaluatorResults<E>(new ArrayList<E>((List<E>) _singles), 
				new ArrayList<Couple<E>>((List<Couple<E>>) (List) _couples)); 
		return results;
	}
	
	private class NextGenButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1L;

		public NextGenButton() {
			super("Next Generation");
			this.addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e) {
			synchronized (SurfaceEvaluator.this) {
				SurfaceEvaluator.this.notifyAll();
			}
			System.out.println("Next Generation");
		}
	}
	
	private class GradientPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private Color _color1, _color2;
		
		public GradientPanel(Color color1, Color color2) {
			_color1 = color1;
			_color2 = color2;
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			//Paint gradiant
			Graphics2D g2 =(Graphics2D)g;
			
			int w = this.getWidth();
			int h = this.getHeight();
			
			GradientPaint gp = new GradientPaint(0,0,_color1,0,h,_color2);
			
			g2.setPaint( gp );
			g2.fillRect( 0, 0, w, h );
		}
	}
	
	private class OrganismPanel extends JPanel implements MouseListener, 
														   WindowListener,
														   KeyListener,
														   KeyEventDispatcher {
		
		private static final long serialVersionUID = 1L;
		private JFrame _frame;
		private Organism<UV, Point3d> _organism;
		private SurfacePanel _surfacePanel;
		private BufferedImage _thumbnail;
		private int _coupleNum;
		private boolean _single, _couple;
		private KeyboardFocusManager _manager;
		
		public OrganismPanel(Organism<UV, Point3d> organism, int width, int height) {
			this.setPreferredSize(new Dimension(width, height));
			this.setOpaque(false);
			
			_coupleNum = 0;
			_single = _couple = false;
			
			_organism = organism;
			_surfacePanel = new SurfacePanel(_organism);
			_thumbnail = _surfacePanel.getSnapshot(width, height);
			
			_manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			_manager.addKeyEventDispatcher(this);
			
			_frame = new JFrame();
			_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			_frame.add(_surfacePanel);
			_frame.pack();
			_frame.setVisible(false);
			_frame.addWindowListener(this);
			
			this.addMouseListener(this);
			this.addKeyListener(this);
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
		
		public Organism<UV, Point3d> getOrganism() {
			return _organism;
		}
		
		public int getCoupleNum() {
			return _coupleNum;
		}
		
		public void cleanup() {
			_surfacePanel.cleanup();
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D)g;
			
			//Draw image in center of panel
			int x = (this.getWidth()-_thumbnail.getWidth())/2; 
			int y = (this.getHeight()-_thumbnail.getHeight())/2;
			g2.drawImage(_thumbnail, null, x, y);
		}

		//Display 3D window on double click
		public void mouseClicked(MouseEvent e) {
			if(e.getClickCount() >= 1) {
				if(e.isShiftDown()) {
					//Remove Couple
					if(_couple) {
						for(Couple<Organism<UV, Point3d>> couple: _couples) {
							if(couple.org1().equals(_organism) ||
							   couple.org2().equals(_organism)) {
								_couples.remove(couple);
								
								for(Component comp: _phenotypePanel.getComponents()) {
									OrganismPanel orgPanel = (OrganismPanel)comp;
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
					else {
						//Add candidate for match
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
						//Add couple
						else if(_match != this) {
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
							
							_couples.add(new Couple<Organism<UV,Point3d>>(_organism, 
									_match.getOrganism()));
							_match = null;
						}
					}
				}
				// Add as single
				else {
					if (_single) {
						_singles.remove(_organism);
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
					else {
						_single = true;
						_singles.add(_organism);
						
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
			}
			if(e.getClickCount() == 2) {
				_frame.setVisible(true);
			}
		}
		
		//Change thumb-nail image to last view position
		public void windowClosing(WindowEvent e) {
			_thumbnail = _surfacePanel.getSnapshot(256, 256);
			this.repaint();
		}
		
		//Empty methods required by interfaces
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e) {}		
		public void windowDeactivated(WindowEvent e) {}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e) {}
		public void windowOpened(WindowEvent e) {}

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				_frame.setVisible(false);
			}
		}

		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		public boolean dispatchKeyEvent(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				_manager.redispatchEvent(this, e);
				return true;
			}
			return false;
		}
	}
	
	/*public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	List<Organism<UV, Point3d>> organisms = 
            		new ArrayList<Organism<UV, Point3d>>();
            	
                SurfaceEvaluator eval = new SurfaceEvaluator();
                eval.evaluate(organisms);
            }
        });
	}*/
}
