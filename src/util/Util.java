package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import tipos.Tipo;
import tipos.TipoNumero;

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
	
	public static int getTamanhoTipo(byte tipo) {
		if (tipo == Tipo.ENVIA_BIT.getCode() || tipo == Tipo.RECEBE_BIT.getCode() ||
				tipo == Tipo.ENVIA_BYTE.getCode() || tipo == Tipo.RECEBE_BYTE.getCode()) {
			return 1;
		} else if (tipo == Tipo.ENVIA_WORD.getCode() || tipo == Tipo.RECEBE_WORD.getCode() ||
				tipo == Tipo.ENVIA_INT.getCode() || tipo == Tipo.RECEBE_INT.getCode()) {
			return 2;
		} else if (tipo == Tipo.ENVIA_DWORD.getCode() || tipo == Tipo.RECEBE_DWORD.getCode() ||
				tipo == Tipo.ENVIA_DINT.getCode() || tipo == Tipo.RECEBE_DINT.getCode() ||
				tipo == Tipo.ENVIA_FLOAT.getCode() || tipo == Tipo.RECEBE_FLOAT.getCode()) {
			return 4;
		}
		
		return 0;
	}
	
	public static byte[] protocoloResposta(short idTransacao, byte tipo, byte[] valor) {
		byte[] transacao = new byte[2];
		transacao[0] = (byte) ((idTransacao >> 4) & 0x00FF);
		transacao[1] = (byte) (((idTransacao << 4) & 0x00F0) + (converteTipoParaTipoNumero(tipo) & 0x0F));
		byte tamanho = (byte) getTamanhoTipo(tipo);
		
		return ArrayUtils.addAll(ArrayUtils.addAll(transacao, tamanho), valor);
	}
	
	public static byte converteTipoParaTipoNumero(byte tipo) {
		if (tipo == Tipo.RECEBE_BIT.getCode()) {
			return (byte) TipoNumero.BIT.getCode();
		} else if (tipo == Tipo.RECEBE_BYTE.getCode()) {
			return (byte) TipoNumero.BYTE.getCode();
		} else if (tipo == Tipo.RECEBE_INT.getCode()) { 
			return (byte) TipoNumero.INT.getCode();
		} else if (tipo == Tipo.RECEBE_WORD.getCode()) {
			return (byte) TipoNumero.WORD.getCode(); 
		} else if (tipo == Tipo.RECEBE_DWORD.getCode()) {
			return (byte) TipoNumero.DWORD.getCode();
		} else if (tipo == Tipo.RECEBE_DINT.getCode()) {
			return (byte) TipoNumero.DINT.getCode();
		} else if (tipo == Tipo.RECEBE_FLOAT.getCode()) {
			return (byte) TipoNumero.REAL.getCode();
		}
		
		return 0x0;
	}
}
