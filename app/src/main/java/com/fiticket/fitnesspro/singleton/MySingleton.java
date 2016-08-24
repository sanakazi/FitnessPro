package com.fiticket.fitnesspro.singleton;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private String[] classTypes;
    /*private int[] classTypeImages={
            R.drawable.ic_cardio,
            R.drawable.ic_combat,
            R.drawable.ic_dance,
            R.drawable.ic_pilates,
            R.drawable.ic_spin,
            R.drawable.ic_surf,
            R.drawable.ic_swim,
            R.drawable.ic_tone,
            R.drawable.ic_yoga,
            R.drawable.ic_zumba,
            R.drawable.ic_gym,
            R.drawable.ic_crossfit
    };
    private int[] classTypeRectImages={
            R.drawable.ic_rect_cardi0,
            R.drawable.ic_rect_combat,
            R.drawable.ic_rect_dance,
            R.drawable.ic_rect_pilates,
            R.drawable.ic_rect_spin,
            R.drawable.ic_rect_surf,
            R.drawable.ic_rect_swim,
            R.drawable.ic_rect_tone,
            R.drawable.ic_rect_yoga,
            R.drawable.ic_rect_zumba,
            R.drawable.ic_rect_gym,
            R.drawable.ic_rect_crossfit
    };
*/
    private MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
        //classTypes= context.getResources().getStringArray(R.array.categoryArray);
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public String[] getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(String[] classTypes) {
        this.classTypes = classTypes;
    }

    /*public int[] getClassTypeImages() {
        return classTypeImages;
    }

    public void setClassTypeImages(int[] classTypeImages) {
        this.classTypeImages = classTypeImages;
    }

    public int[] getClassTypeRectImages() {
        return classTypeRectImages;
    }

    public void setClassTypeRectImages(int[] classTypeRectImages) {
        this.classTypeRectImages = classTypeRectImages;
    }*/
}