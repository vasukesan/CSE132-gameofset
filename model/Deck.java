
package lab5.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Represents a Deck of Cards
 * 
 * This holds all the cards in a stack and creates the 81 cards for the game.
 * 
 * @author 			Amjit, Billy, Alex, Jared
 * Lab:				5b
 * File:			Deck.java
 */
public class Deck extends Stack<Card> {

	private String[] numbers = {"1","2","3"},
					 colors = {"R","G","B"},
					 shadings = {"F","O","S"},
					 shapes = {"D","O","S"};
	private int maxCards = 18;
	private int initialCards = 12;
	private Card[] currentCards = new Card[maxCards];
	private int currentNumCards;
	/**
	 * Adds all 81 variations of cards to the stack, shuffles them,
	 *  and sends out the initial number of cards for the deck
	 */
	public Deck() {
		for(String n: numbers){
			for(String c: colors){
				for(String shad: shadings){
					for(String shap: shapes){
						this.add(new Card(n,c,shad,shap));
					}
				}
			}
		}
		Collections.shuffle(this);
		for (int i=0;i<initialCards;i++) {
			popCard();
		}
	}

	/**
	 * Takes a card out of the deck and returns the card as a string and
	 * adds the card to the cards on the field
	 * @return the card removed from the deck as a string
	 */
	public String popCard() {
		Card c  = this.pop();
		for(int i=0;i<maxCards;i++){
			if (currentCards[i]==null) {
				currentCards[i] = c;
				break;
			}
		}
		return c+"";
	}
	
	/**
	 * Takes in the position of 3 new cards and deletes them from the current cards on the field
	 * and then returns the cards that are replacing those on the field.
	 * @param a position of card 1
	 * @param b position of card 2
	 * @param c position of card 3
	 * @return the replacing cards as a Stack of strings
	 */
	public Stack<String> threeNewCards(int a,int b, int c) {
		Stack<String> newCards = new Stack<String>();
		for (int i=0;i<maxCards;i++) {
			if (i==a || i==b || i==c) {
				currentCards[i] =null;
				popCard();
				newCards.add(currentCards[i]+"");
			}
		}
		return newCards;
	}
	
	/**
	 * Gets the current cards on the field
	 * @return the current cards on the field
	 */
	public Card[] getCurrentCards() {
		return currentCards;
	}
	
	/**
	 * gets the number of cards on the playing field
	 * @return int
	 */
	public int getCurrentNumCards() {
		int count = 0;
		for (int i=0;i<maxCards;i++) {
			if(currentCards[i]!=null) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * getter for max number of cards
	 * @return
	 */
	public int getMaxCards() {
		return maxCards;
	}
	
	/**
	 * get the initial number of cards
	 * @return
	 */
	public int getInitialNumCards(){
		return initialCards;
	}
	
	/**
	 * takes the current cards and shrinks the array to remove null spaces 
	 */
	public void shrinkCurrentCards() {
		Card[] newCurrentCards = new Card[maxCards];
		int count = 0;
		for (int i=0;i<this.currentCards.length;i++) {
			if(currentCards[i]!=null) {
				newCurrentCards[count]=currentCards[i];
				count++;
			} 
		}
		currentCards = newCurrentCards;
	}
	
	/**
	 * deletes three cards, used if cards don't need to replaced. 
	 * @param a position on board
	 * @param b position on board
	 * @param c position on board
	 */
	public void deleteThreeCards(int a,int b, int c) {
		for (int i=0;i<maxCards;i++) {
			if (i==a || i==b || i==c) {
				currentCards[i] =null;
			}
		}
	}
	

	
}
