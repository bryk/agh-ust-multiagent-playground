package pl.edu.agh.multiagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FullscreenActivity extends Activity implements OnClickListener {
	View contentView;
	View layout1;
	View layout2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_fullscreen);
		
		contentView = findViewById(R.id.mainFormLayout);
		layout1 = findViewById(R.id.linear1);
		layout2 = findViewById(R.id.linear2);

		

		Button button = (Button) contentView.findViewById(R.id.confirmParamsButton);
		button.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent myIntent = new Intent(FullscreenActivity.this, MainActivity.class);
		String host = ((EditText)findViewById(R.id.ipFormText)).getText().toString();
		String port = ((EditText)findViewById(R.id.portFormText)).getText().toString();
		myIntent.putExtra("host", host);
		myIntent.putExtra("port", port);
		
		FullscreenActivity.this.startActivity(myIntent);
	}
}
