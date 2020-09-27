package com.myapplication.fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.myapplication.R
import com.myapplication.adapter.MovieListAdapter
import com.myapplication.model.MovieListModel
import com.myapplication.model.SearchItem
import com.myapplication.utils.APIClient
import com.myapplication.utils.APiInterface
import kotlinx.android.synthetic.main.fragment_movie_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class MovieListFragment : Fragment(), View.OnClickListener {

    lateinit var header: TextView
    lateinit var adapter : MovieListAdapter
    var movieList : MutableList<SearchItem>? = null
     var loading: ProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }
    private fun initView(){
        header = activity?.findViewById<View>(R.id.header) as TextView
        usersRecyclerView?.layoutManager = GridLayoutManager(context,2)
        movieList = ArrayList<SearchItem>()
        adapter = MovieListAdapter(movieList as ArrayList<SearchItem>,context)
        usersRecyclerView?.adapter = adapter
        searchButton.setOnClickListener(this)
    }
    override fun onResume() {
        super.onResume()
        header.text = getString(R.string.movie_list)
        if (!(searchMovie.text.toString().trim().isEmpty())){
            if (isNetworkAvailable()) {
                getMovieListAPI()
            }
        }
    }

    private fun getMovieListAPI(){
        showProgressBar()
        val apiService: APiInterface = APIClient().getClient().create(APiInterface::class.java)
        val call: Call<MovieListModel> = apiService.getMovieList("b9bd48a6", searchMovie.text.toString().trim(), "movie")
        call.enqueue(object : Callback<MovieListModel> {
            override fun onResponse(call: Call<MovieListModel>, response: Response<MovieListModel>) {
                if (response != null) {
                  if (response.body() != null){
                      if (response.body()!!.search != null){
                          movieList?.clear()
                          movieList?.addAll(response.body()!!.search)
                          adapter.notifyDataSetChanged()
                      }
                      else {
                          movieList?.clear()
                          Toast.makeText(context, "No Matching Movies", Toast.LENGTH_LONG).show()
                      }

                  }
                }
                loadingDismiss()
            }

            override fun onFailure(call: Call<MovieListModel>, t: Throwable) {
                loadingDismiss()
                Log.e("Failure", t.toString())
            }
        })
    }
    fun showProgressBar() {
        loading = ProgressDialog(context, R.style.MyAlertDialogStyle)
            loading?.setCancelable(false)
            loading?.setMessage("Loading")
            loading?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            loading?.show()

    }
    fun loadingDismiss() {
            loading?.dismiss()
    }
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected) {
            return true
        } else {
            Toast.makeText(context, "Internet Connection not available", Toast.LENGTH_LONG).show()
        }
        return false
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
           R.id.searchButton -> {
               if (searchMovie.text.toString().trim().isEmpty()){
                   search_error_text.visibility = View.VISIBLE
               }
               else {
                   search_error_text.visibility = View.GONE
                   if (isNetworkAvailable()) {
                       getMovieListAPI()
                   }
               }
           }
        }
    }

}
