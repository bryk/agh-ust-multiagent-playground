package pl.edu.agh.multiagent.jade;

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
}
