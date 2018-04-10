package Estimator;

public class Utilities {
	public static byte[] toByteArray(int value) {
		byte[] Result = new byte[4];
		Result[3] = (byte) ((value >>> (8 * 0)) & 0xFF);
		Result[2] = (byte) ((value >>> (8 * 1)) & 0xFF);
		Result[1] = (byte) ((value >>> (8 * 2)) & 0xFF);
		Result[0] = (byte) ((value >>> (8 * 3)) & 0xFF);
		return Result;
	}

	public static int fromByteArray(byte[] value, int start, int length) {
		int Return = 0;
		for (int i = start; i < start + length; i++) {
			Return = (Return << 8) + (value[i] & 0xff);
		}
		return Return;
	}
}
