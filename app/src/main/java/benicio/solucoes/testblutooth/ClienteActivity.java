package benicio.solucoes.testblutooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import benicio.solucoes.testblutooth.databinding.ActivityClienteBinding;
import benicio.solucoes.testblutooth.databinding.ActivityServidorBinding;

public class ClienteActivity extends AppCompatActivity {

    private ActivityClienteBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityClienteBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        mainBinding.buttonSend.setOnClickListener(view -> {

            String ip = mainBinding.edtIp.getText().toString().trim();
            String message = mainBinding.edtMsg.getText().toString().trim();

            new Thread(() -> {
                try {
                    Socket socket = new Socket(ip, 4203); // IP do servidor
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(message);
                    socket.close();
                } catch (IOException e) {
                    Log.d("mayara", "onCreate: " + e.getMessage());
                    e.printStackTrace();
                }
            }).start();

        });
    }
}