package org.realityforge.ssf;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An extremely simple session manager that uses in memory session management.
 */
public class SimpleSessionManager
  implements SessionManager, Serializable
{
  private final Map<String, SimpleSessionInfo> _sessions =
    Collections.synchronizedMap( new HashMap<String, SimpleSessionInfo>() );

  @Override
  @Nonnull
  public String getSessionKey()
  {
    return "sid";
  }

  @Override
  public boolean invalidateSession( @Nonnull final String sessionID )
  {
    return null != _sessions.remove( sessionID );
  }

  @Override
  @Nullable
  public SessionInfo getSession( @Nonnull final String sessionID )
  {
    final SimpleSessionInfo sessionInfo = _sessions.get( sessionID );
    if( null != sessionInfo )
    {
      sessionInfo.updateAccessTime();
    }
    return sessionInfo;
  }

  @Override
  @Nonnull
  public SessionInfo createSession( @Nonnull final String username )
  {
    final String sessionID = UUID.randomUUID().toString();
    final SimpleSessionInfo sessionInfo = new SimpleSessionInfo( sessionID, username );
    _sessions.put( sessionInfo.getSessionID(), sessionInfo );
    return sessionInfo;
  }
}
