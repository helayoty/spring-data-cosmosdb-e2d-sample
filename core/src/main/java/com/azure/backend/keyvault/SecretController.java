
package com.azure.backend.keyvault;


import com.azure.backend.services.configuration.IConfigurationService;
import com.azure.backend.services.keyvault.IKeyVaultService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "api/secret")
public class SecretController {
  private static final Logger logger =   LogManager.getLogger(SecretController.class);

  @Autowired private IKeyVaultService keyVaultService;

  @Autowired
  IConfigurationService config;

  /** getSecret. */
  @GetMapping(
      value = {"/", ""},
      produces = MediaType.TEXT_PLAIN_VALUE)
  public Mono<ResponseEntity<String>> getSecret(String secretName) {
    try {
      return keyVaultService
          .getSecretValue(secretName)
          .map(ResponseEntity::ok)
          .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    } catch (Exception ex) {
      logger.error("Error recieved in SecretController", ex);
      return Mono.error(new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "secret Error"));
    }
  }

  public IKeyVaultService getKeyVaultService() {
    return keyVaultService;
  }
}
