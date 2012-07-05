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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavServletRequest
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
name|configuration
operator|.
name|ArchivaConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * UnauthenticatedDavResourceFactory  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaDavResourceFactory#unauthenticated"
argument_list|)
specifier|public
class|class
name|UnauthenticatedDavResourceFactory
extends|extends
name|ArchivaDavResourceFactory
block|{
annotation|@
name|Inject
specifier|public
name|UnauthenticatedDavResourceFactory
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|,
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
name|super
argument_list|(
name|applicationContext
argument_list|,
name|plexusSisuBridge
argument_list|,
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isAuthorized
parameter_list|(
name|DavServletRequest
name|request
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|DavException
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

