package com.poborca.githubapp.models

import com.google.gson.annotations.SerializedName

data class GithubRepoCommitParent(
    @SerializedName("author")
    val author: GithubUser?,
    @SerializedName("committer")
    val committer: GithubUser?,
    @SerializedName("commit")
    val commit: GitCommit,
) {
    fun getSomeAuthorName(): String {
        var str = (committer?.login)
        str = str ?: (commit.committer?.login)
        str = str ?: (author?.login)
        str = str ?: (commit.author?.login)
        str = str ?: (commit.committer?.email)
        if (str.isNullOrBlank())
            return "N/A"
        return str
    }
    fun getSomeAuthorImageUrl(): String {
        var str = (committer?.avatar_url)
        str = str ?: (commit.committer?.avatar_url)
        str = str ?: (author?.avatar_url)
        str = str ?: (commit.author?.avatar_url)
        if (str.isNullOrBlank())
            return "N/A"
        return str
    }
}

fun List<GithubRepoCommitParent>.firstThreeOrLess(): List<GithubRepoCommitParent> {
    return subList(0, 3.coerceAtMost(size))
}

data class GitCommit(
    @SerializedName("author")
    val author: GithubUser?,
    @SerializedName("committer")
    val committer: GithubUser?,
    @SerializedName("message")
    val message: String
)