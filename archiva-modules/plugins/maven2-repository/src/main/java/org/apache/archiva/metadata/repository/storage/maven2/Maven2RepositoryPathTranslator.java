begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|RepositoryPathTranslator
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * @plexus.component role="org.apache.archiva.metadata.repository.storage.RepositoryPathTranslator" role-hint="maven2"  */
end_comment

begin_class
specifier|public
class|class
name|Maven2RepositoryPathTranslator
implements|implements
name|RepositoryPathTranslator
block|{
specifier|private
specifier|static
specifier|final
name|char
name|PATH_SEPARATOR
init|=
literal|'/'
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|GROUP_SEPARATOR
init|=
literal|'.'
decl_stmt|;
specifier|public
name|File
name|toFile
parameter_list|(
name|File
name|basedir
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|basedir
argument_list|,
name|toPath
argument_list|(
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
name|filename
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|toPath
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|String
name|filename
parameter_list|)
block|{
name|StringBuilder
name|path
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|formatAsDirectory
argument_list|(
name|namespace
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|projectId
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|projectVersion
argument_list|)
operator|.
name|append
argument_list|(
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
name|path
operator|.
name|append
argument_list|(
name|filename
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|formatAsDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
return|return
name|directory
operator|.
name|replace
argument_list|(
name|GROUP_SEPARATOR
argument_list|,
name|PATH_SEPARATOR
argument_list|)
return|;
block|}
block|}
end_class

end_unit

