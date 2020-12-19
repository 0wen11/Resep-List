package com.example.resepappapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.resepappapi.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        private val Tag = MainActivity::class.java.simpleName
    }

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Button.setOnClickListener {
            val intent = Intent(this, ListRecep::class.java)
            startActivity(intent)
        }

        getrandomdata()
    }

    private fun getrandomdata() {
        binding.ProgressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://www.themealdb.com/api/json/v1/1/random.php"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding.ProgressBar.visibility = View.INVISIBLE

                val result = responseBody?.let { String(it) }
                // val result = String(responseBody)
                Log.e("results", result.toString())

                try {
                    val jsonObjects = JSONObject(result)
                    val meals = jsonObjects.getJSONArray("meals")

                    val dataObject = meals.getJSONObject(0)

                    val strInstruct = dataObject.getString("strInstructions")
                    val strMeal = dataObject.getString("strMeal")
//                    val strThumb = dataObject.getString("strMealThumb")

                    binding.T1tle.text = strMeal
                    binding.Instruction.text = strInstruct

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "error on catch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.ProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this@MainActivity, "failure", Toast.LENGTH_SHORT).show()
            }

        })

    }
}