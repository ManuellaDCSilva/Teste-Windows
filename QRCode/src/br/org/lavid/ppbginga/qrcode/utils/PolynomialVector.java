package br.org.lavid.ppbginga.qrcode.utils;

public class PolynomialVector {

	private int vector[];
	private int length;

	public PolynomialVector(int lenght) {
		this.length = lenght;
		this.vector = new int[lenght + 1];
	}

	public int[] getVector() {
		return vector;
	}

	public void setVector(int[] vector) {
		this.vector = vector;
	}

	public int getTerm(int exponent) {
		if ((exponent <= length) && (exponent >= 0))
			return vector[exponent];
		else
			return -1;
	}

	public void setTerm(int coefficient, int exponent) {
		vector[exponent] = coefficient;
	}

	public int length() {
		return this.length;
	}
	
	public String toString(){
		String result = "";
		for(int i=this.length; i>=0; i--){
			result += "a" + this.vector[i] + "x" + i + " + ";
		}
		return result;
	}

}
