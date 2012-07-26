package edu.tongji.andriy;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Another3000Activity extends Activity {

	private Another3000Manager manager = null;
	
	private Button testButton1;
	private Button testButton2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.another3000_main);
		
		manager = new Another3000Manager();
		
		
		testButton1 = (Button) this.findViewById(R.id.another3000_testButton1);
		testButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(Another3000Activity.this);
				
				List<Pair<Integer, Integer>> orderList;
//				orderList= manager.GetReciteOrder();
//				orderList = manager.GetRecitedUnits();
				orderList = manager.GetNextUnits(100);
				String msg = "";
				for (Pair<Integer, Integer> pair : orderList) {
					msg += ("PDF " + (pair.first + 1) + "; UNIT " + (pair.second + 1) + "\n");
				}
				
				builder.setMessage(msg);
				builder.create().show();
			}
		});
		
		testButton2 = (Button) this.findViewById(R.id.another3000_testButton2);
		testButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				manager.RandomizeReciteOrder();
			}
		});
	}

}
