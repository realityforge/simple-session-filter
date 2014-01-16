package org.realityforge.ssf;

import javax.annotation.Nonnull;

/**
 * A basic interface defining a session.
 */
public interface SessionInfo
{
  /**
   * @return an opaque ID representing session.
   */
  @Nonnull
  String getSessionID();

  /**
   * @return the username for session if available.
   */
  @Nonnull
  String getUsername();

  /**
   * @return the time at which session was created.
   */
  long getCreatedAt();

  /**
   * @return the time at which session was last accessed.
   */
  long getLastAccessedAt();

  /**
   * Update the access time to now.
   */
  void updateAccessTime();
}
