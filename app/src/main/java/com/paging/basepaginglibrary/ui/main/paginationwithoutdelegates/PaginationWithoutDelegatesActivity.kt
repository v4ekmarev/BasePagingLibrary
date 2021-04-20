package com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paging.basepaginglibrary.R

class PaginationWithoutDelegatesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination_without_delegates)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, CharactersListFragment.newInstance())
            .commit()
    }
}