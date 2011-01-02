package com.cannonmatthews.sudoku2;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.FontMetrics;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;

public class PuzzleView extends View {
	public static final String TAG = "Sudoku";
	
	private final Game game;
	private final Rect selRect = new Rect();
	
	private float width;
	private float height;
	private int selX;
	private int selY;
	
	public PuzzleView(Context context) {
		super(context);
		Log.d(TAG, "PuzzleView" + "(" + context + ")");
		this.game = (Game) context;
		setFocusable(true);
		setFocusableInTouchMode(true);
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		Log.d(TAG, "onKeyDown: keycode=" + keyCode + "event=" + event);
		switch (keyCode){
		case KeyEvent.KEYCODE_DPAD_UP:
			Log.d(TAG, "dpad up, moving to" + selX+ ","  + (selY-1));
			select(selX,selY-1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			Log.d(TAG, "dpad dn, moving to" + selX + ","  + (selY+1));
			select(selX,selY+1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			Log.d(TAG, "dpad left, moving to" + (selX-1)+ ","  + (selY));
			select(selX-1,selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			Log.d(TAG, "dpad right, moving to" + (selX+1)+ "," + (selY));
			select(selX+1,selY);
			break;
		case KeyEvent.KEYCODE_0: 
		case KeyEvent.KEYCODE_SPACE: setSelectedTile(0);break;
		case KeyEvent.KEYCODE_1:	 setSelectedTile(1);break;
		case KeyEvent.KEYCODE_2:	 setSelectedTile(2);break;
		case KeyEvent.KEYCODE_3:	 setSelectedTile(3);break;
		case KeyEvent.KEYCODE_4:	 setSelectedTile(4);break;
		case KeyEvent.KEYCODE_5:	 setSelectedTile(5);break;
		case KeyEvent.KEYCODE_6:	 setSelectedTile(6);break;
		case KeyEvent.KEYCODE_7:	 setSelectedTile(7);break;
		case KeyEvent.KEYCODE_8:	 setSelectedTile(8);break;
		case KeyEvent.KEYCODE_9:	 setSelectedTile(9);break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			game.showKeypadOrError(selX,selY);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		Log.d(TAG, "onTouchEvent" + "(" + event + ")");
		if (event.getAction() != MotionEvent.ACTION_DOWN){
			Log.d(TAG, "motion was not action down");
			return super.onTouchEvent(event);
		}
		select((int)(event.getX() /width),
			   (int)(event.getY() /height));
		game.showKeypadOrError(selX, selY);
		Log.d(TAG, "onTouch: x " + selX + ", y " + selY);
		return true;
	}
	public void setSelectedTile(int tile){
		Log.d(TAG, "setSelectedTile" + "(" + tile +")" );
		if (game.setTileIfValid(selX,selY, tile)) {
			invalidate();//may change hints
		} else {
			//Number is not valid for this tile
			Log.d(TAG, "setSelectedTile: ivalid:" +tile );
			startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		Log.d(TAG, "onSizeChanged" + "(" + w +","+ h+","+ oldw+","+ oldh +")" );
		width = w /9f;
		height = h /9f;
		getRect(selX,selY,selRect);
		Log.d(TAG, "onSizedChanged: width " + width + ", height "+ height);
		super.onSizeChanged(w, h, oldw, oldh);
	
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw" + "(" + canvas +")" );
		// draw back ground
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.puzzle_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		//draw the board
		// define colors for grid lines
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.puzzle_dark));
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.puzzle_light));
		
		// draw the minor lines
		
		for(int i = 0 ; i < 9 ; i++){
			canvas.drawLine(0, i*height, getWidth(), i* height, light);
			canvas.drawLine(0, i*height +1, getWidth(), i*height+ 1, hilite);
			canvas.drawLine(i*width, 0, i*width, getHeight(), light);
			canvas.drawLine(i*width+1, 0, i*width+1, getHeight(), hilite);
		}
		//major lines
		for(int i = 0 ; i < 9 ; i++){
			if ( i % 3 != 0 )
				continue;
			canvas.drawLine(0, i*height, getWidth(), i* height, dark);
			canvas.drawLine(0, i*height +1, getWidth(), i*height+ 1, hilite);
			canvas.drawLine(i*width, 0, i*width, getHeight(), dark);
			canvas.drawLine(i*width+1, 0, i*width+1, getHeight(), hilite);
	
			
		}
		//draw the numbers
		//define colors and styles
		Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
		foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
		foreground.setStyle(Style.FILL);
		foreground.setTextSize(height*0.75f);
		foreground.setTextScaleX(width/height);
		foreground.setTextAlign(Paint.Align.CENTER);
		//draw number in center of tile
		FontMetrics fm = foreground.getFontMetrics();
		//centering in X
		float x = width/2;
		//centering in y mesaure asecent/ decent first
		float y = height/2 - ( fm.ascent+ fm.descent) /2;
		for ( int i = 0; i < 9; i++){
			for (int j=0;j<9;j++){
				canvas.drawText(this.game.getTileString(i,j), i*width+x, j* height +y, foreground);
				
			}
		}
		// draw the hints
		//pick a color based on #moves left
		Paint hint = new Paint();
		int c[] = { getResources().getColor(R.color.puzzle_hint_0),
					getResources().getColor(R.color.puzzle_hint_1) ,
					getResources().getColor(R.color.puzzle_hint_2)
		};
		Rect r = new Rect();
		for (int i=0; i<9; i++){
			for (int j=0; j<9; j++){
				int movesleft = 9 - game.getUsedTiles(i,j).length;
				if ( movesleft <c.length){
					getRect(i,j,r);
					hint.setColor(c[movesleft]);
					canvas.drawRect(r, hint);
				}
					
			}
		}
		// draw the selection
		Log.d(TAG, "selRect=" + selRect);
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.puzzle_selected));
		canvas.drawRect(selRect, selected);
		
		
	}
	
	private void getRect(int x, int y , Rect rect) {
		Log.d(TAG, "getRect" + "(" + x +","+ y+","+ rect + ")");
		rect.set(
					(int)(x *width),
					(int)(y*height), 
					(int)(x*width+width), 
					(int)(y*height+height)
				);
	}
	private void select(int x, int y){
		Log.d(TAG, "select" + "(" + x+","+ y + ")");
		Log.v(TAG, "invalidating Rect(" +selRect+")");
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0),8);
		selY = Math.min(Math.max(y, 0),8);
		Log.v(TAG, "setting SelX = " + selX + "selY= " + selY);
		getRect(selX, selY, selRect);
		Log.v(TAG, "invalidating Rect(" +selRect+")");
		invalidate(selRect);
		
	}
	
}
