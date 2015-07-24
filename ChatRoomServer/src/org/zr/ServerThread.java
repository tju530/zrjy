package org.zr;

import java.net.*;
import java.io.*;
import java.util.Arrays;

public class ServerThread  implements Runnable	{
	private Socket socket = null;
	private BufferedReader bufReader = null;
	private PrintStream printStream = null;
	
	public ServerThread(Socket socket)	throws IOException 	{
		this.socket = socket;
		this.bufReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.printStream = new PrintStream( socket.getOutputStream() );
	}
	
	public void run()	{
		String readContent;
		String flag = "";
		try{
				while((readContent = bufReader.readLine()) != null)	{
					System.out.println(readContent);
					//user log in
					if(readContent.startsWith(ChatRoomProtocol.USER_ROUND)&&
							readContent.endsWith(ChatRoomProtocol.USER_ROUND))	{
						String userName = getMsg(readContent);
						if(/*ChatRoomServer.clients.getKeyByValue(printStream)== null*/
								!ChatRoomServer.clients.map.containsKey(userName))	{
							ChatRoomServer.clients.put(userName, printStream);
							flag = ChatRoomProtocol.LOGIN_SUCCESS;
							printStream.println(flag);
							System.out.println(ChatRoomServer.clients.getKeyByValue(printStream) + " log in successful");
						}
						else	{
							flag = ChatRoomProtocol.NAME_REP;
							printStream.println(flag);
							System.out.println("user exits");
						}
					}
					//private communicate
					else if( readContent.startsWith(ChatRoomProtocol.PRIVATE_ROUND) && readContent.endsWith(ChatRoomProtocol.PRIVATE_ROUND))	{
						String tempContent = getMsg(readContent);
						System.out.println(tempContent);
						String  [] part = tempContent.split(ChatRoomProtocol.SPLIT_SIGN);
						String userName = part[0];
						String msg = part[1];
						ChatRoomServer.clients.map.get(userName).println(ChatRoomProtocol.PRIVATE_ROUND +
																		ChatRoomServer.clients.getKeyByValue(printStream) +
																		"  say to you :  " + msg + ChatRoomProtocol.PRIVATE_ROUND);
					}
					//public communicate
					else	{
						String tempContent = getMsg(readContent);
						for( PrintStream ps : ChatRoomServer.clients.getValue() )	{
							if(ps != printStream)	{
								ps.println(ChatRoomProtocol.MSG_ROUND + ChatRoomServer.clients.getKeyByValue(printStream) +
											" : " + tempContent + ChatRoomProtocol.MSG_ROUND );
							}
						}
					}

				}
		}
		catch(IOException e){
			//the PrintStream is error, delete the socket from the clients Map
			ChatRoomServer.clients.deleteByValue(printStream);
			try	{
				if(printStream != null)	{
					printStream.close();
				}
				if(bufReader != null)	{
					bufReader.close();
				}
				if(!socket.isClosed())	{
					socket.close();
				}
			}
			catch(IOException ioe)	{
				ioe.printStackTrace();
			}
		}
	}

	private String getMsg(String content)	{
		return content.substring(ChatRoomProtocol.PROTOCOL_SIZE, content.length()- ChatRoomProtocol.PROTOCOL_SIZE);
	}

}
