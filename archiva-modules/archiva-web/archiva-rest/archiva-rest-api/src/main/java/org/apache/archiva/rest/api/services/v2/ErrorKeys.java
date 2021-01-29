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
name|services
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
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
name|services
operator|.
name|v2
operator|.
name|ErrorMessage
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
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ErrorKeys
block|{
name|String
name|PREFIX
init|=
literal|"archiva."
decl_stmt|;
name|String
name|REPOSITORY_GROUP_PREFIX
init|=
name|PREFIX
operator|+
literal|"repository_group."
decl_stmt|;
name|String
name|REPOSITORY_PREFIX
init|=
name|PREFIX
operator|+
literal|"repository."
decl_stmt|;
name|String
name|INVALID_RESULT_SET_ERROR
init|=
literal|"archiva.result_set.invalid"
decl_stmt|;
name|String
name|REPOSITORY_ADMIN_ERROR
init|=
literal|"archiva.repositoryadmin.error"
decl_stmt|;
name|String
name|LDAP_CF_INIT_FAILED
init|=
literal|"archiva.ldap.cf.init.failed"
decl_stmt|;
name|String
name|LDAP_USER_MAPPER_INIT_FAILED
init|=
literal|"archiva.ldap.usermapper.init.failed"
decl_stmt|;
name|String
name|LDAP_COMMUNICATION_ERROR
init|=
literal|"archiva.ldap.communication_error"
decl_stmt|;
name|String
name|LDAP_INVALID_NAME
init|=
literal|"archiva.ldap.invalid_name"
decl_stmt|;
name|String
name|LDAP_GENERIC_ERROR
init|=
literal|"archiva.ldap.generic_error"
decl_stmt|;
name|String
name|LDAP_SERVICE_UNAVAILABLE
init|=
literal|"archiva.ldap.service_unavailable"
decl_stmt|;
name|String
name|LDAP_SERVICE_AUTHENTICATION_FAILED
init|=
literal|"archiva.ldap.authentication.failed"
decl_stmt|;
name|String
name|LDAP_SERVICE_AUTHENTICATION_NOT_SUPPORTED
init|=
literal|"archiva.ldap.authentication.not_supported"
decl_stmt|;
name|String
name|LDAP_SERVICE_NO_PERMISSION
init|=
literal|"archiva.ldap.no_permissions"
decl_stmt|;
name|String
name|PROPERTY_NOT_FOUND
init|=
literal|"archiva.property.not.found"
decl_stmt|;
name|String
name|MISSING_DATA
init|=
literal|"archiva.missing.data"
decl_stmt|;
name|String
name|REPOSITORY_GROUP_NOT_FOUND
init|=
name|REPOSITORY_GROUP_PREFIX
operator|+
literal|"notfound"
decl_stmt|;
name|String
name|REPOSITORY_GROUP_ADD_FAILED
init|=
name|REPOSITORY_GROUP_PREFIX
operator|+
literal|"add.failed"
decl_stmt|;
name|String
name|REPOSITORY_GROUP_EXIST
init|=
name|REPOSITORY_GROUP_PREFIX
operator|+
literal|"exists"
decl_stmt|;
name|String
name|REPOSITORY_GROUP_DELETE_FAILED
init|=
name|REPOSITORY_GROUP_PREFIX
operator|+
literal|"delete.failed"
decl_stmt|;
name|String
name|REPOSITORY_NOT_FOUND
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"notfound"
decl_stmt|;
name|String
name|REPOSITORY_MANAGED_NOT_FOUND
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"managed.notfound"
decl_stmt|;
name|String
name|REPOSITORY_REMOTE_NOT_FOUND
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"remote.notfound"
decl_stmt|;
name|String
name|REPOSITORY_METADATA_ERROR
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"metadata_error"
decl_stmt|;
name|String
name|TASK_QUEUE_FAILED
init|=
name|PREFIX
operator|+
literal|"task.queue_failed"
decl_stmt|;
name|String
name|REPOSITORY_SCAN_FAILED
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"scan.failed"
decl_stmt|;
name|String
name|ARTIFACT_EXISTS_AT_DEST
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"artifact.dest.exists"
decl_stmt|;
name|String
name|REPOSITORY_REMOTE_INDEX_DOWNLOAD_FAILED
init|=
name|REPOSITORY_PREFIX
operator|+
literal|"remote.index.download_failed"
decl_stmt|;
block|}
end_interface

end_unit

