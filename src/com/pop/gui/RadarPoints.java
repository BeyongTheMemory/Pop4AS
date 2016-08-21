
package com.pop.gui;

import com.pop.lib.marker.ImageMarker;
import com.pop.show.DataView;
import com.pop.lib.marker.Marker;
import com.pop.lib.gui.PaintScreen;
import com.pop.lib.gui.ScreenObj;
import com.pop.data.DataHandler;

import android.graphics.Color;

import java.util.Iterator;
import java.util.Map;

/** Takes care of the small radar in the top left corner and of its points
 * 雷达
 * @author daniele
 *
 */
public class RadarPoints implements ScreenObj {
	/** The screen */
	public DataView view;
	/** The radar's range */
	float range;
	/** Radius in pixel on screen */
	public static float RADIUS = 80;
	/** Position on screen */
	static float originX = 0 , originY = 0;
	/** Color */
	static int radarColor = Color.argb(20,255,255,255);
	static int pointColor = Color.BLACK;
	
	public void paint(PaintScreen dw) {//按照1Km等比例缩放
		/** radius is in KM. */
		//乘1000不对吧
		range = view.getRadius() * 1000;
		/** Draw the radar */
		dw.setFill(true);
		dw.setColor(radarColor);
		dw.paintCircle(originX + RADIUS, originY + RADIUS, RADIUS);

		/** put the markers in it */
		float scale = range / RADIUS;

		DataHandler jLayer = view.getDataHandler();
		for (int i = 0; i < jLayer.getMarkerCount(); i++) {
			Marker pm = jLayer.getMarker(i);
			float x = pm.getLocationVector().x / scale;
			float y = pm.getLocationVector().z / scale;

			if (pm.isActive() && (x * x + y * y < RADIUS * RADIUS)) {
				dw.setFill(true);

				// For OpenStreetMap the color is changing based on the URL
				dw.setColor(pointColor);
				dw.paintRect(x + RADIUS - 1, y + RADIUS - 1, 2, 2);
			}
		}
	}

	/** Width on screen */
	public float getWidth() {
		return RADIUS * 2;
	}

	/** Height on screen */
	public float getHeight() {
		return RADIUS * 2;
	}
}

