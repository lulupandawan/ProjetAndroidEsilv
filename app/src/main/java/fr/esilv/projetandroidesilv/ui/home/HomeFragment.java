package fr.esilv.projetandroidesilv.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
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
import es.dmoral.toasty.Toasty;
import fr.esilv.projetandroidesilv.Adapters.HealthLabelAdapter;
import fr.esilv.projetandroidesilv.Adapters.RecipeListAdapter;
import fr.esilv.projetandroidesilv.R;
import fr.esilv.projetandroidesilv.api.Hits;
import fr.esilv.projetandroidesilv.api.RecipeSearchResponse;
import fr.esilv.projetandroidesilv.api.RecipeService;
import fr.esilv.projetandroidesilv.model.HealthLabel;
import fr.esilv.projetandroidesilv.model.Recipe;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private static final String APP_ID = "d8d49529";
    private static final String APP_KEY = "e611a21486cca33b2bc4bd68766a1dad";
    private Integer from = 0;
    private Integer to = 3;
    private RecipeService service;
    private String action = "none";

    private Button load_more;
    private Button search_btn;
    private EditText search_bar;
    private RecipeListAdapter _recipeListAdapter ;
    private ArrayList<Recipe> _recipeList = new ArrayList<>(); // contains all recipes without filter
    private ArrayList<Recipe> _recipeListFiltered = new ArrayList<>(); // contains recipes filtered with tags selected
    private HealthLabelAdapter _healthLabelListAdapter ;
    private List<HealthLabel> _healthlabelList = new ArrayList<>(); // contains all labels possible from the API
    private List<HealthLabel> _healthlabelSelected = new ArrayList<>(); // contains all labels selected by the user
    private LinearLayoutManager llm = null; // for recipe list
    private LinearLayoutManager hlm = null; // for health label

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // buttons listeners

        load_more = (Button) root.findViewById(R.id.load_more);
        load_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("load more", "load more");
                from += 3;
                to += 3;
                action = "load_more";
                String q = search_bar.getText().toString();
                if(!q.equals(""))
                    launchSearch(search_bar.getText().toString());
                else Toast.makeText(getContext(), "Empty search bar", Toast.LENGTH_SHORT).show();
            }
        });


        search_bar = (EditText) root.findViewById(R.id.search_bar);
        getQuerySaved();
        search_btn = (Button) root.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String q = search_bar.getText().toString();
                if(!q.matches("")){
                    Log.i("search query", "search query: " + q);
                    saveQuery();
                    launchSearch(q);
                }else
                    Toast.makeText(getContext(), "Empty search", Toast.LENGTH_SHORT).show();
            }
        });

        // load all labels from the API
        load_health_Label();

        // load 2 adapters
        _recipeListAdapter = new RecipeListAdapter(this.getContext(), this._recipeListFiltered);
        _healthLabelListAdapter = new HealthLabelAdapter(this.getContext(), this._healthlabelList, new HealthLabelAdapter.OnHealthLabelClickListener() {
            @Override public void onItemClick(HealthLabel item) {
                changeFilter(item);
                _recipeListAdapter.setRecipeList(_recipeListFiltered);
            }
        });

        // find recycler view
        final RecyclerView tmpRecyclerView = root.findViewById(R.id.recipe_list_recycler_view);
        final RecyclerView tmpHealthlabelRecyclerView = root.findViewById(R.id.health_label_recycler_view);

        // create layouts managers
        llm = new LinearLayoutManager(root.getContext());
        hlm = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

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

        // set animation and adapter for recipe recycler view
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(tmpRecyclerView.getContext(), resId);
        tmpRecyclerView.setLayoutAnimation(animation);
        tmpRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tmpRecyclerView.setLayoutManager(llm);
        tmpRecyclerView.setAdapter(_recipeListAdapter);
        if(llm.findLastVisibleItemPosition() == _recipeListFiltered.size()-1 && _recipeListFiltered.size() > 0){
            load_more.setVisibility(View.VISIBLE);
        }

        // set animation and adapter for recipe recycler view
        tmpHealthlabelRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tmpHealthlabelRecyclerView.setLayoutManager(hlm);
        tmpHealthlabelRecyclerView.setAdapter(_healthLabelListAdapter);
        prepareQuery();
        return root;
    }

    private void prepareQuery(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  // <-- this is the important line!

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.edamam.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(RecipeService.class);
    }

    private void launchSearch(String query) {
        service.search(query, APP_KEY, APP_ID, from, to).enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeSearchResponse> call, @NonNull Response<RecipeSearchResponse> response) {
                Log.d("onResponse", "onResponse");
                if (response.isSuccessful()) {
                    Log.d("isSuccessful", "isSuccessful");
                    RecipeSearchResponse recipeSearchResponse = response.body();
                    ArrayList<Hits> hits = recipeSearchResponse.getHits();
                    if(hits.size() > 0){
                        Log.d("isSuccessful", "size > 0");
                        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
                        for (Hits hit: hits){
                            Log.d("recipe name: ", hit.getRecipe().getLabel());
                            Log.d("recipe URI: ", hit.getRecipe().getUri());
                            Log.d("recipe imageUrl: ", hit.getRecipe().getImageUrl());
                            Log.d("recipe yield: ", hit.getRecipe().getYield());
                            recipes.add(hit.getRecipe());
                        }
                        if(action.equals("load_more")){
                            _recipeList.addAll(recipes);
                            filterList(recipes);
                            _recipeListAdapter.addAll(recipes);
                            llm.scrollToPosition(_recipeListAdapter.getItemCount() - 1);
                        }
                        else{
                            _recipeList = recipes;
                            changeFilteredItem();
                            _recipeListAdapter.setRecipeList(_recipeListFiltered);
                            load_more.setVisibility(View.VISIBLE);
                        }
                    }
                    else Toast.makeText(getContext(), "No recipe found", Toast.LENGTH_SHORT).show();

                    action = "";
                    if(llm.findLastVisibleItemPosition() == _recipeListFiltered.size()-1 && _recipeListFiltered.size() > 0){
                        load_more.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {
                Log.e("onFailure", "onFailure", t);
            }
        });
    }

    private void changeFilter(HealthLabel hl){
        int index = this._healthlabelSelected.indexOf(hl);
        if(index == -1) {
            this._healthlabelSelected.add(hl);
            if(this._recipeList.size() == 0)
                Toasty.info(getContext(), "There is no recipe to filter", Toast.LENGTH_SHORT, true).show();
        }
        else {
            this._healthlabelSelected.remove(hl);
        }
        changeFilteredItem();
    }

    private void changeFilteredItem(){
        Log.i("add", "1");
        if(this._recipeList.size() > 0){
            this._recipeListFiltered.clear();
            Log.i("add", "2");
            if(this._healthlabelSelected.size() > 0) {
                Log.i("add", "3");
                for (int e = 0; e < this._recipeList.size(); e++) {
                    Recipe rec = this._recipeList.get(e);
                    Log.i("add", rec.getAllLabels().toString());
                    Boolean toAdd = true;
                    for (int i = 0; i < this._healthlabelSelected.size(); i++) {
                        HealthLabel tmpHl = this._healthlabelSelected.get(i);
                        Log.i("add", "recipe label: rezrza");
                        Log.i("add", tmpHl.getLabel());

                        if (!rec.getAllLabels().contains(tmpHl.getLabel())) {
                            Log.i("add", "add recipe: " + rec.getLabel());
                            toAdd = false;
                        }
                    }
                    if(toAdd){
                        this._recipeListFiltered.add(rec);
                    }
                }
            }
            else
                _recipeListFiltered = new ArrayList(_recipeList); //copy without references
            // set Adapter
            //_recipeListAdapter.setRecipeList(this._recipeListFiltered);
        }
    }

    private void filterList(ArrayList<Recipe> recipes){
        if(recipes.size() > 0){
            for(int i = 0 ; i < this._healthlabelSelected.size(); i++) {
                HealthLabel tmpHl = this._healthlabelSelected.get(i);
                for (int e = 0 ; e < recipes.size(); e++){
                    Recipe rec = recipes.get(e);
                    if (!rec.getAllLabels().contains(tmpHl.getLabel())){
                        recipes.remove(rec);
                    }
                }
            }
        }
    }

    private void load_health_Label(){
        this._healthlabelList.add(new HealthLabel("Low-Carb"));
        this._healthlabelList.add(new HealthLabel("Sugar-Conscious"));
        this._healthlabelList.add(new HealthLabel("Low-Fat"));
        this._healthlabelList.add(new HealthLabel("High Protein"));
        this._healthlabelList.add(new HealthLabel("Balanced"));
        this._healthlabelList.add(new HealthLabel("Vegetarian"));
        this._healthlabelList.add(new HealthLabel("Vegan"));
        this._healthlabelList.add(new HealthLabel("Alcohol-Free"));
    }

    private void getQuerySaved(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        String query = sharedPreferences.getString("query_recipe", "");
        this.search_bar.setText(query);
    }

    private void saveQuery(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("query_recipe", this.search_bar.getText().toString());
        editor.apply();
        Log.i("query saved", "query saved: " + this.search_bar.getText().toString());
    }

}