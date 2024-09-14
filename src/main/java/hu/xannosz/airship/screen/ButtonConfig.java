package hu.xannosz.airship.screen;

import hu.xannosz.airship.util.ButtonId;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.core.BlockPos;

@Getter
@Builder
public class ButtonConfig {
	private ButtonId buttonId;
	private int hoveredX;
	private int hoveredY;
	private int hitBoxX;
	private int hitBoxY;
	private int hitBoxW;
	private int hitBoxH;
	private int normalX;
	private int normalY;
	private BlockPos position;
}
