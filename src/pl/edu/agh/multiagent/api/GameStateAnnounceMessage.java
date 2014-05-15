package pl.edu.agh.multiagent.api;

import java.io.Serializable;

public class GameStateAnnounceMessage implements Serializable {
	public static final String JADE_NAME = "GameStateAnnounceMessage";
	private static final long serialVersionUID = 1L;
	private AgentInfo agent;

	@Override
	public String toString() {
		return "AgentJoinedMessage [agent=" + agent + "]";
	}

	public AgentInfo getAgent() {
		return agent;
	}

	public void setAgent(AgentInfo agent) {
		this.agent = agent;
	}
}
