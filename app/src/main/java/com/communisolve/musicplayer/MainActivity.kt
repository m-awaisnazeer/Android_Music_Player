package com.communisolve.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE: Int = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions()
        initViewPager()
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
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE
                )
            }
        }
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