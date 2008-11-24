package com.weltevree.ego.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

	private static int GROUP_COUNT = 12;

	private static Pattern pattern;

	static {
		String patCapGN = "7.*?\\[(.*?)\\]"; // Captures the game number
		String patCapWP = ".*?([^ ].*?) "; // Captures white player
		String patCapWR = ".*?\\[.*?([^ ].*?)\\] vs\\."; // Captur white rank
		String patCapBP = ".*?([^ ].*?) "; // Captures black player
		String patCapBR = ".*?\\[.*?([^ ].*?)\\].*?\\("; // Captures black
		// rank
		String patCapMV = "(.*?) *?"; // Captures move
		String patCapBS = "([^ ]*?) *?"; // Captures board size
		String patCapHN = "([^ ]*?) *?"; // Captures hand
		String patCapKO = "([^ ]*?) *?"; // Captures Komi
		String patCapBY = "([^ ]*?) *?"; // Captures Byomi
		String patCapFR = "([^ ]*?)\\)"; // Captures rated game & game type
		String patCapOB = ".*?\\( *?(.*?)\\)"; // Captures observers

		String pat = patCapGN + patCapWP + patCapWR + patCapBP + patCapBR
				+ patCapMV + patCapBS + patCapHN + patCapKO + patCapBY
				+ patCapFR + patCapOB;

		pattern = Pattern.compile(pat);
	}

	private Player blackPlayer;

	private int boardSize;

	private int byoyomi;

	private int gameNumber;

	private String gameType;

	private int handicap;

	private float komi;

	private int moveNumber;

	private int observers;

	private String rated;

	private Player whitePlayer;

	private GoServer goServer;

	private ArrayList<BoardPoint> boardPoints = new ArrayList<BoardPoint>(
			getBoardSize() * getBoardSize());

	public Game(GoServer goServer, String gameString) {
		Matcher m = pattern.matcher(gameString);
		m.find();

		if (m.groupCount() != GROUP_COUNT)
			throw new RuntimeException("Invalid gameString: " + gameString);

		try {
			setGameNumber(Integer.parseInt(m.group(1).trim()));
			setWhitePlayer(new Player(m.group(2), m.group(3)));
			setBlackPlayer(new Player(m.group(4), m.group(5)));
			setMoveNumber(Integer.parseInt(m.group(6).trim()));
			setBoardSize(Integer.parseInt(m.group(7).trim()));
			setHandicap(Integer.parseInt(m.group(8).trim()));
			setKomi(Float.parseFloat(m.group(9).trim()));
			setByoyomi(Integer.parseInt(m.group(10).trim()));
			if (m.group(11).length() == 1) {
				setRated(" ");
				setGameType(m.group(11).substring(0, 1));
			} else {
				setRated(m.group(11).substring(0, 1));
				setGameType(m.group(11).substring(1, 2));
			}

			setObservers(Integer.parseInt(m.group(12).trim()));
			this.goServer = goServer;
			goServer.addGame(this);
		} catch (Exception e) {
			System.out.println("Error parsing game " + gameString);
		}

	}

	/**
	 * @return Returns the blackPlayer.
	 */
	public Player getBlackPlayer() {
		return blackPlayer;
	}

	/**
	 * @return Returns the boardSize.
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * @return Returns the byoyomi.
	 */
	public int getByoyomi() {
		return byoyomi;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	/**
	 * @return Returns the gameType.
	 */
	public String getGameType() {
		return gameType;
	}

	/**
	 * @return Returns the handicap.
	 */
	public int getHandicap() {
		return handicap;
	}

	/**
	 * @return Returns the komi.
	 */
	public float getKomi() {
		return komi;
	}

	/**
	 * @return Returns the moveNumber.
	 */
	public int getMoveNumber() {
		return moveNumber;
	}

	/**
	 * @return Returns the observers.
	 */
	public int getObservers() {
		return observers;
	}

	/**
	 * @return Returns the rated.
	 */
	public String getRated() {
		return rated;
	}

	/**
	 * @return Returns the whitePlayer.
	 */
	public Player getWhitePlayer() {
		return whitePlayer;
	}

	/**
	 * @param blackPlayer
	 *            The blackPlayer to set.
	 */
	public void setBlackPlayer(Player blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	/**
	 * @param boardSize
	 *            The boardSize to set.
	 */
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	/**
	 * @param byoyomi
	 *            The byoyomi to set.
	 */
	public void setByoyomi(int byoyomi) {
		this.byoyomi = byoyomi;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	/**
	 * Sets the gametype according to mnemonic string
	 * 
	 * @param type,
	 *            the mnemonic string
	 */
	public void setGameType(String type) {

		if (type.equals("I"))
			gameType = "Go";
		if (type.equals("C"))
			gameType = "Chinese Chess";
		if (type.equals("G"))
			gameType = "GOE Go";
		if (type.equals("P"))
			gameType = "GOE Pro Go";
		if (type.equals("S"))
			gameType = "Shogi";

	}

	/**
	 * @param handicap
	 *            The handicap to set.
	 */
	public void setHandicap(int handicap) {
		this.handicap = handicap;
	}

	/**
	 * @param komi
	 *            The komi to set.
	 */
	public void setKomi(float komi) {
		this.komi = komi;
	}

	/**
	 * @param moveNumber
	 *            The moveNumber to set.
	 */
	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	/**
	 * @param observers
	 *            The observers to set.
	 */
	public void setObservers(int observers) {
		this.observers = observers;
	}

	/**
	 * @param rated
	 *            The rated to set.
	 */
	public void setRated(String ratedCode) {

		if (ratedCode.equals(" "))
			rated = "Rated";
		if (ratedCode.equals("F"))
			rated = "Free";
		if (ratedCode.equals("T"))
			rated = "Training";
		if (ratedCode.equals("*"))
			rated = "Tournament";

	}

	/**
	 * @param whitePlayer
	 *            The whitePlayer to set.
	 */
	public void setWhitePlayer(Player whitePlayer) {
		this.whitePlayer = whitePlayer;
	}

	/**
	 * @return Returns the goServer.
	 */
	public GoServer getGoServer() {
		return goServer;
	}

	public List<BoardPoint> getBoardPoints() {

		return boardPoints;

	}
}
