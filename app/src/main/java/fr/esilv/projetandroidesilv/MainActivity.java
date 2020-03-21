package fr.esilv.projetandroidesilv;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import fr.esilv.projetandroidesilv.api.Hits;
import fr.esilv.projetandroidesilv.api.RecipeSearchResponse;
import fr.esilv.projetandroidesilv.api.RecipeService;
import fr.esilv.projetandroidesilv.api.ResExample;
import fr.esilv.projetandroidesilv.model.Recipe;
import fr.esilv.projetandroidesilv.ui.dashboard.DashboardFragment;
import fr.esilv.projetandroidesilv.ui.home.HomeFragment;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity  extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navView;
    final Fragment recipeFragment = new HomeFragment();
    Fragment favRecipeFragment = new DashboardFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = recipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.frame, favRecipeFragment, "2").hide(favRecipeFragment).commit();
        fm.beginTransaction().add(R.id.frame, recipeFragment, "1").commit();

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);


        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_receipe:
                Log.i("onnav", menuItem.toString());
                Log.i("onnav", active.toString());
                fm.beginTransaction().hide(active).show(recipeFragment).commit();
                active = recipeFragment;
                return true;

            case R.id.navigation_favorite:
                Log.i("onnav", menuItem.toString());
                Log.i("onnav", active.toString());

                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.detach(favRecipeFragment);
                ft.attach(favRecipeFragment);
                ft.commit();
                fm.beginTransaction().hide(active).show(favRecipeFragment).commit();
                active = favRecipeFragment;
                return true;

        }

        return false;

    }

    public void refreshFragmentsFav(){
        favRecipeFragment = new DashboardFragment();
    }

}
