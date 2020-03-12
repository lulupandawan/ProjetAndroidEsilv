package fr.esilv.projetandroidesilv.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.esilv.projetandroidesilv.Adapters.RecipeListAdapter;
import fr.esilv.projetandroidesilv.R;
import fr.esilv.projetandroidesilv.model.Recipe;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button load_more;
    private RecipeListAdapter _recipeListAdapter ;
    private List<Recipe> _recipeList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        // button listener
        load_more = (Button) root.findViewById(R.id.load_more);
        load_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("load more", "load more");
            }
        });



        // load recipe
        //loadRecipe();
        this.loadData();
        _recipeListAdapter = new RecipeListAdapter(this.getContext(), this._recipeList);
        final RecyclerView tmpRecyclerView = root.findViewById(R.id.recipe_list_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(root.getContext());
        tmpRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    load_more.setVisibility(View.VISIBLE);
                }
                else if(load_more.getVisibility() == View.VISIBLE){
                    load_more.setVisibility(View.INVISIBLE);
                }
            }
        });
        tmpRecyclerView.setLayoutManager(llm);
        tmpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tmpRecyclerView.setAdapter(_recipeListAdapter);

        Log.i("taggg", "tagggggg");

        return root;
    }

    private void loadRecipe(){
        this._recipeList.add(new Recipe("1", "poulett", "rezar.png", "Ceci est un poulett", "4", false));
        this._recipeList.add(new Recipe("2", "chicken", "rezar.png", "Ceci est un chicken", "4", false));
        this._recipeList.add(new Recipe("3", "poulett", "rezar.png", "Ceci est un poulett", "4", false));
        this._recipeList.add(new Recipe("4", "chicken", "rezar.png", "Ceci est un chicken", "4", false));
        this._recipeList.add(new Recipe("5", "poulett", "rezar.png", "Ceci est un poulett", "4", false));
        this._recipeList.add(new Recipe("6", "chicken", "rezar.png", "Ceci est un chicken", "4", false));
        this._recipeList.add(new Recipe("7", "poulett", "rezar.png", "Ceci est un poulett", "4", false));
        this._recipeList.add(new Recipe("8", "chicken", "rezar.png", "Ceci est un chicken", "4", false));
        this._recipeList.add(new Recipe("9", "poulett", "rezar.png", "Ceci est un poulett", "4", false));
        this._recipeList.add(new Recipe("10", "chicken", "rezar.png", "Ceci est un chicken", "4", false));
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
        this.saveData();
    }


    private void saveData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this._recipeList);
        editor.putString("recipe list", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recipe list", null);
        Type type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        this._recipeList = gson.fromJson(json, type);

    }

}