package lab5.model;

public class Player extends Actor {
	private String name; 	// name of the Player
	private int ID;			// player's ID
	private int score;   	// player's score in game
	
	/**
	 * @param name name of the Player
	 */
	public Player(String name, int ID) {
		super(name);
		this.name = name;
		this.ID = ID;
		score = 0;
	}
	
	/**
	 * Accessor for name.
	 * @return name of the Player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Accessor for score.
	 * @return score of the Player
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Accessor for player's ID.
	 * @return ID
	 */
	public synchronized int getID() {
		return ID;
	}
	
	/**
	 * Mutator for score.
	 */
	public void setScore(int score) {
		this.score = score;
		status(""+score);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name +":" + this.score;
	}

}
