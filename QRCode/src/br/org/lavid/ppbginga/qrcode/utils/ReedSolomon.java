package br.org.lavid.ppbginga.qrcode.utils;

public class ReedSolomon {

	// Log Table for Galois Field 256 - index from 0 to 256 (length: 257)
	private static int logTable[] = { 1, 2, 4, 8, 16, 32, 64, 128, 29, 58, 116,
			232, 205, 135, 19, 38, 76, 152, 45, 90, 180, 117, 234, 201, 143, 3,
			6, 12, 24, 48, 96, 192, 157, 39, 78, 156, 37, 74, 148, 53, 106,
			212, 181, 119, 238, 193, 159, 35, 70, 140, 5, 10, 20, 40, 80, 160,
			93, 186, 105, 210, 185, 111, 222, 161, 95, 190, 97, 194, 153, 47,
			94, 188, 101, 202, 137, 15, 30, 60, 120, 240, 253, 231, 211, 187,
			107, 214, 177, 127, 254, 225, 223, 163, 91, 182, 113, 226, 217,
			175, 67, 134, 17, 34, 68, 136, 13, 26, 52, 104, 208, 189, 103, 206,
			129, 31, 62, 124, 248, 237, 199, 147, 59, 118, 236, 197, 151, 51,
			102, 204, 133, 23, 46, 92, 184, 109, 218, 169, 79, 158, 33, 66,
			132, 21, 42, 84, 168, 77, 154, 41, 82, 164, 85, 170, 73, 146, 57,
			114, 228, 213, 183, 115, 230, 209, 191, 99, 198, 145, 63, 126, 252,
			229, 215, 179, 123, 246, 241, 255, 227, 219, 171, 75, 150, 49, 98,
			196, 149, 55, 110, 220, 165, 87, 174, 65, 130, 25, 50, 100, 200,
			141, 7, 14, 28, 56, 112, 224, 221, 167, 83, 166, 81, 162, 89, 178,
			121, 242, 249, 239, 195, 155, 43, 86, 172, 69, 138, 9, 18, 36, 72,
			144, 61, 122, 244, 245, 247, 243, 251, 235, 203, 139, 11, 22, 44,
			88, 176, 125, 250, 233, 207, 131, 27, 54, 108, 216, 173, 71, 142, 1, 0 };

	// Antilog Table for Galois Field 256 - index from 1 to 256 (length: 256)
	private static int antilogTable[] = { 256, 0, 1, 25, 2, 50, 26, 198, 3, 223, 51,
			238, 27, 104, 199, 75, 4, 100, 224, 14, 52, 141, 239, 129, 28, 193,
			105, 248, 200, 8, 76, 113, 5, 138, 101, 47, 225, 36, 15, 33, 53,
			147, 142, 218, 240, 18, 130, 69, 29, 181, 194, 125, 106, 39, 249,
			185, 201, 154, 9, 120, 77, 228, 114, 166, 6, 191, 139, 98, 102,
			221, 48, 253, 226, 152, 37, 179, 16, 145, 34, 136, 54, 208, 148,
			206, 143, 150, 219, 189, 241, 210, 19, 92, 131, 56, 70, 64, 30, 66,
			182, 163, 195, 72, 126, 110, 107, 58, 40, 84, 250, 133, 186, 61,
			202, 94, 155, 159, 10, 21, 121, 43, 78, 212, 229, 172, 115, 243,
			167, 87, 7, 112, 192, 247, 140, 128, 99, 13, 103, 74, 222, 237, 49,
			197, 254, 24, 227, 165, 153, 119, 38, 184, 180, 124, 17, 68, 146,
			217, 35, 32, 137, 46, 55, 63, 209, 91, 149, 188, 207, 205, 144,
			135, 151, 178, 220, 252, 190, 97, 242, 86, 211, 171, 20, 42, 93,
			158, 132, 60, 57, 83, 71, 109, 65, 162, 31, 45, 67, 216, 183, 123,
			164, 118, 196, 23, 73, 236, 127, 12, 111, 246, 108, 161, 59, 82,
			41, 157, 85, 170, 251, 96, 134, 177, 187, 204, 62, 90, 203, 89, 95,
			176, 156, 169, 160, 81, 11, 245, 22, 235, 122, 117, 44, 215, 79,
			174, 213, 233, 230, 231, 173, 232, 116, 214, 244, 234, 168, 80, 88,
			175 };

