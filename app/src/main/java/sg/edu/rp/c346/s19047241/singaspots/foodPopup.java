package sg.edu.rp.c346.s19047241.singaspots;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;

public class foodPopup extends Activity {

    ImageView imgThumbnail;
    TextView txtIngredients, txtMarinate, txtPrep;
    Button btnFav;
    YouTubePlayerView vPlayer;

    String key, url, ingred, marinate, prep, vidId = "";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_pop);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        String foodName = getIntent().getStringExtra("name");

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.8));
        setTitle(foodName);

        imgThumbnail = findViewById(R.id.imageThumbnailPop);
        txtIngredients = findViewById(R.id.textIngredients);
        txtMarinate = findViewById(R.id.textMarinate);
        txtPrep = findViewById(R.id.textPreparation);
        btnFav = findViewById(R.id.buttonFavouriteFood);
        vPlayer = findViewById(R.id.videoPlayer);

        // store selected food
        food foodItem = new food();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("food");
        DatabaseReference refFav = FirebaseDatabase.getInstance().getReference().child("favourites").child("food");

        ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtIngredients.setText("");
                txtMarinate.setText("");
                txtPrep.setText("");

                for(DataSnapshot snap : snapshot.getChildren()){
                    food foods = snap.getValue(food.class);

                    if(foods.getName().equals(foodName)){
                        ingred = foods.getIngredients();
                        marinate = foods.getMarinade();
                        prep = foods.getMethods();

                        key = snap.getKey();

                        String[] ingList = ingred.split("//");
                        for(int i = 0; i < ingList.length; i++){

                            if(i == ingList.length - 1){
                                txtIngredients.append(String.format("%s", ingList[i]));
                            }else{
                                txtIngredients.append(String.format("%s\n", ingList[i]));
                            }
                        }

                        String[] marList = marinate.split("//");
                        for(int i = 0; i < marList.length; i++){
                            if(i == marList.length -1){
                                txtMarinate.append(String.format("%s", marList[i]));
                            }else{
                                txtMarinate.append(String.format("%s\n", marList[i]));
                            }
                        }

                        String[] methodList = prep.split("//");
                        for(int i = 0; i < methodList.length; i++){


                            if(i == methodList.length - 1){
                                txtPrep.append(String.format("%d) %s", i+1, methodList[i]));
                            }else{
                                txtPrep.append(String.format("%d) %s\n\n", i+1, methodList[i]));
                            }
                        }

                        if(foods.isFavourite()){
                            btnFav.setBackgroundColor(Color.MAGENTA);
                            btnFav.setText("Favourited");
                        }else{
                            btnFav.setBackgroundColor(Color.BLUE);
                            btnFav.setText("Favourite");
                        }

                        url = foods.getThumbnail();
                        LoadImage loadImage = new LoadImage(imgThumbnail);
                        loadImage.execute(url);

                        vidId = foods.getVidId();
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

                    FirebaseDatabase.getInstance().getReference().child("favourites").child("food").child(key).removeValue();
                }else{
                    ref.child(key).child("favourite").setValue(true);
                    btnFav.setBackgroundColor(Color.MAGENTA);
                    btnFav.setText("Favourited");

                    foodItem.setName(getTitle().toString());
                    foodItem.setIngredients(ingred);
                    foodItem.setMarinade(marinate);
                    foodItem.setMethods(prep);
                    foodItem.setFavourite(true);
                    foodItem.setThumbnail(url);
                    foodItem.setVidId(vidId);

                    refFav.child(key).setValue(foodItem);
                }
            }
        });

        vPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                youTubePlayer.cueVideo(vidId, 0);

            }
        });

        vPlayer.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                vPlayer.enterFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                vPlayer.exitFullScreen();
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
