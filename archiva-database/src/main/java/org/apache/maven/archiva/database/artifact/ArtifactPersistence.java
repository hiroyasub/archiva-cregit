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
name|artifact
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|ibatis
operator|.
name|sqlmap
operator|.
name|client
operator|.
name|SqlMapClient
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
name|AbstractIbatisStore
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
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_comment
comment|/**  * ArtifactPersistence  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.database.artifact.ArtifactPersistence"  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactPersistence
extends|extends
name|AbstractIbatisStore
block|{
specifier|protected
name|String
index|[]
name|getTableNames
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"ArtifactKeys"
block|}
return|;
block|}
specifier|private
name|ArtifactKey
name|toKey
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ArtifactKey
name|key
init|=
operator|new
name|ArtifactKey
argument_list|()
decl_stmt|;
name|key
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|setClassifier
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|key
operator|.
name|setType
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|key
return|;
block|}
specifier|public
name|void
name|create
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|SqlMapClient
name|sqlMap
init|=
name|ibatisHelper
operator|.
name|getSqlMapClient
argument_list|()
decl_stmt|;
try|try
block|{
name|sqlMap
operator|.
name|startTransaction
argument_list|()
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Adding artifact."
argument_list|)
expr_stmt|;
name|sqlMap
operator|.
name|update
argument_list|(
literal|"addArtifact"
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|sqlMap
operator|.
name|commitTransaction
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Error while executing statement, showing all linked exceptions in SQLException."
argument_list|)
expr_stmt|;
while|while
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|getLogger
argument_list|()
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
name|e
operator|=
name|e
operator|.
name|getNextException
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|ArchivaDatabaseException
argument_list|(
literal|"Error while executing statement."
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|sqlMap
operator|.
name|endTransaction
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Artifact
name|read
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
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|read
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
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Artifact
name|read
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
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|update
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
block|}
specifier|public
name|void
name|delete
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
block|}
specifier|public
name|void
name|delete
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
block|}
specifier|public
name|void
name|delete
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
name|type
parameter_list|)
block|{
block|}
specifier|public
name|void
name|delete
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
block|}
block|}
end_class

end_unit

