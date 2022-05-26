package chat;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
	
		try {
			serverSocket = new ServerSocket(9090);
			System.out.println("Server On");
			Socket socket = serverSocket.accept();
			System.out.println("client 연결완료");

			Thread listenThread = new Thread() {

				@Override
				public void run() {
					try {
						Scanner listenScanner = new Scanner(socket.getInputStream());
						while (listenScanner.hasNext()) {
							Thread.sleep(100);
							String str = listenScanner.nextLine();
							System.out.println("<<"+str);
							if (str.equals("exit")) {
								break;
							}
						}
						listenScanner.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			};
			listenThread.start();

			Thread sendThread = new Thread() {

				@Override
				public void run() {
					try {
						Scanner inputScanner = new Scanner(System.in);
						PrintWriter pw = new PrintWriter(socket.getOutputStream());
						while (true) {
							Thread.sleep(100);
							String str = inputScanner.nextLine();
							System.out.println(">>"+str);
							pw.println(str);
							pw.flush();
							if (str.equals("exit")) {
								inputScanner.close();
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			};
			sendThread.start();
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
