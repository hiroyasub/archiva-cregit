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
name|jcr
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneId
import|;
end_import

begin_comment
comment|/**  * Node types and properties defined in the schema.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|JcrConstants
block|{
name|String
name|BASE_NODE_TYPE
init|=
literal|"archiva:base"
decl_stmt|;
name|String
name|CONTENT_NODE_TYPE
init|=
literal|"archiva:content"
decl_stmt|;
name|String
name|NAMESPACE_MIXIN_TYPE
init|=
literal|"archiva:namespace"
decl_stmt|;
name|String
name|PROJECT_MIXIN_TYPE
init|=
literal|"archiva:project"
decl_stmt|;
name|String
name|PROJECT_VERSION_NODE_TYPE
init|=
literal|"archiva:projectVersion"
decl_stmt|;
name|String
name|ARTIFACT_NODE_TYPE
init|=
literal|"archiva:artifact"
decl_stmt|;
name|String
name|REPOSITORY_NODE_TYPE
init|=
literal|"archiva:repository"
decl_stmt|;
name|String
name|FACET_NODE_TYPE
init|=
literal|"archiva:facet"
decl_stmt|;
name|String
name|MIXIN_META_SCM
init|=
literal|"archiva:meta_scm"
decl_stmt|;
name|String
name|MIXIN_META_CI
init|=
literal|"archiva:meta_ci"
decl_stmt|;
name|String
name|MIXIN_META_ISSUE
init|=
literal|"archiva:meta_issue"
decl_stmt|;
name|String
name|MIXIN_META_ORGANIZATION
init|=
literal|"archiva:meta_organization"
decl_stmt|;
name|String
name|MAILINGLIST_NODE_TYPE
init|=
literal|"archiva:mailinglist"
decl_stmt|;
name|String
name|MAILINGLISTS_FOLDER_TYPE
init|=
literal|"archiva:mailinglists"
decl_stmt|;
name|String
name|LICENSES_FOLDER_TYPE
init|=
literal|"archiva:licenses"
decl_stmt|;
name|String
name|LICENSE_NODE_TYPE
init|=
literal|"archiva:license"
decl_stmt|;
name|String
name|DEPENDENCY_NODE_TYPE
init|=
literal|"archiva:dependency"
decl_stmt|;
name|String
name|DEPENDENCIES_FOLDER_TYPE
init|=
literal|"archiva:dependencies"
decl_stmt|;
name|String
name|CHECKSUM_NODE_TYPE
init|=
literal|"archiva:checksum"
decl_stmt|;
name|String
name|CHECKSUMS_FOLDER_TYPE
init|=
literal|"archiva:checksums"
decl_stmt|;
name|String
name|FACETS_FOLDER_TYPE
init|=
literal|"archiva:facets"
decl_stmt|;
name|String
name|FACET_ID_CONTAINER_TYPE
init|=
literal|"archiva:facetIdContainer"
decl_stmt|;
name|String
name|FOLDER_TYPE
init|=
literal|"archiva:folder"
decl_stmt|;
comment|// Must be alphabetically ordered!
name|String
index|[]
name|PROJECT_VERSION_VERSION_PROPERTIES
init|=
block|{
literal|"ci.system"
block|,
literal|"ci.url"
block|,
literal|"description"
block|,
literal|"incomplete"
block|,
literal|"issue.system"
block|,
literal|"issue.url"
block|,
literal|"name"
block|,
literal|"org.name"
block|,
literal|"org.url"
block|,
literal|"url"
block|,
literal|"scm.connection"
block|,
literal|"scm.developerConnection"
block|,
literal|"scm.url"
block|}
decl_stmt|;
block|}
end_interface

end_unit
