package com.poborca.githubapp.api

import com.poborca.githubapp.models.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("/users")
    suspend fun fetchUsers(@Query("since") lastUserId: Int? = null, @Query("per_page") perPage: Int = 50): Response<List<GithubUser>>

    @GET("/users/{user_name}")
    suspend fun getUserDetails(@Path("user_name") userName: String): Response<DetailedGithubUser>

    @GET("/users/{user_name}/repos")
    suspend fun getUserRepositories(@Path("user_name") userName: String): Response<List<GithubRepository>>

    @GET("/repos/{user_name}/{repo}/commits")
    suspend fun getUserCommitsToRepo(@Path("user_name") userName: String, @Path("repo") repo: String): Response<List<GithubRepoCommitParent>>

    companion object {
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                var request = it.request()
                request.newBuilder()
                    .addHeader(
                        "Authorization", "Bearer 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"
                    )
                it.proceed(request)
            }
            .build()
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private val _api = lazy {
             retrofit.create(GithubApi::class.java)
         }
        val api: GithubApi
            get() = _api.value
    }
}