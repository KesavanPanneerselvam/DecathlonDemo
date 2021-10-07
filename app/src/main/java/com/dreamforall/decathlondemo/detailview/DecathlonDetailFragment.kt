package com.dreamforall.decathlondemo.detailview

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.dreamforall.decathlondemo.R
import com.dreamforall.decathlondemo.adapter.DataSportList
import com.dreamforall.decathlondemo.databinding.DecathlonDetailFragmentBinding
import com.dreamforall.decathlondemo.detailview.model.DecathlonDetailViewModel
import com.google.android.material.chip.Chip


class DecathlonDetailFragment : Fragment() {

    companion object {
        var decathlonDetailData: DataSportList = DataSportList()
        fun newInstance() = DecathlonDetailFragment()
    }

    private lateinit var viewModel: DecathlonDetailViewModel


    private var _binding: DecathlonDetailFragmentBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DecathlonDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DecathlonDetailViewModel::class.java)
        // TODO: Use the ViewModel

        binding.txtTitleName.text = "Sports Type"
        binding.txtTitleNameValue.text = decathlonDetailData.attributes.name
        binding.txtTitleDesc.text = "Description"
        binding.txtTitleDescValue.text = decathlonDetailData.attributes.description

        setCategoryChips(decathlonDetailData.relationships.tags.data)

        if (decathlonDetailData.relationships.images.data.isNotEmpty()) {
            Glide
                .with(requireContext())
                .load(decathlonDetailData.relationships.images.data[0].url)
                .centerCrop()
                .placeholder(R.drawable.icbroken_image_black)
                .into(binding.imgDetailedView)
            binding.imgDetailedView.loadUrl(decathlonDetailData.relationships.images.data[0].url)
        }
    }


    fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .placeholder(R.drawable.icbroken_image_black)
            .error(R.drawable.icbroken_image_black)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    fun setCategoryChips(categorys: List<String?>) {
        if (categorys.isNotEmpty()) {
            binding.cardMaterialChips.visibility = View.VISIBLE
            for (category in categorys) {
                val mChip =
                    this.layoutInflater.inflate(R.layout.item_chips, null, false) as Chip
                mChip.text = category
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                mChip.setOnCheckedChangeListener { compoundButton, b -> }
                binding.chipsPrograms.addView(mChip)
            }
        } else {
            binding.cardMaterialChips.visibility = View.GONE
        }
    }
}