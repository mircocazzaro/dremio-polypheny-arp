/*
 * Copyright (C) 2017-2018 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.exec.store.jdbc.conf;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.constraints.NotBlank;

import com.dremio.exec.catalog.conf.DisplayMetadata;
import com.dremio.exec.catalog.conf.NotMetadataImpacting;
import com.dremio.exec.catalog.conf.SourceType;
import com.dremio.exec.store.jdbc.CloseableDataSource;
import com.dremio.exec.store.jdbc.DataSources;
import com.dremio.exec.store.jdbc.JdbcPluginConfig;
import com.dremio.exec.store.jdbc.JdbcStoragePlugin;
import com.dremio.exec.store.jdbc.dialect.arp.ArpDialect;
import com.dremio.options.OptionManager;
import com.dremio.services.credentials.CredentialsService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.annotations.VisibleForTesting;

import io.protostuff.Tag;

/**
 * Configuration for POLYPHENY sources.
 */
@SourceType(value = "POLYPHENY", label = "POLYPHENY")
public class PolyphenyConf extends AbstractArpConf<PolyphenyConf> {
  private static final String ARP_FILENAME = "arp/implementation/polypheny-arp.yaml";
  private static final ArpDialect ARP_DIALECT =
      AbstractArpConf.loadArpFile(ARP_FILENAME, (ArpDialect::new));
  private static final String DRIVER = "org.polypheny.jdbc.Driver";

  @NotBlank
  @Tag(1)
  @DisplayMetadata(label = "Polypheny host <HOST:PORT>")
  public String database;

  @Tag(2)
  @DisplayMetadata(label = "Record fetch size")
  @NotMetadataImpacting
  public int fetchSize = 200;

//  If you've written your source prior to Dremio 16, and it allows for external query via a flag like below, you should
//  mark the flag as @JsonIgnore and remove use of the flag since external query support is now managed by the SourceType
//  annotation and if the user has been granted the EXTERNAL QUERY permission (enterprise only). Marking the flag as @JsonIgnore
//  will hide the external query tickbox field, but allow your users to upgrade Dremio without breaking existing source
//  configurations. An example of how to dummy this out is commented out below.
//  @Tag(3)
//  @NotMetadataImpacting
//  @JsonIgnore
//  public boolean enableExternalQuery = false;

  @Tag(6)
  @DisplayMetadata(label = "Polypheny User Name")
  public String username = "pa";

  @Tag(7)
  @DisplayMetadata(label = "Polypheny Password")
  public String password = "";

  @Tag(4)
  @DisplayMetadata(label = "Maximum idle connections")
  @NotMetadataImpacting
  public int maxIdleConns = 8;

  @Tag(5)
  @DisplayMetadata(label = "Connection idle time (s)")
  @NotMetadataImpacting
  public int idleTimeSec = 60;

  @VisibleForTesting
  public String toJdbcConnectionString() {
    final String database = checkNotNull(this.database, "Missing database.");

    return String.format("jdbc:polypheny://%s", database);
  }

  @Override
  @VisibleForTesting
  public JdbcPluginConfig buildPluginConfig(
          JdbcPluginConfig.Builder configBuilder,
          CredentialsService credentialsService,
          OptionManager optionManager
  ) {
    return configBuilder.withDialect(getDialect())
            .withDialect(getDialect())
            .withFetchSize(fetchSize)
            .withDatasourceFactory(this::newDataSource)
            .clearHiddenSchemas()
            .addHiddenSchema("SYSTEM")
            .build();
  }

  private CloseableDataSource newDataSource() {
    return DataSources.newGenericConnectionPoolDataSource(DRIVER,
      toJdbcConnectionString(), username, password, null, DataSources.CommitMode.DRIVER_SPECIFIED_COMMIT_MODE,
            maxIdleConns, idleTimeSec);
  }

  @Override
  public ArpDialect getDialect() {
    return ARP_DIALECT;
  }

  @VisibleForTesting
  public static ArpDialect getDialectSingleton() {
    return ARP_DIALECT;
  }
}
