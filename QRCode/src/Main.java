import java.io.IOException;


public class Main {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Mode mode = new Mode(Mode.ALPHANUMERIC);
		
		QRCode qr = new QRCode(new Version(8, mode), new ErrorCorrectionLevel(ErrorCorrectionLevel.Q), mode, "Hello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello World");
		Encoder encoder = new Encoder();
		
		BitVector bitVector = encoder.encode(qr);
		System.out.println("bitVector: ");
		System.out.println(bitVector);
		
		//MATRIX
		try {
			MatrizImgQR.GerarImagem(qr, bitVector, 5);
		} catch (IOException ex) {
			
		}
		           
		
		//BINARY VECTOR
		//QRCode qr = new QRCode(15, 2, 2, "Hello World
		//Hello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello WorldHello World");
		//QRCode qr = new QRCode(1, 2, 2, "testando.");
		
		
		
		//qr.init();
		/*System.out.println();
		for(int i=0; i<= qr.getVector().getNumberOfBits(); i++){
			System.out.print(qr.getVector().getBitWise(i));
		}
		System.out.println();*/
		
		//System.out.println(qr.getVector().toString());
		
//		BitVector bitVector = new BitVector(2);
//		bitVector.appendBits(4, 32);
//		bitVector.appendBits(4, 31);
//		
//		System.out.println("ToString: "+bitVector.toString());
//		System.out.println("0: "+bitVector.get(0));
//		System.out.println("1: "+bitVector.get(0));
//
//		System.out.println(bitVector.getOffset());
//		
//		System.out.println("Teste: "+bitVector.getNBits(60, 3));
//		
//
//		System.out.println(bitVector.getOffset());
		
		/*int[] bits = new int[1];
		int size=bits.length;
		System.out.println("Passo 1 - bits.length="+bits.length+", bits[0]="+bits[0]);
		int aux = (size & 0x1F);
		int index = size >> 5;
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		System.out.println("Passo 2 - bits.length="+bits.length+", bits[0]="+bits[0]);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		size++;
		index = size>> 5;
		aux = (size & 0x1F);
		bits[index] |= 1 << aux;
		System.out.println("Index="+index+", Aux="+aux);
		System.out.println("Passo 2 - bits.length="+bits.length+", bits[0]="+bits[0]);*/
		
		
	}

}
