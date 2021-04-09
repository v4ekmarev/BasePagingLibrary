package com.paging.basepaginglibrary.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.justcashback.ims_justcash.ui.base.viewbindings.withrefceltion.viewBinding
import com.paging.basepage.paging.states.ListAdapterState
import com.paging.basepage.paging.states.ListViewState
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.databinding.MainFragmentBinding
import com.paging.basepaginglibrary.ui.main.adapter.CharactersListAdapter

class CharactersListFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = CharactersListFragment()
    }

    private lateinit var viewModel: CharactersListViewModel

    var viewAdapter: CharactersListAdapter = CharactersListAdapter()

    private val mainFragmentBinding: MainFragmentBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CharactersListViewModel::class.java)

        viewAdapter.setClickRetryAdd {
            viewModel.retryAddCharactersList()
        }
        mainFragmentBinding.charactersList.apply {
            adapter = viewAdapter
            (layoutManager as? GridLayoutManager)?.spanSizeLookup = viewAdapter.getSpanSizeLookup()
        }

        viewModel.state().observe(viewLifecycleOwner, { viewState ->
            Log.d("TASD", viewState.toString())
            Log.d("TASD", viewAdapter.itemCount.toString())

            when (viewState) {
                is ListViewState.Loaded -> {
                    mainFragmentBinding.loading.visibility = View.INVISIBLE
                    viewAdapter.submitState(ListAdapterState.Added)
                }
                is ListViewState.AddLoading -> {
                    viewAdapter.submitState(ListAdapterState.AddLoading)
                }
                is ListViewState.AddError -> {
                    viewAdapter.submitState(ListAdapterState.AddError)
                }
                is ListViewState.NoMoreElements -> {
                    viewAdapter.submitState(ListAdapterState.NoMore)
                }
                ListViewState.Empty -> TODO()
                ListViewState.Error -> TODO()
                ListViewState.Loading -> {
                    mainFragmentBinding.loading.visibility = View.VISIBLE
                }
                ListViewState.Refreshing -> TODO()
            }
        })

        viewModel.getData().observe(viewLifecycleOwner, {
            viewAdapter.submitList(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}