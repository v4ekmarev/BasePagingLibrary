package com.paging.basepaginglibrary.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.justcashback.ims_justcash.ui.base.viewbindings.withrefceltion.viewBinding
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.databinding.MainFragmentBinding
import com.paging.basepaginglibrary.ui.base.paging.ListAdapterState
import com.paging.basepaginglibrary.ui.base.paging.ListViewState
import com.paging.basepaginglibrary.ui.main.adapter.CharactersListAdapter

class CharactersListFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = CharactersListFragment()
    }

    private lateinit var viewModel: CharactersListViewModel

    var viewAdapter: CharactersListAdapter = CharactersListAdapter()

    private val mainFragmentBinding: MainFragmentBinding by viewBinding()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CharactersListViewModel::class.java)

        viewAdapter.setClickRetryAdd {
            viewModel.retryAddCharactersList()
        }

        mainFragmentBinding.charactersList.apply {
            adapter = viewAdapter
        }

        viewModel.data.observe(viewLifecycleOwner, {
            viewAdapter.submitList(it)
        })

        viewModel.state.observe(viewLifecycleOwner, { viewState ->
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
                is ListViewState.Loading -> {
                    mainFragmentBinding.loading.visibility = View.VISIBLE
                }
                is ListViewState.Empty -> {
                    mainFragmentBinding.loading.visibility = View.INVISIBLE
                }
            }
        })
    }
}