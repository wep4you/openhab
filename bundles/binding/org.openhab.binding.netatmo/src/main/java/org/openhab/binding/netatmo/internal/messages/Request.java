/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

/**
 * Base interface for all Netatmo API responses.
 *
 * @author Andreas Brenk
 * @since 1.4.0
 */
public interface Request {

    /**
     * Send this request to the Netatmo API. Implementations specify a more
     * concrete {@link Response} class.
     * 
     * @return a {@link Response} containing the requested data or an error
     */
    Response execute();

}
