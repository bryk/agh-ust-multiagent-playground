package pl.edu.agh.multiagent.jade;

import pl.edu.agh.multiagent.api.AgentInfoMessage;
import pl.edu.agh.multiagent.api.GameMoveMessage;
import pl.edu.agh.multiagent.api.GameStateAnnounceMessage;

public interface GameAgentListener {
	/** Called when agent info is announced. */
	void onAgentInfoAnnounce(AgentInfoMessage agent);
	
	/** Called when game state is announced. */
	void onGameStateAnnounce(GameStateAnnounceMessage gameStateAnnounceMessage);

	/** Called when somebody wants to make a move. */
	void onGameMove(GameMoveMessage gameMoveMessage);
}