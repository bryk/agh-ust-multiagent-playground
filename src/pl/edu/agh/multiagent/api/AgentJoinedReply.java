package pl.edu.agh.multiagent.api;

import java.io.Serializable;
import java.util.List;

public class AgentJoinedReply implements Serializable {
	public static final String JADE_NAME = "AgentJoinedReply";
	private static final long serialVersionUID = 1L;
	private AgentInfo myself;
	private List<GameState> myGames;

	public AgentInfo getMyself() {
		return myself;
	}

	public void setMyself(AgentInfo myself) {
		this.myself = myself;
	}

	public List<GameState> getMyGames() {
		return myGames;
	}

	public void setMyGames(List<GameState> myGames) {
		this.myGames = myGames;
	}

	@Override
	public String toString() {
		return "AgentJoinedReply [myself=" + myself + ", myGames=" + myGames + "]";
	}
}
