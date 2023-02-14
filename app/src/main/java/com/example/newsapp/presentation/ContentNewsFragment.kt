package com.example.newsapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.FragmentContentNewsBinding
import com.example.newsapp.domain.models.News

class ContentNewsFragment : Fragment() {

    private var news: News? = null
    private var _binding: FragmentContentNewsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(args: Bundle?): ContentNewsFragment {
            val fragment = ContentNewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentNewsBinding.inflate(inflater, container,false)
        if (arguments == null) {
           return binding.root
        }
        getNews()
        setContent()
        return binding.root
    }

    private fun getNews() {
        news = arguments?.getParcelable("news")
    }

    private fun setContent() {
        Glide.with(requireContext()).load(news?.img).into(binding.imageNews)
        binding.webView.loadUrl(news?.mobile_url.toString())
    }

}