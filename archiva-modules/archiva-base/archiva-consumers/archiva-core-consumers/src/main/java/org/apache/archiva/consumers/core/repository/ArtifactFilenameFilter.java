begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilenameFilter
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
comment|/**  * Filename filter for getting all the files related to a specific artifact.  *  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactFilenameFilter
implements|implements
name|FilenameFilter
block|{
specifier|private
name|String
name|filename
decl_stmt|;
specifier|public
name|ArtifactFilenameFilter
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ArtifactFilenameFilter
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
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|startsWith
argument_list|(
name|filename
argument_list|)
operator|)
return|;
block|}
block|}
end_class

end_unit

