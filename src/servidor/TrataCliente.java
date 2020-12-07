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
				byte[] transacao = new byte[2];
				entrada.read(transacao);
				byte tamanho = entrada.readByte();
				byte[] dados = new byte[tamanho];
				entrada.read(dados);
				
				byte[] protocolo = ArrayUtils.addAll(ArrayUtils.addAll(transacao, tamanho), dados);
				
				trataComunicacaoLeitura(protocolo);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void trataComunicacaoLeitura(byte[] b) throws IOException {
		byte tipoTransacao = (byte) ((b[1] >> 4) & 0x0F);
		short idTransacao = (short) ((b[0] << 4) + tipoTransacao);
		byte tipoDado = (byte) (b[1] & 0x0F);
		int tamanhoTipo = Util.getTamanhoTipo(tipoDado);
		int db = (b[3] << 8) + b[4];
		short offsetDb = (short) ((b[5] << 5) + ((b[6] >> 3) & 31));
		byte nBit = (byte) (b[6] & 07);
		
		//byte[] valorLido = {12, 65, 32, 18};
		if (tipoTransacao > 010) {
			byte[] valorLido = servidorCLP.read(DaveArea.DB, db, tamanhoTipo, offsetDb);
			enviaResposta(idTransacao, tipoDado, valorLido);
		} else {
			byte[] envio = Arrays.copyOfRange(b, 7, b.length);
			servidorCLP.write(DaveArea.DB, db, offsetDb, envio);
		}
		
		/*
		byte[] id = Arrays.copyOfRange(b, 0, 8);
		byte tipo = b[8];
		short db = ByteBuffer.wrap(Arrays.copyOfRange(b, 9, 11)).order(ByteOrder.BIG_ENDIAN).getShort();
		short offset = ByteBuffer.wrap(Arrays.copyOfRange(b, 11, 13)).order(ByteOrder.BIG_ENDIAN).getShort();
		byte[] bs = servidorCLP.read(DaveArea.DB, db, tipo, offset);
		enviaResposta(id, tipo, bs);
		*/
	}
	
	public void enviaResposta(short idTransacao, byte tipo, byte[] valor) throws IOException {
		byte[] envia = Util.protocoloResposta(idTransacao, tipo, valor);
		
		saida.write(envia);
		saida.flush();
		//System.out.println(Arrays.toString(envia));
	}
}
