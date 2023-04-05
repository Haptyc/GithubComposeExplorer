package com.poborca.githubapp.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.poborca.githubapp.R
import com.poborca.githubapp.models.GithubUser
import com.poborca.githubapp.models.utility.MetaViewState
import com.poborca.githubapp.screens.detail.UserDetailActivity
import com.poborca.githubapp.ui.theme.GithubAppTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    GithubUserPage()
                }
            }
        }
    }
}

@Composable
fun GithubUserPage(homeVm: HomeViewModel = viewModel()) {
    val uiState by homeVm.vs.collectAsState()
    when (uiState.metaState) {
        MetaViewState.LOADING -> {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        MetaViewState.UPDATING,
        MetaViewState.SUCCESS -> {
            GithubUserList(list = uiState.listOfUsers, partialLoad = uiState.metaState == MetaViewState.UPDATING)
        }
        else -> {
            Text(text = "Failed to load")
        }
    }
}

@Composable
fun GithubUserList(list: List<GithubUser>, partialLoad: Boolean = false) {
    val listState = rememberLazyListState()
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        Box(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(12.dp)) {
            Image(bitmap = ImageBitmap.imageResource(id = R.drawable.github_logo_vector), contentDescription = "github",
                modifier = Modifier.align(Alignment.Center).height(100.dp).width(250.dp))
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1F)
        ) {
            items(count = list.size) {
                GithubUserItem(user = list[it])
            }
        }
        if (partialLoad) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
    if (listState.firstVisibleItemIndex > ((list.size / 10) * 6)) {
        val homeVm: HomeViewModel = viewModel()
        homeVm.fetchMoreUsers()
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterialApi::class)
@Composable
fun GithubUserItem(user: GithubUser) {
    val ctx = LocalContext.current
    Box(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()) {
        Card(onClick = {
           ctx.startActivity(Intent(ctx, UserDetailActivity::class.java).apply {
               putExtra(UserDetailActivity.USER_INFO, user)
           })
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), elevation = 10.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                GlideImage(
                    model = user.avatar_url, contentDescription = user.login, modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                )
                Column(modifier = Modifier
                    .wrapContentHeight()
                    .weight(1F)
                    .padding(end = 8.dp)) {
                    Text(text = user.login, modifier = Modifier.align(Alignment.End))
                    Text(text = "ID: ${user.id}", modifier = Modifier.align(Alignment.End))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GithubAppTheme {
        GithubUserPage()
    }
}