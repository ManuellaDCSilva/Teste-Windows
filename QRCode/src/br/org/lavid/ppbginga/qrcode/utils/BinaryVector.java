package br.org.lavid.ppbginga.qrcode.utils;

public class BinaryVector {

	private int pointerBlockN; // Point to the block number
	private int pointerBlockPosition; // Point to the position in block
	private int mode;
	private int version;
	private String data;
	private int vector[];
	private int capacityDatabits;
	
	
	
	public BinaryVector(int mode, int version, String data, int capacityDatabits, int vectorLength) {
		super();
		this.mode = mode;
		this.version = version;
		this.data = data;
		this.capacityDatabits = capacityDatabits;
		this.vector = new int[vectorLength];
		this.pointerBlockN = 0;
		this.pointerBlockPosition = 0;
	}

	public int encodeModeIndicator() {
		int binaryVector = -1;
		int result = -1;

		switch (mode) {
		case 1:
			// Numeric Mode
			binaryVector = 1;
			break;
		case 3:
			// Binary Mode
			binaryVector = 3;
			break;
		case 4:
			// Kanji Mode
			binaryVector = 4;
			break;
		default:
			// Alphanumeric Mode
			binaryVector = 2;
			break;
		}

		if (binaryVector != -1) {
			this.vector[0] = binaryVector;
			this.pointerBlockN = 0;
			this.pointerBlockPosition = 4;
			result = 1;
		}

		return result;
	}
	
	public int encodeContentLength() {
		int binaryVector = -1;
		int result = -1;

		switch (mode) {
		case 1:
			// Numeric Mode
			if (this.version < 10) {
				this.vector[0] = this.vector[0] << 10; // Desloca a direita 10
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 10;
			} else if ((this.version > 9) && (this.version < 27)) {
				this.vector[0] = this.vector[0] << 12; // Desloca a direita 12
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 12;
			} else if ((this.version > 26) && (this.version < 41)) {
				this.vector[0] = this.vector[0] << 14; // Desloca a direita 14
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 14;
			}

			binaryVector = this.data.length();
			this.vector[0] = this.vector[0] + binaryVector;

			break;
		case 3:
			// Binary Mode
			if (this.version < 10) {
				this.vector[0] = this.vector[0] << 8; // Desloca a direita 8
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 8;
			} else if ((this.version > 9) && (this.version < 27)) {
				this.vector[0] = this.vector[0] << 16; // Desloca a direita 16
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 16;
			} else if ((this.version > 26) && (this.version < 41)) {
				this.vector[0] = this.vector[0] << 16; // Desloca a direita 16
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 16;
			}

			binaryVector = this.data.length();
			this.vector[0] = this.vector[0] + binaryVector;

			break;
		case 4:
			// Kanji Mode
			if (this.version < 10) {
				this.vector[0] = this.vector[0] << 8; // Desloca a direita 8
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 8;
			} else if ((this.version > 9) && (this.version < 27)) {
				this.vector[0] = this.vector[0] << 10; // Desloca a direita 10
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 10;
			} else if ((this.version > 26) && (this.version < 41)) {
				this.vector[0] = this.vector[0] << 12; // Desloca a direita 12
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 12;
			}

			binaryVector = this.data.length();
			this.vector[0] = this.vector[0] + binaryVector;

			break;
		default:
			// Alphanumeric Mode
			if (this.version < 10) {
				this.vector[0] = this.vector[0] << 9; // Desloca a direita 9
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 9;
			} else if ((this.version > 9) && (this.version < 27)) {
				this.vector[0] = this.vector[0] << 11; // Desloca a direita 11
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 11;
			} else if ((this.version > 26) && (this.version < 41)) {
				this.vector[0] = this.vector[0] << 13; // Desloca a direita 13
														// posicoes para receber
														// o tamanho de content
				this.pointerBlockPosition += 13;
			}

			binaryVector = this.data.length();
			this.vector[0] = this.vector[0] + binaryVector;

			break;
		}

		if (binaryVector != -1) {
			result = 1;
			this.pointerBlockN = 0;
		}

		return result;
	}
	
