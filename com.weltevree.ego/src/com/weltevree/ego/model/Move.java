package com.weltevree.ego.model;

public class Move {

	private int moveNumber;
	private BoardPoint point;
	private int color;

	public Move(int color, BoardPoint point, int moveNumber) {
		if (color != EGo.BLACK && color != EGo.WHITE)
			new AssertionError("Class Move:Constructor:Wrong Color");
		this.color = color;
		this.point = point;
		this.moveNumber = moveNumber;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public BoardPoint getBoardPoint() {
		return point;
	}

	public int getColor() {
		return color;
	}
}
