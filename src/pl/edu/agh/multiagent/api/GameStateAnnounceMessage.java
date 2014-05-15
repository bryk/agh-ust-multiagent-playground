package pl.edu.agh.multiagent.api;

import java.io.Serializable;

public class GameStateAnnounceMessage implements Serializable {
	public static final String JADE_NAME = "GameStateAnnounceMessage";
	private static final long serialVersionUID = 1L;
	private AgentInfo agent;
	private GameState gameState;

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public AgentInfo getAgent() {
		return agent;
	}

	public void setAgentInfo(AgentInfo agent) {
		this.agent = agent;
	}

	@Override
	public String toString() {
		return "GameStateAnnounceMessage [agent=" + agent + ", gameState=" + gameState + "]";
	}
}
