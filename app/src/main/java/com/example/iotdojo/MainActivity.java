package com.example.iotdojo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn_pick;
    private TextView txr_red, txt_green, txt_blue;

    // Access a Cloud Firestore instance from your Activity
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
    DatabaseReference red_led= ref.child("red");
    DatabaseReference green_led= ref.child("green");
    DatabaseReference blue_led= ref.child("blue");

    private int red_val = 0;
    private int green_val = 0;

    private int blue_val = 0;
    private  String color_btn= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn_pick = findViewById(R.id.btn_picker);
        txr_red = findViewById(R.id.txt_red);
        txt_green = findViewById(R.id.txt_green);
        txt_blue = findViewById(R.id.txt_blue);

        red_led.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                red_val= dataSnapshot.getValue(Integer.class);
                txr_red.setText(String.valueOf(red_val));
                setBtn_pick(red_val, green_val, blue_val);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error to load value Red", Toast.LENGTH_SHORT).show();
            }
        });

        green_led.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                green_val= dataSnapshot.getValue(Integer.class);

                txt_green.setText(String.valueOf(green_val));
                setBtn_pick(red_val, green_val, blue_val);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error to load value Red", Toast.LENGTH_SHORT).show();
            }
        });

        blue_led.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blue_val = dataSnapshot.getValue(Integer.class);
                txt_blue.setText(String.valueOf(blue_val));
                setBtn_pick(red_val, green_val, blue_val);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error to load value Red", Toast.LENGTH_SHORT).show();
            }
        });




        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ColorPickerDialogBuilder
                        .with(MainActivity.this)
                        .setTitle("Choose color")
                        .initialColor(0xffffff00)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                               // Toast.makeText(MainActivity.this , "onColorSelected: 0x" + Integer.toHexString(selectedColor).substring(2), Toast.LENGTH_SHORT).show();


                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                               // changeBackgroundColor(selectedColor);
                                btn_pick.setBackgroundColor(selectedColor);

                        //        Toast.makeText(MainActivity.this, ""+ Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
                                String color = Integer.toHexString(selectedColor).substring(2);


                                String red = color.substring(0,2);
                                String green = color.substring(2,4);
                                String blue = color.substring(4,6);

                              //  Toast.makeText(MainActivity.this, red + "  "+ green + " "+ blue, Toast.LENGTH_LONG).show();

                                 red_val = Integer.parseInt(red,16);
                                 green_val = Integer.parseInt(green,16);

                                 blue_val = Integer.parseInt(blue,16);


                                txr_red.setText(String.valueOf(red_val));
                                txt_green.setText(String.valueOf(green_val));
                                txt_blue.setText(String.valueOf(blue_val));

                                red_led.setValue(red_val);
                                green_led.setValue(green_val);
                                blue_led.setValue(blue_val);


                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();


            }
        });


    }


    void setBtn_pick(int red, int green, int blue){

        color_btn = "ff" + Integer.toHexString(red)+ Integer.toHexString(green)+Integer.toHexString(blue);


    }
}

