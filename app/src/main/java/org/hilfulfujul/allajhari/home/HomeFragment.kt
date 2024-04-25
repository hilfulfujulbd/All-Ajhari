package org.hilfulfujul.allajhari.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.hilfulfujul.allajhari.adapters.BooksAdapter
import org.hilfulfujul.allajhari.databinding.FragmentHomeBinding
import org.hilfulfujul.allajhari.setting.Response
import org.hilfulfujul.allajhari.setting.SharedPreferencesAds
import org.hilfulfujul.allajhari.viewmodel.BooksViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: BooksViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BooksAdapter
    private lateinit var sharedPref: SharedPreferencesAds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        sharedPref = SharedPreferencesAds(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the variable
        recyclerView = binding.allBooksRecyclerView

        adapter = BooksAdapter { book ->
            val navDirections = HomeFragmentDirections.actionHomeFragmentToChapterFragment(book.bid)
            findNavController().navigate(navDirections)
        }
        recyclerView.adapter = adapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[BooksViewModel::class.java]

        viewModel.bookData.observe(viewLifecycleOwner) { response -> // Observe LiveData
            binding.progressCircular.visibility = View.GONE
            when (response) {
                is Response.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    adapter.submitList(response.data)
                }

                is Response.Error -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//                if(!showAds){
//                    adMobHelper.loadBannerAd(binding.bannerAdsLayout)
//                }
//
//
//            }
//        }
        // Coroutine for safer lifecycle management


    }
}