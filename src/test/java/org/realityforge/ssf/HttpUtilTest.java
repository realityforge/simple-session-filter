package org.realityforge.ssf;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class HttpUtilTest
{
  @DataProvider( name = "contextURL" )
  public Object[][] createData1()
  {
    return new Object[][]{
      { "http", 80, "example.com", "/foo", "http://example.com/foo" },
      { "http", 80, "example.com", "/", "http://example.com/" },
      { "http", 83, "example.com", "/foo", "http://example.com:83/foo" },
      { "https", 83, "example.com", "/foo", "https://example.com:83/foo" },
      { "https", 443, "example.com", "/foo", "https://example.com/foo" },
    };
  }

  @Test( dataProvider = "contextURL" )
  public void getContextURL( final String scheme,
                             final int port,
                             final String serverName,
                             final String contextPath,
                             final String expected )
  {
    final HttpServletRequest request = mock( HttpServletRequest.class );
    when( request.getScheme() ).thenReturn( scheme );
    when( request.getServerPort() ).thenReturn( port );
    when( request.getServerName() ).thenReturn( serverName );
    when( request.getContextPath() ).thenReturn( contextPath );
    assertEquals( HttpUtil.getContextURL( request ).toString(), expected );
  }

  @Test
  public void findCookie()
  {
    final Cookie c1 = new Cookie( "c1", "v1" );
    final Cookie c2 = new Cookie( "c2", "v2" );
    testFindCookies( null, "Foo", null );
    testFindCookies( new Cookie[0], c1.getName(), null );
    testFindCookies( new Cookie[]{ c1 }, c1.getName(), c1 );
    testFindCookies( new Cookie[]{ c2 }, c1.getName(), null );
    testFindCookies( new Cookie[]{ c1, c2 }, c1.getName(), c1 );
  }

  private void testFindCookies( final Cookie[] cookies, final String name, final Cookie expected )
  {
    final HttpServletRequest request = mock( HttpServletRequest.class );
    when( request.getCookies() ).thenReturn( cookies );
    assertEquals( HttpUtil.findCookie( request, name ), expected );
  }
}
