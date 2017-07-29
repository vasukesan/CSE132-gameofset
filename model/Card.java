package lab5.model;

/**
 * Represents a cards
 * 
 * This holds all information a card can have such as number of objects, color, shading, and shape
 * 
 * @author 			Amjit, Billy, Alex, Jared
 * Lab:				5b
 * File:			Deck.java
 */
public class Card {
	private String number, color, shading, shape;

	/**
	 * creates a new card using the parameters to determine color, number, shading, and shape
	 * @param number the number of objects on the card
	 * @param color the color of the objects on the card
	 * @param shading the shading of the objects on the card
	 * @param shape the shape of the objects on the card
	 */
	public Card(String number, String color, String shading, String shape) {
		this.number = number;
		this.color = color;
		this.shading = shading;
		this.shape = shape;
	}
	
	/**
	 * creates a new card based on the name of a card image
	 * @param filename the image name of a card which should contain all the features of the card
	 */
	public Card(String filename) {
		this.number = filename.charAt(0)+ "";
		this.color = filename.charAt(2) + "";
		this.shading = filename.charAt(4) + "";
		this.shape = filename.charAt(6) + "";
		
	}
	
	/**
	 * checks to see if the set between cards a,b, and c is acceptable
	 * @param a one card in the set
	 * @param b another card in the set
	 * @param c another card in the set
	 * @return true if it is a match; false if the cards don't match up
	 */
	public static boolean checkSet(Card a, Card b, Card c) {
		if (numHelp(a,b,c) && colorHelp(a,b,c) && shadingHelp(a,b,c) && shapeHelp(a,b,c) ) return true;
		return false;
	}
	
	/**
	 * checks to see if the matching of numbers between cards a,b, and c is acceptable
	 * @param a one card in the set
	 * @param b another card in the set
	 * @param c another card in the set
	 * @return true if it is a match; false if the cards don't match up
	 */
	private static boolean numHelp(Card a, Card b, Card c) {
		if (a.getNumber().equals(b.getNumber()) && b.getNumber().equals(c.getNumber())) return true;
		if (!(a.getNumber().equals(b.getNumber())) && !(b.getNumber().equals(c.getNumber())) && !(a.getNumber().equals(c.getNumber())) ) return true;
		return false;
	}
	
	/**
	 * checks to see if the matching of colors between cards a,b, and c is acceptable
	 * @param a one card in the set
	 * @param b another card in the set
	 * @param c another card in the set
	 * @return true if it is a match; false if the cards don't match up
	 */
	private static boolean	colorHelp(Card a, Card b, Card c) {
		if (a.getColor().equals(b.getColor()) && b.getColor().equals(c.getColor())) return true;
		if (!(a.getColor().equals(b.getColor())) && !(b.getColor().equals(c.getColor())) && !(a.getColor().equals(c.getColor())) ) return true;
		return false;
	}
	
	/**
	 * checks to see if the matching of shadings between cards a,b, and c is acceptable
	 * @param a one card in the set
	 * @param b another card in the set
	 * @param c another card in the set
	 * @return true if it is a match; false if the cards don't match up
	 */
	private static boolean	shadingHelp(Card a, Card b, Card c) {
		if (a.getShading().equals(b.getShading()) && b.getShading().equals(c.getShading())) return true;
		if (!(a.getShading().equals(b.getShading())) && !(b.getShading().equals(c.getShading())) && !(a.getShading().equals(c.getShading())) ) return true;
		return false;
	}
	
	/**
	 * checks to see if the matching of shapes between cards a,b, and c is acceptable
	 * @param a one card in the set
	 * @param b another card in the set
	 * @param c another card in the set
	 * @return true if it is a match; false if the cards don't match up
	 */
	private static boolean	shapeHelp(Card a, Card b, Card c) {
		if (a.getShape().equals(b.getShape()) && b.getShape().equals(c.getShape())) return true;
		if (!(a.getShape().equals(b.getShape())) && !(b.getShape().equals(c.getShape())) && !(a.getShape().equals(c.getShape())) ) return true;
		return false;
	}
	
	

	
	/**
	 * gets the number of objects on the card
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * gets the color of objects on the card
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * gets the shading of objects on the card 
	 * @return the shading
	 */
	public String getShading() {
		return shading;
	}

	/**
	 * gets the shape of objects on the card
	 * @return the shape
	 */
	public String getShape() {
		return shape;
	}

	/**
	 * returns the card as a string which corresponds to the image that 
	 * has the features of the specified card
	 */
	public String toString(){
		return number + "_" + color + "_" + shading + "_" + shape + ".jpg";
	}
	
	
}
