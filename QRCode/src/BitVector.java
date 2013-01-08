public class BitVector {

	private int vector[];
	private int offset;

	public BitVector(int length) {
		this.offset = 0;
		this.vector = new int[length];
	}

	public void increaseBlock(int num) {
	}
	
	public int[] getVector() {
		return vector;
	}

	public int getOffset() {
		return offset;
	}

	public void appendBit(boolean bit) {
		if (bit) {
			this.vector[this.offset >> 5] |= 1 << (this.offset & 0x1F);
		}
		this.offset++;
	}

	public void appendBits(int value, int numBits) {
		if (numBits < 0 || numBits > 32) {
			return;// TODO Exception Class
		}
		for (int numBitsLeft = numBits; numBitsLeft > 0; numBitsLeft--) {
			appendBit(((value >> (numBitsLeft - 1)) & 0x01) == 1);
		}
	}

	public boolean get(int i) {
		return (this.vector[i >> 5] & (1 << (i & 0x1F))) != 0;
	}
	
	public int getBitWise(int i) {
		if (get(i)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public int getNBits(int offset, int count) {
		int i = 0;
		int bits = 0;
		int bit;
		while(i<count){
			if(get(offset+i)){
				bit = 1;
			}else{
				bit = 0;
			}
			
			bits+=bit*Math.pow(2, count-i-1);
			
			i++;
		}
		return bits;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < this.offset; i++) {
			if ((i & 0x07) == 0) {
				result += "\n";
			}

			if (get(i)) {
				result += "1";
			} else {
				result += "0";
			}
		}
		return result;
	}
}
