package fr.esilv.projetandroidesilv.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.R;

public class FavRecipeViewHolder  extends  RecyclerView.ViewHolder{

    public TextView recipeName;
    public ImageView imageView;
    public ImageView toastLove;
    public TextView yield;
    public TextView starNumber;
    public Boolean isFavorite = false;

    public FavRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        recipeName = (TextView) itemView.findViewById(R.id.text_view_recipe_name);
        yield = (TextView) itemView.findViewById(R.id.yield);
        starNumber = (TextView) itemView.findViewById(R.id.star);
        imageView = (ImageView) itemView.findViewById(R.id.image_view_recipe);
        toastLove = (ImageView) itemView.findViewById(R.id.toastLove);

    }

}