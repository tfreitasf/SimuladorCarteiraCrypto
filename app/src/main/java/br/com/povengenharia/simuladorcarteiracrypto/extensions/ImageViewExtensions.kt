package br.com.povengenharia.simuladorcarteiracrypto.extensions

import android.widget.ImageView
import br.com.povengenharia.simuladorcarteiracrypto.R
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest


fun ImageView.TryLoadImage(url: String? = null) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    val request = ImageRequest.Builder(context)
        .data(url)
        .crossfade(true)
        .fallback(R.drawable.imgfallback)
        .error(R.drawable.errorimg)
        .placeholder(R.drawable.placeholder)
        .target(this)
        .build()
    imageLoader.enqueue(request)

}