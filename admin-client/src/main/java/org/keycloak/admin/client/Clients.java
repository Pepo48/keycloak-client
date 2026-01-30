package org.keycloak.admin.client;

import org.keycloak.client.v2.api.ClientsV2Api;
import org.keycloak.client.v2.invoker.ApiClient;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;

import java.util.List;

public class Clients {
  private final Keycloak keycloak;
  private final Config config;
  private final String realmName;

  Clients(Keycloak keycloak, Config config, String realmName) {
    this.keycloak = keycloak;
    this.config = config;
    this.realmName = realmName;
  }

  private ClientsResource legacyDelegate() {
    return keycloak.realm(realmName).clients();
  }

  // Delegation to legacy API
  // TODO, discuss if this is needed. if the function call should look like adminClient.clients(REALM_NAME).get(ID) -> then we need it

  public ClientResource get(String id) {
    return legacyDelegate().get(id);
  }

  public Response create(ClientRepresentation clientRepresentation) {
    return legacyDelegate().create(clientRepresentation);
  }

  public List<ClientRepresentation> findAll() {
    return legacyDelegate().findAll();
  }

  List<ClientRepresentation> findAll(boolean viewableOnly) {
    return legacyDelegate().findAll(viewableOnly);
  }
  List<ClientRepresentation> findAll(String clientId,
                                     Boolean viewableOnly,
                                     Boolean search,
                                     Integer firstResult,
                                     Integer maxResults) {
    return legacyDelegate().findAll(clientId, viewableOnly, search, firstResult, maxResults);
  }


  public List<ClientRepresentation> findByClientId(String clientId) {
    return legacyDelegate().findByClientId(clientId);
  }

  List<ClientRepresentation> query(String searchQuery) {
    return legacyDelegate().query(searchQuery);
  }

  public Response delete(String id) {
    return legacyDelegate().delete(id);
  }

  // delegate to v2 Clients API

  public ClientsV2Api v2() {
    String token = keycloak.tokenManager().getAccessTokenString();

    ApiClient apiClient = new ApiClient()
        .setBasePath(config.getServerUrl())
//        .setBasePath(config.getServerUrl() + "/admin/api/v2/realms/" + realmName)
        .addDefaultHeader("Authorization", "Bearer " + token);

    return new ClientsV2Api(apiClient);
  }
}
