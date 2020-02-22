package servidor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.factory.S7ConnectorFactory;

public class Servidor {
	private int porta;
	private List<Socket> clientes;
	private S7Connector conexao;
	
	public Servidor(int porta) {
		this.porta = porta;
		this.clientes = new ArrayList<Socket>();
	}
	
	public void executa() throws IOException {
		ServerSocket servidor = new ServerSocket(this.porta);
		System.out.println("Porta 12345 aberta!");
		/*conexao = S7ConnectorFactory
				.buildTCPConnector()
				.withHost("100.70.7.10")
				.build();
				*/
		
		while(true) {
			Socket cliente = servidor.accept();
			System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
			
			this.clientes.add(cliente);
			
			TrataCliente tc = new TrataCliente(cliente, conexao);
			new Thread(tc).start();
			
			//byte[] b = {10, 10, 22, 33};
			//cliente.getOutputStream().write(b);
		}
	}
	
}
