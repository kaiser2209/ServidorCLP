package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util {
	public final static byte BYTE = 1, WORD = 2, FLOAT = 4;
	
	public static String bytesToString(byte[] bytes) {
		String resultado = "";
		
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] > 0) {
				resultado += (char) bytes[i];
			}
		}
		
		return resultado;
	}
	
	public static short bytesToShort(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
}
