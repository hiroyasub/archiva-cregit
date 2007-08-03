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
name|jdo
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
name|Constraint
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
name|ProjectModelDAO
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
name|jpox
operator|.
name|ArchivaProjectModelKey
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
comment|/**  * JdoProjectModelDAO   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role-hint="jdo"  */
end_comment

begin_class
specifier|public
class|class
name|JdoProjectModelDAO
implements|implements
name|ProjectModelDAO
block|{
comment|/**      * @plexus.requirement role-hint="archiva"      */
specifier|private
name|JdoAccess
name|jdo
decl_stmt|;
specifier|public
name|ArchivaProjectModel
name|createProjectModel
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
name|ArchivaProjectModel
name|model
decl_stmt|;
try|try
block|{
name|model
operator|=
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|model
operator|=
operator|new
name|ArchivaProjectModel
argument_list|()
expr_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
return|return
name|model
return|;
block|}
specifier|public
name|ArchivaProjectModel
name|getProjectModel
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
name|ArchivaProjectModelKey
name|key
init|=
operator|new
name|ArchivaProjectModelKey
argument_list|()
decl_stmt|;
name|key
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|key
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
name|key
operator|.
name|version
operator|=
name|version
expr_stmt|;
return|return
operator|(
name|ArchivaProjectModel
operator|)
name|jdo
operator|.
name|getObjectById
argument_list|(
name|ArchivaProjectModel
operator|.
name|class
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|List
name|queryProjectModels
parameter_list|(
name|Constraint
name|constraint
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
return|return
name|jdo
operator|.
name|queryObjects
argument_list|(
name|ArchivaProjectModel
operator|.
name|class
argument_list|,
name|constraint
argument_list|)
return|;
block|}
specifier|public
name|ArchivaProjectModel
name|saveProjectModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
return|return
operator|(
name|ArchivaProjectModel
operator|)
name|jdo
operator|.
name|saveObject
argument_list|(
name|model
argument_list|)
return|;
block|}
specifier|public
name|void
name|deleteProjectModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|jdo
operator|.
name|removeObject
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

