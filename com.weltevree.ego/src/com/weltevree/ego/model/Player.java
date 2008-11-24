package com.weltevree.ego.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Player {

	private String name;

	private String rank;

	private String info;

	private String country;

	private int gamesWon;

	private int gamesLost;

	private int observing;

	private int playing;

	private String idleTime;

	private String flags;

	private boolean quietMode = false;

	private boolean receiveShout = true;

	private boolean openForGames = true;

	private boolean lookingForMatch = false;

	private String language;

	private GoServer goServer;

	private static int GROUP_COUNT = 11;

	private static Pattern pattern;

	static {
		String patCapUS = "42 *([^ ]*).."; // Captures the users
		String patCapIN = "(.{16}) *?"; // Captures Info String
		String patCapCO = "(.{7}) *"; // Capture Country
		String patCapRA = "([^ ]*) *"; // Captures Rank
		String patCapGW = "([^ ]*)/ *"; // Captures Games Won
		String patCapGL = "([^ ]*) *"; // Captures Games Lost
		String patCapOB = "([^ ]*) *"; // Captures observing
		String patCapPL = "([^ ]*) *"; // Captures playing
		String patCapID = "([^ ]*) *"; // Captures Idle
		String patCapFL = "([^ ]*) *"; // Captures Flags
		String patCapLA = "([^ ]*)"; // Captures language

		String pat = patCapUS + patCapIN + patCapCO + patCapRA + patCapGW
				+ patCapGL + patCapOB + patCapPL + patCapID + patCapFL
				+ patCapLA;

		pattern = Pattern.compile(pat);
	}

	public Player(GoServer goServer, String gameString) {
		Matcher m = pattern.matcher(gameString);
		m.find();

		if (m.groupCount() != GROUP_COUNT)
			throw new RuntimeException("Invalid gameString: " + gameString);
		try {
			setName(m.group(1).trim());
			setInfo(m.group(2).trim());
			setCountry(m.group(3).trim());
			setRank(m.group(4).trim());
			setGamesWon(Integer.parseInt(m.group(5).trim()));
			setGamesLost(Integer.parseInt(m.group(6).trim()));
			if (m.group(7).equals("-"))
				setObserving(0);
			else
				setObserving(Integer.parseInt(m.group(7).trim()));
			if (m.group(8).equals("-"))
				setPlaying(0);

			else
				setPlaying(Integer.parseInt(m.group(8).trim()));
			setIdleTime(m.group(9).trim());
			setFlags(m.group(10).trim());
			setLanguage(m.group(11).trim());
			this.goServer = goServer;
			goServer.addUser(this);
		} catch (Exception e) {
			System.out.println("Error parsing user " + gameString);
		}

	}

	public void setLanguage(String language) {
		this.language = language;
	}

	private void setGamesWon(int gamesWon) {
		this.gamesWon = gamesWon;

	}

	private void setGamesLost(int gamesWon) {
		this.gamesLost = gamesWon;

	}

	private void setObserving(int observing) {
		this.observing = observing;

	}

	private void setPlaying(int playing) {
		this.playing = playing;

	}

	private void setInfo(String info) {
		this.info = info;
	}

	private void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}

	private void setFlags(String flags) {

		this.flags = flags;

		for (int ix = 0; ix < flags.length(); ix++) {

			if (flags.substring(ix, ix + 1).equals("Q"))
				quietMode = quietMode ? false : true;
			if (flags.substring(ix, ix + 1).equals("S"))
				receiveShout = receiveShout ? false : true;
			if (flags.substring(ix, ix + 1).equals("X"))
				openForGames = openForGames ? false : true;
			if (flags.substring(ix, ix + 1).equals("!"))
				lookingForMatch = lookingForMatch ? false : true;

		}
	}

	private void setCountry(String country) {
		this.country = country;
	}

	public Player() {
		name = "";
		rank = "";
	}

	public Player(String name, String rank) {
		this.name = name;
		this.rank = rank;
		;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * Translates the rank in an int to get some sorting.
	 * 
	 * @return int, the rank as int
	 */
	public int getRankToInt() {

		String stripped = getRank().replace('*', ' ').trim();

		if (stripped.endsWith("k"))
			return 100 * Integer.parseInt(stripped.replace('k', ' ').trim());
		if (stripped.endsWith("d"))
			return 80 - Integer.parseInt(stripped.replace('d', ' ').trim());
		if (stripped.endsWith("p"))
			return 60 - Integer.parseInt(stripped.replace('p', ' ').trim());

		return 10000;
	}

	/**
	 * @return Returns the lookingForMatch.
	 */
	public boolean isLookingForMatch() {
		return lookingForMatch;
	}

	/**
	 * @param lookingForMatch
	 *            The lookingForMatch to set.
	 */
	public void setLookingForMatch(boolean lookingForMatch) {
		this.lookingForMatch = lookingForMatch;
	}

	/**
	 * @return Returns the openForGames.
	 */
	public boolean isOpenForGames() {
		return openForGames;
	}

	/**
	 * @param openForGames
	 *            The openForGames to set.
	 */
	public void setOpenForGames(boolean openForGames) {
		this.openForGames = openForGames;
	}

	/**
	 * @return Returns the quietMode.
	 */
	public boolean isQuietMode() {
		return quietMode;
	}

	/**
	 * @param quietMode
	 *            The quietMode to set.
	 */
	public void setQuietMode(boolean quietMode) {
		this.quietMode = quietMode;
	}

	/**
	 * @return Returns the receiveShout.
	 */
	public boolean isReceiveShout() {
		return receiveShout;
	}

	/**
	 * @param receiveShout
	 *            The receiveShout to set.
	 */
	public void setReceiveShout(boolean receiveShout) {
		this.receiveShout = receiveShout;
	}

	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return Returns the flags.
	 */
	public String getFlags() {
		return flags;
	}

	/**
	 * @return Returns the gamesLost.
	 */
	public int getGamesLost() {
		return gamesLost;
	}

	/**
	 * @return Returns the gamesWon.
	 */
	public int getGamesWon() {
		return gamesWon;
	}

	/**
	 * @return Returns the idleTime.
	 */
	public String getIdleTime() {
		return idleTime;
	}

	/**
	 * @return Returns the info.
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @return Returns the language.
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return Returns the observing game id.
	 */
	public int getObserving() {
		return observing;
	}

	/**
	 * @return Game, Returns the observing game
	 */
	public Game getObservingGame() {
		return goServer.getGame(observing);
	}

	/**
	 * @return Returns the playing.
	 */
	public int getPlaying() {
		return playing;
	}

	/**
	 * @return Returns the goServer.
	 */
	public GoServer getGoServer() {
		return goServer;
	}
}
