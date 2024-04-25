package org.hilfulfujul.allajhari.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.hilfulfujul.allajhari.ads.Ads
import org.hilfulfujul.allajhari.setting.Response

class AdsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collectionName = "ads"

    fun getBannerAds(): Flow<Response<Ads>> = flow {
        try {
            val snapshot = firestore.collection(collectionName).document("banner").get().await()

            emit(
                Response.Success(
                    Ads(
                        snapshot.get("ads") as Boolean, snapshot.get("banner") as String
                    )
                )
            )

        } catch (exception: Exception) {
            emit(Response.Error(exception.message ?: "Error fetching ads data"))
        }
    }

    fun getInterstitialAds(): Flow<Response<Ads>> = flow {
        try {
            val snapshot =
                firestore.collection(collectionName).document("interstitial").get().await()

            emit(
                Response.Success(
                    Ads(
                        snapshot.get("ads") as Boolean, snapshot.get("interstitial") as String
                    )
                )
            )

        } catch (exception: Exception) {
            emit(Response.Error(exception.message ?: "Error fetching ads data"))
        }
    }
}