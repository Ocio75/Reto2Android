package com.example.chatbien;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.chatbien.objetos.Mensaje;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.twilio.voice.Call;
import com.twilio.voice.CallException;
import com.twilio.voice.ConnectOptions;
import com.twilio.voice.Voice;
import com.twilio.voice.Call.Listener;
public class menu_chat extends AppCompatActivity implements View.OnClickListener {
    private Button btAjustes,btEnviar;
    private EditText etMensaje;
    private String usuario;
    private Socket socket;
    private PrintWriter output;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private ListView lvChat;
    private List<Mensaje> listaMensajes;
    private AdaptadorMensajes adaptadorMensajes;
    private Button btnEmoticonos;
    private String dniUsuario;
    private static final int MIC_PERMISSION_REQUEST_CODE = 123;
    private String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chat);
        btAjustes=findViewById(R.id.btAjustes);
        btEnviar=findViewById(R.id.btEnviar);
        etMensaje= findViewById(R.id.etMensaje);
        btAjustes.setOnClickListener(this);
        btEnviar.setOnClickListener(this);
        lvChat = findViewById(R.id.lvChat);
        listaMensajes = new ArrayList<>();
        adaptadorMensajes = new AdaptadorMensajes(this, listaMensajes);
        lvChat.setAdapter(adaptadorMensajes);
        dniUsuario = "2099";
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("USUARIO")) {
            usuario = extras.getString("USUARIO");
        } else {
            usuario = "Usuario";
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Si el permiso no está concedido, solicitarlo al usuario
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION_REQUEST_CODE);
        }
        String twilioPhoneNumber = "tu_número_de_teléfono_de_Twilio_o_el_número_de_destino";
        //Call call = Voice.call(context, "client:tu_nombre_de_usuario", twilioPhoneNumber, null);
        btnEmoticonos = findViewById(R.id.btnEmoticonos);
        btnEmoticonos.setOnClickListener(this);

        // Iniciar la conexión SSL
        new Thread(() -> {
            InputStream inputStream = getResources().openRawResource(R.raw.certificado);
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null); // Inicializar el almacén de claves
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate certificado = certificateFactory.generateCertificate(inputStream);
                keyStore.setCertificateEntry("1234567", certificado); // Agregar el certificado al almacén
                // Configurar el contexto SSL
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

                // Crear el socket SSL
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                socket = sslSocketFactory.createSocket("10.0.2.2", 12345);

                output = new PrintWriter(socket.getOutputStream(), true);
                output.println(dniUsuario);
                Scanner input = new Scanner(socket.getInputStream());

                while (true) {
                    if (input.hasNext()) {
                        String message = input.nextLine();
                        //if (message.startsWith(dniUsuario)) {
                            runOnUiThread(() -> {
                                displayMessage(message, false);
                           });
                        //}
                    }
                }
            } catch (IOException e) {
                Log.e("ClientAsyncTask", "Error de conexión: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                Log.e("ClientAsyncTask", "Error al configurar SSL: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        btEnviar.setOnClickListener(view -> {
            sendMessage();
        });
    }
    private void fetchAccessToken() {
        String tokenUrl = "http://10.0.2.2:12345/token";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(tokenUrl).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String token = response.body().string();
                    runOnUiThread(() -> {
                        makeCall(token);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                Log.e("fetchAccessToken", "Error al obtener el token: " + e.getMessage());
            }
        });
    }
    private void requestTokenFromServer() {
        new Thread(() -> {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // Envía un comando específico para solicitar el token
                out.println("COMMAND:GET_TOKEN");
                Scanner in = new Scanner(socket.getInputStream());
                if (in.hasNextLine()) {
                    final String token = in.nextLine();
                    runOnUiThread(() -> makeCall(token)); // Usa el token para hacer la llamada
                }
            } catch (IOException e) {
                Log.e("requestTokenFromServer", "Error al solicitar el token: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.llamada) {
            requestTokenFromServer(); // Solicita el token del servidor
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void makeCall(String accessToken) {
        Map<String, String> params = new HashMap<>();
        params.put("to", "+1234567890");

        Listener listener = new Listener() {
            @Override
            public void onConnected(Call call) {
                Log.d("Twilio", "Llamada conectada");
            }

            @Override
            public void onReconnecting(Call call, CallException callException) {

            }

            @Override
            public void onReconnected(Call call) {

            }

            @Override
            public void onDisconnected(Call call, CallException e) {
                Log.d("Twilio", "Llamada desconectada");
            }

            @Override
            public void onConnectFailure(Call call, CallException e) {
                Log.e("Twilio", "Error al conectar la llamada");
            }

            @Override
            public void onRinging(Call call) {

            }
        };
        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                .params(params) // tus parámetros aquí
                .build();
        // Verifica que el Contexto usado aquí sea adecuado; puede que necesites usar getApplicationContext() o referenciar a tu Activity directamente
        Call call = Voice.connect(menu_chat.this, connectOptions, listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuchat, menu);
        return true;
    }

    private void sendMessage() {
        String message = etMensaje.getText().toString();
        if (!message.isEmpty() && output != null) {
            executor.execute(() -> {
                try {
                    output.println( message);
                    runOnUiThread(() -> {
                        displayMessage(dniUsuario + ": " + message, true);
                        adaptadorMensajes.notifyDataSetChanged();
                    });
                } catch (Exception e) {
                    Log.e("SendMessageAsyncTask", "Error al enviar mensaje: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            etMensaje.setText("");
            Log.d("ChatActivity", "Mensaje enviado correctamente.");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btAjustes)) {
            Intent i = new Intent(this, menu_configuracion.class);
            startActivity(i);
            finish();
        } else if (v.equals(btEnviar)) {
            sendMessage();
        } else if (v.equals(btnEmoticonos)) {
            abrirVentanaEmoticonos();
        }
    }

    class AdaptadorMensajes extends ArrayAdapter<Mensaje> {
        AppCompatActivity appCompatActivity;

        AdaptadorMensajes(AppCompatActivity context, List<Mensaje> listaMensajes) {
            super(context, R.layout.mensa_propio, listaMensajes);
            appCompatActivity = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = appCompatActivity.getLayoutInflater();
            View item = inflater.inflate(R.layout.mensa_propio, null);
            TextView tvMensa = item.findViewById(R.id.tvMensaj);
            TextView tvHora = item.findViewById(R.id.tvHor);
            Calendar calendar = Calendar.getInstance();
            java.util.Date fecha = calendar.getTime();
            Time horaActual = new Time(fecha.getTime());
            tvHora.setText((horaActual.getHours()+1)+":"+horaActual.getMinutes());
            tvMensa.setText(listaMensajes.get(position).getMensaje());
            return item;
        }
    }

    private void displayMessage(String message, boolean isSentByUser) {
        if (!isSentByUser) {
            listaMensajes.add(new Mensaje(1111, message));
            adaptadorMensajes.notifyDataSetChanged();
        }
    }

    private void abrirVentanaEmoticonos() {
        String[] emojis = {
                "😊", "😢", "😄", "😛", "😉", "❤️", "😮", "😐", "😕",
                "🍔", "🍟", "🍕", "🌮", "🌯", "🍣", "🍱", "🍜", "🍝", "🍛",
                "🍚", "🍙", "🍘", "🍢", "🍡", "🍧", "🍨", "🍦", "🍩", "🍰",
                "🎂", "🧁", "🍪", "🍫", "🍬", "🍭", "🍮", "🍯", "☕", "🍵",
                "🍶", "🍹", "🍺", "🍻", "🍷", "🥂", "🥃", "🍸", "🍾", "🥄",
                "🍴", "🍽️", "🥢", "🥡", "🥤", "🍼", "🥧", "🍦", "🍨", "🍧",
                "🎃", "🍁", "🍂", "🍃", "☔", "❄️", "⛄", "🌬️", "🌧️", "⛈️",
                "🌦️", "🌥️", "🌤️", "☀️", "🌞", "🌈", "🌪️", "🌫️", "🌊", "🍽️",
                "🌰", "🍖", "🍗", "🥓", "🥩", "🍕", "🍟", "🍔", "🍦", "🍧"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un emoji");
        builder.setItems(emojis, (dialog, which) -> {
            String emojiSeleccionado = emojis[which];
            insertarEmojiEnMensaje(emojiSeleccionado);
        });
        builder.show();
    }

    private void insertarEmojiEnMensaje(String emoji) {
        String mensajeActual = etMensaje.getText().toString();
        String nuevoMensaje = mensajeActual + " " + emoji;
        etMensaje.setText(nuevoMensaje);
        etMensaje.setSelection(nuevoMensaje.length());
    }
}
