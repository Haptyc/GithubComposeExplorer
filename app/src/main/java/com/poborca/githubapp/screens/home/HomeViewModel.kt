package com.poborca.githubapp.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.poborca.githubapp.api.GithubApi
import com.poborca.githubapp.models.GithubUser
import com.poborca.githubapp.models.utility.MetaViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel: ViewModel() {

    private val _vs = MutableStateFlow(HomeViewState())
    val vs: StateFlow<HomeViewState>  = _vs.asStateFlow()

    init {
        fetchMoreUsers()
    }

    fun fetchMoreUsers() {
        if (vs.value.metaState == MetaViewState.UPDATING) {
            //do nothing already fetching
            return
        }
        _vs.update {
            it.copy(metaState = if (it.listOfUsers.isNotEmpty()) MetaViewState.UPDATING else MetaViewState.LOADING)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = GithubApi.api.fetchUsers(lastUserId = vs.value.listOfUsers.lastOrNull()?.id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    _vs.update {
                        val updated = HashMap<Int, GithubUser>()
                        updated.putAll(it.mapOfUsers)
                        response.body()!!.forEach { user -> updated[user.id] = user }

                        it.copy(metaState = MetaViewState.SUCCESS,
                            mapOfUsers = updated)
                    }
                } else {
                    _vs.update {
                        it.copy(metaState = MetaViewState.FAILED)
                    }
                }

            }
        }
    }
}

data class HomeViewState(
    internal val mapOfUsers: Map<Int, GithubUser> = mapOf(),
    val metaState: MetaViewState = MetaViewState.LOADING
) {
    val listOfUsers: List<GithubUser>
        get() = mapOfUsers.keys.sorted().map { key -> mapOfUsers[key]!! }
}