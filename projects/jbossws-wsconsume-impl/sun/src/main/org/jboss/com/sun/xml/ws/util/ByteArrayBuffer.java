/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package org.jboss.com.sun.xml.ws.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Read/write buffer that stores a sequence of bytes.
 *
 * <p>
 * It works in a way similar to {@link ByteArrayOutputStream} but
 * this class works better in the following ways:
 *
 * <ol>
 *  <li>no synchronization
 *  <li>offers a {@link #newInputStream()} that creates a new {@link InputStream}
 *      that won't cause buffer reallocation.
 *  <li>less parameter correctness checking
 *  <li>offers a {@link #write(InputStream)} method that reads the entirety of the
 *      given {@link InputStream} without using a temporary buffer.
 * </ol>
 *
 * @author Kohsuke Kawaguchi
 */
public final class ByteArrayBuffer extends OutputStream {
    /**
     * The buffer where data is stored.
     */
    private byte buf[];

    /**
     * The number of valid bytes in the buffer.
     */
    private int count;

    /**
     * Creates a new byte array output stream. The buffer capacity is
     * initially 32 bytes, though its size increases if necessary.
     */
    public ByteArrayBuffer() {
        this(32);
    }

    /**
     * Creates a new byte array output stream, with a buffer capacity of
     * the specified size, in bytes.
     *
     * @param size the initial size.
     * @throws IllegalArgumentException if size is negative.
     */
    public ByteArrayBuffer(int size) {
        if (size <= 0)
            throw new IllegalArgumentException();
        buf = new byte[size];
    }

    public ByteArrayBuffer(byte[] data) {
        this.buf = data;
    }

    /**
     * Reads all the data of the given {@link InputStream} and appends them
     * into this buffer.
     *
     * @throws IOException
     *      if the read operation fails with an {@link IOException}.
     */
    public void write(InputStream in) throws IOException {
        while(true) {
            int cap = buf.length-count;     // the remaining buffer space
            int sz = in.read(buf,count,cap);
            if(sz<0)    return;     // hit EOS
            count += sz;

            
            if(cap==sz)
                ensureCapacity(buf.length*2);   // buffer filled up.
        }
    }

    public void write(int b) {
        int newcount = count + 1;
        ensureCapacity(newcount);
        buf[count] = (byte) b;
        count = newcount;
    }

    public void write(byte b[], int off, int len) {
        int newcount = count + len;
        ensureCapacity(newcount);
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
    }

    private void ensureCapacity(int newcount) {
        if (newcount > buf.length) {
            byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    public void reset() {
        count = 0;
    }

    /**
     * Gets the <b>copy</b> of exact-size byte[] that represents the written data.
     *
     * <p>
     * Since this method needs to allocate a new byte[], this method will be costly.
     *
     * @deprecated
     *      this method causes a buffer reallocation. Use it only when
     *      you have to.
     */
    public byte toByteArray()[] {
        byte newbuf[] = new byte[count];
        System.arraycopy(buf, 0, newbuf, 0, count);
        return newbuf;
    }

    public int size() {
        return count;
    }

    public void close() {
    }

    /**
     * Creates a new {@link InputStream} that reads from this buffer.
     */
    public InputStream newInputStream() {
        return new ByteArrayInputStream(buf,0,count);
    }

    /**
     * Creates a new {@link InputStream} that reads a part of this bfufer.
     */
    public InputStream newInputStream(int start, int length) {
        return new ByteArrayInputStream(buf,start,length);
    }
}
