/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.welcome;

import org.apache.commons.lang.StringUtils;

/**
 * @author Ing. Peter Weiss
 * @since 1.9.0
 *
 * This enum holds all the different attributes for netatmo welcome
 */
public enum NetatmoWelcomeAttributes {
    HOME_NAME("Name"),
	HOME_PLACE_COUNTRY("PlaceCountry"),
	HOME_PLACE_TIMEZONE("PlaceTimezone"),

	HOME_PERSON_OUTOFSIGHT("OutOfSight"),
	HOME_PERSON_PSEUDO("Pseudo"),
	HOME_PERSON_LASTSEEN("LastSeen"),
	HOME_PERSON_FACE_ID("FaceId"),
	HOME_PERSON_FACE_KEY("FaceKey"),

	HOME_UNKNWOWN_HOME_COUNT("HomeCount"),
	HOME_UNKNWOWN_AWAY_COUNT("AwayCount"),
	HOME_UNKNWOWN_OUTOFSIGHT_LIST("OutOfSightList"),
	HOME_UNKNWOWN_LASTSEEN_LIST("LastSeenList"),
	HOME_UNKNWOWN_FACE_ID_LIST("FaceIdList"),
	HOME_UNKNWOWN_FACE_KEY_LIST("FaceKeyList"),
	 
	HOME_CAMERA_NAME("Name"),
	HOME_CAMERA_STATUS("Status"),
	HOME_CAMERA_SD_STATUS("SdStatus"),
	HOME_CAMERA_ALIM_STATUS("AlimStatus");


    final String attribute;

    /**
     * 
     * @param attribute
     */
    private NetatmoWelcomeAttributes(String attribute) {
        this.attribute = attribute;
    }

    /**
     * 
     * @return
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * 
     * @param attribute
     * @return
     */
    public static NetatmoWelcomeAttributes fromString(String attribute) {
        if (!StringUtils.isEmpty(attribute)) {
            for (NetatmoWelcomeAttributes enumAttribute : NetatmoWelcomeAttributes.values()) {
                if (enumAttribute.getAttribute().equalsIgnoreCase(attribute)) {
                    return enumAttribute;
                }
            }
        }
        throw new IllegalArgumentException("Invalid attribute: " + attribute);
    }

}
