package org.realityforge.ssf;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An extremely simple session manager that uses in memory session management.
 */
public abstract class InMemorySessionManager<T extends SessionInfo>
  implements SessionManager<T>, Serializable
{
  private final Map<String, T> _sessions =
    Collections.synchronizedMap( new HashMap<String, T>() );

  @Override
  @Nonnull
  public String getSessionKey()
  {
    return "sid";
  }

  @Override
  public boolean invalidateSession( @Nonnull final String sessionID )
  {
    return null != removeSession( sessionID );
  }

  protected T removeSession( final String sessionID )
  {
    return _sessions.remove( sessionID );
  }

  @Override
  @Nullable
  public T getSession( @Nonnull final String sessionID )
  {
    final T sessionInfo = _sessions.get( sessionID );
    if( null != sessionInfo )
    {
      sessionInfo.updateAccessTime();
    }
    return sessionInfo;
  }

  @Override
  @Nonnull
  public T createSession()
  {
    final T sessionInfo = newSessionInfo();
    _sessions.put( sessionInfo.getSessionID(), sessionInfo );
    return sessionInfo;
  }

  @Nonnull
  protected final Map<String, T> getSessions()
  {
    return _sessions;
  }

  @Nonnull
  protected abstract T newSessionInfo();
}
