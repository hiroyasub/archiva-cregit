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
name|project
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|model
operator|.
name|ArchivaModelCloner
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|VersionedReference
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
name|project
operator|.
name|ProjectModelException
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
name|project
operator|.
name|ProjectModelResolver
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
name|project
operator|.
name|resolvers
operator|.
name|FilesystemBasedResolver
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
name|project
operator|.
name|resolvers
operator|.
name|ProjectModelResolutionListener
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

begin_comment
comment|/**  * Just in Time save of project models to the database, implemented as a listener  * on {@link ProjectModelResolver} objects that implement {@link FilesystemBasedResolver}.  *  * @version $Id$  *   * @plexus.component   *              role="org.apache.maven.archiva.repository.project.resolvers.ProjectModelResolutionListener"  *              role-hint="model-to-db"  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModelToDatabaseListener
implements|implements
name|ProjectModelResolutionListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ProjectModelToDatabaseListener
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
specifier|private
name|void
name|saveInDatabase
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ProjectModelException
block|{
try|try
block|{
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|saveProjectModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectModelException
argument_list|(
literal|"Unable to save model to database: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|removeFromDatabase
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ProjectModelException
block|{
try|try
block|{
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|deleteProjectModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectModelException
argument_list|(
literal|"Unable to remove existing model from database: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|existsInDatabase
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ProjectModelException
block|{
try|try
block|{
name|ArchivaProjectModel
name|dbmodel
init|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|getProjectModel
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|dbmodel
operator|!=
literal|null
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProjectModelException
argument_list|(
literal|"Unable to check for existing model from database: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|resolutionAttempting
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|resolutionError
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|,
name|Exception
name|cause
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|resolutionMiss
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|resolutionNotFound
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|List
argument_list|<
name|ProjectModelResolver
argument_list|>
name|resolverList
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|resolutionStart
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|List
argument_list|<
name|ProjectModelResolver
argument_list|>
name|resolverList
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|resolutionSuccess
parameter_list|(
name|VersionedReference
name|projectRef
parameter_list|,
name|ProjectModelResolver
name|resolver
parameter_list|,
name|ArchivaProjectModel
name|model
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|resolver
operator|instanceof
name|FilesystemBasedResolver
operator|)
condition|)
block|{
comment|// Nothing to do. skip it.
return|return;
block|}
comment|// Clone model, since DAO while detachingCopy resets contents of the model
comment|// this changes behaviour of EffectiveProjectModelFilter
name|model
operator|=
name|ArchivaModelCloner
operator|.
name|clone
argument_list|(
name|model
argument_list|)
expr_stmt|;
try|try
block|{
comment|// Test if it exists.
if|if
condition|(
name|existsInDatabase
argument_list|(
name|model
argument_list|)
condition|)
block|{
name|removeFromDatabase
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
name|saveInDatabase
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProjectModelException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
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
block|}
end_class

end_unit

