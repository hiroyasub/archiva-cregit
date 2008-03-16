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
name|indexer
operator|.
name|filecontent
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
name|indexer
operator|.
name|lucene
operator|.
name|LuceneRepositoryContentRecord
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

begin_comment
comment|/**  * Lucene record for {@link File} contents.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FileContentRecord
implements|implements
name|LuceneRepositoryContentRecord
block|{
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|String
name|filename
decl_stmt|;
comment|/**      * Optional artifact reference for the file content.      */
specifier|private
name|ArchivaArtifact
name|artifact
decl_stmt|;
specifier|private
name|String
name|contents
decl_stmt|;
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|String
name|getContents
parameter_list|()
block|{
return|return
name|contents
return|;
block|}
specifier|public
name|void
name|setContents
parameter_list|(
name|String
name|contents
parameter_list|)
block|{
name|this
operator|.
name|contents
operator|=
name|contents
expr_stmt|;
block|}
specifier|public
name|String
name|getPrimaryKey
parameter_list|()
block|{
return|return
name|filename
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|PRIME
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|filename
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|filename
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|FileContentRecord
name|other
init|=
operator|(
name|FileContentRecord
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|filename
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|filename
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|filename
operator|.
name|equals
argument_list|(
name|other
operator|.
name|filename
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
name|filename
return|;
block|}
specifier|public
name|void
name|setFilename
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
block|}
specifier|public
name|ArchivaArtifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|setArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
block|}
end_class

end_unit

