package com.example.localizacion2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {


    LocationManager manager;
    ListenerPosicion listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (validaPermisos()) {
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            listener = new ListenerPosicion();
            long tiempo = 5000;
            float distancia = 10;


            //manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, tiempo, distancia, listener);
            String codigo = "com.example.localizacion2.PICHICHU";
            Intent intent = new Intent(codigo);
            PendingIntent pi = PendingIntent.getBroadcast(this, -1, intent, 0);
            //double latitud = -33.1501297;
            double latitud = -33.67327862;
            //double longitud = -66.308107700206;
            double longitud = -65.44101707;
            float radio = 500f;
            long caducidad = -1;
            IntentFilter filtro = new IntentFilter(codigo);
            registerReceiver(new ReceptorProximidad(), filtro);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }

            manager.addProximityAlert(latitud, longitud, radio, caducidad, pi);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        manager.removeUpdates(listener);
    }

    private boolean validaPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION))) {
            cargarDialogoRecomendacion();
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                solicitarPermisosManual();
            }
        }

    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(MainActivity.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 100);
            }
        });
        dialogo.show();
    }

    class ListenerPosicion implements LocationListener{
        public void onLocationChanged(Location location){
            Toast.makeText(getApplicationContext(), "Lat: "+location.getLatitude() + "Long: "+location.getLongitude(), Toast.LENGTH_LONG).show();
        }
        public void onProviderDisabled(String provider){
            Toast.makeText(getApplicationContext(), "El proveedor ha sido desconectado", Toast.LENGTH_LONG).show();
        }
        public void onProviderEnabled(String provider){
            Toast.makeText(getApplicationContext(), "El proveedor ha sido conectado", Toast.LENGTH_LONG).show();
        }
        public void onStatusChanged(String provider, int status, Bundle extras){
            Toast.makeText(getApplicationContext(), "Cambio en el estado del proveedor", Toast.LENGTH_LONG).show();
        }
    }


}
