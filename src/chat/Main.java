package chat;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
	private static String message = "";

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(9090);
			System.out.println("Server On");
			while (true) {
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
								System.out.println("<<" + str);
								message = str;
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
								if (message.equals("") || message == null) {
									continue;
								} else {

									System.out.println(">>" + message);
									pw.println(message);
									pw.flush();
									message = "";

								}
								if (message.equals("exit")) {
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
