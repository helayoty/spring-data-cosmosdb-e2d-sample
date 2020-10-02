package com.azure.backend.services.configuration;

import com.azure.backend.services.keyvault.IKeyVaultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConfigurationService implements IConfigurationService {
  private static final Logger logger =   LogManager.getLogger(ConfigurationService.class);

  private IKeyVaultService keyVaultService;

  Map<String, String> configEntries = new ConcurrentHashMap<>();

  public Map<String, String> getConfigEntries() {
    return configEntries;
  }

  /**
   * ConfigurationService.
   */
  @Autowired
  public ConfigurationService(IKeyVaultService kvService) {
    try {
      if (kvService == null) {
        logger.info("keyVaultService is null");
        System.exit(-1);
      }

      keyVaultService = kvService;
      Map<String, String> secrets = keyVaultService.getSecrets();
      if (logger.isInfoEnabled()) {
        logger.info(MessageFormat.format("Secrets are {0}", secrets == null ? "NULL" : "NOT NULL"));
      }
      configEntries = secrets;

    } catch (Exception ex) {
      logger.error(ex.getMessage());
      throw ex;
    }
  }
}