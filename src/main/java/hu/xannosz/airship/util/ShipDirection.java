package hu.xannosz.airship.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShipDirection {
	N(0,0,-1), E(1,1,0), S(2,0,1), W(3,-1,0),
	NE(4,0.707f,-0.707f), NW(5,-0.707f,-0.707f), SE(6,0.707f,0.707f), SW(7,-0.707f,0.707f),
	NNE(8,0.383f,-0.924f), ENE(9,0.924f,-0.383f), NNW(10,-0.383f,-0.924f), WNW(11,-0.924f,-0.383f),
	SSE(12,0.383f,0.924f), ESE(13,0.924f,0.383f), SSW(14,-0.383f,0.924f), WSW(15,-0.924f,0.383f);

	private final int code;
	private final float x;
	private final float z;

	public ShipDirection right() {
		switch (this) {
			case N -> {
				return NNE;
			}
			case E -> {
				return ESE;
			}
			case S -> {
				return SSW;
			}
			case W -> {
				return WNW;
			}
			case NE -> {
				return ENE;
			}
			case NW -> {
				return NNW;
			}
			case SE -> {
				return SSE;
			}
			case SW -> {
				return WSW;
			}
			case NNE -> {
				return NE;
			}
			case ENE -> {
				return E;
			}
			case NNW -> {
				return N;
			}
			case WNW -> {
				return NW;
			}
			case SSE -> {
				return S;
			}
			case ESE -> {
				return SE;
			}
			case SSW -> {
				return SW;
			}
			case WSW -> {
				return W;
			}
		}
		throw new CodeShouldNeverReach();
	}

	public ShipDirection left() {
		for (ShipDirection direction : values()) {
			if (direction.right().equals(this)) {
				return direction;
			}
		}
		throw new CodeShouldNeverReach();
	}

	public static ShipDirection fromCode(int code) {
		for (ShipDirection direction : values()) {
			if (direction.getCode() == code) {
				return direction;
			}
		}
		throw new CodeShouldNeverReach();
	}
}
