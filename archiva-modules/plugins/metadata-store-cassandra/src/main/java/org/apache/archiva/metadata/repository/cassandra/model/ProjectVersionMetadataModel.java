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
name|cassandra
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|entitystore
operator|.
name|Serializer
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
name|CiManagement
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
name|Dependency
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
name|IssueManagement
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
name|License
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
name|MailingList
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
name|Organization
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
name|Scm
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
name|cassandra
operator|.
name|CassandraUtils
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Column
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
annotation|@
name|Entity
specifier|public
class|class
name|ProjectVersionMetadataModel
block|{
comment|// repositoryId + namespace + projectId + id (version)
annotation|@
name|Id
annotation|@
name|Serializer
argument_list|(
name|HugeStringSerializer
operator|.
name|class
argument_list|)
specifier|private
name|String
name|rowId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"namespace"
argument_list|)
specifier|private
name|Namespace
name|namespace
decl_stmt|;
comment|/**      * id is the version      */
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"id"
argument_list|)
annotation|@
name|Serializer
argument_list|(
name|HugeStringSerializer
operator|.
name|class
argument_list|)
specifier|private
name|String
name|id
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"projectId"
argument_list|)
annotation|@
name|Serializer
argument_list|(
name|HugeStringSerializer
operator|.
name|class
argument_list|)
specifier|private
name|String
name|projectId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"url"
argument_list|)
annotation|@
name|Serializer
argument_list|(
name|HugeStringSerializer
operator|.
name|class
argument_list|)
specifier|private
name|String
name|url
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"name"
argument_list|)
annotation|@
name|Serializer
argument_list|(
name|HugeStringSerializer
operator|.
name|class
argument_list|)
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"description"
argument_list|)
annotation|@
name|Serializer
argument_list|(
name|HugeStringSerializer
operator|.
name|class
argument_list|)
specifier|private
name|String
name|description
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"organization"
argument_list|)
specifier|private
name|Organization
name|organization
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"issueManagement"
argument_list|)
specifier|private
name|IssueManagement
name|issueManagement
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"scm"
argument_list|)
specifier|private
name|Scm
name|scm
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"ciManagement"
argument_list|)
specifier|private
name|CiManagement
name|ciManagement
decl_stmt|;
comment|// FIXME store those values in a separate table
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"licenses"
argument_list|)
specifier|private
name|List
argument_list|<
name|License
argument_list|>
name|licenses
init|=
operator|new
name|ArrayList
argument_list|<
name|License
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"mailingLists"
argument_list|)
specifier|private
name|List
argument_list|<
name|MailingList
argument_list|>
name|mailingLists
init|=
operator|new
name|ArrayList
argument_list|<
name|MailingList
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"dependencies"
argument_list|)
specifier|private
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|<
name|Dependency
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|name
operator|=
literal|"incomplete"
argument_list|)
specifier|private
name|boolean
name|incomplete
decl_stmt|;
specifier|public
name|String
name|getProjectId
parameter_list|()
block|{
return|return
name|projectId
return|;
block|}
specifier|public
name|void
name|setProjectId
parameter_list|(
name|String
name|projectId
parameter_list|)
block|{
name|this
operator|.
name|projectId
operator|=
name|projectId
expr_stmt|;
block|}
specifier|public
name|String
name|getRowId
parameter_list|()
block|{
return|return
name|rowId
return|;
block|}
specifier|public
name|void
name|setRowId
parameter_list|(
name|String
name|rowId
parameter_list|)
block|{
name|this
operator|.
name|rowId
operator|=
name|rowId
expr_stmt|;
block|}
comment|// FIXME must be renamed getVersion !!!
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|Organization
name|getOrganization
parameter_list|()
block|{
return|return
name|organization
return|;
block|}
specifier|public
name|void
name|setOrganization
parameter_list|(
name|Organization
name|organization
parameter_list|)
block|{
name|this
operator|.
name|organization
operator|=
name|organization
expr_stmt|;
block|}
specifier|public
name|IssueManagement
name|getIssueManagement
parameter_list|()
block|{
return|return
name|issueManagement
return|;
block|}
specifier|public
name|void
name|setIssueManagement
parameter_list|(
name|IssueManagement
name|issueManagement
parameter_list|)
block|{
name|this
operator|.
name|issueManagement
operator|=
name|issueManagement
expr_stmt|;
block|}
specifier|public
name|Scm
name|getScm
parameter_list|()
block|{
return|return
name|scm
return|;
block|}
specifier|public
name|void
name|setScm
parameter_list|(
name|Scm
name|scm
parameter_list|)
block|{
name|this
operator|.
name|scm
operator|=
name|scm
expr_stmt|;
block|}
specifier|public
name|CiManagement
name|getCiManagement
parameter_list|()
block|{
return|return
name|ciManagement
return|;
block|}
specifier|public
name|void
name|setCiManagement
parameter_list|(
name|CiManagement
name|ciManagement
parameter_list|)
block|{
name|this
operator|.
name|ciManagement
operator|=
name|ciManagement
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIncomplete
parameter_list|()
block|{
return|return
name|incomplete
return|;
block|}
specifier|public
name|void
name|setIncomplete
parameter_list|(
name|boolean
name|incomplete
parameter_list|)
block|{
name|this
operator|.
name|incomplete
operator|=
name|incomplete
expr_stmt|;
block|}
specifier|public
name|Namespace
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|void
name|setNamespace
parameter_list|(
name|Namespace
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|License
argument_list|>
name|getLicenses
parameter_list|()
block|{
return|return
name|licenses
return|;
block|}
specifier|public
name|void
name|setLicenses
parameter_list|(
name|List
argument_list|<
name|License
argument_list|>
name|licenses
parameter_list|)
block|{
name|this
operator|.
name|licenses
operator|=
name|licenses
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MailingList
argument_list|>
name|getMailingLists
parameter_list|()
block|{
return|return
name|mailingLists
return|;
block|}
specifier|public
name|void
name|setMailingLists
parameter_list|(
name|List
argument_list|<
name|MailingList
argument_list|>
name|mailingLists
parameter_list|)
block|{
name|this
operator|.
name|mailingLists
operator|=
name|mailingLists
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Dependency
argument_list|>
name|getDependencies
parameter_list|()
block|{
return|return
name|dependencies
return|;
block|}
specifier|public
name|void
name|setDependencies
parameter_list|(
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
parameter_list|)
block|{
name|this
operator|.
name|dependencies
operator|=
name|dependencies
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"ProjectVersionMetadataModel{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"rowId='"
argument_list|)
operator|.
name|append
argument_list|(
name|rowId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", namespace="
argument_list|)
operator|.
name|append
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", id='"
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", projectId='"
argument_list|)
operator|.
name|append
argument_list|(
name|projectId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", url='"
argument_list|)
operator|.
name|append
argument_list|(
name|url
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", name='"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", description='"
argument_list|)
operator|.
name|append
argument_list|(
name|description
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", organization="
argument_list|)
operator|.
name|append
argument_list|(
name|organization
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", issueManagement="
argument_list|)
operator|.
name|append
argument_list|(
name|issueManagement
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", scm="
argument_list|)
operator|.
name|append
argument_list|(
name|scm
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", ciManagement="
argument_list|)
operator|.
name|append
argument_list|(
name|ciManagement
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", incomplete="
argument_list|)
operator|.
name|append
argument_list|(
name|incomplete
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectVersionMetadataModel
name|that
init|=
operator|(
name|ProjectVersionMetadataModel
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|rowId
operator|.
name|equals
argument_list|(
name|that
operator|.
name|rowId
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|rowId
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|KeyBuilder
block|{
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|String
name|projectId
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|public
name|KeyBuilder
parameter_list|()
block|{
block|}
specifier|public
name|KeyBuilder
name|withNamespace
parameter_list|(
name|Namespace
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|repositoryId
operator|=
name|namespace
operator|.
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KeyBuilder
name|withNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KeyBuilder
name|withRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KeyBuilder
name|withRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repository
operator|.
name|getId
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KeyBuilder
name|withProjectId
parameter_list|(
name|String
name|projectId
parameter_list|)
block|{
name|this
operator|.
name|projectId
operator|=
name|projectId
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|KeyBuilder
name|withId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|build
parameter_list|()
block|{
comment|// FIXME add some controls
return|return
name|CassandraUtils
operator|.
name|generateKey
argument_list|(
name|this
operator|.
name|repositoryId
argument_list|,
name|this
operator|.
name|namespace
argument_list|,
name|this
operator|.
name|projectId
argument_list|,
name|this
operator|.
name|id
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

