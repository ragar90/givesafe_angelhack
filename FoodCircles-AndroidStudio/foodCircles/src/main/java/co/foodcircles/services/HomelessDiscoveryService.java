package co.foodcircles.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Push;
import com.gimbal.android.Visit;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import co.foodcircles.util.GimbalDAO;
import co.foodcircles.util.GimbalEvent;

public class HomelessDiscoveryService extends Service {
    private static final int MAX_NUM_EVENTS = 100;
    private BeaconEventListener beaconSightingListener;
    private BeaconManager beaconManager;
    public static boolean serviceIsOn = false;
    public static final String BECON_DEVICE_ID = "BECON_DEVICE_ID";
    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;
    public static HashMap<String,GimbalEvent> events;

    public HomelessDiscoveryService() {
    }
    public void onCreate(){
        //super.onCreate();
        Gimbal.setApiKey(this.getApplication(), "26ad5d51-bb3a-4db6-844a-e8776082a671");
        setPlaceEventListener();
        setComunicationListener();
        setBeaconSightingListener();
        HomelessDiscoveryService.serviceIsOn = true;
        events = new HashMap<>();
    }

    private void setComunicationListener(){
        communicationListener = new CommunicationListener() {
            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Visit visit) {

                for (Communication communication : communications) {
                    GimbalEvent.TYPE type = GimbalEvent.TYPE.NOTIFICATION_CLICKED;
                    String title = communication.getTitle();
                    String id = communication.getIdentifier();
                    Date date = new Date(communication.getDeliveryDate());
                    sendEvent(new GimbalEvent(type, title, id, date));
                }

                // let the SDK post notifications for the communicates
                return communications;
            }

            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Push push) {

                for (Communication communication : communications) {
                    GimbalEvent.TYPE type = GimbalEvent.TYPE.NOTIFICATION_CLICKED;
                    String title = communication.getTitle();
                    String id = communication.getIdentifier();
                    Date date = new Date(communication.getDeliveryDate());
                    sendEvent(new GimbalEvent(type, title, id, date));
                }

                // let the SDK post notifications for the communicates
                return communications;
            }

            @Override
            public void onNotificationClicked(List<Communication> communications) {
                for (Communication communication : communications) {
                    GimbalEvent.TYPE type = GimbalEvent.TYPE.NOTIFICATION_CLICKED;
                    String title = communication.getTitle();
                    String id = communication.getIdentifier();
                    Date date = new Date(communication.getDeliveryDate());
                    sendEvent(new GimbalEvent(type, title, id, date));
                }
            }
        };
        CommunicationManager.getInstance().addListener(communicationListener);
        CommunicationManager.getInstance().startReceivingCommunications();
    }

    private void setPlaceEventListener(){
        placeEventListener = new PlaceEventListener() {

            @Override
            public void onVisitStart(Visit visit) {
                GimbalEvent.TYPE type = GimbalEvent.TYPE.PLACE_ENTER;
                String name = visit.getPlace().getName();
                String deviceId = visit.getPlace().getIdentifier();
                Date date = new Date(visit.getDepartureTimeInMillis());
                sendEvent(new GimbalEvent(type, name, deviceId, date));
            }

            @Override
            public void onVisitEnd(Visit visit) {
                GimbalEvent.TYPE type = GimbalEvent.TYPE.PLACE_EXIT;
                String name = visit.getPlace().getName();
                String deviceId = visit.getPlace().getIdentifier();
                Date date = new Date(visit.getDepartureTimeInMillis());
                GimbalEvent event = new GimbalEvent(type, name, deviceId, date);
                events.put(deviceId, event);
                sendEvent(event);
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);
        PlaceManager.getInstance().startMonitoring();
    }

    private void setBeaconSightingListener(){
        beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sighting) {
                int RSSI = sighting.getRSSI();

                String deviceId = sighting.getBeacon().getIdentifier();
                if(RSSI >= -80 && !events.containsKey(deviceId) ){
                    GimbalEvent.TYPE type = GimbalEvent.TYPE.BECON_DETECTION;
                    String name = sighting.getBeacon().getName();
                    Date date = new Date(sighting.getTimeInMillis());
                    GimbalEvent event = new GimbalEvent(type, name, deviceId, date);
                    events.put(deviceId, event);
                    sendEvent(event);
                }
                Log.i("Gimbal Becon Info", sighting.toString());
            }
        };
        beaconManager = new BeaconManager();
        beaconManager.addListener(beaconSightingListener);
        beaconManager.startListening();
    }

    private void sendEvent(GimbalEvent event) {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        GimbalDAO.sendNotification(getApplicationContext(), mNotifyMgr, event);
    }

    /**
     * Called by the system to notify a Service that it is no longer used and is being removed.  The
     * service should clean up any resources it holds (threads, registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.  Do not call this method directly.
     */
    @Override
    public void onDestroy() {
        HomelessDiscoveryService.serviceIsOn = false;
        PlaceManager.getInstance().removeListener(placeEventListener);
        CommunicationManager.getInstance().removeListener(communicationListener);
        PlaceManager.getInstance().stopMonitoring();
        CommunicationManager.getInstance().stopReceivingCommunications();
        beaconManager.stopListening();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
