package com.matkinson.apiproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    String maNouriture = "Cheddar Cheese";
    TextView foodName, hitNumber, published, ingredientsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodName =findViewById(R.id.foodName);
        hitNumber = findViewById(R.id.hitNumber);
        published = findViewById(R.id.published);
        ingredientsText = findViewById(R.id.ingredientsText);
        afficher();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem= menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("ecrire le nom du nourriture");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                maNouriture=query;
               afficher();

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void  afficher(){
        String url = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=w6XvNOqDCgi4z8otEsvChm18EI0E3JNl3de8sxgU&query="+maNouriture;
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                   String totalHits = response.getString("totalHits");

                    hitNumber.setText("Il y a "+totalHits+" produits sous cette nom");
                    JSONArray foods = response.getJSONArray("foods");
                    JSONObject food_object = foods.getJSONObject(0);
                    String description =  food_object.getString("lowercaseDescription");
                    String Ingrediants = food_object.getString("ingredients");
                    String publishedDate = food_object.getString("publishedDate");

                    ingredientsText.setText("Ce produit est constituer de: "+Ingrediants);
                    foodName.setText("Le nom du produit rechercher est: "+description);
                    published.setText("Cette produit fut produit depuis "+publishedDate);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}