package com.example.myrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myrecipeapp.Adapters.RandomRecipeAdapter;
import com.example.myrecipeapp.Listeners.RandomRecipeResponseListener;
import com.example.myrecipeapp.Listeners.RecipeClickListener;
import com.example.myrecipeapp.Models.RandomRecipeApiResponse;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;

    CallRecipe manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        searchView=findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                manager.getRandomRecipes(randomRecipeResponseListener);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        manager = new CallRecipe(this);
        manager.getRandomRecipes(randomRecipeResponseListener);
        dialog.show();
    }
        private final RandomRecipeResponseListener randomRecipeResponseListener=new RandomRecipeResponseListener() {
            @Override
            public void didFetch(RandomRecipeApiResponse response, String message) {
                recyclerView=findViewById(R.id.recycler_random);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
                randomRecipeAdapter=new RandomRecipeAdapter(MainActivity.this,response.recipes,recipeClickListener);
                recyclerView.setAdapter(randomRecipeAdapter);
            }

            @Override
            public void didError(String message) {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        };

    private final RecipeClickListener recipeClickListener =new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(MainActivity.this,RecipeDetailsActivity.class)
                    .putExtra("id",id));
        }
    };
    }