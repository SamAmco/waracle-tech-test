package com.example.waracletechtest.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.waracletechtest.R
import com.example.waracletechtest.data.Cake
import com.example.waracletechtest.databinding.MainFragmentBinding

class MainFragment : Fragment() {
    companion object { fun newInstance() = MainFragment() }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var listAdapter: CakesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
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
        viewModel.cakes.observe(this, Observer {
            listAdapter.submitList(it)
        })
    }
}

class MainViewModel : ViewModel() {
    val cakes : LiveData<List<Cake>> get() { return _cakes }
    private val _cakes = MutableLiveData<List<Cake>>(createMockCakes())

    private fun createMockCakes(): List<Cake> {
        return listOf(
            Cake("cake 1", "tasty tasty", "no url"),
            Cake("cake 2", "tasty tasty", "no url"),
            Cake("cake 3", "tasty tasty", "no url"),
            Cake("cake 4", "tasty tasty", "no url"),
            Cake("cake 5", "tasty tasty", "no url"),
            Cake("cake 6", "tasty tasty", "no url"),
            Cake("cake 7", "tasty tasty", "no url"),
            Cake("cake 8", "tasty tasty", "no url"),
            Cake("cake 9", "tasty tasty", "no url")
        )
    }
}
