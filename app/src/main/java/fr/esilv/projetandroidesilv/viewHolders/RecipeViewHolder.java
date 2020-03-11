package fr.esilv.projetandroidesilv.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.R;

public class RecipeViewHolder  extends  RecyclerView.ViewHolder{

    public TextView recipeName;
    public ImageView imageView;
    public TextView yield;
    public TextView starNumber;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeName = (TextView) itemView.findViewById(R.id.text_view_recipe_name);
        yield = (TextView) itemView.findViewById(R.id.yield);
        starNumber = (TextView) itemView.findViewById(R.id.star);
        imageView = (ImageView) itemView.findViewById(R.id.image_view_recipe);

    }

}
