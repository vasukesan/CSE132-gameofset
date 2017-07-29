package lab5.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * A customized DataOutputStream
 * 
 * @author 			Amjit, Billy, Alex, Jared
 * Lab:				5b
 * File:			SetMsgOutputStream.java
 */
public class SetMsgOutputStream extends DataOutputStream {
	
	/**
	 * creates a SetMsgOutputStream from os
	 * @param os an output stream
	 */
	public SetMsgOutputStream(OutputStream os) {
		super(os);
	}
	
	/**
	 * Correctly writes a number using the specified message formatting. params are obvious
	 * @param start
	 * @param type
	 * @param length
	 * @param payload
	 */
	public void writeNum(byte start, byte type, short length, byte payload ) {
		try {
			writeByte(start);
			writeByte(type);
			this.writeShort(length);
			writeByte(payload);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Correctly writes a String using the specified message formatting. params are obvious
	 * @param start
	 * @param type
	 * @param length
	 * @param payload
	 */
	public void writeString(byte start, byte type, short length, String payload) {
		try {
			writeByte(start);
			writeByte(type);
			this.writeShort(length);
			writeUTF(payload);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * writes the 0 type command with proper formatting. convenience method
	 * @param start
	 * @param type
	 * @param length
	 * @param payload
	 * @param model
	 */
	public void write0(byte start, byte type, short length, String payload,SetGame model){
		try {
			writeByte(start);
			writeByte(type);
			writeShort(length); //cards.getsize
			for (Card c : model.getDeck()) {
				writeUTF(c+"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * * writes the 1 type command with proper formatting. convenience method
	 * @param start
	 * @param type
	 * @param length
	 * @param model
	 */
	public void write1(byte start, byte type, short length,SetGame model){
		try {
			writeByte(start);
			writeByte(type);
			writeShort(length); //model.getplayers.size
			for (Player p : model.getPlayers()) {
				writeUTF(p.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * * writes the 2 type command with proper formatting. convenience method
	 * @param start
	 * @param type
	 * @param length
	 * @param model
	 */
	public void write2(byte start, byte type, short length, SetGame model) {
		try {
			writeByte(start);
			writeByte(type);
			writeShort(length);
			writeUTF(model.getDeck().popCard());
			writeUTF(model.getDeck().popCard());
			writeUTF(model.getDeck().popCard());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * * writes the 5 type command with proper formatting. convenience method
	 * @param start
	 * @param type
	 * @param length
	 * @param model
	 */
	public void write5(byte start, byte type, short length, SetGame model) {
		try {
			writeByte(start);
			writeByte(type);
			writeShort(model.getPlayers().size());
			for (Player p : model.getPlayers()) {
				writeUTF(p+"");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

