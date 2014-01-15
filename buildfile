require 'buildr/git_auto_version'

desc 'A simple servlet filter for implementing custom session management'
define 'simple-session-filter' do
  project.group = 'org.realityforge.ssf'
  compile.options.source = '1.7'
  compile.options.target = '1.7'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache2_license
  pom.add_github_project('realityforge/simple-session-filter')
  pom.add_developer('realityforge', 'Peter Donald')
  pom.provided_dependencies.concat [:javax_servlet]

  compile.with :javax_servlet

  package(:jar)
  package(:sources)
  package(:javadoc)

  iml.add_jruby_facet
end
