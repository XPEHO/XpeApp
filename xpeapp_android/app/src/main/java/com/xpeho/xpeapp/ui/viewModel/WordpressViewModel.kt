package com.xpeho.xpeapp.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.WordpressAPI
import com.xpeho.xpeapp.ui.uiState.WordpressUiState
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.io.IOException

class WordpressViewModel : ViewModel() {

    var body: AuthentificationBody? by mutableStateOf(null)

    var wordpressState: WordpressUiState by mutableStateOf(WordpressUiState.EMPTY)
    var token: WordpressToken? by mutableStateOf(null)

    fun onLogin(datastorePref: DatastorePref, featureFlippingViewModel: FeatureFlippingViewModel) {
        viewModelScope.launch {
            body?.let { authent ->
                // Show loader
                wordpressState = WordpressUiState.LOADING

                try {
                    // Connect to Wordpress API
                    val result = WordpressAPI.service.authentification(authent)
                    token = result

                    // Connect to Firebase
                    FirebaseAuth.getInstance().signInAnonymously().await()

                    // Check if the user is authorized
                    if (checkUserAuthorization(authent.username)) {
                        Log.d("WordpressViewModel", "Firebase connected")
                        // Save the connected boolean in Datastore
                        viewModelScope.launch { datastorePref.setIsConnectedLeastOneTime(true) }

                        // Get the user id from database
                        val userId = WordpressAPI.service.getUserId(authent.username)

                        // Save the user id in Datastore
                        viewModelScope.launch { datastorePref.setUserId(userId) }

                        // Load feature flipping
                        featureFlippingViewModel.getFeatureFlipping()
                        wordpressState = if (featureFlippingViewModel.uiState is FeatureFlippingUiState.SUCCESS) {
                            WordpressUiState.SUCCESS(token = result)
                        } else {
                            Log.e("WordpressViewModel", featureFlippingViewModel.uiState.toString())
                            WordpressUiState.ERROR(
                                error = "Oups, il y a eu un problème dans le chargement des fonctionnalités"
                            )
                        }
                    } else {
                        wordpressState = WordpressUiState.ERROR(
                            error = "Vous n'avez pas les droits pour accéder à cette page"
                        )
                    }
                } catch (httpException: HttpException) {
                    wordpressState = WordpressUiState.ERROR(
                        error = getErrorMessage(
                            error = httpException.code().toString()
                        )
                    )
                } catch (ioException: IOException) {
                    wordpressState = WordpressUiState.ERROR(
                        error = ioException.message ?: "Une erreur est survenue ! Veuillez réessayer."
                    )
                }
            }
        }
        println("WordpressViewModel : $wordpressState")
    }

    private suspend fun checkUserAuthorization(username: String): Boolean {
        val isAuthorized = mutableStateOf(false)

        val db = FirebaseFirestore.getInstance()

        val users = db.collection("wordpressUsers")
            .get()
            .await()

        for (user in users.documents) {
            if (user["email"] == username) {
                isAuthorized.value = true
                break
            } else {
                isAuthorized.value = false
            }
        }

        return isAuthorized.value
    }

    fun logout() {
        token = null
        FirebaseAuth.getInstance().signOut()
        wordpressState = WordpressUiState.EMPTY
    }

    private fun getErrorMessage(error: String?): String {
        val errorReturn = mutableStateOf("Une erreur est survenue ! Veuillez réessayer.")
        when (error) {
            "400" -> errorReturn.value = "Identifiants incorrects"
            "403" -> errorReturn.value = "Vous n'avez pas les droits pour accéder à cette page"
            "404" -> errorReturn.value = "Page introuvable"
            "500" -> errorReturn.value = "Une erreur est survenue ! Veuillez réessayer."
        }
        return errorReturn.value
    }
}
