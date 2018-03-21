package com.example.luisguilherme.icult;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.luisguilherme.icult.modelo.ImageUploadLoc;

import java.util.List;

/**
 * Created by Luis Guilherme on 11/09/2017.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter {

    private List<ImageUploadLoc> imagens;

    public ImageRecyclerAdapter(List<ImageUploadLoc> imagens){
        this.imagens = imagens;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imagens.size();
    }
}