	public int encodeContentData() {
		
		int result = -1;
		int length = this.data.length();

		try {

			for (int i = 0; i < length-1; i = i + 2) {// Percorre 2 a 2 os
														// pares do qr.content

				int first = convertToASCIIValue(this.data.charAt(i));
				int second = convertToASCIIValue(this.data.charAt(i + 1));
				
				int pair = (first * 45) + second; // Deve ser representado
				
				
				if (this.pointerBlockPosition <= 20) { // Caso o bloco ainda
														// caiba a palavra os 11
														// bits

					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 11;
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + pair;
					this.pointerBlockPosition += 11;
					
				} else { // Caso contrario

					int emptyBlockPosition = 31 - this.pointerBlockPosition; // Descobre
																				// quantas
																				// posicoes
																				// estao
																				// vagas
																				// no
																				// bloco

					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << emptyBlockPosition; // Desloca
																												// para
																												// direita
																												// as
																												// posicoes
																												// vagas

					int parteAlta = pair >> (11 - emptyBlockPosition); // Retira
																		// a
																		// qtde
																		// certa
																		// da
																		// parte
																		// alta
																		// dos
																		// 11
																		// bits
																		// significativos
																		// de
																		// pair
					int aux = (int) Math.pow(2, 11 - emptyBlockPosition);
					int parteBaixa =  (pair - (parteAlta*aux));
					//int parteBaixa = pair << (32 - (11 - emptyBlockPosition)); // Retira
																				// a
																				// qtde
																				// certa
																				// da
																				// parte
																				// baixa
																				// dos
																				// 11
																				// bits
																				// significativos
																				// de
																				// pair
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteAlta;

					this.pointerBlockN++;
					this.pointerBlockPosition = 11 - emptyBlockPosition;
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteBaixa;		
															
				}
				
			}
			if ((length % 2) != 0) { // Caso o número de caracteres seja impar
				int odd = convertToASCIIValue(this.data.charAt(length - 1)); // Utiliza
												// somente
												// 6
												// bits

				if (this.pointerBlockPosition <= 25) { // Caso o bloco ainda
														// caiba a palavra os 6
														// bits
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 6;
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + odd;
					this.pointerBlockPosition += 6;
				} else { // Caso contrario

					 int emptyBlockPosition = 31 - this.pointerBlockPosition; // Descobre quantas posicoes estao vagas no bloco 
					 this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << emptyBlockPosition; // Desloca para direita as posicoes vagas
					 int parteAlta = odd >> (6 - emptyBlockPosition); // Retira a qtde certa da parte alta dos
					                                                  // 6 bits significativos de pair 
					 int aux = (int)Math.pow(2, 6 - emptyBlockPosition);
					 int parteBaixa =  (odd - (parteAlta*aux));
					 					 
					 this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteAlta;
					  
					 this.pointerBlockN++; 
					 this.pointerBlockPosition = (6 - emptyBlockPosition);
					  
					 this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteBaixa;
					 

				}
			}

			result = 1;
		} catch (Exception e) {
			result = -1;
		}

		return result;

	}


	/*
	 * E se a qtdade de bits jah for multiplo de 8?
	 * O problema eh que se for multiplo e se for seguida a sequencia normal pode gerar um messagePolynomial com algum termo nulo
	 */
	/**
	 * 
	 * @return
	 */
	public int terminateBits() {
		int numberOfBits = (31 * (this.pointerBlockN)) + this.pointerBlockPosition;
		
		int result = -1;
		int dif = this.capacityDatabits	- ((this.pointerBlockN * 31) + this.pointerBlockPosition);

		System.out.println("NOB = " + numberOfBits);
		System.out.println("this.capacityDatabits = " + this.capacityDatabits);
		System.out.println("((this.pointerBlockN * 31) + this.pointerBlockPosition) = " + ((this.pointerBlockN * 31) + this.pointerBlockPosition));
		System.out.println("dif = " + dif);
				
		switch (dif) {
		case 0:
			break;
		case 1:
			if (this.pointerBlockPosition <= 30){
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 1;
				this.pointerBlockPosition++;
			}else{
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 1;
				this.pointerBlockN++;
				this.pointerBlockPosition=1;
			}
			break;
		case 2:
			if (this.pointerBlockPosition <= 29){
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 2;
				this.pointerBlockPosition+=2;
			}else{
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-this.pointerBlockPosition);
				this.pointerBlockN++;
				this.pointerBlockPosition=2-(31-this.pointerBlockPosition);
			}
			break;
		case 3:
			if (this.pointerBlockPosition <= 28){
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 3;
				this.pointerBlockPosition+=3;
			}else{
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-this.pointerBlockPosition);
				this.pointerBlockN++;
				this.pointerBlockPosition=3-(31-this.pointerBlockPosition);
			}
			break;
		default:
			if (this.pointerBlockPosition <= 27){
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 4;
				this.pointerBlockPosition+=4;
			
			}else{
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-this.pointerBlockPosition);
				
