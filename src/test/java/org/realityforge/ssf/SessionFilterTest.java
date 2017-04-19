package org.realityforge.ssf;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SessionFilterTest
{
  @Test
  public void skipSessionCheck()
    throws Exception
  {
    final TestFilter filter = new TestFilter( false, false );
    final HttpServletRequest request = mock( HttpServletRequest.class );
    final HttpServletResponse response = mock( HttpServletResponse.class );
    final FilterChain chain = mock( FilterChain.class );

    doFilter( filter, request, response, chain );

    verifyNextInChainInvoked( request, response, chain );
  }

  @Test
  public void nativeSessionInvalidated()
    throws Exception
  {
    final TestFilter filter = new TestFilter( false, false );
    final HttpServletRequest request = mock( HttpServletRequest.class );
    final HttpServletResponse response = mock( HttpServletResponse.class );
    final FilterChain chain = mock( FilterChain.class );

    final HttpSession session = setupSession( request );
    doFilter( filter, request, response, chain );
    verifySessionInvalidated( session );
  }

  @Test
  public void sessionChecked()
    throws Exception
  {
    final TestFilter filter = new TestFilter( true, false );
    final HttpServletRequest request = mock( HttpServletRequest.class );
    final HttpServletResponse response = mock( HttpServletResponse.class );
    final FilterChain chain = mock( FilterChain.class );

    final String sid = "sid";
    final String sessionID = "1234";

    when( filter.getSessionManager().getSessionKey() ).thenReturn( sid );
    when( request.getCookies() ).thenReturn( new Cookie[]{ new Cookie( sid, sessionID ) } );
    when( filter.getSessionManager().getSession( sessionID ) ).thenReturn( new SimpleSessionInfo( null, sessionID ) );

    doFilter( filter, request, response, chain );

    verifyNextInChainInvoked( request, response, chain );
  }

  @Test
  public void sessionCheckedButInvalid()
    throws Exception
  {
    final TestFilter filter = new TestFilter( true, false );
    final HttpServletRequest request = mock( HttpServletRequest.class );
    final HttpServletResponse response = mock( HttpServletResponse.class );
    final FilterChain chain = mock( FilterChain.class );

    final String sid = "sid";
    final String sessionID = "1234";

    when( filter.getSessionManager().getSessionKey() ).thenReturn( sid );
    when( request.getCookies() ).thenReturn( new Cookie[]{ new Cookie( sid, sessionID ) } );
    when( filter.getSessionManager().getSession( sessionID ) ).thenReturn( null );

    doFilter( filter, request, response, chain );

    assertTrue( filter.isHandleInvalidCalled() );

    final Cookie cookie = new Cookie( sid, sessionID );
    cookie.setMaxAge( 0 );
    verify( response ).addCookie( refEq( cookie ) );

    verifyNextInChainInvoked( request, response, chain );
  }


  @Test
  public void sessionCheckedButInvalidAndHandled()
    throws Exception
  {
    final TestFilter filter = new TestFilter( true, true );
    final HttpServletRequest request = mock( HttpServletRequest.class );
    final HttpServletResponse response = mock( HttpServletResponse.class );
    final FilterChain chain = mock( FilterChain.class );

    final String sid = "sid";

    when( filter.getSessionManager().getSessionKey() ).thenReturn( sid );
    when( request.getCookies() ).thenReturn( null );

    doFilter( filter, request, response, chain );

    assertTrue( filter.isHandleInvalidCalled() );

    verifyNextInChainNotInvoked( request, response, chain );
  }

  private void verifySessionInvalidated( final HttpSession session )
  {
    verify( session ).invalidate();
  }

  private HttpSession setupSession( final HttpServletRequest request )
  {
    final HttpSession session = mock( HttpSession.class );
    when( request.getSession() ).thenReturn( session );
    return session;
  }

  private void verifyNextInChainInvoked( final HttpServletRequest request,
                                         final HttpServletResponse response,
                                         final FilterChain chain )
    throws IOException, ServletException
  {
    verify( chain, times( 1 ) ).doFilter( request, response );
  }

  private void verifyNextInChainNotInvoked( final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain )
    throws IOException, ServletException
  {
    verify( chain, never() ).doFilter( request, response );
  }

  private void doFilter( final Filter filter,
                         final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain )
    throws IOException, ServletException
  {
    filter.init( null );
    filter.doFilter( request, response, chain );
    filter.destroy();
  }
}
