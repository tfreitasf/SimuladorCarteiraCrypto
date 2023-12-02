package br.com.povengenharia.simuladorcarteiracrypto.extensions

import android.widget.ImageView
import br.com.povengenharia.simuladorcarteiracrypto.R
import coil.load


fun ImageView.TryLoadImage(url: String? = null) {
    load(url) {
        crossfade(true)
        fallback(R.drawable.imgfallback)
        error(R.drawable.errorimg)
        placeholder(R.drawable.placeholder)

    }
}