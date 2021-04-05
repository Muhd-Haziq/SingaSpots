package sg.edu.rp.c346.s19047241.singaspots;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class favouritesScreen extends AppCompatActivity {

    ListView locList, foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_screen);

        locList = findViewById(R.id.locationFavList);
        foodList = findViewById(R.id.foodFavList);

        // Location class
        ArrayList<locations> locArrayList = new ArrayList<>();
        ArrayList<String> locNameList = new ArrayList<>();
        ArrayAdapter locAdapter = new LocationAdapter(this, R.layout.list_row, locArrayList);
        locList.setAdapter(locAdapter);

        // food class test
        ArrayList<food> foodArrayList = new ArrayList<>();
        ArrayList<String> foodNameList = new ArrayList<>();
        ArrayAdapter foodAdapter = new FoodAdapter(this, R.layout.list_row_food, foodArrayList);
        foodList.setAdapter(foodAdapter);

        // Create a reference to Firebase for locations that were favourites
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("favourites").child("locations");
        DatabaseReference refFood = FirebaseDatabase.getInstance().getReference().child("favourites").child("food");

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locArrayList.clear();
                locNameList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    locations locs = snap.getValue(locations.class);
                    String location = locs.getName();
                    //Log.d("test", location);
                    //locationsList.add(location);

                    // Location class test
                    locArrayList.add(locs);
                    locNameList.add(location);
                }

                //adapter.notifyDataSetChanged();

                // Sort namesList and locationsList with Collections.sort
                Collections.sort(locNameList, String.CASE_INSENSITIVE_ORDER);
                Collections.sort(locArrayList, Comparator.comparing(locations::getName));

                locAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refFood.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodArrayList.clear();
                foodNameList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    food foods = snap.getValue(food.class);
                    String foodName = foods.getName();
                    //Log.d("test", location);
                    //locationsList.add(location);

                    // Location class test
                    foodArrayList.add(foods);
                    foodNameList.add(foodName);
                }

                //adapter.notifyDataSetChanged();

                // Sort namesList and locationsList with Collections.sort
                Collections.sort(foodNameList, String.CASE_INSENSITIVE_ORDER);
                Collections.sort(foodArrayList, Comparator.comparing(food::getName));

                foodAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        locList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(favouritesScreen.this, locationDetails.class);
                //intent.putExtra("name", (String) adapter.getItem(position));
                intent.putExtra("name", locNameList.get(position));
                startActivity(intent);
                //Toast.makeText(locationScreen.this, (String) adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(favouritesScreen.this, foodPopup.class);
                intent.putExtra("name", foodNameList.get(position));
                startActivity(intent);
            }
        });

    }


}