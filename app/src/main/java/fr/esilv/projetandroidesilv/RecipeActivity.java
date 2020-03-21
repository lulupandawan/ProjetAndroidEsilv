package fr.esilv.projetandroidesilv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import fr.esilv.projetandroidesilv.Adapters.HealthLabelAdapter;
import fr.esilv.projetandroidesilv.model.HealthLabel;
import fr.esilv.projetandroidesilv.model.Recipe;
import fr.esilv.projetandroidesilv.viewHolders.RecipeViewHolder;

public class RecipeActivity  extends AppCompatActivity {

    private TextView recipeLabel;
    private TextView star;
    private TextView desc_recipe;
    private TextView ingredient_recipe;
    private ImageView favorite;
    private ImageView image_view_recipe;
    private LinearLayoutManager hlm = null; // for health label
    private HealthLabelAdapter _healthLabelListAdapter ;

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
        desc_recipe = (TextView) findViewById(R.id.text_desc_recipe);
        ingredient_recipe = (TextView) findViewById(R.id.text_ingredient_recipe);
        star = (TextView) findViewById(R.id.star);
        favorite = (ImageView) findViewById(R.id.toastLove);
        image_view_recipe = (ImageView) findViewById(R.id.image_view_recipe);

        // find recycler view
        final RecyclerView tmpHealthlabelRecyclerView = findViewById(R.id.health_label_rv_activity_recipe);

        hlm = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        // set animation and adapter for recipe recycler view
        _healthLabelListAdapter = new HealthLabelAdapter(getApplicationContext(), recipe.getAllLabelsObj());
        tmpHealthlabelRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tmpHealthlabelRecyclerView.setLayoutManager(hlm);
        tmpHealthlabelRecyclerView.setAdapter(_healthLabelListAdapter);

        Glide.with(getApplicationContext()).load(recipe.getImageUrl()).into(image_view_recipe);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.setFavorite(!recipe.isFavorite());
                swapFavoriteIcon(recipe);
                if(recipe.isFavorite()){
                    addRecetteAsFavorite(recipe);
                    Toasty.success(getApplicationContext(), "Recipe " + recipe.getLabel() + " added to favorite", Toast.LENGTH_LONG, true).show();
                }
                else{
                    deleteRecipeFromFavorite(recipe);
                    Toasty.info(getApplicationContext(), "Recipe " + recipe.getLabel() + " removed from favorite", Toast.LENGTH_LONG, true).show();
                }
            }
        });

        swapFavoriteIcon(recipe);

        recipeLabel.setText(recipe.getLabel());
        recipeLabel.setText(recipe.getLabel());
        String desc = "for " + recipe.getYield() + " persons\n" +
                recipe.getTotalTime() + "\n";
        desc_recipe.setText(desc);
        ingredient_recipe.setText(TextUtils.join("\n\n\n", recipe.getIngredientLines()));
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

        Log.i("deleteasfavoriterecipe", "remove R id -> " + r.getUri());

        for(int i = 0 ; i < recipeList.size(); i++){
            Recipe rec = recipeList.get(i);

            Log.i("deleteasfavoriterecipe", "remove REC id -> " + rec.getUri());

            if (rec.getUri().equals(r.getUri())){
                Log.i("deleteasfavoriterecipe", "remove recipe id -> " + rec.getUri());
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
