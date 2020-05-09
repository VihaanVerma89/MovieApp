package com.example.noonapp.ui.movies

import android.os.Bundle
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
        getMoviesResponse(searchTerm)
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
        val shimmerList = getShimmerList()
        val runnable = Runnable {
            Log.d(TAG, "onGetMoviesResponseLoading: shimmerList commited")
        }
        submitList(shimmerList, runnable)
        showMoviesRv()
        hideRetry()
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
    private var networkThrowable: NetworkThrowable? = null
    private var dataThrowable: DataThrowable? = null
    private fun onGetMoviesResponseSuccess(requestResult: RequestResult.Success<Any>) {
        Log.d(TAG, "onGetMoviesResponseSuccess: ${requestResult.toString()}")
        val data = requestResult.data
        if (data is SearchedMovie) {
            if (data.isFromDatabase()) {
                onGetMoviesResponseDatabaseSuccess(data)
            }
        }
    }

    private fun onGetMoviesResponseDatabaseSuccess(data: SearchedMovie) {
        searchedMovie = data
        val movies = data.movies
        if (movies.isNotEmpty()) {
            val commitCallback = Runnable {
                Log.d(TAG, "onGetMoviesResponseSuccess: Runnable commited")
//                val handler = Handler()
//                handler.postDelayed({
//                    Log.d(TAG, "onGetMoviesResponseSuccess: handler runs")
//                    movies_rv.visibility = View.VISIBLE
//                }, 500)
            }
            submitList(movies, commitCallback)
            hideRetry()
            showMoviesRv()
        } else {
            // database doesn't have movies
            showRetryForDbIfRequired(data)
            showNotFoundIfRequired(data)
        }
    }

    private fun showRetryForDbIfRequired(data: SearchedMovie) {
        val networkThrowableSearchTerm = networkThrowable?.any ?: ""
        if (networkThrowableSearchTerm is String) {
            if (networkThrowableSearchTerm.isNotEmpty()) {
                val searchTermObj = data.searchTerm
                if (networkThrowableSearchTerm == searchTermObj.searchTerm) {
                    // for this search term we don't have anything in database
                    // and we aren't able to connect to internet
                    hideMoviesRv()
                    showRetry()
                }
            }
        }
    }

    private fun showRetryForNetworkIfRequired() {
        val searchedMovie1 = searchedMovie
        if (searchedMovie1 != null) {
            if (searchedMovie1.movies.isNullOrEmpty()) {
                // database doesn't have movies
                // and we received network error
                hideMoviesRv()
                showRetry()
            } else {
                // database is showing cached movies
                // we are unable to refresh
                toast?.cancel()
                toast = requireActivity().showToastAboveKeyboard(
                    getString(R.string.not_connected_retry_later),
                    Toast.LENGTH_SHORT
                )
            }
        }
        else{
            // wait for database to push data to view
        }
    }

    private fun showNotFoundIfRequired(data: SearchedMovie) {

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

    private var toast: Toast? = null
    private fun onGetMoviesNetworkError(networkThrowable: NetworkThrowable) {
        this.networkThrowable = networkThrowable
        showRetryForNetworkIfRequired()
    }

    private fun hideMoviesRv() {
        movies_rv.visibility = View.GONE
    }

    private fun showMoviesRv() {
        movies_rv.visibility = View.VISIBLE
    }

    private fun showRetry() {
        retry_cl.visibility = View.VISIBLE
        retry_iv.playAnimation()
        retry_iv.setOnClickListener {
            retry_iv.playAnimation()
        }
        retry_btn.setOnClickListener {
            getMoviesResponse(searchTerm)
        }
    }

    private fun hideRetry() {
        retry_cl.visibility = View.GONE
    }

    private fun onGetMoviesDataThrowable(dataThrowable: DataThrowable) {
        toast?.cancel()
        val searchTerm = dataThrowable.any
        val message = "${getString(R.string.nothing_found_for)} \"$searchTerm\""
        toast =
            requireActivity().showToastAboveKeyboard(message, Toast.LENGTH_LONG)
        this.dataThrowable = dataThrowable
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

    private var searchTerm: String = "matrix"
    private fun getMoviesResponse(searchTerm: String) {
        this.searchTerm = searchTerm
        viewModel.getMovies(searchTerm)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}