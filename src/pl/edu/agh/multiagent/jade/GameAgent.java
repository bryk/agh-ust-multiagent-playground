package pl.edu.agh.multiagent.jade;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import pl.edu.agh.multiagent.api.AgentInfo;
import pl.edu.agh.multiagent.api.AgentInfoMessage;
import pl.edu.agh.multiagent.api.GameMoveMessage;
import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.api.GameStateAnnounceMessage;
import android.util.Log;

/**
 * Main agent of the system.
 */
public class GameAgent extends Agent implements GameAgentInterface {
	private String tag = "GameAgent";
	private static final long serialVersionUID = 1L;
	private final AgentInfo agentInfo = AgentInfo.newAgent("Piotr");
	private static final int STATE_EXPIRE_TIME = 5;
	protected static final int REBROADCAST_TIME = 1;
	private Map<String, AgentInfo> agents = new LinkedHashMap<String, AgentInfo>();
	private Map<String, GameState> games = new LinkedHashMap<String, GameState>();
	private Map<String, GameState> myGames = new LinkedHashMap<String, GameState>();
	private Map<String, Long> agentTimestamps = new LinkedHashMap<String, Long>();
	private Map<String, Long> gameTimestamps = new LinkedHashMap<String, Long>();
	private CyclicBehaviour cyclic;

	public GameAgent() {
		registerO2AInterface(GameAgentInterface.class, this);
	}

