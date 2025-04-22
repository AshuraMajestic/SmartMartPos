package com.example.scannercollege;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scannercollege.Domain.Product;
import com.example.scannercollege.Domain.UserData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {

    private ArrayList<Product> productList;
    private LinearLayout productListLayout;
    private TextView price, gstPrice, totalPrice,billNumber,customerName;
    private Button submitButton;
    private ScrollView layoutToPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        productList = getIntent().getParcelableArrayListExtra("productList");
        price = findViewById(R.id.price);
        gstPrice = findViewById(R.id.gstPrice);
        totalPrice = findViewById(R.id.totalPrice);
        billNumber=findViewById(R.id.billNumber);
        customerName=findViewById(R.id.customerName);
        layoutToPDF = findViewById(R.id.whiteBill);
        productListLayout=findViewById(R.id.productList);
        submitButton=findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();

            }
        });
        UserData userData = UserData.getInstance();
        String name = userData.getName();
        customerName.setText(name);
        for (Product product : productList) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);


            TextView labelTextView = new TextView(this);
            labelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            labelTextView.setText(product.getName());
            rowLayout.addView(labelTextView);

            TextView quantityTextView = new TextView(this);
            quantityTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            quantityTextView.setText(product.getQuantity());
            quantityTextView.setGravity(Gravity.CENTER);
            rowLayout.addView(quantityTextView);

            TextView valueTextView = new TextView(this);
            valueTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            valueTextView.setText("₹"+product.getPrice());
            valueTextView.setTextColor(Color.BLACK);
            valueTextView.setGravity(Gravity.END);
            rowLayout.addView(valueTextView);

            // Add the rowLayout to the productListLayout
            productListLayout.addView(rowLayout);
        }
        double subtotal = calculateSubtotal(productList);
        price.setText("₹" + String.format("%.2f", subtotal)); // Format subtotal to show only last two decimal places

        double gst = 0.18 * subtotal;
        gstPrice.setText("₹" + String.format("%.2f", gst)); // Format gst to show only last two decimal places

        double total = subtotal + gst;
        totalPrice.setText("₹" + String.format("%.2f", total)); // Format total to show only last two decimal places


    }

    private void generatePDF() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(layoutToPDF.getWidth(), layoutToPDF.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        layoutToPDF.draw(canvas);
        document.finishPage(page);

        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filePath = directoryPath + "/generated_pdf.pdf";
        File file = new File(filePath);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF Saved to: " + filePath, Toast.LENGTH_SHORT).show();
            makeLog(filePath);

            // Forward the PDF to the next activity
            Intent intent = new Intent(BillActivity.this, ThankYouActivity.class);
            intent.putExtra("pdfFilePath", filePath);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        document.close();
    }


    private double calculateSubtotal(ArrayList<Product> productList) {
        double subtotal = 0;
        for (Product product : productList) {
            double productPrice = Double.parseDouble(product.getPrice());
            int productQuantity = Integer.parseInt(product.getQuantity());
            subtotal += productPrice * productQuantity;
        }
        return subtotal;
    }



    private void makeLog(String s) {
        Log.d("AshuraDB",s);
    }
}