package br.org.lavid.ppbginga.qrcode;

import java.io.IOException;

import br.org.lavid.ppbginga.qrcode.utils.*;

public class QRCode {
	
	private int version;
	private int mode;
	private int eccLevel; 
	private int capacityDataBits;
	private String content;
	private BinaryVector vector;
	
	private static int capacityTable[][] = { {152, 128, 104, 72}, {272, 224, 176, 128}, {440, 52, 272, 208}, 
											 {640, 512, 384, 288}, {864, 688, 496, 368}, {1088, 864, 608, 480}, 
											 {1248, 992, 704, 528}, {1552, 1232, 880, 688}, {1856, 1456, 1056, 800},
											 {2192, 1728, 1232, 976}, {2592, 2032, 144, 112}, {296, 232, 1648, 1264},
											 {3424, 2672, 1952, 144}, {3688, 292, 2088, 1576}, {4184, 332, 236, 1784}, 
											 {4712, 3624, 26, 2024}, {5176, 4056, 2936, 2264}, {5768, 4504, 3176, 2504}, 
											 {636, 5016, 356, 2728}, {6888, 5352, 388, 308}, {7456, 5712, 4096, 3248},
											 {8048, 6256, 4544, 3536}, {8752, 688, 4912, 3712}, {9392, 7312, 5312, 4112},
											 {10208, 8, 5744, 4304}, {1096, 8496, 6032, 4768}, {11744, 9024, 6464, 5024},
											 {12248, 9544, 6968, 5288}, {13048, 10136, 7288, 5608}, {1388, 10984, 788, 596},
											 {14744, 1164, 8264, 6344}, {1564, 12328, 892, 676}, {16568, 13048, 9368, 7208},
											 {17528, 138, 9848, 7688}, {18448, 14496, 10288, 7888}, {19472, 15312, 10832, 8432},
											 {20528, 15936, 11408, 8768}, {21616, 16816, 12016, 9136}, {22496, 17728, 12656, 9776},
											 {23648, 18672, 13328, 10208}};
	
	private static int capacityCharacterTable[] = {25,20,16,10,47,38,29,20,77,61,47,35,114,90,67,50,154,122,87,64,195,154,108,84,
												   224,178,125,93,279,221,157,122,335,262,189,143,395,311,221,174,468,366,259,200,
												   535,419,296,227,619,483,352,259,667,528,376,283,758,600,426,321,854,656,470,365,
												   938,734,531,408,1046,816,574,452,1153,909,644,493,1249,970,702,557,1352,1035,742,
												   587,146,1134,823,640,1588,1248,890,672,1704,1326,963,744,1853,1451,1041,779,199,
												   1542,1094,864,2132,1637,1172,910,2223,1732,1263,958,2369,1839,1322,1016,252,1994,
												   1429,108,2677,2113,1499,115,284,2238,1618,1226,3009,2369,17,1307,3183,2506,1787,
												   1394,3351,2632,1867,1431,3537,278,1966,153,3729,2894,2071,1591,3927,3054,2181,
												   1658,4087,322,2298,1774,4296,3391,242,1852};
	
	private static int eccWordBlockTable[][] = {{7, 10, 13, 17}, {10, 26, 22, 28}, {15, 26, 18, 22}, {20, 18, 26, 16}, 
												{26, 24, 18, 22}, {18, 16, 24, 28}, {20, 18, 18, 26}, {24, 22, 22, 26}, 
												{30, 22, 20, 24}, {18, 26, 24, 28}, {20, 30, 24, 28}, {24, 22, 26, 28}, 
												{26, 22, 24, 22}, {30, 24, 20, 24}, {22, 24, 30, 24}, {24, 28, 24, 30}, 
												{28, 28, 28, 28}, {30, 26, 28, 28}, {28, 26, 26, 26}, {28, 26, 30, 28}, 
												{28, 26, 28, 30}, {28, 28, 30, 24}, {30, 28, 30, 30}, {30, 28, 30, 30}, 
												{26, 28, 30, 30}, {28, 28, 28, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}, 
												{30, 28, 30, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}, 
												{30, 28, 30, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}, 
												{30, 28, 30, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}, {30, 28, 30, 30}};

