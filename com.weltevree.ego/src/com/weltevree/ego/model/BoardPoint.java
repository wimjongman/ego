package com.weltevree.ego.model;

import java.util.ArrayList;

public class BoardPoint {

	private int x;
	private int y;

	private ArrayList<Move> history = new ArrayList<Move>();
	private Move lastMove;

	public BoardPoint(int x, int y) {
		this.x = x;
		this.y = x;
	}

	public void doMove(Move move) {
		this.lastMove = move;
		history.add(move);
	}

	public Move getLastMove() {
		return lastMove;
	}

	public ArrayList<Move> getHistory() {
		return history;
	}

}
