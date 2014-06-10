package pl.edu.agh.multiagent.jade;

import pl.edu.agh.multiagent.api.GameState;

public interface GameAgentListener {
	void onGameState(GameState state);
}