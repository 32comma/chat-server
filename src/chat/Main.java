package chat;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		ArrayList<Socket> socketList = new ArrayList<Socket>();
		try {
			serverSocket = new ServerSocket(9090);
			while (true) {
				Socket socket = serverSocket.accept();
				Thread th1 = new Thread() {

					@Override
					public void run() {
						InputStream is = null;
						try {
							is = socket.getInputStream();
							socketList.add(socket);
							InetAddress inetAddress = socket.getInetAddress();
							System.out.println(inetAddress.getHostAddress());
							Scanner sc = new Scanner(is);
							Thread.sleep(3000);
							while (sc.hasNext()) {
								String message = sc.nextLine();
								System.out.println();
								for (int i = 0; i < socketList.size(); i++) {
									System.out.println(socketList.get(i).getInetAddress().toString());
									PrintWriter pw = new PrintWriter(socketList.get(i).getOutputStream());
									// filter++
									pw.println(message);
									pw.flush();
								}
							}
							sc.close();
						} catch (Exception e) {

							e.printStackTrace();
						}
					}

				};
				th1.start();

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
