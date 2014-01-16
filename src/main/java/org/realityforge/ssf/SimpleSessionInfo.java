package org.realityforge.ssf;

import java.io.Serializable;
import javax.annotation.Nonnull;

public class SimpleSessionInfo
  implements SessionInfo, Serializable
{
  private final String _sessionID;
  private final String _username;
  private long _createdAt;
  private long _lastAccessedAt;

  public SimpleSessionInfo( @Nonnull final String sessionID, @Nonnull final String username )
  {
    _sessionID = sessionID;
    _username = username;
    _createdAt = _lastAccessedAt = System.currentTimeMillis();
  }

  @Nonnull
  public String getSessionID()
  {
    return _sessionID;
  }

  @Nonnull
  public String getUsername()
  {
    return _username;
  }

  public long getCreatedAt()
  {
    return _createdAt;
  }

  public long getLastAccessedAt()
  {
    return _lastAccessedAt;
  }

  public void updateAccessTime()
  {
    _lastAccessedAt = System.currentTimeMillis();
  }
}
