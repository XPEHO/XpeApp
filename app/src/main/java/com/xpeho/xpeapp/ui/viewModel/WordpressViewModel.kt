package com.xpeho.xpeapp.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.xpeho.xpeapp.data.DatastorePref
import com.xpeho.xpeapp.data.entity.AuthentificationBody
import com.xpeho.xpeapp.data.model.WordpressToken
import com.xpeho.xpeapp.data.service.ErrorResponse
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

    fun onLogin(datastorePref: DatastorePref) {
        viewModelScope.launch {
            body?.let { authent ->
                wordpressState = try {
                    // Connect to Wordpress API
                    val result = WordpressAPI.service.authentification(authent)
                    token = result
                    // Connect to Firebase
                    FirebaseAuth.getInstance().signInAnonymously().await()
                    // Save the connected boolean in Datastore
                    datastorePref.setIsConnectedLeastOneTime(true)
                    // Check if the user is authorized
                    if (checkUserAuthorization(authent.username)) {
                        WordpressUiState.SUCCESS(token = result)
                    } else {
                        WordpressUiState.ERROR(
                            error = "Vous n'avez pas les droits pour accéder à cette page"
                        )
                    }
                } catch (httpException: HttpException) {
                    val errorBody = httpException.response()?.errorBody()?.string()

                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)

                    val errorDescription = getErrorMessage(
                        errorResponse?.code,
                    )

                    WordpressUiState.ERROR(error = errorDescription)
                } catch (ioException: IOException) {
                    WordpressUiState.ERROR(
                        error = ioException.message ?: "Une erreur est survenue ! Veuillez réessayer."
                    )
                }
            }
        }
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