	public int getLogTable(int exponent) {
		return logTable[exponent];
	}

	public int getAntilogTable(int integer) {
		return antilogTable[integer];
	}
	
	public ReedSolomon() {
	}

	public PolynomialVector createMessagePolynomial(int capacityDataBits, int vector[]) {

		/*int version = bv.getQr().getVersion();
		int mode = bv.getMode();
		int length = bv.getCapacityDatabits() / 8;*/
		

		//int current[] = bv.adjustToGroupOf8Bits();
				
		int length = capacityDataBits/8;
		
		PolynomialVector r = new PolynomialVector(length-1);
		
		int current[] = vector;
		int cont = length - 1;

		int pointPosition = 0;
		int pointBlock = 0;
		int groupOf8Bits = 0;
		int auxGroupOf8Bits = 0;
		int emptyPosition = 0;
		int readBits = 0;

		while ((readBits < capacityDataBits)&&(cont>=0)) {
			if (pointPosition == 0) {
				groupOf8Bits = current[pointBlock] >> 23;
				pointPosition = 8;
			} else if ((pointPosition >= 1) && (pointPosition <= 23)) {
				emptyPosition = 31 - pointPosition;
				auxGroupOf8Bits = current[pointBlock] >> emptyPosition; // 31-1
																		// Descobre
																		// valor
																		// da
																		// parte
																		// a
																		// esquerda
				groupOf8Bits = (int) Math.pow(2, emptyPosition);
				groupOf8Bits = current[pointBlock]
						- (auxGroupOf8Bits * groupOf8Bits); // Extrai valor da
															// parte direita
				groupOf8Bits = groupOf8Bits >> (31 - (8 + pointPosition)); // 31-(8+1)
																			// Extrai
																			// valor
																			// da
																			// parte
																			// direita
																			// por
																			// deslocamento
				pointPosition += 8;
			} else if ((pointPosition >= 24) && (pointPosition <= 30)) {
				// Elimina parte a esquerda
				emptyPosition = 31 - pointPosition;
				auxGroupOf8Bits = current[pointBlock] >> emptyPosition; // 31-24
																		// Descobre
																		// valor
																		// da
																		// parte
																		// a
																		// esquerda
				groupOf8Bits = (int) Math.pow(2, emptyPosition);
				auxGroupOf8Bits = current[pointBlock]
						- (auxGroupOf8Bits * groupOf8Bits); // Extrai valor da
															// parte direita
				groupOf8Bits = auxGroupOf8Bits
						* ((int) Math.pow(2, (8 - emptyPosition))); // 8-(31-24)
																	// Extrai
																	// valor da
																	// parte
																	// direito
																	// por
																	// deslocamento

				// Adiciona bit(s) que faltou(aram)
				pointBlock++; // Avanca ao proximo bloco
				auxGroupOf8Bits = current[pointBlock] >> (31 - (8 - emptyPosition)); // 31-(8-(31-24))
																						// Descobre
																						// bit(s)
																						// que
																						// faltou(aram)
				groupOf8Bits = groupOf8Bits + auxGroupOf8Bits; // Adiciona a
																// parte alta
				pointPosition = 8 - emptyPosition;
			} else if (pointPosition == 31) {
				pointBlock++;
				groupOf8Bits = current[pointBlock] >> 23;
				pointPosition = 8;
			}
			System.out.println("r.length: " + r.length() + ", groupOf8Bits: " + groupOf8Bits + ", cont: "+cont);
			r.setTerm(groupOf8Bits, cont);
			auxGroupOf8Bits = groupOf8Bits = emptyPosition = 0;
			readBits = (pointBlock * 31) + pointPosition;
			cont--;
			
		}
		return r;
	}

	public PolynomialVector createGeneratorPolynomial(int ECCWBlocks) {
		
		PolynomialVector first = new PolynomialVector(1);
		PolynomialVector multiplier = new PolynomialVector(1);

		// Inicializa polinomio auxiliar, inicialment 'first'
		first.setTerm(0, 1);
		first.setTerm(0, 0);

		// Cria e inicializa polinomio multiplicador (multiplier)
		multiplier.setTerm(0, 1);
		multiplier.setTerm(1, 0);

		// Criacao do polonimio ('aux') que auxiliara a construcao do
		// "generator polynomial"
		PolynomialVector aux = multiplyPolynomials(first, multiplier);
		

		// Cria generatorPolynomial
		for (int i = 1; i < ECCWBlocks - 1; i++) {
			
			multiplier.setTerm(i + 1, 0);
			aux = multiplyPolynomials(aux, multiplier);
			
		}
		
		if (aux.length() == ECCWBlocks) {
			
			return aux;
		} else {

			return null;
		}

	}

