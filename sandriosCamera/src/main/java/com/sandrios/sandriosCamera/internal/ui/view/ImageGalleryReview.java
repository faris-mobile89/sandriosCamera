package com.sandrios.sandriosCamera.internal.ui.view;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.manager.CameraOutputModel;
import com.sandrios.sandriosCamera.internal.ui.BaseSandriosActivity;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryReview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageGalleryAdapter imageGalleryAdapter;
    private List<CameraOutputModel> cameraOutputModelList;

    private ImageView currentImageView;

    private ImageView imageViewDelete, ImageViewCancel, ImageViewConfirm;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_review);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        currentImageView = findViewById(R.id.imageView);

        imageViewDelete = findViewById(R.id.imageViewDelete);
        ImageViewCancel = findViewById(R.id.ImageViewCancel);
        ImageViewConfirm = findViewById(R.id.ImageViewConfirm);


        imageViewDelete.setOnClickListener(onClickListener);
        ImageViewCancel.setOnClickListener(onClickListener);
        ImageViewConfirm.setOnClickListener(onClickListener);

        Bundle extra = getIntent().getBundleExtra("extra");
        cameraOutputModelList = (ArrayList<CameraOutputModel>)extra.getSerializable("objects");
        imageGalleryAdapter = new ImageGalleryAdapter(this, cameraOutputModelList);
        recyclerView.setAdapter(imageGalleryAdapter);

        imageGalleryAdapter.setOnItemClickListener(new ImageGalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              bindImageWithThumb(position);
            }
        });

        bindImageWithThumb(0);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == imageViewDelete){
                cameraOutputModelList.remove(currentIndex);
                imageGalleryAdapter.notifyDataSetChanged();
            }else if (view == ImageViewCancel){
                finish();
            }else if (view == ImageViewConfirm){

            }
        }
    };

    private void bindImageWithThumb(int indexOfThumb){
        currentIndex = indexOfThumb;
        ImageGalleryAdapter.PickerTile pickerTile = imageGalleryAdapter.getItem(indexOfThumb);
        Uri uri = pickerTile.getImageUri();
        if (uri != null) {
            int type = BaseSandriosActivity.getMimeType(ImageGalleryReview.this, uri.toString());
            //if (type == SandriosCamera.MediaType.PHOTO)
            Glide.with(ImageGalleryReview.this)
                    .load(uri)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(ImageGalleryReview.this, R.drawable.ic_gallery))
                    .error(ContextCompat.getDrawable(ImageGalleryReview.this, R.drawable.ic_error))
                    .into(currentImageView);
        }
    }
}
