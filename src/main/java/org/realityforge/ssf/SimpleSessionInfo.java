package org.realityforge.ssf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SimpleSessionInfo
  implements SessionInfo, Serializable
{
  private final String _sessionID;
  private long _createdAt;
  private long _lastAccessedAt;
  private Map<String, Serializable> _attributes = new HashMap<>();

  public SimpleSessionInfo( @Nonnull final String sessionID )
  {
    _sessionID = sessionID;
    _createdAt = _lastAccessedAt = System.currentTimeMillis();
  }

  @Nonnull
  @Override
  public Set<String> getAttributeKeys()
  {
    return _attributes.keySet();
  }

  @Nonnull
  public String getSessionID()
  {
    return _sessionID;
  }

  @Nullable
  @Override
  public Serializable getAttribute( @Nonnull final String key )
  {
    return _attributes.get( key );
  }

  @Override
  public void setAttribute( @Nonnull final String key, @Nonnull final Serializable value )
  {
    _attributes.put( key, value );
  }

  @Override
  public void removeAttribute( @Nonnull final String key )
  {
    _attributes.remove( key );
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
