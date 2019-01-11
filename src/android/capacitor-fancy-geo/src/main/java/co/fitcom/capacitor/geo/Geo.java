package co.fitcom.capacitor.geo;

import android.support.annotation.Nullable;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.github.triniwiz.fancygeo.FancyGeo;

import org.json.JSONException;
import org.json.JSONObject;

@NativePlugin(
        requestCodes = {
                FancyGeo.GEO_LOCATION_PERMISSIONS_REQUEST
        }
)
public class Geo extends Plugin {
    private FancyGeo fancyGeo;

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        PluginCall call = getSavedCall();
        if (call != null) {
            if (FancyGeo.hasPermission(getContext())) {
                JSObject object = new JSObject();
                object.put("value", true);
                call.resolve(object);
            } else {
                call.reject("Permission Denied!");
            }
        }
    }

    @Override
    public void load() {
        super.load();
        FancyGeo.init(getActivity().getApplication());
        fancyGeo = new FancyGeo(getContext());
    }

    @PluginMethod(returnType = "callback")
    public void setOnMessageListener(final PluginCall call) {
        FancyGeo.setOnMessageReceivedListener(new FancyGeo.NotificationsListener() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSObject object = new JSObject(s);
                    call.success(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    call.error(e.getLocalizedMessage(), e);
                }
            }

            @Override
            public void onError(Exception e) {
                call.error(e.getMessage(), e);
            }
        });
    }

    @PluginMethod()
    public void initiate(PluginCall call) {
        call.resolve();
    }

    @PluginMethod()
    public void createCircleFence(final PluginCall call) {
        String id = call.getString("id", null);
        int transition = call.getInt("transition", -1);
        if (transition == -1) {
            call.reject("Transition missing");
            return;
        }
        int loiteringDelay = call.getInt("loiteringDelay", 0);
        JSArray pointsArray = call.getArray("points");
        double[] points;
        try {
            double lat = (double) pointsArray.get(0);
            double lon = (double) pointsArray.get(1);
            points = new double[]{lat, lon};
        } catch (JSONException e) {
            call.reject("Coordinates missing");
            return;
        }

        double radius = call.getDouble("radius");
        Object notificationObj = call.getObject("notification");
        FancyGeo.FenceNotification notification = null;
        if (notificationObj != null) {
            notification = new FancyGeo.FenceNotification();
            notification.setTitle(((JSObject) notificationObj).getString("title"));
            notification.setBody(((JSObject) notificationObj).getString("body"));
            notification.setId(((JSObject) notificationObj).getInteger("id"));
        }
        fancyGeo.createFenceCircle(id, FancyGeo.FenceTransition.values()[transition], loiteringDelay, points, radius, notification, new FancyGeo.FenceCallback() {
            @Override
            public void onSuccess(String id) {
                JSObject object = new JSObject();
                object.put("id", id);
                call.resolve(object);
            }

            @Override
            public void onFail(Exception e) {
                call.reject(e.getMessage(), e);
            }
        });
    }

    @PluginMethod()
    public void hasPermission(PluginCall call) {
        boolean value = FancyGeo.hasPermission(getContext());
        JSObject object = new JSObject();
        object.put("value", value);
    }

    @PluginMethod()
    public void requestPermission(PluginCall call) {
        FancyGeo.requestPermission(getContext());
    }

    @PluginMethod()
    public void getCurrentLocation(final PluginCall call) {
        fancyGeo.getCurrentLocation(new FancyGeo.FancyGeoCurrentLocationListener() {
            @Override
            public void onLocation(FancyGeo.FancyLocation fancyLocation) {
                String location = fancyLocation.toJson();
                try {
                    JSObject object = new JSObject(location);
                    call.resolve(object);
                } catch (JSONException e) {
                    call.reject(e.getMessage(), e);
                }
            }

            @Override
            public void onLocationError(Exception e) {
                call.reject(e.getMessage(), e);
            }
        });
    }

    @PluginMethod()
    public void getAllFences(PluginCall call) {
        fancyGeo.getAllFences();
    }

    @PluginMethod()
    public void getFence(PluginCall call) {
        String id = call.getString("id");
        String fence = fancyGeo.getFence(id).toJson();
        try {
            JSObject object = new JSObject(fence);
            call.resolve(object);
        } catch (JSONException e) {
            call.resolve();
        }
    }

    @PluginMethod()
    public void removeAllFences(final PluginCall call) {
        fancyGeo.removeAllFences(new FancyGeo.Callback() {
            @Override
            public void onSuccess() {
                call.resolve();
            }

            @Override
            public void onFail(Exception e) {
                call.reject(e.getMessage(), e);
            }
        });
    }

    @PluginMethod()
    public void removeFence(final PluginCall call) {
        String id = call.getString("id");
        fancyGeo.removeFence(id, new FancyGeo.Callback() {
            @Override
            public void onSuccess() {
                call.success();
            }

            @Override
            public void onFail(Exception e) {
                call.reject(e.getMessage(), e);
            }
        });
    }
}
