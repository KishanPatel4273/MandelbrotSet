package MandelbrotSet;

public class Render {
	
	private int width, height, color;
	protected int[] pixels;
	
	public Render(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		color = 0;
	}
	
	public void drawPoint(int x, int y, int color){
		if(x > 0 && x < width && y > 0 && y < height){
			pixels[x + y * width] = color;
		}
	}
	
	/**
	 * @return from (x1, y1) to (x2, y2) draws line
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		if(x1 != x2){
			float slope = ((float)(y1 - y2) / (x1 - x2));
			for(int x = Math.min(x1, x2); x < Math.max(x1, x2); x++){
				int y = (int) ((float)(slope * (x - x1) + y1)); 
				if(x > 0 && x < width && y > 0 && y < height){
					pixels[x + y * width] = color;
				}
			}
			for(int y = Math.min(y1, y2); y < Math.max(y1, y2); y++){
				int x = (int) ((float)((y - y1) / slope)) + x1; 
				if(x > 0 && x < width && y > 0 && y < height){
					pixels[x + y * width] = color;
				}
			}
		} else {
			for(int y = Math.min(y1, y2); y < Math.max(y1, y2); y++){
				if(x1 > 0 && x1 < width && y > 0 && y < height){
					pixels[x1 + y * width] = color;
				}
			}
		}
	}
	
	public void fillTriangle(float[] xx, float[] yy){
		if(xx.length == 3 && yy.length == 3){
			for(int x = (int) xx[0]; x < xx[1]; x++){
				float y = lineY(x, xx[0], yy[0], xx[1], yy[1]);
				float y1 = lineY(x, xx[0], yy[0], xx[2], yy[2]);
				float y2 = lineY(x, xx[1], yy[1], xx[2], yy[2]);
				drawLine((int) x, (int) y, (int) x, (int) y1);
				
			}
		}
	}
	
	/**
	 * @param x has length of 3 x-components of the triangle
	 * @param y has length of 3 y-components of the triangle
	 */
	public void drawTriangle(float[] x, float[] y){
		if(x.length == 3 && y.length == 3){
			drawLine((int) x[0], (int) y[0], (int) x[1], (int) y[1]);
			drawLine((int) x[2], (int) y[2], (int) x[1], (int) y[1]);
			drawLine((int) x[0], (int) y[0], (int) x[2], (int) y[2]);
		}
	}
	
	public void drawEllipse(int centerX, int centerY, int a, int b){
		for(int x = centerX - a; x < centerX + a; x++){
			int y = (int) (float)(Math.sqrt((float)b*b * (1f - (float)((x - centerX)*(x - centerX))/(a*a))) + centerY);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
			y = (int) (float)(-Math.sqrt((float)b*b * (1f - (float)((x - centerX)*(x - centerX))/(a*a))) + centerY);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
		}
		for(int y = centerY - b; y < centerY + b; y++){
			int x = (int) ((float)(Math.sqrt((float)a*a * (1f - (float)(y - centerY)*(y - centerY)/(b*b)))) + centerX);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
			x = (int) ((float)(-Math.sqrt((float)a*a * (1f - (float)(y - centerY)*(y - centerY)/(b*b)))) + centerX);
			if(x > 0 && x < width && y > 0 && y < height){
				pixels[x + y * width] = color;
			}
		}
	}
	
	public float lineY(float x, float x1, float y1, float x2, float y2){
		if(x1 != x2){
			return ((float)(((float)(y1 - y2) / (x1 - x2)) * (x - x1) + y1)); 
		} else {
			return x1;
		}
	}
	
	public float lineX(float y, float x1, float y1, float x2, float y2){
		if(x1 != x2){
			return (int) ((float)((y - y1) / ((float)(y1 - y2) / (x1 - x2)))) + x1; 
		} else {
			return x1;
		}
	}
	
	public float distance(float x1, float y1, float x2, float y2){
		return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public float map(float x, float from1, float from2, float to1, float to2){
		from2 -= from1;
		to2 -= to1;
		x = (float)(x * (float)(to2/from2));
		return x + to1;
	}
	
	
	/**
	 * 
	 * @param x list of x-coordinates 
	 * @param y list of y-coordinates 
	 * @param c if c == 0 then x; if c == 1 then y;
	 */
	public void getMin(float[] x, float[] y, int c){
		if(c == 0){
			for(int i = 0; i < x.length; i++){
				
			}
		}
	}
	
	public int getRandom(int a, int b){
		return (int) ((b-a + 1) * Math.random() + a);
	}
	
	public void clear(){
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 0;
		}
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
}