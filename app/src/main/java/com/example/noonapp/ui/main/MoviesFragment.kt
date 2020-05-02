package com.example.noonapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noonapp.R
import com.example.noonapp.models.MoviesResponse
import com.example.noonapp.models.Search
import com.example.noonapp.network.RequestResult
import com.example.noonapp.ui.utils.RxSearchObservable
import kotlinx.android.synthetic.main.movies_fragment.*
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
        initRV()
        initViewModel()
        initViewModelObservers()
        getMoviesResponse("marvel")
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

        val data = requestResult.data
        if (data is MoviesResponse) {
            submitList(data.search)
        }
    }

    private fun onGetMoviesResponseError(requestResult: RequestResult.Error<Any>) {

        Log.d(TAG, "onGetMoviesResponseError: $requestResult")
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


    fun submitList(searchList: List<Search>) {
        adapter.submitList(searchList)
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