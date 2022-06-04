package com.crzaor.zaply_shop.usescases.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.crzaor.zaply_shop.databinding.ActivityOrderBinding;
import com.crzaor.zaply_shop.model.Product;
import com.crzaor.zaply_shop.usescases.main.MainActivity;
import com.crzaor.zaply_shop.provider.DBController;
import com.crzaor.zaply_shop.util.Notificator;
import com.crzaor.zaply_shop.util.SessionManager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements Serializable {

    private ActivityOrderBinding binding;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private OrderRecyclerAdapter adapter;
    private DBController dbController = new DBController();
    private List<EditText> camps;
    private double total_cost = 0;
    List<Product> products = new ArrayList<>();
//    private NotificationManager notificationManager;
    private static Notificator notificator = new Notificator();
    private static final String channelId = "com.crzaor.zaply_shop.notificaciones";
    private static int notificacionId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManager sessionManager = new SessionManager(this);

//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        createNotificationChannel(channelId, "Zaply Shop");

        Intent intent = getIntent();

        try {
            products = (List<Product>) intent.getSerializableExtra("products");
        }catch (Exception e){
            products = dbController.getCardProducts(sessionManager.getUserDetail().get("USER_ID"));
        }

        adapter = new OrderRecyclerAdapter(products);
        recyclerView = binding.recyclerProductsOrder;
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        EditText name = binding.Name;
        EditText surnames = binding.surnames;
        EditText direction = binding.direction;
        EditText postcode = binding.postcode;
        EditText province = binding.province;
        EditText city = binding.city;
        EditText phone = binding.phoneNumber;
        EditText numCard = binding.numCard;
        EditText nameCard = binding.nameCard;
        EditText month = binding.month;
        EditText year = binding.year;
        EditText cvv = binding.cvv;
        camps = Arrays.asList(name,surnames, direction, postcode, province, city, phone, numCard, nameCard, month, year, cvv);
        binding.backOrder.setOnClickListener(v -> {finish();});

        total_cost = 0;
        int totalProducts = 0;
        for(Product p: products){
            int count = p.getCount();
            total_cost += (p.getPrice()*count);
            totalProducts += count;
        }

        LocalDate localDate = LocalDate.now();
        LocalDate finalDate = LocalDate.from(localDate.plusDays(7));
        binding.dateArrival.setText(localDate.getDayOfMonth()+ " "+ localDate.getMonth().toString().toLowerCase()
                + " - "+finalDate.getDayOfMonth()+" "+finalDate.getMonth().toString().toLowerCase());
        binding.countProducts.setText(Integer.toString(totalProducts));
        binding.orderTotalPrice.setText(String.format("%.2f", total_cost));
        binding.backOrder.setOnClickListener(v ->{
            finish();
        });

        binding.btnBuy.setOnClickListener(v ->{
            if(verifyContent()){
                String email = sessionManager.getUserDetail().get("USER_ID");
                int user_id = (int) dbController.getUser(email).getId();

                ArrayList<Integer> id_products = new ArrayList<>();
                for (Product p: products){id_products.add( p.getId());}

                String name_reciever,Sdirection,Sphone,Snumcard,titular,date,Scvv;
                name_reciever = name.getText().toString()+" "+surnames.getText().toString();
                Sdirection = direction.getText().toString()+", " +
                        postcode.getText().toString()+", "+
                        province.getText().toString()+", "+
                        city.getText().toString();
                Sphone = phone.getText().toString();
                Snumcard = numCard.getText().toString();
                titular = nameCard.getText().toString();
                date = month.getText().toString()+"/"+year.getText().toString();
                Scvv = cvv.getText().toString();

                boolean inserted = dbController.insertOrder(user_id,name_reciever,Sdirection,Sphone,Snumcard,titular,date,Scvv,id_products,total_cost,"Pendiente");

                if(inserted){
                    notificator.sendNotification(v,"Compra realizada","¡Gracias por su compra, lo recibirá pronto!");
                    dbController.updateCard(email,new ArrayList<Integer>());
                    Intent intent2 = new Intent(this, MainActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }

    private boolean verifyContent(){
        for(EditText camp: camps){
            String text = camp.getText().toString().replace(" ","");
            if(TextUtils.isEmpty(text)){
                camp.setError("El campo no puede estar vacío");
                return false;
            }
        }

        if(binding.numCard.getText().toString().replace(" ","").length() < 16){
            binding.numCard.setError("Número de tarjeta no válido");
            return false;
        }

        if(binding.postcode.getText().toString().replace(" ","").length() < 5){
            binding.postcode.setError("Código postar no válido");
            return false;
        }

        if(binding.phoneNumber.getText().toString().replace(" ","").length() < 9){
            binding.phoneNumber.setError("Número no válido");
            return false;
        }

        return true;
    }
//    protected void createNotificationChannel(String channelId, String name) {
//        int importancia = NotificationManager.IMPORTANCE_DEFAULT;
//        NotificationChannel channel = new NotificationChannel(channelId, name, importancia);
//        channel.setName("Zaply Shop notification");
//        channel.enableVibration(true);
//        channel.setVibrationPattern(new long[]{100, 200, 300});
//        channel.enableLights(true);
//        channel.setLightColor(Color.BLUE);
//
//        notificationManager.createNotificationChannel(channel);
//    }
//
//    public void sendNotification(View v, String title, String message){
//        Notification.Builder notificacion = new Notification.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setNumber(3)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setChannelId(channelId);
//        notificationManager.notify(notificacionId, notificacion.build());
//        notificacionId++;
//    }
}