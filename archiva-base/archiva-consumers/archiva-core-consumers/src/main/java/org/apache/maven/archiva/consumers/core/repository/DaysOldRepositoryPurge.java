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
name|consumers
operator|.
name|core
operator|.
name|repository
package|;
end_package

begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
end_comment

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
name|layout
operator|.
name|FilenameParts
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
name|layout
operator|.
name|LayoutException
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
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|VersionUtil
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
name|RepositoryConfiguration
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
name|Configuration
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
name|indexer
operator|.
name|RepositoryIndexException
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
name|database
operator|.
name|ArtifactDAO
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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

begin_comment
comment|/**  * Purge repository for snapshots older than the specified days in the repository configuration.  *  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  */
end_comment

begin_class
specifier|public
class|class
name|DaysOldRepositoryPurge
extends|extends
name|AbstractRepositoryPurge
block|{
specifier|private
name|RepositoryConfiguration
name|repoConfig
decl_stmt|;
specifier|public
name|DaysOldRepositoryPurge
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|,
name|BidirectionalRepositoryLayout
name|layout
parameter_list|,
name|ArtifactDAO
name|artifactDao
parameter_list|,
name|RepositoryConfiguration
name|repoConfig
parameter_list|)
block|{
name|super
argument_list|(
name|repository
argument_list|,
name|layout
argument_list|,
name|artifactDao
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoConfig
operator|=
name|repoConfig
expr_stmt|;
block|}
specifier|public
name|void
name|process
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|RepositoryPurgeException
block|{
try|try
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
name|FilenameParts
name|parts
init|=
name|getFilenameParts
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|parts
operator|.
name|version
argument_list|)
condition|)
block|{
name|Calendar
name|olderThanThisDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|olderThanThisDate
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|,
operator|(
operator|-
literal|1
operator|*
name|repoConfig
operator|.
name|getDaysOlder
argument_list|()
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifactFile
operator|.
name|lastModified
argument_list|()
operator|<
name|olderThanThisDate
operator|.
name|getTimeInMillis
argument_list|()
condition|)
block|{
name|String
index|[]
name|fileParts
init|=
name|artifactFile
operator|.
name|getName
argument_list|()
operator|.
name|split
argument_list|(
literal|"."
operator|+
name|parts
operator|.
name|extension
argument_list|)
decl_stmt|;
name|File
index|[]
name|artifactFiles
init|=
name|getFiles
argument_list|(
name|artifactFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|fileParts
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|purge
argument_list|(
name|artifactFiles
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|le
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryPurgeException
argument_list|(
name|le
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

