package com.example.scannercollege;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.scannercollege.Domain.UserData;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ThankYouActivity extends AppCompatActivity {
private AppCompatButton exit,newBill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        String filePath = getIntent().getStringExtra("pdfFilePath");
//        try {
//            sendEmailWithAttachment(filePath);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
        exit=findViewById(R.id.exitButton);
        newBill=findViewById(R.id.billButton);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exit the application
                finishAffinity(); // Finish this activity and all parent activities
                System.exit(0); // Exit the application
            }
        });
        newBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ThankYouActivity.this, SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendEmailWithAttachment(String filePath) throws MessagingException {
     try{
        final String senderEmail = "kushvahaabhisek33@gmail.com";
        final String password = "Ashura#143";
        UserData userData = UserData.getInstance();
        String mail = userData.getEmail();
        final String receiverEmail = mail;

        // Defining the SMTP server host
        final String stringHost = "smtp.gmail.com";
        java.util.Properties properties = System.getProperties();
        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Creating a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        // Creating a MimeMessage
        MimeMessage mimeMessage = new MimeMessage(session);

        // Adding the recipient's email address
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

        // Setting the subject and message content
        mimeMessage.setSubject("Bill");
        mimeMessage.setText("Aapka bill");

        // Creating MimeBodyPart for attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filePath);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName("attachment.pdf"); // Set the file name for attachment

        // Creating a Multipart object to combine message and attachment
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(attachmentPart);

        // Set the Multipart object as the content of the MimeMessage
        mimeMessage.setContent(multipart);

        // Sending the email
        Transport.send(mimeMessage);

        // Displaying a toast message indicating that the email was sent successfully
        Toast.makeText(this, "Sent Successfully", Toast.LENGTH_SHORT).show();
    } catch (MessagingException e) {
        // Handling messaging exception
        Log.e("Email", "Failed to send email", e);
        Toast.makeText(this, "Failed to send email", Toast.LENGTH_SHORT).show();
    }

    }
}