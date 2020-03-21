package fr.esilv.projetandroidesilv.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.R;
import fr.esilv.projetandroidesilv.model.HealthLabel;
import fr.esilv.projetandroidesilv.viewHolders.HealthLabelViewHolder;
import fr.esilv.projetandroidesilv.viewHolders.RecipeViewHolder;

public class HealthLabelAdapter  extends RecyclerView.Adapter<HealthLabelViewHolder> {

    private List<HealthLabel> _healthLabelList;
    private Context c;
    private OnHealthLabelClickListener listener = null;

    // CONSTRUCTORS
    public HealthLabelAdapter(Context c, List<HealthLabel> heathlabelList, OnHealthLabelClickListener listener) {
        this.c = c;
        this._healthLabelList = heathlabelList;
        this.listener = listener;
    }

    public HealthLabelAdapter(Context c, List<HealthLabel> heathlabelList) {
        this.c = c;
        this._healthLabelList = heathlabelList;
    }

    @NonNull
    @Override
    public HealthLabelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_health_label, parent, false);
        HealthLabelViewHolder tmpHealthLabelVH = new HealthLabelViewHolder(itemView);
        return tmpHealthLabelVH;
    }

    @Override
    public void onBindViewHolder(@NonNull final HealthLabelViewHolder holder, int position) {
        final HealthLabel tmpHealthLabel = _healthLabelList.get(position);
        holder.healthLabelBtn.setText(tmpHealthLabel.getLabel());

        holder.healthLabelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    if(!tmpHealthLabel.isSelected())
                        holder.healthLabelBtn.setBackgroundTintList(c.getResources().getColorStateList(R.color.lightOrange));
                    else
                        holder.healthLabelBtn.setBackgroundTintList(c.getResources().getColorStateList(R.color.lightBlue));
                    tmpHealthLabel.setIsSelected(!tmpHealthLabel.isSelected());

                    listener.onItemClick(tmpHealthLabel);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return this._healthLabelList.size();
    }


    public interface OnHealthLabelClickListener {
        void onItemClick(HealthLabel item);
    }
}
