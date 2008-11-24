/*
 * Created on May 2, 2005 TODO To change the template for this generated file go
 * to Window - Preferences - Java - Code Style - Code Templates
 */
package com.weltevree.ego.model;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import com.weltevree.ego.server.ServerListener;

/**
 * TODO Enter text
 * 
 * @author Remain BV - W.S. Jongman
 * @version 0.1
 */
public class GoServer implements Serializable {

	/**
	 * Serial for saving
	 */
	private static final long serialVersionUID = 2490846875322309619L;

	/**
	 * The description of this server
	 */
	private String description = null;

	/**
	 * The filename used to save/serialize this object.
	 */
	private String fileName = null;

	/**
	 * The list with games
	 */
	private HashMap gameList = new HashMap();

	/**
	 * The login name
	 */
	private String login = null;

	/**
	 * The password.
	 */
	private String password = null;

	/**
	 * The list with players
	 */
	private HashMap playerList = new HashMap();

	/**
	 * The port on which to connect.
	 */
	private String port = null;

	private transient SocketChannel sc = null;

	/**
	 * The server IP number or DNS name
	 */
	private String serverAddress = null;

	/**
	 * The list with users
	 */
	private HashMap userList = new HashMap();

	/**
	 * Adds a game to the gameslist.
	 * 
	 * @param game
	 */
	public void addGame(Game game) {

		gameList.put(Integer.toString(game.getGameNumber()), game);

	}

	/**
	 * Adds a player to the player list.
	 * 
	 * @param player
	 */
	public void addPlayer(Player player) {

		playerList.put(player.getName(), player);

	}

	public void addUser(Player player) {
		userList.put(player.getName(), player);

	}


	/**
	 * This connects to the server.
	 * 
	 * @throws IOException
	 */
	public void connect() throws IOException {

		InetAddress iad = InetAddress.getByName(serverAddress);
		InetSocketAddress add = new InetSocketAddress(iad, Integer
				.parseInt(port));

		sc = SocketChannel.open(add);

		ByteBuffer bb = ByteBuffer.allocate(102400);

		int len = sc.read(bb);

		String text = new String(bb.array(), 0, len);

		if (text.trim().endsWith("Login:")) {
			bb = ByteBuffer.wrap((login + "\r").getBytes());

			sc.write(bb);

			bb = ByteBuffer.allocate(10000);
			len = sc.read(bb);

			text = new String(bb.array(), 0, len);

			if (text.trim().endsWith("Password:")
					|| text.trim().endsWith("1 1")) {
				bb = ByteBuffer.wrap((password + "\r").getBytes());
				sc.write(bb);

				bb = ByteBuffer.allocate(10000);
				len = sc.read(bb);

				text = new String(bb.array(), 0, len);
			}

			/*
			 * Toggle client mode
			 */
			if (text.trim().endsWith(">")) {
				bb = ByteBuffer.wrap(("toggle client\r").getBytes());
				sc.write(bb);
			}
			
			/*
			 * Start the listener thread
			 */
			ServerListener runner = new ServerListener(this,sc);
			Thread thread = new Thread(runner);
			thread.start();
		}
	}

	/**
	 * This closes the connection to the server.
	 * 
	 * @return boolean, true if disconnect was succesful.
	 */
	public boolean disconnect() {

		ByteBuffer bb = ByteBuffer.wrap(("exit" + "\r").getBytes());
		try {
			sc.write(bb);
		} catch (IOException e) {
		}

		try {
			sc.close();
			return true;
		} catch (IOException e1) {

			return false;
		}

	}

	/**
	 * Returns the description of this server.
	 * 
	 * @return String the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the file by which this server was saved.
	 * 
	 * @return String the filename
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * returns the game with the given id. If no such game exists then the games
	 * are loaded.
	 * 
	 * @param int,
	 *            the game id
	 * @return Game, or null if no game found
	 */
	public Game getGame(int gameID) {
		if (gameList.get(Integer.toString(gameID)) == null)
			loadGames();
		return (Game) gameList.get(Integer.toString(gameID));
	}

	/**
	 * Returns the games in a hashmap. Games may not be loaded.
	 * 
	 * @see loadGames()
	 * @return HashMap, the current games.
	 */
	public HashMap getGames() {
		return gameList;
	}

	public String getImageKey() {
		if (isConnected())
			return "connect_co.gif";
		else
			return "disconnect_co.gif";
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * receive the user with the given name. If no such user exists the the
	 * users are loaded.
	 * 
	 * @param String,
	 *            the user name
	 * @return Player, or null if user not found
	 */
	public Player getUser(String name) {
		if (userList.get(name) == null)
			loadUsers();
		return (Player) userList.get(name);
	}

	/**
	 * Returns the users in a hashmap. Could be an unloaded hashmap.
	 * 
	 * @see loadUsers()
	 * @return HashMap, the users.
	 */
	public HashMap getUsers() {
		return userList;
	}

	/**
	 * Returns the connected status of the server.
	 * 
	 * @return boolean true if this server is connected.
	 */
	public boolean isConnected() {
		if (sc == null)
			return false;
		return sc.isConnected();
	}

	/**
	 * Loads the games.
	 * 
	 */
	public void loadGames() {

		if (isConnected()) {
			
			gameList.clear();

			ByteBuffer bb = ByteBuffer.wrap(("games" + "\r").getBytes());
			try {
				sc.write(bb);
			//	parseOutput();
			} catch (IOException e) {
				System.out
						.println("Error loading gameslist: " + e.getMessage());
			}

			System.out.println(getGames().size() + " games loaded");
		}
	}

	/**
	 * Loads the users.
	 * 
	 */
	public void loadUsers() {

		if (isConnected()) {

			userList.clear();

			ByteBuffer bb = ByteBuffer.wrap(("user" + "\r").getBytes());
			try {
				sc.write(bb);
				// parseOutput();
			} catch (IOException e) {
				System.out.println("Error loading userlist: " + e.getMessage());
			}
		}
	}

	/**
	 * Process the output of the server after a command.
	 */
	private void parseOutput() {

	
	}

	/**
	 * Removes a game from the gameslist.
	 * 
	 * @param game
	 */
	public void removeGame(Game game) {

		gameList.remove(Integer.toString(game.getGameNumber()));
	}

	/**
	 * Removes a player from the list.
	 * 
	 * @param player
	 */
	public void removePlayer(Player player) {

		gameList.remove(player.getName());
	}

	/**
	 * Sets the description.
	 * 
	 * @param String
	 *            description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the filename used to save this server.
	 * 
	 * @param String
	 *            the filename
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setServerAddress(String server) {
		this.serverAddress = server;
	}

	public String toString() {
		return getDescription();
	}
}