	public PolynomialVector multiplyPolynomials(PolynomialVector p,
			PolynomialVector q) {

		// Polinomio p(x) = ao + a1x + a2x² + a3x³ +...+ amxm, m = pLength
		// Polinomio q(x) = bo + b1x + b2x² + b3x³ +...+ bnxn, n = qLength
		// Polinomio resultante r(x) = p(x)·q(x) = co + c1x + c2x² + c3x³ +...+
		// c(m+n)x(m+n), m+n = rLength

		int pLength = p.length();
		int qLength = q.length();
		int rLength = pLength + qLength;
		PolynomialVector r = new PolynomialVector(rLength);

		
		int aux = 0;
		int auxLogPrev = 0, auxLogNext = 0;
		int auxXOR = 0;
		int auxAntilog = 0;
		boolean isFirst;

		// Calcula cada ck = a0bk + a1bk-1 + a2bk-2 + a3bk-3 +...+ ak-1b1 + akb0
		// onde k = 0...rlength
		for (int k = 0; k <= rLength; k++) {
			
			isFirst = true;

			for (int i = 0; i <= k; i++) {

				int j = k - i;
				

				// Ignora os indices (i ou j) nao existentes em p e q
				if ((i > pLength) || (j > qLength))
					continue;

				// Calcula o aibj
				aux = p.getTerm(i) + q.getTerm(j);
				
				// Muda o coeficiente (aux) quando maior que 255 usando
				// a formula (aux % 256) + floor(aux / 256)
				if (aux > 255) {
					aux = (aux % 256) + (aux / 256);
				}

				// Calcula o r indice k, usando as tabelas de 'log' e
				// 'antiLog', fazendo:
				// 1-Converte os termos de mesmo coeficiente da notacao
				// alfa para a inteira
				// 2-Faz o XOR entre eles
				// 3-Volta a notacao alfa o resultado do XOR

				if (isFirst) {
					auxAntilog = aux;
				} else {
					auxLogPrev = this.getLogTable(r.getTerm(k));// 1
					auxLogNext = this.getLogTable(aux);// 1
					auxXOR = auxLogPrev ^ auxLogNext;// 2
					auxAntilog = this.getAntilogTable(auxXOR);// 3

					
				}

				// Preenche o r indice k
				r.setTerm(auxAntilog, k);

				isFirst = false;

				

			}
		}

		
		return r;
	}

	public PolynomialVector convertoToIntegerNotation(PolynomialVector p) {
		int pLength = p.length();
		PolynomialVector r = new PolynomialVector(pLength);

		try {
			for (int i = 0; i <= pLength; i++) {
				r.setTerm(this.getLogTable(p.getTerm(i)), i);
			}
			return r;
		} catch (Exception e) {
			return null;
		}

	}

	public PolynomialVector convertoToAlphaNotation(PolynomialVector p) {
		int pLength = p.length();
		
		PolynomialVector r = new PolynomialVector(pLength);
		
		try {
			for (int i = 0; i <= pLength; i++) {
				
				r.setTerm(this.getAntilogTable(p.getTerm(i)), i);
			}
			
			return r;
		} catch (Exception e) {
			
			return null;
		}

	}

	public PolynomialVector multiplyPolynomialByCoefficient(PolynomialVector p,
			int coefficient) {
		int pLength = p.length();
		int newTerm = 0;
		PolynomialVector r = new PolynomialVector(pLength);

		try {
			
			for (int i = 0; i <= pLength; i++) {
				newTerm = p.getTerm(i) + coefficient;
				if (newTerm > 255) {
					newTerm = newTerm % 255;
				}
				r.setTerm(newTerm, i);
				
			}
			
			return r;
		} catch (Exception e) {
			return null;
		}

	}

