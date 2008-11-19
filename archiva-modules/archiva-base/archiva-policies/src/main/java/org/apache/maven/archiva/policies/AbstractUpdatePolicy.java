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
name|Calendar
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
name|Properties
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
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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

begin_comment
comment|/**  * AbstractUpdatePolicy   *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractUpdatePolicy
implements|implements
name|PreDownloadPolicy
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractUpdatePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The ALWAYS policy setting means that the artifact is always uipdated from the remote repo.      */
specifier|public
specifier|static
specifier|final
name|String
name|ALWAYS
init|=
literal|"always"
decl_stmt|;
comment|/**      * The NEVER policy setting means that the artifact is never updated from the remote repo.      */
specifier|public
specifier|static
specifier|final
name|String
name|NEVER
init|=
literal|"never"
decl_stmt|;
comment|/**      *<p>      * The DAILY policy means that the artifact retrieval occurs only if one of      * the following conditions are met...      *</p>      *<ul>      *<li>The local artifact is not present.</li>      *<li>The local artifact has a last modified timestamp older than (now - 1 day).</li>      *</ul>      */
specifier|public
specifier|static
specifier|final
name|String
name|DAILY
init|=
literal|"daily"
decl_stmt|;
comment|/**      *<p>      * The HOURLY policy means that the artifact retrieval occurs only if one of      * the following conditions are met...      *</p>      *<ul>      *<li>The local artifact is not present.</li>      *<li>The local artifact has a last modified timestamp older than (now - 1 hour).</li>      *</ul>      */
specifier|public
specifier|static
specifier|final
name|String
name|HOURLY
init|=
literal|"hourly"
decl_stmt|;
comment|/**      * The ONCE policy means that the artifact retrieval occurs only if the      * local artifact is not present.  This means that the retreival can only      * occur once.      */
specifier|public
specifier|static
specifier|final
name|String
name|ONCE
init|=
literal|"once"
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
name|AbstractUpdatePolicy
parameter_list|()
block|{
name|options
operator|.
name|add
argument_list|(
name|ALWAYS
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|HOURLY
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|DAILY
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|ONCE
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|NEVER
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|boolean
name|isSnapshotPolicy
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|String
name|getUpdateMode
parameter_list|()
function_decl|;
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
specifier|public
name|void
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
parameter_list|)
throws|throws
name|PolicyViolationException
throws|,
name|PolicyConfigurationException
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getProperty
argument_list|(
literal|"filetype"
argument_list|)
argument_list|,
literal|"artifact"
argument_list|)
condition|)
block|{
comment|// Only process artifact file types.
return|return;
block|}
name|String
name|version
init|=
name|request
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|boolean
name|isSnapshotVersion
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|isSnapshotVersion
operator|=
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
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
literal|"Unknown "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|" policy setting ["
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
name|ALWAYS
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Skip means ok to update.
name|log
operator|.
name|debug
argument_list|(
literal|"OK to update, "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|" policy set to ALWAYS."
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Test for mismatches.
if|if
condition|(
operator|!
name|isSnapshotVersion
operator|&&
name|isSnapshotPolicy
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"OK to update, snapshot policy does not apply for non-snapshot versions."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|isSnapshotVersion
operator|&&
operator|!
name|isSnapshotPolicy
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"OK to update, release policy does not apply for snapshot versions."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|NEVER
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Reject means no.
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"NO to update, "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|" policy set to NEVER."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|localFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// No file means it's ok.
name|log
operator|.
name|debug
argument_list|(
literal|"OK to update "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|", local file does not exist."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|ONCE
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// File exists, but policy is once.
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"NO to update "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|", policy is ONCE, and local file exist."
argument_list|)
throw|;
block|}
if|if
condition|(
name|DAILY
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Calendar
name|fileCal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|fileCal
operator|.
name|setTimeInMillis
argument_list|(
name|localFile
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cal
operator|.
name|after
argument_list|(
name|fileCal
argument_list|)
condition|)
block|{
comment|// Its ok.
return|return;
block|}
else|else
block|{
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"NO to update "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|", policy is DAILY, local file exist, and has been updated within the last day."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|HOURLY
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|HOUR
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|Calendar
name|fileCal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|fileCal
operator|.
name|setTimeInMillis
argument_list|(
name|localFile
operator|.
name|lastModified
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cal
operator|.
name|after
argument_list|(
name|fileCal
argument_list|)
condition|)
block|{
comment|// Its ok.
return|return;
block|}
else|else
block|{
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"NO to update "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|", policy is HOURLY, local file exist, and has been updated within the last hour."
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|PolicyConfigurationException
argument_list|(
literal|"Unable to process "
operator|+
name|getUpdateMode
argument_list|()
operator|+
literal|" policy of ["
operator|+
name|policySetting
operator|+
literal|"], please file a bug report."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

