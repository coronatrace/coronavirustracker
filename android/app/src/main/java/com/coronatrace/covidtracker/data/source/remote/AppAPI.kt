package com.coronatrace.covidtracker.data.source.remote

import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.graphql.GraphQLRequest
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.graphql.MutationType
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Contact
import com.amplifyframework.datastore.generated.model.DetectionSource
import com.amplifyframework.datastore.generated.model.Infection
import com.coronatrace.covidtracker.auth.Auth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class AppAPI(context: Context) {

    private var isAuthenticated = false

    // Initialise Auth
    init {
        if (!isAuthenticated) {
            Auth(context)
            isAuthenticated = true

            // Initialise amplify
            try {
                Log.i("AmplifyGetStarted", "Amplify is ready for use!")
            } catch (configurationFailure: AmplifyException) {
                Log.e(
                    "AmplifyGetStarted",
                    "Failed to configure Amplify",
                    configurationFailure
                )
            }
        }
    }

    suspend fun addContact(contactTimestamp: Long, contactUserId: String): Contact {
        val contact: Contact =
            Contact.builder().userId("dummyForAmplifyOnly").contactUserId(contactUserId)
                .contactTimestamp(contactTimestamp).build()

        return suspendCoroutine<Contact> { continuation ->
            Amplify.API.mutate(contact, MutationType.CREATE,
                { res -> continuation.resume(res.data) },
                { err -> continuation.resumeWithException(err) })
        }
    }

    suspend fun addInfection(infectedTimestamp: Long, detectionSource: DetectionSource): Infection {
        val infection: Infection = Infection.builder().infectedTimestamp(infectedTimestamp)
            .detectionSource(detectionSource).build()

        return suspendCoroutine<Infection> { continuation ->
            Amplify.API.mutate(infection, MutationType.CREATE,
                { res -> continuation.resume(res.data) },
                { err -> continuation.resumeWithException(err) })
        }
    }

    suspend fun getInfections(userId: String): Infection{
        return suspendCoroutine<Infection> { continuation ->
            Amplify.API.query(Infection::class.java,
                userId,
                { res -> continuation.resume(res.getData()) },
                { err -> continuation.resumeWithException(err) }
            )
        }
    }

}