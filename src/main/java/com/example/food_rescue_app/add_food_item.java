package com.example.food_rescue_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class add_food_item extends AppCompatActivity {

    EditText name, details, quantity, date, time, location;
    ImageView image;
    Food_Item food_item;

    public static final int PICK_IMAGE = 100;
    public static final int PERMISSION_CODE = 1001;
    Uri imageUri;

    DatePickerDialog.OnDateSetListener setListener;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        image = findViewById(R.id.food_item_image);
        name = findViewById(R.id.food_item_name);
        details = findViewById(R.id.food_item_details);
        quantity = findViewById(R.id.food_item_qty);
        date = findViewById(R.id.food_item_date);
        time = findViewById(R.id.food_item_time);
        location = findViewById(R.id.food_item_loc);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(add_food_item.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String dt = day+"/"+month+"/"+year;
                        date.setText(dt);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

    }

    public void Add_Food(View view) {
        Register_Food();
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    private void Register_Food() {
        String im,na, det, qty, dt, tm, loc;

        na = name.getText().toString();
        det = details.getText().toString();
        qty = quantity.getText().toString();
        dt = date.getText().toString();
        tm = time.getText().toString();
        loc = location.getText().toString();

        if (na.isEmpty())
        {
            name.setError("Please provide name of the food item!");
            name.requestFocus();
            return;
        }

        if (det.isEmpty())
        {
            details.setError("Please provide details of food item!");
            details.requestFocus();
            return;
        }

        if (qty.isEmpty())
        {
            quantity.setError("Please provide quantity!");
            quantity.requestFocus();
            return;
        }

        if (dt.isEmpty())
        {
            date.setError("Please provide date for pickup!");
            date.requestFocus();
            return;
        }

        if (tm.isEmpty())
        {
            time.setError("Please provide pickup time!");
            time.requestFocus();
            return;
        }

        if (loc.isEmpty())
        {
            location.setError("Please provide location!");
            location.requestFocus();
            return;
        }

        food_item = new Food_Item(na,det,qty,dt,tm,loc);
        upload_image();

        Intent intent = new Intent(this, home.class);
        startActivity(intent);

    }

    private void upload_image() {

        final ProgressDialog pd =new ProgressDialog(this);
        pd.setTitle("Uploading now..");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference image_ref = storageReference.child("images/"+randomKey);

        image_ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //pd.dismiss();
                        image_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                food_item.setImage(uri.toString());
                                Toast.makeText(add_food_item.this, "Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                                rootNode = FirebaseDatabase.getInstance();
                                reference = rootNode.getReference("Food_Item");

                                reference.child("Food_Item").push().setValue(food_item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(add_food_item.this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(add_food_item.this, "Error: Item not added!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double percentageUpload = (100.00 * (snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                        pd.setMessage("Percentage: "+(int) percentageUpload +"%");
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void Upload_image(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED){
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);

            }else{
                
                pickImage();

            }
        }else{

            pickImage();
            
        }

    }

    private void pickImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,
                "Sellect Picture"), PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickImage();
                }else{
                    Toast.makeText(this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}