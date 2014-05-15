package pl.edu.agh.multiagent.jade;

import pl.edu.agh.multiagent.api.AgentInfo;
import pl.edu.agh.multiagent.api.AgentJoinedMessage;
import pl.edu.agh.multiagent.api.GameMoveMessage;
import pl.edu.agh.multiagent.api.GameStateAnnounceMessage;
import android.util.Log;
import jade.core.Agent;

/**
 * Main agent of the system.
 */
public class GameAgent extends Agent {
	private static final String TAG = "GameAgent";
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		Log.i(TAG, "Starting game agent");
		addBehaviour(new GameAgentBehaviour());
	}

	@Override
	protected void takeDown() {
		Log.i(TAG, "Taking down game agent");
	}

	/** Called when new agent has joined. */
	void onAgentJoined(AgentJoinedMessage agent) {
	}

	/** Called when game state is announced. */
	void onGameStateAnnounce(GameStateAnnounceMessage gameStateAnnounceMessage) {
	}

	/** Called when somebody wants to make a move. */
	void onGameMove(GameMoveMessage gameMoveMessage) {
	}

	public AgentInfo getAgentInfo() {
		// TODO(bryk): Implement this.
		return null;
	}
}
