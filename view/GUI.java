package lab5.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import lab5.*;
import lab5.model.BlockingQueue;
import lab5.model.Card;
import lab5.model.Player;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import sun.java2d.pipe.DrawImage;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import java.awt.ScrollPane;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ScrollPaneConstants;
/**
 * 
 * @author			Amjit, Billy, Alex, Jared
 * Lab 5
 * GUI.java
 * 
 * this is the GUI class that is most important part of the view
 * it consists of a JFrame that displays the active board to the user
 * also the scores and rules
 * 
 * supplies functionality for the game
*
 *
 */
public class GUI extends JFrame {

	private String playerName = "UNCHOOSABLE NAME";
	private JPanel contentPane;
	private int frameWidth = 1000;
	private int frameHeight = 750;
	final int xIncrement = 268;
	final int yIncrement = frameHeight;
	private static int maxCards = 18;
	private int picWidth=(frameWidth-205)/3;
	private int picHeight=frameHeight; //needs to be divided
	private JLabel[] pics = new JLabel[maxCards];
	private Stack<String> selectedFiles = new Stack<String>();
	private ArrayList<Integer> selectedPositions = new ArrayList<Integer>();  //the "i"'s 
	private static Set<String> playersScores = new HashSet<String>();
	private static BlockingQueue<Object> commandQ = new BlockingQueue<Object>(1000);
	private	JScrollPane scrollForPeople = new JScrollPane();
	private JList peopleList = new JList();
	private JButton noSets = new JButton("No Possible Sets");


