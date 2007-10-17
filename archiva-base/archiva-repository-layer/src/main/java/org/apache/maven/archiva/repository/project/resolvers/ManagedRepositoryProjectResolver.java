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
name|repository
operator|.
name|project
operator|.
name|resolvers
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
name|ManagedRepositoryContent
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
name|ProjectModelReader
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * Resolve Project from managed repository.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ManagedRepositoryProjectResolver
implements|implements
name|ProjectModelResolver
implements|,
name|FilesystemBasedResolver
block|{
specifier|private
name|ManagedRepositoryContent
name|repository
decl_stmt|;
specifier|private
name|ProjectModelReader
name|reader
decl_stmt|;
specifier|public
name|ManagedRepositoryProjectResolver
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|ProjectModelReader
name|reader
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|reader
operator|=
name|reader
expr_stmt|;
block|}
specifier|public
name|ArchivaProjectModel
name|resolveProjectModel
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ProjectModelException
block|{
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
literal|""
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|File
name|repoFile
init|=
name|repository
operator|.
name|toFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|reader
operator|.
name|read
argument_list|(
name|repoFile
argument_list|)
return|;
block|}
block|}
end_class

end_unit

