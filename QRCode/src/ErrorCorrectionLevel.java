public class ErrorCorrectionLevel {

	public static final int L = 1;// 1
	public static final int M = 2;// 2
	public static final int Q = 3;// 3
	public static final int H = 4;// 4

	public final int bits;
	
	
	ErrorCorrectionLevel(int eccLevel) {
		this.bits = eccLevel;
	}

	public int getBits() {
		return bits;
	}

}
