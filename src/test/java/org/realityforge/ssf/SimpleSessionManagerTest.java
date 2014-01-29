package org.realityforge.ssf;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SimpleSessionManagerTest
{
  @Test
  public void basicWorkflow()
    throws Exception
  {

    final SimpleSessionManager sm = new SimpleSessionManager();
    assertEquals( sm.getSessionKey(), "sid" );
    assertEquals( sm.getSession( "MySessionID" ), null );
    final SessionInfo sessionInfo = sm.createSession();
    assertNotNull( sessionInfo );
    assertNotNull( sessionInfo.getSessionID() );
    assertEquals( sessionInfo.getAttribute( "Username" ), null );
    sessionInfo.setAttribute( "Username", "Bob" );
    assertEquals( sessionInfo.getAttribute( "Username" ), "Bob" );
    sessionInfo.removeAttribute( "Username" );
    assertEquals( sessionInfo.getAttribute( "Username" ), null );
    assertEquals( sessionInfo.getCreatedAt(), sessionInfo.getLastAccessedAt() );
    assertTrue( System.currentTimeMillis() - sessionInfo.getCreatedAt() < 100L );
    assertTrue( System.currentTimeMillis() - sessionInfo.getLastAccessedAt() < 100L );
    Thread.sleep( 1 );

    // The next line should update the last accessed time too!
    assertEquals( sm.getSession( sessionInfo.getSessionID() ), sessionInfo );
    assertNotEquals( sessionInfo.getCreatedAt(), sessionInfo.getLastAccessedAt() );

    assertTrue( sm.invalidateSession( sessionInfo.getSessionID() ) );
    assertFalse( sm.invalidateSession( sessionInfo.getSessionID() ) );
    assertNull( sm.getSession( sessionInfo.getSessionID() ) );
  }
}
