package com.example.noonapp.ui.movies

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noonapp.R
import com.example.noonapp.data.RequestResult
import com.example.noonapp.data.database.DataThrowable
import com.example.noonapp.data.models.MovieShimmer
import com.example.noonapp.data.models.SearchedMovie
import com.example.noonapp.data.network.NetworkThrowable
import com.example.noonapp.ui.showToastAboveKeyboard
import com.example.noonapp.ui.utils.RxSearchObservable
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.movies_fragment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MoviesFragment : DaggerFragment() {

    companion object {
        val TAG = MoviesFragment.javaClass.name
        fun newInstance() = MoviesFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MoviesViewModel> {
        viewModelFactory
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.movies_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val findItem = menu.findItem(R.id.action_search)
        val actionView = findItem.actionView
        if (actionView is SearchView) {
            initSearchView(actionView)
        }
    }

    private fun init() {
        initRV()
        initViewModelObservers()
        getMoviesResponse("Matrix")
//        startShimmering()
    }

//    private fun startShimmering() {
//        shimmer_fl.startShimmer()
//    }
//
//    private fun stopShimmering() {
//        shimmer_fl.stopShimmer()
//    }


    override fun onStop() {
//        stopShimmering()
        super.onStop()
    }

    private fun initViewModelObservers() {
        with(viewModel)
        {
            getMoviesMld.observe(viewLifecycleOwner, Observer {
                onGetMoviesResponse(it)
            })
        }
    }

    private fun onGetMoviesResponse(requestResult: RequestResult<Any>) {
        Log.d(TAG, "onGetMoviesResponse: ${requestResult.toString()}")
        when (requestResult) {
            is RequestResult.Loading -> onGetMoviesResponseLoading(requestResult)
            is RequestResult.Error -> onGetMoviesResponseError(requestResult)
            is RequestResult.Success -> onGetMoviesResponseSuccess(requestResult)
        }
    }

    private fun onGetMoviesResponseLoading(requestResult: RequestResult.Loading<Any>) {
//        shimmer_fl.visibility = View.VISIBLE
        val shimmerList = getShimmerList()
        val runnable = Runnable {
            Log.d(TAG, "onGetMoviesResponseLoading: shimmerList commited")
        }
        submitList(shimmerList, runnable)
        movies_rv.visibility = View.VISIBLE
        Log.d(TAG, "onGetMoviesResponseLoading: ${requestResult.toString()}")
    }


    private fun getShimmerList(): ArrayList<Any> {
        val arrayListOf: ArrayList<Any> = arrayListOf<Any>()
        for (i in 0..10) {
            arrayListOf.add(MovieShimmer())
        }
        return arrayListOf
    }

    private var searchedMovie: SearchedMovie? = null
    private fun onGetMoviesResponseSuccess(requestResult: RequestResult.Success<Any>) {
        Log.d(TAG, "onGetMoviesResponseSuccess: ${requestResult.toString()}")
        val data = requestResult.data
        if (data is SearchedMovie) {
            searchedMovie = data
            val movies = data.movies
            val commitCallback = Runnable {
                Log.d(TAG, "onGetMoviesResponseSuccess: Runnable commited")
                val handler = Handler()
                handler.postDelayed({
                    Log.d(TAG, "onGetMoviesResponseSuccess: handler runs")
//                    stopShimmering()
//                    shimmer_fl.visibility = View.GONE
                    movies_rv.visibility = View.VISIBLE
                }, 500)
            }
            submitList(movies, commitCallback)
        }
    }

    private fun onGetMoviesResponseError(requestResult: RequestResult.Error<Any>) {

        Log.d(TAG, "onGetMoviesResponseError: $requestResult")
        val throwable = requestResult.throwable
        if (throwable is NetworkThrowable) {
            onGetMoviesNetworkError(throwable)
        } else if (throwable is DataThrowable) {
            onGetMoviesDataThrowable(throwable)
        }
    }

    private fun onGetMoviesNetworkError(throwable: NetworkThrowable) {
        requireActivity().showToastAboveKeyboard(
            getString(R.string.not_connected_retry_later),
            Toast.LENGTH_SHORT
        )
    }

    private fun onGetMoviesDataThrowable(throwable: DataThrowable) {
        val searchTerm = throwable.any
        val message = "${getString(R.string.nothing_found_for)} \"$searchTerm\""
        requireActivity().showToastAboveKeyboard(message, Toast.LENGTH_LONG)
    }

    private fun showToast(activity: Activity, view: View, message: String, duration: Int) {

    }

    private lateinit var adapter: MoviesAdapter
    private lateinit var itemDecorator: MoviesItemDecorator
    private fun initRV() {
        adapter = MoviesAdapter(
            requireContext()
        )
        itemDecorator = MoviesItemDecorator(requireContext())
        movies_rv.adapter = adapter
        movies_rv.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        movies_rv.addItemDecoration(itemDecorator)
    }


    private fun submitList(movieList: List<Any>, runnable: Runnable) {
        adapter.submitList(movieList, runnable)
    }

    private fun initSearchView(searchView: SearchView) {
        RxSearchObservable.fromview(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter {
                it.length >= 3
            }
            .map {
                it.toLowerCase().trim()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "initSearchView onNext: $it")
                viewModel.getMovies(it)
            }, {
                Log.d(TAG, "initSearchView onError: $it")
            }, {
                Log.d(TAG, "initSearchView onComplete")
                viewModel.getMovies("batman")
                initSearchView(searchView)
            })
    }

    private fun getMoviesResponse(searchTerm: String) {
        viewModel.getMovies(searchTerm)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}