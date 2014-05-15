package pl.edu.agh.multiagent.jade;

import pl.edu.agh.multiagent.api.AgentJoinedMessage;
import pl.edu.agh.multiagent.api.GameState;

/**
 * Controls agent's interaction with Jade. Use its method to announce new games. It will call
 * callbacks on {@link GameAgent}, once appropriate message is received.
 */
public class JadeGameService {
	private GameAgent agent;
	private JadeController controller;

	private JadeGameService() {
	}

	public static JadeGameService newService(GameAgent agent, JadeController controller) {
		JadeGameService gameService = new JadeGameService();
		gameService.agent = agent;
		gameService.controller = controller;
		return gameService;
	}

	/** Call when you want to join game system. */
	public void join() {
		AgentJoinedMessage msg = new AgentJoinedMessage();
		msg.setAgent(agent.getAgentInfo());
		controller.broadcast(agent, msg);
	}

	/** Call when you create a game. */
	public void createGame(GameState gameState) {
		// TODO(bryk): Implement this.
	}

	/** Call when game state updated. */
	public void updateGameState(GameState gameState) {
		// TODO(bryk): Implement this.
	}
}
