package me.valodd.chatserver.test;

import me.valodd.chatserver.network.BufferConnection;

public class Test {

	public Test() {
		testBufferConnection();
	}

	private void testBufferConnection() { // It Works !
		BufferConnection bc = new BufferConnection();
		bc.writeBoolean(true);
		bc.writeInt(1998);

		bc.writeChar('0');
		bc.writeChar('d');
		bc.writeChar('d');
		bc.writeChar('l');
		bc.writeChar('y');
		bc.writeChar('o');
		bc.writeChar('k');
		bc.writeChar('o');
		bc.writeDouble(4.2);
		bc.writeFloat(2.1f);
		bc.writeLong(4444l);
		bc.writeShort((short) 4);
		bc.writeString("Hellow World !");
		bc.writeString("ça va ?");

		System.out.println(bc.readBoolean());
		System.out.println(bc.readInt());

		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readChar());
		System.out.println(bc.readDouble());
		System.out.println(bc.readFloat());
		System.out.println(bc.readLong());
		System.out.println(bc.readShort());
		System.out.println(bc.readString());
		System.out.println(bc.readString());
	}
}
