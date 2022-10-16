package com.example.meamsshare

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.meamsshare.databinding.ActivityMainBinding
import com.example.meamsshare.databinding.ActivityMainBinding.inflate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    var currentUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        val view = binding.root //you can use directly setContentView(binding.root)
        setContentView(view)

        LoadMeams()

        //this is for custom progress dialog
//        val dialog = Dialog(this)
//        val inflate = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null)
//        dialog.setContentView(inflate)
//        dialog.setCancelable(false)
//        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.show()

        binding.btnnext.setOnClickListener {
            //load next meams
            binding.progressBar.visibility = View.VISIBLE
        }
        binding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Hey Check this memes $currentUrl")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share Via"))
        }
    }


    //method for api calling
    private fun LoadMeams(){
        binding.progressBar.visibility = View.VISIBLE
       // val textView = findViewById<TextView>(R.id.text)
// Instantiate the RequestQueue.
       // val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"


// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                currentUrl = response.getString("url")

                Glide.with(this).load(currentUrl).listener(object : RequestListener<Drawable> {

                    //method override
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                }).into(binding.memeimageview)

            },
            {
                Log.d("Error",it.localizedMessage)
            })

// Add the request to the RequestQueue.
       // queue.add(jsonObjectRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}