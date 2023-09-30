package com.picpay.desafio.android.presentation.users.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.picpay.desafio.android.databinding.FragmentListUsersBinding
import com.picpay.desafio.android.presentation.users.adapter.UserListAdapter
import com.picpay.desafio.android.presentation.users.viewModel.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListUsersFragment : Fragment() {

    private val viewModel by viewModel<UsersViewModel>()
    private val userAdapter by lazy { UserListAdapter() }
    private lateinit var binding: FragmentListUsersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.recyclerView.adapter = userAdapter
    }

    private fun setupObservers() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            binding.userListProgressBar.isVisible = state.isLoading
            userAdapter.updateList(state.usersModelList)
            binding.contentError.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), state.iconError))
            binding.contentError.root.isVisible = state.showError
            binding.contentError.titleError.text = state.titleError
            binding.contentError.descriptionError.text = state.descriptionError
            binding.tvOffline.isVisible = state.showOffline
        }

        viewModel.eventLiveData.observe(viewLifecycleOwner) {

        }
    }
}