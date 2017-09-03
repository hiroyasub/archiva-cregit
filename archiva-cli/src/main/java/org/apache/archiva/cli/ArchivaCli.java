begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|cli
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|sampullara
operator|.
name|cli
operator|.
name|Args
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sampullara
operator|.
name|cli
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|ConsumerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|KnownRepositoryContentConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|RepositoryContentConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|apache
operator|.
name|archiva
operator|.
name|converter
operator|.
name|legacy
operator|.
name|LegacyRepositoryConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
operator|.
name|RepositoryScanStatistics
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
operator|.
name|RepositoryScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
operator|.
name|RepositoryScannerException
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
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * ArchivaCli  *<p>  * TODO add back reading of archiva.xml from a given location  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaCli
block|{
comment|// ----------------------------------------------------------------------------
comment|// Properties controlling Repository conversion
comment|// ----------------------------------------------------------------------------
specifier|public
specifier|static
specifier|final
name|String
name|SOURCE_REPO_PATH
init|=
literal|"sourceRepositoryPath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TARGET_REPO_PATH
init|=
literal|"targetRepositoryPath"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BLACKLISTED_PATTERNS
init|=
literal|"blacklistPatterns"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|POM_PROPERTIES
init|=
literal|"/META-INF/maven/org.apache.archiva/archiva-cli/pom.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArchivaCli
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|String
name|getVersion
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|pomStream
init|=
name|ArchivaCli
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|POM_PROPERTIES
argument_list|)
init|)
block|{
if|if
condition|(
name|pomStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to load "
operator|+
name|POM_PROPERTIES
argument_list|)
throw|;
block|}
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|pomStream
argument_list|)
expr_stmt|;
return|return
name|properties
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|)
return|;
block|}
block|}
specifier|private
name|ClassPathXmlApplicationContext
name|applicationContext
decl_stmt|;
specifier|public
name|ArchivaCli
parameter_list|()
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|Commands
name|command
init|=
operator|new
name|Commands
argument_list|()
decl_stmt|;
try|try
block|{
name|Args
operator|.
name|parse
argument_list|(
name|command
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|Args
operator|.
name|usage
argument_list|(
name|command
argument_list|)
expr_stmt|;
return|return;
block|}
name|ArchivaCli
name|cli
init|=
operator|new
name|ArchivaCli
argument_list|()
decl_stmt|;
try|try
block|{
name|cli
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cli
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|destroy
parameter_list|()
block|{
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|execute
parameter_list|(
name|Commands
name|command
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|command
operator|.
name|help
condition|)
block|{
name|Args
operator|.
name|usage
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|command
operator|.
name|version
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Version: {}"
argument_list|,
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|command
operator|.
name|convert
condition|)
block|{
name|doConversion
argument_list|(
name|command
operator|.
name|properties
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|command
operator|.
name|scan
condition|)
block|{
if|if
condition|(
name|command
operator|.
name|repository
operator|==
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"The repository must be specified."
argument_list|)
expr_stmt|;
name|Args
operator|.
name|usage
argument_list|(
name|command
argument_list|)
expr_stmt|;
return|return;
block|}
name|doScan
argument_list|(
name|command
operator|.
name|repository
argument_list|,
name|command
operator|.
name|consumers
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|command
operator|.
name|listConsumers
condition|)
block|{
name|dumpAvailableConsumers
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Args
operator|.
name|usage
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|doScan
parameter_list|(
name|String
name|path
parameter_list|,
name|String
index|[]
name|consumers
parameter_list|)
throws|throws
name|ConsumerException
throws|,
name|MalformedURLException
block|{
name|ManagedRepository
name|repo
init|=
operator|new
name|ManagedRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
literal|"Archiva CLI Provided Repo"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownConsumerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|knownConsumerList
operator|.
name|addAll
argument_list|(
name|getConsumerList
argument_list|(
name|consumers
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidConsumerList
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ignoredContent
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ignoredContent
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|RepositoryScanner
operator|.
name|IGNORABLE_CONTENT
argument_list|)
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|RepositoryScanner
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|RepositoryScanStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repo
argument_list|,
name|knownConsumerList
argument_list|,
name|invalidConsumerList
argument_list|,
name|ignoredContent
argument_list|,
name|RepositoryScanner
operator|.
name|FRESH_SCAN
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
name|stats
operator|.
name|toDump
argument_list|(
name|repo
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryScannerException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|getConsumerList
parameter_list|(
name|String
index|[]
name|consumers
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|consumerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|availableConsumers
init|=
name|getConsumers
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|specifiedConsumer
range|:
name|consumers
control|)
block|{
if|if
condition|(
operator|!
name|availableConsumers
operator|.
name|containsKey
argument_list|(
name|specifiedConsumer
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
literal|"Specified consumer [{}] not found."
argument_list|,
name|specifiedConsumer
argument_list|)
expr_stmt|;
name|dumpAvailableConsumers
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|consumerList
operator|.
name|add
argument_list|(
name|availableConsumers
operator|.
name|get
argument_list|(
name|specifiedConsumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|consumerList
return|;
block|}
specifier|private
name|void
name|dumpAvailableConsumers
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|availableConsumers
init|=
name|getConsumers
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|".\\ Available Consumer List \\.______________________________"
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|entry
range|:
name|availableConsumers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|consumerHint
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|RepositoryContentConsumer
name|consumer
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"  {} : {} ({})"
argument_list|,
comment|//
name|consumerHint
argument_list|,
name|consumer
operator|.
name|getDescription
argument_list|()
argument_list|,
name|consumer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|getConsumers
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|beans
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we use a naming conventions knownRepositoryContentConsumer#hint
comment|// with plexus we used only hint so remove before#
name|Map
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|smallNames
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|beans
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|KnownRepositoryContentConsumer
argument_list|>
name|entry
range|:
name|beans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|smallNames
operator|.
name|put
argument_list|(
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|smallNames
return|;
block|}
specifier|private
name|void
name|doConversion
parameter_list|(
name|String
name|properties
parameter_list|)
throws|throws
name|IOException
throws|,
name|RepositoryConversionException
block|{
name|LegacyRepositoryConverter
name|legacyRepositoryConverter
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|LegacyRepositoryConverter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|fis
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|properties
argument_list|)
argument_list|)
init|)
block|{
name|p
operator|.
name|load
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
name|Path
name|oldRepositoryPath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|SOURCE_REPO_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|Path
name|newRepositoryPath
init|=
name|Paths
operator|.
name|get
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|TARGET_REPO_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Converting {} to {}"
argument_list|,
name|oldRepositoryPath
argument_list|,
name|newRepositoryPath
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileExclusionPatterns
init|=
literal|null
decl_stmt|;
name|String
name|s
init|=
name|p
operator|.
name|getProperty
argument_list|(
name|BLACKLISTED_PATTERNS
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|fileExclusionPatterns
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|StringUtils
operator|.
name|split
argument_list|(
name|s
argument_list|,
literal|","
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|legacyRepositoryConverter
operator|.
name|convertLegacyRepository
argument_list|(
name|oldRepositoryPath
argument_list|,
name|newRepositoryPath
argument_list|,
name|fileExclusionPatterns
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|Commands
block|{
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Display help information"
argument_list|,
name|value
operator|=
literal|"help"
argument_list|,
name|alias
operator|=
literal|"h"
argument_list|)
specifier|private
name|boolean
name|help
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Display version information"
argument_list|,
name|value
operator|=
literal|"version"
argument_list|,
name|alias
operator|=
literal|"v"
argument_list|)
specifier|private
name|boolean
name|version
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"List available consumers"
argument_list|,
name|value
operator|=
literal|"listconsumers"
argument_list|,
name|alias
operator|=
literal|"l"
argument_list|)
specifier|private
name|boolean
name|listConsumers
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"The consumers to use (comma delimited)"
argument_list|,
name|value
operator|=
literal|"consumers"
argument_list|,
name|alias
operator|=
literal|"u"
argument_list|)
specifier|private
name|String
name|consumers
init|=
literal|"count-artifacts"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Scan the specified repository"
argument_list|,
name|value
operator|=
literal|"scan"
argument_list|,
name|alias
operator|=
literal|"s"
argument_list|)
specifier|private
name|boolean
name|scan
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"Convert a legacy Maven 1.x repository to a Maven 2.x repository using a properties file to describe the conversion"
argument_list|,
name|value
operator|=
literal|"convert"
argument_list|,
name|alias
operator|=
literal|"c"
argument_list|)
specifier|private
name|boolean
name|convert
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"The properties file for the conversion"
argument_list|,
name|value
operator|=
literal|"properties"
argument_list|)
specifier|private
name|String
name|properties
init|=
literal|"conversion.properties"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|description
operator|=
literal|"The repository to scan"
argument_list|,
name|value
operator|=
literal|"repository"
argument_list|)
specifier|private
name|String
name|repository
decl_stmt|;
block|}
block|}
end_class

end_unit

