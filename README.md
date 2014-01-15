simple-session-filter
=====================

[![Build Status](https://secure.travis-ci.org/realityforge/simple-session-filter.png?branch=master)](http://travis-ci.org/realityforge/simple-session-filter)

A very simple servlet filter that makes it easy to perform custom session management.

Quick Start
-----------

The simplest way to start is to define a session filter as below.

```java
@WebFilter( urlPatterns = "/*" )
public class SessionFilter
  extends AbstractSessionFilter
{
  @EJB
  private SessionManager _sessionManager;

  @Override
  protected boolean handleInvalid( final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain chain )
    throws IOException, ServletException
  {
    // redirect to url that will perform authentication and session creation
    response.sendRedirect( HttpUtil.getContextURL( request ).append( "/api/auth/login" ).toString() );
    return true;
  }

  @Override
  public SessionManager getSessionManager()
  {
    return _sessionManager;
  }

  @Override
  protected boolean isSessionCheckRequired( final HttpServletRequest request )
    throws IOException, ServletException
  {
    final String method = request.getMethod();
    final String requestURI = HttpUtil.getContextLocalPath( request );
    if ( "GET".equals( method ) )
    {
      return !( requestURI.startsWith( "/images/" ) ||
                requestURI.startsWith( "/stylesheets/" ) ||
                requestURI.startsWith( "/api/auth/" ) ||
                requestURI.equals( "/favicon.ico" ) );
    }
    else
    {
      return "POST".equals( method ) && !requestURI.startsWith( "/api/auth/" );
    }
  }
}
```
