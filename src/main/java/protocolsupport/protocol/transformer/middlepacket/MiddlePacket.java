package protocolsupport.protocol.transformer.middlepacket;

import org.bukkit.entity.Player;
import protocolsupport.buildprocessor.annotations.NeedsNoArgConstructor;


@NeedsNoArgConstructor
public abstract class MiddlePacket {

	protected Player player;

	public boolean needsPlayer() {
		return false;
	}

	public final void setPlayer(Player player) {
		this.player = player;
	}

}
