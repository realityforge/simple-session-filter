package org.realityforge.ssf;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class that simplifies creation of HTTP filters.
 */
abstract class AbstractHttpFilter
  implements Filter
{
  public void init( final FilterConfig filterConfig )
    throws ServletException
  {
  }

  public void destroy()
  {
  }

  public final void doFilter( final ServletRequest servletRequest,
                              final ServletResponse servletResponse,
                              final FilterChain chain )
    throws IOException, ServletException
  {
    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;

    doFilter( request, response, chain );
  }

  /**
   * Sub-classes need to implement this method to provide functionality.
   */
  protected abstract void doFilter( final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain chain )
    throws IOException, ServletException;
}
