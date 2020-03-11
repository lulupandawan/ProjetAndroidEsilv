package fr.esilv.projetandroidesilv.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.R;
import fr.esilv.projetandroidesilv.model.Recipe;
import fr.esilv.projetandroidesilv.viewHolders.RecipeViewHolder;

public class RecipeListAdapter  extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> _recipeList;
    private Context c;

    // CONSTRUCTOR
    public RecipeListAdapter(Context c, List<Recipe> recipeList) {
        this.c = c;
        this._recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe, parent, false);
        RecipeViewHolder tmpRecipeVH = new RecipeViewHolder(itemView);
        return tmpRecipeVH;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe tmpRecipe = _recipeList.get(position);
        holder.recipeName.setText(tmpRecipe.getLabel());
        holder.yield.setText("for " + tmpRecipe.getYield() + " persons");
        holder.starNumber.setText(new DecimalFormat("##.#").format(generateStar(3,5)));
        Drawable myDrawable = c.getResources().getDrawable(R.drawable.demofromapi);
        holder.imageView.setImageDrawable(myDrawable);
    }


    @Override
    public int getItemCount() {
        return this._recipeList.size();
    }

    private Double generateStar(int min, int max){
        Random r = new Random();
        double random = min + r.nextDouble() * (max - min);
        return random;
    }

}
