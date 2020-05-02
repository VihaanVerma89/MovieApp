package com.example.noonapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.noonapp.R
import com.example.noonapp.network.RequestResult
import com.example.noonapp.ui.utils.RxSearchObservable
import java.util.concurrent.TimeUnit

class MoviesFragment : Fragment() {

    companion object {
        val TAG = MoviesFragment.javaClass.name
        fun newInstance() = MoviesFragment()
    }

    private lateinit var viewModel: MoviesViewModel

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
        initViewModel()
        initViewModelObservers()
        getMoviesResponse("shawshank redemption")
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
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

    private fun onGetMoviesResponseSuccess(requestResult: RequestResult.Success<Any>) {

        Log.d(TAG, "onGetMoviesResponseSuccess: $requestResult")
    }

    private fun onGetMoviesResponseError(requestResult: RequestResult.Error<Any>) {

        Log.d(TAG, "onGetMoviesResponseError: $requestResult")
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
            }, {
                Log.d(TAG, "initSearchView onError: $it")
            }, {
                Log.d(TAG, "initSearchView onComplete")
            })
    }

    private fun getMoviesResponse(searchTerm: String) {
        viewModel.getMovies(searchTerm)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}