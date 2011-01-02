package com.cannonmatthews.sudoku2;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

public class Keypad extends Dialog {
	public static final String TAG = "Sudoku";
	private final View keys[] = new View[9];
	private View keypad;
	
	private final int useds[];
	private final PuzzleView puzzleView;
	
	public Keypad(Context context, int useds[], PuzzleView puzzleView){
		super(context);
		Log.d(TAG, "Keypad" + "(" + context +","+ useds+","+ puzzleView + ")");
		this.useds = useds;
		this.puzzleView = puzzleView;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		Log.d(TAG, "onCreate" + "(" + savedInstanceState + ")");
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.keypad_title);
		setContentView(R.layout.keypad);
		findViews();
		for( int element : useds){
			if (element != 0 )
				keys[element -1].setVisibility(View.INVISIBLE);
		}
		setListeners();
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		Log.d(TAG, "onKeyDown" + "(" + keyCode+ ","+ event + ")");
		int tile = 0;
		switch (keyCode){
		case KeyEvent.KEYCODE_0: 
		case KeyEvent.KEYCODE_SPACE: tile = 0;break;
		case KeyEvent.KEYCODE_1:	 tile = 1;break;
		case KeyEvent.KEYCODE_2:	 tile = 2;break;
		case KeyEvent.KEYCODE_3:	 tile = 3;break;
		case KeyEvent.KEYCODE_4:	 tile = 4;break;
		case KeyEvent.KEYCODE_5:	 tile = 5;break;
		case KeyEvent.KEYCODE_6:	 tile = 6;break;
		case KeyEvent.KEYCODE_7:	 tile = 7;break;
		case KeyEvent.KEYCODE_8:	 tile = 8;break;
		case KeyEvent.KEYCODE_9:	 tile = 9;break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		if (isValid(tile)){
			returnResult(tile);
		}
		return true;
	}
	private boolean isValid(int tile){
		Log.d(TAG, "isValid" + "(" + tile + ")");
		for(int t: useds){
			if (tile ==t ){
				return false;
			}
		}
		return true;
	}
	private void returnResult(int tile){
		Log.d(TAG, "returnResult" + "(" + tile + ")");
		puzzleView.setSelectedTile(tile);
		dismiss();
	}
	private void findViews() {
		Log.d(TAG, "findViews" + "(" +")");
		keypad = findViewById(R.id.keypad);
		keys[0]= findViewById(R.id.keypad_1);
		keys[1]= findViewById(R.id.keypad_2);
		keys[2]= findViewById(R.id.keypad_3);
		keys[3]= findViewById(R.id.keypad_4);
		keys[4]= findViewById(R.id.keypad_5);
		keys[5]= findViewById(R.id.keypad_6);
		keys[6]= findViewById(R.id.keypad_7);
		keys[7]= findViewById(R.id.keypad_8);
		keys[8]= findViewById(R.id.keypad_9);
	}
	private void setListeners() {
		Log.d(TAG, "setListeners" + "("+")");
		for( int i = 0; i<keys.length; i++){
			final int t = i+1;
			keys[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					returnResult(t);
				}
			});
		}
		keypad.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				returnResult(0);				
			}
		});
	}
}
