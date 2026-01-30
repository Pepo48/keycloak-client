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
import org.keycloak.representations.idm.RealmRepresentation;

import java.util.List;

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
  public void getClientsV2() throws ApiException {


    Assert.assertNames(
        adminClient.clients(REALM_NAME).v2().getAllClients(""),
        "account", "account-console", "realm-management", "security-admin-console", "broker", Constants.ADMIN_CLI_CLIENT_ID);
  }

  @Test
  public void putClientsV2WithoutAnyWrapping() throws ApiException {

    adminClient.clients().adminApiV2RealmsNameClientsIdPut("REALM_NAME", "ID", null);
  }



}
