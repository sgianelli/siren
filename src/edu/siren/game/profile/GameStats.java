package edu.siren.game.profile;

import java.io.Serializable;

public class GameStats implements Serializable {
	
	// Game Stats
	private int games;
	private int battlesWon;
	private int battlesLost;
	
	// Profile Stats
	private int health;
	private int attack;
	private int defense;
	private int special;
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
	
	
	
}
