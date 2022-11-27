package fr.su.smartnewscommun.services.googleapi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.FileSystems;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import fr.su.smartnewscommun.services.googleapi.drive.GoogleDriveApiScope;
import fr.su.smartnewscommun.services.googleapi.sheets.GoogleSheetsApiScope;
import fr.su.smartnewscommun.services.googleapi.slides.GoogleSlideApiScope;

/**
 * Configuration Spring du SDK Googles API
 */
@ConditionalOnProperty(prefix = "google", name = "credentials.folder.path")
public class GoogleApiMainConfiguration {
    @Value("${http.proxy.host}")
    private String proxyHost;

    @Value("${http.proxy.port}")
    private Integer proxyPort;

    @Value("${google.credentials.folder.path}")
    private String credentialsFolderPath;

    @Value("${google.api.json.credentials.base64}")
    private String jsonCredentialsBase64;

    @Value("${google.api.stored.credential.base64}")
    private String storedCredentialsBase64;

    @Value("${google.api.userId}")
    private String googleApiUserId;

    @Value("${google.api.drive.scopes:#{null}}")
    private Optional<List<GoogleDriveApiScope>> googleApiDriveScopes;

    @Value("${google.api.slides.scopes:#{null}}")
    private Optional<List<GoogleSlideApiScope>> googleApiSlidesScopes;

    @Value("${google.api.sheets.scopes:#{null}}")
    private Optional<List<GoogleSheetsApiScope>> googleApiSheetsScopes;

    public static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * {@link GoogleApiMainConfiguration#createApplicationProxy}
     *
     * @return {@link Proxy}
     */
    @Bean
    public Proxy applicationProxy() {
        return createApplicationProxy();
    }

    /**
     * Crée l'object Google portant l'authentification aux APIs Google
     *
     * @return {@link Credential}
     * @throws IOException problème lors de la création/lecture des fichiers de conf
     */
    @Bean
    public Credential getCredentials() throws IOException {
        final File jsonCredentialsFile = new File(credentialsFolderPath + FileSystems.getDefault().getSeparator() + "credentials.json");
        FileUtils.writeByteArrayToFile(jsonCredentialsFile, Base64.getDecoder().decode(jsonCredentialsBase64), false);
        if (!storedCredentialsBase64.isEmpty()) {
            FileUtils.writeByteArrayToFile(new File(credentialsFolderPath + FileSystems.getDefault().getSeparator() + "StoredCredential"),
                    Base64.getDecoder().decode(storedCredentialsBase64), false);
        }

        final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new FileReader(jsonCredentialsFile));
        final List<String> scopes = getApisScopes();

        final GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(getNetHttpTransport(), JSON_FACTORY, clientSecrets,
                scopes).setDataStoreFactory(new FileDataStoreFactory(new File(credentialsFolderPath))).setAccessType("offline").build();

        final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize(this.googleApiUserId);

    }

    /**
     * Récupère les scopes demandées par le client des APIs
     *
     * @return des scopes du SDK Google
     */
    private List<String> getApisScopes() {
        final List<String> scopes = new LinkedList<>();

        this.googleApiDriveScopes.ifPresent(
                (googleApiDriveScopes) -> googleApiDriveScopes.stream().map(GoogleDriveApiScope::getGoogleApiScopeValue).forEach(scopes::add));

        this.googleApiSlidesScopes.ifPresent(
                (googleApiSlidesScopes) -> googleApiSlidesScopes.stream().map(GoogleSlideApiScope::getGoogleApiScopeValue).forEach(scopes::add));

        this.googleApiSheetsScopes.ifPresent(
                (googleApiSheetsScopes) -> googleApiSheetsScopes.stream().map(GoogleSheetsApiScope::getGoogleApiScopeValue).forEach(scopes::add));

        return scopes;
    }

    /**
     * Crée une instance de {@link Proxy} si les props proxy.host et proxu.port existent
     *
     * @return {@link Proxy}
     */
    private Proxy createApplicationProxy() {
        if (!this.proxyHost.isEmpty() && this.proxyPort != null) {
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.proxyHost, this.proxyPort));
        }
        return null;
    }

    /**
     * Crée la couche de transport qui va être utilisée pour les appels Google API : avec ou sans proxy
     *
     * @return
     */
    private NetHttpTransport getNetHttpTransport() {
        final NetHttpTransport.Builder httpTransportBuilder = new NetHttpTransport.Builder();
        final Proxy applicationProxy = this.createApplicationProxy();
        if (applicationProxy != null) {
            httpTransportBuilder.setProxy(applicationProxy);
        }
        return httpTransportBuilder.build();
    }
}
