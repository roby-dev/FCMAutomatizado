package com.example.automatizadofcm.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

import com.example.automatizadofcm.R
import com.example.automatizadofcm.data.NotificationData
import com.example.automatizadofcm.data.PushNotification
import com.example.automatizadofcm.instance.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC ="/topics/myTopic"

class MainActivity : AppCompatActivity() {

    val TAG="MainActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSend = findViewById<Button>(R.id.btnSend);
        val txtTitle = findViewById<EditText>(R.id.txtTitulo);
        val txtMensaje = findViewById<EditText>(R.id.txtMensaje);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        btnSend.setOnClickListener {
            val title = txtTitle.text.toString()
            val message = txtMensaje.text.toString()
            if(title.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(title,message),
                    TOPIC
                ).also {
                    sendNotification(it)
                }
            }
        }

    }



    private fun sendNotification(notification:PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d(TAG,"Response: ${Gson().toJson(response)}")
            }else{
                Log.e(TAG,response.errorBody().toString())
            }
        }catch (e:Exception){
            Log.e(TAG,e.toString());
        }
    }
}