package com.example.instagrame.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.instagrame.R
import com.example.instagrame.databinding.ActivityMainBinding
import com.example.instagrame.ui.Adapter.MessagingAdapter
import com.example.instagrame.ui.Data.Message
import com.example.instagrame.ui.Util.BotResponse
import com.example.instagrame.ui.Util.Constaints.OPEN_GOOGLE
import com.example.instagrame.ui.Util.Constaints.OPEN_SEARCH
import com.example.instagrame.ui.Util.Constaints.RECEIVE_ID
import com.example.instagrame.ui.Util.Constaints.SEND_ID
import com.example.instagrame.ui.Util.Time
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    lateinit var recyclerView: Recycler
    lateinit var msgedittext: EditText
    lateinit var sendbtn: ImageView

    var messagesList = mutableListOf<Message>()
    private val botList = listOf("Bilkis", "moto","AI","CHATBOT")
    private lateinit var adapter: MessagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root )
        msgedittext = findViewById(R.id.msgedittext)
        sendbtn = findViewById(R.id.sendbtn)
        adapter= MessagingAdapter()
        recyclerView()

        clickEvents()

        val random = (0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")


        binding.sendbtn.setOnClickListener {
            sendMessage()
        }

        binding.msgedittext.setOnClickListener {
            GlobalScope
            .launch {
                delay(50)
                withContext(Dispatchers.Main) {
                    binding.rec.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun clickEvents() {
        binding.sendbtn.setOnClickListener {
            sendMessage()
        }
    }

    private fun recyclerView() {
        binding.rec.layoutManager = LinearLayoutManager(applicationContext)
        binding.rec.adapter = MessagingAdapter()

    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.rec.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = msgedittext.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(Message(message, SEND_ID, timeStamp))
            msgedittext.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            binding.rec.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)

                //Adds it to our local list
                messagesList.add(Message(response, RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                binding.rec.scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }

                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }


    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                binding.rec.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}
