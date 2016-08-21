
package com.pop.mgr.location;

import com.pop.mgr.downloader.DownloadManager;

import android.hardware.GeomagneticField;
import android.location.Location;

/**
 * This class is repsonsible for finding the location, and sending it back to
 * the mixcontext.
 */
public interface LocationFinder {

	/**
	 * Possible status of LocationFinder̬
	 */
	public enum LocationFinderState {
		Active, // Providing Location Information
		Inactive, // No-Active
		Confused // Same problem in internal state
	}

	/**
	 * Finds the location through the providers
	 */
	void findLocation();

	/**
	 * A working location provider has been found: check if 
	 * the found location has the best accuracy.
	 */
	void locationCallback(String provider);
	
	/**
	 * Returns the current location
	 */
	Location getCurrentLocation();

	/**
	 * Gets the location that was used in the last download for
	 * datasources.
	 * @return
	 */
	Location getLocationAtLastDownload();

	/**
	 * Sets the property to the location with the last successfull download.
	 */
	void setLocationAtLastDownload(Location locationAtLastDownload);

	/**
	 * Set the DownloadManager manager at this service
	 * 
	 * @param downloadManager
	 */
	void setDownloadManager(DownloadManager downloadManager);

	/**
	 * Request to active the service
	 */
	void switchOn();

	/**
	 * Request to deactive the service
	 */
	void switchOff();

	/**
	 * Status of service
	 * @return
	 */
	LocationFinderState getStatus();

	/**
	 * 
	 * @return GeomagneticField
	 */
	GeomagneticField getGeomagneticField();

}