package vn.edu.stu.leafmusic.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        holder.tvName.setText(item.getTenDs());

        // Highlight default list
        if (item.isDefaultList()) {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.Green4));
        } else {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.black));
        }


        holder.itemView.setOnClickListener(v -> {
            // Show Toast with the id_ds of the clicked item
            Toast.makeText(context, "ID DS: " + item.getIdDs(), Toast.LENGTH_SHORT).show();
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
