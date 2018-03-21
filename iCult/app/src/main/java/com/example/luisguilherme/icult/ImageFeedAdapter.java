package com.example.luisguilherme.icult;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luisguilherme.icult.modelo.ImageUpload;

import java.util.List;

/**
 * Created by Luis Guilherme on 19/06/2017.
 */

public class ImageFeedAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;

    public ImageFeedAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.textViewTituloImagem);
        TextView tvDescricao = (TextView) v.findViewById(R.id.textViewDescricaoImagem);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);

        tvName.setText(listImage.get(position).getTitulo());
        tvDescricao.setText(listImage.get(position).getDescricao());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);

        return v;
    }
}
