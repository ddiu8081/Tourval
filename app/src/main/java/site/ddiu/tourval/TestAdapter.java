package site.ddiu.tourval;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView.ViewHolder;

public class TestAdapter extends RecyclerView.Adapter {
    Context mContext;

    public TestAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_view_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    class ItemViewHolder extends ViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}