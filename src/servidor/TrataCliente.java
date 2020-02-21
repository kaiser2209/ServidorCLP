package servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;

import util.Util;

public class TrataCliente implements Runnable {
	private DataInputStream entrada;
	private DataOutputStream saida;
	private S7Connector servidorCLP;
	
	public TrataCliente(Socket cliente, S7Connector servidorCLP) throws IOException {
		this.entrada = new DataInputStream(cliente.getInputStream());
		this.saida = new DataOutputStream(cliente.getOutputStream());
		this.servidorCLP = servidorCLP;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				byte[] b = new byte[13];
				entrada.read(b);
				
				trataComunicacao(b);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void trataComunicacao(byte[] b) throws IOException {
		byte[] id = Arrays.copyOfRange(b, 0, 8);
		byte tipo = b[8];
		short db = ByteBuffer.wrap(Arrays.copyOfRange(b, 9, 11)).order(ByteOrder.BIG_ENDIAN).getShort();
		short offset = ByteBuffer.wrap(Arrays.copyOfRange(b, 11, 13)).order(ByteOrder.BIG_ENDIAN).getShort();
		//id = new String(ArrayUtils.removeAllOccurences(Arrays.copyOfRange(b, 0, 8), remove));
		byte[] bs = servidorCLP.read(DaveArea.DB, db, tipo, offset);
		//int valor = ByteBuffer.wrap(bs).getShort();
		//System.out.println(valor);
		enviaResposta(id, tipo, bs);
	}
	
	public void enviaResposta(byte[] id, byte tipo, byte[] valor) throws IOException {
		byte[] complemento = new byte[4 - valor.length];
		byte[] envia = new byte[13];
		for(int i = 0; i < complemento.length ; i++) {
			complemento[i] = 0;
		}
		
		byte[] novoValor = ArrayUtils.addAll(complemento, valor);
		
		for(int i = 0; i < envia.length; i++) {
			if (i < 8) {
				envia[i] = id[i];
			} else if (i < 9) {
				envia[i] = tipo;
			} else {
				envia[i] = novoValor[i - 9];
			}
		}
		
		saida.write(envia);
		saida.flush();
		//System.out.println(Arrays.toString(envia));
	}
}
