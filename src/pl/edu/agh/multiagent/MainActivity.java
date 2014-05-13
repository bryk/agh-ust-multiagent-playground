package pl.edu.agh.multiagent;

import jade.android.RuntimeCallback;
import jade.wrapper.AgentController;
import pl.edu.agh.multiagent.jade.JadeController;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		jadeController = new JadeController(i.getStringExtra("host"), i.getStringExtra("port"), this);
		jadeController.startJadeRuntimeService(new RuntimeCallback<Void>() {
			@Override
			public void onSuccess(Void nothing) {
				Log.i(TAG, "Succesfully started chat");
				jadeController.startGameAgent(new RuntimeCallback<AgentController>() {
					@Override
					public void onFailure(Throwable arg0) {
						Log.i(TAG, "Failed:" + arg0);
					}

					@Override
					public void onSuccess(AgentController agentController) {
						Log.i(TAG, "Started agent:" + agentController);
					}
				});
			}

			@Override
			public void onFailure(Throwable arg0) {
				Log.i(TAG, "Failed:" + arg0);
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
