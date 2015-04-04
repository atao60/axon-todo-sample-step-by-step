project {
	modelVersion '4.0.0'

	groupId 'popsuite-axon-samples'
	artifactId 'step3-helloworld'
	version '0.0.1-SNAPSHOT'

	name 'Axon Helloworld (step 3) Sample Project'
	description 'Axon with Xtend using Polyglot for Maven'

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

		'build.helper.maven.version' '1.9.1'
		'shade.maven.version' '2.3'
		'clean.maven.version' '2.5'
		'site.maven.version' '3.3'
		'xtend.maven.version' '2.7.3'
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
		'xtend.version' '2.7.3'   // atm even jnario-1.0.1 is anable to use xtend 2.8.0
		'hamcrest.version' '1.3'
		'logback.version' '1.1.3'
		'slf4j.version' '1.7.12'

	}
	build {
		plugins {
			plugin { artifactId 'maven-clean-plugin' }
			plugin {
				groupId 'org.codehaus.mojo'
				artifactId 'build-helper-maven-plugin'
			}
			plugin {
				groupId 'org.eclipse.xtend'
				artifactId 'xtend-maven-plugin'
			}
			plugin { artifactId 'maven-enforcer-plugin' }
			plugin {
				artifactId 'maven-shade-plugin'
				executions {
					execution {
						phase 'package'
						goals { goal 'shade' }
						configuration {
							transformers {
								transformer(implementation:'org.apache.maven.plugins.shade.resource.ManifestResourceTransformer') { mainClass 'helloworld.HelloworldRunner' }
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
				plugin {
					groupId 'org.codehaus.mojo'
					artifactId 'build-helper-maven-plugin'
					version '${build.helper.maven.version}'
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
				plugin {
					groupId 'org.eclipse.xtend'
					artifactId 'xtend-maven-plugin'
					version '${xtend.maven.version}'
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
				plugin {
					artifactId 'maven-enforcer-plugin'
					version '${enforcer.maven.version}'
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
				plugin {
					artifactId 'maven-compiler-plugin'
					version '${compiler.maven.version}'
				}
				plugin {
					artifactId 'maven-surefire-plugin'
					version '${surefire.maven.version}'
				}
				plugin {
					artifactId 'maven-jar-plugin'
					version '${jar.maven.version}'
				}
				plugin {
					artifactId 'maven-clean-plugin'
					version '${clean.maven.version}'
				}
				plugin {
					artifactId 'maven-install-plugin'
					version '${install.maven.version}'
				}
				plugin {
					artifactId 'maven-site-plugin'
					version '${site.maven.version}'
				}
				plugin {
					artifactId 'maven-resources-plugin'
					version '${resources.maven.version}'
				}
				plugin {
					artifactId 'maven-deploy-plugin'
					version '${deploy.maven.version}'
				}
				plugin {
					artifactId 'maven-assembly-plugin'
					version '${assembly.maven.version}'
				}
				plugin {
					artifactId 'maven-shade-plugin'
					version '${shade.maven.version}'
				}
			}
		}
	}

	dependencies {
		dependency {
			groupId 'popsuite-xtend-contrib'
			artifactId 'xtend-contrib'
			version '0.0.1-SNAPSHOT'
		}
		dependency {
			groupId 'org.axonframework'
			artifactId 'axon-core'
			version '${axon.version}'
		}
		dependency {
			groupId 'org.axonframework'
			artifactId 'axon-test'
			version '${axon.version}'
			scope 'test'
		}
		dependency {
			groupId 'org.eclipse.xtend'
			artifactId 'org.eclipse.xtend.lib'
			version '${xtend.version}'
		}
		dependency {
			groupId 'org.slf4j'
			artifactId 'log4j-over-slf4j'
			version '${slf4j.version}'
		}
		dependency {
			groupId 'ch.qos.logback'
			artifactId 'logback-classic'
			version '${logback.version}'
		}
		dependency {
			groupId 'junit'
			artifactId 'junit'
			version '${junit.version}'
			scope 'test'
		}
		dependency {
			groupId 'org.hamcrest'
			artifactId 'hamcrest-all'
			version '${hamcrest.version}'
			scope 'test'
		}
	}
}
