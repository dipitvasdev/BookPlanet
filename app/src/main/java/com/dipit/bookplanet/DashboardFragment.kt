package com.dipit.bookplanet

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dipit.bookplanet.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {
    lateinit var recyclerDash:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: DashboardAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    var bookInfoList= arrayListOf<Book>()
    var ratingComparator = Comparator<Book> {book1,book2 ->
        if(book1.bookRating.compareTo(book2.bookRating,ignoreCase = true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_dashboard,container,false)
        setHasOptionsMenu(true)
        recyclerDash=view.findViewById(R.id.recyclerview)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        layoutManager=LinearLayoutManager(activity)
        val queue= Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v1/book/fetch_books/"
        if(ConnectionManager().checkConnectivity(activity as Context)){
            val jsonObjectRequest= object : JsonObjectRequest(Request.Method.GET,url,null,Response.Listener {
                try {
                    progressLayout.visibility=View.GONE
                    val success= it.getBoolean("success")
                    if(success){
                        val data= it.getJSONArray("data")
                        for(i in 0 until data.length()){
                            val bookJsonObject=data.getJSONObject(i)
                            val bookObject=Book (
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("image")
                            )
                            bookInfoList.add(bookObject)
                            recyclerAdapter= DashboardAdapter(activity as Context,bookInfoList)
                            recyclerDash.adapter=recyclerAdapter
                            recyclerDash.layoutManager=layoutManager
                        }
                    } else {
                        Toast.makeText(activity as Context,"Some Error Occurred!",Toast.LENGTH_SHORT).show()
                    }
                }  catch(e:JSONException){
                    Toast.makeText(activity as Context,"Some Unexpected Error on Our End, please try again later ! ",Toast.LENGTH_LONG).show()
                }
            } , Response.ErrorListener {
                if(activity!=null) {
                    Toast.makeText(activity as Context, "App Internal Error ! ", Toast.LENGTH_LONG)
                        .show()
                }
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers= HashMap<String,String>()
                    headers["Content-type"]= "application/json"
                    headers["token"] = "ed0e68368529be"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Oops! Seems like you are not connected to internet!!")
            dialog.setPositiveButton("Open Settings") {text,listener->
                val settingIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") {text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }

        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}