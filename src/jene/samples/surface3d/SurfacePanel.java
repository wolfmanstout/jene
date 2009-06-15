package jene.samples.surface3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import jene.Organism;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;


/**
 *  
 * @author Andrew Loomis
 *
 */
 
 public class SurfacePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Organism<UV, Point3d> _organism;
	private Canvas3D _canvas3D;
	private OffScreenCanvas3D _offScreenCanvas3D;
	private JTextPane _infoPane;
	private SimpleUniverse _simpleU;
	
	public SurfacePanel(Organism<UV, Point3d> organism) {
		_organism = organism;
		
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(720, 720));
		
		GraphicsConfiguration config = 
			SimpleUniverse.getPreferredConfiguration();
		
		//Set up on screen canvas 
		_canvas3D = new Canvas3D(config);
		
		BranchGroup scene = createSceneGraph();
		
		_simpleU = new SimpleUniverse(_canvas3D);
		_simpleU.getViewingPlatform().setNominalViewingTransform();
		_simpleU.addBranchGraph(scene);
		
		//Set up off screen canvas
		_offScreenCanvas3D = new OffScreenCanvas3D(config);
		
		//Set the off screen to match the on screen
	    Screen3D screenOn = _canvas3D.getScreen3D();
	    Screen3D screenOff = _offScreenCanvas3D.getScreen3D();
	    screenOff.setSize(screenOn.getSize());
	    screenOff.setPhysicalScreenWidth(screenOn.getPhysicalScreenWidth());
	    screenOff.setPhysicalScreenHeight(screenOn.getPhysicalScreenHeight());
		
		_simpleU.getViewer().getView().addCanvas3D(_offScreenCanvas3D);
		
		_infoPane = new JTextPane();
		_infoPane.setEditable(false);
		_infoPane.setBackground(Color.black);
		MutableAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setAlignment(aSet, StyleConstants.ALIGN_CENTER);
		StyleConstants.setForeground(aSet, Color.white);
		_infoPane.setParagraphAttributes(aSet, true);
		_infoPane.setText("Genotype: " + _organism.nodes().toString());
		
		this.add(BorderLayout.NORTH, _infoPane);
		this.add(BorderLayout.CENTER, _canvas3D);
	}
	
	public void cleanup() {
		_simpleU.removeAllLocales();
		_simpleU.cleanup();
		_simpleU = null;
	}
	
	private BranchGroup createSceneGraph() {
		BranchGroup objRoot = new BranchGroup();
		
		//Set up node for rotating the surface based on mouse drags
		TransformGroup objTransform = new TransformGroup();
		objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		//Set up node for translating and scaling the surface
		TransformGroup surfaceTranslate = new TransformGroup();
		TransformGroup surfaceScale = new TransformGroup();
		surfaceTranslate.setBoundsAutoCompute(true);
		surfaceScale.setBoundsAutoCompute(true);
		
		surfaceTranslate.addChild(new Surface(_organism, 32, 32));
		BoundingSphere bSphere = new BoundingSphere(surfaceTranslate.getBounds());
		Point3d center = new Point3d();
		bSphere.getCenter(center);
		double radius = bSphere.getRadius();
		
		Matrix3d rotate = new Matrix3d();
		rotate.setIdentity();
		Vector3d translate = new Vector3d(center);
		translate.negate();
		double scale = 1/radius;
		
		surfaceTranslate.setTransform(new Transform3D(rotate,translate,1.0));
		surfaceScale.setTransform(new Transform3D(rotate,new Vector3d(),scale));
		surfaceScale.addChild(surfaceTranslate);
		
		//random rotation
		Transform3D rotX = new Transform3D();
		Transform3D rotY = new Transform3D();
		Transform3D rotZ = new Transform3D();
		rotX.rotX(2*Math.PI*Math.random());
		rotY.rotY(2*Math.PI*Math.random());
		rotZ.rotZ(2*Math.PI*Math.random());
		
		rotX.mul(rotY);
		rotX.mul(rotZ);
		
		TransformGroup randRot = new TransformGroup(rotX);
		randRot.addChild(surfaceScale);
		
		//Add nodes to root tree
		objTransform.addChild(randRot);
		objRoot.addChild(objTransform);
		
		//Set up lights
		DirectionalLight dirLight = 
			new DirectionalLight(new Color3f(.9f,.9f,.9f),new Vector3f(0,0,-1));
		dirLight.setInfluencingBounds(surfaceScale.getBounds());
		objRoot.addChild(dirLight);
		
		AmbientLight ambLight = new AmbientLight(new Color3f(.3f,.3f,.3f));
		ambLight.setInfluencingBounds(surfaceScale.getBounds());
		objRoot.addChild(ambLight);
		
		//Set up mouse interactions
		MouseRotate myMouseRotate = new MouseRotate();
		myMouseRotate.setTransformGroup(objTransform);
		myMouseRotate.setSchedulingBounds(surfaceScale.getBounds());
		objRoot.addChild(myMouseRotate);
	    
		MouseZoom myMouseZoom = new MouseZoom();
		myMouseZoom.setTransformGroup(objTransform);
		myMouseZoom.setSchedulingBounds(surfaceScale.getBounds());
		objRoot.addChild(myMouseZoom);
		
		MouseTranslate myMouseTranslate = new MouseTranslate();
		myMouseTranslate.setTransformGroup(objTransform);
		myMouseTranslate.setSchedulingBounds(surfaceScale.getBounds());
		objRoot.addChild(myMouseTranslate);
		
	    objRoot.compile();
	    
		return objRoot;
	}
	
	public BufferedImage getSnapshot(int width, int height) {
		return _offScreenCanvas3D.render(width, height);
	}
	
	private class OffScreenCanvas3D extends Canvas3D {
		private static final long serialVersionUID = 1L;

		public OffScreenCanvas3D(GraphicsConfiguration config) {
			super(config, true);
		}
		
		public BufferedImage render(int width, int height) {
			
			BufferedImage image = 
				new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			ImageComponent2D buffer = 
				new ImageComponent2D(ImageComponent.FORMAT_RGBA, image);
			
			this.setOffScreenBuffer(buffer);
			this.renderOffScreenBuffer();
			this.waitForOffScreenRendering();
			
			return this.getOffScreenBuffer().getImage();
		}
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Mesh Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new SurfacePanel(null));//new Organism()));
		
		frame.pack();
		frame.setVisible(true);
	}
}