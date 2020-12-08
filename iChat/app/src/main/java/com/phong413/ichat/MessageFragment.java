package com.phong413.ichat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.phong413.ichat.databinding.FragmentMessageBinding;

import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        ListMessageAdapter adapter = new ListMessageAdapter(requireContext());
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

//        binding.avatar.setImageURI(Uri.parse("http://" + ChatThread.getInstance().getHost() + ":8080/ichat" + ChatThread.getInstance().getAccount().getAvatarUrl()));

        Glide.with(this)
                .load("http://" + ChatThread.getInstance().getHost() + ":8080/ichat" + ChatThread.getInstance().getAccount().getAvatarUrl())
                .into(binding.avatar);
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
                    Executors.newSingleThreadExecutor().submit(() -> {
                        try {
                            OkHttpClient client = new OkHttpClient.Builder()
                                    .build();

                            InputStreamRequestBody avatarBody = new InputStreamRequestBody(
                                    MediaType.parse("image"),
                                    requireContext().getContentResolver(),
                                    imageUri
                            );

                            RequestBody body = new  MultipartBody.Builder()
                                    .addFormDataPart("image", "IMG_" + System.currentTimeMillis() + ".jpeg", avatarBody)
                                    .build();

                            Request req = new Request.Builder()
                                    .url("http://" + "192.168.43.173" + ":8080/ichat/upload")
                                    .post(body)
                                    .build();
                            Response resp = client.newCall(req).execute();
                            int code = resp.code();
                            if (code == 200) {
                                String link = resp.body().string();
                                ChatThread.getInstance().sendMessage(
                                        new MessageBuilder()
                                                .setStatus(Status.ONLINE)
                                                .setMessageType(MessageType.IMAGE)
                                                .setImageUrl(link)
                                                .build()
                                );
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}