package principal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import servidor.Servidor;

public class Main {
	public static void main(String[] args) throws IOException {
		new Servidor(12345).executa();
	}
}
