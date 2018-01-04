package net.apercova.io;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Allows to iterate over a {@link LineNumberReader} while keep line
 * number tracking. Works with java 1.5+
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @version 1.0 2017.08
 * @version 1.1 2018.01 Adding {@link #hasMoreLines()} and {@link #getNextLine()}
 * 
 * 
 * <pre>
 * Use:
 * {@code
 *  IterableLineNumberReader it = new IterableLineNumberReader(reader);
 *  while (it.hasMoreLines()){
 *      String nextLine = it.getNextLine();
 *      int lineNum = it.getLineNumber();
 *      System.out.printf("#[%d]-%s%n",lineNum, nextLine);
 *  }}
 *  </pre>
 */
public class IterableLineNumberReader extends LineNumberReader implements Iterator<String>, Iterable<String>{

    protected boolean readForward;
    protected String nextLine;
    protected List<Throwable> suppressed;

    public IterableLineNumberReader(Reader in) {
        super(in);
        readForward = true;
        suppressed = new LinkedList<Throwable>();
    }

    public IterableLineNumberReader(Reader in, int sz) {
        super(in, sz);
        readForward = true;
        suppressed = new LinkedList<Throwable>();
    }

    /**
     * Exception list caused by reading errors when iterating.
     * {@link Iterator#next()} does not declare a {@code throws} statement.
     * @return Suppressed exceptions list.
     */
    public List<Throwable> getSuppressed() {
        return suppressed;
    }

    /**
     * Returns an instance of this {@link IterableLineNumberReader} as {@link Iterator}.
     * @return Iterator.
     */
    public Iterator<String> iterator() {
        return this;
    }

    /**
     * Looks up next text line within underlying reader.
     */
    protected void readNextLine(){
        try {
            nextLine = readLine();
        }catch(IOException e){
            suppressed.add(e);
            nextLine = null;
        }
    }

    /**
     * Returns {@code true} if there's more lines to be read.
     * (In other words, returns {@code true} if {@link #getNextLine()} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if there's more lines to read
     */
    public boolean hasNext() {
        //look forward if next line is available
        readNextLine();
        readForward = false;
        return nextLine != null;
    }

    /**
     * Returns the next line read.
     *
     * @return Value of the next line read or null if there's no more lines to be read. 
     */
    @Override
    public String next(){
        if(readForward){
            //read next line
            readNextLine();
        }else{
            readForward = true;
        }
        return nextLine;
    }

    /**
     * Not supported.
     * @see Iterator#remove()
     * @throws UnsupportedOperationException Not supported.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if there's more lines to be read.
     * (In other words, returns {@code true} if {@link #getNextLine()} would
     * return an element rather than throwing an exception.)
     * Convenience alias for {@link #hasNext()}
     * @return {@code true} if there's more lines to be read
     */
    public boolean hasMoreLines() {
        return this.hasNext();
    }

    /**
     * Returns the next line read.
     * Convenience alias for {@link #next()}
     * @return Value of the next line read or null if there's no more lines to be read.
     */
    public String getNextLine() {
        return this.next();
    }

}
