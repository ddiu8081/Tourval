package site.ddiu.tourval

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.all_likelist_item.view.*
import java.text.DecimalFormat

class AllLikeListAdapter(val items : List<MainActivity.LocItem>, private val itemClickListener: (MainActivity.LocItem)->Unit) : RecyclerView.Adapter<AllLikeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_likelist_item, parent, false)
        return ViewHolder(view, itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    class ViewHolder(val view: View, private val itemClickListener: (MainActivity.LocItem) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: MainActivity.LocItem, pos: Int) {
            view.textView_locItemName.text = item.name
            val dFormat = DecimalFormat(".00")
            if (pos < 3) {
                view.linearLayout_bg.setBackgroundColor(Color.parseColor("#11ffc408"))
            }
            view.textView_locItemSimilarity.text = dFormat.format(item.similarity * 100).toString() + "%"
            Picasso.get().load(item.imgSrc).placeholder(R.drawable.nopic).into(view.imageView_likeListImgSrc)
            view.setOnClickListener {
                itemClickListener(item)
            }
        }
    }
}