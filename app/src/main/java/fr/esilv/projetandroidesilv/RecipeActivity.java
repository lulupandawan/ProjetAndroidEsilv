package fr.esilv.projetandroidesilv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import fr.esilv.projetandroidesilv.model.Recipe;
import fr.esilv.projetandroidesilv.viewHolders.RecipeViewHolder;

public class RecipeActivity  extends AppCompatActivity {

    private TextView recipeLabel;
    private TextView star;
    private ImageView favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        // To retrieve object in second Activity
        final Recipe recipe = (Recipe) intent.getSerializableExtra("MyRecipe");
        if(recipe == null){
            finish();
        }


        setContentView(R.layout.activity_recipe);
        recipeLabel = (TextView) findViewById(R.id.recipeLabel);
        star = (TextView) findViewById(R.id.star);
        favorite = (ImageView) findViewById(R.id.toastLove);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setFavorite(!recipe.isFavorite());
                swapFavoriteIcon(recipe);
                if(recipe.isFavorite()){
                    addRecetteAsFavorite(recipe);
                    Toast.makeText(getApplicationContext(), "Recipe " + recipe.getLabel() + " added to favorite", Toast.LENGTH_SHORT).show();
                }
                else{
                    deleteRecipeFromFavorite(recipe);
                    Toast.makeText(getApplicationContext(), "Recipe " + recipe.getLabel() + " remove from favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swapFavoriteIcon(recipe);

        recipeLabel.setText(recipe.getLabel());
        star.setText(recipe.getStarFormated());

        recipeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void swapFavoriteIcon(Recipe r){
        Drawable redIcon = getApplicationContext().getResources().getDrawable(R.drawable.ic_favorite_red_48dp);
        Drawable blackIcon = getApplicationContext().getResources().getDrawable(R.drawable.ic_favorite_border_black_48dp);
        favorite.setImageDrawable(r.isFavorite() ? redIcon : blackIcon);
    }

    public void addRecetteAsFavorite(Recipe r){
        List<Recipe> recipeList ;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        recipeList = gson.fromJson(json, type);
        if(recipeList == null){
            recipeList = new ArrayList<Recipe>();
        }
        recipeList.add(r);
        saveFavoriteRecipe(recipeList);
        Log.i("saveasfavoriterecipe", "save a recipe in my favorite recipe into shared preferences");
    }

    public void deleteRecipeFromFavorite(Recipe r){
        List<Recipe> recipeList = new ArrayList<Recipe>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        recipeList = gson.fromJson(json, type);

        Log.i("deleteasfavoriterecipe", "remove R id -> " + r.getId());

        for(int i = 0 ; i < recipeList.size(); i++){
            Recipe rec = recipeList.get(i);

            Log.i("deleteasfavoriterecipe", "remove REC id -> " + rec.getId());

            if (rec.getId().equals(r.getId())){
                Log.i("deleteasfavoriterecipe", "remove recipe id -> " + rec.getId());
                recipeList.remove(rec);
                Log.i("deleteasfavoriterecipe", "delete recipe in my favorite recipe into shared preferences");
                break;
            }
        }
        saveFavoriteRecipe(recipeList);
    }

    private void saveFavoriteRecipe(List<Recipe> recipeList){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipeList);
        editor.putString("favorite recipe list", json);
        editor.apply();
        Log.i("savefavoriterecipe", "save my favorite recipe into sharedpreferences");
        Log.i("saveasfavoriterecipe", Integer.toString(recipeList.size()));
    }

}
