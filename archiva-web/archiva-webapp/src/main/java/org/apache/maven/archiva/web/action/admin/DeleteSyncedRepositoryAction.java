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
name|web
operator|.
name|action
operator|.
name|admin
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|//import org.apache.maven.archiva.configuration.AbstractRepositoryConfiguration;
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
name|configuration
operator|.
name|Configuration
import|;
end_import

begin_comment
comment|//import org.apache.maven.archiva.configuration.SyncedRepositoryConfiguration;
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Configures the application repositories.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="deleteSyncedRepositoryAction"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteSyncedRepositoryAction
extends|extends
name|AbstractDeleteRepositoryAction
block|{
comment|//    protected AbstractRepositoryConfiguration getRepository( Configuration configuration )
comment|//    {
comment|//        return configuration.getSyncedRepositoryById( repoId );
comment|//    }
comment|//
comment|//    protected void removeRepository( Configuration configuration, AbstractRepositoryConfiguration existingRepository )
comment|//    {
comment|//        configuration.removeSyncedRepository( (SyncedRepositoryConfiguration) existingRepository );
comment|//    }
comment|//
comment|//    protected void removeContents( AbstractRepositoryConfiguration existingRepository )
comment|//        throws IOException
comment|//    {
comment|//        // TODO! remove the contents
comment|//    }
block|}
end_class

end_unit

