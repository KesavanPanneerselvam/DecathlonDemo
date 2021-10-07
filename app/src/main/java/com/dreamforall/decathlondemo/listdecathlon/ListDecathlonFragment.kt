package com.dreamforall.decathlondemo.listdecathlon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreamforall.decathlondemo.adapter.DataSportList
import com.dreamforall.decathlondemo.adapter.DecathlonAdapter
import com.dreamforall.decathlondemo.adapter.DecathlonResponseModel
import com.dreamforall.decathlondemo.adapter.ItemClickListener
import com.dreamforall.decathlondemo.databinding.FragmentFirstBinding
import com.dreamforall.decathlondemo.detailview.DecathlonDetailFragment
import com.dreamforall.decathlondemo.detailview.DecathlonDetailFragment.Companion.decathlonDetailData
import com.dreamforall.decathlondemo.listdecathlon.model.ListDecathlonViewModel


class ListDecathlonFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ListDecathlonViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListDecathlonViewModel::class.java)

        initObservable()
    }

    private fun initObservable() {
        viewModel.decathlonSportsInfo.observe(viewLifecycleOwner, ObservableDecathlonSportsInfo)
        viewModel.decathlonSportsInfoLoading.observe(
                viewLifecycleOwner,
                ObservableDecathlonSportsInfoLoading
        )

    }

    private val ObservableDecathlonSportsInfoLoading = Observer<Boolean> {
        binding.progressLoading.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        binding.progressLoading.visibility = View.GONE
    }

    private val ObservableDecathlonSportsInfo = Observer<DecathlonResponseModel> {
        binding.progressLoading.visibility = View.GONE
        if (it.data.isNotEmpty()) {
            binding.recyclerDecathlonList.layoutManager = LinearLayoutManager(context)
            binding.recyclerDecathlonList.addItemDecoration(DividerItemDecoration(binding.recyclerDecathlonList.getContext(), DividerItemDecoration.VERTICAL))
            binding.recyclerDecathlonList.adapter = DecathlonAdapter(
                    it.data,
                    requireContext(),
                    this
            )
        } else {
            binding.txtErr.text = "No Data Found"
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance() = DecathlonDetailFragment()
    }

    override fun OnItemClicked(data: DataSportList) {
        decathlonDetailData = data
        val action = ListDecathlonFragmentDirections.actionFirstFragmentToDecathlonDetailFragment()
        findNavController().navigate(action)
    }

}