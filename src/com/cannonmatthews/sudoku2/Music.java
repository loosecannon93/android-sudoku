package com.cannonmatthews.sudoku2;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {
	private static MediaPlayer mp;
	public static void play(Context context, int resid){
		stop(context);
		if (Prefs.getMusic(context)){
			mp = MediaPlayer.create(context, resid);
			mp.setLooping(true);
			mp.start();
		}
	}
	public static void stop(Context context) {
		if (mp !=null){
			mp.stop();
			mp.release();
			mp = null;
		}
	}
}
