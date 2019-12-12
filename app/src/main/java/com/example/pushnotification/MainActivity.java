package com.example.pushnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNama, editTextMakanan, editTextJumlah, editTextAlamat, editTextNotelp;
    private String nama, makanan, jumlah, alamat, notelp;
    private DatabaseReference mDatabase;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the database in firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // define the textview
        editTextNama = findViewById(R.id.edit_nama);
        editTextMakanan = findViewById(R.id.edit_makanan);
        editTextJumlah = findViewById(R.id.edit_jumlah_makanan);
        editTextAlamat = findViewById(R.id.edit_alamat);
        editTextNotelp = findViewById(R.id.edit_no_telp);
    }

    public void onSubmit(View view) {
        nama = editTextNama.getText().toString();
        makanan = editTextMakanan.getText().toString();
        jumlah = editTextJumlah.getText().toString();
        alamat = editTextAlamat.getText().toString();
        notelp = editTextNotelp.getText().toString();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Pemesanan Makanan") // title for notification
                .setContentText("Pemesanan berhasil!")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());

        DatabaseReference newData = mDatabase.child("data");
        newData.child("notelp").setValue(notelp);
        newData.child("alamat").setValue(alamat);
        newData.child("jumlah").setValue(jumlah);
        newData.child("makanan").setValue(makanan);
        newData.child("nama").setValue(nama).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, mBuilder.build());

            }
        });
    }
}
