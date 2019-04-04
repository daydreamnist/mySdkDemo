package com.broadlink.mysdkdemo.commonUtils;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;

public class ImageLoadTask extends AsyncTask<Void,Void, Bitmap> {
    private String image_url;
    private int default_image;
    private int error_image;
    private loadCallback callback;
    private int width,height;
    private Exception ioException;
    public ImageLoadTask(String image_url, int default_image, int error_image, int width, int height, loadCallback callback) {
        this.image_url = image_url;
        this.default_image = default_image;
        this.error_image = error_image;
        this.callback = callback;
        this.width = width;
        this.height = height;
    }

    public ImageLoadTask(String image_url,loadCallback callback) {
        this.callback = callback;
        this.image_url = image_url;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Picasso picasso = Picasso.get();
        RequestCreator requestCreator = picasso.load(image_url);
        try {
            return requestCreator.get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        callback.onSuccess(bitmap);

    }
}