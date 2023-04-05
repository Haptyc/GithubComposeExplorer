package com.poborca.githubapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DetailedGithubUser(
    @SerializedName(value = "login")
    val login: String, //"login": "octocat",
    @SerializedName(value = "id")
    val id: Int, //"id": 1,
    @SerializedName(value = "node_id")
    val node_id: String, //"node_id": "MDQ6VXNlcjE=",
    @SerializedName(value = "avatar_url")
    val avatar_url: String, //"avatar_url": "https://github.com/images/error/octocat_happy.gif",
    @SerializedName(value = "gravatar_id")
    val gravatar_id: String, //"gravatar_id": "",
    @SerializedName(value = "url")
    val url: String, //"url": "https://api.github.com/users/octocat",
    @SerializedName(value = "html_url")
    val html_url: String, //"html_url": "https://github.com/octocat",
    @SerializedName(value = "followers_url")
    val followers_url: String, //"followers_url": "https://api.github.com/users/octocat/followers",
    @SerializedName(value = "following_url")
    val following_url: String, //"following_url": "https://api.github.com/users/octocat/following{/other_user}",
    @SerializedName(value = "gists_url")
    val gists_url: String, //"gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
    @SerializedName(value = "starred_url")
    val starred_url: String, //"starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
    @SerializedName(value = "subscriptions_url")
    val subscriptions_url: String, //"subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
    @SerializedName(value = "organizations_url")
    val organizations_url: String, //"organizations_url": "https://api.github.com/users/octocat/orgs",
    @SerializedName(value = "repos_url")
    val repos_url: String, //"repos_url": "https://api.github.com/users/octocat/repos",
    @SerializedName(value = "events_url")
    val events_url: String, //"events_url": "https://api.github.com/users/octocat/events{/privacy}",
    @SerializedName(value = "received_events_url")
    val received_events_url: String, //"received_events_url": "https://api.github.com/users/octocat/received_events",
    @SerializedName(value = "type")
    val type: String, //"type": "User",
    @SerializedName(value = "site_admin")
    val site_admin: Boolean, //"site_admin": false,
    @SerializedName("name")
    val name: String,  // "monalisa octocat",
    @SerializedName("company")
    val company: String,  // "GitHub",
    @SerializedName("blog")
    val blog: String,  // "https://github.com/blog",
    @SerializedName("location")
    val location: String,  // "San Francisco",
    @SerializedName("email")
    val email: String,  // "octocat@github.com",
    @SerializedName("hireable")
    val hireable: Boolean,  // false,
    @SerializedName("bio")
    val bio: String?,  // "There once was...",
    @SerializedName("twitter_username")
    val twitterUsername: String,  // "monatheoctocat",
    @SerializedName("public_repos")
    val publicRepos: Int,  // 2,
    @SerializedName("public_gists")
    val publicGists: Int,  // 1,
    @SerializedName("followers")
    val followers: Int,  // 20,
    @SerializedName("following")
    val following: Int,  // 0,
    @SerializedName("created_at")
    val createdAt: Date,  // "2008-01-14T04:33:35Z",
    @SerializedName("updated_at")
    val updatedAt: Date,  // "2008-01-14T04:33:35Z"
): Parcelable