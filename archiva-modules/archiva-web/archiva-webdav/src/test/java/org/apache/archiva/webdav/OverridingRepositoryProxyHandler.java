begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|proxy
operator|.
name|MavenRepositoryProxyHandler
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
name|proxy
operator|.
name|model
operator|.
name|ProxyFetchResult
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
name|repository
operator|.
name|ManagedRepository
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
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
class|class
name|OverridingRepositoryProxyHandler
extends|extends
name|MavenRepositoryProxyHandler
block|{
specifier|private
name|ArchivaDavResourceFactoryTest
name|archivaDavResourceFactoryTest
decl_stmt|;
specifier|public
name|OverridingRepositoryProxyHandler
parameter_list|(
name|ArchivaDavResourceFactoryTest
name|archivaDavResourceFactoryTest
parameter_list|)
block|{
name|this
operator|.
name|archivaDavResourceFactoryTest
operator|=
name|archivaDavResourceFactoryTest
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ProxyFetchResult
name|fetchMetadataFromProxies
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|String
name|logicalPath
parameter_list|)
block|{
name|StorageAsset
name|target
init|=
name|repository
operator|.
name|getAsset
argument_list|(
name|logicalPath
argument_list|)
decl_stmt|;
try|try
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|archivaDavResourceFactoryTest
operator|.
name|getProjectBase
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"target/test-classes/maven-metadata.xml"
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|target
operator|.
name|getFilePath
argument_list|()
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
block|}
return|return
operator|new
name|ProxyFetchResult
argument_list|(
name|target
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

