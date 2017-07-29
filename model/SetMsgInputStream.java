package lab5.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * A customized DataInputStream
 * 
 * @author 			Amjit, Billy, Alex, Jared
 * Lab:				5b
 * File:			SetMsgInputStream.java
 */
public class SetMsgInputStream extends DataInputStream {

	/**
	 * creates a SetMsgInputStream from is
	 * @param is an input stream
	 */
	public SetMsgInputStream (InputStream is) {
		super(is);
	}


}
