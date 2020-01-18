package com.example.waracletechtest.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waracletechtest.R
import com.example.waracletechtest.data.Cake
import com.example.waracletechtest.databinding.MainFragmentBinding
import com.example.waracletechtest.service.cakeservice.CakesService
import kotlinx.coroutines.*
import java.io.IOException

class MainFragment : Fragment() {
    companion object { fun newInstance() = MainFragment() }

    private lateinit var binding: MainFragmentBinding
    private lateinit var listAdapter: CakesAdapter
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        listAdapter = CakesAdapter(context!!)

        initViewModel()
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        val viewManager = LinearLayoutManager(context)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = listAdapter
            addItemDecoration(divider)
        }
        binding.swipeToRefresh.setOnRefreshListener { viewModel.refresh() }
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            binding.swipeToRefresh.isRefreshing = it
        })
    }

    private fun initViewModel() {
        viewModel.cakes.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })
        viewModel.errorState.observe(viewLifecycleOwner, Observer {
            //TODO this is a pretty ugly way of communicating an error to the user.
            // Should probably use an AlertDialog with a pre-constructed message
            // and keep the gory details to your self.
            if (it != null) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }
}

class MainViewModel : ViewModel() {
    private var updateJob = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + updateJob)

    val isLoading : LiveData<Boolean> get() { return _isLoading }
    private val _isLoading = MutableLiveData<Boolean>(false)

    val errorState : LiveData<String?> get() { return _errorState }
    private val _errorState = MutableLiveData<String?>(null)

    val cakes : LiveData<List<Cake>> get() { return _cakes }
    private val _cakes = MutableLiveData<List<Cake>>(listOf())

    private val cakesService = CakesService()

    init { refresh() }

    fun refresh() = ioScope.launch {
        withContext(Dispatchers.Main) { _isLoading.value = true }
        val cakesList = getCakes()
        withContext(Dispatchers.Main) {
            _cakes.value = cakesList
            _isLoading.value = false
        }
    }

    private suspend fun getCakes(): List<Cake> {
        try { return cakesService.getCakesList() }
        catch (e: IOException) {
            withContext(Dispatchers.Main) { _errorState.value = e.message }
        }
        return listOf()
    }

    override fun onCleared() {
        super.onCleared()
        ioScope.cancel()
    }
}
