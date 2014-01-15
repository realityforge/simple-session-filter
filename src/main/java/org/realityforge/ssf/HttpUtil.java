package org.realityforge.ssf;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * A utility class that simplifies interaction with the servlet API.
 */
public final class HttpUtil
{
  private HttpUtil()
  {
  }

  /**
   * Return the fully reconstructed URL of the servlet context.
   *
   * @param request the incoming request.
   * @return the url that the client used to access the site.
   */
  @Nonnull
  public static StringBuffer getContextURL( @Nonnull final HttpServletRequest request )
  {
    final StringBuffer url = new StringBuffer();
    final String scheme = request.getScheme();
    final int port = request.getServerPort();

    url.append( scheme ); // http, https
    url.append( "://" );
    url.append( request.getServerName() );
    if ( ( "http".equals( scheme ) && 80 != port ) || ( "https".equals( scheme ) && 443 != port ) )
    {
      url.append( ':' );
      url.append( port );
    }
    url.append( request.getContextPath() );

    return url;
  }

  /**
   * Return the component of the uri below the context.
   *
   * @param request the incoming request.
   * @return the context local path.
   */
  @Nonnull
  public static String getContextLocalPath( @Nonnull final HttpServletRequest request )
  {
    final String requestURI = request.getRequestURI();
    final String contextPath = request.getServletContext().getContextPath();
    return requestURI.substring( contextPath.length() );
  }

  /**
   * Return the value of the cookie with the specified name or null if no such cookie.
   *
   * @param request the incoming request.
   * @param cookieName the name of the cookie.
   * @return the value of the cookie, if it exists, null otherwise.
   */
  @Nullable
  public static Cookie findCookie( final HttpServletRequest request, final String cookieName )
  {
    final Cookie[] cookies = request.getCookies();
    if ( null != cookies )
    {
      for ( final Cookie cookie : cookies )
      {
        if ( cookie.getName().equals( cookieName ) )
        {
          return cookie;
        }
      }
    }
    return null;
  }
}
