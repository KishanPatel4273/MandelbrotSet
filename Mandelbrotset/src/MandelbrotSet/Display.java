package MandelbrotSet;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Display extends Canvas implements Runnable {

	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	public static final String TITLE = "MandelbortSet";
	
	
	private Thread thread;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	Render render;
	MandelbrotSet ms;
	InputHandler input;
	JuliaSet js;
	float mx, my;
	int jsc  = WIDTH/4;
	int jsx = WIDTH - jsc;
	int jsy = 0;
	int depth = 500;
	float[][] poi = {//http://www.cuug.ab.ca/dewara/mandelbrot/images.html
			{-0.7463f, 0.1102f,  0.005f},//0
			{-0.7453f, 0.1127f, 6.5E-4f},//1 
			{ -0.16f, 1.0405f,.026f},//2''
			{-0.748f,0.1f,0.0014f},//3
			{-0.722f,0.246f, 0.019f},//4'
			{-0.7463f, 0.1102f, 2f},//5
			{-0.748f,0.1f,1/64f},//6
			{-.6f,.5f,1/12f},//7 '
			{-1.750111f,0.0f,1/1000f},//8'''
			{-.7105f,0.246f,1.937151E-4f},//9'
			{-1.25066f,0.02012f,1.7E-4f},//10''speed up
			{-0.235125f,0.827215f,4.0E-5f},//11''' low depth(200)
			{ -0.16070135f,1.0375665f,1.0E-3f},//12
			{  -0.840719f, 0.22442f, 7.9E-5f},//13'' low depth(100)
			{-1.99999911758738f,0f,7E-4f},//14
			{-0.7612f,-0.0847596f, 1/3425f},//15' low depth (400)
			{0f,0f, 2f},//16' normal
	};
	int r =  16;
	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
		
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		ms = new MandelbrotSet(depth, WIDTH, HEIGHT);
		js = new JuliaSet(depth/4, jsc, jsc);
		
		mx = ms.map(input.MouseX, poi[r][0] -  poi[r][2], poi[r][1] -  poi[r][2], 0, 1000);
		my = ms.map(input.MouseY, poi[r][0] -  poi[r][2], poi[r][1] -  poi[r][2], 0, 1000);
		
		ms.zoom(poi[r][0], poi[r][1], poi[r][2]);
		js.zoom(poi[r][0], poi[r][1], poi[r][2], new Complex(-0.8f,0));
		for(int i = 0; i < ms.pixels.length; i++){
			pixels[i] = ms.pixels[i];
		}
		for(int x = 0; x < jsc; x++){
			for(int y = 0; y < jsc; y++){
				pixels[(x + jsx) + (y + jsy) * WIDTH] = js.pixels[x + y * jsc];
			}
		}
	}

	private void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;

		while (running) {
			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				//tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					//System.out.println(frames + "fps");
					previousTime += 1000;
					frames = 0;
				}

			}
			if (ticked) {
				render();
				frames++;
			}
			render();
			frames++;
		}
	}
	
	int time;
	float zoom = 2f;
	int a = 0;
	float[] b = {1/2f, 1/4f, 1/64f, 1/128f, 1/256f, 1/1000f};
	private void tick() {
		time++;
		if(time % 1 == 0){
			if(zoom > 5/100f){
				zoom -= 1/100f;
			} else if(zoom > 1/500f) {
				zoom -= 1/1000f;
			} else {
				zoom -= 1/10000f;
			}
			ms.zoom(poi[r][0], poi[r][1], zoom);
			render();
			//r++;
			a++;
		}
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}		
		Graphics g = bs.getDrawGraphics();
		
		mx = ms.map(input.MouseX,  0, 1000, (poi[r][0] -  poi[r][2]), (poi[r][1] +  poi[r][2]));
		my = ms.map(input.MouseY,  0, 1000, (poi[r][0] -  poi[r][2]), (poi[r][1] +  poi[r][2]));
		//System.out.println(new Complex(mx, my).toString());
		js.zoom(poi[16][0], poi[16][1], poi[16][2], new Complex(mx, my));
		for(int i = 0; i < ms.pixels.length; i++){
			pixels[i] = ms.pixels[i];
		}
		for(int x = 0; x < jsc; x++){
			for(int y = 0; y < jsc; y++){
				pixels[(x + jsx) + (y + jsy) * WIDTH] = js.pixels[x + y * jsc];
			}
		}
		
		g.drawImage(img, 0, 0, null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Display game = new Display();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);;
		game.start();
	}	
}