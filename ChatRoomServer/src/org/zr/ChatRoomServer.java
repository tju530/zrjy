package org.zr;

import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatRoomServer {
	public static ChatRoomMap<String, PrintStream> clients = new ChatRoomMap<>();
	private static final int PORT = 3002;
	private static ServerSocket serverSocket = null;
	
	public static void main(String [] args)	{

		new Thread(new Runnable(){
			public void run()	{
				Scanner keyIn = new Scanner(System.in);
				while( keyIn.hasNextLine() )	{
					String command = keyIn.nextLine();
					System.out.println(command);
					if(command.equals("exit"))	{
						try {
							serverSocket.close();
							System.out.println("server is closed !");
							System.exit(0);
						}
						catch(IOException e){
							System.out.println("the server was closed !");
						}
					}
				}
			}
		}).start();

		serverRun();
	}
	
	public static void serverRun()	{
		String port = JOptionPane.showInputDialog("input the port");
		try{
			serverSocket = new ServerSocket(Integer.valueOf(port));
			System.out.println("Server is ready. The port is " + port);
			while(true)	{
				Socket socket = serverSocket.accept();
				new Thread( new ServerThread(socket) ).start();
			}
		}
		catch(IOException e)	{
			e.printStackTrace();
		}
	}
}
