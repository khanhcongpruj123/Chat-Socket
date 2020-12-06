package com.phong413.ichat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.messages.Message;
import com.phong413.ichat.databinding.ItemMessLeftBinding;
import com.phong413.ichat.databinding.ItemMessRightBinding;
import com.phong413.ichat.databinding.ItemMessageServerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.MessageHolder> {

    private static final int TYPE_USER = 1;
    private static final int TYPE_OTHER = 2;
    private static final int TYPE_SERVER = 3;


    private ArrayList<Message> listMessage = new ArrayList<>();

    private Context context;

    public ListMessageAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            ItemMessRightBinding binding = ItemMessRightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MessageRightHolder(binding);
        } if (viewType == TYPE_SERVER) {
            ItemMessageServerBinding binding = ItemMessageServerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MessageServerHolder(binding);
        } else {
            ItemMessLeftBinding binding = ItemMessLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MessageLeftHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.bind(listMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    public void updateListMessage(List<Message> list) {
        this.listMessage.clear();
        this.listMessage.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (listMessage.get(position).getUser() == null) {
            return TYPE_SERVER;
        }
        if (listMessage.get(position).getUser().getUsername().equals(ChatThread.getInstance().getUser().getUsername())) {
            return TYPE_USER;
        } else {
            return TYPE_OTHER;
        }
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Message m) {

        }
    }

    class MessageLeftHolder extends MessageHolder {
        private ItemMessLeftBinding binding;

        public MessageLeftHolder(@NonNull ItemMessLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message m) {
            binding.message.setText(m.getMessage());
            binding.getRoot().post(() -> {
                if (m.getUser() != null) {
                    Glide.with(binding.avatar)
                            .load(LinkUtils.BASE_URL + m.getUser().getAvatarUrl())
                            .into(binding.avatar);
                }

                String imgUrl = m.getImageUrl();
                if (imgUrl != null) {
                    Glide.with(binding.image)
                            .load(LinkUtils.BASE_URL + imgUrl)
                            .into(binding.image);
                    binding.image.setVisibility(View.VISIBLE);
                } else {
                    binding.image.setImageBitmap(null);
                    binding.image.setVisibility(View.GONE);
                }

                binding.image.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    intent.setData(Uri.parse(LinkUtils.BASE_URL + imgUrl));
                    context.startActivity(intent);
                });
            });
        }
    }

    class MessageServerHolder extends MessageHolder {
        private ItemMessageServerBinding binding;

        public MessageServerHolder(@NonNull ItemMessageServerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message m) {
            binding.message.setText(m.getMessage());
        }
    }

    class MessageRightHolder extends MessageHolder {
        private ItemMessRightBinding binding;

        public MessageRightHolder(@NonNull ItemMessRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message m) {
            binding.message.setText(m.getMessage());
            binding.getRoot().post(() -> {
                if (m.getUser() != null) {
                    Glide.with(binding.avatar)
                            .load(LinkUtils.BASE_URL + m.getUser().getAvatarUrl())
                            .into(binding.avatar);
                }

                String imgUrl = m.getImageUrl();
                if (imgUrl != null) {
                    Glide.with(binding.image)
                            .load(LinkUtils.BASE_URL + imgUrl)
                            .into(binding.image);
                    binding.image.setVisibility(View.VISIBLE);
                } else {
                    binding.image.setImageBitmap(null);
                    binding.image.setVisibility(View.GONE);
                }

                binding.image.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    intent.setData(Uri.parse(LinkUtils.BASE_URL + imgUrl));
                    context.startActivity(intent);
                });
            });
        }
    }
}
