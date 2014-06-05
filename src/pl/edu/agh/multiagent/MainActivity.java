package pl.edu.agh.multiagent;

import java.util.ArrayList;
import java.util.List;

import jade.android.RuntimeCallback;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.api.MoveResoultionStrategy;
import pl.edu.agh.multiagent.jade.GameAgentInterface;
import pl.edu.agh.multiagent.jade.JadeController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private JadeController jadeController;
	
	private GameAgentInterface agent;
	
	private LinearLayout menuLayout;
	
	private LinearLayout gameLayout;
	
	private LinearLayout gameFinderLayout;
	
	private LinearLayout myGamesLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menuLayout = (LinearLayout) findViewById(R.id.mainFormLayout);
		gameLayout = (LinearLayout) findViewById(R.id.gameLayout);
		gameFinderLayout = (LinearLayout) findViewById(R.id.gameFinder);
		myGamesLayout = (LinearLayout) findViewById(R.id.myGames);
		
		menuLayout.setVisibility(View.VISIBLE);
		gameLayout.setVisibility(View.GONE);
		gameFinderLayout.setVisibility(View.GONE);
		myGamesLayout.setVisibility(View.GONE);
		
		Intent i = getIntent();
		jadeController = new JadeController(i.getStringExtra("host"),
				i.getStringExtra("port"), getApplicationContext());
		jadeController.startJadeRuntimeService(new RuntimeCallback<Void>() {
			@Override
			public void onSuccess(Void arg0) {
				Log.w(TAG, "Success 1");
				jadeController
						.startGameAgent(new RuntimeCallback<AgentController>() {
							@Override
							public void onSuccess(AgentController arg0) {
								Log.w(TAG, "Started agent");
								try {
									GameAgentInterface agent = arg0
											.getO2AInterface(GameAgentInterface.class);
									if (agent != null) {
										MainActivity.this.agent = agent;
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

	public void startGame(View view) {
		Log.w(TAG, "Start Game click");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		ViewGroup dialog = (ViewGroup) getLayoutInflater().inflate(
				R.layout.dialog_start_game, null);
		final EditText gameNameView = (EditText) dialog.findViewById(R.id.enterGameName);
		final Spinner choiceSpinner = (Spinner) dialog
				.findViewById(R.id.moveResolutionSpinner);
		List<String> list = new ArrayList<String>();
		for(MoveResoultionStrategy s : MoveResoultionStrategy.values()){
			list.add(s.toString());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);

		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		choiceSpinner.setAdapter(dataAdapter);
		builder.setView(dialog)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						agent.createGame(GameState.newGameState(gameNameView.getText().toString(), MoveResoultionStrategy.valueOf(choiceSpinner.getSelectedItem().toString()), agent.getAgentInfo()));
						//TODO move to game view
						switchLayout(gameLayout);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		builder.create().show();

	}

	public void browseGames(View view) {
		Log.w(TAG, "Browse click");

	}

	public void myGames(View view) {
		Log.w(TAG, "My Games click");

	}
	
	private void switchLayout(LinearLayout l){
		menuLayout.setVisibility(View.GONE);
		gameLayout.setVisibility(View.GONE);
		gameFinderLayout.setVisibility(View.GONE);
		myGamesLayout.setVisibility(View.GONE);
		l.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onBackPressed() {
	    if(menuLayout.getVisibility() == View.VISIBLE){
	    	super.onBackPressed();
	    }
	    else{
	    	switchLayout(menuLayout);
	    }
	}
}
