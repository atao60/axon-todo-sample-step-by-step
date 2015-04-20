project(modelVersion: '4.0.0')
{

	groupId 'popsuite-axon-samples'
	artifactId 'step4-helloworld'
	version '0.0.1-SNAPSHOT'

	name 'Axon Helloworld (step 4) Sample Project'
	description 'Axon with Xtend and JPA persistence'

	properties {

		/* Project parameters */

		'xtend.outputDir' '${project.build.directory}/xtend-gen/main'
		'xtend.testOutputDir' '${project.build.directory}/xtend-gen/test'

		/* JVM Management */

		'project.build.sourceEncoding' 'UTF-8'
		'project.reporting.outputEncoding' 'UTF-8'

		'jdk.version' '1.8'
		'maven.compiler.source' '${jdk.version}'
		'maven.compiler.target' '${jdk.version}'
		'maven.compiler.compilerVersion' '${jdk.version}'
		'maven.compiler.debug' 'true'
		'maven.compiler.optimize' 'true'
		'maven.compiler.verbose' 'true'
		'maven.compiler.fork' 'true'

		/* Maven and Plugin Management */

		'maven.minimal.version' '3.3.1' // Maven 3.3.1 or above to get Polyglot

		'xtendVersion' '2.7.3' // jnario-1.0.1 seems unable to use xtend 2.8.0
		
		'xtend.maven.version' '${xtendVersion}'
		'build.helper.maven.version' '1.9.1'
		'shade.maven.version' '2.3'
		'clean.maven.version' '2.5'
		'site.maven.version' '3.3'
		'install.maven.version' '2.4'
		'surefire.maven.version' '2.12.4'
		'junit.version' '4.12'
		'resources.maven.version' '2.6'
		'compiler.maven.version' '2.5.1'
		'assembly.maven.version' '2.5.3'
		'jar.maven.version' '2.4'
		'enforcer.maven.version' '1.4'
		'deploy.maven.version' '2.7'

		/* Dependency Management */

		'axon.version' '2.4'
		'xtend.version' '${xtendVersion}'   // atm even jnario-1.0.1 is anable to use xtend 2.8.0

		'hsqldb.version' '2.3.2'
		'hibernate.version' '4.3.8.Final'
		'spring.version' '4.1.6.RELEASE'
		'aspectjweaver.version' '1.8.5'
		
		'hamcrest.version' '1.3'
		'logback.version' '1.1.3'
		'slf4j.version' '1.7.12'

	}
	build {
		plugins {
			plugin('org.eclipse.xtend:xtend-maven-plugin')
			plugin('org.codehaus.mojo:build-helper-maven-plugin')
			plugin('org.apache.maven.plugins:maven-enforcer-plugin')
			plugin('org.apache.maven.plugins:maven-shade-plugin') {
				executions {
					execution {
						phase 'package'
						goals { goal 'shade' }
						configuration {
							transformers {
								transformer(implementation:'org.apache.maven.plugins.shade.resource.ManifestResourceTransformer') {
									mainClass 'helloworld.HelloworldRunner'
								}
								transformer(implementation:'org.apache.maven.plugins.shade.resource.IncludeResourceTransformer') {
									file 'README.md'
									resource 'README.md'
								}
							}
							shadedArtifactAttached 'true'
							shadedClassifierName 'standalone'
						}
					}
				}
			}
		}
		pluginManagement {
			plugins {
				plugin('org.codehaus.mojo:build-helper-maven-plugin:${build.helper.maven.version}') {
					executions {
						execution {
							id 'get-maven-version'
							goals { goal 'maven-version' }
						}
						execution {
							id 'add-source'
							phase 'generate-sources'
							goals { goal 'add-source' }
							configuration {
								sources { source 'src/main/xtend' }
							}
						}
						execution {
							id 'add-test-source'
							phase 'generate-test-sources'
							goals { goal 'add-test-source' }
							configuration {
								sources { source 'src/test/xtend' }
							}
						}
					}
				}
				plugin('org.eclipse.xtend:xtend-maven-plugin:${xtend.maven.version}') {
					executions {
						execution {
							goals {
								goal 'compile'
								goal 'testCompile'
							}
							configuration {
								outputDirectory '${xtend.outputDir}'
								testOutputDirectory '${xtend.testOutputDir}'
							}
						}
					}
				}
				plugin('org.apache.maven.plugins:maven-enforcer-plugin:${enforcer.maven.version}') {
					executions {
						execution {
							id 'enforce-version'
							goals { goal 'enforce' }
							configuration {
								fail 'true'
								rules {
									requireMavenVersion {
										version '${maven.minimal.version}'
										message '''[ERROR] OLD MAVEN [${maven.version}] in use.
                                            Maven ${maven.minimal.version} or newer is required.'''
									}
									requireJavaVersion {
										version '${jdk.version}'
										message '''[ERROR] OLD JDK [${java.version}] in use.
                                            JDK ${jdk.version} or newer is required.'''
									}
									requirePluginVersions {
										banLatest 'true'
										banRelease 'true'
										banSnapshots 'true'
									}
									bannedDependencies {
										searchTransitive 'true'
										excludes {
											exclude 'commons-logging'
											exclude 'log4j'
											exclude 'org.apache.logging.log4j'
										}
									}
								}
							}
						}
					}
				}
				plugin('org.apache.maven.plugins:maven-compiler-plugin:${compiler.maven.version}')
				plugin('org.apache.maven.plugins:maven-surefire-plugin:${surefire.maven.version}')
				plugin('org.apache.maven.plugins:maven-jar-plugin:${jar.maven.version}')
				plugin('org.apache.maven.plugins:maven-clean-plugin:${clean.maven.version}')
				plugin('org.apache.maven.plugins:maven-install-plugin:${install.maven.version}')
				plugin('org.apache.maven.plugins:maven-site-plugin:${site.maven.version}')
				plugin('org.apache.maven.plugins:maven-resources-plugin:${resources.maven.version}')
				plugin('org.apache.maven.plugins:maven-deploy-plugin:${deploy.maven.version}')
				plugin('org.apache.maven.plugins:maven-assembly-plugin:${assembly.maven.version}')
				plugin('org.apache.maven.plugins:maven-shade-plugin:${shade.maven.version}')
			}
		}
	}

	dependencies {
		dependency('popsuite-xtend-contrib:xtend-contrib:0.0.1-SNAPSHOT')
		
		/* main frameworks */
		dependency('org.axonframework:axon-core:${axon.version}') {
			exclusions {exclusion('xmlpull:xmlpull')}
		}
		dependency('xmlpull:xmlpull:1.1.3.1')
		dependency('org.eclipse.xtend:org.eclipse.xtend.lib:${xtend.version}')
		dependency('org.springframework:spring-context-support:${spring.version}')
		{
			exclusions {exclusion('commons-logging:commons-logging')}
		}
		dependency('org.springframework:spring-aspects:${spring.version}')
		dependency('org.aspectj:aspectjweaver:${aspectjweaver.version}')

		/* persistence */
		dependency('org.springframework:spring-orm:${spring.version}')
		dependency('org.hibernate:hibernate-entitymanager:${hibernate.version}')
		dependency('org.hsqldb:hsqldb:${hsqldb.version}')

		/* logs */
		dependency('org.slf4j:log4j-over-slf4j:${slf4j.version}')
		dependency('org.slf4j:jcl-over-slf4j:${slf4j.version}')
		dependency('ch.qos.logback:logback-classic:${logback.version}')

		/* tests */
		dependency('org.springframework:spring-test:${spring.version}:test')
		dependency('org.axonframework:axon-test:${axon.version}:test')
		dependency('junit:junit:${junit.version}:test')
		dependency('org.hamcrest:hamcrest-all:${hamcrest.version}:test')
	}
}
