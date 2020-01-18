package com.example.waracletechtest.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waracletechtest.R
import com.example.waracletechtest.data.Cake
import com.example.waracletechtest.databinding.MainFragmentBinding
import com.example.waracletechtest.service.cakeservice.CakesService
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class MainFragment : Fragment() {
    companion object { fun newInstance() = MainFragment() }

    private lateinit var binding: MainFragmentBinding
    private lateinit var listAdapter: CakesAdapter
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        listAdapter = CakesAdapter()

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
    }

    private fun initViewModel() {
        viewModel.cakes.observe(viewLifecycleOwner, Observer {
            listAdapter.submitList(it)
        })
        viewModel.errorState.observe(viewLifecycleOwner, Observer {
            if (it != null) Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }
}

class MainViewModel : ViewModel() {
    val errorState : LiveData<String?> get() { return _errorState }
    private val _errorState = MutableLiveData<String?>(null)

    val cakes : LiveData<List<Cake>> = liveData(Dispatchers.IO) {
        try { emit(CakesService().getCakesList()) }
        catch (e: IOException) { _errorState.value = e.message }
    }
}
