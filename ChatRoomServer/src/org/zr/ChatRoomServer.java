package org.zr;

import java.net.*;
import java.io.*;

public class ChatRoomServer {
	public static ChatRoomMap<String, PrintStream> clients = new ChatRoomMap<>();
	private static final int PORT = 3002;
	
	public static void main(String [] args)	{

		serverRun();
	}
	
	public static void serverRun()	{
		try{
			ServerSocket serverSocket = new ServerSocket(PORT);
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
