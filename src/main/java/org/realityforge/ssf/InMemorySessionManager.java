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

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String getSessionKey()
  {
    return "sid";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean invalidateSession( @Nonnull final String sessionID )
  {
    return null != removeSession( sessionID );
  }

  /**
   * Remove session with specified id.
   *
   * @param sessionID the session id.
   * @return the session removed if any.
   */
  protected T removeSession( final String sessionID )
  {
    return _sessions.remove( sessionID );
  }

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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

  /**
   * Override method to create a new session.
   *
   * @return the new session.
   */
  @Nonnull
  protected abstract T newSessionInfo();
}
