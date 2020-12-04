package com.phong413.ichat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messages.Message;
import com.messages.MessageType;
import com.phong413.ichat.databinding.FragmentMessageBinding;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

public class MessageFragment extends Fragment implements MessageView {

    private static final int PICK_IMAGE = 10;
    private FragmentMessageBinding binding;
    private MessageController messageController;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageController = new MessageController(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListMessageAdapter adapter = new ListMessageAdapter();
        binding.listMessage.setAdapter(adapter);

        binding.btnSend.setOnClickListener(v -> {
            String messStr = binding.inputMess.getText().toString();

            if (messStr == null || messStr.isEmpty()) return;

            Message mess = new MessageBuilder()
                    .setMessageType(MessageType.USER)
                    .setMessage(messStr)
                    .build();

            messageController.sendMessage(mess);
        });

        binding.btnSendImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Chọn ảnh đại diện"), PICK_IMAGE);
        });

        adapter.updateListMessage(ChatThread.getInstance().getListMessage());
    }

    @Override
    public void updateMessage(List<Message> listMessage) {
        binding.getRoot().post(() -> {
            ((ListMessageAdapter) binding.listMessage.getAdapter()).updateListMessage(listMessage);
            SystemClock.sleep(100);
            binding.listMessage.scrollToPosition(((ListMessageAdapter) binding.listMessage.getAdapter()).getItemCount() - 1);
            binding.inputMess.setText("");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                        Executors.newSingleThreadExecutor().submit(() -> {
                            Bitmap resizeBitmap = BitmapUtils.resizeBitmap(bitmap, bitmap.getWidth() / 20, bitmap.getWidth() / 20);
                            byte[] img = BitmapUtils.bitmapToByte(resizeBitmap);
                            Message mess = new MessageBuilder()
                                    .setMessageType(MessageType.IMAGE)
                                    .setImageMessage(img)
                                    .build();
                            messageController.sendMessage(mess);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}