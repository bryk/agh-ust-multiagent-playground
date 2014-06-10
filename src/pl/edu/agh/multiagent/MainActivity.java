package pl.edu.agh.multiagent;

import jade.android.RuntimeCallback;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.api.MoveResoultionStrategy;
import pl.edu.agh.multiagent.api.State;
import pl.edu.agh.multiagent.jade.GameAgentInterface;
import pl.edu.agh.multiagent.jade.JadeController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";

	private JadeController jadeController;
	
	private GameAgentInterface agent;
	
	private LinearLayout menuLayout;
	
	private LinearLayout gameLayout;
	
	private LinearLayout gameFinderLayout;
	
	private LinearLayout myGamesLayout;
	
	private Board currentBoard;

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
						GameState state = GameState.newGameState(gameNameView.getText().toString(), MoveResoultionStrategy.valueOf(choiceSpinner.getSelectedItem().toString()), agent.getAgentInfo());
						agent.createGame(state);
						
						switchLayout(gameLayout);
						initGameFromState(state);
						
						
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
		switchLayout(gameFinderLayout);
		TableLayout table = (TableLayout) findViewById(R.id.otherGamesTable);
		table.removeAllViews();
		addHeader(table);
		for(GameState s :agent.getAllActiveGames()){
			if(s.getState().equals(State.OPEN)){
				table.addView(new MyTableRow(this, s, this));
			}
		}

	}

	public void myGames(View view) {
		switchLayout(gameFinderLayout);
		TableLayout table = (TableLayout) findViewById(R.id.otherGamesTable);
		table.removeAllViews();
		addHeader(table);
		for(GameState s :agent.getMyGames()){
			if(s.getState().equals(State.OPEN)){
				table.addView(new MyTableRow(this, s, this));
			}
		}

	}
	
	@SuppressWarnings("deprecation")
	private void addHeader(TableLayout table) {
		TableRow row = new TableRow(this);
		
		String owner = "Game owner";
		TextView ownerView = new TextView(this);
		ownerView.setText(owner);
		row.addView(ownerView);
		
		String gameName = "Game name";
		TextView nameView = new TextView(this);
		nameView.setText(gameName);
		row.addView(nameView);
		
		String whose = "Move";
		TextView whoseView = new TextView(this);
		whoseView.setText(whose);
		row.addView(whoseView);
		
		String moveNumber = "Move number";
		TextView moveView = new TextView(this);
		moveView.setText(moveNumber);
		row.addView(moveView);
		
		table.addView(row);
		
		View line = new View(this);
		line.setBackgroundColor(Color.BLACK);
		line.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1));
		
		table.addView(line);
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
	
	@Override
	public void onDestroy(){
		//TODO clean agent? stop container? stop service? nothing? dunno
		super.onDestroy();
	}
	
	private void initGameFromState(GameState state){
		Board board = new Board(findViewById(R.id.board), agent, state, this, this);
		agent.setGameAgentListener(board);
		currentBoard = board;
	}
	
	public void boardClick(View v){
		currentBoard.click(v);
	}

	
	/**
	 * use only for choosing game row
	 */
	@Override
	public void onClick(View v) {
		switchLayout(gameLayout);
		initGameFromState(((MyTableRow) v).getState());
		
	}
}
