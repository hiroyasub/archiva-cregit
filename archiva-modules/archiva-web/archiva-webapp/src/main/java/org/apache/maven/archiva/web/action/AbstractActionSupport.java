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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionSupport
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
name|audit
operator|.
name|AuditEvent
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
name|audit
operator|.
name|AuditListener
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
name|audit
operator|.
name|Auditable
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
name|RepositorySessionFactory
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
name|lang
operator|.
name|StringUtils
import|;
end_import

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
name|security
operator|.
name|ArchivaXworkUser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|ServletActionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|interceptor
operator|.
name|SessionAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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

begin_comment
comment|/**  * LogEnabled and SessionAware ActionSupport  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractActionSupport
extends|extends
name|ActionSupport
implements|implements
name|SessionAware
implements|,
name|Auditable
block|{
specifier|protected
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|session
decl_stmt|;
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|/**      * plexus.requirement role="org.apache.archiva.audit.AuditListener"      */
specifier|private
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
init|=
operator|new
name|ArrayList
argument_list|<
name|AuditListener
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * plexus.requirement      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositorySessionFactory"
argument_list|)
specifier|protected
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|String
name|principal
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
comment|// TODO some caching here
name|this
operator|.
name|auditListeners
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|AuditListener
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|setSession
parameter_list|(
name|Map
name|map
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|void
name|addAuditListener
parameter_list|(
name|AuditListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearAuditListeners
parameter_list|()
block|{
name|this
operator|.
name|auditListeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|removeAuditListener
parameter_list|(
name|AuditListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AuditEvent
name|event
init|=
operator|new
name|AuditEvent
argument_list|(
name|repositoryId
argument_list|,
name|getPrincipal
argument_list|()
argument_list|,
name|resource
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|event
operator|.
name|setRemoteIP
argument_list|(
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|listener
range|:
name|auditListeners
control|)
block|{
name|listener
operator|.
name|auditEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AuditEvent
name|event
init|=
operator|new
name|AuditEvent
argument_list|(
literal|null
argument_list|,
name|getPrincipal
argument_list|()
argument_list|,
name|resource
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|event
operator|.
name|setRemoteIP
argument_list|(
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|listener
range|:
name|auditListeners
control|)
block|{
name|listener
operator|.
name|auditEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|AuditEvent
name|event
init|=
operator|new
name|AuditEvent
argument_list|(
literal|null
argument_list|,
name|getPrincipal
argument_list|()
argument_list|,
literal|null
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|event
operator|.
name|setRemoteIP
argument_list|(
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|listener
range|:
name|auditListeners
control|)
block|{
name|listener
operator|.
name|auditEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getRemoteAddr
parameter_list|()
block|{
name|HttpServletRequest
name|request
init|=
name|ServletActionContext
operator|.
name|getRequest
argument_list|()
decl_stmt|;
return|return
name|request
operator|!=
literal|null
condition|?
name|request
operator|.
name|getRemoteAddr
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|String
name|getPrincipal
parameter_list|()
block|{
if|if
condition|(
name|principal
operator|!=
literal|null
condition|)
block|{
return|return
name|principal
return|;
block|}
return|return
name|ArchivaXworkUser
operator|.
name|getActivePrincipal
argument_list|(
name|ActionContext
operator|.
name|getContext
argument_list|()
operator|.
name|getSession
argument_list|()
argument_list|)
return|;
block|}
name|void
name|setPrincipal
parameter_list|(
name|String
name|principal
parameter_list|)
block|{
name|this
operator|.
name|principal
operator|=
name|principal
expr_stmt|;
block|}
specifier|public
name|void
name|setAuditListeners
parameter_list|(
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|=
name|auditListeners
expr_stmt|;
block|}
specifier|public
name|void
name|setRepositorySessionFactory
parameter_list|(
name|RepositorySessionFactory
name|repositorySessionFactory
parameter_list|)
block|{
name|this
operator|.
name|repositorySessionFactory
operator|=
name|repositorySessionFactory
expr_stmt|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|getBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
comment|//TODO do some caching here !!!
comment|// olamy : with plexus we get only roleHint
comment|// as per convention we named spring bean role#hint remove role# if exists
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|springBeans
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|beans
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
argument_list|(
name|springBeans
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|entry
range|:
name|springBeans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
decl_stmt|;
name|beans
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|beans
return|;
block|}
block|}
end_class

end_unit

