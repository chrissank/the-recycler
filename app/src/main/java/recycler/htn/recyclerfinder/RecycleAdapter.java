package recycler.htn.recyclerfinder;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    public static int globalPosition = -1;
    private LayoutInflater li;
    private ItemClickListener l;
    public Recycler recycler;

    public RecycleAdapter() {
        recycler = Recycler.instance;
        this.li = LayoutInflater.from(recycler);
        this.setClickListener(new RecycleAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new RecyclingInfo(Recycler.toRecycle.get(position)).createDialog();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.object_layout, parent, false);
        view.setPadding(20, 10, 0, 10);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecycleObject obj = recycler.toRecycle.get(position);
        holder.objectName.setText(obj.type + " (" + (obj.prob * 100) + "%)");
    }

    @Override
    public int getItemCount() {
        return recycler.toRecycle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView objectName;

        ViewHolder(View itemView) {
            super(itemView);
            objectName = itemView.findViewById(R.id.itemName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (l != null) l.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.l = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
