package com.communisolve.musicplayer

import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.communisolve.musicplayer.databinding.ActivityPlayerBinding
import com.communisolve.musicplayer.model.MusicFiles

class PlayerActivity : AppCompatActivity() {
    companion object {
        var listOfSongs: ArrayList<MusicFiles> = ArrayList()
        lateinit var uri: Uri
        var mediaPlayer: MediaPlayer? = null
    }

    lateinit var binding: ActivityPlayerBinding
    var position = -1
    var handler: Handler = Handler()
    lateinit var playPauseThread: Thread
    lateinit var prevThread: Thread
    lateinit var nextThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        getIntentMethod()
        binding.songName.text = listOfSongs[position].title
        binding.songArtist.text = listOfSongs[position].artist

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer?.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        this.runOnUiThread(object : Runnable {
            override fun run() {
                if (mediaPlayer != null) {
                    var mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                    binding.seekBar.progress = mCurrentPosition
                    binding.txtDurationPlayed.text = formattedTime(mCurrentPosition)
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun formattedTime(mCurrentPosition: Int): String {
        var totalOut = ""
        var totalNew = ""
        var seconds: String = (mCurrentPosition % 60).toString()
        var minutes: String = (mCurrentPosition / 60).toString()
        totalOut = minutes + ":" + seconds
        totalNew = minutes + ":" + "0" + seconds
        if (seconds.length == 1) {
            return totalNew
        } else {
            return totalOut
        }
    }

    private fun getIntentMethod() {
        position = intent.getIntExtra("position", -1)
        listOfSongs = MainActivity.musicFiles
        if (listOfSongs.size > 0) {
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_24)
            uri = Uri.parse(listOfSongs[position].path)
        }
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer?.start()

        } else {
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            mediaPlayer?.start()
        }

        binding.seekBar.max = mediaPlayer?.duration!! / 1000
        metaData(uri)
    }

    private fun metaData(uri: Uri) {
        val retriever: MediaMetadataRetriever = MediaMetadataRetriever()
        retriever.setDataSource(uri.toString())
        val durationTotal = Integer.parseInt(listOfSongs[position].duration) / 1000
        binding.txtDurationTotal.text = formattedTime(durationTotal)
        var art: ByteArray? = retriever.embeddedPicture
        art?.let {
            Glide.with(this).asBitmap().load(it).into(binding.coverArt)
        }
    }

    override fun onResume() {
        playPauseThreadBtn()
        nextThreadBtn()
        prevThreadBtn()
        super.onResume()
    }

    private fun prevThreadBtn() {

        prevThread = object : Thread() {
            override fun run() {
                super.run()
                binding.btnSkipPrevious.setOnClickListener {
                    prevBtnClicked()
                }
            }
        }
        prevThread.start()
    }

    private fun prevBtnClicked() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = if (position - 1 < 0) {
                listOfSongs.size - 1
            } else {
                position - 1
            }

            uri = Uri.parse(listOfSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            binding.songName.text = listOfSongs[position].title
            binding.songArtist.text = listOfSongs[position].artist
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        var mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                        binding.seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer?.start()
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = if (position - 1 < 0) {
                listOfSongs.size - 1
            } else {
                position - 1
            }
            uri = Uri.parse(listOfSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            binding.songName.text = listOfSongs[position].title
            binding.songArtist.text = listOfSongs[position].artist
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        var mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                        binding.seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun nextThreadBtn() {
        nextThread = object : Thread() {
            override fun run() {
                super.run()
                binding.btnSkipNext.setOnClickListener {
                    nextBtnClicked()
                }
            }
        }
        nextThread.start()
    }

    private fun nextBtnClicked() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = (position + 1) % listOfSongs.size
            uri = Uri.parse(listOfSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            binding.songName.text = listOfSongs[position].title
            binding.songArtist.text = listOfSongs[position].artist
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        var mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                        binding.seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer?.start()
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            position = (position + 1) % listOfSongs.size
            uri = Uri.parse(listOfSongs[position].path)
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
            metaData(uri)
            binding.songName.text = listOfSongs[position].title
            binding.songArtist.text = listOfSongs[position].artist
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        var mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                        binding.seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun playPauseThreadBtn() {
        playPauseThread = object : Thread() {
            override fun run() {
                super.run()
                binding.pausePlay.setOnClickListener {
                    playPauseBtnClicked()
                }
            }
        }
        playPauseThread.start()
    }

    private fun playPauseBtnClicked() {
        if (mediaPlayer?.isPlaying == true) {
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            mediaPlayer?.pause()
            binding.seekBar.max = mediaPlayer?.duration!! / 1000
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        var mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                        binding.seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
        } else {
            binding.pausePlay.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer?.start()
            binding.seekBar.max = mediaPlayer?.duration!! / 1000

            this.runOnUiThread(object : Runnable {
                override fun run() {
                    if (mediaPlayer != null) {
                        val mCurrentPosition = mediaPlayer?.currentPosition!! / 1000
                        binding.seekBar.progress = mCurrentPosition
                    }
                    handler.postDelayed(this, 1000)
                }
            })
        }
    }
}