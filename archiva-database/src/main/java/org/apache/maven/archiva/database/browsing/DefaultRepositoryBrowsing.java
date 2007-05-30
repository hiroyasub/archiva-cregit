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
name|database
operator|.
name|browsing
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
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDAO
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
name|ArchivaDatabaseException
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
name|ObjectNotFoundException
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
name|constraints
operator|.
name|ProjectsByArtifactUsageConstraint
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
name|constraints
operator|.
name|UniqueArtifactIdConstraint
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
name|constraints
operator|.
name|UniqueGroupIdConstraint
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
name|constraints
operator|.
name|UniqueVersionConstraint
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
name|updater
operator|.
name|DatabaseUpdater
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
name|ArchivaArtifact
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
name|ArchivaProjectModel
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
name|logging
operator|.
name|AbstractLogEnabled
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
name|List
import|;
end_import

begin_comment
comment|/**  * DefaultRepositoryBrowsing   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.database.browsing.RepositoryBrowsing"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryBrowsing
extends|extends
name|AbstractLogEnabled
implements|implements
name|RepositoryBrowsing
block|{
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|DatabaseUpdater
name|dbUpdater
decl_stmt|;
specifier|public
name|BrowsingResults
name|getRoot
parameter_list|()
block|{
name|List
name|groups
init|=
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueGroupIdConstraint
argument_list|()
argument_list|)
decl_stmt|;
name|BrowsingResults
name|results
init|=
operator|new
name|BrowsingResults
argument_list|()
decl_stmt|;
name|results
operator|.
name|setGroupIds
argument_list|(
name|GroupIdFilter
operator|.
name|filterGroups
argument_list|(
name|groups
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
specifier|public
name|BrowsingResults
name|selectArtifactId
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
comment|// List groups = dao.query( new UniqueGroupIdConstraint( groupId ) );
comment|// List artifacts = dao.query( new UniqueArtifactIdConstraint( groupId ) );
name|List
name|versions
init|=
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueVersionConstraint
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
argument_list|)
decl_stmt|;
name|BrowsingResults
name|results
init|=
operator|new
name|BrowsingResults
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
decl_stmt|;
comment|// results.setGroupIds( groups );
comment|// results.setArtifacts( artifacts );
name|results
operator|.
name|setVersions
argument_list|(
name|versions
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
specifier|public
name|BrowsingResults
name|selectGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|List
name|groups
init|=
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueGroupIdConstraint
argument_list|(
name|groupId
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|artifacts
init|=
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueArtifactIdConstraint
argument_list|(
name|groupId
argument_list|)
argument_list|)
decl_stmt|;
name|BrowsingResults
name|results
init|=
operator|new
name|BrowsingResults
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
name|results
operator|.
name|setGroupIds
argument_list|(
name|groups
argument_list|)
expr_stmt|;
name|results
operator|.
name|setArtifacts
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
specifier|public
name|ArchivaProjectModel
name|selectVersion
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|ArchivaArtifact
name|pomArtifact
init|=
literal|null
decl_stmt|;
try|try
block|{
name|pomArtifact
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
if|if
condition|(
name|pomArtifact
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to find artifact ["
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
name|ArchivaProjectModel
name|model
decl_stmt|;
if|if
condition|(
name|pomArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|isProcessed
argument_list|()
condition|)
block|{
comment|// It's been processed. return it.
name|model
operator|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
comment|// Process it.
name|dbUpdater
operator|.
name|updateUnprocessed
argument_list|(
name|pomArtifact
argument_list|)
expr_stmt|;
comment|// Find it.
try|try
block|{
name|model
operator|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to find project model for ["
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
operator|+
literal|"]"
argument_list|)
throw|;
block|}
return|return
name|model
return|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
specifier|public
name|List
name|getUsedBy
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|ProjectsByArtifactUsageConstraint
name|constraint
init|=
operator|new
name|ProjectsByArtifactUsageConstraint
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|queryProjectModels
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
comment|// defensive. to honor contract as specified. never null.
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
return|return
name|results
return|;
block|}
block|}
end_class

end_unit

