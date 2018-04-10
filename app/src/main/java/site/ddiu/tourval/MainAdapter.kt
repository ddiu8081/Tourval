package site.ddiu.tourval

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_view_item.view.*

class MainAdapter(val items : List<MainActivity.LocItem>, val itemClickListener: (MainActivity.LocItem)->Unit) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_view_item, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(val view: View, val itemClickListener: (MainActivity.LocItem) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: MainActivity.LocItem) {
            view.textView8.text = item._id
            view.textView9.text = item.desc
            view.setOnClickListener {
                itemClickListener(item)
            }
        }
    }
}