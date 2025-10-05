import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.testdatabinding.data.model.TripModel
import com.example.testdatabinding.databinding.ItemTripBinding
import com.example.testdatabinding.ui.adapter.TripDiffCallback
import com.example.testdatabinding.ui.adapter.TripViewHolder

class TripAdapter(
    private val onItemClick: (TripModel) -> Unit
) : ListAdapter<TripModel, TripViewHolder>(TripDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding, onItemClick)
    }
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = getItem(position)
        holder.bind(trip)
    }
}
