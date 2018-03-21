package com.example.luisguilherme.icult;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Luis Guilherme on 09/08/2017.
 */

public class ImageFragmentAdapter extends ArrayAdapter {
    public ImageFragmentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }
}
