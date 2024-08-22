package com.sherrif.sneakerhub.glidephoto

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sherrif.sneakerhub.R

class GlideLoader {
     companion object {
          fun loadImage(url: String, imageView: ImageView) {
               Glide.with(imageView.context)
                    .load(url)
                    .apply(
                         RequestOptions()
                              .placeholder(R.drawable.shoe)
                              .error(R.drawable.error_image)
                              .override(100, 100)
                    )
                    .into(imageView)
          }
     }
}

