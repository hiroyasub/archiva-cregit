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
name|ArtifactDAO
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
name|ArchivaArtifactModel
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
name|ArchivaArtifactModelKey
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
name|ArrayList
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

begin_comment
comment|/**  * JdoArtifactDAO   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role-hint="jdo"  */
end_comment

begin_class
specifier|public
class|class
name|JdoArtifactDAO
extends|extends
name|AbstractLogEnabled
implements|implements
name|ArtifactDAO
block|{
comment|/**      * @plexus.requirement role-hint="archiva"      */
specifier|private
name|JdoAccess
name|jdo
decl_stmt|;
comment|/* .\ Archiva Artifact \. _____________________________________________________________ */
specifier|public
name|ArchivaArtifact
name|createArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|ArchivaArtifact
name|artifact
decl_stmt|;
try|try
block|{
name|artifact
operator|=
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|artifact
return|;
block|}
specifier|public
name|ArchivaArtifact
name|getArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|ArchivaArtifactModelKey
name|key
init|=
operator|new
name|ArchivaArtifactModelKey
argument_list|()
decl_stmt|;
name|key
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|key
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|key
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|key
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|key
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|ArchivaArtifactModel
name|model
init|=
operator|(
name|ArchivaArtifactModel
operator|)
name|jdo
operator|.
name|getObjectById
argument_list|(
name|ArchivaArtifactModel
operator|.
name|class
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
operator|new
name|ArchivaArtifact
argument_list|(
name|model
argument_list|)
return|;
block|}
specifier|public
name|List
name|queryArtifacts
parameter_list|(
name|Constraint
name|constraint
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|List
name|results
init|=
name|jdo
operator|.
name|getAllObjects
argument_list|(
name|ArchivaArtifactModel
operator|.
name|class
argument_list|,
name|constraint
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|results
operator|==
literal|null
operator|)
operator|||
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|results
return|;
block|}
name|List
name|ret
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|results
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ArchivaArtifactModel
name|model
init|=
operator|(
name|ArchivaArtifactModel
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
operator|new
name|ArchivaArtifact
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|ArchivaArtifact
name|saveArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|ArchivaArtifactModel
name|model
init|=
operator|(
name|ArchivaArtifactModel
operator|)
name|jdo
operator|.
name|saveObject
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|ArchivaArtifact
argument_list|(
name|model
argument_list|)
return|;
block|}
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|jdo
operator|.
name|removeObject
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

