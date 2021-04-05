package sg.edu.rp.c346.s19047241.singaspots;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class locationScreen extends AppCompatActivity {

    ListView listView;
    EditText searchText;
    TextView txtType;

    String userSearch = "";
    ArrayAdapter adapterFull;
    DatabaseReference ref;
    ArrayList itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);

        listView = findViewById(R.id.locationList);
        searchText = findViewById(R.id.txtSearch);
        txtType = findViewById(R.id.textType);

        String type = getIntent().getStringExtra("type");

        // Initialize ArrayList of locations to retrieve data from database
        ArrayList<locations> locationsArrayList = new ArrayList<>();
        ArrayList<food> foodArrayList = new ArrayList<>();
        ArrayList<String> namesList = new ArrayList<>();

        // Create a reference to Firebase
        ref = FirebaseDatabase.getInstance().getReference();

        if(type.equals("locations")){
            txtType.setText("Locations");

            adapterFull = new LocationAdapter(this, R.layout.list_row, locationsArrayList);
            ref = ref.child("locations");
        }else if(type.equals("food")){
            txtType.setText("Food");

            adapterFull = new FoodAdapter(this, R.layout.list_row_food, foodArrayList);
            ref = ref.child("food");
        }

        listView.setAdapter(adapterFull);

        // Create a listener on search textfield to check if what user input exists in database
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userSearch = s.toString();

                ref.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        foodArrayList.clear();
                        locationsArrayList.clear();
                        namesList.clear();

                        if(type.equals("locations")){
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                locations locs = snap.getValue(locations.class);

                                if (userSearch.equals("")) {
                                    String location = locs.getName();
                                    //Log.d("test", location);
                                    //locationsList.add(location);

                                    // Location class test
                                    locationsArrayList.add(locs);
                                    namesList.add(location);
                                } else {

                                    if (locs.getName().toLowerCase().contains(userSearch)) {
                                        String location = locs.getName();

                                        locationsArrayList.add(locs);
                                        namesList.add(location);

                                        for (locations l : locationsArrayList) {
                                            Log.d("Location name: ", l.getName());
                                        }

                                        Log.d("test", location);
                                    }
                                }
                            }
                            Collections.sort(locationsArrayList, Comparator.comparing(locations::getName));
                        }else if(type.equals("food")){
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                food foodItem = snap.getValue(food.class);

                                if (userSearch.equals("")) {
                                    String location = foodItem.getName();
                                    // Location class test
                                    foodArrayList.add(foodItem);
                                    namesList.add(location);
                                } else {
                                    if (foodItem.getName().toLowerCase().contains(userSearch)) {
                                        String location = foodItem.getName();

                                        foodArrayList.add(foodItem);
                                        namesList.add(location);

                                        for (food f : foodArrayList) {
                                            Log.d("Location name: ", f.getName());
                                        }

                                        Log.d("test", location);
                                    }

                                    Collections.sort(foodArrayList, Comparator.comparing(food::getName));
                                }
                            }
                        }

                        // Sort namesList and locationsList with Collections.sort
                        Collections.sort(namesList, String.CASE_INSENSITIVE_ORDER);

                        adapterFull.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodArrayList.clear();
                locationsArrayList.clear();
                namesList.clear();

                if(type.equals("locations")){
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        locations locs = snap.getValue(locations.class);
                        String location = locs.getName();
                        //Log.d("test", location);
                        //locationsList.add(location);

                        // Location class test
                        locationsArrayList.add(locs);
                        namesList.add(location);
                    }

                    Collections.sort(locationsArrayList, Comparator.comparing(locations::getName));
                }else if(type.equals("food")){
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        food foodItem = snap.getValue(food.class);
                        String fName = foodItem.getName();
                        //Log.d("test", location);
                        //locationsList.add(location);

                        // Location class test
                        foodArrayList.add(foodItem);
                        namesList.add(fName);
                    }

                    Collections.sort(foodArrayList, Comparator.comparing(food::getName));
                }

                // Sort namesList and locationsList with Collections.sort
                Collections.sort(namesList, String.CASE_INSENSITIVE_ORDER);


                adapterFull.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent;

                if(type.equals("locations")){
                    intent = new Intent(locationScreen.this, locationDetails.class);

                }else{
                    intent = new Intent(locationScreen.this, foodPopup.class);

                }
                intent.putExtra("name", namesList.get(position));
                startActivity(intent);

            }
        });

    }


}