	private static int blockDataTable[][][] = {{{1, 19, 0, 0}, {1, 16, 0, 0}, {1, 13, 0, 0}, {1, 9, 0, 0}}, 
											   {{1, 34, 0, 0}, {1, 28, 0, 0}, {1, 22, 0, 0}, {1, 16, 0, 0}},
											   {{1, 55, 0, 0}, {1, 44, 0, 0}, {2, 17, 0, 0}, {2, 13, 0, 0}},
											   {{1, 80, 0, 0}, {2, 32, 0, 0}, {2, 24, 0, 0}, {4, 9, 0, 0}},
											   {{1, 108, 0, 0}, {2, 43, 0, 0}, {2, 15, 2, 16}, {2, 11, 2, 12}},
											   {{2, 68, 0, 0}, {4, 27, 0, 0}, {4, 19, 0, 0}, {4, 15, 0, 0}},
											   {{2, 78, 0, 0}, {4, 31, 0, 0}, {2, 14, 4, 15}, {4, 13, 1, 14}},
											   {{2, 97, 0, 0}, {2, 38, 2, 39}, {4, 18, 2, 19}, {4, 14, 2, 15}},
											   {{2, 116, 0, 0}, {3, 36, 2, 37}, {4, 16, 4, 17}, {4, 12, 4, 13}},
											   {{2, 68, 2, 69}, {4, 43, 1, 44}, {6, 19, 2, 20}, {6, 15, 2, 16}},
											   {{4, 81, 0, 0}, {1, 50, 4, 51}, {4, 22, 4, 23}, {3, 12, 8, 13}}, 
											   {{2, 92, 2, 93}, {6, 36, 2, 37}, {4, 20, 6, 21}, {7, 14, 4, 15}},
											   {{4, 107, 0, 0}, {8, 37, 1, 38}, {8, 20, 4, 21}, {12, 11, 4, 12}},
											   {{3, 115, 1, 116}, {4, 40, 5, 41}, {11, 16, 5, 17}, {11, 12, 5, 13}},
											   {{5, 87, 1, 88}, {5, 41, 5, 42}, {5, 24, 7, 25}, {11, 12, 7, 13}},
											   {{5, 98, 1, 99}, {7, 45, 3, 46}, {15, 19, 2, 20}, {3, 15, 13, 16}},
											   {{1, 107, 5, 108}, {10, 46, 1, 47}, {1, 22, 15, 23}, {2, 14, 17, 15}},
											   {{5, 120, 1, 121}, {9, 43, 4, 44}, {17, 22, 1, 23}, {2, 14, 19, 15}},
											   {{3, 113, 4, 114}, {3, 44, 11, 45}, {17, 21, 4, 22}, {9, 13, 16, 14}},
											   {{3, 107, 5, 108}, {3, 41, 13, 42}, {15, 24, 5, 25}, {15, 15, 10, 16}},										
											   {{4, 116, 4, 117}, {17, 42, 0, 0}, {17, 22, 6, 23}, {19, 16, 6, 17}},
											   {{2, 111, 7, 112}, {17, 46, 0, 0}, {7, 24, 16, 25}, {34, 13, 0, 0}},
											   {{4, 121, 5, 122}, {4, 47, 14, 48}, {11, 24, 14, 25}, {16, 15, 14, 16}},
											   {{6, 117, 4, 118}, {6, 45, 14, 46}, {11, 24, 16, 25}, {30, 16, 2, 17}},
											   {{8, 106, 4, 107}, {8, 47, 13, 48}, {7, 24, 22, 25}, {22, 15, 13, 16}},
											   {{10, 114, 2, 115}, {19, 46, 4, 47}, {28, 22, 6, 23}, {33, 16, 4, 17}},
											   {{8, 122, 4, 123}, {22, 45, 3, 46}, {8, 23, 26, 24}, {12, 15, 28, 16}},
											   {{3, 117, 10, 118}, {3, 45, 23, 46}, {4, 24, 31, 25}, {11, 15, 31, 16}},
											   {{7, 116, 7, 117}, {21, 45, 7, 46}, {1, 23, 37, 24}, {19, 15, 26, 16}},
											   {{5, 115, 10, 116}, {19, 47, 10, 48}, {15, 24, 25, 25}, {23, 15, 25, 16}},
											   {{13, 115, 3, 116}, {2, 46, 29, 47}, {42, 24, 1, 25}, {23, 15, 28, 16}},
											   {{17, 115, 0, 0}, {10, 46, 23, 47}, {10, 24, 35, 25}, {19, 15, 35, 16}},
											   {{17, 115, 1, 116}, {14, 46, 21, 47}, {29, 24, 19, 25}, {11, 15, 46, 16}},
											   {{13, 115, 6, 116}, {14, 46, 23, 47}, {44, 24, 7, 25}, {59, 16, 1, 17}},
											   {{12, 121, 7, 122}, {12, 47, 26, 48}, {39, 24, 14, 25}, {22, 15, 41, 16}},
											   {{6, 121, 14, 122}, {6, 47, 34, 48}, {46, 24, 10, 25}, {2, 15, 64, 16}},
											   {{17, 122, 4, 123}, {29, 46, 14, 47}, {49, 24, 10, 25}, {24, 15, 46, 16}},
											   {{4, 122, 18, 123}, {13, 46, 32, 47}, {48, 24, 14, 25}, {42, 15, 32, 16}},
											   {{20, 117, 4, 118}, {40, 47, 7, 48}, {43, 24, 22, 25}, {10, 15, 67, 16}},
											   {{19, 118, 6, 119}, {18, 47, 31, 48}, {34, 24, 34, 25}, {20, 15, 61, 16}}};


	
	
