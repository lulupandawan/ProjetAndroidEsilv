package fr.esilv.projetandroidesilv.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.R;
import fr.esilv.projetandroidesilv.model.Recipe;
import fr.esilv.projetandroidesilv.viewHolders.RecipeViewHolder;

import static android.content.Context.MODE_PRIVATE;

public class RecipeListAdapter  extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> _recipeList;
    private List<Recipe> _favoriteRecipe = new ArrayList<Recipe>();
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
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {
        final Recipe tmpRecipe = _recipeList.get(position);
        holder.recipeName.setText(tmpRecipe.getLabel());
        holder.yield.setText("for " + tmpRecipe.getYield() + " persons");
        holder.starNumber.setText(new DecimalFormat("##.#").format(generateStar(3,5)));
        Drawable myDrawable = c.getResources().getDrawable(R.drawable.demofromapi);
        holder.imageView.setImageDrawable(myDrawable);
        swapFavoriteIcon(tmpRecipe, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tagg", "view clicked");
            }
        });

        holder.toastLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tagg", "toastLove clicked");
                tmpRecipe.setFavorite(!tmpRecipe.isFavorite());
                swapFavoriteIcon(tmpRecipe, holder);
                holder.isFavorite = !holder.isFavorite;
                if(holder.isFavorite){
                    addRecetteAsFavorite(tmpRecipe);
                    Toast.makeText(c, "Recipe " + tmpRecipe.getLabel() + " added to favorite", Toast.LENGTH_SHORT).show();
                }
                else{
                    deleteRecipeFromFavorite(tmpRecipe);
                    Toast.makeText(c, "Recipe " + tmpRecipe.getLabel() + " remove from favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void swapFavoriteIcon(Recipe r, RecipeViewHolder holder){
        Drawable redIcon = c.getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
        Drawable blackIcon = c.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp);
        holder.toastLove.setImageDrawable(r.isFavorite() ? redIcon : blackIcon);
    }

    public void addRecetteAsFavorite(Recipe r){
        SharedPreferences sharedPreferences = this.c.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        this._favoriteRecipe = gson.fromJson(json, type);
        if(this._favoriteRecipe == null){
            this._favoriteRecipe = new ArrayList<Recipe>();
        }
        this._favoriteRecipe.add(r);
        saveFavoriteRecipe();
        Log.i("saveasfavoriterecipe", "save a recipe in my favorite recipe into shared preferences");
    }

    public void deleteRecipeFromFavorite(Recipe r){
        SharedPreferences sharedPreferences = this.c.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        this._favoriteRecipe = gson.fromJson(json, type);

        Log.i("deleteasfavoriterecipe", "remove R id -> " + r.getId());

        for(int i = 0 ; i < this._favoriteRecipe.size(); i++){
            Recipe rec = this._favoriteRecipe.get(i);

            Log.i("deleteasfavoriterecipe", "remove REC id -> " + rec.getId());

            if (rec.getId().equals(r.getId())){
                Log.i("deleteasfavoriterecipe", "remove recipe id -> " + rec.getId());
                this._favoriteRecipe.remove(rec);
                Log.i("deleteasfavoriterecipe", "delete recipe in my favorite recipe into shared preferences");
                break;
            }
        }
        saveFavoriteRecipe();
    }

    private void saveFavoriteRecipe(){
        SharedPreferences sharedPreferences = this.c.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this._favoriteRecipe);
        editor.putString("favorite recipe list", json);
        editor.apply();
        Log.i("savefavoriterecipe", "save my favorite recipe into sharedpreferences");
        Log.i("saveasfavoriterecipe", Integer.toString(this._favoriteRecipe.size()));
    }

}
