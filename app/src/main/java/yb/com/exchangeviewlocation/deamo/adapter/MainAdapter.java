package yb.com.exchangeviewlocation.deamo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.adapter.bean.ViewModel;

/**
 * Created by Administrator on 2015/7/30.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private final int mItemLayout;
    private final List<ViewModel> mItmes;
    private  RecyclerOnItemClickListener listener;

    public void setListener(RecyclerOnItemClickListener listener) {
        this.listener = listener;
    }

    public MainAdapter(List<ViewModel> items, int itemLayout) {
        this.mItmes = items;
        this.mItemLayout=itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ViewModel item = mItmes.get(position);
        holder.text.setText(item.card_name);
        holder.image.setImageResource(item.getImageResourceId(holder.itemView.getContext()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.ItmeclickListener(holder);
                }
            }
        });
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return mItmes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_img);
            text = (TextView) itemView.findViewById(R.id.card_name);
        }
    }

    public interface RecyclerOnItemClickListener {
        void ItmeclickListener(ViewHolder tag);
    }
}
