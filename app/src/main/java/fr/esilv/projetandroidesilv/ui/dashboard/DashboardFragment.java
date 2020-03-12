package fr.esilv.projetandroidesilv.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.Adapters.FavRecipeListAdapter;
import fr.esilv.projetandroidesilv.Adapters.RecipeListAdapter;
import fr.esilv.projetandroidesilv.R;
import fr.esilv.projetandroidesilv.model.Recipe;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FavRecipeListAdapter _favRecipeListAdapter ;
    private List<Recipe> _favRecipeList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        loadData();

        _favRecipeListAdapter = new FavRecipeListAdapter(this.getContext(), this._favRecipeList);
        final RecyclerView tmpRecyclerView = root.findViewById(R.id.fav_recipe_list_recycler_view);
        GridLayoutManager glm = new GridLayoutManager(root.getContext(), 2);
        tmpRecyclerView.setLayoutManager(glm);
        tmpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tmpRecyclerView.setAdapter(_favRecipeListAdapter);
        return root;
    }


    private void loadData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        this._favRecipeList = gson.fromJson(json, type);
        if(this._favRecipeList == null){
            this._favRecipeList = new ArrayList<Recipe>();
        }
    }

}