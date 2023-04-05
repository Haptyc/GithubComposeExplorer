package com.poborca.githubapp.screens.detail

import androidx.lifecycle.ViewModel
import com.poborca.githubapp.api.GithubApi
import com.poborca.githubapp.models.DetailedGithubUser
import com.poborca.githubapp.models.GithubRepoCommitParent
import com.poborca.githubapp.models.GithubRepository
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

class UserDetailViewModel : ViewModel() {
    private val _vs = MutableStateFlow(UserDetailViewState())
    val vs: StateFlow<UserDetailViewState> = _vs.asStateFlow()

    fun setUser(user: GithubUser?) {

        if (vs.value.user == null && user != null) {
            _vs.update { it.copy(user = user) }
            CoroutineScope(Dispatchers.IO).launch {
                val response = GithubApi.api.getUserDetails(userName = user.login)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        _vs.update { c -> c.copy(metaViewState = MetaViewState.SUCCESS, detailedUser = response.body()!!) }
                    } else {
                        _vs.update { c -> c.copy(metaViewState = MetaViewState.FAILED) }
                    }
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                val response = GithubApi.api.getUserRepositories(userName = user.login)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        _vs.update { c -> c.copy(fetchingRepositories = false, repositories = response.body()!!) }
                    } else {
                        _vs.update { c -> c.copy(fetchingRepositories = false) }
                    }
                }
            }

        }
    }

    fun toggleCommitExpansion(repo: GithubRepository) {

        if (repo.commits != null) {
            _vs.update {
                it.copy(
                    repositories = changeRepo(expanded = !repo.expanded, repo = repo, c = vs.value)
                )
            }
            return
        }
        // "dumb" approach as it doesn't support fetching multiple commit histories, but works as a safeguard for this approach
        if (vs.value.asyncCommitFetch != null || repo.owner == null)
            return
        _vs.update {
            it.copy(asyncCommitFetch = repo.asyncHash)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val response = GithubApi.api.getUserCommitsToRepo(userName = repo.owner.login, repo = repo.name)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    _vs.update { c -> c.copy(asyncCommitFetch = null,  repositories = changeRepo(true, response.body()!!, repo, c)) }
                } else {
                    _vs.update { c -> c.copy(asyncCommitFetch = null) }
                }
            }
        }
    }

    private fun changeRepo(expanded: Boolean = false, listOfCommits: List<GithubRepoCommitParent>? = null, repo: GithubRepository, c: UserDetailViewState): List<GithubRepository> {
        return c.repositories.map { rep ->
            if (rep.id == repo.id) {
                var updated = rep
                if (listOfCommits != null) {
                    updated = rep.copy(
                        commits = listOfCommits,
                    )
                }
                updated = updated.copy(
                    expanded = expanded
                )
                updated
            } else {
                rep
            }
        }
    }

}

data class UserDetailViewState(
    val user: GithubUser? = null,
    val detailedUser: DetailedGithubUser? = null,
    val metaViewState: MetaViewState = MetaViewState.LOADING,
    val fetchingRepositories: Boolean = true,
    val repositories: List<GithubRepository> = listOf(),
    val asyncCommitFetch: String? = null
)
