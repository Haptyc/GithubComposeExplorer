package com.poborca.githubapp.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class GithubRepository(
    @SerializedName("id")
    val id: Int, // 1296269,
    @SerializedName("node_id")
    val node_id: String, // "MDEwOlJlcG9zaXRvcnkxMjk2MjY5",
    @SerializedName("name")
    val name: String, // "Hello-World",
    @SerializedName("full_name")
    val full_name: String, // "octocat/Hello-World",
    @SerializedName("private")
    val private: Boolean, // false,
    @SerializedName("html_url")
    val html_url: String, // "https://github.com/octocat/Hello-World",
    @SerializedName("description")
    val description: String? = null, // "This your first repo!",
    @SerializedName("fork")
    val fork: Boolean, // false,
    @SerializedName("visibility")
    val visibility: String, // "public",
    @SerializedName("pushed_at")
    val pushed_at: Date, // "2011-01-26T19:06:43Z",
    @SerializedName("created_at")
    val created_at: Date, // "2011-01-26T19:01:12Z",
    @SerializedName("updated_at")
    val updated_at: Date, // "2011-01-26T19:14:43Z",
    @SerializedName("owner")
    val owner: GithubUser? = null,
    @SerializedName("commits")
    val commits: List<GithubRepoCommitParent>? = null,
    val expanded: Boolean = false
) {
    val asyncHash
        get() = name + owner?.login
}