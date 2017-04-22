package ssmith.android.media;

import java.util.HashMap;

import com.scs.worldcrafter.Statics;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundManager {

	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap; // ResourceID : sound id
	private AudioManager  mAudioManager;
	private Context mContext;
	private float streamVolume;

	public SoundManager(Context theContext) {
		mContext = theContext;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);

		streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//streamVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				if (status == 0) {
					soundPool.play(sampleId, streamVolume, streamVolume, 1, 0, 1f);
					//boolean loaded = true;
				}
			}
		});
	}

	/*public void addSound(int index, int SoundID) {
		mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}*/


	public int playSound(int res_id) {
		if (Statics.cfg.mute == false) {
			int sound_id = -1;
			if (mSoundPoolMap.containsKey(res_id) == false) {
				sound_id = mSoundPool.load(mContext, res_id, 1);
				mSoundPoolMap.put(res_id, sound_id);
			} else {
				sound_id = mSoundPoolMap.get(res_id);
			}
			return mSoundPool.play(sound_id, streamVolume, streamVolume, 1, 0, 1f);
		} else {
			return 0;
		}
	}


	public void stopSound(int sound) {
		mSoundPool.stop(sound);
	}


}
