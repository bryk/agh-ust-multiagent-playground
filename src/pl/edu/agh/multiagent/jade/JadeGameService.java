package pl.edu.agh.multiagent.jade;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.Serializable;
import java.util.List;

import pl.edu.agh.multiagent.api.AgentInfo;
import pl.edu.agh.multiagent.api.AgentJoinedMessage;
import pl.edu.agh.multiagent.api.GameMoveMessage;
import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.api.GameStateAnnounceMessage;

/**
 * Controls agent's interaction with Jade. Use its method to announce new games. It will call
 * callbacks on {@link GameAgent}, once appropriate message is received.
 */
public class JadeGameService {
	/** Time in seconds after AgentInfo or GameState expires. */
	private static final int STATE_EXPIRE_TIME = 5;
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
		agent.addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage message = agent.receive();
				if (message != null) {
					try {
						Serializable obj = message.getContentObject();
						processObjMessage(obj, System.currentTimeMillis());
					} catch (UnreadableException e) {
						throw new RuntimeException(e);
					}
				} else {
					block();
				}
			}
		});
		AgentJoinedMessage msg = new AgentJoinedMessage();
		msg.setAgent(agent.getAgentInfo());
		controller.broadcast(agent, msg);
	}

	protected void processObjMessage(Serializable obj, long timestamp) {
		if (obj instanceof GameStateAnnounceMessage) {
			processGameState((GameStateAnnounceMessage) obj, timestamp);
			agent.onGameStateAnnounce((GameStateAnnounceMessage) obj);
		} else if (obj instanceof AgentJoinedMessage) {
			processNewAgent((AgentJoinedMessage) obj, timestamp);
			agent.onAgentJoined((AgentJoinedMessage) obj);
		} else if (obj instanceof GameMoveMessage) {
			agent.onGameMove((GameMoveMessage) obj);
		} else {
			throw new IllegalArgumentException("Cannot categorize message: " + obj);
		}
	}

	/** Call when you create a game. */
	public void createGame(GameState gameState) {
		GameStateAnnounceMessage msg = new GameStateAnnounceMessage();
		msg.setAgentInfo(agent.getAgentInfo());
		msg.setGameState(gameState);
		controller.broadcast(agent, msg);
	}

	/** Call when game state updated. */
	public void updateGameState(GameState gameState) {
		GameStateAnnounceMessage msg = new GameStateAnnounceMessage();
		msg.setAgentInfo(agent.getAgentInfo());
		msg.setGameState(gameState);
		controller.broadcast(agent, msg);
	}

	/** Call to make a move. */
	public void makeMove(GameState newGameState) {
		GameMoveMessage msg = new GameMoveMessage();
		msg.setMover(agent.getAgentInfo());
		msg.setNewState(newGameState);
		controller.broadcast(agent, msg);
	}

	public List<AgentInfo> getAllAgents() {
		// TODO(bryk): Implement this.
		return null;
	}

	public List<GameState> getAllActiveGames() {
		// TODO(bryk): Implement this.
		return null;
	}

	private void processNewAgent(AgentJoinedMessage obj, long timestamp) {
		// TODO(bryk): Implement this.
	}

	private void processGameState(GameStateAnnounceMessage gameState, long timestamp) {
		// TODO(bryk): Implement this.
	}
}
