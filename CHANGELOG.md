## 0.3:
* Backport InMemorySessionManager.removeIdleSessions from downstream applications. This supports removal
  of sessions that exceed an idle threshold.
* Introduce a locking architecture in InMemorySessionManager to protect the session map in a concurrent
  application.
* Make the InMemorySessionManager.getSessions() return an unmodifiable map to avoid invalid modification
  of the sessions map.
* Improve the javadocs of InMemorySessionManager.

## 0.2:
* Rework SimpleSessionInfo to support a mutable sessionID in sub-classes.
* Rework SessionInfo to support setting of arbitrary attributes. Remove accessor for username and treat
  username as a standard attribute.

## 0.1:

* Initial release
