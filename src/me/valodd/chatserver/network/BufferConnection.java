package me.valodd.chatserver.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class contain the buffer which will be sent over the Connection<br />
 * <br />
 * Created: 05 juil. 2017 (20:37:18);<br />
 * Last edit: 05 juil. 2017 (20:37:18);<br />
 * 
 * @author 0ddlyoko<br />
 *         <br />
 */
public class BufferConnection {
	private byte[] buff;
	private int sizebuff;
	private int currPointer;

	public BufferConnection() {
		buff = new byte[50];
		sizebuff = 0;
		currPointer = 0;
	}

	/**
	 * public BufferConnection: Main constructor.
	 *
	 */
	public BufferConnection(int size) {
		buff = new byte[size];
		sizebuff = 0;
		currPointer = 0;
	}

	/**
	 * public void allocateMore(): This method will rise the buff size
	 * 
	 */
	public void allocateMore(int size) {
		byte[] privbuff = new byte[buff.length + size];
		for (int i = 0; i < this.sizebuff; i++)
			privbuff[i] = buff[i];
		buff = privbuff;
	}

	public int getSizeBuff() {
		return sizebuff;
	}

	public BufferConnection writeBoolean(boolean bool) {
		writeByte((byte) (bool ? 1 : 0));
		return this;
	}

	public BufferConnection writeByte(byte b) {
		if (sizebuff >= buff.length)
			allocateMore(50);
		buff[sizebuff++] = b;
		return this;
	}

	public BufferConnection writeShort(short s) {
		writeByte((byte) ((s >> 8) & 0xFF));
		writeByte((byte) (s & 0xFF));
		return this;
	}

	public BufferConnection writeInt(int i) {
		writeByte((byte) ((i >>> 24) & 0xFF));
		writeByte((byte) ((i >>> 16) & 0xFF));
		writeByte((byte) ((i >>> 8) & 0xFF));
		writeByte((byte) (i & 0xFF));
		return this;
	}

	public BufferConnection writeLong(long l) {
		writeByte((byte) ((l >>> 56) & 0xFF));
		writeByte((byte) ((l >>> 48) & 0xFF));
		writeByte((byte) ((l >>> 40) & 0xFF));
		writeByte((byte) ((l >>> 32) & 0xFF));
		writeByte((byte) ((l >>> 24) & 0xFF));
		writeByte((byte) ((l >>> 16) & 0xFF));
		writeByte((byte) ((l >>> 8) & 0xFF));
		writeByte((byte) (l & 0xFF));
		return this;
	}

	public BufferConnection writeChar(char c) {
		writeByte((byte) ((c >>> 8) & 0xFF));
		writeByte((byte) (c & 0xFF));
		return this;
	}

	public BufferConnection writeChars(char[] cs) {
		for (char c : cs)
			writeChar(c);
		return this;
	}

	public BufferConnection writeString(String str) {
		char[] cs = str.toCharArray();
		writeChars(cs);
		writeChar('\0');
		return this;
	}

	public BufferConnection writeFloat(float f) {
		writeBytes(ByteBuffer.allocate(4).putFloat(f).array());
		return this;
	}

	public BufferConnection writeDouble(double d) {
		writeBytes(ByteBuffer.allocate(8).putDouble(d).array());
		return this;
	}

	public BufferConnection writeBytes(BufferConnection bc) {
		writeBytes(bc.getAllBytes());
		return this;
	}

	public BufferConnection writeBytes(BufferConnection bc, int ln) {
		for (int i = 0; i < ln; i++)
			writeByte(bc.readByte());
		return this;
	}

	public BufferConnection writeBytes(BufferConnection bc, int start, int ln) {
		bc.gotoPointer(start);
		writeBytes(bc, ln);
		return this;
	}

	public BufferConnection writeBytes(byte[] b) {
		for (int i = 0; i < b.length; i++)
			writeByte(b[i]);
		return this;
	}

	public BufferConnection writeBytes(byte[] b, int ln) {
		for (int i = 0; i < ln; i++)
			writeByte(b[i]);
		return this;
	}

	public BufferConnection writeBytes(byte[] b, int start, int ln) {
		for (int i = 0; i < ln; i++)
			writeByte(b[start + i]);
		return this;
	}

	public boolean readBoolean() {
		return readByte() == (byte) 1;
	}

	public byte readByte() {
		return buff[currPointer++];
	}

	public short readShort() {
		BufferConnection bc = readBytes(2);
		return (short) (((bc.readByte() & 0xFF) << 8) | (bc.readByte() & 0xFF));
	}

	public int readInt() {
		BufferConnection bc = readBytes(4);
		return ((bc.readByte() & 0xFF) << 24) | ((bc.readByte() & 0xFF) << 16) | ((bc.readByte() & 0xFF) << 8)
				| (bc.readByte() & 0xFF);
	}

	public long readLong() {
		BufferConnection bc = readBytes(8);
		return ((long) (bc.readByte() & 0xFF) << 56) | ((long) (bc.readByte() & 0xFF) << 48)
				| ((long) (bc.readByte() & 0xFF) << 40) | ((long) (bc.readByte() & 0xFF) << 32)
				| ((long) (bc.readByte() & 0xFF) << 24) | ((long) (bc.readByte() & 0xFF) << 16)
				| ((long) (bc.readByte() & 0xFF) << 8) | (long) (bc.readByte() & 0xFF);
	}

	public char readChar() {
		BufferConnection bc = readBytes(2);
		return (char) (((bc.readByte() & 0xFF) << 8) | (bc.readByte() & 0xFF));
	}

	public String readString() {
		StringBuilder sb = new StringBuilder();
		char c = readChar();
		while (c != 0) {
			sb.append(c);
			c = readChar();
		}
		return sb.toString();
	}

	public float readFloat() {
		return ByteBuffer.wrap(readBytes(4).getAllBytes()).order(ByteOrder.BIG_ENDIAN).getFloat();
	}

	public double readDouble() {
		return ByteBuffer.wrap(readBytes(8).getAllBytes()).order(ByteOrder.BIG_ENDIAN).getDouble();
	}

	public BufferConnection readBytes(int ln) {
		BufferConnection bc = new BufferConnection(ln);
		for (int i = 0; i < ln; i++)
			bc.writeByte(readByte());
		return bc;
	}

	public BufferConnection readBytes(int start, int ln) {
		gotoPointer(start);
		return readBytes(ln);
	}

	public BufferConnection skipBytes(int ln) {
		currPointer += ln;
		return this;
	}

	public BufferConnection backBytes(int ln) {
		currPointer -= ln;
		return this;
	}

	public int currPointer() {
		return currPointer;
	}

	public BufferConnection gotoPointer(int empl) {
		currPointer = empl;
		return this;
	}

	public byte[] getAllBytes() {
		if (sizebuff == buff.length)
			return buff;
		byte[] buff = new byte[sizebuff];
		gotoPointer(0);
		for (int i = 0; i < sizebuff; i++)
			buff[i] = this.buff[i];
		return buff;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		sb.append(Integer.toString(sizebuff)).append(") - (").append(Integer.toString(currPointer())).append(") - ");
		for (int i = 0; i < sizebuff; i++)
			sb.append(":" + Integer.toHexString(buff[i]) + ":");
		return sb.toString();
	}
}
