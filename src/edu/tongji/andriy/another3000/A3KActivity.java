package edu.tongji.andriy.another3000;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.tongji.andriy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

public class A3KActivity extends Activity {

	private static final String EXTERNAL_DATA_FILENAME = "randriy_a3k_recited.dat";
	
	private A3KManager manager = null;

	private Button pickOneButton;
	private Button clearDoneButton;
	
	private ListView toReciteListView;
	private ListView recitedListView;
	
	private A3KIndicesListAdapter toReciteListAdapter = new A3KIndicesListAdapter();
	private A3KIndicesListAdapter recitedListAdapter = new A3KIndicesListAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.a3k_main);
		
		manager = new A3KManager();
		manager.LoadFromDB(this);
		
		pickOneButton = (Button) this.findViewById(R.id.a3k_pickOneButton);
		clearDoneButton = (Button) this.findViewById(R.id.a3k_clearDoneButton);

		toReciteListView = (ListView) this.findViewById(R.id.a3k_toReciteListView);
		recitedListView = (ListView) this.findViewById(R.id.a3k_recitedListView);
		
		this.RefreshToReciteList();
		this.RefreshRecitedList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				manager.SaveIntoDB(A3KActivity.this);				
			}
		});
		thread.start();
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
		
		clearDoneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(A3KActivity.this);
				builder.setTitle("CLEAR ALL RECITED, ARE YOU SURE?");
				builder.setPositiveButton("CLEAR", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						manager.ClearRecited();
						RefreshToReciteList();
						RefreshRecitedList();
					}
				});
				builder.setNegativeButton("KEEP IT", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void RefreshToReciteList() {
		toReciteListAdapter.FillData(manager.GetNextUnits());
		toReciteListAdapter.Refresh();
	}
	
	private void RefreshRecitedList() {
		recitedListAdapter.FillData(manager.GetRecitedUnits());
		recitedListAdapter.Refresh();
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
			this.RefreshToReciteList();
			return true;
			
		case R.id.a3k_menu_sorted:
			manager.SortReciteOrder();
			this.RefreshToReciteList();
			return true;
			
		case R.id.a3k_menu_import: 
			this.doImport();
			return true;
			
		case R.id.a3k_menu_export:
			this.doExport();
			return true;

		default:
			return false;
		}
	}
	
	/**
	 * Import data from SDCard
	 */
	private void doImport() {
		if (!this.isSDCardReady()) {
			Toast.makeText(this, "SDCard not ready", Toast.LENGTH_SHORT).show();
			return;
		}
		
		File inputFile = new File(this.getExternalFilesDir(null), EXTERNAL_DATA_FILENAME);
		if (!inputFile.exists()) {
			Toast.makeText(this, "No previous data", Toast.LENGTH_SHORT).show();
			return;
		}
		
		List<A3KIndex> recitedUnits = new ArrayList<A3KIndex>();
		try {
			FileInputStream fis = new FileInputStream(inputFile);
			DataInputStream dis = new DataInputStream(fis);
			while (true) {
				try {
					int index = dis.readInt();
					recitedUnits.add(new A3KIndex(index));					
				}
				catch (IOException e) {
					break;
				}
			}
			dis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.manager.setRecitedUnits(recitedUnits);
		Toast.makeText(this, "Imported", Toast.LENGTH_SHORT).show();
		this.RefreshToReciteList();
		this.RefreshRecitedList();
	}
	
	/**
	 * Export data to SDCard
	 */
	private void doExport() {
		if (!this.isSDCardReady()) {
			Toast.makeText(this, "SDCard not ready", Toast.LENGTH_SHORT).show();
			return;
		}
		
		File outputFile = new File(this.getExternalFilesDir(null), EXTERNAL_DATA_FILENAME);
		List<A3KIndex> recitedUnits = this.manager.GetRecitedUnits();
		try {
			FileOutputStream fos = new FileOutputStream(outputFile, false);
			DataOutputStream dos = new DataOutputStream(fos);
			for (A3KIndex unit : recitedUnits) {
				dos.writeInt(unit.GetTotalIndex());
			}
			dos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Toast.makeText(this, "Exported", Toast.LENGTH_SHORT).show();
	}

	/**
	 * @return whether SDCard is mounted
	 */
	private boolean isSDCardReady() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
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
