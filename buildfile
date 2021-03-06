require 'buildr/git_auto_version'
require 'buildr/gpg'

PROVIDED_DEPS = [:javaee_api, :javax_annotation]
KEYCLOAK_DEPS = [:keycloak_adapter_core, :keycloak_adapter_spi, :keycloak_core, :keycloak_common]
OPTIONAL_DEPS = [:simple_keycloak_service] + KEYCLOAK_DEPS

desc 'A simple servlet filter for implementing custom session management'
define 'simple-session-filter' do
  project.group = 'org.realityforge.ssf'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/simple-session-filter')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat PROVIDED_DEPS
  pom.optional_dependencies.concat OPTIONAL_DEPS

  compile.with PROVIDED_DEPS, OPTIONAL_DEPS

  test.using :testng
  test.with :mockito, :guiceyloops, :glassfish_embedded

  package(:jar)
  package(:sources)
  package(:javadoc)

  iml.add_jruby_facet
end
