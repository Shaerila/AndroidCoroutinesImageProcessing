package com.devtides.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.devtides.imageprocessingcoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val IMAGE_URL =
        "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    private val IMAGE_OTHER =
        "https://res.cloudinary.com/teepublic/image/private/s--b24gPl2v--/t_Resized%20Artwork/c_fit,g_north_west,h_954,w_954/co_000000,e_outline:48/co_000000,e_outline:inner_fill:48/co_ffffff,e_outline:48/co_ffffff,e_outline:inner_fill:48/co_bbbbbb,e_outline:3:1000/c_mpad,g_center,h_1260,w_1260/b_rgb:eeeeee/c_limit,f_auto,h_630,q_auto:good:420,w_630/v1556103623/production/designs/4706622_0.jpg"
    private val CoroutineScope = CoroutineScope(Dispatchers.Main)

    val random = Random.nextInt(2)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //CoroutineScope.launch(Dispatchers.Default)
        runBlocking{
            //Network communication with input/output, so using IO dispatcher
            val originalDeferred = CoroutineScope.async(Dispatchers.IO) { getOriginalBitmap() }

            val originalBitmap = originalDeferred.await()

            loadImage(originalBitmap)
        }



    }



    private fun getOriginalBitmap() =

        // This is a network call, so it shouldn't be done from the main thread
        URL(choosenImage(random)).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun loadImage(bmp: Bitmap){
        binding.progressBar.visibility = View.GONE
        binding.imageView.setImageBitmap(bmp)
        binding.imageView.visibility = View.VISIBLE

    }

    private fun choosenImage(num: Int): String{
        var result = ""
        when(num) {
            0 -> result = IMAGE_OTHER
            1 -> result = IMAGE_URL
        }
        return result
    }
}

