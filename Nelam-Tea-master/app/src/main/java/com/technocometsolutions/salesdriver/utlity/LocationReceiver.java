package com.technocometsolutions.salesdriver.utlity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;






public class LocationReceiver extends BroadcastReceiver  {


    @Override
    public void onReceive(Context context, Intent intent) {
        // updatelocation();

       /* double latitude= Double.parseDouble(intent.getStringExtra("latitude"));
        double longitude= Double.parseDouble(intent.getStringExtra("longitude"));


        Log.d("latitude12345",""+latitude);
        Log.d("longitude12345",""+longitude);
*/


        Log.i(LocationReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");

        context.startService(new Intent(context,LocationReceiverServices.class));


        Log.d("context123", "" + context);
        Log.d("context1234", "" + intent);
    }

}
       /*     if(intent!=null)
            {
                    String action=intent.getAction();
                if(UPDATE_LOCATION.equals(action))
                {
                    LocationResult result=LocationResult.extractResult(intent);
                    if(result!=null)
                    {
                        Location location=result.getLastLocation();
                        String location_string=new StringBuilder(""+location.getLatitude()).append("/").append(""+location.getLongitude()).toString();
                        Log.d("location_string",""+location_string);

                        try {
                            Double latitude=location.getLatitude();
                            Double longitude=location.getLongitude();

                            Log.d("latitude",""+latitude);
                            Log.d("longitude",""+longitude);

//                            MainActivity.getInstance().updatedText(location_string);

                        }
                        catch (Exception ex)
                        {

                        }
                    }


                }
            }
*/






/*
    private void updatelocation() {
        buildlocatinRequest();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getpendingIntent());
    }

    private PendingIntent getpendingIntent() {
        Intent intent=new Intent();
        intent.setAction(UPDATE_LOCATION);
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildlocatinRequest() {



        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(3000)
                .setSmallestDisplacement(10f);
    }*/

