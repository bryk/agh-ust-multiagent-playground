package pl.edu.agh.multiagent;

import pl.edu.agh.multiagent.api.Cell;
import pl.edu.agh.multiagent.api.GameState;
import pl.edu.agh.multiagent.jade.GameAgentInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Board {

	private static final String TAG = "Board";

	private ImageButton[][] buttons;
	private GameState state;
	private GameAgentInterface agent;
	private Cell player;

	public Board(View grid, GameAgentInterface agent, GameState state) {
		buttons = new ImageButton[3][3];
		this.agent = agent;
		this.state = state;
		if (state.getOwner().equals(agent.getAgentInfo())) {
			player = Cell.X;
		} else {
			player = Cell.O;
		}
		loadButtons(grid);
		init();
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
		} else if (!state.getOwner().equals(agent.getAgentInfo()) && state.getMoveNumber() % 2 == 0) {
			Log.i(TAG, "Not your move 2");
			return;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].equals(v)) {
					state.setCell(i, j, player);
					switch (player) {
					case X:
						((ImageButton) v).setImageResource(R.drawable.x);
						break;
					case O:
						((ImageButton) v).setImageResource(R.drawable.o);
						break;
					}
				}
			}
		}
		if(checkVictory()){
			//TODO What to do when the game ends? does something happen on winner moving, or on host?
		}
		Log.i(TAG, "Move almost made");
		// TODO that doesn't work, and it should somehow
		// agent.updateGameState(state);
	}

	private boolean checkVictory() {
		return checkCross() || checkColumns() || checkRows();

	}

	private boolean checkRows() {
		for (int y = 0; y < 3; y++) {
			int total = 0;
			for (int x = 0; x < 3; x++) {
				if (state.getCells()[x][y] == player) {
					total++;
				}
			}
			if (total >= 3) {
				return true; // they win
			}
		}
		return false;
	}

	private boolean checkColumns() {
		for (int x = 0; x < 3; x++) {
			int total = 0;
			for (int y = 0; y < 3; y++) {
				if (state.getCells()[x][y] == player) {
					total++;
				}
			}
			if (total >= 3) {
				return true; // they win
			}
		}
		return false;
	}

	private boolean checkCross() {
		int right = 0;
		int left = 0;
		for (int i = 0; i < 3; i++) {
			if (state.getCells()[i][i].equals(player)) {
				right++;
			}
			if (state.getCells()[2 - i][i].equals(player)) {
				left++;
			}
		}
		return (right == 3 || left == 3);

	}

}
