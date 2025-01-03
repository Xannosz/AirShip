package hu.xannosz.airship.registries;

import lombok.Data;

import java.util.Map;

@Data
public class InnerMapData {
	private long time;
	private Map<Integer, Map<Integer, Integer>> colors;
}
