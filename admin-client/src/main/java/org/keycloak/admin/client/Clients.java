package org.keycloak.admin.client;

import org.keycloak.client.v2.api.ClientsV2Api;
import org.keycloak.client.v2.invoker.ApiClient;
import org.keycloak.client.v2.invoker.ApiException;
import org.keycloak.client.v2.model.OIDCClientRepresentation;
import org.keycloak.client.v2.model.SAMLClientRepresentation;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;

import java.util.List;

public class Clients {
  private final Keycloak keycloak;
  private final Config config;
  private final String realmName;
  private final Client httpClient;

  Clients(Keycloak keycloak, Config config, String realmName, Client httpClient) {
    this.keycloak = keycloak;
    this.config = config;
    this.realmName = realmName;
    this.httpClient = httpClient;
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

  private static final String V2 = "v2";

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
      ApiClient apiClient = new ApiClient()
          .setBasePath(config.getServerUrl())
          .addDefaultHeader("Authorization", "Bearer " + token);
      // Reuse the same HTTP client (with SSL configuration) as the legacy admin client
      if (httpClient != null) {
        apiClient.setHttpClient(httpClient);
      }
      return apiClient;
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
    public List<org.keycloak.client.v2.model.BaseClientRepresentation> getAll() throws ApiException {
      return new ClientsV2Api(getApiClient()).getClients(realmName, V2);
    }

    /**
     * Gets a specific client by its ID.
     */
    public org.keycloak.client.v2.model.BaseClientRepresentation get(String id) throws ApiException {
      return new ClientsV2Api(getApiClient()).getClient(realmName, V2, id);
    }

    /**
     * Creates a new client in the realm.
     */
    public Object create(org.keycloak.client.v2.model.BaseClientRepresentation client) throws ApiException {
      return new ClientsV2Api(getApiClient()).createClient(realmName, V2, client);
    }

    /**
     * Creates or updates a client.
     */
    public Object createOrUpdate(String id, org.keycloak.client.v2.model.BaseClientRepresentation client) throws ApiException {
      return new ClientsV2Api(getApiClient()).createOrUpdateClient(realmName, V2, id, client);
    }

    /**
     * Partially updates a client using JSON Merge Patch.
     */
    public org.keycloak.client.v2.model.BaseClientRepresentation patch(String id, List<Object> patchOperations) throws ApiException {
      return new ClientsV2Api(getApiClient()).patchClient(realmName, V2, id, patchOperations);
    }

    /**
     * Deletes a client from the realm.
     */
    public void delete(String id) throws ApiException {
      new ClientsV2Api(getApiClient()).deleteClient(realmName, V2, id);
    }
  }
}
