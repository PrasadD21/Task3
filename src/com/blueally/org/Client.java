package com.blueally.org;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Socket {

    private static Client instance = null;
	

    
    public static Client init( String host, Integer port ) throws UnknownHostException, IOException
    {
        if ( Client.instance == null )
            Client.instance = new Client( host, port );
        return Client.instance;
    }



    private Client( String host, Integer port ) throws UnknownHostException, IOException
    {
        super( host, port );
    }

    public void sendFile( String filename ) throws FileNotFoundException, IOException
    {
        // The file object from the filename
        File file = new File( filename );

        StringBuilder exception_message = new StringBuilder();
        exception_message.append( "The File [" ).append( filename ).append( "] " );

        // Check if the file exists
        if ( !file.exists() )
            throw new FileNotFoundException( exception_message + "does not exists." );

        // Check if the file size is not empty
        if ( file.length() <= 0 )
            throw new IOException( exception_message + "has zero size." );

        // Save the filesize
        Long file_size = file.length();

        // Check if the filesize is something reasonable
        if ( file_size > Integer.MAX_VALUE )
            throw new IOException( exception_message + "is too big to be sent." );

        byte[] bytes = new byte[file_size.intValue()];

        FileInputStream fis = new FileInputStream( file );
        BufferedInputStream bis = new BufferedInputStream( fis );
        BufferedOutputStream bos = new BufferedOutputStream( this.getOutputStream() );

        int count;

        // Loop used to send the file in bytes group
        while ( ( count = bis.read( bytes ) ) > 0 )
        {
            bos.write( bytes, 0, count );
        }

        bos.flush();
        bos.close();
        fis.close();
        bis.close();
    }    
    
    
    public void sendMessage( String message ) throws IOException
    {
        OutputStream os = this.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter( os );
        BufferedWriter bw = new BufferedWriter( osw );

        bw.write( message );
        bw.flush();
    }

  
    public String getMessage() throws IOException
    {
        InputStream is = this.getInputStream();
        InputStreamReader isr = new InputStreamReader( is );
        BufferedReader br = new BufferedReader( isr );

        String message = br.readLine();

        return message;
    }
}