	public static int getCapacityCharacterTable(int version, int eccLevel) {
		System.out.println(((version-1)*4)+" "+(eccLevel-1));
		return capacityCharacterTable[((version-1)*4)+(eccLevel-1)];
	}

	public int[] getBlockDataTable(int version, int eccLevel) {
		return blockDataTable[version - 1][eccLevel - 1];
	}

	public int getEccWordBlockTable(int version, int eccLevel) {
		return eccWordBlockTable[version - 1][eccLevel - 1];
	}

	public QRCode(int version, int eccLevel, int mode, String content) {
		super();
		this.version = version;
		this.mode = mode;
		this.eccLevel = eccLevel;
		this.content = content;
		setCapacityDataBits(getCapacityTable(version, eccLevel));
		int vectorLength = (((getBlockDataTable(version, eccLevel)[0]*getBlockDataTable(version, eccLevel)[1])+
						     (getBlockDataTable(version, eccLevel)[2]*getBlockDataTable(version, eccLevel)[3]))*8) +
						     ((getBlockDataTable(version, eccLevel)[0]+getBlockDataTable(version, eccLevel)[2])*
						       getEccWordBlockTable(version, eccLevel)*8);
		this.vector = new BinaryVector(mode, version, content, capacityDataBits, vectorLength);
	}
	
	public void init(){
		if(this.content.length() <= getCapacityCharacterTable(this.version, this.eccLevel)){
			
			this.vector.encodeModeIndicator();
			this.vector.encodeContentLength();
			this.vector.encodeContentData();
			this.vector.terminateBits();
			
			ReedSolomon rS = new ReedSolomon();
			PolynomialVector messagePolynomial = rS.createMessagePolynomial(this.capacityDataBits, this.vector.adjustToGroupOf8Bits());
			
			
			PolynomialVector ECCWB = rS.generateErrorCorrectionCode(messagePolynomial, version, eccLevel, getEccWordBlockTable(version, eccLevel),
																	getBlockDataTable(version, eccLevel)[0], getBlockDataTable(version, eccLevel)[1], 
																	getBlockDataTable(version, eccLevel)[2], getBlockDataTable(version, eccLevel)[3]);
			this.vector.insertErrorCorrectionBytes(ECCWB);
			this.vector.reminderBits();
		}else{		
			System.err.println("Conteudo da mensagem eh muito grande para essa versao nesse nivel de correcao de erro!");
		}
		
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public int getMode() {
		return mode;
	}


	public void setMode(int mode) {
		this.mode = mode;
	}


	public int getEccLevel() {
		return eccLevel;
	}


	public void setEccLevel(int eccLevel) {
		this.eccLevel = eccLevel;
	}


	public int getCapacityDataBits() {
		return capacityDataBits;
	}
	

	public void setCapacityDataBits(int capacityDataBits) {
		this.capacityDataBits = capacityDataBits;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public BinaryVector getVector() {
		return vector;
	}


	public void setVector(BinaryVector vector) {
		this.vector = vector;
	}


	public static int getCapacityTable(int version, int ECCLevel) {
		return capacityTable[version-1][ECCLevel-1];
	}
	

	public static int[][] getCapacityTable() {
		return capacityTable;
	}


	public static void setCapacityTable(int[][] capacityTable) {
		QRCode.capacityTable = capacityTable;
	}

}
