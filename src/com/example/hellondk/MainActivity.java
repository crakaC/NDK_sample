package com.example.hellondk;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import com.actionbarsherlock.view.MenuItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {

	final int CUSTOM_DIALOG = 1;
	TextView tv1,tv2;
	int n = 35;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv1 = (TextView)findViewById(R.id.textView1);
		tv1.setText("0");
		tv2 = (TextView)findViewById(R.id.textView2);
		tv2.setText("0");
		
		findViewById(R.id.fibo).setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				fiboJava(n);
				showToast("Start fibo("+n+") @ java");
			}
		});
		
		findViewById(R.id.fibo_native).setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				fiboNative(n);
				showToast("Start fibo("+n+") @ native");
			}
		});
		
		findViewById(R.id.reset).setOnClickListener( new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tv1.setText("0");
				tv2.setText("0");
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			showDialog(CUSTOM_DIALOG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CUSTOM_DIALOG:
			LayoutInflater factory = LayoutInflater.from(this);
			final View inputView = factory.inflate(R.layout.input_dialog, null);
			return new AlertDialog.Builder(MainActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Input value of N ( now : "+ n +")")
				.setView(inputView)
				.setPositiveButton("Set", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText et =  (EditText)inputView.findViewById(R.id.dialog_edittext);
						if ( et == null ){
							showToast("please enter any number");
							return ;
						}
						String str = et.getText().toString();
						n = Integer.parseInt( str );
						showToast("set n " + n);
					}
					
				})
				.setNegativeButton("Cancel", null)
				.create();
		}
		return null;
	}
	
	

	@Override
	@Deprecated
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case CUSTOM_DIALOG:
			dialog.setTitle("Input value of N ( now : "+ n +")");
		}
	}

	private void fiboJava(int n) {
		final ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar1);
		final Button btn = (Button)findViewById(R.id.fibo);
		AsyncTask<Integer, Void, Long> task = new AsyncTask<Integer, Void, Long>(){
			
			
			@Override
			protected void onPreExecute() {
				bar.setVisibility(View.VISIBLE);
				tv1.setVisibility(View.GONE);
				btn.setEnabled(false);
			}

			@Override
			protected Long doInBackground(Integer... params) {
				return fibo(params[0]);
			}
			
			@Override
			protected void onPostExecute(Long result) {
				tv1.setVisibility(View.VISIBLE);
				btn.setEnabled(true);
				bar.setVisibility(View.GONE);
				tv1.setText(String.valueOf(result));
			}
		};
		task.execute(n);
	}

	private void fiboNative(int n) {
		final ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar2);
		final Button btn = (Button)findViewById(R.id.fibo_native);
		AsyncTask<Integer, Void, Long> task = new AsyncTask<Integer, Void, Long>(){
			
			@Override
			protected void onPreExecute() {
				bar.setVisibility(View.VISIBLE);
				tv2.setVisibility(View.GONE);
				btn.setEnabled(false);
			}
			
			@Override
			protected Long doInBackground(Integer... params) {
				return fiboCpp(params[0]);
			}

			@Override
			protected void onPostExecute(Long result) {
				tv2.setVisibility(View.VISIBLE);
				btn.setEnabled(true);
				bar.setVisibility(View.GONE);
				tv2.setText(String.valueOf(result));
			}
		};
		task.execute(n);
	}
	
	
	private long fibo(int n) {
		if (n < 3) {
			return 1;
		}
		return fibo(n-1) + fibo(n-2);
	}
	
	private native long fiboCpp(int n);
	static {
		System.loadLibrary("fibo");
	}
	
	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
