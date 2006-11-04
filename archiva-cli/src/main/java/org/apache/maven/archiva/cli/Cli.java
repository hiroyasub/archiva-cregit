begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|cli
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|CommandLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|cli
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|Archiva
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|discoverer
operator|.
name|DiscovererException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|converter
operator|.
name|RepositoryConversionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|classworlds
operator|.
name|ClassWorld
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|DefaultPlexusContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusContainerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author jason van zyl  * @version $Id$  * @noinspection UseOfSystemOutOrSystemErr,ACCESS_STATIC_VIA_INSTANCE  * @todo complete separate out the general cli processing  * @todo create a simple component to do the invocation  */
end_comment

begin_class
specifier|public
class|class
name|Cli
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|ClassWorld
name|classWorld
init|=
operator|new
name|ClassWorld
argument_list|(
literal|"plexus.core"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|result
init|=
name|main
argument_list|(
name|args
argument_list|,
name|classWorld
argument_list|)
decl_stmt|;
name|System
operator|.
name|exit
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**      * @noinspection ConfusingMainMethod      */
specifier|public
specifier|static
name|int
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|,
name|ClassWorld
name|classWorld
parameter_list|)
block|{
comment|// ----------------------------------------------------------------------
comment|// Setup the command line parser
comment|// ----------------------------------------------------------------------
name|CliManager
name|cliManager
init|=
operator|new
name|CliManager
argument_list|()
decl_stmt|;
name|CommandLine
name|cli
decl_stmt|;
try|try
block|{
name|cli
operator|=
name|cliManager
operator|.
name|parse
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable to parse command line options: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|cliManager
operator|.
name|displayHelp
argument_list|()
expr_stmt|;
return|return
literal|1
return|;
block|}
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.version"
argument_list|,
literal|"44.0"
argument_list|)
operator|.
name|compareTo
argument_list|(
literal|"48.0"
argument_list|)
operator|<
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Sorry, but JDK 1.4 or above is required to execute Maven"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"You appear to be using Java version: "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.version"
argument_list|,
literal|"<unknown>"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
name|boolean
name|debug
init|=
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|DEBUG
argument_list|)
decl_stmt|;
name|boolean
name|quiet
init|=
operator|!
name|debug
operator|&&
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|QUIET
argument_list|)
decl_stmt|;
name|boolean
name|showErrors
init|=
name|debug
operator|||
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|ERRORS
argument_list|)
decl_stmt|;
if|if
condition|(
name|showErrors
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"+ Error stacktraces are turned on."
argument_list|)
expr_stmt|;
block|}
comment|// ----------------------------------------------------------------------------
comment|// Logging
comment|// ----------------------------------------------------------------------------
name|int
name|loggingLevel
decl_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|loggingLevel
operator|=
literal|0
expr_stmt|;
comment|//MavenExecutionRequest.LOGGING_LEVEL_DEBUG;
block|}
if|else if
condition|(
name|quiet
condition|)
block|{
name|loggingLevel
operator|=
literal|0
expr_stmt|;
comment|//MavenExecutionRequest.LOGGING_LEVEL_ERROR;
block|}
else|else
block|{
name|loggingLevel
operator|=
literal|0
expr_stmt|;
comment|//MavenExecutionRequest.LOGGING_LEVEL_INFO;
block|}
comment|// ----------------------------------------------------------------------
comment|// Process particular command line options
comment|// ----------------------------------------------------------------------
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|HELP
argument_list|)
condition|)
block|{
name|cliManager
operator|.
name|displayHelp
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|VERSION
argument_list|)
condition|)
block|{
name|showVersion
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
if|else if
condition|(
name|debug
condition|)
block|{
name|showVersion
argument_list|()
expr_stmt|;
block|}
comment|// ----------------------------------------------------------------------------
comment|// This is what we will generalize for the invocation of the command line.
comment|// ----------------------------------------------------------------------------
try|try
block|{
name|PlexusContainer
name|plexus
init|=
operator|new
name|DefaultPlexusContainer
argument_list|(
literal|"plexus.core"
argument_list|,
name|classWorld
argument_list|)
decl_stmt|;
name|Archiva
name|archiva
init|=
operator|(
name|Archiva
operator|)
name|plexus
operator|.
name|lookup
argument_list|(
name|Archiva
operator|.
name|ROLE
argument_list|)
decl_stmt|;
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|CONVERT
argument_list|)
condition|)
block|{
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|OLD_REPOSITORY_PATH
argument_list|)
operator|&&
name|cli
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|NEW_REPOSITORY_PATH
argument_list|)
condition|)
block|{
name|File
name|oldRepositoryPath
init|=
operator|new
name|File
argument_list|(
name|cli
operator|.
name|getOptionValue
argument_list|(
name|CliManager
operator|.
name|OLD_REPOSITORY_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|newRepositoryPath
init|=
operator|new
name|File
argument_list|(
name|cli
operator|.
name|getOptionValue
argument_list|(
name|CliManager
operator|.
name|NEW_REPOSITORY_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Converting "
operator|+
name|oldRepositoryPath
operator|+
literal|" to "
operator|+
name|newRepositoryPath
argument_list|)
expr_stmt|;
try|try
block|{
name|archiva
operator|.
name|convertLegacyRepository
argument_list|(
name|oldRepositoryPath
argument_list|,
name|newRepositoryPath
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryConversionException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|""
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DiscovererException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|""
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"You need to specify both a repository to convert and the path for the repository that will be created."
argument_list|)
expr_stmt|;
name|cliManager
operator|.
name|displayHelp
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|PlexusContainerException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|"Cannot create Plexus container."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ComponentLookupException
name|e
parameter_list|)
block|{
name|showError
argument_list|(
literal|"Cannot lookup application component."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
literal|0
return|;
block|}
specifier|private
specifier|static
name|int
name|showFatalError
parameter_list|(
name|String
name|message
parameter_list|,
name|Exception
name|e
parameter_list|,
name|boolean
name|show
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"FATAL ERROR: "
operator|+
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|show
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error stacktrace:"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"For more information, run with the -e flag"
argument_list|)
expr_stmt|;
block|}
return|return
literal|1
return|;
block|}
specifier|private
specifier|static
name|void
name|showError
parameter_list|(
name|String
name|message
parameter_list|,
name|Exception
name|e
parameter_list|,
name|boolean
name|show
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|show
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error stacktrace:"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Need to get the versions of the application in a general way, so that I need a way to get the
comment|// specifics of the application so that I can do this in a general way.
specifier|private
specifier|static
name|void
name|showVersion
parameter_list|()
block|{
name|InputStream
name|resourceAsStream
decl_stmt|;
try|try
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|resourceAsStream
operator|=
name|Cli
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"META-INF/maven/org.apache.maven/maven-core/pom.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|resourceAsStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"builtOn"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Maven version: "
operator|+
name|properties
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|,
literal|"unknown"
argument_list|)
operator|+
literal|" built on "
operator|+
name|properties
operator|.
name|getProperty
argument_list|(
literal|"builtOn"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Maven version: "
operator|+
name|properties
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|,
literal|"unknown"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Unable determine version from JAR file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ----------------------------------------------------------------------
comment|// System properties handling
comment|// ----------------------------------------------------------------------
specifier|private
specifier|static
name|Properties
name|getExecutionProperties
parameter_list|(
name|CommandLine
name|commandLine
parameter_list|)
block|{
name|Properties
name|executionProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
comment|// ----------------------------------------------------------------------
comment|// Options that are set on the command line become system properties
comment|// and therefore are set in the session properties. System properties
comment|// are most dominant.
comment|// ----------------------------------------------------------------------
if|if
condition|(
name|commandLine
operator|.
name|hasOption
argument_list|(
name|CliManager
operator|.
name|SET_SYSTEM_PROPERTY
argument_list|)
condition|)
block|{
name|String
index|[]
name|defStrs
init|=
name|commandLine
operator|.
name|getOptionValues
argument_list|(
name|CliManager
operator|.
name|SET_SYSTEM_PROPERTY
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|defStrs
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|setCliProperty
argument_list|(
name|defStrs
index|[
name|i
index|]
argument_list|,
name|executionProperties
argument_list|)
expr_stmt|;
block|}
block|}
name|executionProperties
operator|.
name|putAll
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|executionProperties
return|;
block|}
specifier|private
specifier|static
name|void
name|setCliProperty
parameter_list|(
name|String
name|property
parameter_list|,
name|Properties
name|executionProperties
parameter_list|)
block|{
name|String
name|name
decl_stmt|;
name|String
name|value
decl_stmt|;
name|int
name|i
init|=
name|property
operator|.
name|indexOf
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|<=
literal|0
condition|)
block|{
name|name
operator|=
name|property
operator|.
name|trim
argument_list|()
expr_stmt|;
name|value
operator|=
literal|"true"
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|property
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|value
operator|=
name|property
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|executionProperties
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// ----------------------------------------------------------------------
comment|// I'm leaving the setting of system properties here as not to break
comment|// the SystemPropertyProfileActivator. This won't harm embedding. jvz.
comment|// ----------------------------------------------------------------------
name|System
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

