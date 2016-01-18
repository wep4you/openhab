/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import org.openhab.binding.netatmo.NetatmoBindingProvider;
import org.openhab.binding.netatmo.welcome.NetatmoWelcomeAttributes;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * <p>
 * Valid bindings for the main device are:
 * <ul>
 * <li><code>{ netatmo="&lt;device_id&gt;#Measurement" }</code></li>
 * <ul>
 * <li><code>{ netatmo="00:00:00:00:00:00#Temperature" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Humidity" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Co2" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Pressure" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Noise" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#WifiStatus" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Altitude" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Latitude" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Longitude" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#TimeStamp" }</code></li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Valid bindings for a module are:
 * <ul>
 * <li>
 * <code>{ netatmo="&lt;device_id&gt;#&lt;module_id&gt;#Measurement" }</code></li>
 * <ul>
 * <li>
 * <code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Temperature" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Humidity" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Co2" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Rain" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#RfStatus" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#BatteryVp" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#TimeStamp" }</code></li>
 * </ul>
 * </li>
 * </ul>
 * 
 * <b>Netatmo welcome:</b>
 * Valid bindings for a netatmo welcome home are:
 * <ul>
 * <li>
 * <code>{ netatmo="welcome#&lt;home_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#Name" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#PlaceCountry" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#PlaceTimezone" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 *
 * Valid bindings for a netatmo welcome person are:
 * <ul>
 * <li>
 * <code>{ netatmo="welcome#&lt;home_id&gt;#&lt;person_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#Pseudo" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#LastSeen" }</code></li>
 * <li><code>{ nnetatmo="welcome#1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#OutOfSight" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#FaceId" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#12345678-9abc-defg-hijk-lmnopqrstuvw#FaceKey" }</code></li>
 * </ul>
 * </li> </ul>
 *
 * Valid bindings for netatmo welcome unknown persons are:
 * <ul>
 * <li>
 * <code>{ netatmo="welcome#&lt;home_id&gt;#&lt;UNKNOWN&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#UNKNOWN#HomeCount" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#UNKNOWN#AwayCount" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#UNKNOWN#LastSeenList" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#UNKNOWN#OutOfSightList" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#UNKNOWN#FaceIdList" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#UNKNOWN#FaceKeyList" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 * Valid bindings for netatmo welcome camera are:
 * <ul>
 * <li>
 * <code>{ netatmo="welcome#&lt;home_id&gt;#&lt;camera_id&gt;#attribute" }</code></li>
 * <ul>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#00:00:00:00:00:00#Status" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#00:00:00:00:00:00#SdStatus" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#00:00:00:00:00:00#AlimStatus" }</code></li>
 * <li><code>{ netatmo="welcome#1234567890abcdefghijklmn#00:00:00:00:00:00#Name" }</code></li>
 * </ul>
 * </li> </ul>
 * 
 * Valid bindings for netatmo welcome event is -> NOT IMPLEMENTED NOW: TODO
 * <ul>
 * <li>
 * <code>{ netatmo="welcome&lt;home_id&gt;#&lt;event_id&gt;#attribute" }</code></li>
 * <ul>
 * </ul>
 * </li> </ul>

 *
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @author Rob Nielsen
 * @author Ing. Peter Weiss
 * @since 1.4.0
 */
public class NetatmoGenericBindingProvider extends AbstractGenericBindingProvider implements NetatmoBindingProvider {

    private static Logger logger = LoggerFactory.getLogger(NetatmoGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "netatmo";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(final Item item, final String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof NumberItem || item instanceof DateTimeItem || item instanceof LocationItem
                || item instanceof StringItem || item instanceof SwitchItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only NumberItems, DateTimeItems, StringItems, Switch and LocationItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserid(final String itemName) {
        final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.userid : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeviceId(final String itemName) {
        final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.deviceId : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetatmoMeasureType getMeasureType(String itemName) {
        final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.measureType : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getModuleId(final String itemName) {
        final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.moduleId : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetatmoScale getNetatmoScale(String itemName) {
        final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.netatmoScale : null;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public String getHomeId(String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.homeId : null;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public String getPersonId(String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.personId : null;
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	public NetatmoWelcomeAttributes getAttribute(String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.attribute : null;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public String getCameraId(String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs.get(itemName);
		return config != null ? config.cameraId : null;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(final String context, final Item item, final String bindingConfig)
            throws BindingConfigParseException {
        logger.debug("Processing binding configuration: '{}'", bindingConfig);

        super.processBindingConfiguration(context, item, bindingConfig);

        final NetatmoBindingConfig config = new NetatmoBindingConfig();

        final String[] configParts = bindingConfig.split("#");

        if(configParts.length > 0 && "welcome".equals(configParts[0])) {
        	switch (configParts.length) {
        	case 3:
        		config.homeId=configParts[1];
        		config.attribute=NetatmoWelcomeAttributes.fromString(configParts[2]);
        		break;
        	case 4:
        		config.homeId=configParts[1];
        		config.attribute=NetatmoWelcomeAttributes.fromString(configParts[3]);

        		//Check Format (Mac Adress with : is a camera)
        		String sTmp=configParts[2];
        		final String[] sItem = sTmp.split(":");
        		if (sItem.length == 6)
        			config.cameraId = sTmp;
        		else
        			config.personId = sTmp;		
        		break;
        	default:
        		throw new BindingConfigParseException(
        				"A Netatmo welcome binding configuration must consist of three, four or five parts - please verify your *.items file");
        	}
        }
        else {
        	String measureTypeString;
        	switch (configParts.length) {
        	case 2:
        		config.deviceId = configParts[0];
        		measureTypeString = configParts[1];
        		break;
        	case 3:
        		config.deviceId = configParts[0];
        		config.moduleId = configParts[1];
        		measureTypeString = configParts[2];
        		break;
        	case 4:
        		config.userid = configParts[0];
        		config.deviceId = configParts[1];
        		config.moduleId = configParts[2];
        		measureTypeString = configParts[3];
        		break;
        	default:
        		throw new BindingConfigParseException(
        				"A Netatmo binding configuration must consist of two, three or four parts - please verify your *.items file");
        	}

        	/*
        	 * use a ',' when including scale so that it does not break backwards
        	 * compatibility with case 4 above.
        	 */
        	final String[] measureTypeParts = measureTypeString.split(",");
        	switch (measureTypeParts.length) {
        	case 1:
        		config.measureType = NetatmoMeasureType.fromString(measureTypeParts[0]);
        		config.netatmoScale = config.measureType.getDefaultScale();
        		break;
        	case 2:
        		config.measureType = NetatmoMeasureType.fromString(measureTypeParts[0]);
        		config.netatmoScale = NetatmoScale.fromString(measureTypeParts[1]);
        		break;
        	default:
        		throw new BindingConfigParseException(
        				"The last part of the Netatmo binding configuration must be 'type' or 'type,scale'"
        						+ " - please verify your *.items file");
        	}
        }

        logger.debug("Adding binding: {}", config);

        addBindingConfig(item, config);
    }

    private static class NetatmoBindingConfig implements BindingConfig {

        String userid;
        String deviceId;
        String moduleId;
        NetatmoMeasureType measureType;
        NetatmoScale netatmoScale;

        // Netatmo welcome
		String homeId;
		String personId;
		String cameraId;
		NetatmoWelcomeAttributes attribute;

        @Override
        public String toString() {
            return "NetatmoBindingConfig [userid=" + this.userid + ", deviceId=" + this.deviceId + ", moduleId="
                    + this.moduleId + ", measure=" + (this.measureType!=null?this.measureType.getMeasure():null)
                    + ", homeId=" + this.homeId
					+ ", personId=" + this.personId
					+ ", cameraId=" + this.cameraId
					+ ", attribute=" + this.attribute
					+ "]";
        }
    }

}
