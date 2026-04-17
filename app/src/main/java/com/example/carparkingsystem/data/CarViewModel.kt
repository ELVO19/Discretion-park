package com.example.carparkingsystem.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.carparkingsystem.models.CarModel
import com.example.carparkingsystem.navigation.ROUTE_CAR_LIST
import com.example.carparkingsystem.navigation.ROUTE_DASHBOARD
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

class CarViewModel : ViewModel() {

    val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dc34acst4/image/upload"
    val uploadPreset  = "image_folder"

    // ── State ──────────────────────────────────────────────────────────────
    private val _cars = mutableStateListOf<CarModel>()
    val cars: List<CarModel> = _cars

    // ── Fetch ──────────────────────────────────────────────────────────────
    fun fetchCar(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("Cars")
        ref.get()
            .addOnSuccessListener { snapshot ->
                _cars.clear()
                for (child in snapshot.children) {
                    val car = child.getValue(CarModel::class.java)
                    car?.let {
                        it.id = child.key
                        _cars.add(it)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load cars", Toast.LENGTH_LONG).show()
            }
    }

    // ── Upload (Add) ───────────────────────────────────────────────────────
    fun uploadCar(
        imageUri: Uri?,
        plateNumber: String,
        vehicleType: String,
        driverName: String,
        phoneNumber: String,
        carColor: String,
        entryTime: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = FirebaseDatabase.getInstance().getReference("Cars").push()
                val carData = mapOf(
                    "id"          to ref.key,
                    "plateNumber" to plateNumber,
                    "vehicleType" to vehicleType,
                    "driverName"  to driverName,
                    "phoneNumber" to phoneNumber,
                    "carColor"    to carColor,
                    "entryTime"   to entryTime,
                    "imageUrl"    to imageUrl
                )
                ref.setValue(carData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car saved successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARD)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car not saved: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ── Delete ─────────────────────────────────────────────────────────────
    fun deleteCar(carId: String, context: Context) {
        FirebaseDatabase.getInstance()
            .getReference("Cars")
            .child(carId)
            .removeValue()
            .addOnSuccessListener {
                _cars.removeIf { it.id == carId }
                Toast.makeText(context, "Vehicle deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Delete failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    // ── Update ─────────────────────────────────────────────────────────────
    fun updateCar(
        carId: String,
        imageUri: Uri?,
        existingImageUrl: String?,
        plateNumber: String,
        vehicleType: String,
        driverName: String,
        phoneNumber: String,
        carColor: String,
        entryTime: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Upload new image only if user picked one, otherwise keep existing URL
                val imageUrl = if (imageUri != null) {
                    uploadToCloudinary(context, imageUri)
                } else {
                    existingImageUrl
                }

                val updates = mapOf(
                    "plateNumber" to plateNumber,
                    "vehicleType" to vehicleType,
                    "driverName"  to driverName,
                    "phoneNumber" to phoneNumber,
                    "carColor"    to carColor,
                    "entryTime"   to entryTime,
                    "imageUrl"    to imageUrl
                )

                FirebaseDatabase.getInstance()
                    .getReference("Cars")
                    .child(carId)
                    .updateChildren(updates)
                    .await()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Vehicle updated successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_CAR_LIST) {
                        popUpTo(ROUTE_CAR_LIST) { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // ── Cloudinary helper ──────────────────────────────────────────────────
    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val fileBytes = inputStream?.readBytes() ?: throw Exception("Image read failed")
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", "image.jpg",
                RequestBody.create("image/*".toMediaTypeOrNull(), fileBytes)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()
        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = OkHttpClient().newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Upload failed")
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)
        return secureUrl ?: throw Exception("Failed to get image URL")
    }
}