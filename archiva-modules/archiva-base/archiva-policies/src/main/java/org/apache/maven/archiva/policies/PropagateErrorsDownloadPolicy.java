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
name|policies
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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|java
operator|.
name|io
operator|.
name|File
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

begin_comment
comment|/**  * PropagateErrorsPolicy - a policy applied on error to determine how to treat the error.  *  * @plexus.component role="org.apache.maven.archiva.policies.DownloadErrorPolicy"  * role-hint="propagate-errors"  */
end_comment

begin_class
specifier|public
class|class
name|PropagateErrorsDownloadPolicy
implements|implements
name|DownloadErrorPolicy
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PropagateErrorsDownloadPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Signifies any error should stop searching for other proxies.      */
specifier|public
specifier|static
specifier|final
name|String
name|STOP
init|=
literal|"stop"
decl_stmt|;
comment|/**      * Propagate errors at the end after all are gathered, if there was no successful download from other proxies.      */
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE
init|=
literal|"queue error"
decl_stmt|;
comment|/**      * Ignore errors and treat as if it were not found.      */
specifier|public
specifier|static
specifier|final
name|String
name|IGNORE
init|=
literal|"ignore"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|PropagateErrorsDownloadPolicy
parameter_list|()
block|{
name|options
operator|.
name|add
argument_list|(
name|STOP
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|QUEUE
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|IGNORE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|applyPolicy
parameter_list|(
name|String
name|policySetting
parameter_list|,
name|Properties
name|request
parameter_list|,
name|File
name|localFile
parameter_list|,
name|Exception
name|exception
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|previousExceptions
parameter_list|)
throws|throws
name|PolicyConfigurationException
block|{
if|if
condition|(
operator|!
name|options
operator|.
name|contains
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Not a valid code.
throw|throw
operator|new
name|PolicyConfigurationException
argument_list|(
literal|"Unknown error policy setting ["
operator|+
name|policySetting
operator|+
literal|"], valid settings are ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|options
operator|.
name|iterator
argument_list|()
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]"
argument_list|)
throw|;
block|}
if|if
condition|(
name|IGNORE
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Ignore.
name|log
operator|.
name|debug
argument_list|(
literal|"Error policy set to IGNORE."
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|repositoryId
init|=
name|request
operator|.
name|getProperty
argument_list|(
literal|"remoteRepositoryId"
argument_list|)
decl_stmt|;
if|if
condition|(
name|STOP
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|QUEUE
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
name|previousExceptions
operator|.
name|put
argument_list|(
name|repositoryId
argument_list|,
name|exception
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
throw|throw
operator|new
name|PolicyConfigurationException
argument_list|(
literal|"Unable to process checksum policy of ["
operator|+
name|policySetting
operator|+
literal|"], please file a bug report."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getDefaultOption
parameter_list|()
block|{
return|return
name|QUEUE
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"propagate-errors"
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"On remote error"
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

