package org.zr;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Leo Rabit on 2015/7/24.
 */
public class ClientThread implements Runnable {
    private BufferedReader bufReader = null;

    public ClientThread(BufferedReader bufReader){
        this.bufReader = bufReader;
    }

    public void run(){
        String msg;
        try {
            while((msg = bufReader.readLine()) != null) {
                System.out.println(msg.substring(ChatRoomProtocol.PROTOCOL_SIZE, msg.length() - ChatRoomProtocol.PROTOCOL_SIZE));
            }
        }
        catch(IOException e)    {
            e.printStackTrace();
        }
        finally {
            try{
                if(bufReader != null)
                    bufReader.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
