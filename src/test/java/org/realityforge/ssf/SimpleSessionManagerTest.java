package org.realityforge.ssf;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReadWriteLock;
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

    // Make sure we can also get it thorugh the map interface
    assertEquals( sm.getSessions().get( sessionInfo.getSessionID() ), sessionInfo );

    // The next line should update the last accessed time too!
    assertEquals( sm.getSession( sessionInfo.getSessionID() ), sessionInfo );
    assertNotEquals( sessionInfo.getCreatedAt(), sessionInfo.getLastAccessedAt() );

    assertTrue( sm.invalidateSession( sessionInfo.getSessionID() ) );
    assertFalse( sm.invalidateSession( sessionInfo.getSessionID() ) );
    assertNull( sm.getSession( sessionInfo.getSessionID() ) );
  }

  @Test
  public void locking()
    throws Exception
  {
    final SimpleSessionManager sm = new SimpleSessionManager();
    final ReadWriteLock lock = sm.getLock();

    // Variable used to pass data back from threads
    final SimpleSessionInfo[] sessions = new SimpleSessionInfo[ 2 ];

    lock.readLock().lock();

    // Make sure createSession can not complete if something has a read lock
    final CyclicBarrier end = go( new Runnable()
    {
      @Override
      public void run()
      {
        sessions[ 0 ] = sm.createSession();
      }
    } );

    assertNull( sessions[ 0 ] );
    lock.readLock().unlock();
    end.await();
    assertNotNull( sessions[ 0 ] );

    lock.writeLock().lock();

    // Make sure getSession can acquire a read lock
    final CyclicBarrier end2 = go( new Runnable()
    {
      @Override
      public void run()
      {
        sessions[ 1 ] = sm.getSession( sessions[ 0 ].getSessionID() );
      }
    } );

    assertNull( sessions[ 1 ] );
    lock.writeLock().unlock();
    end2.await();
    assertNotNull( sessions[ 1 ] );

    lock.readLock().lock();

    final Boolean[] invalidated = new Boolean[ 1 ];
    // Make sure createSession can not complete if something has a read lock
    final CyclicBarrier end3 = go( new Runnable()
    {
      @Override
      public void run()
      {
        invalidated[ 0 ] = sm.invalidateSession( sessions[ 0 ].getSessionID() );
      }
    } );

    assertNull( invalidated[ 0 ] );
    lock.readLock().unlock();
    end3.await();
    assertEquals( invalidated[ 0 ], Boolean.TRUE );
  }

  private CyclicBarrier go( final Runnable target )
    throws Exception
  {
    final CyclicBarrier start = new CyclicBarrier( 2 );
    final CyclicBarrier stop = new CyclicBarrier( 2 );
    new Thread( new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          start.await();
          target.run();
          stop.await();
        }
        catch ( Exception e )
        {
          // Ignored
        }
      }
    } ).start();
    start.await();
    Thread.sleep( 1 );
    return stop;
  }


  @Test
  public void removeIdleSessions()
    throws Exception
  {
    final SimpleSessionManager sm = new SimpleSessionManager();
    final SimpleSessionInfo session = sm.createSession();
    final long accessedAt = session.getLastAccessedAt();
    while ( System.currentTimeMillis() == accessedAt )
    {
      Thread.yield();
    }
    final int removeCount = sm.removeIdleSessions( 10000 );
    assertEquals( removeCount, 0 );
    assertEquals( sm.getSessions().get( session.getSessionID() ), session );

    final int removeCount2 = sm.removeIdleSessions( 0 );
    assertEquals( removeCount2, 1 );
    assertNull( sm.getSessions().get( session.getSessionID() ) );
  }
}
