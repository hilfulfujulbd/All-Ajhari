package org.hilfulfujul.allajhari.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import org.hilfulfujul.allajhari.R
import org.hilfulfujul.allajhari.adapters.ChapterAdapter
import org.hilfulfujul.allajhari.ads.AdMobHelper
import org.hilfulfujul.allajhari.databinding.FragmentChapterBinding
import org.hilfulfujul.allajhari.setting.Response
import org.hilfulfujul.allajhari.viewmodel.BooksViewModel

class ChapterFragment : Fragment() {

    private lateinit var binding: FragmentChapterBinding
    private val args: ChapterFragmentArgs by navArgs()
    private lateinit var viewModel: BooksViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChapterAdapter

    private var writerInfo: String = ""
    private var publisherInfo: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the variable
        recyclerView = binding.allChaptersRecyclerView

        val bid = args.BID

        adapter = ChapterAdapter { chapter ->
            if (AdMobHelper.isLoaded()) {
                AdMobHelper.showInterstitialAd(requireActivity())
            }

            val navDirections = ChapterFragmentDirections.actionChapterFragmentToWebViewFragment(
                title = chapter.title, url = chapter.url
            )
            findNavController().navigate(navDirections)
        }

        recyclerView.adapter = adapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[BooksViewModel::class.java]

        viewModel.getChapterData(bid).observe(viewLifecycleOwner) { response ->
            binding.progressCircular.visibility = View.GONE
            when (response) {
                is Response.Error -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                }

                Response.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }

                is Response.Success -> {
                    adapter.submitList(response.data.chapter)
                    publisherInfo = response.data.information[0].url
                    writerInfo = response.data.information[1].url
                    setHasOptionsMenu(true)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chapter_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.publisher_info -> {
                val navDirections =
                    ChapterFragmentDirections.actionChapterFragmentToWebViewFragment(
                        title = "প্রকাশনীর কথা", url = publisherInfo
                    )
                findNavController().navigate(navDirections)
            }

            R.id.writer_info -> {
                val navDirections =
                    ChapterFragmentDirections.actionChapterFragmentToWebViewFragment(
                        title = "লেখকের কথা", url = writerInfo
                    )
                findNavController().navigate(navDirections)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}