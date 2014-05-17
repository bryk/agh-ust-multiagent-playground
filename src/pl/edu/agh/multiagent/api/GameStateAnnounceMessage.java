package pl.edu.agh.multiagent.api;

import java.io.Serializable;

public class GameStateAnnounceMessage implements Serializable {
	public static final String JADE_NAME = "GameStateAnnounceMessage";
	private static final long serialVersionUID = 1L;
	private GameState gameState;

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	@Override
	public String toString() {
		return "GameStateAnnounceMessage [gameState=" + gameState + "]";
	}
}
