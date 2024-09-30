package hu.xannosz.airship.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShipDirection {
	N(0, 0, -1, 0F, "Z--"), E(1, 1, 0, 90F, "X++"), S(2, 0, 1, 180F, "Z++"), W(3, -1, 0, 270F, "X--"),
	NE(4, 0.707f, -0.707f, 45F, "X+ Z-"), NW(5, -0.707f, -0.707f, 315F, "X- Z-"), SE(6, 0.707f, 0.707f, 135F, "X+ Z+"), SW(7, -0.707f, 0.707f, 225F, "X- Z+"),
	NNE(8, 0.383f, -0.924f, 22.5F, "X+ Z--"), ENE(9, 0.924f, -0.383f, 67.5F, "X++ Z-"), NNW(10, -0.383f, -0.924f, 337.5F, "X- Z--"), WNW(11, -0.924f, -0.383f, 292.5F, "X-- Z-"),
	SSE(12, 0.383f, 0.924f, 157.5F, "X+ Z++"), ESE(13, 0.924f, 0.383f, 112.5F, "X++ Z+"), SSW(14, -0.383f, 0.924f, 202.5F, "X- Z++"), WSW(15, -0.924f, 0.383f, 247.5F, "X-- Z+");

	private final int code;
	private final float x;
	private final float z;
	private final float angel;
	private final String name;

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
