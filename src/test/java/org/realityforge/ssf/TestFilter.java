package org.realityforge.ssf;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;

class TestFilter
  extends AbstractSessionFilter
{
  private final SessionManager _sessionManager = mock( SessionManager.class );
  private final boolean _sessionRequired;
  private final boolean _invalidHandled;
  private boolean _handleInvalidCalled;

  TestFilter( final boolean sessionRequired, final boolean invalidHandled )
  {
    _sessionRequired = sessionRequired;
    _invalidHandled = invalidHandled;
  }

  @Override
  public SessionManager getSessionManager()
  {
    return _sessionManager;
  }

  boolean isHandleInvalidCalled()
  {
    return _handleInvalidCalled;
  }

  @Override
  protected boolean handleInvalid( final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final FilterChain chain )
    throws IOException, ServletException
  {
    _handleInvalidCalled = true;
    return _invalidHandled;
  }

  @Override
  protected boolean isSessionCheckRequired( final HttpServletRequest request )
    throws IOException, ServletException
  {
    return _sessionRequired;
  }
}
