package com.phong413.ichat;

import android.content.ContentResolver;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class InputStreamRequestBody extends RequestBody {

    private MediaType mediaType;
    private ContentResolver contentResolver;
    private Uri uri;

    public InputStreamRequestBody(MediaType mediaType, ContentResolver contentResolver, Uri uri) {
        this.mediaType = mediaType;
        this.contentResolver = contentResolver;
        this.uri = uri;
    }


    @Nullable
    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        InputStream in = contentResolver.openInputStream(uri);
        byte[] data = new byte[in.available()];
        in.read(data);
        bufferedSink.write(data);
    }
}
