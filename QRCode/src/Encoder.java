
public class Encoder {

	public Encoder() {
		super();
	}
	
	public BitVector encode(QRCode qr) {
		Version version = qr.getVersion();
		Mode mode = qr.getMode();
		ErrorCorrectionLevel ECL = qr.getErrorCorrectionLevel();
		String content = qr.getData();
		
		int vectorLength = version.getMaximunDataCapacityBits()[ECL.getBits()-1];
		if (vectorLength % 32 == 0){
			vectorLength = vectorLength / 32;
		}else{
			vectorLength = (vectorLength / 32) + 1;
		}
		
		BitVector vector = new BitVector(vectorLength);

		appendMode(mode, vector);
		appendData(content, mode, version, vector);
		terminateVector(version, ECL, vector);
		PolynomialVector data = ReedSolomon.createMessagePolynomial(version.getMaximunDataCapacityBits()[ECL.getBits()-1], vector);
		System.out.println("data: " + data.toString());
		
		PolynomialVector ECCW = ReedSolomon.generateErrorCorrectionCode(data, version, ECL);
		System.out.println("ECCW: " + ECCW.toString());
		
		BitVector finalVector = interweaveErrorCorrectionCode(version, ECL, data, ECCW);
		reminderBits(version, finalVector);
				
		return finalVector;
	}

	public void appendMode(Mode mode, BitVector vector) {
		vector.appendBits(mode.getBits(), 4);

	}

	public void appendData(String data, Mode mode, Version version,
			BitVector vector) {
		vector.appendBits(data.length(),
				mode.getCharacterCountIndicator(version.getBits()));
		switch (mode.getIndicator()) {
		case Mode.NUMERIC:
			appendNumericBytes(data, vector);
			break;
		case Mode.ALPHANUMERIC:
			appendAlphanumericBytes(data, vector);
			break;
		case Mode.EIGHT_BIT:
			append8BitBytes(data, vector);
			break;
		case Mode.KANJI:
			appendKanjiBytes(data, vector);
			break;
		default:
			// Exception
		}

	}

	public void appendNumericBytes(String data, BitVector vector) {
		int length = data.length();
		int i = 0;
		while (i < length) {
			int num1 = data.charAt(i) - '0';
			if (i + 2 < length) {
				// Encode three numeric letters in ten bits.
				int num2 = data.charAt(i + 1) - '0';
				int num3 = data.charAt(i + 2) - '0';
				vector.appendBits(num1 * 100 + num2 * 10 + num3, 10);
				i += 3;
			} else if (i + 1 < length) {
				// Encode two numeric letters in seven bits.
				int num2 = data.charAt(i + 1) - '0';
				vector.appendBits(num1 * 10 + num2, 7);
				i += 2;
			} else {
				// Encode one numeric letter in four bits.
				vector.appendBits(num1, 4);
				i++;
			}
		}

	}

	public void appendAlphanumericBytes(String data, BitVector vector) {
		int length = data.length();
		int i = 0;
		while (i < length) {
			int char1 = convertToASCIIValue(data.charAt(i));
			if (char1 == -1) {
				// Exception
			}
			if (i + 1 < length) {
				int char2 = convertToASCIIValue(data.charAt(i + 1));
				if (char2 == -1) {
					// Exception
				}
				// Encode two alphanumeric letters in 11 bits.
				vector.appendBits(char1 * 45 + char2, 11);
				i += 2;
			} else {
				// Encode one alphanumeric letter in six bits.
				vector.appendBits(char1, 6);
				i++;
			}
		}
	}

	public void append8BitBytes(String data, BitVector vector) {
		// Not implemented yet
	}

	public void appendKanjiBytes(String data, BitVector vector) {
		// Not implemented yet
	}

	public void terminateVector(Version version, ErrorCorrectionLevel ECL, BitVector vector) {
		int numOfBits = vector.getOffset();
		int capacity = version.getMaximunDataCapacityBits()[ECL.getBits()-1];

		// Put the terminator (Session 8.4.8 of ISO/IEC 18004)
		switch (capacity - numOfBits) {
		case 0:
			break;
		case 1:
			vector.appendBits(0, 1);
			break;
		case 2:
			vector.appendBits(0, 2);
			break;
		case 3:
			vector.appendBits(0, 3);
			break;
		default:
			vector.appendBits(0, 4);
			break;
		}

		// Put the bit stream to codeword conversion (Session 8.4.9 of ISO/IEC
		// 18004)
		// Step 1: Padding bits shall be added after the final bit (least
		// significant bit) of the data stream.
		numOfBits = vector.getOffset();
		if ((numOfBits % 8) != 0) {
			vector.appendBits(0, 8 - (numOfBits % 8));
		}

		// Step 2: The message bit stream shall then be extended to fill the
		// data capacity of the symbol corresponding to the Version and Error
		// Correction Level by the addition of the Pad Codewords 11101100 and
		// 00010001 alternately
		int i = 0;
		numOfBits = vector.getOffset();
		while (numOfBits < capacity) {
			if ((i % 2) == 0) {
				int n = 236;
				vector.appendBits(n, 8);
			} else {
				int n = 17;
				vector.appendBits(n, 8);
			}
			numOfBits = vector.getOffset();
			i++;
		}

	}

