package com.poborca.githubapp.screens.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.poborca.githubapp.R
import com.poborca.githubapp.models.*
import com.poborca.githubapp.ui.theme.GithubAppTheme
import java.text.SimpleDateFormat

class UserDetailActivity : ComponentActivity() {

    val dateFormatter = SimpleDateFormat("MM/DD/YY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = intent.extras!!.getParcelable<GithubUser>(USER_INFO)
        setContent {
            val vm: UserDetailViewModel = viewModel()
            vm.setUser(user)
            GithubAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    GithubDetailUserPage(vm)
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun GithubDetailUserPage(vm: UserDetailViewModel) {

        val state = vm.vs.collectAsState()
        val detailedUser = state.value.detailedUser
        if (detailedUser == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            return
        }
        val bio = if (detailedUser.bio.isNullOrBlank()) getString(R.string.no_bio) else detailedUser.bio
        Column(modifier = Modifier.padding(12.dp)) {
            CreateDetailedHeader(detailedUser)
            Box(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()

            ) {
                Text(text = bio, fontSize = 12.sp, modifier = Modifier.padding(8.dp))
            }
            Box(modifier = Modifier.height(8.dp))
            if (state.value.fetchingRepositories) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                val repos = state.value.repositories
                if (repos.isEmpty()) {
                    Text(text = getString(R.string.no_repositories))
                } else {
                    LazyColumn() {
                        items(count = repos.size) {
                            repoCard(repos[it])
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
    @Composable
    private fun repoCard(githubRepository: GithubRepository) {
        val vm = viewModel<UserDetailViewModel>()
        val state = vm.vs.collectAsState()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = {
                vm.toggleCommitExpansion(githubRepository)
            }
        ) {
            Column() {
                Row(Modifier.padding(8.dp)) {
                    Column {
                        Text(text = githubRepository.name, fontSize = 14.sp)
                        Text(text = githubRepository.full_name, fontSize = 10.sp)
                    }
                    Box(modifier = Modifier.weight(1F)) {}
                    Column {
                        Text(text = githubRepository.owner?.login ?: "n/a", fontSize = 12.sp)
                        Text(text = dateFormatter.format(githubRepository.created_at), fontSize = 8.sp)
                    }
                }
                if (state.value.asyncCommitFetch == githubRepository.asyncHash) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)) {
                        CircularProgressIndicator(modifier = Modifier.align(Center))
                    }
                } else if (githubRepository.commits?.isNotEmpty() == true && githubRepository.expanded) {
                    githubRepository.commits.firstThreeOrLess()
                        .forEach {
                            CommitEntry(it)
                        }
                }
            }
        }
    }

    @OptIn(ExperimentalGlideComposeApi::class)
    @Composable
    fun CommitEntry(cP: GithubRepoCommitParent) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(color = Color(getColor(R.color.teal_200))))
            Row(Modifier.fillMaxWidth()) {
                Column() {
                    Box(modifier = Modifier.padding(8.dp)) {
                        Row() {
                            GlideImage(
                                model = cP.getSomeAuthorImageUrl(), contentDescription = cP.getSomeAuthorName(),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                            Text(text = cP.getSomeAuthorName(), modifier = Modifier.padding(start = 6.dp))
                        }
                    }
                }
                Box(modifier = Modifier.weight(1F))
                Text(text = cP.commit.message, fontSize = 12.sp, modifier = Modifier.padding(8.dp))
            }
        }
    }

    @Composable
    @OptIn(ExperimentalGlideComposeApi::class)
    private fun CreateDetailedHeader(detailedUser: DetailedGithubUser) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                GlideImage(
                    model = detailedUser.avatar_url, contentDescription = detailedUser.login,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)

                )
                Column(Modifier.padding(10.dp)) {
                    Text(text = detailedUser.name, fontSize = 14.sp)
                    Text(text = detailedUser.login, fontSize = 10.sp)
                    Text(text = dateFormatter.format(detailedUser.createdAt), fontSize = 7.sp)
                }
                Box(modifier = Modifier.weight(1F))
                Column(modifier = Modifier.align(alignment = Bottom)) {
                    Row(Modifier.padding(8.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_people_alt_24),
                            contentDescription = getString(R.string.follower_counter),
                            Modifier.padding(8.dp)
                        )
                        Text(
                            text = detailedUser.followers.toString(), fontSize = 10.sp,
                            modifier = Modifier.align(CenterVertically)
                        )
                    }
                    Row(Modifier.padding(8.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_folder_copy_24),
                            contentDescription = getString(R.string.repo_count),
                            Modifier.padding(8.dp)
                        )
                        Text(
                            text = detailedUser.followers.toString(), fontSize = 10.sp,
                            modifier = Modifier.align(CenterVertically)
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val USER_INFO = "USER_INFO"
    }
}
