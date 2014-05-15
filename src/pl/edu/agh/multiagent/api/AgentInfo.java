package pl.edu.agh.multiagent.api;

import java.io.Serializable;
import java.util.UUID;

public class AgentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String uuid;
	private String name;
	private int points;

	private AgentInfo(String uuid) {
		this.uuid = uuid;
	}

	public static AgentInfo newAgent(String name) {
		AgentInfo agent = new AgentInfo(UUID.randomUUID().toString());
		agent.name = name;
		return agent;
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

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void addPoints(int toAdd) {
		this.points += toAdd;
	}

	@Override
	public String toString() {
		return "Agent [uuid=" + uuid + ", name=" + name + ", points=" + points + "]";
	}
}
