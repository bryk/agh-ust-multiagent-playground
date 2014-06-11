package pl.edu.agh.multiagent;

import pl.edu.agh.multiagent.api.Cell;
import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.api.State;
import pl.edu.agh.multiagent.jade.GameAgentInterface;
import pl.edu.agh.multiagent.jade.GameAgentListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Board implements GameAgentListener {

	private static final String TAG = "Board";

	private ImageButton[][] buttons;
	private GameState state;
	private GameAgentInterface agent;
	private Cell playersCellMarker;
	private Activity activity;
	private boolean alertShown = false;

	public Board(View grid, GameAgentInterface agent, GameState state,
			Context context, Activity activity) {
		buttons = new ImageButton[3][3];
		this.agent = agent;
		this.state = state;
		if (state.getOwner().equals(agent.getAgentInfo())) {
			playersCellMarker = Cell.X;
		} else {
			playersCellMarker = Cell.O;
		}
		loadButtons(grid);
		init();
		this.activity = activity;
	}

	private void init() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				switch (state.getCells()[i][j]) {
				case X:
					buttons[i][j].setImageResource(R.drawable.x);
					break;
				case NOTHING:
					buttons[i][j].setImageResource(R.drawable.nothing);
					break;
				case O:
					buttons[i][j].setImageResource(R.drawable.o);
					break;
				default:
					break;
				}
			}
		}
	}

	private void loadButtons(View grid) {
		TableLayout table = (TableLayout) grid;
		buttons[0][0] = (ImageButton) ((TableRow) table.getChildAt(0))
				.getChildAt(0);
		buttons[0][1] = (ImageButton) ((TableRow) table.getChildAt(0))
				.getChildAt(1);
		buttons[0][2] = (ImageButton) ((TableRow) table.getChildAt(0))
				.getChildAt(2);
		buttons[1][0] = (ImageButton) ((TableRow) table.getChildAt(1))
				.getChildAt(0);
		buttons[1][1] = (ImageButton) ((TableRow) table.getChildAt(1))
				.getChildAt(1);
		buttons[1][2] = (ImageButton) ((TableRow) table.getChildAt(1))
				.getChildAt(2);
		buttons[2][0] = (ImageButton) ((TableRow) table.getChildAt(2))
				.getChildAt(0);
		buttons[2][1] = (ImageButton) ((TableRow) table.getChildAt(2))
				.getChildAt(1);
		buttons[2][2] = (ImageButton) ((TableRow) table.getChildAt(2))
				.getChildAt(2);
	}

	public void click(View v) {
		Log.i(TAG, "Move makin");
		if (state.getOwner().equals(agent.getAgentInfo())
				&& state.getMoveNumber() % 2 == 1) {
			Log.i(TAG, "Not your move 1");
			return;
		} else if (!state.getOwner().equals(agent.getAgentInfo())
				&& state.getMoveNumber() % 2 == 0) {
			Log.i(TAG, "Not your move 2");
			return;
		} else if (state.getState() != null
				&& state.getState().equals(State.FINISHED)) {
			Log.i(TAG, "Game finished already");
			return;
		}

		if (state.getState() != State.FINISHED) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (buttons[i][j].equals(v)) {
						if (!state.getCells()[i][j].equals(Cell.NOTHING)) {
							// no move overridin
							return;
						}
						state.setCell(i, j, playersCellMarker);
						switch (playersCellMarker) {
						case X:
							((ImageButton) v).setImageResource(R.drawable.x);
							break;
						case O:
							((ImageButton) v).setImageResource(R.drawable.o);
							break;
						default:
							throw new RuntimeException("Invalid player's cell");
						}
					}
				}
			}
		}
		handleLostWon();

		if (state.getOwner().equals(agent.getAgentInfo())) {
			agent.updateGameState(state);
		} else {
			agent.makeMove(state);
		}

		if (state.getMoveNumber() == 9) {
			handleEndGame();
		}
	}

	private void handleEndGame() {
		if (state.getState() != State.FINISHED) {
			state.setState(State.FINISHED);
		}
		if (!alertShown) {
				this.alertShown = true;
				this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								activity);
						builder.setMessage("Game ended without a winner")
								.setCancelable(false)
								.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// if this button is clicked,
												// close
												// current activity
												dialog.dismiss();
											}
										}).show();
					}
				});
			}

	}

	private void handleLostWon() {
		if (checkVictory(Cell.X) || checkVictory(Cell.O)) {
			state.setState(State.FINISHED);
			if (!alertShown) {
				this.alertShown = true;
				this.activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (state.getOwner().equals(agent.getAgentInfo())) {
							if (state.getMoveNumber() % 2 != 0) {
								victory();
							} else {
								lost();
							}
						} else {
							if (state.getMoveNumber() % 2 == 0) {
								victory();
							} else {
								lost();
							}
						}
					}
				});
			}
		}
	}

	private void victory() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("You have won the game")
				.setCancelable(false)
				.setPositiveButton("Yay!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.dismiss();
							}
						}).show();
	}

	private void lost() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("You have lost the game")
				.setCancelable(false)
				.setNegativeButton("Oh no!",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.dismiss();
							}
						}).show();
	}

	private boolean checkVictory() {
		return checkCross(playersCellMarker) || checkColumns(playersCellMarker) || checkRows(playersCellMarker);
	}
	
	private boolean checkVictory(Cell c) {
		return checkCross(c) || checkColumns(c) || checkRows(c);
	}

	private boolean checkRows(Cell c) {
		for (int y = 0; y < 3; y++) {
			int total = 0;
			for (int x = 0; x < 3; x++) {
				if (state.getCells()[x][y] == c) {
					total++;
				}
			}
			if (total >= 3) {
				return true; // they win
			}
		}
		return false;
	}

	private boolean checkColumns(Cell c) {
		for (int x = 0; x < 3; x++) {
			int total = 0;
			for (int y = 0; y < 3; y++) {
				if (state.getCells()[x][y] == c) {
					total++;
				}
			}
			if (total >= 3) {
				return true; // they win
			}
		}
		return false;
	}

	private boolean checkCross(Cell c) {
		int right = 0;
		int left = 0;
		for (int i = 0; i < 3; i++) {
			if (state.getCells()[i][i].equals(c)) {
				right++;
			}
			if (state.getCells()[2 - i][i].equals(c)) {
				left++;
			}
		}
		return (right == 3 || left == 3);

	}

	@Override
	public void onGameState(GameState state) {
		Log.i(TAG, "Move accepted");
		if (this.state.getUuid().equals(state.getUuid())) {
			Log.i(TAG, "Move accepted, repainting");
			this.state = state;
			this.activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					init();
				}
			});
			handleLostWon();
			if (state.getMoveNumber() == 9) {
				handleEndGame();
			}
		}
	}
}
