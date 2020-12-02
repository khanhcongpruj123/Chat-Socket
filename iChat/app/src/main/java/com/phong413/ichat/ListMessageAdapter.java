package com.phong413.ichat;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.messages.Message;
import com.phong413.ichat.databinding.ItemMessLeftBinding;
import com.phong413.ichat.databinding.ItemMessRightBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.MessageHolder> {

    private static final int TYPE_USER = 1;
    private static final int TYPE_OTHER = 2;

    private ArrayList<Message> listMessage = new ArrayList<>();

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_USER) {
            ItemMessRightBinding binding = ItemMessRightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MessageRightHolder(binding);
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
        if (listMessage.get(position).getName().equals(ChatThread.getInstance().getUsername())) {
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
            binding.message.setText(m.getMsg());
            Executors.newSingleThreadExecutor().submit(() -> {
                Bitmap img = BitmapUtils.bitMapFromByte(m.getPicture());
                binding.getRoot().post(() -> {
                    binding.avatar.setImageBitmap(img);
                });

                byte[] imgMsg = m.getImgMsg();
                if (imgMsg != null) {
                    Bitmap b = BitmapUtils.bitMapFromByte(imgMsg);
                    binding.image.setImageBitmap(b);
                } else {
                    binding.image.setImageBitmap(null);
                }
            });
        }
    }

    class MessageRightHolder extends MessageHolder {
        private ItemMessRightBinding binding;

        public MessageRightHolder(@NonNull ItemMessRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message m) {
            binding.message.setText(m.getMsg());
            Executors.newSingleThreadExecutor().submit(() -> {
                Bitmap img = BitmapUtils.bitMapFromByte(m.getPicture());
                binding.getRoot().post(() -> {
                    binding.avatar.setImageBitmap(img);
                });
                byte[] imgMsg = m.getImgMsg();
                if (imgMsg != null) {
                    Bitmap b = BitmapUtils.bitMapFromByte(imgMsg);
                    binding.image.setImageBitmap(b);
                } else {
                    binding.image.setImageBitmap(null);
                }
            });
        }
    }
}
