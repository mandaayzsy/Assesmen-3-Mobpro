package com.manda0101.assesmen3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manda0101.assesmen3.model.JamStatus
import com.manda0101.assesmen3.network.ApiStatus
import com.manda0101.assesmen3.network.JamApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {
    var data = mutableStateOf<JamStatus?>(null)
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = JamApi.service.getJam(token)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    suspend fun register(nama: String, email: String, password: String): String {
        var token = ""
        try {
            val result = JamApi.service.postRegister(
                nama,
                email,
                password
            )

            if (result.success) {
                token = result.data ?: ""
            }
        } catch (e: Exception) {
            Log.e("MainViewModel", "Failure: ${e.message}")
        }

        return token
    }


    fun saveData(token: String, name: String, rating: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = JamApi.service.postJam(
                    token,
                    name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    rating.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.success)
                    retrieveData(token)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    fun updateData(token: String, id_wikul: Long, name: String, rating: String, bitmap: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imagePart = bitmap?.toMultipartBody()
                val result = JamApi.service.updateJam(
                    token,
                    "PUT".toRequestBody("text/plain".toMediaTypeOrNull()),
                    id_wikul,
                    name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    rating.toRequestBody("text/plain".toMediaTypeOrNull()),
                    imagePart
                )

                if (result.success)
                    retrieveData(token)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    fun deleteData(token: String, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = JamApi.service.deleteJam(
                    token,
                    id
                )

                if (result.success)
                    retrieveData(token)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                errorMessage.value = e.message
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image","image.jpg", requestBody
        )
    }

    fun clearMessage() { errorMessage.value = null }
}