package MandelbrotSet;

public class Complex {
	
	private float real, imaginary;
	
	public Complex(float real, float imaginary){
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public void add(Complex a){
		real += a.getReal();
		imaginary += a.getImaginary();
	}
	
	public void sub(Complex a){
		real -= a.getReal();
		imaginary -= a.getImaginary();
	}
	
	public void multiply(Complex a){
		float r = real * a.getReal()  - imaginary * a.getImaginary();
		float i = real * a.getImaginary() + imaginary * a.real;
		real = r;
		imaginary = i;
	}
	
	public float getMagnitude(){
		return (float) Math.sqrt(real * real + imaginary * imaginary);
	}
	
	public float getReal(){
		return real;
	}
	
	public float getImaginary(){
		return imaginary;
	}
	public String toString(){
		return real + " + " + imaginary + "i ";
	}
}