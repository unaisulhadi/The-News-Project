package com.hadi.thenewsproject.activity

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadi.inspire.Utils.LatestItemDecoration
import com.hadi.thenewsproject.R
import com.hadi.thenewsproject.adapter.LatestAdapter
import com.hadi.thenewsproject.model.ArticlesItem
import com.hadi.thenewsproject.model.Headlines
import com.hadi.thenewsproject.network.RetrofitClient
import com.hadi.thenewsproject.utils.Constatnts
import com.hadi.thenewsproject.utils.Constatnts.API_KEY
import com.hadi.thenewsproject.utils.CustomTabHelper
import com.hadi.thenewsproject.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), LatestAdapter.OnItemClickListener {

    private val TAG = "SearchActivity"
    private var customTabHelper = CustomTabHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        init()
    }

    private fun init() {

        rvSearch.setHasFixedSize(true)
        rvSearch.layoutManager = LinearLayoutManager(this)
        rvSearch.addItemDecoration(LatestItemDecoration(20))

        btnBack.setOnClickListener {
            finish()
        }

        edtSearch.requestFocus()
        edtSearch.setOnEditorActionListener { v, actionId, event ->

            if (!v?.text.isNullOrEmpty()) {

                v.hideKeyboard()


                val query = v?.text.toString()

                val apiInterface = RetrofitClient.getInstance(this)
                val call = apiInterface.news.searchNews(query, API_KEY)

                call.enqueue(object : Callback<Headlines?> {

                    override fun onResponse(
                        call: Call<Headlines?>,
                        response: Response<Headlines?>
                    ) {

                        Log.d(TAG, "Search Response: ${response.body()}");
                        if (response.isSuccessful) {
                            if (response.body()?.status.equals("ok")) {
                                val articles = response.body()?.articles
                                val adapter = LatestAdapter(
                                    this@SearchActivity,
                                    articles as List<ArticlesItem>, this@SearchActivity
                                )

                                rvSearch.adapter = adapter
                            }
                        }else{
                            Toast.makeText(this@SearchActivity, "Error Occured", Toast.LENGTH_SHORT).show();
                        }
                    }

                    override fun onFailure(call: Call<Headlines?>, t: Throwable) {

                    }
                })


            }

            true
        }


    }


    override fun onItemClick(item: ArticlesItem) {

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this@SearchActivity, R.color.colorPrimary))
        builder.addDefaultShareMenuItem()

        val anotherCustomTab = CustomTabsIntent.Builder().build()
        val requestCode = 100
        val intent = anotherCustomTab.intent
        intent.putExtra("URLL", "TEST")
        intent.data = Uri.parse(item.url)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // add menu item to overflow
        builder.addMenuItem("Shareee", pendingIntent)

        builder.setShowTitle(true)

        builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()

        // check is chrom available
        val packageName = customTabHelper.getPackageNameToUse(this, item.url)

        if (packageName == null) {
            // if chrome not available open in web view
            val intentOpenUri = Intent(this, WebViewActivity::class.java)
            intentOpenUri.putExtra(Constatnts.EXTRA_URL, Uri.parse(item.url).toString())
            startActivity(intentOpenUri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(this, Uri.parse(item.url))
        }


    }
}
