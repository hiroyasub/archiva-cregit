begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
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
name|configuration
operator|.
name|FileTypes
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
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|InvalidRepositoryContentConsumer
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
name|KnownRepositoryContentConsumer
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
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|DirectoryWalker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * DefaultRepositoryScanner  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryScanner#default"
argument_list|)
specifier|public
class|class
name|DefaultRepositoryScanner
implements|implements
name|RepositoryScanner
block|{
comment|/**      * plexus.requirement      */
annotation|@
name|Inject
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
comment|/**      * plexus.requirement      */
annotation|@
name|Inject
specifier|private
name|RepositoryContentConsumers
name|consumerUtil
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|RepositoryScannerInstance
argument_list|>
name|inProgressScans
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|RepositoryScannerInstance
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|RepositoryScanStatistics
name|scan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|,
name|long
name|changesSince
parameter_list|)
throws|throws
name|RepositoryScannerException
block|{
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownContentConsumers
init|=
name|consumerUtil
operator|.
name|getSelectedKnownConsumers
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidContentConsumers
init|=
name|consumerUtil
operator|.
name|getSelectedInvalidConsumers
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ignoredPatterns
init|=
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|IGNORED
argument_list|)
decl_stmt|;
return|return
name|scan
argument_list|(
name|repository
argument_list|,
name|knownContentConsumers
argument_list|,
name|invalidContentConsumers
argument_list|,
name|ignoredPatterns
argument_list|,
name|changesSince
argument_list|)
return|;
block|}
specifier|public
name|RepositoryScanStatistics
name|scan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|,
name|List
argument_list|<
name|KnownRepositoryContentConsumer
argument_list|>
name|knownContentConsumers
parameter_list|,
name|List
argument_list|<
name|InvalidRepositoryContentConsumer
argument_list|>
name|invalidContentConsumers
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|ignoredContentPatterns
parameter_list|,
name|long
name|changesSince
parameter_list|)
throws|throws
name|RepositoryScannerException
block|{
if|if
condition|(
name|repository
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to operate on a null repository."
argument_list|)
throw|;
block|}
name|File
name|repositoryBase
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
comment|//MRM-1342 Repository statistics report doesn't appear to be working correctly
comment|//create the repo if not existing to have an empty stats
if|if
condition|(
operator|!
name|repositoryBase
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|repositoryBase
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Unable to scan a repository, directory "
operator|+
name|repositoryBase
operator|.
name|getPath
argument_list|()
operator|+
literal|" does not exist."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repositoryBase
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Unable to scan a repository, path "
operator|+
name|repositoryBase
operator|.
name|getPath
argument_list|()
operator|+
literal|" is not a directory."
argument_list|)
throw|;
block|}
comment|// Setup Includes / Excludes.
name|List
argument_list|<
name|String
argument_list|>
name|allExcludes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|allIncludes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|ignoredContentPatterns
argument_list|)
condition|)
block|{
name|allExcludes
operator|.
name|addAll
argument_list|(
name|ignoredContentPatterns
argument_list|)
expr_stmt|;
block|}
comment|// Scan All Content. (intentional)
name|allIncludes
operator|.
name|add
argument_list|(
literal|"**/*"
argument_list|)
expr_stmt|;
comment|// Setup Directory Walker
name|DirectoryWalker
name|dirWalker
init|=
operator|new
name|DirectoryWalker
argument_list|()
decl_stmt|;
name|dirWalker
operator|.
name|setBaseDir
argument_list|(
name|repositoryBase
argument_list|)
expr_stmt|;
name|dirWalker
operator|.
name|setIncludes
argument_list|(
name|allIncludes
argument_list|)
expr_stmt|;
name|dirWalker
operator|.
name|setExcludes
argument_list|(
name|allExcludes
argument_list|)
expr_stmt|;
comment|// Setup the Scan Instance
name|RepositoryScannerInstance
name|scannerInstance
init|=
operator|new
name|RepositoryScannerInstance
argument_list|(
name|repository
argument_list|,
name|knownContentConsumers
argument_list|,
name|invalidContentConsumers
argument_list|,
name|changesSince
argument_list|)
decl_stmt|;
name|inProgressScans
operator|.
name|add
argument_list|(
name|scannerInstance
argument_list|)
expr_stmt|;
name|dirWalker
operator|.
name|addDirectoryWalkListener
argument_list|(
name|scannerInstance
argument_list|)
expr_stmt|;
comment|// Execute scan.
name|dirWalker
operator|.
name|scan
argument_list|()
expr_stmt|;
name|RepositoryScanStatistics
name|stats
init|=
name|scannerInstance
operator|.
name|getStatistics
argument_list|()
decl_stmt|;
name|stats
operator|.
name|setKnownConsumers
argument_list|(
name|gatherIds
argument_list|(
name|knownContentConsumers
argument_list|)
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setInvalidConsumers
argument_list|(
name|gatherIds
argument_list|(
name|invalidContentConsumers
argument_list|)
argument_list|)
expr_stmt|;
name|inProgressScans
operator|.
name|remove
argument_list|(
name|scannerInstance
argument_list|)
expr_stmt|;
return|return
name|stats
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|gatherIds
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|RepositoryContentConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryContentConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ids
return|;
block|}
specifier|public
name|Set
argument_list|<
name|RepositoryScannerInstance
argument_list|>
name|getInProgressScans
parameter_list|()
block|{
return|return
name|inProgressScans
return|;
block|}
block|}
end_class

end_unit

