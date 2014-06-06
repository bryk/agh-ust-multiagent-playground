package pl.edu.agh.multiagent;

import pl.edu.agh.multiagent.api.GameState;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;
import android.widget.TextView;

public class MyTableRow extends TableRow {
	
	private Context context;
	private GameState state;
	
	public MyTableRow(Context context, GameState state, OnClickListener l) {
		super(context);
		this.context = context;
		this.state = state;
		init( l);
	}

	public MyTableRow(Context context, AttributeSet attrs, GameState state, OnClickListener l) {
		super(context, attrs);
		init( l);
	}
	
	public GameState getState(){
		return state;
	}
	
	private void init( OnClickListener l) {
		String owner = state.getOwner().getName();
		TextView ownerView = new TextView(context);
		ownerView.setText(owner);
		addView(ownerView);
		
		String gameName = state.getName();
		TextView nameView = new TextView(context);
		nameView.setText(gameName);
		addView(nameView);
		
		String whose = (state.getMoveNumber() % 2 == 0) ? "X" : "O";
		TextView whoseView = new TextView(context);
		whoseView.setText(whose);
		addView(whoseView);
		
		String moveNumber = String.valueOf(state.getMoveNumber());
		TextView moveView = new TextView(context);
		moveView.setText(moveNumber);
		addView(moveView);
		
		setClickable(true);
		setOnClickListener(l);
		
		setPadding(0, 5, 0, 5);
	}	

}
