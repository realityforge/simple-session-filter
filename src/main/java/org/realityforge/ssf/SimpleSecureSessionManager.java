package org.realityforge.ssf;

import java.util.UUID;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.realityforge.keycloak.sks.SimpleAuthService;

public class SimpleSecureSessionManager
  extends InMemorySessionManager<SimpleSessionInfo>
{
  @Inject
  private SimpleAuthService _authService;

  @Nonnull
  @Override
  protected SimpleSessionInfo newSessionInfo()
  {
    final OidcKeycloakAccount account = _authService.findAccount();
    final String userID = null == account ? null : account.getKeycloakSecurityContext().getToken().getId();
    final String sessionID = UUID.randomUUID().toString();
    return new SimpleSessionInfo( userID, sessionID );
  }
}
