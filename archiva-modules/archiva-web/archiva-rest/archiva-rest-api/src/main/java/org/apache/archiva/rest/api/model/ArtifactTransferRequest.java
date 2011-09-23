begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"artifactTransferRequest"
argument_list|)
specifier|public
class|class
name|ArtifactTransferRequest
extends|extends
name|Artifact
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|targetRepositoryId
decl_stmt|;
specifier|public
name|String
name|getTargetRepositoryId
parameter_list|()
block|{
return|return
name|targetRepositoryId
return|;
block|}
specifier|public
name|void
name|setTargetRepositoryId
parameter_list|(
name|String
name|targetRepositoryId
parameter_list|)
block|{
name|this
operator|.
name|targetRepositoryId
operator|=
name|targetRepositoryId
expr_stmt|;
block|}
block|}
end_class

end_unit

