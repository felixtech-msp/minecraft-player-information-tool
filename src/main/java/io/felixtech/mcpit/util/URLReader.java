package io.felixtech.mcpit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class URLReader extends Reader implements AutoCloseable {
    private StringBuilder builder;
    private final InputStreamReader isr;
    private final BufferedReader in;
    private boolean ready = false;

    /**
     * Creates a new character-stream reader whose critical sections will
     * synchronize on the reader itself.
     * @param url the URL from which to read
     * @throws java.io.IOException if an I/O error occurs
     */
    public URLReader(URL url) throws IOException {
        this(url, 5000);
    }

    /**
     * Creates a new character-stream reader whose critical sections will
     * synchronize on the reader itself.
     * @param url the URL from which to read
     * @param timeout the connection and read timeout in milliseconds
     * @throws java.io.IOException if an I/O error occurs
     */
    public URLReader(URL url, int timeout) throws IOException {
        super();
        URLConnection con = url.openConnection();
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);
        this.isr = new InputStreamReader(con.getInputStream());
        this.in = new BufferedReader(this.isr);
        ready = true;
    }

    /**
     * Reads characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     *
     * @param      cbuf  Destination buffer
     * @param      off   Offset at which to start storing characters
     * @param      len   Maximum number of characters to read
     *
     * @return     The number of characters read, or -1 if the end of the
     *             stream has been reached
     *
     * @exception  IOException  If an I/O error occurs
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        String all = this.readAll();
        if(all.length()-off > len) len = all.length()-off;
        if(len == 0) return -1;
        cbuf = all.substring(off, len).toCharArray();
        return cbuf.length;
    }

    /**
     * Closes the stream and releases any system resources associated with
     * it.  Once the stream has been closed, further read(), ready(),
     * mark(), reset(), or skip() invocations will throw an IOException.
     * Closing a previously closed stream has no effect.
     *
     * @exception  IOException  If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if(this.in != null)
            this.in.close();

        if(this.isr != null)
            this.isr.close();
    }

    /**
     * Tells whether this stream is ready to be read.
     *
     * @return True if the next read() is guaranteed not to block for input,
     * false otherwise.  Note that returning false does not guarantee that the
     * next read will block.
     */
    @Override
    public boolean ready() {
        return this.ready;
    }

    /**
     * Reads the complete content of the ressource specified by the URL.
     * @return the complete content of the ressource
     * @throws IOException if an I/O error occurs
     */
    public String readAll() throws IOException {
        builder = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            builder.append(inputLine);

        return builder.toString();
    }

    /**
     * Reads the complete content of the ressource specified by the URL.
     * @param url the URL from which to read
     * @return the complete content of the ressource
     * @throws IOException if an I/O error occurs
     */
    public static String readURL(URL url) throws IOException {
        String str;

        try (URLReader reader = new URLReader(url)) {
            str = reader.readAll();
        }

        return str;
    }
}
