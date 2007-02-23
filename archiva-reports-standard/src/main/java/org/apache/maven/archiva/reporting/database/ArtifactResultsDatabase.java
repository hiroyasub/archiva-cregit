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
name|reporting
operator|.
name|database
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
name|reporting
operator|.
name|model
operator|.
name|ArtifactResults
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
name|reporting
operator|.
name|model
operator|.
name|ArtifactResultsKey
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
name|reporting
operator|.
name|model
operator|.
name|Result
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
name|artifact
operator|.
name|Artifact
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
name|javax
operator|.
name|jdo
operator|.
name|JDOObjectNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|Transaction
import|;
end_import

begin_comment
comment|/**  * ArtifactResultsDatabase - Database of ArtifactResults.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.reporting.database.ArtifactResultsDatabase"  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactResultsDatabase
extends|extends
name|AbstractResultsDatabase
block|{
comment|// -------------------------------------------------------------------
comment|// ArtifactResults methods.
comment|// -------------------------------------------------------------------
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|ArtifactResultsDatabase
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
name|void
name|addFailure
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|processor
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|ArtifactResults
name|results
init|=
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|createResult
argument_list|(
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|contains
argument_list|(
name|result
argument_list|)
condition|)
block|{
name|results
operator|.
name|addFailure
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
name|saveObject
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addNotice
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|processor
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|ArtifactResults
name|results
init|=
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|createResult
argument_list|(
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|results
operator|.
name|getNotices
argument_list|()
operator|.
name|contains
argument_list|(
name|result
argument_list|)
condition|)
block|{
name|results
operator|.
name|addNotice
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
name|saveObject
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addWarning
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|processor
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|ArtifactResults
name|results
init|=
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|Result
name|result
init|=
name|createResult
argument_list|(
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|contains
argument_list|(
name|result
argument_list|)
condition|)
block|{
name|results
operator|.
name|addWarning
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
name|saveObject
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearResults
parameter_list|(
name|ArtifactResults
name|results
parameter_list|)
block|{
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|results
operator|.
name|getNotices
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|saveObject
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getAllArtifactResults
parameter_list|()
block|{
return|return
name|getAllObjects
argument_list|(
name|ArtifactResults
operator|.
name|class
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Iterator
name|getIterator
parameter_list|()
block|{
name|List
name|allartifacts
init|=
name|getAllArtifactResults
argument_list|()
decl_stmt|;
if|if
condition|(
name|allartifacts
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
operator|.
name|iterator
argument_list|()
return|;
block|}
return|return
name|allartifacts
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|List
name|findArtifactResults
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
block|{
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
name|Query
name|query
init|=
name|pm
operator|.
name|newQuery
argument_list|(
literal|"javax.jdo.query.JDOQL"
argument_list|,
literal|"SELECT FROM "
operator|+
name|ArtifactResults
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" WHERE groupId == findGroupId&& "
operator|+
literal|" artifactId == findArtifactId&& "
operator|+
literal|" version == findVersionId"
argument_list|)
decl_stmt|;
name|query
operator|.
name|declareParameters
argument_list|(
literal|"String findGroupId, String findArtifactId, String findVersionId"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setOrdering
argument_list|(
literal|"findArtifactId ascending"
argument_list|)
expr_stmt|;
name|List
name|result
init|=
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|result
operator|=
operator|(
name|List
operator|)
name|pm
operator|.
name|detachCopyAll
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|ArtifactResults
name|results
parameter_list|)
block|{
name|removeObject
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
try|try
block|{
name|ArtifactResults
name|results
init|=
name|lookupArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|remove
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JDOObjectNotFoundException
name|e
parameter_list|)
block|{
comment|// nothing to do.
block|}
block|}
comment|/**      * Get an {@link ArtifactResults} from the store.      * If the store does not have one, create it.      *       * Equivalent to calling {@link #lookupArtifactResults(Artifact)} then if      * not found, using {@link #createArtifactResults(Artifact)}.      *       * @param artifact the artifact information      * @return the ArtifactResults object (may not be in database yet, so don't forget to {@link #saveObject(Object)})      * @see #lookupArtifactResults(Artifact)      * @see #createArtifactResults(Artifact)      */
specifier|public
name|ArtifactResults
name|getArtifactResults
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ArtifactResults
name|results
decl_stmt|;
try|try
block|{
name|results
operator|=
name|lookupArtifactResults
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JDOObjectNotFoundException
name|e
parameter_list|)
block|{
name|results
operator|=
name|createArtifactResults
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * Create a new {@link ArtifactResults} object from the provided Artifact information.      *       * @param artifact the artifact information.      * @return the new {@link ArtifactResults} object.      * @see #getArtifactResults(Artifact)      * @see #lookupArtifactResults(Artifact)      */
specifier|private
name|ArtifactResults
name|createArtifactResults
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
comment|/* The funky StringUtils.defaultString() is used because of database constraints.          * The ArtifactResults object has a complex primary key consisting of groupId, artifactId, version,          * type, classifier.          * This also means that none of those fields may be null.  however, that doesn't eliminate the          * ability to have an empty string in place of a null.          */
name|ArtifactResults
name|results
init|=
operator|new
name|ArtifactResults
argument_list|()
decl_stmt|;
name|results
operator|.
name|setGroupId
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|setArtifactId
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|setVersion
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|setType
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|setClassifier
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
comment|/**      * Lookup the {@link ArtifactResults} in the JDO store from the information in      * the provided Artifact.      *       * @param artifact the artifact information.      * @return the previously saved {@link ArtifactResults} from the JDO store.      * @throws JDOObjectNotFoundException if the {@link ArtifactResults} are not found.      * @see #getArtifactResults(Artifact)      * @see #createArtifactResults(Artifact)      */
specifier|private
name|ArtifactResults
name|lookupArtifactResults
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|JDOObjectNotFoundException
block|{
comment|/* The funky StringUtils.defaultString() is used because of database constraints.          * The ArtifactResults object has a complex primary key consisting of groupId, artifactId, version,          * type, classifier.          * This also means that none of those fields may be null.  however, that doesn't eliminate the          * ability to have an empty string in place of a null.          */
name|ArtifactResultsKey
name|key
init|=
operator|new
name|ArtifactResultsKey
argument_list|()
decl_stmt|;
name|key
operator|.
name|groupId
operator|=
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|artifactId
operator|=
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|version
operator|=
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|type
operator|=
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|classifier
operator|=
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|ArtifactResults
operator|)
name|getObjectByKey
argument_list|(
name|ArtifactResults
operator|.
name|class
argument_list|,
name|key
argument_list|)
return|;
block|}
specifier|public
name|int
name|getNumFailures
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|getIterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|count
operator|+=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
specifier|public
name|int
name|getNumNotices
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|getIterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|count
operator|+=
name|results
operator|.
name|getNotices
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
specifier|public
name|int
name|getNumWarnings
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|getIterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|count
operator|+=
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|count
return|;
block|}
block|}
end_class

end_unit

