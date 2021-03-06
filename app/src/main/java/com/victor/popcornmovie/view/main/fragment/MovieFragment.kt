package com.victor.popcornmovie.view.main.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.victor.popcornmovie.R
import com.victor.popcornmovie.databinding.FragmentMovieBinding
import com.victor.popcornmovie.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MovieFragment : Fragment() {
    private var viewBinding: FragmentMovieBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMovieBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setupView()
    }

    private fun getYearFromDate(date: String): Int {
        val calendar = GregorianCalendar()
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        calendar.time = dateFormat.parse(date) ?: Date()
        return calendar.get(Calendar.YEAR)
    }

    private fun glideListener(): RequestListener<Drawable> = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }
    }

    private fun setupView() {
        viewBinding?.apply {
            (activity as MainActivity).clickedMovie?.let { movie ->
                movieTitle.text = movie.title
                movieYear.text = getYearFromDate(movie.releaseDate).toString()
                movieOverview.text = movie.overview

                Glide.with(root)
                    .load(IMAGE_BASE_URL + movie.posterPath)
                    .centerInside()
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.image_placeholder)
                    .listener(glideListener())
                    .into(moviePoster)
            }
        }
    }

    companion object {
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val DATE_FORMAT = "yyyy-MM-dd"
    }
}