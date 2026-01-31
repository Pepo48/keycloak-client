/*
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.keycloak.client.testsuite;


import org.junit.jupiter.api.Test;
import org.keycloak.client.testsuite.models.Constants;
import org.keycloak.client.v2.invoker.ApiException;
import org.keycloak.client.v2.model.BaseClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientV2Test extends AbstractAdminClientTest {

  @Override
  public List<RealmRepresentation> getRealmsForImport() {
    return super.getRealmsForImport();
  }

  @Test
  public void getRealms() {
    List<RealmRepresentation> realms = adminClient.realms().findAll();
    Assert.assertNames(realms, "master", REALM_NAME, "test");
  }

  @Test
  public void getClientsViaLegacyDelegate() {
    Assert.assertNames(
        adminClient.clients(REALM_NAME).findAll(),
        "account", "account-console", "realm-management", "security-admin-console", "broker", Constants.ADMIN_CLI_CLIENT_ID);
  }

  @Test
  public void getClientsV2_fluentGetAll() throws ApiException {
    List<BaseClientRepresentation> clients = adminClient.clients(REALM_NAME).v2().getAll();

    assertNotNull(clients);
    List<String> clientIds = clients.stream()
        .map(BaseClientRepresentation::getClientId)
        .collect(Collectors.toList());
    
    org.junit.jupiter.api.Assertions.assertTrue(clientIds.contains("account"));
    org.junit.jupiter.api.Assertions.assertTrue(clientIds.contains("account-console"));
  }

  /**
   * Test accessing raw API
   */
  @Test
  public void getClientsV2_rawApiWithSemanticMethodNames() throws ApiException {
    List<BaseClientRepresentation> clients = adminClient.clients(REALM_NAME).v2().raw().getClients(REALM_NAME, "v2");

    assertNotNull(clients);
  }

  /**
   * Fluent wrapper:
   *   adminClient.clients("myRealm").v2().getAll()
   *   adminClient.clients("myRealm").v2().get(id)
   *   adminClient.clients("myRealm").v2().create(oidcClient)
   *   adminClient.clients("myRealm").v2().createOrUpdate(id, client)
   *   adminClient.clients("myRealm").v2().delete(id)
   *
   * Raw:
   *   adminClient.clients(REALM_NAME).v2().raw().getClients(realmName, "v2");
   *   adminClient.clients(REALM_NAME).v2().raw().getClient(realmName, "v2", id);
   *   adminClient.clients(REALM_NAME).v2().raw().createClient(realmName, "v2", client);
   *   adminClient.clients(REALM_NAME).v2().raw().createOrUpdateClient(realmName, "v2", id, client);
   *   adminClient.clients(REALM_NAME).v2().raw().deleteClient(realmName, "v2", id);
   */
  @Test
  public void demonstrateImprovedApiShape() throws ApiException {
    List<BaseClientRepresentation> clients = adminClient.clients(REALM_NAME).v2().getAll();
    assertNotNull(clients);
  }
}