				this.pointerBlockN++;
				this.pointerBlockPosition=4-(31-this.pointerBlockPosition);
			}
			break;
		}
		
		//int 
		numberOfBits = (31 * (this.pointerBlockN)) + this.pointerBlockPosition;
		System.out.println("-NOB = " + numberOfBits);
		
					
		if ((numberOfBits % 8) != 0) {
			if (this.pointerBlockPosition <= (31-(8-(numberOfBits % 8)))){
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (8 - (numberOfBits % 8));
				
				this.pointerBlockPosition += (8 - (numberOfBits % 8));
				
			}else{
				int emptyBlockPosition = 31 - this.pointerBlockPosition;
				
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << emptyBlockPosition;
				
				this.pointerBlockN++;
				this.pointerBlockPosition=((8-(numberOfBits%8))-emptyBlockPosition);
				
			}
		}
		numberOfBits = (31 * (this.pointerBlockN))
				+ this.pointerBlockPosition;
		System.out.println("--NOB = " + numberOfBits);
		
		int i = 0;
		while (numberOfBits < this.capacityDatabits) {
			if ((i % 2) == 0) {
				int n = 236;
				if (this.pointerBlockPosition <= 23) { // Caso o bloco ainda
														// caiba a palavra os 6
														// bits
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 8;
					this.vector[this.pointerBlockN] += n;
					this.pointerBlockPosition += 8;
					
				} else { // Caso contrario

					 int emptyBlockPosition = 31 - this.pointerBlockPosition; // Descobre quantas posicoes estao vagas no bloco 
					 this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << emptyBlockPosition; // Desloca para direita as posicoes vagas
					 int parteAlta = n >> (8 - emptyBlockPosition); // Retira a qtde certa da parte alta dos
					                                                  // 6 bits significativos de pair 
					 
					 int aux = (int)Math.pow(2, 8 - emptyBlockPosition);
					 int parteBaixa =  (n - (parteAlta*aux));
					 //Retira a qtde certa da parte baixa dos 8 bits significativos de pair
					 
					 this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteAlta;
					  
					 this.pointerBlockN++; 
					 this.pointerBlockPosition = (8 - emptyBlockPosition);
					  
					 this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteBaixa;
					 
				}
				
			} else {
				int n = 17;
				if (this.pointerBlockPosition <= 23) { // Caso o bloco ainda
					// caiba a palavra os 6
					// bits
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 8;
					this.vector[this.pointerBlockN] += n;
					this.pointerBlockPosition += 8;
				} else { // Caso contrario
				
					int emptyBlockPosition = 31 - this.pointerBlockPosition; // Descobre quantas posicoes estao vagas no bloco 
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << emptyBlockPosition; // Desloca para direita as posicoes vagas
					int parteAlta = n >> (8 - emptyBlockPosition); // Retira a qtde certa da parte alta dos
					                                  // 6 bits significativos de pair 
					int aux = (int)Math.pow(2, 8 - emptyBlockPosition);
					int parteBaixa =  (n - (parteAlta*aux));
					
					//Retira a qtde certa da parte baixa dos 6 bits significativos de pair
					
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteAlta;
					
					this.pointerBlockN++; 
					this.pointerBlockPosition = (8 - emptyBlockPosition);
					
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] + parteBaixa;
				
				
				}
			}
			numberOfBits += 8;
			i++;
		}
		
		return result;
	}

	public int reminderBits() {
		int numberOfBits = (31 * (this.pointerBlockN)) + this.pointerBlockPosition;
		
		try{
			switch (this.version) {
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
				if (this.pointerBlockPosition <= 27){
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 4;
					this.pointerBlockPosition+=4;
					
				}else{
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-this.pointerBlockPosition);
					
					this.pointerBlockN++;
					this.pointerBlockPosition=4-(31-this.pointerBlockPosition);
				}
				break;
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				if (this.pointerBlockPosition <= 24){
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 7;
					System.out.println("---this.pointerBlockPosition = " + this.pointerBlockPosition);
					this.pointerBlockPosition+=7;
				}else{
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-this.pointerBlockPosition);
					this.pointerBlockN++;
					this.pointerBlockPosition=7-(31-this.pointerBlockPosition);
				}
				break;
				
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
	
			case 28:
			case 29:
			case 30:
			case 31:
			case 32:
			case 33:
			case 34:
				if (this.pointerBlockPosition <= 28){
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 3;
					this.pointerBlockPosition+=3;
				}else{
					this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-this.pointerBlockPosition);
					this.pointerBlockN++;
					this.pointerBlockPosition=3-(31-this.pointerBlockPosition);
				}
				break;
				
			default:
				break;
			}
			return 1;
		}catch(Exception e){
			return -1;
		}
		
	}


	//Insert the error correction code block into the binary vector
	public int insertErrorCorrectionBytes(PolynomialVector ECCWB){
				
		int length = ECCWB.length();
		
		for (int i = 0; i <= length; i++) {
			System.out.println("i - " + i);
			System.out.println(this.pointerBlockN + " - " + this.pointerBlockPosition);
			System.out.println(ECCWB.getTerm(i));
			if (this.pointerBlockPosition < 23) { // Caso o bloco ainda caiba o ECCWB.getTerm(i) (tamanho 8 bits)
				
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << 8;
				
				this.vector[this.pointerBlockN] += ECCWB.getTerm(i);
				
				this.pointerBlockPosition += 8;
				// System.out.println("......................PBP="+this.pointerBlockPosition);
			} else { // Caso contrario

				int emptyBlockPosition = 31 - this.pointerBlockPosition; // Descobre quantas posicoes estao vagas no bloco
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << emptyBlockPosition; // Desloca
																											// para
																											// direita
																											// as
																											// posicoes
																											// vagas
				int parteAlta = ECCWB.getTerm(i) >> (8 - emptyBlockPosition); // Retira a qtde
																// certa da
																// parte alta
																// dos
				// 6 bits significativos de pair

				int aux = (int) Math.pow(2, 8 - emptyBlockPosition);
				int parteBaixa = (ECCWB.getTerm(i) - (parteAlta * aux));
				// Retira a qtde certa da parte baixa dos 8 bits significativos
				// de pair
				
				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN]
						+ parteAlta;
				


				//System.out.println(this.pointerBlockN + " - " + this.pointerBlockPosition);
				this.pointerBlockN++;
				this.pointerBlockPosition = (8 - emptyBlockPosition);

				this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN]
						+ parteBaixa;
			}
		}
		this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-pointerBlockPosition);
		
		for(int i=0; i<=this.pointerBlockN;i++ ){
			System.out.print(this.vector[i] + " | ");
		}
		
		return -1;
	}
	
	
	public int[] getVector() {
		return vector;
	}

	public void setVector(int[] vector) {
		this.vector = vector;
	}

	/**
	 * This methods return the ASCII value of character according with the
	 * QRCode table. And -1 in case of error.
	 * @param caracter char
	 * @return the ASCII value of character according with the QRCode table. And -1 in case of error.
	 */
	public int convertToASCIIValue(char caracter) {
		int result = -1;

		switch (caracter) {
		case '0':
			result = 0;
			break;
		case '1':
			result = 1;
			break;
		case '2':
			result = 2;
			break;
		case '3':
			result = 3;
			break;
		case '4':
			result = 4;
			break;
		case '5':
			result = 5;
			break;
		case '6':
			result = 6;
			break;
		case '7':
			result = 7;
			break;
		case '8':
			result = 8;
			break;
		case '9':
			result = 9;
			break;
		case 'a':
		case 'A':
			result = 10;
			break;
		case 'b':
		case 'B':
			result = 11;
			break;
		case 'c':
		case 'C':
			result = 12;
			break;
		case 'd':
		case 'D':
			result = 13;
			break;
		case 'e':
		case 'E':
			result = 14;
			break;
		case 'f':
		case 'F':
			result = 15;
			break;
		case 'g':
		case 'G':
			result = 16;
			break;
		case 'h':
		case 'H':
			result = 17;
			break;
		case 'i':
		case 'I':
			result = 18;
			break;
		case 'j':
		case 'J':
			result = 19;
			break;
		case 'k':
		case 'K':
			result = 20;
			break;
		case 'l':
		case 'L':
			result = 21;
			break;
		case 'm':
		case 'M':
			result = 22;
			break;
		case 'n':
		case 'N':
			result = 23;
			break;
		case 'o':
		case 'O':
			result = 24;
			break;
		case 'p':
		case 'P':
			result = 25;
			break;
		case 'q':
		case 'Q':
			result = 26;
			break;
		case 'r':
		case 'R':
			result = 27;
			break;
		case 's':
		case 'S':
			result = 28;
			break;
		case 't':
		case 'T':
			result = 29;
			break;
		case 'u':
		case 'U':
			result = 30;
			break;
		case 'v':
		case 'V':
			result = 31;
			break;
		case 'w':
		case 'W':
			result = 32;
			break;
		case 'x':
		case 'X':
			result = 33;
			break;
		case 'y':
		case 'Y':
			result = 34;
			break;
		case 'z':
		case 'Z':
			result = 35;
			break;
		case ' ':
			result = 36;
			break;
		case '$':
			result = 37;
			break;
		case '%':
			result = 38;
			break;
		case '*':
			result = 39;
			break;
		case '+':
			result = 40;
			break;
		case '-':
			result = 41;
			break;
		case '.':
			result = 42;
			break;
		case '/':
			result = 43;
			break;
		case ':':
			result = 44;
			break;

		default:
			result = -1;
			break;
		}

		return result;
	}

	public int[] adjustToGroupOf8Bits(){
		//int r[] = this.vector.clone();
		int length = this.vector.length;
		int r[] = new int[length];
		for(int i=0; i < length; i++){
			r[i]=this.vector[i];
		}
		r[this.pointerBlockN] = r[this.pointerBlockN] << (31-pointerBlockPosition);
		return r;
		//this.vector[this.pointerBlockN] = this.vector[this.pointerBlockN] << (31-pointerBlockPosition);
	}
	
	public int getNumberOfBits(){
		return ((31*this.pointerBlockN) + this.pointerBlockPosition);
	}
	
	public int getBitWise(int index){
		int aux;
		int numberOfBlock = index/31;
		int positionInBlock = index%31;
		aux = this.vector[numberOfBlock]>>(31-(1+positionInBlock));
				
		if ((aux & 1) == 1){
			//System.out.print("1");
			return 1;
		}else{
			//System.out.print("0");
			return 0;
		}
	}
	
	public String toString() {
		String result = "";
		String auxResult = "";
		int auxVector = 0;
		
		for (int i = 0; i <= this.pointerBlockN; i++) {
			auxVector = this.vector[i];
			auxResult = "";
			for (int j = 0; j < 31; j++) {
				
				if ((auxVector & 1) == 1) {
					auxResult = "1" + auxResult;
				} else {
					auxResult = "0" + auxResult;
				}
				auxVector = auxVector >> 1;
			}
			result += auxResult;
		}
				
		String aux = "";
		System.out.println();
		System.out.println("--" + result);
		
		for (int i = 0; i < result.length()-8; i+=8){
			System.out.println("----" + result.substring(i, i+8));
			aux = aux + result.substring(i, i+8) + "\n";
		}

		return aux;
	}

}