	public BitVector interweaveErrorCorrectionCode(Version version,
			ErrorCorrectionLevel ECL, PolynomialVector data,
			PolynomialVector ECCWB) {

		int vectorLength = ((data.length()+1 + ECCWB.length()+1) * 8);
		if (vectorLength % 32 == 0){
			vectorLength = vectorLength / 32;
		}else{
			vectorLength = (vectorLength / 32) + 1;
		}
		BitVector result = new BitVector(vectorLength+1);//The "+1" gives a clearance to add the remainder bits
		
		
		int numOfBlocks1 = version.getDataCodewordsCharacteristic()[ECL
				.getBits()-1][0];
		int numOfBlocks2 = version.getDataCodewordsCharacteristic()[ECL
				.getBits()-1][2];

		
		if ((numOfBlocks1 == 1) && (numOfBlocks2 == 0)) {
			int totalNumOfBlocks = data.length(); 
			for (int i=totalNumOfBlocks; i>= 0; i--){
				result.appendBits(data.getTerm(i), 8);
			}
			
			totalNumOfBlocks = ECCWB.length();
			for (int i=totalNumOfBlocks; i>=0; i--){
				result.appendBits(ECCWB.getTerm(i), 8);
			}
		}else if (numOfBlocks2 == 0){
			int totalNumOfBlocks = data.length(); 
			int lengthOfBlocks = (totalNumOfBlocks/numOfBlocks1)+1;
			int counter = 0;
			
			
			for(int i=totalNumOfBlocks; counter < lengthOfBlocks; i--){
				for(int j = 0; j < numOfBlocks1; j++){
					result.appendBits(data.getTerm(i-(lengthOfBlocks*j)), 8);
				}
				counter++;
			}
			
			totalNumOfBlocks = ECCWB.length(); 
			lengthOfBlocks = version.getErrorCorrectionCodePerBlock()[ECL.getBits()-1];
			System.out.println(lengthOfBlocks);
			counter=0;
			for(int i=totalNumOfBlocks; counter < lengthOfBlocks; i--){
				for(int j = 0; j < numOfBlocks1; j++){
					result.appendBits(ECCWB.getTerm(i-(lengthOfBlocks*j)), 8);
				}
				counter++;
			}
			
		}else{
			int totalNumOfBlocks = data.length(); 
			int lengthOfBlocks1 = version.getDataCodewordsCharacteristic()[ECL.getBits()-1][1];
			int lengthOfBlocks2 = version.getDataCodewordsCharacteristic()[ECL.getBits()-1][3];
			int counter = 0;
			
			System.out.println("totalNumOfBlocks: "+totalNumOfBlocks);
			System.out.println("lengthOfBlocks: "+lengthOfBlocks1);
			
			for(int i=totalNumOfBlocks; counter < lengthOfBlocks1; i--){
				for(int j = 0; j < numOfBlocks1; j++){
					System.out.println((i-(lengthOfBlocks1*j))+" - " + data.getTerm(i-(lengthOfBlocks1*j)));
					result.appendBits(data.getTerm(i-(lengthOfBlocks1*j)), 8);
				}
				for(int j = 0; j < numOfBlocks2; j++){
					System.out.println((i-(lengthOfBlocks1*numOfBlocks1)-(lengthOfBlocks2*j))+" "+(i-(lengthOfBlocks2*j))+" - " + data.getTerm(i-(lengthOfBlocks1*numOfBlocks1)-(lengthOfBlocks2*j)));
					result.appendBits(data.getTerm(i-(lengthOfBlocks1*numOfBlocks1)-(lengthOfBlocks2*j)), 8);
				}
				counter++;
				System.out.println();
			}
			
			for(int i=totalNumOfBlocks; counter < lengthOfBlocks2; i--){
				for(int j = 0; j < numOfBlocks2; j++){
					System.out.println((lengthOfBlocks2 -(counter-lengthOfBlocks1+1))+" - " + (i-lengthOfBlocks1*(numOfBlocks1+1)-lengthOfBlocks2*(j+1)+counter+1)+" - "+data.getTerm(i-lengthOfBlocks1*(numOfBlocks1+1)-lengthOfBlocks2*(j+1)+counter+1));
					result.appendBits(data.getTerm(i-lengthOfBlocks1*(numOfBlocks1+1)-lengthOfBlocks2*(j+1)+counter+1), 8);
				}
				counter++;
			}
						
			
			System.out.println();
			totalNumOfBlocks = ECCWB.length(); 
			int lengthOfBlocks = version.getErrorCorrectionCodePerBlock()[ECL.getBits()-1];
			System.out.println("lengthOfBlocks: " + lengthOfBlocks);
			System.out.println();
			counter=0;
			for(int i=totalNumOfBlocks; counter < lengthOfBlocks; i--){
				for(int j = 0; j < numOfBlocks1+numOfBlocks2; j++){
					System.out.println((i-(lengthOfBlocks*j))+" - "+ECCWB.getTerm(i-(lengthOfBlocks*j)));
					result.appendBits(ECCWB.getTerm(i-(lengthOfBlocks*j)), 8);
				}
				System.out.println();
				counter++;
			}
			
		}

		return result;
	}

	public void reminderBits(Version version, BitVector vector){
		vector.appendBits(0, version.getRemainderBit());
	}
	
	/**
	 * This methods return the ASCII value of character according with the
	 * QRCode table. And -1 in case of error.
	 * 
	 * @param caracter
	 *            char
	 * @return the ASCII value of character according with the QRCode table. And
	 *         -1 in case of error.
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

}