	public PolynomialVector xorPolynomials(PolynomialVector p,
			PolynomialVector q) {
		try {
			if(p.length() >= q.length()){
				int length = p.length();
				PolynomialVector r = new PolynomialVector(length);
				int dif = p.length() - q.length();
				
				
				for(int i=length; i>=dif;i--){
					int aux = p.getTerm(i) ^ q.getTerm(i-dif);
					
					r.setTerm(aux, i);
				}
				for(int i=dif-1; i>=0;i--){
					
					r.setTerm(p.getTerm(i), i);
				}
				return r;
			}else{
				int length = q.length();
				PolynomialVector r = new PolynomialVector(length);
				int dif = q.length() - p.length();
				
				
				for(int i=length; i>=dif;i--){
					int aux = q.getTerm(i) ^ p.getTerm(i-dif);
					
					r.setTerm(aux, i);
				}
				for(int i=dif-1; i>=0;i--){
					
					r.setTerm(q.getTerm(i), i);
				}
				
				return r;
			}		
			
		} catch (Exception e) {
			return null;
		}

	}

	public PolynomialVector generateErrorCorrectionCode(PolynomialVector message, int version, int eccLevel, int eccWordBlock,
														int blockCount1, int numOfBlocks1, int blockCount2, int numOfBlocks2) {
		/*
		 * Mudar primeiro parametro do metodo para BinaryVector e fazer
		 * conversao dele para PolynomialVector antes do seguinte:
		 */
		
		System.out.print("          ");
	    for (int i=message.length(); i>=0; i--){
	    	System.out.print("" + message.getTerm(i)+ ",");
	    }
	    System.out.println();
	    
		
		PolynomialVector result;
		
		try {
			//Uses a unic Data Code Words Blocks length and only one time
			if ((blockCount1 == 1)&&(blockCount2==0)){
								
				//Ajeitar tamanho do codigo de correcao de erros nao  eh para ser numOfBlocks1 e sim eccWordBlock
				PolynomialVector mp;
				System.out.println(message.toString());
				
				/*if(eccWordBlock<numOfBlocks1) {
					//Message Polynomial is greater than generator Polynomial
					mp = new PolynomialVector(eccWordBlock);
					
					for (int i=0; i<=eccWordBlock; i++){
						mp.setTerm(message.getTerm(numOfBlocks1-i-1), (eccWordBlock-i));
						System.out.println("1 - " + mp.getTerm((eccWordBlock-i)));
					}
				
				}else{*/
					mp = new PolynomialVector(numOfBlocks1-1);
					
					for (int i=0; i<numOfBlocks1; i++){
						mp.setTerm(message.getTerm(numOfBlocks1-i-1), (numOfBlocks1-i-1));
						//System.out.println("2 - " + mp.getTerm((numOfBlocks1-i)));
					}
					
				//}
								
				//Create the generator polynomial to the correct version (adequate number of Code Words length)
				PolynomialVector gp = createGeneratorPolynomial(eccWordBlock);// Quando mudanca for feita alterar esse '13' tbm, 13 é o tam de 'message'
				System.out.println(gp.toString());
				
				PolynomialVector auxMultiply = null;
				PolynomialVector auxXOR = null;
				
				//Generate de error correction code block - init
				//1. Multiply the generator polynomial by the first coefficient of the message polynomial (at first case) or the result from XOR
				//2. XOR of the above polinomial and the message polynomial (at first case) or the result from XOR
				//3. Put out the first coefficient of the above polynomial, if it was 0
				//4. Convert the above polynomial to alpha-notation from Galois algorithm
				
				/*int offset = mp.length();
				for(int i=0; i<numOfBlocks1-1; i++){
					//auxMultiply = multiplyPolynomialByCoefficient(gp, message.getTerm(message.length())); //1
					
					//auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(message)); //2
					
					//message = getWithoutFirstNonZeroTerms(auxXOR); //3
					
					//message = convertoToAlphaNotation(message); //4
					System.out.println(" i: "+i);
					System.out.println(" mp.getTerm("+mp.length()+"): " + mp.getTerm(offset));
					System.out.println(" gp: "+gp.toString());
					System.out.println(" mp: "+mp.toString());
					auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(offset)); //1
					System.out.println(" auxMultiply: "+auxMultiply.toString());
					
					auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); //2
					System.out.println(" auxXOR:      "+auxXOR.toString());
						
					mp = getWithoutFirstNonZeroTerms(auxXOR); //3
									
					mp = convertoToAlphaNotation(mp);
					System.out.println(" mp(2):      "+mp.toString());
					System.out.println();
					offset--;
				}*/
				
				if (eccWordBlock<numOfBlocks1){
					int top = numOfBlocks1-1;
					
					for(int i=0; i<top; i++){
						//auxMultiply = multiplyPolynomialByCoefficient(gp, message.getTerm(message.length())); //1
						
						//auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(message)); //2
						
						//message = getWithoutFirstNonZeroTerms(auxXOR); //3
						
						//message = convertoToAlphaNotation(message); //4

						mp = convertoToAlphaNotation(mp);
						
						System.out.println(" i: "+i);
						System.out.println(" mp.getTerm("+mp.length()+"): " + mp.getTerm(mp.length()));
						System.out.println(" gp: "+gp.toString());
						System.out.println(" mp: "+mp.toString());
						auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(mp.length())); //1
						System.out.println(" auxMultiply: "+auxMultiply.toString());
						
						auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); //2
						System.out.println(" auxXOR:      "+auxXOR.toString());
							
						mp = getWithoutFirstNonZeroTerms(auxXOR); //3
											
						System.out.println(" mp(2):      "+mp.toString());
						System.out.println();
					}
				}else{
					int top = numOfBlocks1;
							for(int i=0; i<top; i++){
								//auxMultiply = multiplyPolynomialByCoefficient(gp, message.getTerm(message.length())); //1
								
								//auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(message)); //2
								
								//message = getWithoutFirstNonZeroTerms(auxXOR); //3
								
								//message = convertoToAlphaNotation(message); //4

								mp = convertoToAlphaNotation(mp);
								
								System.out.println(" i: "+i);
								System.out.println(" mp.getTerm("+mp.length()+"): " + mp.getTerm(mp.length()));
								System.out.println(" gp: "+gp.toString());
								System.out.println(" mp: "+mp.toString());
								auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(mp.length())); //1
								System.out.println(" auxMultiply: "+auxMultiply.toString());
								
								auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); //2
								System.out.println(" auxXOR:      "+auxXOR.toString());
									
								mp = getWithoutFirstNonZeroTerms(auxXOR); //3
													
								System.out.println(" mp(2):      "+mp.toString());
								System.out.println();
							}
				}
				
				
				/*
				auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(mp.length())); //1
				
				auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); //2
				
				mp = getWithoutFirstNonZeroTerms(auxXOR); //3*/
				//Generate de error correction code block - end
				
				int length = mp.length();
				result = new PolynomialVector(length);
				
				for(int i=length; i>=0; i--){
					result.setTerm(mp.getTerm(i), length-i);
					System.out.print(result.getTerm(length-i)+",");
				}
				System.out.println();
				
				return result;
			}//Uses a unic Data Code Words Blocks length and much than one time
			else if (blockCount2 == 0){
								
				result = new PolynomialVector((blockCount1 * eccWordBlock) - 1);
				int counterBlock = (numOfBlocks1 * blockCount1) - 1;
				int counterResult = 0;

				// Broke the message polynomial into blockCount1 parts and put it at 'mp'
				for (int k = 0; k < blockCount1; k++) {

					PolynomialVector mp = new PolynomialVector(numOfBlocks1 - 1);

					for (int i = 0; i < numOfBlocks1; i++) {
						mp.setTerm(message.getTerm(counterBlock - i), (numOfBlocks1 - i - 1));
					}

					PolynomialVector gp = createGeneratorPolynomial(eccWordBlock);
					PolynomialVector auxMultiply = null;
					PolynomialVector auxXOR = null;
					
					for (int i = 0; i < numOfBlocks1; i++) {
						mp = convertoToAlphaNotation(mp);
						auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(mp.length())); // 1
						auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); // 2
						mp = getWithoutFirstNonZeroTerms(auxXOR); // 3
					}
					System.out.println("Polinomio(f): " + convertoToAlphaNotation(mp).toString());
					System.out.println();

					for (int i = eccWordBlock - 1; i >= 0; i--) {
						result.setTerm(mp.getTerm(eccWordBlock - i - 1),
								counterResult + i);
					}

					counterBlock -= numOfBlocks1;
					counterResult += eccWordBlock;
				}

				System.out.println(result.toString());
				
				return result;
				
			}//Uses two Data Code Words Blocks length and much than one time
			else{
				result = new PolynomialVector(((blockCount1+blockCount2)*eccWordBlock)-1);
				
				int counterBlock = (numOfBlocks1*blockCount1)+(numOfBlocks2*blockCount2)-1;
				int counterResult = 0;
				
								
				//Broke the message polynomial into blockCount1 parts and put it at 'mp'
				for(int k=0; k<blockCount1; k++){
					
					PolynomialVector mp = new PolynomialVector(numOfBlocks1 - 1);

					for (int i = 0; i < numOfBlocks1; i++) {
						mp.setTerm(message.getTerm(counterBlock - i), (numOfBlocks1 - i - 1));
					}
					System.out.println(convertoToAlphaNotation(mp).toString());

					PolynomialVector gp = createGeneratorPolynomial(eccWordBlock);
					PolynomialVector auxMultiply = null;
					PolynomialVector auxXOR = null;
					
					for (int i = 0; i < numOfBlocks1; i++) {
						mp = convertoToAlphaNotation(mp);
						System.out.println("Polinomio: " + mp.toString());
						auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(mp.length())); // 1
						auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); // 2
						mp = getWithoutFirstNonZeroTerms(auxXOR); // 3
					}
					System.out.println("Polinomio(f): " + convertoToAlphaNotation(mp).toString());
					System.out.println();

					
					for (int i=eccWordBlock-1; i>=0; i--){
						result.setTerm(mp.getTerm(eccWordBlock-i-1), counterResult+i);
					}
					
					counterBlock-=numOfBlocks1;
					counterResult+=eccWordBlock;
				}
				
				//Broke the message polynomial into blockCount1 parts and put it at 'mp'
				for(int k=0; k<blockCount2; k++){
					
					PolynomialVector mp = new PolynomialVector(numOfBlocks2 - 1);

					for (int i = 0; i < numOfBlocks2; i++) {
						mp.setTerm(message.getTerm(counterBlock - i), (numOfBlocks2 - i - 1));
					}
					System.out.println(convertoToAlphaNotation(mp).toString());

					PolynomialVector gp = createGeneratorPolynomial(eccWordBlock);
					PolynomialVector auxMultiply = null;
					PolynomialVector auxXOR = null;
					
	
					System.out.println(gp.toString());
					for (int j = 0; j < numOfBlocks2; j++) {
						mp = convertoToAlphaNotation(mp);
						System.out.println("Polinomio: " + mp.toString());
						auxMultiply = multiplyPolynomialByCoefficient(gp, mp.getTerm(mp.length())); // 1
						auxXOR = xorPolynomials(convertoToIntegerNotation(auxMultiply), convertoToIntegerNotation(mp)); // 2
						mp = getWithoutFirstNonZeroTerms(auxXOR); // 3
					}
					System.out.println("Polinomio(f): " + convertoToAlphaNotation(mp).toString());
					System.out.println();
					
					for (int i=eccWordBlock-1; i>=0; i--){
						result.setTerm(mp.getTerm(eccWordBlock-i-1), counterResult+i);
					}
					
					counterBlock-=numOfBlocks2;
					counterResult+=eccWordBlock;
				}
				
				for(int i=0; i<=result.length();i++){
					System.out.print(result.getTerm(i)+",");
				}
				
				System.out.println();
				
				return result;
			}

		} catch (Exception e) {
			return null;
		}
	}

	public PolynomialVector getNonZeroTerms(PolynomialVector p) {
		int pLength = p.length();
		int counter = 0;

		try {
			for (int i = 0; i <= pLength; i++) {
				if (p.getTerm(i) != 0) {
					counter++;
				}
			}
			PolynomialVector r = new PolynomialVector(counter - 1);
			int rIndex = 0;
			for (int i = 0; i <= pLength; i++) {
				if (p.getTerm(i) != 0) {
					r.setTerm(p.getTerm(i), rIndex);
					rIndex++;
				}
			}
			
			return r;
		} catch (Exception e) {
			return null;
		}
	}

	public PolynomialVector getWithoutFirstNonZeroTerms(PolynomialVector p) {
		try {
			int pLength = p.length();
			int numOffZero = pLength;
			while(p.getTerm(numOffZero)==0){
				numOffZero--;
			}
			
			PolynomialVector r = new PolynomialVector(numOffZero);
			
			for(int i=numOffZero; i>=0; i--){
				r.setTerm(p.getTerm(i),i);
			}
			
			if (numOffZero!=0){
				return r;
			}else{
				return p;
			}
			/*
			if (p.getTerm(pLength) == 0) {
				PolynomialVector r = new PolynomialVector(pLength - 1);

				for (int i = 0; i < pLength; i++) {
					r.setTerm(p.getTerm(i), i);
				}
				return r;
			} else {
				return p;
			}*/
		} catch (Exception e) {
			return null;
		}

	}

}
