package pl.edu.agh.multiagent;

import jade.android.RuntimeCallback;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.api.MoveResoultionStrategy;
import pl.edu.agh.multiagent.jade.GameAgentInterface;
import pl.edu.agh.multiagent.jade.JadeController;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private JadeController jadeController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i = getIntent();
		jadeController = new JadeController(i.getStringExtra("host"), i.getStringExtra("port"),
				this);
		jadeController.startJadeRuntimeService(new RuntimeCallback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				jadeController.startGameAgent(new RuntimeCallback<AgentController>() {
					@Override
					public void onSuccess(AgentController arg0) {
						Log.w(TAG, "Started agent");
						try {
							GameAgentInterface agent = arg0.getO2AInterface(GameAgentInterface.class);
							if (agent != null) {
								GameState gameState = GameState.newGameState("Gra Piotrka",
										MoveResoultionStrategy.FIFO, agent.getAgentInfo());
								agent.createGame(gameState);
							} else {
								Log.i(TAG, "Agent O2A is null");
							}
						} catch (StaleProxyException e) {
							throw new RuntimeException(e);
						}
					}

					@Override
					public void onFailure(Throwable arg0) {
						Log.w(TAG, "Failed to start agent");
					}
				});
			}

			@Override
			public void onFailure(Throwable arg0) {
				Log.w(TAG, "Failed to start Jade");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
