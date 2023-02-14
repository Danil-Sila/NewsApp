package com.example.newsapp.presentation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.adapter.NewsAdapter
import com.example.newsapp.data.adapter.NewsListListener
import com.example.newsapp.databinding.FragmentNewsListBinding
import com.example.newsapp.domain.models.News
import com.example.newsapp.presentation.viewmodels.GetNewsStateFromDB
import com.example.newsapp.presentation.viewmodels.LoadingNewsState
import com.example.newsapp.presentation.viewmodels.NewsViewModel
import com.example.newsapp.presentation.viewmodels.NewsViewModelFactory

class NewsListFragment : Fragment(), NewsListListener, OnQueryTextListener {

    private lateinit var vm: NewsViewModel
    private var _binding: FragmentNewsListBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: NewsAdapter
    private var newsHideMode = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        vm = ViewModelProvider(this, NewsViewModelFactory(requireActivity()))
            .get(NewsViewModel::class.java)

        binding.refreshNews.setOnRefreshListener {
            binding.refreshNews.isRefreshing = false
            vm.getNewsFromService()
        }

        vm.resultGetNewsFromDB.observe(requireActivity()) { state ->
            when(state) {
                is GetNewsStateFromDB.NewsGetState -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setNewsList(state.news)
                    setDefaultScreen()
                }
                GetNewsStateFromDB.NewsEmptyState -> {
                    binding.progressBar.visibility = View.GONE
                    binding.newsLayout.visibility = View.GONE
                    binding.emptyNewsLayout.visibility = View.VISIBLE
                }
            }
        }

        vm.resultLoadingNews.observe(requireActivity()) { state ->
            when(state) {
                LoadingNewsState.DefaultState -> {
                    binding.progressBar.visibility = View.GONE
                    setDefaultScreen()
                }
                is LoadingNewsState.ErrorState -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, state.message, Toast.LENGTH_LONG ).show()
                }
                is LoadingNewsState.NewsEmptyState -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, "Получен пустой список новостей!", Toast.LENGTH_LONG ).show()
                }
                is LoadingNewsState.NewsSucceedState -> {
                    setDefaultScreen()
                    vm.saveNews(state.news)
                    vm.getNews(modeHideNews = newsHideMode)
                }
                LoadingNewsState.SendRequestGetNews -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.newsLayout.visibility = View.GONE
                }
            }
        }

        binding.searchViewNews.setOnQueryTextListener(this)

        vm.getNews(modeHideNews = newsHideMode)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_mode -> {
                newsHideMode =  !newsHideMode
                setNewsIcon(item)
                true
            }
            else -> onOptionsItemSelected(item)
        }
    }

    private fun setNewsIcon(item: MenuItem) {
        if (newsHideMode) {
            item.setIcon(R.drawable.ic_baseline_visibility_off)
        } else {
            item.setIcon(R.drawable.ic_visibility)
        }
        vm.getNews(modeHideNews = newsHideMode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNews.layoutManager = LinearLayoutManager(context)
        adapter = NewsAdapter(this)
        binding.rvNews.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDefaultScreen() {
        binding.newsLayout.visibility = View.VISIBLE
        binding.emptyNewsLayout.visibility = View.GONE
    }

    override fun onClickNews(news: News) {
        val bundle = Bundle()
        bundle.putParcelable("news", news)
        val contentNewsFragment = ContentNewsFragment.newInstance(bundle)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.replace(R.id.container, contentNewsFragment)
            ?.commit()
    }

    override fun onDeleteNews(news: News) {
        vm.deleteNews(news) {
            Toast.makeText(context,"Новость удалена",Toast.LENGTH_SHORT).show()
            vm.getNews(modeHideNews = newsHideMode)
        }
    }

    override fun onHideNews(news: News) {
        vm.setVisibleNews(news) {
            setNewsHide(news.hide_news)
            vm.getNews(modeHideNews = newsHideMode)
        }
    }

    private fun setNewsHide(hideNews: Boolean) {
        when(hideNews) {
            true -> Toast.makeText(context,"Новость скрыта",Toast.LENGTH_SHORT).show()
            false -> Toast.makeText(context,"Новость возвращена",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
    }

}