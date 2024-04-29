package com.example.scannercollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.example.scannercollege.Adapter.ProductAdapter;
import com.example.scannercollege.Domain.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.Manifest;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScanItemActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private long lastScanTime = 0;
    private BarcodeDetector barcodeDetector;
    private ProductAdapter productAdapter;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private AppCompatButton submitButton;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private static final long SCAN_INTERVAL = 3000; // 3 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.scanner);
        recyclerView=findViewById(R.id.products);
        submitButton=findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitButtonClicked();
            }
        });
        // Set layout manager for RecyclerView (Choose either LinearLayoutManager or GridLayoutManager)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList,getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        // Set adapter for RecyclerView
        recyclerView.setAdapter(productAdapter);
        initialiseDetectorsAndSources();

    }
    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanItemActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanItemActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                 Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }
            
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastScanTime >= SCAN_INTERVAL) {
                        lastScanTime = currentTime;
                        String scannedBarcode = barcodes.valueAt(0).displayValue;
                        fetchProductFromBarcode(scannedBarcode);
                    }
                }
            }
        });
    }

    private void fetchProductFromBarcode(String scannedBarcode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").document(scannedBarcode).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Product product = documentSnapshot.toObject(Product.class);
                            if (product != null) {
                                productAdapter.addOrUpdateProduct(product);
                                playBeepSound();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Product not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ScanItemActivity", "Error fetching product: " + e.getMessage());
                    }
                });
    }
    private void onSubmitButtonClicked() {

        Intent intent = new Intent(ScanItemActivity.this, BillActivity.class);


        intent.putParcelableArrayListExtra("productList", (ArrayList<? extends Parcelable>) new ArrayList<>(productList));


        startActivity(intent);
    }
    private void makeLog(String message) {
        Log.d("AshuraDB",message);
    }


    private void playBeepSound() {
        if (toneGen1 != null) {
            toneGen1.startTone(ToneGenerator.TONE_PROP_BEEP, 150);
        }
    }


}