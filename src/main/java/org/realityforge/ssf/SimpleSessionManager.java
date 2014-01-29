package org.realityforge.ssf;

import java.util.UUID;
import javax.annotation.Nonnull;

/**
 * An extremely simple session manager that uses in memory session management.
 */
public class SimpleSessionManager
  extends InMemorySessionManager<SimpleSessionInfo>
{
  @Nonnull
  protected SimpleSessionInfo newSessionInfo()
  {
    final String sessionID = UUID.randomUUID().toString();
    return new SimpleSessionInfo( sessionID );
  }
}
