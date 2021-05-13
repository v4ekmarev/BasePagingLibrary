package com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.justcashback.ims_justcash.ui.base.viewbindings.withrefceltion.viewBinding
import com.paging.basepage.paging.LoadingAdapter
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.databinding.MainFragmentBinding
import com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.adapter.CharactersListAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        mainFragmentBinding.charactersList.apply {
            adapter = viewAdapter.withLoadStateFooter(footer = LoadingAdapter {
                viewAdapter.retry()
            })
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewAdapter.addLoadStateListener { state ->
            mainFragmentBinding.loading.visibility =
                if (state.source.refresh is LoadState.Loading) View.VISIBLE else View.INVISIBLE
        }


        lifecycleScope.launch {
            viewModel.pagerFlow.collect {
                viewAdapter.submitData(viewLifecycleOwner.lifecycle, it)
//                viewAdapter.submitData(it)
            }
        }
    }
}