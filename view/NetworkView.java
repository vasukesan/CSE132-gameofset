package lab5.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import lab5.model.BlockingQueue;
import lab5.model.Card;
import lab5.model.Player;
import lab5.model.SetGame;
import lab5.model.SetMsgInputStream;
import lab5.model.SetMsgOutputStream;
/**
 * The server
 * 
 * This manages all the clients and the model
 * 
 * @author 			Amjit, Billy, Alex, Jared
 * Lab:				5b
 * File:			NetworkView.java
 */
public class NetworkView implements Runnable {
	SetGame model;
	ServerSocket ss;
	Set<SetMsgOutputStream> outputStreams;

	/**
	 * Makes a new network view from the model and throws an IOException
	 * @param model the model of the game
	 * @throws IOException if the stream doesn't work
	 */
	public NetworkView(SetGame model) throws IOException {
		this.model = model;
		ss = new ServerSocket(10501);
		outputStreams = new HashSet<SetMsgOutputStream>();
	}

	/**
	 * Connects to the clients, puts them in new threads, and handles all of the protocal
	 */
	public void run() {
		Socket s = null;
		while(true) {
			try {
				s = ss.accept();
				final SetMsgInputStream smis = new SetMsgInputStream(new DataInputStream(s.getInputStream()));
				final SetMsgOutputStream smos = new SetMsgOutputStream(new DataOutputStream(s.getOutputStream()));
				outputStreams.add(smos);

				Thread t = new Thread(new Runnable() { public void run() {
					while(true) {
						try {
							byte b = smis.readByte();
							while(b!='?') {
								b = smis.readByte();
							}

							byte type = smis.readByte();
							//The "hello" command
							label:
							if(type==0) {
								smos.writeByte((byte)'!');
								smos.writeByte(0);
								int length = 0;
								for(int i=0;i<model.getDeck().getCurrentCards().length;i++) {
									if(model.getDeck().getCurrentCards()[i] !=null){
										length++;
									}
								}
								smos.writeShort(length);
								for(int i=0;i<length;i++) {
									smos.writeUTF(model.getDeck().getCurrentCards()[i]+"");
								}
							}
							// adds a player to the game
							if(type==1) {

								smis.readShort();
								model.addPlayer(smis.readUTF());
								for (SetMsgOutputStream ostream : outputStreams) {
									ostream.write1((byte)'!',(byte)1,(short)model.getPlayers().size(),model);
								}
							}
							//if new cards are needed because of a set being found
							if (type==2) { 
								smis.readShort();
								String player = smis.readUTF();
								for (Player p : model.getPlayers()) {
									if (p.getName().equals(player)){
										p.setScore(p.getScore()+1);
										break;
									}
								}
								boolean newCards;
								String cardA = "";
								String cardB = "";
								String cardC = "";
								int a = smis.readInt();
								int bb = smis.readInt();
								int c = smis.readInt();
								if(model.getDeck().getCurrentNumCards()<=model.getDeck().getInitialNumCards()){
									Stack<String> threeNewCards = model.getDeck().threeNewCards(a,bb,c);
									cardA = threeNewCards.pop();
									cardB = threeNewCards.pop();
									cardC = threeNewCards.pop();
									newCards = true;
								} else {
									model.getDeck().deleteThreeCards(a,bb,c);
									model.getDeck().shrinkCurrentCards();
									newCards = false;
								}


								for (SetMsgOutputStream ostream : outputStreams) {
									ostream.writeByte((byte)'!');
									ostream.writeByte(2);
									if(newCards) {
										ostream.writeByte(6);
										ostream.writeInt(a);
										ostream.writeInt(bb);
										ostream.writeInt(c);
										ostream.writeUTF(cardA);
										ostream.writeUTF(cardB);
										ostream.writeUTF(cardB);
									} else {
										ostream.writeByte(3);
										ostream.writeInt(a);
										ostream.writeInt(bb);
										ostream.writeInt(c);
									}

								}

							}
							//if score needs to be deducted
							if (type==3) { 
								smis.readShort();
								String player = smis.readUTF();
								for (Player p : model.getPlayers()) {
									if (p.getName().equals(player)){
										p.setScore(p.getScore()-1);
										break;
									}
								}
								for (SetMsgOutputStream ostream : outputStreams) {
									ostream.writeByte((byte)'!');
									ostream.writeByte(3);
								}
							}
							//if there are no sets and more cards are needed
							if(type == 4){
								smis.readShort();
								int spaceLeft = smis.readByte();
								if(model.getDeck().size()==0) {
									for (SetMsgOutputStream ostream : outputStreams) {
										ostream.writeByte(6);
										ostream.writeShort(1);
										ostream.writeUTF(model.getWinner());
									}
								}
								String cardA = "";
								String cardB = "";
								String cardC = "";
								boolean space;
								if(spaceLeft!=0) {
									cardA = model.getDeck().popCard();
									cardB = model.getDeck().popCard();
									cardC = model.getDeck().popCard();
									space = true;
								}else {
									space = false;
								}

								if(space) {
									for (SetMsgOutputStream ostream : outputStreams) {
										ostream.writeByte((byte)'!');
										ostream.writeByte(4);
										ostream.writeShort(18-spaceLeft);
										ostream.writeUTF(cardA);
										ostream.writeUTF(cardB);
										ostream.writeUTF(cardC);
									}
								} else {
									for (SetMsgOutputStream ostream : outputStreams) {
										ostream.writeByte((byte)'!');
										ostream.writeByte(4);
										ostream.writeShort(0);
										ostream.writeUTF("");
									}
								}

							}
							//updates number of players and change of score   (need to call toString of Player)
							if(type == 5) {  
								for (SetMsgOutputStream ostream : outputStreams) {
									ostream.write5((byte)'!',(byte)5,(short)model.getPlayers().size(),model);
								}
							}
						}catch (EOFException e) {
							// graceful termination on EOF
						} catch (IOException e) {
							System.exit(-1);
						}

					}
				}
				});
				t.start();

			}  catch (IOException e) {
				System.out.println("Problem opening DataInputStream or DataOutputStream.");
			}
		}

	}
}
