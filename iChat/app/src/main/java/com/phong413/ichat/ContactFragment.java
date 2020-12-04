package com.phong413.ichat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messages.User;
import com.phong413.ichat.databinding.FragmentContactBinding;

import java.util.List;

public class ContactFragment extends Fragment implements ContactView {


    private FragmentContactBinding binding;
    private ContactController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = new ContactController(this);

        ListContactAdapter adapter = new ListContactAdapter();
        binding.listContact.setAdapter(adapter);
        binding.listContact.addItemDecoration(new DividerItemDecoration(requireContext(), RecyclerView.HORIZONTAL));
    }

    @Override
    public void updateListContact(List<User> listContact) {
        ListContactAdapter adapter = (ListContactAdapter) binding.listContact.getAdapter();
        adapter.updateListContact(listContact);
    }
}