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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|Option
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
name|Options
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|DateUtil
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
name|maven
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
name|apache
operator|.
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepository
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
name|model
operator|.
name|RepositoryContentStatistics
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
name|repository
operator|.
name|RepositoryException
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|tools
operator|.
name|cli
operator|.
name|AbstractCli
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
name|FileInputStream
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
name|text
operator|.
name|SimpleDateFormat
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * ArchivaCli   *  * @author Jason van Zyl  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaCli
extends|extends
name|AbstractCli
block|{
comment|// ----------------------------------------------------------------------------
comment|// Options
comment|// ----------------------------------------------------------------------------
specifier|public
specifier|static
specifier|final
name|char
name|CONVERT
init|=
literal|'c'
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|char
name|SCAN
init|=
literal|'s'
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|char
name|CONSUMERS
init|=
literal|'u'
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|char
name|LIST_CONSUMERS
init|=
literal|'l'
decl_stmt|;
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
operator|new
name|ArchivaCli
argument_list|()
operator|.
name|execute
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPomPropertiesPath
parameter_list|()
block|{
return|return
literal|"META-INF/maven/org.apache.maven.archiva/archiva-cli/pom.properties"
return|;
block|}
specifier|private
name|Option
name|createOption
parameter_list|(
name|char
name|shortOpt
parameter_list|,
name|String
name|longOpt
parameter_list|,
name|int
name|argCount
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|boolean
name|hasArg
init|=
operator|(
name|argCount
operator|>
literal|0
operator|)
decl_stmt|;
name|Option
name|opt
init|=
operator|new
name|Option
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|shortOpt
argument_list|)
argument_list|,
name|hasArg
argument_list|,
name|description
argument_list|)
decl_stmt|;
name|opt
operator|.
name|setLongOpt
argument_list|(
name|longOpt
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasArg
condition|)
block|{
name|opt
operator|.
name|setArgs
argument_list|(
name|argCount
argument_list|)
expr_stmt|;
block|}
return|return
name|opt
return|;
block|}
specifier|public
name|Options
name|buildCliOptions
parameter_list|(
name|Options
name|options
parameter_list|)
block|{
name|Option
name|convertOption
init|=
name|createOption
argument_list|(
name|CONVERT
argument_list|,
literal|"convert"
argument_list|,
literal|1
argument_list|,
literal|"Convert a legacy Maven 1.x repository to a "
operator|+
literal|"Maven 2.x repository using a properties file to describe the conversion."
argument_list|)
decl_stmt|;
name|convertOption
operator|.
name|setArgName
argument_list|(
literal|"conversion.properties"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|convertOption
argument_list|)
expr_stmt|;
name|Option
name|scanOption
init|=
name|createOption
argument_list|(
name|SCAN
argument_list|,
literal|"scan"
argument_list|,
literal|1
argument_list|,
literal|"Scan the specified repository."
argument_list|)
decl_stmt|;
name|scanOption
operator|.
name|setArgName
argument_list|(
literal|"repository directory"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|scanOption
argument_list|)
expr_stmt|;
name|Option
name|consumerOption
init|=
name|createOption
argument_list|(
name|CONSUMERS
argument_list|,
literal|"consumers"
argument_list|,
literal|1
argument_list|,
literal|"The consumers to use. "
operator|+
literal|"(comma delimited. default: 'count-artifacts')"
argument_list|)
decl_stmt|;
name|consumerOption
operator|.
name|setArgName
argument_list|(
literal|"consumer list"
argument_list|)
expr_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|consumerOption
argument_list|)
expr_stmt|;
name|Option
name|listConsumersOption
init|=
name|createOption
argument_list|(
name|LIST_CONSUMERS
argument_list|,
literal|"listconsumers"
argument_list|,
literal|0
argument_list|,
literal|"List available consumers."
argument_list|)
decl_stmt|;
name|options
operator|.
name|addOption
argument_list|(
name|listConsumersOption
argument_list|)
expr_stmt|;
return|return
name|options
return|;
block|}
specifier|public
name|void
name|invokePlexusComponent
parameter_list|(
name|CommandLine
name|cli
parameter_list|,
name|PlexusContainer
name|plexus
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CONVERT
argument_list|)
condition|)
block|{
name|doConversion
argument_list|(
name|cli
argument_list|,
name|plexus
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|SCAN
argument_list|)
condition|)
block|{
name|doScan
argument_list|(
name|cli
argument_list|,
name|plexus
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|LIST_CONSUMERS
argument_list|)
condition|)
block|{
name|dumpAvailableConsumers
argument_list|(
name|plexus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|displayHelp
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|doScan
parameter_list|(
name|CommandLine
name|cli
parameter_list|,
name|PlexusContainer
name|plexus
parameter_list|)
throws|throws
name|ConsumerException
throws|,
name|ComponentLookupException
block|{
name|String
name|path
init|=
name|cli
operator|.
name|getOptionValue
argument_list|(
name|SCAN
argument_list|)
decl_stmt|;
name|ArchivaRepository
name|repo
init|=
operator|new
name|ArchivaRepository
argument_list|(
literal|"cliRepo"
argument_list|,
literal|"Archiva CLI Provided Repo"
argument_list|,
literal|"file://"
operator|+
name|path
argument_list|)
decl_stmt|;
name|List
name|consumerList
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|consumerList
operator|.
name|addAll
argument_list|(
name|getConsumerList
argument_list|(
name|cli
argument_list|,
name|plexus
argument_list|)
argument_list|)
expr_stmt|;
name|RepositoryScanner
name|scanner
init|=
operator|new
name|RepositoryScanner
argument_list|()
decl_stmt|;
try|try
block|{
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|repo
argument_list|,
name|consumerList
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|SimpleDateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|".\\ Scan of "
operator|+
name|repo
operator|.
name|getId
argument_list|()
operator|+
literal|" \\.__________________________________________"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Repository URL    : "
operator|+
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Repository Name   : "
operator|+
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Repository Layout : "
operator|+
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|getLayoutName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Consumers         : ("
operator|+
name|consumerList
operator|.
name|size
argument_list|()
operator|+
literal|" active)"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|consumerList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                      "
operator|+
name|consumer
operator|.
name|getId
argument_list|()
operator|+
literal|" - "
operator|+
name|consumer
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Duration          : "
operator|+
name|DateUtil
operator|.
name|getDuration
argument_list|(
name|stats
operator|.
name|getDuration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  When Gathered     : "
operator|+
name|df
operator|.
name|format
argument_list|(
name|stats
operator|.
name|getWhenGathered
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Total File Count  : "
operator|+
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|averageMsPerFile
init|=
operator|(
name|stats
operator|.
name|getDuration
argument_list|()
operator|/
name|stats
operator|.
name|getTotalFileCount
argument_list|()
operator|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  Avg Time Per File : "
operator|+
name|DateUtil
operator|.
name|getDuration
argument_list|(
name|averageMsPerFile
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"______________________________________________________________"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Collection
name|getConsumerList
parameter_list|(
name|CommandLine
name|cli
parameter_list|,
name|PlexusContainer
name|plexus
parameter_list|)
throws|throws
name|ComponentLookupException
throws|,
name|ConsumerException
block|{
name|String
name|specifiedConsumers
init|=
literal|"count-artifacts"
decl_stmt|;
if|if
condition|(
name|cli
operator|.
name|hasOption
argument_list|(
name|CONSUMERS
argument_list|)
condition|)
block|{
name|specifiedConsumers
operator|=
name|cli
operator|.
name|getOptionValue
argument_list|(
name|CONSUMERS
argument_list|)
expr_stmt|;
block|}
name|List
name|consumerList
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Map
name|availableConsumers
init|=
name|plexus
operator|.
name|lookupMap
argument_list|(
name|RepositoryContentConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|consumerArray
index|[]
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|specifiedConsumers
argument_list|,
literal|','
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
name|consumerArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|specifiedConsumer
init|=
name|consumerArray
index|[
name|i
index|]
decl_stmt|;
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Specified consumer ["
operator|+
name|specifiedConsumer
operator|+
literal|"] not found."
argument_list|)
expr_stmt|;
name|dumpAvailableConsumers
argument_list|(
name|plexus
argument_list|)
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
parameter_list|(
name|PlexusContainer
name|plexus
parameter_list|)
throws|throws
name|ComponentLookupException
block|{
name|Map
name|availableConsumers
init|=
name|plexus
operator|.
name|lookupMap
argument_list|(
name|RepositoryContentConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|".\\ Available Consumer List \\.______________________________"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|availableConsumers
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|consumerHint
init|=
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|consumerHint
operator|+
literal|": "
operator|+
name|consumer
operator|.
name|getDescription
argument_list|()
operator|+
literal|" ("
operator|+
name|consumer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|doConversion
parameter_list|(
name|CommandLine
name|cli
parameter_list|,
name|PlexusContainer
name|plexus
parameter_list|)
throws|throws
name|ComponentLookupException
block|{
name|LegacyRepositoryConverter
name|legacyRepositoryConverter
init|=
operator|(
name|LegacyRepositoryConverter
operator|)
name|plexus
operator|.
name|lookup
argument_list|(
name|LegacyRepositoryConverter
operator|.
name|ROLE
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
block|{
name|p
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|cli
operator|.
name|getOptionValue
argument_list|(
name|CONVERT
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|showFatalError
argument_list|(
literal|"Cannot find properties file which describes the conversion."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|File
name|oldRepositoryPath
init|=
operator|new
name|File
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|SOURCE_REPO_PATH
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|newRepositoryPath
init|=
operator|new
name|File
argument_list|(
name|p
operator|.
name|getProperty
argument_list|(
name|TARGET_REPO_PATH
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
name|List
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
try|try
block|{
name|legacyRepositoryConverter
operator|.
name|convertLegacyRepository
argument_list|(
name|oldRepositoryPath
argument_list|,
name|newRepositoryPath
argument_list|,
name|fileExclusionPatterns
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
literal|"Error converting repository."
argument_list|,
name|e
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

