package solutions.s4y.effectivem.flight_tickets.screens.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectm.domain.models.ImageValue
import solutions.s4y.effectm.domain.models.Offer

class OffersPager2Adapter(private var _offers: Array<Offer>) :
    RecyclerView.Adapter<OffersPager2Adapter.OfferViewHolder>() {

    var offers: Array<Offer>
        get() = _offers
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            _offers = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
        return OfferViewHolder(view)
    }

    override fun getItemCount(): Int = _offers.size.takeIf { it > 0 } ?: 5

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position")
        if (position >= _offers.size) {
            loadPlaceHolder(holder)
        } else {
            val offer = _offers[position]
            loadOffer(holder, offer)
        }
    }

    private fun loadPlaceHolder(holder: OfferViewHolder) {
        holder.offerImage.visibility = View.GONE
        holder.loading.visibility = View.VISIBLE
        Log.d(TAG, "loadPlaceHolder")
    }

    private fun loadOffer(holder: OfferViewHolder, offer: Offer) {
        holder.offerImage.visibility = View.VISIBLE
        holder.loading.visibility = View.GONE
        loadImageValueToImageView(offer.image, holder.offerImage)
        Log.d(TAG, "loadOffer")
    }

    inner class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val offerImage: ImageView = view.findViewById(R.id.image)
        val loading: ProgressBar = view.findViewById(R.id.loading)
        val town: TextView = view.findViewById(R.id.town)
        val price: TextView = view.findViewById(R.id.price)
    }

    companion object {
        private const val TAG = "OffersPagerAdapter"

        // TODO: placeholder
        // should be implemented with Glide
        // should work with loading state
        fun loadImageValueToImageView(imageValue: ImageValue, imageView: ImageView) {
            when (imageValue) {
                is ImageValue.NoImage -> imageView.visibility = View.GONE
                is ImageValue.ImageValueById -> {
                    val resourceId = when (imageValue.id) {
                        1 -> R.drawable.offer_1
                        2 -> R.drawable.offer_2
                        3 -> R.drawable.offer_3
                        else -> 0
                    }
                    if (resourceId == 0) {
                        imageView.visibility = View.GONE
                    } else {
                        imageView.visibility = View.VISIBLE
                        imageView.setImageResource(resourceId)
                    }
                }
            }
        }
    }
}