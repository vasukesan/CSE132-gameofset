package lab5.model;

import java.io.EOFException;
import java.io.IOException;
import java.util.Stack;

public class PlayerHandler extends Thread {
	SetMsgInputStream smis;
	SetMsgOutputStream smos;
	SetGame model;
	public static BlockingQueue<Object> commands = new BlockingQueue<Object>(100);
	public static BlockingQueue<Object> recieved = new BlockingQueue<Object>(100);
	int spaceLeft;

	public PlayerHandler(SetMsgInputStream smis, SetMsgOutputStream smos, SetGame model) {
		this.smis = smis;
		this.smos = smos;
		this.model = model;
	}

	@Override
	public void run() {
		Thread t = new Thread(new Runnable() { public void run() {
			while(true) {
				try {
					byte b = smis.readByte();
					while(b!='?') {
						b = smis.readByte();
					}
					byte type = smis.readByte();
					System.out.println("server gets"+type);
					if(type==0) {
						commands.enqueue(0);
					}

					if(type==1) {
						smis.readShort();
						model.addPlayer(smis.readUTF());
						System.out.println("got past name?");
						commands.enqueue(1);
					}
					if (type==2) { //if new cards are needed because of a set being found
						smis.readShort();
						String player = smis.readUTF();
						for (Player p : model.getPlayers()) {
							if (p.getName().equals(player)){
								p.setScore(p.getScore()+1);
								break;
							}
						}
						commands.enqueue(2);
						

					}
					if (type==3) { //if score needs to be deducted
						System.out.println("server gets 3");
						smis.readShort();
						String player = smis.readUTF();
						for (Player p : model.getPlayers()) {
							if (p.getName().equals(player)){
								p.setScore(p.getScore()-1);
								break;
							}
						}
						commands.enqueue(3);
					}
					if(type == 4){ //if there are no sets and more cards are needed
						System.out.println("reads 4");
						smis.readShort();
						spaceLeft = smis.readByte();
						System.out.println(spaceLeft);
						commands.enqueue(4);
					}
					if(type == 5) {  //updates number of players and change of score   (need to call toString of Player)
						System.out.println("server reads 5");
						commands.enqueue(5);
					}
					Integer type2 = (Integer) recieved.dequeue();
					System.out.println(type2+"type2222222");
					if(type2 == 0){
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
					if(type2== 1){
						smos.write1((byte)'!',(byte)1,(short)model.getPlayers().size(),model);
					}
					if(type2== 2){
						smos.writeByte((byte)'!');
						smos.writeByte(2);
						Stack<String> threeNewCards = model.getDeck().threeNewCards(smis.readInt(),smis.readInt(),smis.readInt());
						for(int i=0;i<3;i++) {
							smos.writeUTF(threeNewCards.pop());
						}
						}
					if(type2== 3){
						smos.writeByte((byte)'!');
						smos.writeByte(3);
					}
					if(type2== 4){
						smos.writeByte((byte)'!');
						smos.writeByte(4);
						if(spaceLeft!=0) {
							smos.writeShort(18-spaceLeft);
							for(int i = 0;i<3;i++) {  //always add three cards
								smos.writeUTF(model.getDeck().popCard());
							} 
						}else {
							smos.writeShort(0);
							smos.writeUTF("");

						}
					}
					if(type2== 5){
						smos.write5((byte)'!',(byte)5,(short)model.getPlayers().size(),model);
					}
				}catch (EOFException e) {
					// graceful termination on EOF
				} catch (IOException e) {
					//System.out.println("Remote connection reset");
					System.exit(-1);
				}

			}
		}
		});
		t.start();


	}
	public Object pop(){
		return commands.dequeue();
	}
	public void enqueue(Object thing){
		recieved.enqueue(thing);
	}
}
