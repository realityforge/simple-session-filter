package org.realityforge.ssf;

import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.keycloak.representations.JsonWebToken;
import org.realityforge.guiceyloops.server.AssertUtil;
import org.realityforge.guiceyloops.shared.ValueUtil;
import org.realityforge.keycloak.sks.SimpleAuthService;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SimpleSecureSessionManagerTest
{
  @Test
  public void isCdiType()
  {
    AssertUtil.assertNoFinalMethodsForCDI( SimpleSecureSessionManager.class );
  }

  @Test
  public void basicWorkflow()
    throws Exception
  {
    final SimpleAuthService authService = mock( SimpleAuthService.class );
    final SimpleSecureSessionManager sm = new SimpleSecureSessionManager();
    setField( sm, authService );

    final String userID = ValueUtil.randomString();

    final OidcKeycloakAccount account = mock( OidcKeycloakAccount.class );
    final AccessToken token = new AccessToken();
    setField( token, userID );

    final KeycloakSecurityContext context =
      new KeycloakSecurityContext( ValueUtil.randomString(), token, ValueUtil.randomString(), new IDToken() );
    when( account.getKeycloakSecurityContext() ).thenReturn( context );
    when( authService.findAccount() ).thenReturn( account );

    final SessionInfo sessionInfo = sm.createSession();
    assertNotNull( sessionInfo );
    assertEquals( sessionInfo.getUserID(), userID );
  }

  @Test
  public void supportUnAuthenticated()
    throws Exception
  {
    final SimpleAuthService authService = mock( SimpleAuthService.class );
    final SimpleSecureSessionManager sm = new SimpleSecureSessionManager();
    setField( sm, authService );

    when( authService.findAccount() ).thenReturn( null );

    final SessionInfo sessionInfo = sm.createSession();
    assertNotNull( sessionInfo );
    assertEquals( sessionInfo.getUserID(), null );
  }

  private void setField( @Nonnull final JsonWebToken object, @Nonnull final String value )
    throws NoSuchFieldException, IllegalAccessException
  {
    final Field field = JsonWebToken.class.getDeclaredField( "id" );
    field.setAccessible( true );
    field.set( object, value );
  }

  private void setField( @Nonnull final SimpleSecureSessionManager object, @Nonnull final SimpleAuthService value )
    throws NoSuchFieldException, IllegalAccessException
  {
    final Field field = SimpleSecureSessionManager.class.getDeclaredField( "_authService" );
    field.setAccessible( true );
    field.set( object, value );
  }
}
