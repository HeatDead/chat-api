package com.hakathon.chatapi.config;

import com.hakathon.chatapi.model.Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakInitializerRunner implements CommandLineRunner {

    private final Keycloak keycloakAdmin;

    @Override
    public void run(String... args) {
        log.info("Initializing '{}' realm in Keycloak ...", COMPANY_SERVICE_REALM_NAME);

        Optional<RealmRepresentation> representationOptional = keycloakAdmin.realms()
                .findAll()
                .stream()
                .filter(r -> r.getRealm().equals(COMPANY_SERVICE_REALM_NAME))
                .findAny();
        if (representationOptional.isPresent()) {
            log.info("Removing already pre-configured '{}' realm", COMPANY_SERVICE_REALM_NAME);
            keycloakAdmin.realm(COMPANY_SERVICE_REALM_NAME).remove();
            //return;
        }

        // Realm
        RealmRepresentation realmRepresentation = new RealmRepresentation();
        realmRepresentation.setRealm(COMPANY_SERVICE_REALM_NAME);
        realmRepresentation.setEnabled(true);
        realmRepresentation.setRegistrationAllowed(true);

        // Client
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(CHAT_APP_CLIENT_ID);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setPublicClient(true);
        clientRepresentation.setRedirectUris(List.of(CHAT_APP_REDIRECT_URL));
        clientRepresentation.setDefaultRoles(new String[]{WebSecurityConfig.USER,
                Manager.CREDIT_MANAGER.toString(),
                Manager.EXPENSE_MANAGER.toString(),
                Manager.FACTORING_MANAGER.toString(),
                Manager.GUARANTEES_MANAGER.toString()});
        realmRepresentation.setClients(List.of(clientRepresentation));

        // Users
        List<UserRepresentation> userRepresentations = CHAT_APP_USERS.stream()
                .map(userPass -> {
                    // User Credentials
                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                    credentialRepresentation.setValue(userPass.password());

                    // User
                    UserRepresentation userRepresentation = new UserRepresentation();
                    userRepresentation.setUsername(userPass.username());
                    userRepresentation.setEnabled(true);
                    userRepresentation.setCredentials(List.of(credentialRepresentation));
                    userRepresentation.setClientRoles(getClientRoles(userPass));

                    return userRepresentation;
                })
                .toList();
        realmRepresentation.setUsers(userRepresentations);

        // Create Realm
        keycloakAdmin.realms().create(realmRepresentation);

        // Testing
        UserPass admin = CHAT_APP_USERS.get(0);
        log.info("Testing getting token for '{}' ...", admin.username());

        Keycloak keycloakMovieApp = KeycloakBuilder.builder().serverUrl(KEYCLOAK_SERVER_URL)
                .realm(COMPANY_SERVICE_REALM_NAME).username(admin.username()).password(admin.password())
                .clientId(CHAT_APP_CLIENT_ID).build();

        log.info("'{}' token: {}", admin.username(), keycloakMovieApp.tokenManager().grantToken().getToken());
        log.info("'{}' initialization completed successfully!", COMPANY_SERVICE_REALM_NAME);
    }

    private Map<String, List<String>> getClientRoles(UserPass userPass) {
        List<String> roles = new ArrayList<>();
        if ("admin".equals(userPass.username())) {
            roles.add(WebSecurityConfig.CHAT_MANAGER);
        } else if ("credit_manager".equals(userPass.username())) {
            roles.add(Manager.CREDIT_MANAGER.toString());
        } else if ("expense_manager".equals(userPass.username())) {
            roles.add(Manager.EXPENSE_MANAGER.toString());
        } else if ("factoring_manager".equals(userPass.username())) {
            roles.add(Manager.FACTORING_MANAGER.toString());
        } else if ("guarantees_manager".equals(userPass.username())) {
            roles.add(Manager.GUARANTEES_MANAGER.toString());
        }else {
            roles.add(WebSecurityConfig.USER);
        }
        return Map.of(CHAT_APP_CLIENT_ID, roles);
    }

    private static final String KEYCLOAK_SERVER_URL = "http://localhost:8080";
    private static final String COMPANY_SERVICE_REALM_NAME = "company-services";
    private static final String CHAT_APP_CLIENT_ID = "movies-app";
    private static final String CHAT_APP_REDIRECT_URL = "http://localhost:3000/*";
    private static final List<UserPass> CHAT_APP_USERS = Arrays.asList(
            new UserPass("admin", "admin"),
            new UserPass("user", "user"),
            new UserPass("credit_manager", "manager"),
            new UserPass("expense_manager", "manager"),
            new UserPass("factoring_manager", "manager"),
            new UserPass("guarantees_manager", "manager"));

    private record UserPass(String username, String password) {
    }
}