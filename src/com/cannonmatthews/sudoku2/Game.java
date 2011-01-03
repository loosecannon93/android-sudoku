package com.cannonmatthews.sudoku2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game extends Activity {
	private static final String TAG = "Sudoku";
	public static final String KEY_DIFFICULTY = "com.cannonmatthews.sudoku.difficulty";
	public static final int DIFFICULTY_EASY = 0;
	public static final int DIFFICULTY_MEDIUM = 1;
	public static final int DIFFICULTY_HARD = 2;
	private int puzzle[] = new int[9*9];
	private PuzzleView puzzleView;
	private final int used[][][] = new int[9][9][];
	private final String easyPuzzle = 
		"360000000004230800000004200"+
		"070460003820000014500013020"+
		"001900000007048300000000045";
	private final String mediumPuzzle = 
		"360000000004230800000004200"+
		"070460003820000014500013020"+
		"001900000007048300000000045";
	private final String hardPuzzle = 
		"360000000004230800000004200"+
		"070460003820000014500013020"+
		"001900000007048300000000045";
	public String getTileString(int x, int y) {
		int v = getTile(x,y);
		if (v ==0)
			return "";
		else
			return String.valueOf(v);
	}

	public boolean setTileIfValid(int x, int y, int value) {
		int tiles[] = getUsedTiles(x,y);
		if (value !=0){
			for ( int tile : tiles){
				if (tile == value)
					return false;
			}
		}
		setTile(x,y,value);
		calculateUsedTiles();
		return true;
	}

	static protected int[] fromPuzzleString(String string){
		int[] puz = new int[string.length()];
		for (int i = 0; i < puz.length; i++) {
			puz[i] = string.charAt(i) - '0';
		}
		return puz;
	}

	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();
		
		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();

	}
	@Override
	protected void onResume(){
		super.onResume();
		Music.play(this, R.raw.game);
	}
	@Override
	protected void onPause() {
		super.onPause();
		Music.stop(this);
	}
	
	protected void showKeypadOrError(int x, int y) {
		Log.d(TAG, "showKeypadOrError(" + x + "," + y+")");
		int tiles[] = getUsedTiles(x,y);
		Log.d(TAG, "got used tiles : "+ tiles);
		if (tiles.length == 9){
			Log.d(TAG, "showError: were 9 used tiles showing Toast");
			Toast toast = Toast.makeText(this,
				R.string.no_moves_label, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER,0,0);
			toast.show();
		} else {
			Log.d(TAG, "showKeypad: used = " + toPuzzleString(tiles));
			Dialog v = new Keypad(this, tiles, puzzleView);
			v.show();
			Log.d(TAG, "shown Keypad");
		}
	}
	protected int[] getUsedTiles(int x, int y){
		Log.d(TAG, "getUsedTiles(" + x + "," + y +")");
		Log.d(TAG, "returning :" + used[x][y]);
		return used[x][y];
	}

	static private String toPuzzleString(int[] puz){
		StringBuilder buf = new StringBuilder();
		for (int element : puz) {
			buf.append(element);
		}
		return buf.toString();
	}

	private void calculateUsedTiles() {
		for(int x=0; x<9; x++){
			for (int y=0; y<9; y++){
				used[x][y] = calculateUsedTiles(x,y);
				
			}
		}
	}

	private int[] calculateUsedTiles(int x, int y) {
		int c[] = new int[9];
		//horiz
		for(int i =0; i<9; i++){
			if (i ==y)
				continue;
			int t = getTile(x,i);
			if (t!= 0)
				c[t-1]= t;
		}	
		///vert
		for(int i =0; i<9; i++){
			if (i ==x)
				continue;
			int t = getTile(i,y);
			if (t!= 0)
				c[t-1]= t;
		}
		//block
		int startx = (x/3)*3;
		int starty = (y/3)*3;
		for (int i = startx; i < startx+3; i++){
			for (int j = starty; j < starty+3; j++){
				if (i == x && j==y)
					continue;
				int t =getTile(i,j);
				if (t!=0)
					c[t-1]= t;
			}
		}
		//remove zeros
		int nused = 0;
		for (int t :c){
			if (t!=0)
				nused++;
		}
		int c1[] = new int[nused];
		nused = 0;
		for (int t :c){
			if (t!=0)
				c1[nused++]= t;
		}
		return c1;		
	}
	private int[] getPuzzle(int diff){
		String puz;
		//TODO: continue last game;
		switch(diff){
		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICULTY_EASY:
		default:
			puz = easyPuzzle;
			break;
		}
		return fromPuzzleString(puz);
	}
	/** Return the tile at the given coordinates */
   private int getTile(int x, int y) {
      return puzzle[y * 9 + x];
   }
   /** Change the tile at the given coordinates */
   private void setTile(int x, int y, int value) {
      puzzle[y * 9 + x] = value;
   }


	
}
