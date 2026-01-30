package org.keycloak.admin.client;

import org.keycloak.client.v2.api.ClientsV2Api;
import org.keycloak.client.v2.invoker.ApiClient;
import org.keycloak.client.v2.invoker.ApiException;
import org.keycloak.client.v2.model.JsonNode;

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

  // V2 API - fluent wrapper

  /**
   * Returns a V2 API accessor with fluent methods.
   */
  public V2 v2() {
    return new V2();
  }

  /**
   * Inner class providing fluent access to V2 Clients API.
   */
  public class V2 {
    private ApiClient getApiClient() {
      String token = keycloak.tokenManager().getAccessTokenString();
      return new ApiClient()
          .setBasePath(config.getServerUrl())
          .addDefaultHeader("Authorization", "Bearer " + token);
    }

    /**
     * Returns the underlying generated API for advanced use cases.
     */
    public ClientsV2Api raw() {
      return new ClientsV2Api(getApiClient());
    }

    /**
     * Lists all clients in the realm.
     */
    public List<org.keycloak.client.v2.model.ClientRepresentation> getAll() throws ApiException {
      return new ClientsV2Api(getApiClient()).listClientsV2(realmName);
    }

    /**
     * Gets a specific client by its ID.
     */
    public org.keycloak.client.v2.model.ClientRepresentation get(String id) throws ApiException {
      return new ClientsV2Api(getApiClient()).getClientV2(realmName, id);
    }

    /**
     * Creates a new client in the realm.
     */
    public void create(org.keycloak.client.v2.model.ClientRepresentation client) throws ApiException {
      new ClientsV2Api(getApiClient()).createClientV2(realmName, client);
    }

    /**
     * Fully updates an existing client.
     */
    public void update(String id, org.keycloak.client.v2.model.ClientRepresentation client) throws ApiException {
      new ClientsV2Api(getApiClient()).updateClientV2(realmName, id, client);
    }

    /**
     * Partially updates an existing client using JSON Merge Patch.
     */
    public org.keycloak.client.v2.model.ClientRepresentation patch(String id, JsonNode patch) throws ApiException {
      return new ClientsV2Api(getApiClient()).patchClientV2(realmName, id, patch);
    }

    /**
     * Deletes a client from the realm.
     */
    public void delete(String id) throws ApiException {
      new ClientsV2Api(getApiClient()).deleteClientV2(realmName, id);
    }
  }
}
