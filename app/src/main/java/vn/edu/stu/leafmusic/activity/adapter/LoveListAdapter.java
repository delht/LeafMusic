package vn.edu.stu.leafmusic.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.model.LoveLIst;

public class LoveListAdapter extends RecyclerView.Adapter<LoveListAdapter.LoveListViewHolder> {

    private final Context context;
    private final List<LoveLIst> loveList;

    public LoveListAdapter(Context context, List<LoveLIst> loveList) {
        this.context = context;
        this.loveList = loveList;

        for (LoveLIst item : loveList) {
            Log.d("Adapter Init", "Item: " + item.getTenDs());
        }
    }

    @NonNull
    @Override
    public LoveListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lovelist, parent, false);
        return new LoveListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoveListViewHolder holder, int position) {
        LoveLIst item = loveList.get(position);
        Log.d("LoveListAdapter", "Item Name: " + item.getTenDs());

        holder.tvName.setText(item.getTenDs());

        // Nếu cần thêm xử lý sự kiện
        holder.itemView.setOnClickListener(v -> {
            // Xử lý khi nhấn vào item
        });
    }

    @Override
    public int getItemCount() {
        return loveList.size();
    }

    public static class LoveListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgIcon;

        public LoveListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ds_name);
            imgIcon = itemView.findViewById(R.id.img_ds);
        }
    }
}
