package net.minecraft.src;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.lax1dude.eaglercraft.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.IWebSocketFrame;
import net.peyton.eagler.minecraft.network.PacketBuffer;

public class NetworkManager {
	private boolean isRunning = true;
	private NetHandler netHandler;
	private List<Packet> readPackets = new ArrayList<Packet>();
	private boolean isServerTerminating = false;
	private boolean isTerminating = false;
	private String terminationReason = "";
	private Object[] field_20101_t;
	private int timeSinceLastRead = 0;
	private int sendQueueByteLength = 0;
	public int chunkDataSendCounter = 0;
	
	public IWebSocketClient webSocket;
	protected final PacketBuffer temporaryBuffer;
	protected int debugPacketCounter = 0;
	
	public static final Logger logger = LogManager.getLogger("NetworkManager");

	public NetworkManager(NetHandler var3) {
		this.netHandler = var3;
		this.temporaryBuffer = new PacketBuffer(Unpooled.buffer(0x1FFFF));
	}
	
	public void setWebsocketClient(IWebSocketClient client) {
		this.webSocket = client;
	}
	
	
	public void addToSendQueue(Packet var1) {
		if(!this.isServerTerminating) {
			if(isOpen()) {
				temporaryBuffer.clear();
				try {
					Packet.writePacket(var1, temporaryBuffer);
				} catch(Exception e) {
					logger.error("Failed to write packet {}!", var1.getClass().getSimpleName());
					this.onNetworkError(e, false);
				}
				
				int len = temporaryBuffer.writerIndex();
				byte[] bytes = new byte[len];
				temporaryBuffer.getBytes(0, bytes);
				
				webSocket.send(bytes);
			} else {
				this.networkShutdown("Connection closed");
			}
		}
	}

	private void onNetworkError(Exception var1, boolean print) {
		if(print) {
			logger.error(var1);
		}
		this.networkShutdown("disconnect.genericReason", new Object[]{"Internal exception: " + var1.toString()});
	}

	public void networkShutdown(String var1, Object... var2) {
		if(this.isRunning) {
			this.isTerminating = true;
			this.terminationReason = var1;
			this.field_20101_t = var2;
			this.isRunning = false;
		}
		
		if(isOpen()) {
			try {
				this.webSocket.close();
			}catch(Exception e) {
			}
			this.webSocket = null;
		}
	}
	
	private final PacketBuffer tempBuf2 = new PacketBuffer(null);
	public void readPacket() {
		if(netHandler == null) return;
		int frames = webSocket.availableStringFrames();
		if(frames > 0) {
			logger.warn("discarding {} string frames recieved on a binary connection", frames);
			webSocket.clearStringFrames();
		}
		
		List<IWebSocketFrame> pkts = webSocket.getNextBinaryFrames();
		if(pkts == null) {
			return;
		}
		
		for(int i = 0, j = pkts.size(); i < j; ++i) {
			IWebSocketFrame next = pkts.get(i);
			++debugPacketCounter;
			
			try {
				byte[] asByteArray = next.getByteArray();
				
				ByteBuf nettyBuffer = Unpooled.buffer(asByteArray, asByteArray.length);
				nettyBuffer.writerIndex(asByteArray.length);
				
				Packet pkt;
				try {
					pkt = Packet.readPacket(tempBuf2.setBuf(nettyBuffer));
				} catch(EOFException e) {
					throw new IOException("End of stream");
				} catch(IOException e) {
					if(!this.isTerminating) {
						this.onNetworkError(e, true);
					}
					return;
				}
				
				if(pkt == null) {
					throw new IOException("Recieved packet type which is undefined");
				} else {
					this.readPackets.add(pkt);
				}
			} catch(Throwable t) {
				logger.error("Failed to process websocket frame {}! It'll be skipped for debug purposes.", debugPacketCounter);
				logger.error(t);
			}
		}
	}
	
	public void processReadPackets() {
		if(this.sendQueueByteLength > 1048576) {
			this.networkShutdown("disconnect.overflow", new Object[0]);
		}

		if(this.readPackets.isEmpty()) {
			if(this.timeSinceLastRead++ == 1200) {
				this.networkShutdown("disconnect.timeout", new Object[0]);
			}
		} else {
			this.timeSinceLastRead = 0;
		}

		int var1 = 100;

		while(!this.readPackets.isEmpty() && var1-- >= 0) {
			Packet var2 = (Packet)this.readPackets.remove(0);
			var2.processPacket(this.netHandler);
		}

		if(this.isTerminating && this.readPackets.isEmpty()) {
			this.netHandler.handleErrorMessage(this.terminationReason, this.field_20101_t);
		}
	}
	
	private boolean isOpen() {
		return this.webSocket != null && this.webSocket.getState() == EnumEaglerConnectionState.CONNECTED && this.webSocket.isOpen();
	}

}
