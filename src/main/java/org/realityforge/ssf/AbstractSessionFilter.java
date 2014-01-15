package org.realityforge.ssf;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class AbstractSessionFilter
  extends AbstractHttpFilter
{
  public abstract SessionManager getSessionManager();

  protected final void doFilter( final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final FilterChain chain )
    throws IOException, ServletException
  {
    if ( isSessionCheckRequired( request ) )
    {
      Cookie cookie = HttpUtil.findCookie( request, getSessionManager().getSessionKey() );
      if ( !( null != cookie && null != getSessionManager().getSession( cookie.getValue() ) ) )
      {
        // Cookie session key is not valid according to the session manager
        // so we set the age to 0 to ensure the cookie is deleted.
        if ( null != cookie )
        {
          cookie.setMaxAge( 0 );
          response.addCookie( cookie );
        }
        if ( handleInvalid( request, response, chain ) )
        {
          return;
        }
      }
    }

    removeContainerSession( request );
    chain.doFilter( request, response );
  }

  /**
   * Override to handle requests where a session is expected but no (valid) session is present.
   * The method should return true if scenario was handled otherwise filter will forward request
   * onto next element of the filter chain.
   */
  protected abstract boolean handleInvalid( final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain )
    throws IOException, ServletException;

  /**
   * Return true if the request should check for a session.
   */
  protected abstract boolean isSessionCheckRequired( final HttpServletRequest request )
    throws IOException, ServletException;

  private void removeContainerSession( final HttpServletRequest request )
  {
    final HttpSession session = request.getSession();
    if ( null != session )
    {
      session.invalidate();
    }
  }
}
