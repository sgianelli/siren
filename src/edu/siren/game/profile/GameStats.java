package edu.siren.game.profile;

import java.io.Serializable;
import java.util.ArrayList;

import edu.siren.game.items.Item;

public class GameStats implements Serializable {
    private static final long serialVersionUID = 1746960259570862954L;
    
    // Game Stats
	private int games;
	private int battlesWon;
	private int battlesLost;
	private int coins;
	
	// Moves Allowed in Battle
	private int movesAllowed;
	
	// Profile Stats
	private int experience;
	private int health;
	private int strength;
	private int attack;
	private int defense;
	private int special;

    private ArrayList<Item> items = new ArrayList<Item>();
	
	// Constants for Game
	public static final int MAX_HEALTH = 100;
	public static final int DEFAULT_MOVES = 1;
	public static final int MAX_EXPERIENCE = 10;
	public static final int DEFAULT_EXPERIENCE = 1;
	public static final int DEFAULT_COINS = 100;
	public static final int DEFAULT_STRENGTH = 10;
	
	public GameStats() {

		// Set the Default Stats
		this.health = MAX_HEALTH;
		this.movesAllowed = DEFAULT_MOVES;
		this.experience = DEFAULT_EXPERIENCE;
		this.coins = DEFAULT_COINS;
		this.strength = DEFAULT_STRENGTH;
	}

	
	/**
	 * @return the games
	 */
	public int getGames() {
		return games;
	}
	/**
	 * @param games the games to set
	 */
	public void setGames(int games) {
		this.games = games;
	}
	/**
	 * @return the battlesWon
	 */
	public int getBattlesWon() {
		return battlesWon;
	}
	/**
	 * @param battlesWon the battlesWon to set
	 */
	public void setBattlesWon(int battlesWon) {
		this.battlesWon = battlesWon;
	}
	/**
	 * @return the battlesLost
	 */
	public int getBattlesLost() {
		return battlesLost;
	}
	/**
	 * @param battlesLost the battlesLost to set
	 */
	public void setBattlesLost(int battlesLost) {
		this.battlesLost = battlesLost;
	}
	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}
	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}
	/**
	 * @return the attack
	 */
	public int getAttack() {
		return attack;
	}
	/**
	 * @param attack the attack to set
	 */
	public void setAttack(int attack) {
		this.attack = attack;
	}
	/**
	 * @return the defense
	 */
	public int getDefense() {
		return defense;
	}
	/**
	 * @param defense the defense to set
	 */
	public void setDefense(int defense) {
		this.defense = defense;
	}
	/**
	 * @return the special
	 */
	public int getSpecial() {
		return special;
	}
	/**
	 * @param special the special to set
	 */
	public void setSpecial(int special) {
		this.special = special;
	}
	/**
	 * @return the movesAllowed
	 */
	public int getMovesAllowed() {
		return movesAllowed;
	}
	/**
	 * @param movesAllowed the movesAllowed to set
	 */
	public void setMovesAllowed(int movesAllowed) {
		this.movesAllowed = movesAllowed;
	}


	/**
	 * @return the experience
	 */
	public int getExperience() {
		return experience;
	}


	/**
	 * @param experience the experience to set
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}


	/**
	 * @return the coins
	 */
	public int getCoins() {
		return coins;
	}


	/**
	 * @param coins the coins to set
	 */
	public void setCoins(int coins) {
		this.coins = coins;
	}


	/**
	 * @return the strength
	 */
	public int getStrength() {
		return strength;
	}


	/**
	 * @param strength the strength to set
	 */
	public void setStrength(int strength) {
		this.strength = strength;
	}


    public void addExperience(int total) {
        this.experience += total;
    }


    public void addItem(Item item) {
        if (this.items == null) {
            this.items = new ArrayList<Item>();
        }
        this.items.add(item);
    }
    
    public ArrayList<Item> getItems() {
        return items;
    }
}
