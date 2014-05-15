package pl.edu.agh.multiagent.api;

import java.io.Serializable;

public class GameMoveMessage implements Serializable {
	public static final String JADE_NAME = "GameMoveMessage";
	private static final long serialVersionUID = 1L;
	private AgentInfo mover;
	private GameState newState;

	public AgentInfo getMover() {
		return mover;
	}

	public void setMover(AgentInfo mover) {
		this.mover = mover;
	}

	public GameState getNewState() {
		return newState;
	}

	public void setNewState(GameState newState) {
		this.newState = newState;
	}

	@Override
	public String toString() {
		return "GameMoveMessage [mover=" + mover + ", newState=" + newState + "]";
	}
}
