package com.weltevree.ego.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.weltevree.ego.model.Game;
import com.weltevree.ego.model.GoServer;
import com.weltevree.ego.model.Player;

/**
 * This class is a runnable that listens to server output notifies the passed
 * server object of any changes.
 * 
 * @author jongw
 * 
 */
public class ServerListener implements Runnable {

	private SocketChannel channel;
	private GoServer server;

	public ServerListener(GoServer server, SocketChannel channel) {
		this.server = server;
		this.channel = channel;
	}

	@Override
	public void run() {

		if (server == null) {
			return;
		}

		if (channel == null) {
			return;
		}

		listen();

	}

	private void listen() {

		while (channel.isConnected()) {

			boolean eob = false;
			String text = "";
			ByteBuffer bb = ByteBuffer.allocate(1000);
			int len = 0;
			try {
				len = channel.read(bb);

			} catch (IOException e) {
				eob = true;
			}

			/*
			 * We have Data
			 */
			while (!eob) {

				/*
				 * Split the lines
				 */
				text = text + new String(bb.array(), 0, len);
				String[] lines = text.split("\n");
				text = "";

				/*
				 * Process all lines
				 */
				for (int ix = 0; ix < lines.length; ix++) {

					/*
					 * If this is a full line
					 */
					if (lines[ix].endsWith("\r")) {

						String[] tokens = lines[ix].split(" ");

						if (tokens.length > 0) {
							parseLine(tokens[0], lines[ix]);
						}

						/*
						 * Partial line, more to come after next read.
						 */
					} else
						text = lines[ix];
				}

				/*
				 * Read more data.
				 */
				if (!eob) {
					try {
						bb = ByteBuffer.allocate(1000);
						len = channel.read(bb);
					} catch (IOException e) {
						eob = true;
					}
				}
			}

			try {
				Thread.sleep(250);
				System.out.print(".");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Server disconnected.");

	}

	private void parseLine(final String token, final String line) {

		if (token.equals("7"))
			new Game(server, line);

		else if (token.equals("9"))
			System.out.println(line);

		else if (token.equals("24") || token.equals("9"))
			; // empty
		//
		// Removed because of headless refactoring FIXME
		//
		// Display.getDefault().asyncExec(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// MessageDialog.openInformation(Display.getDefault()
		// .getActiveShell(), "Message from "
		// + line.split(" ")[1], line.split(":")[1]);
		//
		// }
		// });

		else if (token.equals("42"))
			new Player(server, line);

	}
}
