package edu.tongji.andriy.another3000;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.tongji.andriy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class A3KActivity extends Activity {

	private A3KManager manager = null;
	
	private Button testButton1;
	private Button testButton2;
	
	private ListView toReciteListView;
	private ListView recitedListView;
	
	private A3KIndicesListAdapter toReciteListAdapter = new A3KIndicesListAdapter();
	private A3KIndicesListAdapter recitedListAdapter = new A3KIndicesListAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.another3000_main);
		
		manager = new A3KManager();
		
//		testButton1 = (Button) this.findViewById(R.id.a3k_testButton1);
//		testButton2 = (Button) this.findViewById(R.id.a3k_testButton2);
		testButton1 = new Button(this);
		testButton2 = new Button(this);

		toReciteListView = (ListView) this.findViewById(R.id.a3k_toReciteListView);
		recitedListView = (ListView) this.findViewById(R.id.a3k_recitedListView);
		
		toReciteListAdapter.FillData(manager.GetNextUnits());
		recitedListAdapter.FillData(manager.GetRecitedUnits());
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		testButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(A3KActivity.this);
				
				List<A3KIndex> orderList;
//				orderList= manager.GetReciteOrder();
				orderList = manager.GetRecitedUnits();
//				orderList = manager.GetNextUnits(100);
				toReciteListAdapter.FillData(orderList);
				toReciteListAdapter.notifyDataSetChanged();
				String msg = "";
				for (A3KIndex index : orderList) {
					msg += ("PDF " + (index.GetPDF() + 1) + "; UNIT " + (index.GetUnit() + 1) + "\n");					
				}
				
				builder.setMessage(msg);
//				builder.create().show();
			}
		});
		
		testButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				manager.RandomizeReciteOrder();
				
				AlertDialog.Builder builder = new Builder(A3KActivity.this);
				
				List<A3KIndex> orderList;
//				orderList= manager.GetReciteOrder();
//				orderList = manager.GetRecitedUnits();
				orderList = manager.GetNextUnits(100);
				recitedListAdapter.FillData(orderList);
				recitedListAdapter.notifyDataSetChanged();
				String msg = "";
				for (A3KIndex index : orderList) {
					msg += ("PDF " + (index.GetPDF() + 1) + "; UNIT " + (index.GetUnit() + 1) + "\n");					
				}
				
				builder.setMessage(msg);
//				builder.create().show();
			}
		});
		
		toReciteListView.setAdapter(toReciteListAdapter);
		recitedListView.setAdapter(recitedListAdapter);
	}

	
	private class A3KIndicesListAdapter extends BaseAdapter {

		private final List<A3KIndex> indexList = new ArrayList<A3KIndex>();
		
		/**
		 * 填充数据进来，原本的数据（如果有的话）会被换掉
		 * @param data
		 */
		public void FillData(Collection<? extends A3KIndex> data) {
			this.indexList.clear();
			this.indexList.addAll(data);
		}
		
		public void Refresh() {
			this.notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return this.indexList.isEmpty() ? 1 : this.indexList.size();
		}

		@Override
		public A3KIndex getItem(int position) {
			return indexList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (this.indexList.isEmpty()) {
				TextView textView = new TextView(A3KActivity.this);
				textView.setText("空");
				
				convertView = textView;
			}
			else {
				A3KIndex index = this.getItem(position);
				
				String pdfString = Integer.toString(index.GetPDF() + 1);
				if (index.GetPDF()+1 < 10) {
					pdfString = "0" + pdfString;
				}
				String unitString = Integer.toString(index.GetUnit() + 1);
				if (index.GetUnit()+1 < 10) {
					unitString = "0" + unitString;
				}
				
				TextView textView = new TextView(A3KActivity.this);
				textView.setText("List_" + pdfString + " -- " + " Unit_" + unitString);
				convertView = textView;
			}
			
			return convertView;
		}
	}

}
