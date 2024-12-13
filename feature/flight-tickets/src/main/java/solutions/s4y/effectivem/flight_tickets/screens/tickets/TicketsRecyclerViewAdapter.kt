package solutions.s4y.effectivem.flight_tickets.screens.tickets

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import solutions.s4y.effectivem.flight_tickets.R
import solutions.s4y.effectm.domain.models.Ticket
import solutions.s4y.effectm.domain.models.formatted
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class TicketsRecyclerViewAdapter(private var _tickets: Array<Ticket>) :
    RecyclerView.Adapter<TicketsRecyclerViewAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return ItemViewHolder(view)
    }

    var tickets: Array<Ticket>
        get() = _tickets
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            Log.d(TAG, "setTickets ${value.size} to ${_tickets.size}")
            _tickets = value
            notifyDataSetChanged()
        }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val ticket = _tickets[position]
        if ((ticket.badge ?: "").isEmpty()) {
            holder.badge.visibility = View.GONE
        } else {
            holder.badge.visibility = View.VISIBLE
            holder.badge.text = ticket.badge
        }
        holder.price.text = ticket.price.formatted
        holder.departureTime.text = toHHMM(ticket.departure.date)
        holder.arrivalTime.text = toHHMM(ticket.arrival.date)
        holder.duration.text =
            msToRoundedHours(ticket.arrival.date.time - ticket.departure.date.time)
        if (ticket.hasTransfer) {
            holder.directFly.visibility = View.INVISIBLE
            holder.directFlySep.visibility = View.INVISIBLE
        } else {
            holder.directFly.visibility = View.VISIBLE
            holder.directFlySep.visibility = View.VISIBLE
        }
        holder.departureAirPort.text = ticket.departure.airport
        holder.arrivalAirPort.text = ticket.arrival.airport
    }

    override fun getItemCount(): Int {
        return _tickets.size
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val badge: TextView = view.findViewById(R.id.badge)
        val price: TextView = view.findViewById(R.id.price)
        val departureTime: TextView = view.findViewById(R.id.departure_time)
        val arrivalTime: TextView = view.findViewById(R.id.arrival_time)
        val duration: TextView = view.findViewById(R.id.duration)
        val directFly: TextView = view.findViewById(R.id.direct_flight)
        val directFlySep: TextView = view.findViewById(R.id.direct_flight_separator)
        val departureAirPort: TextView = view.findViewById(R.id.departure_airport)
        val arrivalAirPort: TextView = view.findViewById(R.id.arrival_airport)
    }

    companion object {
        private const val TAG = "TicketsRecyclerViewAdapter"
        fun msToRoundedHours(ms: Long): String {
            val hours = ms / (60 * 60 * 1000).toDouble()
            val rounded = (hours * 2).roundToInt() / 2.0 // Rounds to nearest 0.5
            return if (rounded == rounded.toInt().toDouble()) {
                "${rounded.toInt()}"
            } else {
                "${rounded}"
            }
        }
        private val formatter = SimpleDateFormat("HH:mm")
        fun toHHMM(date: Date): String {
            return formatter.format(date)
        }
    }
}