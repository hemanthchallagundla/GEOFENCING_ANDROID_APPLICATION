package com.example.login.ui.updateg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.login.databinding.FragmentUpdategBinding;

public class UpdategFragment extends Fragment {

private FragmentUpdategBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        UpdateViewModel slideshowViewModel =
                new ViewModelProvider(this).get(UpdateViewModel.class);

    binding = FragmentUpdategBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textUpdateg;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}