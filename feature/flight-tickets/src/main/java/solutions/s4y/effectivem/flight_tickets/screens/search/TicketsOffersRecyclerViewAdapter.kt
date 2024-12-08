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
import solutions.s4y.effectm.domain.models.TicketOffer
import solutions.s4y.effectm.domain.models.formatted

class TicketsOffersRecyclerViewAdapter(private var _offers: Array<TicketOffer>) : RecyclerView.Adapter<TicketsOffersRecyclerViewAdapter.OfferViewHolder>() {

    var offers: Array<TicketOffer>
        get() = _offers
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            Log.d(TAG, "setOffers ${value.size} to ${_offers.size}")
            _offers = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticker_offer, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder $position of ${_offers.size}")
        if (position >= _offers.size) {
            loadPlaceHolder(holder)
        } else {
            val offer = _offers[position]
            loadOffer(position, holder, offer)
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount ${_offers.size}")
        return _offers.size.takeIf { it > 0 } ?: 3
    }

    private fun loadPlaceHolder(holder: OfferViewHolder) {
        Log.d(TAG, "loadPlaceHolder")
        holder.marquee.visibility = View.GONE
        holder.airline.visibility = View.GONE
        holder.price.visibility = View.GONE
        holder.dates.visibility = View.GONE
        holder.loading.visibility = View.VISIBLE
    }

    private fun loadOffer(position: Int,holder: OfferViewHolder, offer: TicketOffer) {
        Log.d(TAG, "loadOffer")
        holder.loading.visibility = View.GONE
        holder.marquee.visibility = View.VISIBLE
        holder.airline.visibility = View.VISIBLE
        holder.price.visibility = View.VISIBLE
        holder.dates.visibility = View.VISIBLE
        holder.marquee.setBackgroundColor(when (position) {
            1 -> holder.itemView.resources.getColor(solutions.s4y.effectivem.views.R.color.red)
            2 -> holder.itemView.resources.getColor(solutions.s4y.effectivem.views.R.color.blue)
            else -> holder.itemView.resources.getColor(solutions.s4y.effectivem.views.R.color.white)
        })
        holder.price.text = offer.price.formatted
        holder.dates.text = offer.timeRange.joinToString(separator = " ")
        holder.airline.text = offer.title
    }

    inner class OfferViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val marquee: View = view.findViewById(R.id.marquee)
        val airline: TextView = view.findViewById(R.id.airline)
        val price: TextView = view.findViewById(R.id.price)
        val dates: TextView = view.findViewById(R.id.dates)
        val loading: ProgressBar = view.findViewById(R.id.loading)
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