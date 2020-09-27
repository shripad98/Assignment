package com.myapplication.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.myapplication.R
import com.myapplication.fragments.MovieDetailFragment
import com.myapplication.model.SearchItem
import com.squareup.picasso.Picasso


class MovieListAdapter(val userList: ArrayList<SearchItem>, val context: Context?) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.listing_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return this.userList.size
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {
        holder.titleName?.text = this.userList.get(position).title
        holder.userItem?.setOnClickListener(View.OnClickListener { v ->
            openUserDetailPage(this.userList.get(position).imdbID)
        })
        Picasso.get()
            .load(this.userList.get(position).poster)
            //.resize(50, 50) // here you resize your image to whatever width and height you like
            .into(holder.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleName : TextView? = null
        var userItem : ConstraintLayout? = null
        var image :ImageView ? = null
        init {
            titleName = itemView.findViewById(R.id.titleName)
            userItem = itemView.findViewById(R.id.userItem)
            image = itemView.findViewById(R.id.image)
        }
    }

    private fun openUserDetailPage(id: String){
        val fragmentManager = (this.context as AppCompatActivity).supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val userDetailFragment = MovieDetailFragment()
        val data = Bundle()
        data.putString(this.context.getString(R.string.ID_VALUE), id);
        userDetailFragment.arguments = data
        fragmentTransaction.replace(R.id.frame_layout, userDetailFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    /*private class GetXMLTask :
        AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg p0: String?): Bitmap? {
            var map: Bitmap? = null
            for (url in p0) {
                map = downloadImage(p0.toString())
            }
            return map
        }
      *//*  protected override fun doInBackground(vararg urls: String): Bitmap? {
            var map: Bitmap? = null
            for (url in urls) {
                map = downloadImage(url)
            }
            return map
        }*//*

        // Sets the Bitmap returned by doInBackground
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }

        // Creates Bitmap from InputStream and returns it
        private fun downloadImage(url: String): Bitmap? {
            var bitmap: Bitmap? = null
            var stream: InputStream? = null
            val bmOptions = BitmapFactory.Options()
            bmOptions.inSampleSize = 1
            try {
                stream = getHttpConnection(url)
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions)
                stream?.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
            return bitmap
        }

        // Makes HttpURLConnection and returns InputStream
        @Throws(IOException::class)
        private fun getHttpConnection(urlString: String): InputStream? {
            var stream: InputStream? = null
            val url = URL(urlString)
            val connection: URLConnection = url.openConnection()
            try {
                val httpConnection: HttpURLConnection = connection as HttpURLConnection
                httpConnection.setRequestMethod("GET")
                httpConnection.connect()
                if (httpConnection.getResponseCode() === HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return stream
        }


    }*/
}