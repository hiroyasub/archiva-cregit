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
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"projectVersionMetadata"
argument_list|)
specifier|public
class|class
name|ProjectVersionMetadata
extends|extends
name|FacetedMetadata
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5506968284780639002L
decl_stmt|;
comment|/**      * id is the version      */
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|description
decl_stmt|;
specifier|private
name|Organization
name|organization
decl_stmt|;
specifier|private
name|IssueManagement
name|issueManagement
decl_stmt|;
specifier|private
name|Scm
name|scm
decl_stmt|;
specifier|private
name|CiManagement
name|ciManagement
decl_stmt|;
specifier|private
name|List
argument_list|<
name|License
argument_list|>
name|licenses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MailingList
argument_list|>
name|mailingLists
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|incomplete
decl_stmt|;
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
name|String
name|getVersion
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
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
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
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
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
name|void
name|addLicense
parameter_list|(
name|License
name|license
parameter_list|)
block|{
name|this
operator|.
name|licenses
operator|.
name|add
argument_list|(
name|license
argument_list|)
expr_stmt|;
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
name|addMailingList
parameter_list|(
name|MailingList
name|mailingList
parameter_list|)
block|{
name|this
operator|.
name|mailingLists
operator|.
name|add
argument_list|(
name|mailingList
argument_list|)
expr_stmt|;
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
name|addDependency
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
name|this
operator|.
name|dependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
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
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ProjectVersionMetadata{"
operator|+
literal|"id='"
operator|+
name|id
operator|+
literal|'\''
operator|+
literal|", url='"
operator|+
name|url
operator|+
literal|'\''
operator|+
literal|", name='"
operator|+
name|name
operator|+
literal|'\''
operator|+
literal|", description='"
operator|+
name|description
operator|+
literal|'\''
operator|+
literal|", organization="
operator|+
name|organization
operator|+
literal|", issueManagement="
operator|+
name|issueManagement
operator|+
literal|", scm="
operator|+
name|scm
operator|+
literal|", ciManagement="
operator|+
name|ciManagement
operator|+
literal|", licenses="
operator|+
name|licenses
operator|+
literal|", mailingLists="
operator|+
name|mailingLists
operator|+
literal|", dependencies="
operator|+
name|dependencies
operator|+
literal|", incomplete="
operator|+
name|incomplete
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

