package pl.edu.agh.multiagent.jade;

import android.util.Log;
import jade.core.behaviours.CyclicBehaviour;

public class GameAgentBehaviour extends CyclicBehaviour {
	private static final String TAG = "GameAgentBehaviour";
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		Log.i(TAG, "Action");
		block();
	}
}
