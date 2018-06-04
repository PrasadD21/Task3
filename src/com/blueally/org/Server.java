package com.blueally.org;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ServerSocket {

    private static Server instance = null;
    private Socket socket = null;

    public static Server init( Integer port ) throws IOException
    {
        if ( Server.instance == null )
            Server.instance = new Server( port );
        return Server.instance;
    }

    
    private Server( Integer port ) throws IOException
    {
        super( port );

        
        while ( true )
            this.socket = this.accept();
    }

    
    public void sendMessage( String message ) throws IOException
    {
        OutputStream os = this.socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter( os );
        BufferedWriter bw = new BufferedWriter( osw );

        bw.write( message );

        bw.flush();
    }


public String getMessage() throws IOException
    {
        InputStream is = this.socket.getInputStream();
        InputStreamReader isr = new InputStreamReader( is );
        BufferedReader br = new BufferedReader( isr );
          //read each line
        String message = br.readLine();

        return message;
    }
}
