package edu.tongji.andriy;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RandriyActivity extends Activity {

	private final Random randomGenerator = new Random();

	private int intRange = 2;

	private TextView intRangeText;
	private Button nextIntButton;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        intRangeText = (TextView) this.findViewById(R.id.intRangeText);
        intRangeText.setText(Integer.toString(intRange));
        intRangeText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SetIntRange();
			}
		});
        
        nextIntButton = (Button) this.findViewById(R.id.nextIntButton);
        nextIntButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int result = randomGenerator.nextInt(intRange);
				ShowIntResult(result);
			}
		});
    }
    
    /**
     * 显示整数随机的结果
     * @param result
     */
    private void ShowIntResult(int result) {
		AlertDialog.Builder builder = new Builder(RandriyActivity.this);
		builder.setTitle("随机整数: ");
		builder.setMessage(Integer.toString(result));
		builder.create().show();    	
    }
    
    /**
     * by pop up a dialog
     */
    private void SetIntRange() {
    	AlertDialog.Builder builder = new Builder(this);
    	builder.setTitle("设置整数范围：[0, ?)");
    	View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_int_range, null);
    	final EditText inputText = (EditText) view.findViewById(R.id.dialog_intRangeEditText);
    	builder.setView(view);
    	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					intRange = Integer.parseInt(inputText.getText().toString());
					if (intRange == 0) {
						Toast.makeText(RandriyActivity.this, "你怎么可以输入0！", Toast.LENGTH_SHORT).show();
						intRange = 1;
					}
					intRangeText.setText(Integer.toString(intRange));
				} catch (Exception e) {
				}
			}
    	});
    	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				intRangeText.setText(Integer.toString(intRange));
			}
		});
    	builder.create().show();
    }
}