package com.example.noonapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noonapp.R
import com.example.noonapp.data.NetworkThrowable
import com.example.noonapp.data.database.DataThrowable
import com.example.noonapp.data.network.RequestResult
import com.example.noonapp.di.InjectorUtils
import com.example.noonapp.models.Movie
import com.example.noonapp.models.SearchedMovie
import com.example.noonapp.ui.utils.RxSearchObservable
import kotlinx.android.synthetic.main.movies_fragment.*
import java.util.concurrent.TimeUnit

class MoviesFragment : Fragment() {

    companion object {
        val TAG = MoviesFragment.javaClass.name
        fun newInstance() = MoviesFragment()
    }

    private val viewModel: MoviesViewModel by viewModels {
        InjectorUtils.provideMoviesViewModelFactory(requireContext())
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
        when (requestResult) {
            is RequestResult.Loading -> onGetMoviesResponseLoading(requestResult)
            is RequestResult.Error -> onGetMoviesResponseError(requestResult)
            is RequestResult.Success -> onGetMoviesResponseSuccess(requestResult)
        }
    }

    private fun onGetMoviesResponseLoading(requestResult: RequestResult.Loading<Any>) {
        Log.d(TAG, "onGetMoviesResponseLoading: $requestResult")
    }

    private var searchedMovie: SearchedMovie? = null
    private fun onGetMoviesResponseSuccess(requestResult: RequestResult.Success<Any>) {
        val data = requestResult.data
        if (data is SearchedMovie) {
            searchedMovie = data
            val movies = data.movies
            submitList(movies)
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
        Toast.makeText(
            requireContext(),
            getString(R.string.not_connected_retry_later),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onGetMoviesDataThrowable(throwable: DataThrowable) {
        val searchTerm = throwable.any
        Toast.makeText(
            requireContext(),
            getString(R.string.nothing_found_for) + " $searchTerm",
            Toast.LENGTH_SHORT
        ).show()
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


    fun submitList(movieList: List<Movie>) {
        adapter.submitList(movieList)
    }

    fun initSearchView(searchView: SearchView) {
        RxSearchObservable.fromview(searchView)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter {
                it.length >= 3
            }
            .map {
                it.toLowerCase().trim()
            }
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