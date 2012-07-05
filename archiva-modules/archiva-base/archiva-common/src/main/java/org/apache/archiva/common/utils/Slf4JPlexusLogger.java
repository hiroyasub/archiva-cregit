begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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

begin_comment
comment|/**  * Slf4JPlexusLogger - temporary logger to provide an Slf4j Logger to those components  * outside of the archiva codebase that require a plexus logger.  *  *  */
end_comment

begin_class
specifier|public
class|class
name|Slf4JPlexusLogger
implements|implements
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
block|{
specifier|private
name|Logger
name|log
decl_stmt|;
specifier|public
name|Slf4JPlexusLogger
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Slf4JPlexusLogger
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|log
operator|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|debug
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fatalError
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
name|getChildLogger
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|Slf4JPlexusLogger
argument_list|(
name|log
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|name
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|log
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|int
name|getThreshold
parameter_list|()
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
return|return
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
operator|.
name|LEVEL_DEBUG
return|;
block|}
if|else if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
return|return
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
operator|.
name|LEVEL_DEBUG
return|;
block|}
if|else if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
return|return
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
operator|.
name|LEVEL_INFO
return|;
block|}
if|else if
condition|(
name|log
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
return|return
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
operator|.
name|LEVEL_WARN
return|;
block|}
if|else if
condition|(
name|log
operator|.
name|isErrorEnabled
argument_list|()
condition|)
block|{
return|return
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
operator|.
name|LEVEL_ERROR
return|;
block|}
return|return
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|Logger
operator|.
name|LEVEL_DISABLED
return|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|info
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDebugEnabled
parameter_list|()
block|{
return|return
name|log
operator|.
name|isDebugEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isErrorEnabled
parameter_list|()
block|{
return|return
name|log
operator|.
name|isErrorEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isFatalErrorEnabled
parameter_list|()
block|{
return|return
name|log
operator|.
name|isErrorEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isInfoEnabled
parameter_list|()
block|{
return|return
name|log
operator|.
name|isInfoEnabled
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isWarnEnabled
parameter_list|()
block|{
return|return
name|log
operator|.
name|isWarnEnabled
argument_list|()
return|;
block|}
specifier|public
name|void
name|setThreshold
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|warn
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

