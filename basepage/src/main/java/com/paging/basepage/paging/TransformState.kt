package com.paging.basepage.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.paging.basepage.paging.states.ListViewState
import com.paging.basepage.paging.states.NetworkState

internal class TransformState {

    companion object {
        fun transformState(data: LiveData<NetworkState>): LiveData<ListViewState> {
            return Transformations.map(data) {
                when (it) {
                    is NetworkState.Success ->
                        if (it.isAdditional && it.isEmptyResponse) {
                            ListViewState.NoMoreElements
                        } else if (it.isEmptyResponse) {
                            ListViewState.Empty
                        } else {
                            ListViewState.Loaded
                        }
                    is NetworkState.Loading ->
                        if (it.isAdditional) {
                            ListViewState.AddLoading
                        } else {
                            ListViewState.Loading
                        }
                    is NetworkState.Error ->
                        if (it.isAdditional) {
                            ListViewState.AddError
                        } else {
                            ListViewState.Error
                        }
                }
            }
        }

    }
}