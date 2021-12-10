package com.communisolve.musicplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.communisolve.musicplayer.fragments.AlbumFragment
import com.communisolve.musicplayer.fragments.SongsFragment
import com.communisolve.musicplayer.model.MusicFiles
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var musicFiles: ArrayList<MusicFiles>
    }
    private val REQUEST_CODE: Int = 111
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions()
    }

    private fun permissions() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE
            )
        } else {
            musicFiles = getAllAudios(this)
            initViewPager()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                musicFiles = getAllAudios(this)
                initViewPager()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE
                )
            }
        }
    }

    fun getAllAudios(context: Context): ArrayList<MusicFiles> {
        val tempAudiosList: ArrayList<MusicFiles> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA, // for path
            MediaStore.Audio.Media.ARTIST
        )

        val cursor: Cursor = context.contentResolver.query(
            uri, projection, null, null, null
        )!!

        while (cursor.moveToNext()) {
            val album = cursor.getString(0)
            val title = cursor.getString(1)
            val duration = cursor.getString(2)
            val path = cursor.getString(3)
            val artist = cursor.getString(4)

            val musicFiles = MusicFiles(
                path, title, artist, album, duration
            )
            tempAudiosList.add(musicFiles)
        }
        cursor.close()
        return tempAudiosList
    }


    private fun initViewPager() {
        val mViewPager: ViewPager = findViewById(R.id.viewPager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val fragments = arrayListOf<Fragment>(SongsFragment(), AlbumFragment())
        val titles = arrayListOf<String>("Songs", "Albums")
        val viewPager: ViewPagerAdapter =
            ViewPagerAdapter(supportFragmentManager, fragments, titles)
        mViewPager.adapter = viewPager
        tabLayout.setupWithViewPager(mViewPager)
    }


}

class ViewPagerAdapter(
    val fm: FragmentManager,
    var fragments: ArrayList<Fragment>,
    var titles: ArrayList<String>
) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        return titles.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }
}