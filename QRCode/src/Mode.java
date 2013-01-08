public class Mode {

	public static final int NUMERIC = 1;// 1
	public static final int ALPHANUMERIC = 2;// 2
	public static final int EIGHT_BIT = 3;// 4
	public static final int KANJI = 4;// 8

	public static final int[][] numCharacterCountIndicator = { { 10, 12, 14 },
			{ 9, 11, 13 }, { 8, 16, 16 }, { 8, 10, 12 } };

	public final int[] characterCountBitsForVersions;
	
	public final int bits;

	public final int indicator;

	public Mode(int mode) {
		this.indicator = mode;
		this.bits = (int) Math.pow(2, (mode - 1));
		this.characterCountBitsForVersions = numCharacterCountIndicator[mode - 1];	}

	public int getBits() {
		return this.bits;
	}

	public int getIndicator() {
		return this.indicator;
	}

	public int[] getCharacterCountBitsForVersions() {
		return characterCountBitsForVersions;
	}

	public int getCharacterCountIndicator(int version) {
		if (version <= 9) {
			return getCharacterCountBitsForVersions()[0];
		} else if (version <= 26) {
			return getCharacterCountBitsForVersions()[1];
		} else {
			return getCharacterCountBitsForVersions()[2];
		}
	}

}
