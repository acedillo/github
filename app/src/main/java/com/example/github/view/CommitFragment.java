package com.example.github.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.github.R;
import com.example.github.model.GithubResponse;
import com.example.github.viewmodel.GithubViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommitFragment extends Fragment {

    private RecyclerView commitList;
    private EditText owner;
    private EditText repo;
    private TextView loadingView;

    private GithubViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this, GithubViewModel.getFactory())
                .get(GithubViewModel.class);

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (Boolean.TRUE.equals(isLoading)) {
                showLoadingText();
            } else {
                hideLoadingText();
            }
        });

        viewModel.getResponse().observe(getViewLifecycleOwner(),
                githubResponses -> updateCommitList(githubResponses));

        return inflater.inflate(R.layout.fragment_commit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        commitList = view.findViewById(R.id.fragment_commit_list);
        Button searchButton = view.findViewById(R.id.fragment_commit_button);
        owner = view.findViewById(R.id.fragment_commit_owner);
        repo = view.findViewById(R.id.fragment_commit_repo);
        loadingView = view.findViewById(R.id.fragment_commit_loading);

        commitList.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));

        commitList.setAdapter(new CommitAdapter(new ArrayList<>()));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        commitList.addItemDecoration(decoration);

        searchButton.setOnClickListener(v -> viewModel.loadCommitList(
                owner.getText().toString(), repo.getText().toString()));

        super.onViewCreated(view, savedInstanceState);
    }

    private void showLoadingText() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingText() {
        loadingView.setVisibility(View.GONE);
    }

    private void updateCommitList(List<GithubResponse> list) {
        if (commitList.getAdapter() != null) {
            ((CommitAdapter) commitList.getAdapter()).update(list);
        }
    }
}

