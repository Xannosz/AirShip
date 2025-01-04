package hu.xannosz.airship.computer;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GroundRadarData {
	private Map<Integer, Map<Integer, Integer>> colors = new HashMap<>();

	public void setColor(int x, int y, int color) {
		colors.computeIfAbsent(x, k -> new HashMap<>());
		colors.get(x).put(y, color);
	}
}
