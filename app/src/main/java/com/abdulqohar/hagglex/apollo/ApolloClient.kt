package com.abdulqohar.hagglex.apollo

import android.content.Context
import com.abdulqohar.hagglex.utils.SharedPreferenceManager
import com.abdulqohar.hagglex.utils.TOKENKEY
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

val apolloClient: ApolloClient =
    ApolloClient.builder().serverUrl("https://api-staging.hagglex.com/graphql").build()

private class AuthorizationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${SharedPreferenceManager(context).getToken(TOKENKEY)}")
            .build()

        return chain.proceed(request)
    }
}

private var instance: ApolloClient? = null


fun apolloClient(context: Context): ApolloClient {
    if (instance != null) {
        return instance!!
    }
    instance = ApolloClient.builder()
        .serverUrl("https://api-staging.hagglex.com/graphql")
        .okHttpClient(
            OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor(context)).build()
        ).build()
    return instance!!
}
