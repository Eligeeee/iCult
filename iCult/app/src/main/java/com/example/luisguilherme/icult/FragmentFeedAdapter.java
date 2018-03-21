package com.example.luisguilherme.icult;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.example.luisguilherme.icult.modelo.ImageUpload;

import java.util.List;

/**
 * Created by Luis Guilherme on 16/08/2017.
 */

public class FragmentFeedAdapter extends ArrayAdapter<ImageUpload> {
    public FragmentFeedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
    }
}
