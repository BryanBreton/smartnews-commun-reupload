package fr.su.smartnewscommun.services.googleapi.drive;

import java.io.IOException;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;

import fr.su.smartnewscommun.services.googleapi.GoogleApiMainConfiguration;

/**
 * Configuration Spring pour la connexion à l'API Google Slides
 * Dépend de {@link GoogleApiMainConfiguration}
 */
@ConditionalOnProperty(prefix = "google", name = "api.drive.scopes")
public class GoogleDriveApiConfiguration {

    @Value("${google.api.read.timeout.milliseconds:10000}")
    int readTimeout;

    @Value("${google.api.connect.timeout.milliseconds:10000}")
    int connectTimeout;

    @Autowired
    private Credential googleCredential;

    @Autowired(required = false)
    private Proxy applicationProxy;

    @Value("${google.api.userId}")
    private String googleApiUserId;

    public static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * Service de connexion aux API Google Drive
     *
     * @return {@link Drive}
     */
    @Bean
    public Drive driveService() {
        final NetHttpTransport.Builder httpTransportBuilder = new NetHttpTransport.Builder();
        if (this.applicationProxy != null) {
            httpTransportBuilder.setProxy(this.applicationProxy);
        }

        return new Drive.Builder(httpTransportBuilder.build(), JSON_FACTORY, setHttpTimeout(this.googleCredential))
                .setApplicationName(this.googleApiUserId)
                .build();
    }

    /**
     * Positionne les timeout de connexion et lecture pour les appels d'APIs Google
     *
     * @param requestInitializer {@link Credential}
     * @return {@link HttpRequestInitializer}
     */
    private HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                requestInitializer.initialize(httpRequest);
                httpRequest.setConnectTimeout(GoogleDriveApiConfiguration.this.connectTimeout);
                httpRequest.setReadTimeout(GoogleDriveApiConfiguration.this.readTimeout);
            }
        };
    }
}
