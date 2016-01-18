package org.openhab.binding.netatmo.internal;

import org.openhab.binding.netatmo.internal.messages.GetStationsDataRequest;
import org.openhab.binding.netatmo.internal.messages.GetStationsDataResponse;
import org.openhab.binding.netatmo.internal.messages.RefreshTokenRequest;
import org.openhab.binding.netatmo.internal.messages.RefreshTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This internal class holds the different crendentials necessary for the
 * OAuth2 flow to work. It also provides basic methods to refresh the access
 * token.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @author Ing. Peter Weiss
 * @since 1.6.0
 */

public class OAuthCredentials {
	
	private static final Logger logger = LoggerFactory
			.getLogger(OAuthCredentials.class);

		/**
		 * The client id to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client
		 *      Credentials</a>
		 */
		String clientId;

		/**
		 * The client secret to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client
		 *      Credentials</a>
		 */
		String clientSecret;

		/**
		 * The refresh token to access the Netatmo API. Normally set in
		 * <code>openhab.cfg</code>.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/usercred">Client&nbsp;Credentials</a>
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh&nbsp;Token</a>
		 */
		String refreshToken;

		/**
		 * The access token to access the Netatmo API. Automatically renewed
		 * from the API using the refresh token.
		 * 
		 * @see <a
		 *      href="http://dev.netatmo.com/doc/authentication/refreshtoken">Refresh
		 *      Token</a>
		 * @see #refreshAccessToken()
		 */
		String accessToken;

		public String getAccessToken() {
			return accessToken;
		}

		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}

		GetStationsDataResponse getStationsDataResponse = null;
		GetStationsDataRequest getStationsDataRequest = null;

		boolean firstExecution = true;

		public boolean noAccessToken() {
			return this.accessToken == null;
		}

		public void refreshAccessToken() {
			logger.debug("Refreshing access token.");

			final RefreshTokenRequest request = new RefreshTokenRequest(
					this.clientId, this.clientSecret, this.refreshToken);
			logger.debug("Request: {}", request);

			final RefreshTokenResponse response = request.execute();
			logger.debug("Response: {}", response);

			if (response == null) {
				throw new NetatmoException("Could not refresh access token! If you see "
						+ "'Fatal transport error: javax.net.ssl.SSLHandshakeException' "
						+ "above. You need to install the StartCom CA certificate and restart openHAB. "
						+ "See https://github.com/openhab/openhab/wiki/Netatmo-Binding#missing-certificate-authority "
						+ "for more information.");
			}

			this.accessToken = response.getAccessToken();

			getStationsDataRequest = new GetStationsDataRequest(this.accessToken);
			getStationsDataResponse = getStationsDataRequest.execute();
		}

	}

