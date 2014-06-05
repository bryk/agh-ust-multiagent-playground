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
	
	public Board(View grid, GameAgentInterface agent, GameState state){
		buttons = new ImageButton[3][3];
		this.agent = agent;
		this.state = state;
		loadButtons(grid);
		init();
	}

	private void init() {
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				switch(state.getCells()[i][j]){
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
		TableRow t = (TableRow) table.getChildAt(0);
		if(t == null){
			Log.e(TAG, "dupa");
		}
		buttons[0][0] = (ImageButton) ((TableRow) table.getChildAt(0)).getChildAt(0);
		buttons[0][1] = (ImageButton) ((TableRow) table.getChildAt(0)).getChildAt(1);
		buttons[0][2] = (ImageButton) ((TableRow) table.getChildAt(0)).getChildAt(2);
		buttons[1][0] = (ImageButton) ((TableRow) table.getChildAt(1)).getChildAt(0);
		buttons[1][1] = (ImageButton) ((TableRow) table.getChildAt(1)).getChildAt(1);
		buttons[1][2] = (ImageButton) ((TableRow) table.getChildAt(1)).getChildAt(2);
		buttons[2][0] = (ImageButton) ((TableRow) table.getChildAt(2)).getChildAt(0);
		buttons[2][1] = (ImageButton) ((TableRow) table.getChildAt(2)).getChildAt(1);
		buttons[2][2] = (ImageButton) ((TableRow) table.getChildAt(2)).getChildAt(2);
	}
	
	public void click(View v){
		Log.i(TAG, "Move makin");
		Cell cell;
		if(state.getOwner().equals(agent.getAgentInfo())){
			if(state.getMoveNumber() % 2 == 1){
				//not Xs move
				return;
			}
			cell = Cell.X;
		}
		else{
			if(state.getMoveNumber() % 2 == 0){
				//not Os move
				return;
			}
			cell = Cell.O;
		}
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				if(buttons[i][j].equals(v)){
					state.setCell(i, j, cell);
					switch(cell){
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
		Log.i(TAG, "Move almost made");
		//TODO that doesn't work, and it should somehow
		//agent.updateGameState(state);
	}

}
