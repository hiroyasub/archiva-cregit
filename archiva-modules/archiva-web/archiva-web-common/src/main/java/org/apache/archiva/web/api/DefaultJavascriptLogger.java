begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|api
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|model
operator|.
name|JavascriptLog
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
name|stereotype
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"javascriptLogger#default"
argument_list|)
specifier|public
class|class
name|DefaultJavascriptLogger
implements|implements
name|JavascriptLogger
block|{
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Boolean
name|trace
parameter_list|(
name|JavascriptLog
name|javascriptLog
parameter_list|)
block|{
name|Logger
name|toUse
init|=
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
operator|==
literal|null
condition|?
name|logger
else|:
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
name|toUse
operator|.
name|trace
argument_list|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
expr|@
name|Override
operator|.
name|TRUE
return|;
block|}
specifier|public
name|Boolean
name|debug
parameter_list|(
name|JavascriptLog
name|javascriptLog
parameter_list|)
block|{
name|Logger
name|toUse
init|=
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
operator|==
literal|null
condition|?
name|logger
else|:
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
name|toUse
operator|.
name|debug
argument_list|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|Override
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|public
name|Boolean
name|info
parameter_list|(
name|JavascriptLog
name|javascriptLog
parameter_list|)
block|{
name|Logger
name|toUse
init|=
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
operator|==
literal|null
condition|?
name|logger
else|:
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
name|toUse
operator|.
name|info
argument_list|(
name|javascriptLog
operator|.
name|getMessage
expr|@
name|Override
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|public
name|Boolean
name|warn
parameter_list|(
name|JavascriptLog
name|javascriptLog
parameter_list|)
block|{
name|Logger
name|toUse
init|=
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
operator|==
literal|null
condition|?
name|logger
else|:
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
name|toUse
operator|.
name|warn
argument_list|(
name|javascript
expr|@
name|Override
name|Log
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|public
name|Boolean
name|error
parameter_list|(
name|JavascriptLog
name|javascriptLog
parameter_list|)
block|{
name|Logger
name|toUse
init|=
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
operator|==
literal|null
condition|?
name|logger
else|:
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|javascriptLog
operator|.
name|getLoggerName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
name|toUse
operator|.
name|error
argument_list|(
name|javascriptLog
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
end_class

end_unit

