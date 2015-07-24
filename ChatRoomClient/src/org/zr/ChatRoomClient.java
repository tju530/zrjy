package org.zr;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Leo Rabit on 2015/7/24.
 */


public class ChatRoomClient {
    private BufferedReader bufReader = null;
    private PrintStream printStream = null;
    private Scanner keyIn = null;
    private Socket socket = null;
    private final int PORT = 3002;
    private String flag = "";


    public ChatRoomClient() throws  IOException {
        String tips = "";
        String ip = JOptionPane.showInputDialog(tips + "input the server IP");
        String port = JOptionPane.showInputDialog(tips + "input the PORT");
        this.socket = new Socket(ip, Integer.valueOf(port));
        this.printStream = new PrintStream(socket.getOutputStream());
        this.bufReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.keyIn = new Scanner(System.in);
    }

    public void init()  {
        //log in
        try {
            String tip = "";
            while(true)  {
                String userName = JOptionPane.showInputDialog(tip + "input the user name");
                printStream.println(ChatRoomProtocol.USER_ROUND + userName + ChatRoomProtocol.USER_ROUND);
                flag = bufReader.readLine();
                if(flag.equals(ChatRoomProtocol.NAME_REP))  {
                    System.out.println("user has logged, uses an other user name");
                    continue;
                }
                else if (flag.equals(ChatRoomProtocol.LOGIN_SUCCESS))
                    break;
            }
            System.out.println("log in successful");
            new Thread(new ClientThread(bufReader)).start();
        }
        catch(UnknownHostException ex)    {
            System.out.println("canot find server ");
            closeRs();
        }
        catch(IOException e)    {
            System.out.println("connect error");
            System.out.println("log in again");
            closeRs();
        }
    }

    public void readAndSend(){
        while(keyIn.hasNextLine()) {
            String msg = keyIn.nextLine();
            if(msg.equals("")) {
                System.out.println("The Content Cannot be Null !");
                continue;
            }
            //private communicate
            if(msg.indexOf(":")>0 && msg.startsWith("//"))  {
                String userName = msg.substring(2).split(":")[0];
                String message = msg.substring(2).split(":")[1];
                printStream.println(ChatRoomProtocol.PRIVATE_ROUND + userName + ChatRoomProtocol.SPLIT_SIGN + message
                                    + ChatRoomProtocol.PRIVATE_ROUND);
            }
            else {
                printStream.println(ChatRoomProtocol.MSG_ROUND + msg + ChatRoomProtocol.MSG_ROUND);
            }
        }
    }

    //close streams
    public void closeRs()   {
        try {
            if( keyIn == null)
                keyIn.close();
            if( printStream == null)
                printStream.close();
            if( bufReader == null)
                bufReader.close();
            if( !socket.isClosed())
                socket.close();
            System.exit(0);
        }
        catch (IOException e)   {
            e.printStackTrace();
        }
    }


    public static void main(String []args) throws IOException {
        ChatRoomClient client = new ChatRoomClient();
        client.init();
        client.readAndSend();
    }

}
