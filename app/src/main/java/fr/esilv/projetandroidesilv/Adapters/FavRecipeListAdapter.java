package fr.esilv.projetandroidesilv.Adapters;

import android.content.Context;
import android.content.Intent;
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
import fr.esilv.projetandroidesilv.RecipeActivity;
import fr.esilv.projetandroidesilv.model.Recipe;
import fr.esilv.projetandroidesilv.viewHolders.FavRecipeViewHolder;
import fr.esilv.projetandroidesilv.viewHolders.RecipeViewHolder;

import static android.content.Context.MODE_PRIVATE;

public class FavRecipeListAdapter  extends RecyclerView.Adapter<FavRecipeViewHolder> {

    private List<Recipe> _favRecipeList;
    private Context c;

    // CONSTRUCTOR
    public FavRecipeListAdapter(Context c, List<Recipe> recipeList) {
        this.c = c;
        this._favRecipeList = recipeList;
    }

    @NonNull
    @Override
    public FavRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fav_recipe, parent, false);
        FavRecipeViewHolder tmpRecipeVH = new FavRecipeViewHolder(itemView);
        return tmpRecipeVH;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavRecipeViewHolder holder, final int position) {
        final Recipe tmpRecipe = _favRecipeList.get(position);
        holder.starNumber.setText(new DecimalFormat("##.#").format(tmpRecipe.getStar()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, RecipeActivity.class);
                intent.putExtra("MyRecipe", tmpRecipe);
                c.startActivity(intent);
            }
        });

        holder.toastLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpRecipe.setFavorite(!tmpRecipe.isFavorite());
                int index = _favRecipeList.indexOf(tmpRecipe);
                holder.isFavorite = !holder.isFavorite;
                    deleteRecipeFromFavorite(tmpRecipe);
                    Toast.makeText(c, "Recipe " + tmpRecipe.getLabel() + " remove from favorite", Toast.LENGTH_SHORT).show();
                    notifyItemRemoved(index);
            }
        });
    }


    @Override
    public int getItemCount() {
        return this._favRecipeList.size();
    }

    private Double generateStar(int min, int max){
        Random r = new Random();
        double random = min + r.nextDouble() * (max - min);
        return random;
    }

    public void deleteRecipeFromFavorite(Recipe r){
        SharedPreferences sharedPreferences = this.c.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        this._favRecipeList = gson.fromJson(json, type);

        Log.i("deleteasfavoriterecipe", "remove R id -> " + r.getId());

        for(int i = 0 ; i < this._favRecipeList.size(); i++){
            Recipe rec = this._favRecipeList.get(i);

            Log.i("deleteasfavoriterecipe", "remove REC id -> " + rec.getId());

            if (rec.getId().equals(r.getId())){
                Log.i("deleteasfavoriterecipe", "remove recipe id -> " + rec.getId());
                this._favRecipeList.remove(rec);
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
        String json = gson.toJson(this._favRecipeList);
        editor.putString("favorite recipe list", json);
        editor.apply();
        Log.i("savefavoriterecipe", "save my favorite recipe into sharedpreferences");
        Log.i("saveasfavoriterecipe", Integer.toString(this._favRecipeList.size()));
    }

}
