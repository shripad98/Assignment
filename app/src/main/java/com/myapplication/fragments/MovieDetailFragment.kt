package com.myapplication.fragments

import android.app.ProgressDialog
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.myapplication.R

import com.myapplication.model.MovieDetailModel
import com.myapplication.utils.APIClient
import com.myapplication.utils.APiInterface
import kotlinx.android.synthetic.main.fragment_movie_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailFragment : Fragment() {

    lateinit var header: TextView
     var id : kotlin.String? = null
    var loading: ProgressDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }
    private fun initView(){
        header = activity?.findViewById<View>(R.id.header) as TextView
        id = arguments?.getString(context?.getString(R.string.ID_VALUE))
        if (isNetworkAvailable()) {
            getMovieDetailsAPI()
        }
    }

    override fun onResume() {
        super.onResume()
        header.text = getString(R.string.movie_details)
    }
    private fun getMovieDetailsAPI(){
        showProgressBar()
        val apiService: APiInterface = APIClient().getClient().create(APiInterface::class.java)
        val call: Call<MovieDetailModel> = apiService.getMovieDetail("b9bd48a6",id)
        call.enqueue(object : Callback<MovieDetailModel> {
            override fun onResponse(call: Call<MovieDetailModel>, response: Response<MovieDetailModel>) {
                loadingDismiss()
                if (response != null) {
                    if (response.body() != null){
                      setValues(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetailModel>, t: Throwable) {
                loadingDismiss()
                Log.e("Failure", t.toString())
            }
        })
    }
    private fun setValues(userResponse : MovieDetailModel){
        titleValue?.text = userResponse.title
        yearValue?.text = userResponse.year
        val split = userResponse.runtime.split(" ")
        val hours = split[0].toInt() / 60
        val min = split[0].toInt() % 60
        runTime?.text = hours.toString()+" hr "+ min.toString()+" min"
        ratings?.text = userResponse.imdbRating
        synopsisValue?.text = userResponse.plot
        scoreValue?.text = userResponse.metascore
        popularityValue?.text = userResponse.imdbVotes
        directorValue?.text = userResponse.director
        writerValue?.text = userResponse.writer
        actorsValue?.text = userResponse.actors
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
}
