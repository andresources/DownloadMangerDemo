package com.downloadmanger

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData


class MainActivity : AppCompatActivity() {
    lateinit var tv:TextView
    companion object{
        val liveData: MutableLiveData<String> = MutableLiveData()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv = findViewById(R.id.tv)

        liveData.observe(this,{
            //tv.setText("status : $it")
        })
    }

    @SuppressLint("Range")
    fun funStartDownload(view: View) {
        val downloader = AndroidDownloader(this)
        val fileUrl= "https://images.pexels.com/photos/1624496/pexels-photo-1624496.jpeg"
        val downloadId = downloader.downloadFile(fileUrl)
        Thread {
            var downloading = true
            while (downloading) {
                val q = DownloadManager.Query()
                q.setFilterById(downloadId)

                val cursor: Cursor = downloader.getDW().query(q)
                cursor.moveToFirst()
                val bytes_downloaded: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val bytes_total: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) === DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }

                val dl_progress = (bytes_downloaded * 100L / bytes_total).toInt()
                runOnUiThread { tv.setText("$dl_progress % ") }
                cursor.close()
            }
        }.start()
    }
}