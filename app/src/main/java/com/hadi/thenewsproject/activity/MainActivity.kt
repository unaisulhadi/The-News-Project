package com.hadi.thenewsproject.activity

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadi.inspire.Utils.LatestItemDecoration
import com.hadi.inspire.Utils.MarginItemDecoration
import com.hadi.thenewsproject.R
import com.hadi.thenewsproject.adapter.CustomTabsBroadcastReceiver
import com.hadi.thenewsproject.adapter.HeadlineAdapter
import com.hadi.thenewsproject.adapter.LatestAdapter
import com.hadi.thenewsproject.model.ArticlesItem
import com.hadi.thenewsproject.model.Headlines
import com.hadi.thenewsproject.network.RetrofitClient
import com.hadi.thenewsproject.utils.Constatnts.*
import com.hadi.thenewsproject.utils.CustomTabHelper
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity(), HeadlineAdapter.OnItemClickListener,
    LatestAdapter.OnItemClickListener {

    private val TAG = "MainActivity"

    lateinit var adapter:HeadlineAdapter
    private var customTabHelper = CustomTabHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        fetchHeadlines()

        btnSearch.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }

    }

    private fun init() {
        vp_headlines.addItemDecoration(MarginItemDecoration(10))


        rv_latest.setHasFixedSize(true)
        rv_latest.layoutManager = LinearLayoutManager(this)
        rv_latest.addItemDecoration(LatestItemDecoration(20))


        //WebView


    }

    private fun fetchHeadlines() {

        val apiInterface = RetrofitClient.getInstance(this)
        val call = apiInterface.news.getHeadlines(COUNTRY, API_KEY)

        call.enqueue(object : Callback<Headlines?> {

            override fun onResponse(call: Call<Headlines?>, response: Response<Headlines?>) {

                        shimmer.visibility = View.GONE
                        shimmer_headline.visibility = View.GONE
                        vp_headlines.visibility = View.VISIBLE
                        Log.d(TAG, "Headlines : ${response.body().toString()}");

                if(response.body()?.status.equals("ok")) {

                    val articles = response.body()?.articles as MutableList<ArticlesItem>
                    adapter = HeadlineAdapter(this@MainActivity, articles, this@MainActivity)
                    vp_headlines.adapter = adapter

                    articles.shuffle()


                    rv_latest.visibility = View.VISIBLE

                    val latestAdapter =
                        LatestAdapter(this@MainActivity, articles, this@MainActivity)
                    rv_latest.adapter = latestAdapter
                }else{
                    Toast.makeText(this@MainActivity, "Error Occured", Toast.LENGTH_SHORT).show();
                }

            }
            override fun onFailure(call: Call<Headlines?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Server Error", Toast.LENGTH_SHORT).show();
            }

        })

    }

    override fun onItemClick(item: ArticlesItem) {
//        val intent = Intent(this,NewsViewActivity::class.java)
//        intent.putExtra("NEWS_URL",item.url)
//        startActivity(intent)


        //Toast.makeText(this, item.content, Toast.LENGTH_SHORT).show();

/*

        val intent = Intent(this, CustomTabsBroadcastReceiver::class.java)

        val label = "Copy link"
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
1
        val customTabsIntent = CustomTabsIntent.Builder()
            .addMenuItem(label, pendingIntent)
            .build()

        customTabsIntent.launchUrl(this, Uri.parse("http://www.google.it"))
*/



        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
        builder.addDefaultShareMenuItem()

        val anotherCustomTab = CustomTabsIntent.Builder().build()
        val requestCode = 100
        //val intent = anotherCustomTab.intent
        val intent = Intent(this,CustomTabsBroadcastReceiver::class.java)
        val label = "Copy link"
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


//        intent.data = Uri.parse(item.url)
//        intent.putExtra("URLL","TEST")


       /* val pendingIntent = PendingIntent.getBroadcast(this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)*/

        // add menu item to overflow
        builder.addMenuItem(label, pendingIntent)

        builder.setShowTitle(true)

        builder.setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
        builder.setExitAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)

        val customTabsIntent = builder.build()

        // check is chrome available
        val packageName = customTabHelper.getPackageNameToUse(this, item.url)

        if (packageName == null) {
            // if chrome not available open in web view
            val intentOpenUri = Intent(this, WebViewActivity::class.java)
            intentOpenUri.putExtra(EXTRA_URL, Uri.parse(item.url).toString())
            startActivity(intentOpenUri)
        } else {
            customTabsIntent.intent.setPackage(packageName)
            customTabsIntent.launchUrl(this, Uri.parse(item.url))
        }
    }
}
