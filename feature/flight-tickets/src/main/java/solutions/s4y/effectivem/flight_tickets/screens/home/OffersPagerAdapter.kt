import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectm.domain.models.ImageValue
import solutions.s4y.effectm.domain.models.Offer

class OffersPagerAdapter(private var _offers: Array<Offer>) : PagerAdapter() {

    var offers: Array<Offer>
        get() = _offers
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            _offers = value
            Log.d(TAG, "setOffers ${_offers.size}")
            notifyDataSetChanged()
        }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.d(TAG, "instantiateItem $position of ${_offers.size}")
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_offer, container, false)
        val holder = OfferViewHolder(view)
        if (position >= _offers.size) {
            loadPlaceHolder(holder)
        } else {
            val offer = _offers[position]
            loadOffer(holder, offer)
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        Log.d(TAG, "getCount ${_offers.size}")
        return _offers.size.takeIf { it > 0 } ?: 5
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean =
        view == `object`

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
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
        loadImageValueToImageView(offer.image, holder.offerImage)
    }

    inner class OfferViewHolder(view: View) {
        val offerImage: ImageView = view.findViewById(R.id.image)
        val loading: ProgressBar = view.findViewById(R.id.loading)
        val town: TextView = view.findViewById(R.id.town)
        val price: TextView = view.findViewById(R.id.price)
    }

    companion object {
        val TAG = OffersPagerAdapter::class.java.simpleName
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