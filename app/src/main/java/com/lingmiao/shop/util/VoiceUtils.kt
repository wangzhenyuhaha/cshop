package com.lingmiao.shop.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import com.lingmiao.shop.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object VoiceUtils {
    private var soundPool: SoundPool? = null
    private var soundID = 0

    fun playVoice(context: Context?) {
        if (soundPool == null) {
           // soundPool = SoundPool.Builder().setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build()).setMaxStreams(1).build();
            soundPool = SoundPool(1, AudioManager.STREAM_ALARM, 0)
            soundID = soundPool!!.load(context, R.raw.message_new_order, 1)
        }
        MainScope().launch {
            delay(200)//load音频需要时间,没有load完则会没有声音
            soundPool?.play(soundID, 1f, 1f, 0, 0, 1f)
        }
    }

    fun release() {
        if (soundPool != null) {
            soundPool!!.release()
            soundPool = null
        }
    }
}