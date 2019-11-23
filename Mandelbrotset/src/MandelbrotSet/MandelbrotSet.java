package MandelbrotSet;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class MandelbrotSet extends Render{

	private int depth, width, height;
	private float pXV, pYV;
	private float distanceX, distanceY;
	private ArrayList<Point> points;
	
	public MandelbrotSet(int depth, int width, int height) {
		super(width, height);
		this.depth = depth;
		this.width = width;
		this.height = height;
		pXV = 4f / width;
		pYV = 4f / height;
		points = new ArrayList<Point>();
	}

	public Complex f(Complex z, Complex c) {//f(z) = z^2 + c
		z.multiply(z);
		z.add(c);
		return z;
	}
	
	public void zoom(float cx, float cy, float s){
		long a = System.currentTimeMillis();
		//System.out.println("1 zoom:" + s);
		generate(cx - s, cx + s, cy - s, cy + s);
		System.out.println("2 time:" + (float)(System.currentTimeMillis() - a)/1000f);
	}
	
	public void generate(float minX,float maxX, float minY, float maxY) {
		points.clear();
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 0;
		}
		float[] range ={Integer.MAX_VALUE,Integer.MIN_VALUE,Integer.MAX_VALUE,Integer.MIN_VALUE};//min max X , min max Y
		distanceX = (float)Math.abs((maxX - minX));
		distanceY = (float)Math.abs((maxY - minY));
		for (float x = minX; x <= maxX; x += (float)(distanceX/width)) {
			for (float y = minY; y <= maxY; y += (float)(distanceY/height)) {
				Complex c = new Complex(x,y);
				Complex z = new Complex(0,0);
				for(int i = 0; i < depth; i++){
					//1002001000 when depth = 1000 zoom normal
					z = f(z,c);
					if(z.getMagnitude() > 2){
						break;//97793466 with break with continue same when depth = 1000 zoom normal
					}
				}
				float a1 = ((float)x * (width/distanceX)) + (float)width/(distanceX);
				float a2 = ((float)y * (height/distanceY)) + (float)height/(distanceY);
				range[0] = Math.min(range[0], a1);
				range[2] = Math.min(range[2], a2);
				if(z.getMagnitude() >= 2){
					points.add(new Point((int)a1-1, (int)a2-1));	
				}
			}
		}
		for(int i = 0; i < points.size(); i++){
			drawPoint((int) (points.get(i).x - range[0]), (int) (points.get(i).y - range[2]), 15402033);//12435678/100);
		}
		setColor(12435678/10);//24567523/34000
		//drawLine(width/2, 0, width/2, height);
		//drawLine(0,height/2, width, height/2);
	}
	
}