	public boolean cleared = true;
	/**
	 * Create the panel.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, frameWidth, frameHeight);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		this.setVisible(true);

		scrollForPeople.setBounds(800, 10, 174, 324);
		contentPane.add(scrollForPeople);
		scrollForPeople.setViewportView(peopleList);

		noSets.setBounds(800, 678, 174, 23);
		contentPane.add(noSets);
		noSets.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int numSpaces = 0;
				for(int i=0;i<GUI.maxCards; ++i) {
					if (pics[i]==null){
						numSpaces++;
					}
				}
				commandQ.enqueue((byte)4);
				commandQ.enqueue((byte)numSpaces);
			}
		});

		JScrollPane scrollForRules = new JScrollPane();
		scrollForRules.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollForRules.setBounds(800, 340, 174, 332);
		contentPane.add(scrollForRules);
		JTextPane rulesPane = new JTextPane();
		scrollForRules.setViewportView(rulesPane);
		rulesPane.setText(stringFromFile("SetRules.txt"));
		
		playerName = JOptionPane.showInputDialog("Player Name?");
		while(this.playerName.equals("UNCHOOSABLE NAME")){}
		commandQ.enqueue((byte)0);
		commandQ.enqueue((byte)1);//need player name
		commandQ.enqueue((byte)5);

	}
	
/**
 * this method displays the cards that are in the deck
 * takes an array of strings(the filenames of the cards) and creates a new JLabel with the card image read from a file
 * then, mouse listeners are added to each Jlabel. Based on player interaction, commands are enqueued and sent to the server
 * @param cards
 */
	public void setCards(final String[] cards) {
		int numberOfCards=0;
		for (int k=0;k<cards.length;k++) {
			if(cards[k]!=null) {
				numberOfCards++;
			}
		}
		
		for( int i=0; i<cards.length;i++){
			try {
				if(cards[i]!=null) {
					if(pics[i]!=null) {
						this.remove(pics[i]);
					}
					final String cardFileName= cards[i];
					final JLabel picLabel = new JLabel(new ImageIcon(ImageIO.read(new File("SetCards",cardFileName)),cardFileName));
					picLabel.setBounds(((i%3)*xIncrement),((i/3)*(yIncrement/(numberOfCards/3))), picWidth, picHeight/(numberOfCards/3));
					getContentPane().add(picLabel);
					pics[i] = picLabel;
					final int cardPosition = i;
					picLabel.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent arg0) {
							if(cleared){
								if(!picLabel.isOpaque()){
									picLabel.setOpaque(true);
									picLabel.setBackground(Color.green);
									selectedFiles.add(cardFileName);
									selectedPositions.add(cardPosition);
								}
								else{
									picLabel.setOpaque(false);
									picLabel.setBackground(Color.white);
									selectedFiles.remove(cardFileName);
									for(int i = 0; i < selectedPositions.size();i++){
									if(selectedPositions.get(i) == cardPosition){
										selectedPositions.remove(i);
									}
									}
								}
								if(selectedFiles.size()==3){
									Card a =new Card(selectedFiles.pop());
									Card b =new Card(selectedFiles.pop());
									Card c =new Card(selectedFiles.pop());
									if (Card.checkSet(a,b,c)) {
										commandQ.enqueue((byte)2); //playername
										commandQ.enqueue(selectedPositions.get(0));
										commandQ.enqueue(selectedPositions.get(1));
										commandQ.enqueue(selectedPositions.get(2));
									
									cleared = false;
										for(int i : selectedPositions){
											pics[i].setOpaque(false);
											pics[i].setBackground(Color.white);
										}
									} else {
										//say the set was wrong, only to submitter
										JOptionPane.showMessageDialog(contentPane, " Not a set! \n -1 point", "WRONG!", JOptionPane.ERROR_MESSAGE);
										for(int i : selectedPositions){
											pics[i].setBackground(Color.red);
										}
										for(int i : selectedPositions){
											pics[i].setOpaque(false);
											pics[i].setBackground(Color.white);
										}
										commandQ.enqueue((byte)3); //need name
										selectedFiles.clear();
										selectedPositions.clear();
									}
									commandQ.enqueue((byte)5);
								}
							}
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		repaint();
	}
/**
 * replaces cards that have been claimed as a set
 * if there are more than 12 on the field and a set is claimed, the set is deleted and not replaced so the board size
 * goes back down towards 12
 * 
 * @param newCards
 * @param x
 * @param y
 * @param z
 */
	public  void replaceCards(Stack<String> newCards,int x, int y, int z){ //param is the 3 replacement cards sent back by the server, only gets called if a true set was found 
		if(!newCards.isEmpty()) {
		for (int i= 0; i<maxCards; i++) {
			if (i == x || i == y || i == z) {
				String cardFileName = newCards.pop();  //WHat if there's fewer than 3 cards left? (blank card, but where handled?)
				remove(pics[i]);
				JLabel picLabel = new JLabel();
				try {
					picLabel = new JLabel(new ImageIcon(ImageIO.read(new File("SetCards",cardFileName)),cardFileName));
				} catch (IOException e) {
					e.printStackTrace();
				}
				pics[i] = picLabel;
				pics[i].revalidate();
			}
		}
		} else {
			JLabel[] newPics = new JLabel[maxCards];
			int count = 0;
			for(int i=0;i<maxCards;i++) {
				if(i == x || i == y || i == z) {
					getContentPane().remove(pics[i]);
					pics[i]=null;
				}
				if(i != x && i != y && i != z) {
					newPics[count] = pics[i];
					count++;
				}
				
			}

			pics = newPics;
		}
		
		cleared = true;
		selectedFiles.clear();
		selectedPositions.clear();
		String[] cards = new String[maxCards];
		for (int k=0;k<maxCards;k++) {
			if(pics[k]!=null) {
				cards[k]=pics[k].getIcon().toString();
			}
		}
		setCards(cards);
		repaint();
	}
	/**
	 * called when 'no sets available' is clicked by a user
	 * it adds cards up to a limit of 18 on the board
	 * as the cards are added to the board, the view dynamically updates
	 * @param current
	 * @param newCards
	 */
	public void moreCards(int current, Stack<String> newCards){ //param is the 3 additions cards sent back by the server
		for(int i=0;i<maxCards;i++) {
			if (pics[i]==null) {
				try {
					String cardFileA = newCards.pop();
					String cardFileB = newCards.pop();
					String cardFileC = newCards.pop();
					pics[i] = new JLabel(new ImageIcon(ImageIO.read(new File("SetCards",cardFileA)),cardFileA));
					pics[i+1] = new JLabel(new ImageIcon(ImageIO.read(new File("SetCards",cardFileB)),cardFileB));
					pics[i+2] = new JLabel(new ImageIcon(ImageIO.read(new File("SetCards",cardFileC)),cardFileC));
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
		}
		String[] cards = new String[maxCards];
		for (int k=0;k<maxCards;k++) {
			if(pics[k]!=null) {
				cards[k]=pics[k].getIcon().toString();
			}
		}
		setCards(cards);

	}

/**
 * updates the players
 * @param newPlayers
 */
	public void updatePlayers(Set<String> newPlayers) {
		playersScores = newPlayers;
		peopleList.setListData(playersScores.toArray());
		peopleList.revalidate();
	}
	/**
	 * pop up message for end of game
	 * @param winner
	 */
	public void gameOver(String winner) {
		JOptionPane.showMessageDialog(contentPane, winner+" wins!", "Game Over", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * @return the command queue
	 */
	public BlockingQueue getCommands() {
		return this.commandQ;
	}

	public void setName(String name){
		this.playerName = name;
	}
	
	public String getName(){
		return this.playerName;
	}
	
	public Set getPlayersScores() {
		return this.playersScores;
	}
/**
 * returns a file line by line
 * @param filename
 * @return
 */
	public String stringFromFile(String filename) {
		try{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			String everything = sb.toString();
			return everything;
		}
		catch(IOException e){
			System.out.println("file name is wrong");
		}
		return "";
	}

}
