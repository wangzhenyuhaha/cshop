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

    fun playVoiceOfNewOrder(context: Context?) {
        playVoice(context, R.raw.voice_new_order);
    }

    fun playVoiceOfOrderCancel(context: Context?) {
        playVoice(context, R.raw.voice_order_cancel);
    }

    fun playVoice(context: Context?, resId: Int) {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_ALARM).build()).setMaxStreams(1).build();
            soundID = soundPool!!.load(context, resId, 1)
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