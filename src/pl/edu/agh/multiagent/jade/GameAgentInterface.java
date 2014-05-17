package pl.edu.agh.multiagent.jade;

import java.io.Serializable;
import java.util.Collection;

import pl.edu.agh.multiagent.api.AgentInfo;
import pl.edu.agh.multiagent.api.GameState;

public interface GameAgentInterface {

	public abstract AgentInfo getAgentInfo();

	public abstract void broadcast(Serializable msg);

	/** Call when you create a game. */
	public abstract void createGame(GameState gameState);

	/** Call when game state updated. */
	public abstract void updateGameState(GameState gameState);

	/** Call to make a move. */
	public abstract void makeMove(GameState newGameState);

	public abstract AgentInfo getAgentByUuid(String uuid);

	public abstract GameState getGameStateByUuid(String uuid);

	public abstract Collection<AgentInfo> getAllAgents();

	public abstract Collection<GameState> getAllActiveGames();

}