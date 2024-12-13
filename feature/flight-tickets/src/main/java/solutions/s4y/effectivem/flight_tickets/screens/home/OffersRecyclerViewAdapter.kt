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
import solutions.s4y.effectm.domain.models.formatted


class OffersRecyclerViewAdapter(private var _offers: Array<Offer>) :
    RecyclerView.Adapter<OffersRecyclerViewAdapter.OfferViewHolder>() {

    var offers: Array<Offer>
        get() = _offers
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            Log.d(TAG, "setOffers ${value.size} to ${_offers.size}")
            _offers = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_offer, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position of ${_offers.size}")
        if (position >= _offers.size) {
            loadPlaceHolder(holder)
        } else {
            val offer = _offers[position]
            loadOffer(holder, offer)
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount ${_offers.size}")
        return _offers.size.takeIf { it > 0 } ?: 5
    }

    private fun loadPlaceHolder(holder: OfferViewHolder) {
        Log.d(TAG, "loadPlaceHolder")
        holder.offerImage.visibility = View.GONE
        holder.loading.visibility = View.VISIBLE
    }

    private fun loadOffer(holder: OfferViewHolder, offer: Offer) {
        Log.d(TAG, "loadOffer")
        holder.offerImage.visibility = View.VISIBLE
        holder.loading.visibility = View.GONE
        holder.title.text = offer.title
        holder.town.text = offer.town ?: ""
        holder.price.text = "от ${offer.price.formatted}"
        loadImageValueToImageView(offer.image, holder.offerImage)
    }

    class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val offerImage: ImageView = view.findViewById(R.id.image)
        val loading: ProgressBar = view.findViewById(R.id.loading)
        val title: TextView = view.findViewById(R.id.title)
        val town: TextView = view.findViewById(R.id.town)
        val price: TextView = view.findViewById(R.id.badge)
    }

    companion object {
        private const val TAG = "OffersRecyclerViewAdapter"

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