	@Override
	protected void setup() {
		Log.i(tag, "Starting game agent");
		tag = getName();
		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				ACLMessage message = receive();
				if (message != null) {
					Log.d(tag, "New message:" + message);
					try {
						Serializable obj = message.getContentObject();
						processObjMessage(obj, System.currentTimeMillis());
					} catch (UnreadableException e) {
						throw new RuntimeException(e);
					}
				} else {
					Log.i(tag, "Block in recv");
					block();
				}
			}
		});
		addBehaviour(new OneShotBehaviour() {
			private static final long serialVersionUID = 1L;

			@Override
			public void action() {
				AgentInfoMessage msg = new AgentInfoMessage();
				msg.setAgent(getAgentInfo());
				broadcast(msg);
			}
		});
		cyclic = new CyclicBehaviour() {
			private static final long serialVersionUID = 1L;
			private long lastCyclic;

			@Override
			public void action() {
				if (System.currentTimeMillis() - 1000 > lastCyclic) {
					gc();
					broadcastAllInfo();
					lastCyclic = System.currentTimeMillis();
				}
				block(1000);
			}
		};
		addBehaviour(cyclic);
		Log.i(tag, "Started game agent");
	}

	@Override
	protected void takeDown() {
		Log.i(tag, "Taking down game agent");
	}

	/** Called when agent info is announced. */
	void onAgentInfoAnnounce(AgentInfoMessage agent) {
	}

	/** Called when game state is announced. */
	void onGameStateAnnounce(GameStateAnnounceMessage gameStateAnnounceMessage) {
	}

	/** Called when somebody wants to make a move. */
	void onGameMove(GameMoveMessage gameMoveMessage) {
	}

	@Override
	public AgentInfo getAgentInfo() {
		return agentInfo;
	}

	@Override
	public synchronized void broadcast(Serializable msg) {
		try {
			AMSAgentDescription desc = new AMSAgentDescription();
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(Long.valueOf(-1));
			AMSAgentDescription[] agents = AMSService.search(this, desc, c);
			if (agents.length > 0) {
				ACLMessage m = new ACLMessage(ACLMessage.INFORM);
				for (AMSAgentDescription agentDescription : agents) {
					m.addReceiver(agentDescription.getName());
				}
				m.setOntology("agents");
				m.setContentObject(msg);
				send(m);
				Log.i(tag, "Num agents: " + agents.length + ", sent: " + m);
			} else {
				Log.i(tag, "No message sent. I am alone.");
			}
		} catch (FIPAException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected synchronized void processObjMessage(Serializable obj, long timestamp) {
		if (obj instanceof GameStateAnnounceMessage) {
			processGameState((GameStateAnnounceMessage) obj, timestamp);
			onGameStateAnnounce((GameStateAnnounceMessage) obj);
		} else if (obj instanceof AgentInfoMessage) {
			processAgentInfo((AgentInfoMessage) obj, timestamp);
			onAgentInfoAnnounce((AgentInfoMessage) obj);
		} else if (obj instanceof GameMoveMessage) {
			onGameMove((GameMoveMessage) obj);
		} else {
			throw new IllegalArgumentException("Cannot categorize message: " + obj);
		}
	}

	@Override
	public void createGame(GameState gameState) {
		Log.d(tag, "Creating game: " + gameState);
		myGames.put(gameState.getUuid(), gameState);
		if (cyclic != null) {
			cyclic.restart();
		}
	}

	@Override
	public synchronized void updateGameState(GameState gameState) {
		Log.d(tag, "Updating game: " + gameState);
		GameStateAnnounceMessage msg = new GameStateAnnounceMessage();
		msg.setGameState(gameState);
		broadcast(msg);
	}

	@Override
	public synchronized void makeMove(GameState newGameState) {
		Log.d(tag, "Making move: " + newGameState);
		GameMoveMessage msg = new GameMoveMessage();
		msg.setMover(getAgentInfo());
		msg.setNewState(newGameState);
		broadcast(msg);
	}

	@Override
	public synchronized AgentInfo getAgentByUuid(String uuid) {
		return this.agents.get(uuid);
	}

	@Override
	public synchronized GameState getGameStateByUuid(String uuid) {
		return this.games.get(uuid);
	}

	@Override
	public synchronized Collection<AgentInfo> getAllAgents() {
		return this.agents.values();
	}

	@Override
	public synchronized Collection<GameState> getAllActiveGames() {
		return this.games.values();
	}

	private synchronized void processAgentInfo(AgentInfoMessage obj, long timestamp) {
		if (!obj.getAgentInfo().getUuid().equals(getAgentInfo().getUuid())) {
			this.agents.put(obj.getAgentInfo().getUuid(), obj.getAgentInfo());
			this.agentTimestamps.put(obj.getAgentInfo().getUuid(), timestamp);
		}
	}

	private synchronized void processGameState(GameStateAnnounceMessage gameState, long timestamp) {
		if (!myGames.containsKey(gameState.getGameState().getUuid())) {
			this.games.put(gameState.getGameState().getUuid(), gameState.getGameState());
			this.gameTimestamps.put(gameState.getGameState().getUuid(), timestamp);
			Log.d(tag, "Num games: " + games.size() + ", processing game: " + gameState);
		}
	}

	protected synchronized void gc() {
		long treshold = System.currentTimeMillis() - STATE_EXPIRE_TIME * 1000;

		Map<String, GameState> newGames = new LinkedHashMap<String, GameState>();
		Map<String, Long> newTimestamps = new LinkedHashMap<String, Long>();
		for (Entry<String, GameState> entry : games.entrySet()) {
			if (gameTimestamps.get(entry.getKey()) > treshold) {
				newGames.put(entry.getKey(), entry.getValue());
				newTimestamps.put(entry.getKey(), gameTimestamps.get(entry.getKey()));
			}
		}
		this.games = newGames;
		this.gameTimestamps = newTimestamps;

		Map<String, AgentInfo> newAgents = new LinkedHashMap<String, AgentInfo>();
		newTimestamps = new LinkedHashMap<String, Long>();
		for (Entry<String, AgentInfo> entry : agents.entrySet()) {
			if (agentTimestamps.get(entry.getKey()) > treshold) {
				newAgents.put(entry.getKey(), entry.getValue());
				newTimestamps.put(entry.getKey(), agentTimestamps.get(entry.getKey()));
			}
		}
		this.agents = newAgents;
		this.agentTimestamps = newTimestamps;
	}

	protected synchronized void broadcastAllInfo() {
		Log.i(tag, "broadcastAllInfo: " + myGames.size());
		for (GameState gameState : myGames.values()) {
			updateGameState(gameState);
		}
	}
}
