package sg.edu.rp.c346.s19047241.singaspots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class locationDetails extends AppCompatActivity {

    TextView txtName, txtDescription;
    ImageView imgThumbnail;
    Button btnFav;

    String key = "";
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);

        txtName = findViewById(R.id.textName);
        txtDescription = findViewById(R.id.textDescription);
        imgThumbnail = findViewById(R.id.imageThumbnail);
        btnFav = findViewById(R.id.buttonFavourite);

        String name = getIntent().getStringExtra("name");

        // store selected location
        locations loc = new locations();

        //Toast.makeText(locationDetails.this, (String) name, Toast.LENGTH_SHORT).show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("locations");
        DatabaseReference refFav = FirebaseDatabase.getInstance().getReference().child("favourites").child("locations");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    locations locs = snap.getValue(locations.class);

                    if(locs.getName().equals(name)){
                        txtName.setText(locs.getName());
                        txtDescription.setText(locs.getDescription());
                        key = snap.getKey();

                        if(locs.isFavourite()){
                            btnFav.setBackgroundColor(Color.MAGENTA);
                            btnFav.setText("Favourited");
                        }else{
                            btnFav.setBackgroundColor(Color.BLUE);
                            btnFav.setText("Favourite");
                        }

                        url = locs.getThumbnail();
                        LoadImage loadImage = new LoadImage(imgThumbnail);
                        loadImage.execute(url);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFav.getText().equals("Favourited")){

                    ref.child(key).child("favourite").setValue(false);
                    btnFav.setBackgroundColor(Color.BLUE);
                    btnFav.setText("Favourite");

                    FirebaseDatabase.getInstance().getReference().child("favourites").child("locations").child(key).removeValue();
                }else{
                    ref.child(key).child("favourite").setValue(true);
                    btnFav.setBackgroundColor(Color.MAGENTA);
                    btnFav.setText("Favourited");

                    loc.setName(txtName.getText().toString());
                    loc.setDescription(txtDescription.getText().toString());
                    loc.setFavourite(true);
                    loc.setThumbnail(url);

                    refFav.child(key).setValue(loc);

                }
            }
        });
    }


    static class LoadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public LoadImage(ImageView imgThumbnail){
            this.imageView = imgThumbnail;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlLink = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream inputStream = new java.net.URL(urlLink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}