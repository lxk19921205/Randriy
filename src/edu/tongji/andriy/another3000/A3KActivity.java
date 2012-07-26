package edu.tongji.andriy.another3000;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.tongji.andriy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class A3KActivity extends Activity {

	private A3KManager manager = null;

	
	private Button pickOneButton;
	
	private ListView toReciteListView;
	private ListView recitedListView;
	
	private A3KIndicesListAdapter toReciteListAdapter = new A3KIndicesListAdapter();
	private A3KIndicesListAdapter recitedListAdapter = new A3KIndicesListAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.another3000_main);
		
		manager = new A3KManager();
		manager.LoadFromDB(this);
		
		pickOneButton = (Button) this.findViewById(R.id.a3k_pickOneButton);

		toReciteListView = (ListView) this.findViewById(R.id.a3k_toReciteListView);
		recitedListView = (ListView) this.findViewById(R.id.a3k_recitedListView);
		
		this.RefreshToReciteList();
		this.RefreshRecitedList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		manager.SaveIntoDB(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		toReciteListView.setAdapter(toReciteListAdapter);
		recitedListView.setAdapter(recitedListAdapter);
		
		pickOneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<A3KIndex> indices = manager.GetNextUnits(1);
				AlertDialog.Builder builder = new Builder(A3KActivity.this);
				builder.setTitle("PICK ONE");
				if (indices.isEmpty()) {
					builder.setMessage("好高兴哦，都背完了！");
				}
				else {
					final A3KIndex index = indices.get(0);
					builder.setMessage(index.toString());
					builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							manager.SetUnitStudied(index, true);
							RefreshToReciteList();
							RefreshRecitedList();
						}
					});
					builder.setNegativeButton("GO", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
				}
				builder.create().show();
			}
		});
	}
	
	private void RefreshToReciteList() {
		toReciteListAdapter.FillData(manager.GetNextUnits());
		toReciteListAdapter.notifyDataSetChanged();
	}
	
	private void RefreshRecitedList() {
		recitedListAdapter.FillData(manager.GetRecitedUnits());
		recitedListAdapter.notifyDataSetChanged();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.a3k_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.a3k_menu_random:
			manager.RandomizeReciteOrder();
			toReciteListAdapter.FillData(manager.GetNextUnits());
			toReciteListAdapter.Refresh();
			return true;
			
		case R.id.a3k_menu_sorted:
			manager.SortReciteOrder();
			toReciteListAdapter.FillData(manager.GetNextUnits());
			toReciteListAdapter.Refresh();
			return true;

		default:
			return false;
		}
	}


	
	/**
	 * 用于显示to_recite和recited的ListView的Adapter
	 * @author Andriy
	 */
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
				TextView textView = new TextView(A3KActivity.this);
				textView.setText(this.getItem(position).toString());
				convertView = textView;
			}
			
			return convertView;
		}
	}

}
