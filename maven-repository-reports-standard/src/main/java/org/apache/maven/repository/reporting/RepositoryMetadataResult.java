begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|repository
operator|.
name|metadata
operator|.
name|RepositoryMetadata
import|;
end_import

begin_comment
comment|/**  * A result of the report for a given artifact being processed.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryMetadataResult
block|{
specifier|private
specifier|final
name|RepositoryMetadata
name|metadata
decl_stmt|;
specifier|private
specifier|final
name|String
name|reason
decl_stmt|;
specifier|public
name|RepositoryMetadataResult
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
name|this
operator|.
name|reason
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|RepositoryMetadataResult
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
specifier|public
name|RepositoryMetadata
name|getMetadata
parameter_list|()
block|{
return|return
name|metadata
return|;
block|}
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|reason
return|;
block|}
block|}
end_class

end_unit

