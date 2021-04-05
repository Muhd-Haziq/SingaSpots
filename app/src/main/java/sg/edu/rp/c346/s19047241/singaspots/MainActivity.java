package sg.edu.rp.c346.s19047241.singaspots;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView txtQuote;
    Button btnLocations, btnFood, btnFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtQuote = findViewById(R.id.textQuote);
        btnLocations = findViewById(R.id.buttonLocations);
        btnFood = findViewById(R.id.buttonFood);
        btnFavourite = findViewById(R.id.buttonFav);

        // Swapping screens to the locations page
        btnLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocations();
            }
        });

        // Swapping screens to the food page
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFood();
            }
        });

        // Swapping screens to the locations page
        btnFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavourite();
            }
        });


    }

    public void openLocations() {
        Intent intent = new Intent(this, locationScreen.class);
        intent.putExtra("type", "locations");
        startActivity(intent);
    }

    public void openFood() {
        Intent intent = new Intent(this, locationScreen.class);
        intent.putExtra("type", "food");
        startActivity(intent);
    }

    public void openFavourite() {
        Intent intent = new Intent(this, favouritesScreen.class);
        startActivity(intent);
    }

}