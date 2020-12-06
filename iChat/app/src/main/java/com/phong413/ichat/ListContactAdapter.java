package com.phong413.ichat;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.messages.User;
import com.phong413.ichat.databinding.ItemContactBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListContactAdapter extends RecyclerView.Adapter<ListContactAdapter.ContactHolder>{

    private ArrayList<User> list = new ArrayList<User>();

    class ContactHolder extends RecyclerView.ViewHolder {

        private ItemContactBinding binding;

        public ContactHolder(@NonNull ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User u) {
            Executors.newSingleThreadExecutor().submit(() -> {
               binding.getRoot().post(() -> {
                   Glide.with(binding.img)
                           .load(LinkUtils.BASE_URL + u.getAvatarUrl())
                           .into(binding.img);
                   binding.name.setText(u.getUsername());
                   String status;
                   switch (u.getStatus()) {
                       case BUSY:
                           status = "Đang bận";
                           break;
                       case ONLINE:
                           status = "Đã trực tuyến";
                           break;
                       default:
                           status = "Đã thoát";
                           break;
                   }
                   binding.status.setText(status);
               });
            });
        }
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding = ItemContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContactHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateListContact(List<User> list) {

        if (list == null) return;

        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}
