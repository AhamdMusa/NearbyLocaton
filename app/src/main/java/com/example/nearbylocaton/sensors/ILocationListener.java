package com.example.nearbylocaton.sensors;

import android.location.Location;

/**
 * Created by xyz on 1/19/18.
 */

public interface ILocationListener {
    void updatePosition(final Location location);
}
