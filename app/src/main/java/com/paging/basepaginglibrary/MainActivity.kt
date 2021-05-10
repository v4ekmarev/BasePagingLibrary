package com.paging.basepaginglibrary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.PaginationWithoutDelegatesActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.paginationWithoutDelegates).setOnClickListener {
            startActivity(Intent(this@MainActivity, PaginationWithoutDelegatesActivity::class.java))
        }

    }
}