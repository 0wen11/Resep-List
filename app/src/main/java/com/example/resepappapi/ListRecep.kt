package com.example.resepappapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.resepappapi.databinding.ActivityListRecepBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception


class ListRecep : AppCompatActivity() {

    companion object {
        private val TAG = ListRecep::class.java.simpleName
    }

    private lateinit var binding: ActivityListRecepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRecepBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "List of Recep"

        getListRecep()
    }

    private fun getListRecep () {
        binding.ProgressBar2.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://www.themealdb.com/api/json/v1/1/categories.php"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>?,
                responseBody: ByteArray
            ) {
                binding.ProgressBar2.visibility = View.INVISIBLE

                val listRecep = ArrayList<String>()

                val result = String(responseBody)
//                Log.d(TAG, result)
                try {
                    val jsonObjects = JSONObject (result)
                    val category = jsonObjects.getJSONArray("categories")

                    for (i in 0 until category.length()) {
                        val jsonObject = category.getJSONObject(i)

                        val strCategory = jsonObject.getString("strCategory")
                        val strdescription = jsonObject.getString("strCategoryDescription")

                        Log.e("data ke ${i+1} is ", strCategory)

                        listRecep.add("\n$strCategory\n - $strdescription\n")
                    }

                    val adapter = ArrayAdapter(this@ListRecep, android.R.layout.simple_list_item_1, listRecep)
                    binding.ListRecep.adapter = adapter
                } catch (e: Exception) {
                    Toast.makeText(this@ListRecep, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.ProgressBar2.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode: $[error.message}"
                }
                Toast.makeText(this@ListRecep, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        )
    }


}