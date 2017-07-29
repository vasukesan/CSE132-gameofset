package lab5.client;

/*
 * TA:  Madeline Enright
 *  -5 cards duplicate
 *  -2 unused variables
 *  -5 commented out code
 * 	
 * 	188/20
 * 
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import lab5.*;
import lab5.model.SetMsgInputStream;
import lab5.model.SetMsgOutputStream;
import lab5.view.GUI;
import lab5.SetCards.*;
/**
 *  Client
 *   Player program for game of Set.
 *
 *   Author: Amjit, Billy, Jared, Alex
 *   Course: CSE 132
 *   Lab:    5
 */

public class Client {
	private static GUI frame;
	private String ip = "localhost";
	SetMsgInputStream smis;
	SetMsgOutputStream smos;
	Socket socket;

	public Client(){
		try {
			socket = new Socket("localhost", 10501);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * this runs the program and connects to the server
	 * deals with messages from server, updates frame and the like
	 * 
	 */
	public void run() {
		frame = new GUI();

		try{

			InetAddress adr = InetAddress.getByName(ip);
			smis = new SetMsgInputStream(new DataInputStream(socket.getInputStream()));
			smos = new SetMsgOutputStream(new DataOutputStream(socket.getOutputStream()));
			Thread t = new Thread(new Runnable(){ public void run() {
				try {
					while (true) {
					byte b = smis.readByte();
					while(b!='!') {
						b = smis.readByte();
					}
					int responseType = smis.readByte();
					if(responseType==0) {   //0 is initialize the cards
						int numCards = smis.readShort();
						String[] cards = new String[numCards];  //filenames
						for(int i = 0;i<numCards;i++){
							cards[i]=smis.readUTF();
						}
						frame.setCards(cards);
					}
					if (responseType==1){// 1 is initialize players command  (sends name of new client too)
						int countPlayers = smis.readShort();
						for(int i = 0; i<countPlayers;i++){
							frame.getPlayersScores().add(smis.readUTF());
						}
						frame.updatePlayers(frame.getPlayersScores());
					}
					if (responseType==2) { //if new cards are needed because of a set being found
						Stack<String> newCards = new Stack<String>();
						if(smis.readByte()==6) {
							int a = smis.readInt();
							int bb = smis.readInt();
							int c = smis.readInt();
							for(int i = 0;i<3;i++){  //sets are always three cards
								newCards.add(smis.readUTF());
							}						
							frame.replaceCards(newCards,a,bb,c);
						} else {
							int a = smis.readInt();
							int bb = smis.readInt();
							int c = smis.readInt();
							frame.replaceCards(newCards,a,bb,c);
						}

					}
					if (responseType==3) { //if score needs to be deducted - no change because no set
						//
					}
					if(responseType == 4){ //if there are no sets and more cards are needed
						Stack<String> newCards = new Stack<String>();
						int numCurrentCards = smis.readShort();
						if (numCurrentCards!=18) {
							for(int i = 0;i<3;i++) {  //always add three cards
								newCards.add(smis.readUTF());
							}						
							frame.moreCards(numCurrentCards, newCards);
						} else {
							smis.readUTF();
						}

					}
					if(responseType == 5) {  //updates number of players and change of score   (need to call toString of Player)
						int numPlayers = smis.readShort();
						Set<String> playersScores = new HashSet<String>();
						for(int i=0; i<numPlayers;i++){
							playersScores.add(smis.readUTF());
						}
						frame.updatePlayers(playersScores);
					}
					if(responseType == 6) {
						smis.readShort();
						frame.gameOver(smis.readUTF());
					}
					frame.repaint();

				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			}

		});
			t.start();
			while(true){
				try {
					Thread.sleep(1000);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();	
				}
				while(!frame.getCommands().isEmpty()){
					byte command = (Byte)frame.getCommands().dequeue();
					if (command==5) {
						smos.writeNum((byte)'?', command, (short)1,command);
					}
					if (command==0 || command ==1 || command==3){
						smos.writeString((byte)'?', command, (short)1,frame.getName());
					}
					if(command==2) {
						smos.writeByte((byte)'?');
						smos.writeByte(2);
						smos.writeShort(4);
						smos.writeUTF(frame.getName());
						smos.writeInt((Integer)frame.getCommands().dequeue());
						smos.writeInt((Integer)frame.getCommands().dequeue());
						smos.writeInt((Integer)frame.getCommands().dequeue());
					}
					if (command==4){
						smos.writeNum((byte)'?', command, (short)1,(Byte)frame.getCommands().dequeue());
					}
					frame.repaint();
				}
			}
	}
	catch(IOException e){
		e.printStackTrace();
	}
}

/**
 * gets the adapter
 * @return the adapter
 */

/**
 * Entry point for lab4 client.
 * @param args unused
 */
public static void main(String[] args) {
	Client c = new Client();
	c.run();
}

}
