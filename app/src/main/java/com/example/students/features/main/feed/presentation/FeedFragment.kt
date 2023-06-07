package com.example.students.features.main.feed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.students.R
import com.example.students.databinding.FragmentFeedBinding
import com.example.students.features.main.feed.presentation.PostFragment.Companion.ARG_POST
import com.example.students.features.main.feed.presentation.model.FeedState
import com.example.students.utils.setSafeOnClickListener
import com.example.students.utils.ui.ToggleOnlyProgrammaticallyWrapper
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FeedFragment : Fragment() {
    private lateinit var binding: FragmentFeedBinding
    private val viewModel: FeedPageViewModel by sharedViewModel()

    private lateinit var adapter: FeedRecyclerViewAdapter

    private lateinit var filter: Filter
    private lateinit var toggleAll: ToggleOnlyProgrammaticallyWrapper
    private lateinit var toggleInfo: ToggleOnlyProgrammaticallyWrapper
    private lateinit var toggleEnt: ToggleOnlyProgrammaticallyWrapper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setElementActionsListeners()
        setupObservers()
    }

    private fun initViews() {
        binding.apply {

            toggleAll = ToggleOnlyProgrammaticallyWrapper(binding.branchTypeAll)
            toggleInfo = ToggleOnlyProgrammaticallyWrapper(binding.branchTypeInfo)
            toggleEnt = ToggleOnlyProgrammaticallyWrapper(binding.branchTypeEnt)

            filter = Filter(
                allToggle = toggleAll,
                infoToggle = toggleInfo,
                entToggle = toggleEnt,
            )

            viewModel.getPosts(0)

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.getPosts(0)
            }

            adapter = FeedRecyclerViewAdapter(
                onItemClick = {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_postFragment,
                        args = bundleOf(Pair(ARG_POST, it))
                    )
                },
                onLikeClick = { post ->
                    viewModel.likePost(post.id.toString())
                }
            )
            postsRecyclerView.adapter = adapter
            postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            createPostBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_feedFragment_to_createPostDialogFragment
                )
            }
        }
    }

    /**
     * слушатели вью элементов
     */
    private fun setElementActionsListeners() {
        setBankMapsObjectsFilterListeners()
    }

    /**
     * слушатели фитьтров
     */
    private fun setBankMapsObjectsFilterListeners() {
        toggleAll.setListener { _, _ ->
            filter.toggleAll()
            viewModel.getPosts(0)
        }
        toggleInfo.setListener { _, _ ->
            filter.toggleInfo()
            viewModel.getPosts(1)
        }
        toggleEnt.setListener { _, _ ->
            filter.toggleEnt()
            viewModel.getPosts(2)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                when (state) {
                    FeedState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Getting posts error",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.swipeRefreshLayout.isRefreshing = false

                    }
                    FeedState.Loading -> {
                        binding.emptyView.isVisible = false
                        binding.progressBar.isVisible = true
                        binding.postsRecyclerView.isVisible = false
                    }
                    is FeedState.PostsLoaded -> {
                        binding.progressBar.isVisible = false
                        binding.postsRecyclerView.isVisible = true

                        binding.swipeRefreshLayout.isRefreshing = false

                        if (state.data.isEmpty()) {
                            binding.postsRecyclerView.isVisible = false
                            binding.emptyView.isVisible = true
                        } else {
                            binding.postsRecyclerView.isVisible = true
                            binding.emptyView.isVisible = false
                            adapter.setPosts(state.data)
                        }
                    }
                }
            }
        }
    }
}

private class Filter(
    private val allToggle: ToggleOnlyProgrammaticallyWrapper,
    private val infoToggle: ToggleOnlyProgrammaticallyWrapper,
    private val entToggle: ToggleOnlyProgrammaticallyWrapper,
) {

    init {
        setChecked(allToggle, true)
        setChecked(infoToggle, false)
        setChecked(entToggle, false)
    }

    private val toggles = listOf(allToggle, infoToggle, entToggle)

    fun isAllChecked(): Boolean = isChecked(allToggle)
    fun isInfoChecked(): Boolean = isChecked(infoToggle)
    fun isEntChecked(): Boolean = isChecked(entToggle)

    fun toggleAll(isChecked: Boolean = !isAllChecked()) {
        if (isLastChecked(allToggle)) {
            setChecked(allToggle, true)
            return
        }
        setChecked(allToggle, isChecked)
        if (isChecked) {
            uncheckAllExcept(allToggle)
        }
    }

    fun toggleInfo(isChecked: Boolean = !isInfoChecked()) {
        toggle(infoToggle, isChecked)
    }

    fun toggleEnt(isChecked: Boolean = !isEntChecked()) {
        toggle(entToggle, isChecked)
    }

    private fun toggle(toggle: ToggleOnlyProgrammaticallyWrapper, isChecked: Boolean) {
        setChecked(toggle, isChecked)
        if (isChecked) {
            uncheckAllExcept(toggle)
        } else if (isLastChecked(toggle)) {
            toggleAll(true)
        }
    }

    private fun uncheckAllExcept(toggle: ToggleOnlyProgrammaticallyWrapper) {
        toggles.forEach {
            if (it.toggleButton.id != toggle.toggleButton.id) {
                setChecked(it, false)
            }
        }
    }

    private fun isLastChecked(toggle: ToggleOnlyProgrammaticallyWrapper): Boolean {
        toggles.forEach {
            if (it.toggleButton.id != toggle.toggleButton.id) {
                if (isChecked(it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun setChecked(toggle: ToggleOnlyProgrammaticallyWrapper, isChecked: Boolean) {
        toggle.toggleButton.tag = isChecked
        toggle.setCheckedWithoutListener(isChecked)
    }

    private fun isChecked(toggle: ToggleOnlyProgrammaticallyWrapper) =
        toggle.toggleButton.tag as Boolean

}