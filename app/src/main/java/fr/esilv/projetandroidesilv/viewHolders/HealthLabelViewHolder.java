package fr.esilv.projetandroidesilv.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.R;

public class HealthLabelViewHolder extends  RecyclerView.ViewHolder {

    public Button healthLabelBtn;

    public HealthLabelViewHolder(@NonNull View itemView) {
        super(itemView);
        healthLabelBtn = (Button) itemView.findViewById(R.id.button_healthlabel_name);
    }
}