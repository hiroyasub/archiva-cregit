begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|remote
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|RemoteRepository
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
name|admin
operator|.
name|repository
operator|.
name|AbstractRepositoryAdminTest
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
name|model
operator|.
name|facets
operator|.
name|AuditEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|RemoteRepositoryAdminTest
extends|extends
name|AbstractRepositoryAdminTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|getAll
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepositories
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|remoteRepositories
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|remoteRepositories
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"remote {}"
argument_list|,
name|remoteRepositories
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getById
parameter_list|()
throws|throws
name|Exception
block|{
name|RemoteRepository
name|central
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
literal|"central"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|central
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://repo.maven.apache.org/maven2"
argument_list|,
name|central
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|60
argument_list|,
name|central
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|central
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|central
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addAndDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|int
name|initialSize
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|RemoteRepository
name|remoteRepository
init|=
name|getRemoteRepository
argument_list|()
decl_stmt|;
name|remoteRepositoryAdmin
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|1
argument_list|,
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repo
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|,
name|repo
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getUserName
argument_list|()
argument_list|,
name|repo
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getTimeout
argument_list|()
argument_list|,
name|repo
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getDescription
argument_list|()
argument_list|,
name|repo
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|remoteRepository
operator|.
name|getExtraHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wine"
argument_list|,
name|remoteRepository
operator|.
name|getExtraHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"beer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|remoteRepository
operator|.
name|getExtraParameters
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|remoteRepository
operator|.
name|getExtraParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|deleteRemoteRepository
argument_list|(
literal|"foo"
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
argument_list|,
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|ADD_REMOTE_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-localhost"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRemoteIP
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|DELETE_REMOTE_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addAndUpdateAndDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|mockAuditListener
operator|.
name|clearEvents
argument_list|()
expr_stmt|;
name|int
name|initialSize
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|RemoteRepository
name|remoteRepository
init|=
name|getRemoteRepository
argument_list|()
decl_stmt|;
name|remoteRepositoryAdmin
operator|.
name|addRemoteRepository
argument_list|(
name|remoteRepository
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|+
literal|1
argument_list|,
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repo
init|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|,
name|repo
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getUrl
argument_list|()
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getUserName
argument_list|()
argument_list|,
name|repo
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getTimeout
argument_list|()
argument_list|,
name|repo
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|getRemoteRepository
argument_list|()
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|,
name|repo
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setUserName
argument_list|(
literal|"foo-name-changed"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setPassword
argument_list|(
literal|"titi"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
literal|"http://foo.com/maven-really-rocks"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRemoteDownloadNetworkProxyId
argument_list|(
literal|"toto"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setDescription
argument_list|(
literal|"archiva rocks!"
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|updateRemoteRepository
argument_list|(
name|repo
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo-name-changed"
argument_list|,
name|repo
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"titi"
argument_list|,
name|repo
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo.com/maven-really-rocks"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toto"
argument_list|,
name|repo
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva rocks!"
argument_list|,
name|repo
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepositoryAdmin
operator|.
name|deleteRemoteRepository
argument_list|(
literal|"foo"
argument_list|,
name|getFakeAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
argument_list|,
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
name|remoteRepositoryAdmin
operator|.
name|getRemoteRepository
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|ADD_REMOTE_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-localhost"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getRemoteIP
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|MODIFY_REMOTE_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-localhost"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getRemoteIP
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AuditEvent
operator|.
name|DELETE_REMOTE_REPO
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|mockAuditListener
operator|.
name|getAuditEvents
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

