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

	public void setMoveResoultionStrategy(MoveResoultionStrategy moveResoultionStrategy) {
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

	@Override
	public String toString() {
		return "GameState [uuid=" + uuid + ", owner=" + owner + ", name=" + name
				+ ", moveResoultionStrategy=" + moveResoultionStrategy + ", cells="
				+ Arrays.toString(cells) + ", state=" + state + ", moveNumber=" + moveNumber + "]";
	}
}
