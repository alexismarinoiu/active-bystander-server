package uk.avocado.supldap;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.directory.api.ldap.model.cursor.CursorException;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.message.BindRequestImpl;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.*;
import uk.avocado.Configuration;

import java.io.IOException;

public class LdapSupplementor {

  private final LdapConnectionPool pool;

  public LdapSupplementor() {
    final LdapConnectionConfig config = new LdapConnectionConfig();
    config.setLdapHost("unixldap.cc.ic.ac.uk");
    config.setLdapPort(636);
    config.setUseSsl(true);

    final DefaultLdapConnectionFactory factory = new DefaultLdapConnectionFactory(config);
    factory.setTimeOut(10);

    final GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
    poolConfig.lifo = true;
    poolConfig.maxActive = 8;
    poolConfig.maxIdle = 8;
    poolConfig.maxWait = -1L;
    poolConfig.minEvictableIdleTimeMillis = 1000L * 60L * 30L;
    poolConfig.minIdle = 0;
    poolConfig.numTestsPerEvictionRun = 3;
    poolConfig.softMinEvictableIdleTimeMillis = -1L;
    poolConfig.testOnBorrow = false;
    poolConfig.testOnReturn = false;
    poolConfig.testWhileIdle = false;
    poolConfig.timeBetweenEvictionRunsMillis = -1L;
    poolConfig.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;

    this.pool = new LdapConnectionPool(new DefaultPoolableLdapConnectionFactory(factory), poolConfig);
  }

  public String getDisplayName(final String username) {
    LdapConnection connection = null;
    EntryCursor cursor = null;

    try {
      connection = pool.getConnection();
      // Bind, if not already
      connection.bind(new Dn(String.format("uid=%s,ou=People,ou=everyone,dc=ic,dc=ac,dc=uk", Configuration.getInstance().getLdapUsername())), Configuration.getInstance().getLdapPassword());
      cursor = connection.search("ou=People,ou=shibboleth,dc=ic,dc=ac,dc=uk", String.format("(uid=%s)", username), SearchScope.ONELEVEL);

      cursor.next();
      final Entry entry = cursor.get();
      if (entry != null) {
        return entry.get("displayName").getString();
      }
    } catch (LdapException | CursorException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          pool.releaseConnection(connection);
        } catch (LdapException e) { }
      }

      if (cursor != null) {
        try {
          cursor.close();
        } catch (IOException e) { }
      }
    }

    return null;
  }
}
