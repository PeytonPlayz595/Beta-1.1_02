package net.lax1dude.eaglercraft.internal;

public class ContextLostError extends Error {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContextLostError() {
		super("WebGL context lost! Please refresh the page to continue");
	}

}
