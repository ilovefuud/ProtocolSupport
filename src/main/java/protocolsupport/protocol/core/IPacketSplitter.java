package protocolsupport.protocol.core;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface IPacketSplitter {

	public void split(ChannelHandlerContext ctx, ByteBuf input, List<Object> list) throws Exception;

}
