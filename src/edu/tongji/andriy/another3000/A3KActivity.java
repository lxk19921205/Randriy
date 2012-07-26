package edu.tongji.andriy.another3000;

import java.util.List;

import edu.tongji.andriy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class A3KActivity extends Activity {

	private A3KManager manager = null;
	
	private Button testButton1;
	private Button testButton2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.another3000_main);
		
		manager = new A3KManager();
		
		
		testButton1 = (Button) this.findViewById(R.id.another3000_testButton1);
		testButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(A3KActivity.this);
				
				List<A3KIndex> orderList;
//				orderList= manager.GetReciteOrder();
				orderList = manager.GetRecitedUnits();
//				orderList = manager.GetNextUnits(100);
				String msg = "";
				for (A3KIndex index : orderList) {
					msg += ("PDF " + (index.GetPDF() + 1) + "; UNIT " + (index.GetUnit() + 1) + "\n");					
				}
				
				builder.setMessage(msg);
				builder.create().show();
			}
		});
		
		testButton2 = (Button) this.findViewById(R.id.another3000_testButton2);
		testButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				manager.RandomizeReciteOrder();
				
				AlertDialog.Builder builder = new Builder(A3KActivity.this);
				
				List<A3KIndex> orderList;
//				orderList= manager.GetReciteOrder();
//				orderList = manager.GetRecitedUnits();
				orderList = manager.GetNextUnits(100);
				String msg = "";
				for (A3KIndex index : orderList) {
					msg += ("PDF " + (index.GetPDF() + 1) + "; UNIT " + (index.GetUnit() + 1) + "\n");					
				}
				
				builder.setMessage(msg);
				builder.create().show();
			}
		});
	}

}
