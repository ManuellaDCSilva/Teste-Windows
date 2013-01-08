package br.org.lavid.ppbginga.qrcode;
import br.org.lavid.ppbginga.qrcode.QRCode;
import br.org.lavid.ppbginga.qrcode.utils.*;
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		QRCode qr = new QRCode(1, 3, 2, "Hello World");

		qr.init();
		
		System.out.println();
		for(int i=0; i<= qr.getVector().getNumberOfBits(); i++){
			System.out.print(qr.getVector().getBitWise(i));
		}
		System.out.println();
		
	}

}
