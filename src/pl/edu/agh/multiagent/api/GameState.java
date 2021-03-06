package pl.edu.agh.multiagent.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.UUID;

public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String uuid;
	private final AgentInfo owner;
	private String name;
	private MoveResoultionStrategy moveResoultionStrategy;
	private Cell cells[][];
	private State state;
	private int moveNumber = 0;

	private GameState(String uuid, AgentInfo owner) {
		this.uuid = uuid;
		this.owner = owner;
	}

	public static GameState newGameState(String name,
			MoveResoultionStrategy moveResoultionStrategy, AgentInfo owner) {
		GameState gameState = new GameState(UUID.randomUUID().toString(), owner);
		gameState.name = name;
		gameState.moveResoultionStrategy = moveResoultionStrategy;
		gameState.cells = new Cell[3][3];
		gameState.state = State.OPEN;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				gameState.cells[i][j] = Cell.NOTHING;
			}
		}
		return gameState;
	}

	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	public MoveResoultionStrategy getMoveResoultionStrategy() {
		return moveResoultionStrategy;
	}

	public void setMoveResoultionStrategy(
			MoveResoultionStrategy moveResoultionStrategy) {
		this.moveResoultionStrategy = moveResoultionStrategy;
	}

	public Cell[][] getCells() {
		return cells;
	}

	public void setCell(int x, int y, Cell cell) {
		this.cells[x][y] = cell;
		moveNumber++;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public AgentInfo getOwner() {
		return owner;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameState other = (GameState) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GameState [uuid=" + uuid + ", owner=" + owner + ", name="
				+ name + ", moveResoultionStrategy=" + moveResoultionStrategy
				+ ", cells=" + Arrays.toString(cells) + ", state=" + state
				+ ", moveNumber=" + moveNumber + "]";
	}
}
