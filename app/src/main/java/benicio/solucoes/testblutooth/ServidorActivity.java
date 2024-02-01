package benicio.solucoes.testblutooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

import benicio.solucoes.testblutooth.databinding.ActivityServidorBinding;

public class ServidorActivity extends AppCompatActivity {

    private ActivityServidorBinding mainBinding;
    private static final String TAG = "mayara";
    ServerSocket serverSocket = null;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityServidorBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mainBinding.textIp.setText("Ip: " + getIPAddress());

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(4203);

                while (true) {
                    try {
                        if ( !serverSocket.isClosed()){
                            Socket socket = serverSocket.accept(); // Espera por conexões
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                            Log.d("mayara", "ouvindo...");

                            final String message = reader.readLine(); // Recebe a string do cliente

                            runOnUiThread(() -> mainBinding.textMensagem.setText("String recebida do cliente: " + message));

                            socket.close();
                        }
                    } catch (IOException e) {
                        Log.d(TAG, "Erro ao aceitar conexão: " + e.getMessage());
                    }
                }

            } catch (BindException e) {
                Log.d(TAG, "Porta já está em uso, continuando a ouvir...");
            } catch (IOException e) {
                Log.d(TAG, "Erro ao criar o servidor: " + e.getMessage());
            }
        }).start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                Log.d(TAG, "onDestroy: fechado" );
            }
        } catch (IOException e) {
            Log.e(TAG, "Erro ao fechar o servidor: " + e.getMessage());
        }
    }

    public static String getIPAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!addr.isLoopbackAddress() && !addr.isLinkLocalAddress() && addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}