package jene.samples.surface3d;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.vecmath.*;

import jene.Organism;

import com.sun.j3d.utils.image.TextureLoader;

import visad.*;


/**
 *  
 * @author Andrew Loomis
 *
 */
 
 public class Surface extends Shape3D {
	
	static final int maxDiv = 4;
	
	private Organism<UV, Point3d> _organism;
	private Geometry _geometry;
	private Appearance _appearance;
	
	public Surface(Organism<UV, Point3d> organism, int uRes, int vRes) {
		_organism = organism;
		
		_geometry = this.createGeometry(uRes, vRes);
		_appearance = this.createAppearance();
		
		this.setGeometry(_geometry);
		this.setAppearance(_appearance);
	}
	
	public Point3d getPoint(double u, double v) {
		if (_organism != null) {
			return _organism.evaluate(new UV(u,v));
		}
		else {
			return new Point3d(u,v,Math.sin(Math.PI*u*v));
		}
	}
	
	private Geometry createGeometry(int uRes, int vRes) {
		
		ArrayList<SurfacePoint> points = new ArrayList<SurfacePoint>();
		ArrayList<SurfacePatch> patches = new ArrayList<SurfacePatch>();
		
		System.out.println("Evaluating Surface.");
		
		//Set up base u v resolution
		double u, v, du, dv;
		du = 2/(double)uRes;
		dv = 2/(double)vRes;
		for(int i = 0; i < uRes; ++i) {
			u = 2*i/(double)uRes-1;
			for(int j = 0; j < vRes; ++j) {
				v = 2*j/(double)vRes-1;
				patches.add(new SurfacePatch(
						new SurfacePoint(u,v,getPoint(u,v)),
						new SurfacePoint(u,v+dv,getPoint(u,v+dv)),
						new SurfacePoint(u+du,v+dv,getPoint(u+du, v+dv)),
						new SurfacePoint(u+du, v, getPoint(u+du, v))));
			}
		}
		
		//Determine average distance between samples on surface
		double avgDist = 0;
		Iterator<SurfacePatch> iter = patches.iterator();
		while(iter.hasNext()) {
			SurfacePatch patch = iter.next();
			
			Vector3d e1 = new Vector3d(patch.sPt1.pt.x - patch.sPt2.pt.x, 
									   patch.sPt1.pt.y - patch.sPt2.pt.y,
									   patch.sPt1.pt.z - patch.sPt2.pt.z);
			Vector3d e2 = new Vector3d(patch.sPt2.pt.x - patch.sPt3.pt.x,
									   patch.sPt2.pt.y - patch.sPt3.pt.y,
									   patch.sPt2.pt.z - patch.sPt3.pt.z);
			Vector3d e3 = new Vector3d(patch.sPt3.pt.x - patch.sPt4.pt.x,
									   patch.sPt3.pt.y - patch.sPt4.pt.y,
									   patch.sPt3.pt.z - patch.sPt4.pt.z);
			Vector3d e4 = new Vector3d(patch.sPt4.pt.x - patch.sPt1.pt.x,
									   patch.sPt4.pt.y - patch.sPt1.pt.y,
									   patch.sPt4.pt.z - patch.sPt1.pt.z);
			
			avgDist += e1.length() + e2.length() + e3.length() + e4.length();
		}
		avgDist /= 4*patches.size();
		
		//Evaluate the surface patches and adaptively add more resolution where
		//needed
		while(!patches.isEmpty()) {
			SurfacePatch patch = patches.remove(0);
			
			//Add the Points to the sample
			if(!points.contains(patch.sPt1)) {
				points.add(patch.sPt1);
			}
			if(!points.contains(patch.sPt2)) {
				points.add(patch.sPt2);
			}
			if(!points.contains(patch.sPt3)) {
				points.add(patch.sPt3);
			}
			if(!points.contains(patch.sPt4)) {
				points.add(patch.sPt4);
			}
			
			// Determine whether or not we need to increase sampling
			double minU = patch.sPt1.u;
			double minV = patch.sPt1.v;
			double maxU = patch.sPt3.u;
			double maxV = patch.sPt3.v;
			double midU = minU+(maxU-minU)/2.0;
			double midV = minV+(maxV-minV)/2.0;
			
			SurfacePoint sPt1 = new SurfacePoint(minU, midV, getPoint(minU, midV));
			SurfacePoint sPt2 = new SurfacePoint(midU, midV, getPoint(midU, midV));
			SurfacePoint sPt3 = new SurfacePoint(midU, minV, getPoint(midU, minV));
			SurfacePoint sPt4 = new SurfacePoint(midU, maxV, getPoint(midU, maxV));
			SurfacePoint sPt5 = new SurfacePoint(maxU, midV, getPoint(maxU, midV));
			
			Point3d midV1Avg = new Point3d();
			Point3d midU1Avg = new Point3d();
			Point3d midV2Avg = new Point3d();
			Point3d midU2Avg = new Point3d();
			
			midV1Avg.interpolate(patch.sPt1.pt, patch.sPt2.pt, .5);
			midU1Avg.interpolate(patch.sPt2.pt, patch.sPt3.pt, .5);
			midV2Avg.interpolate(patch.sPt3.pt, patch.sPt4.pt, .5);
			midU2Avg.interpolate(patch.sPt4.pt, patch.sPt1.pt, .5);
			
			double v1Diff = midV1Avg.distance(sPt1.pt);
			double u1Diff = midV1Avg.distance(sPt4.pt);
			double v2Diff = midV1Avg.distance(sPt5.pt);
			double u2Diff = midV1Avg.distance(sPt3.pt);
			
			Vector3d e1 = new Vector3d(patch.sPt1.pt.x-patch.sPt2.pt.x, 
								   	   patch.sPt1.pt.y-patch.sPt2.pt.y,
								   	   patch.sPt1.pt.z-patch.sPt2.pt.z);
			Vector3d e2 = new Vector3d(patch.sPt2.pt.x-patch.sPt3.pt.x,
								   	   patch.sPt2.pt.y-patch.sPt3.pt.y,
								   	   patch.sPt2.pt.z-patch.sPt3.pt.z);
			Vector3d e3 = new Vector3d(patch.sPt3.pt.x-patch.sPt4.pt.x,
								   	   patch.sPt3.pt.y-patch.sPt4.pt.y,
								   	   patch.sPt3.pt.z-patch.sPt4.pt.z);
			Vector3d e4 = new Vector3d(patch.sPt4.pt.x-patch.sPt1.pt.x,
								   	   patch.sPt4.pt.y-patch.sPt1.pt.y,
								   	   patch.sPt4.pt.z-patch.sPt1.pt.z);
			
			boolean splitU = 
				((e2.length() > avgDist || e4.length() > avgDist || 
				u1Diff > 4/(double)uRes || u2Diff > 6/(double)uRes) &&
				patch.sPt3.u-patch.sPt1.u > 2/(double)(uRes*(maxDiv+1)));
			
			
			boolean splitV = 
				((e1.length() > avgDist || e3.length() > avgDist ||
				v1Diff > 4/(double)vRes || v2Diff > 4/(double)vRes) &&
				patch.sPt3.v-patch.sPt1.v > 2/(double)(vRes*(maxDiv+1)));
			
			//Increase sampling in u and v
			if(splitU && splitV) {
				patches.add(new SurfacePatch(patch.sPt1, sPt1, sPt2, sPt3));
				patches.add(new SurfacePatch(sPt1, patch.sPt2, sPt4, sPt2));
				patches.add(new SurfacePatch(sPt2, sPt4, patch.sPt3, sPt5));
				patches.add(new SurfacePatch(sPt3, sPt2, sPt5, patch.sPt4));
			}
			//Increase sampling in just u
			else if(splitU) {
				patches.add(new SurfacePatch(patch.sPt1, patch.sPt2, sPt4, sPt2));
				patches.add(new SurfacePatch(sPt3, sPt4, patch.sPt3, patch.sPt4));
			}
			//Increase Sampling in just v
			else if(splitV) {
				patches.add(new SurfacePatch(patch.sPt1, sPt1, sPt5, patch.sPt4));
				patches.add(new SurfacePatch(sPt1, patch.sPt2, patch.sPt3, sPt5));
			}
		}
		
		System.out.println("Triangulating Surface.");
		
		//Set up triangulation in parameter space
		float[][] samples = new float[2][points.size()];
		for(int i = 0; i < points.size(); ++i) {
			samples[0][i] = 1000*(float)points.get(i).u;
			samples[1][i] = 1000*(float)points.get(i).v;
		}
		
		//Triangulate in parameter space using visad library and catch any
		//exceptions
		Delaunay delaunay;
		try {
			delaunay = new DelaunayClarkson(samples);
		}
		catch(VisADException e) {
			System.out.println("Triangulation failed.");
			return new TriangleArray(3, GeometryArray.COORDINATES);
		}
		
		//Construct geometry using the triangulation
		int numTris = delaunay.Tri.length;
		double[] vertices = new double[9*numTris];
		float[] normals = new float[9*numTris];
		float[] texCoords = new float[6*numTris];
		
		//Calculate the normals of every triangle
		Vector3d[] triNorms = new Vector3d[numTris];
		for(int i = 0; i < numTris; ++i) {
			SurfacePoint a = points.get(delaunay.Tri[i][0]);
			SurfacePoint b = points.get(delaunay.Tri[i][1]);
			SurfacePoint c = points.get(delaunay.Tri[i][2]);

			//Flip order if necessary
			Vector3d v1 = new Vector3d(b.u-a.u,b.v-a.v,0);
			Vector3d v2 = new Vector3d(c.u-a.u,c.v-a.v,0);
			v1.cross(v2, v1);
			if(v1.z < 0) {
				SurfacePoint temp = b;
				b = c;
				c = temp;
			}
			
			v1 = new Vector3d(b.pt.x-a.pt.x, b.pt.y-a.pt.y, b.pt.z-a.pt.z);
			v2 = new Vector3d(c.pt.x-a.pt.x, c.pt.y-a.pt.y, c.pt.z-a.pt.z);
			
			v1.cross(v2, v1);
			v1.normalize();
			
			triNorms[i] = v1;
		}
		
		//Calculate the interpolated normals of every vertex
		Vector3d[] vertNorms = new Vector3d[delaunay.Vertices.length];
		for(int i = 0; i < delaunay.Vertices.length; ++i) {
			
			//Fix normal calculations at edges
			if(Math.abs(points.get(i).u) > 1.0-2.0/(double)uRes ||
			   Math.abs(points.get(i).v) > 1.0-2.0/(double)vRes) {
				
				Point3d a = points.get(i).pt;
				Point3d b = this.getPoint(points.get(i).u + 2.0/(double)uRes, points.get(i).v);
				Point3d c = this.getPoint(points.get(i).u, points.get(i).v+2.0/(double)vRes);
				
				Vector3d v1 = new Vector3d(b.x-a.x, b.y-a.y, b.z-a.z);
				Vector3d v2 = new Vector3d(c.x-a.x, c.y-a.y, c.z-a.z);
				
				v1.cross(v1, v2);
				v1.normalize();
				
				vertNorms[i] = v1;
				
			}
			else {
				vertNorms[i] = new Vector3d(0,0,0);
				int numNeighbors = delaunay.Vertices[i].length;
				for(int j = 0; j < numNeighbors; ++j) {
					vertNorms[i].x += triNorms[delaunay.Vertices[i][j]].x;
					vertNorms[i].y += triNorms[delaunay.Vertices[i][j]].y;
					vertNorms[i].z += triNorms[delaunay.Vertices[i][j]].z;
				}
				vertNorms[i].normalize();
			}
		}
		
		//Create coordinate normal and color arrays
		for(int i = 0; i < numTris; ++i) {
			
			SurfacePoint a = points.get(delaunay.Tri[i][0]);
			SurfacePoint b = points.get(delaunay.Tri[i][1]);
			SurfacePoint c = points.get(delaunay.Tri[i][2]);
			Vector3d na = vertNorms[delaunay.Tri[i][0]];
			Vector3d nb = vertNorms[delaunay.Tri[i][1]];
			Vector3d nc = vertNorms[delaunay.Tri[i][2]];
			
			//Flip order if necessary
			Vector3d v1 = new Vector3d(b.u-a.u,b.v-a.v,0);
			Vector3d v2 = new Vector3d(c.u-a.u,c.v-a.v,0);
			v1.cross(v2, v1);
			if(v1.z < 0) {
				SurfacePoint temp = b;
				b = c;
				c = temp;
				
				Vector3d tempN = nb;
				nb = nc;
				nc = tempN;
			}
			
			vertices[9*i] = a.pt.x;
			vertices[9*i+1] = a.pt.y;
			vertices[9*i+2] = a.pt.z;
				
			vertices[9*i+3] = c.pt.x;
			vertices[9*i+4] = c.pt.y;
			vertices[9*i+5] = c.pt.z;
						
			vertices[9*i+6] = b.pt.x;
			vertices[9*i+7] = b.pt.y;
			vertices[9*i+8] = b.pt.z;
			
			normals[9*i] = (float)na.x;
			normals[9*i+1] = (float)na.y;
			normals[9*i+2] = (float)na.z;
				
			normals[9*i+3] = (float)nc.x;
			normals[9*i+4] = (float)nc.y;
			normals[9*i+5] = (float)nc.z;
				
			normals[9*i+6] = (float)nb.x;
			normals[9*i+7] = (float)nb.y;
			normals[9*i+8] = (float)nb.z;
			
			texCoords[6*i] = (float)((a.u+1)/2.0);
			texCoords[6*i+1] = (float)((a.v+1)/2.0);
			
			texCoords[6*i+2] = (float)((c.u+1)/2.0);
			texCoords[6*i+3] = (float)((c.v+1)/2.0);
			
			texCoords[6*i+4] = (float)((b.u+1)/2.0);
			texCoords[6*i+5] = (float)((b.v+1)/2.0);
		}
		
		TriangleArray geometry = new TriangleArray(3*numTris, GeometryArray.COORDINATES ^ GeometryArray.NORMALS ^ GeometryArray.TEXTURE_COORDINATE_2);
		geometry.setCoordinates(0, vertices);
		geometry.setNormals(0, normals);
		geometry.setTextureCoordinates(0, 0, texCoords);
		return geometry;
	}
	
	private Appearance createAppearance() {
		
		Material material = new Material();
		material.setSpecularColor(.7f,.7f,.7f);
		
		if(_organism instanceof S3DOrganism) {
			float[] rgb = ((S3DOrganism)_organism).color();
			material.setDiffuseColor(rgb[0], rgb[1], rgb[2]);
		}
		else {
			material.setDiffuseColor(1, 1, 1);
		}
		
		//Load texture
		BufferedImage textureImage;
		try {
			textureImage = ImageIO.read(getClass().getResourceAsStream("checker_mask.png"));
		} catch (IOException ex) {
			// should not happen (texture should be found)
			throw new RuntimeException("Image not found", ex);
		}
		TextureLoader loader = new TextureLoader(textureImage, (Component) null);
		ImageComponent2D  image = loader.getImage();
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		TextureAttributes ta = new TextureAttributes();
		ta.setTextureMode(TextureAttributes.DECAL);
		
		Appearance appearance = new Appearance();
		appearance.setMaterial(material);
		appearance.setTexture(texture);
		
		appearance.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0, true));
		appearance.setColoringAttributes(new ColoringAttributes(1, 1, 1, ColoringAttributes.SHADE_GOURAUD));
		appearance.setTextureAttributes(ta);
		//appearance.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST, .3f));
		
		return appearance;
	}
	
	private class SurfacePoint {
		double u,v;
		Point3d pt;
		
		SurfacePoint(double u, double v, Point3d pt) {
			this.u = u;
			this.v = v;
			this.pt = pt;
		}
		
		@Override
		public boolean equals(Object obj) {
			SurfacePoint sPt = (SurfacePoint)obj;
			if(this.u == sPt.u && this.v == sPt.v && this.pt.equals(sPt.pt)) {
				return true;
			}
			return false;
		}
	}

	private class SurfacePatch {
		SurfacePoint sPt1, sPt2, sPt3, sPt4;
		
		SurfacePatch(SurfacePoint pt1, 
				     SurfacePoint pt2,
				     SurfacePoint pt3,
				     SurfacePoint pt4) {
			this.sPt1 = pt1;
			this.sPt2 = pt2;
			this.sPt3 = pt3;
			this.sPt4 = pt4;
		}
		
		double getArea() {		
			Vector3d v1 = new Vector3d(sPt2.pt.x-sPt1.pt.x, sPt2.pt.y-sPt1.pt.y, sPt2.pt.z-sPt1.pt.z);
			Vector3d v2 = new Vector3d(sPt3.pt.x-sPt1.pt.x, sPt3.pt.y-sPt1.pt.y, sPt3.pt.z-sPt1.pt.z);
			Vector3d v3 = new Vector3d(sPt4.pt.x-sPt1.pt.x, sPt4.pt.y-sPt1.pt.y, sPt4.pt.z-sPt1.pt.z);
			
			v1.cross(v2, v1);
			double area = v1.length()/2.0;
			v1.cross(v3, v2);
			area += v1.length()/2.0;
			
			return area;
		}
	}
}
