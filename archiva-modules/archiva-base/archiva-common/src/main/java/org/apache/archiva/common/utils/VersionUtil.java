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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Version utility methods.  */
end_comment

begin_class
specifier|public
class|class
name|VersionUtil
block|{
comment|/**      * These are the version patterns found in the filenames of the various artifact's versions IDs.      * These patterns are all tackling lowercase version IDs.      */
specifier|private
specifier|static
specifier|final
name|String
name|versionPatterns
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"([0-9][_.0-9a-z]*)"
block|,
literal|"(snapshot)"
block|,
literal|"(g?[_.0-9ab]*(pre|rc|g|m)[_.0-9]*)"
block|,
literal|"(dev[_.0-9]*)"
block|,
literal|"(alpha[_.0-9]*)"
block|,
literal|"(beta[_.0-9]*)"
block|,
literal|"(rc[_.0-9]*)"
block|,
comment|//        "(test[_.0-9]*)", -- omitted for MRM-681, can be reinstated as part of MRM-712
literal|"(debug[_.0-9]*)"
block|,
literal|"(unofficial[_.0-9]*)"
block|,
literal|"(current)"
block|,
literal|"(latest)"
block|,
literal|"(fcs)"
block|,
literal|"(release[_.0-9]*)"
block|,
literal|"(nightly)"
block|,
literal|"(final)"
block|,
literal|"(incubating)"
block|,
literal|"(incubator)"
block|,
literal|"([ab][_.0-9]+)"
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SNAPSHOT
init|=
literal|"SNAPSHOT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Pattern
name|UNIQUE_SNAPSHOT_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(.*)-([0-9]{8}\\.[0-9]{6})-([0-9]+)$"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Pattern
name|TIMESTAMP_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^([0-9]{8})\\.([0-9]{6})$"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Pattern
name|GENERIC_SNAPSHOT_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(.*)-"
operator|+
name|SNAPSHOT
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|VERSION_MEGA_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|StringUtils
operator|.
name|join
argument_list|(
name|versionPatterns
argument_list|,
literal|'|'
argument_list|)
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
comment|/**      *<p>      * Tests if the unknown string contains elements that identify it as a version string (or not).      *</p>      *      *<p>      * The algorithm tests each part of the string that is delimited by a '-' (dash) character.      * If 75% or more of the sections are identified as 'version' strings, the result is      * determined to be of a high probability to be version identifier string.      *</p>      *      * @param unknown the unknown string to test.      * @return true if the unknown string is likely a version string.      */
specifier|public
specifier|static
name|boolean
name|isVersion
parameter_list|(
name|String
name|unknown
parameter_list|)
block|{
name|String
name|versionParts
index|[]
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|unknown
argument_list|,
literal|'-'
argument_list|)
decl_stmt|;
name|Matcher
name|mat
decl_stmt|;
name|int
name|countValidParts
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|versionParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|part
init|=
name|versionParts
index|[
name|i
index|]
decl_stmt|;
name|mat
operator|=
name|VERSION_MEGA_PATTERN
operator|.
name|matcher
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
name|mat
operator|.
name|matches
argument_list|()
condition|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
comment|// loosen rule to return true if first token matches
block|{
return|return
literal|true
return|;
block|}
name|countValidParts
operator|++
expr_stmt|;
block|}
block|}
comment|/* Calculate version probability as true if 3/4's of the input string has pieces of          * of known version identifier strings.          */
name|int
name|threshold
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|floor
argument_list|(
name|Math
operator|.
name|max
argument_list|(
operator|(
name|double
operator|)
literal|1.0
argument_list|,
operator|(
name|double
operator|)
operator|(
name|versionParts
operator|.
name|length
operator|*
literal|0.75
operator|)
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|countValidParts
operator|>=
name|threshold
operator|)
return|;
block|}
comment|/**      *<p>      * Tests if the identifier is a known simple version keyword.      *</p>      *      *<p>      * This method is different from {@link #isVersion(String)} in that it tests the whole input string in      * one go as a simple identifier. (eg "alpha", "1.0", "beta", "debug", "latest", "rc#", etc...)      *</p>      *      * @param identifier the identifier to test.      * @return true if the unknown string is likely a version string.      */
specifier|public
specifier|static
name|boolean
name|isSimpleVersionKeyword
parameter_list|(
name|String
name|identifier
parameter_list|)
block|{
name|Matcher
name|mat
init|=
name|VERSION_MEGA_PATTERN
operator|.
name|matcher
argument_list|(
name|identifier
argument_list|)
decl_stmt|;
return|return
name|mat
operator|.
name|matches
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isSnapshot
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|UNIQUE_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
name|isGenericSnapshot
argument_list|(
name|version
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getBaseVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|UNIQUE_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|+
literal|"-"
operator|+
name|SNAPSHOT
return|;
block|}
else|else
block|{
return|return
name|version
return|;
block|}
block|}
comment|/**      *<p>      * Get the release version of the snapshot version.      *</p>      *       *<p>      * If snapshot version is 1.0-SNAPSHOT, then release version would be 1.0      * And if snapshot version is 1.0-20070113.163208-1.jar, then release version would still be 1.0      *</p>      *      * @param snapshotVersion snapshot version      * @return release version      */
specifier|public
specifier|static
name|String
name|getReleaseVersion
parameter_list|(
name|String
name|snapshotVersion
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|UNIQUE_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|snapshotVersion
argument_list|)
decl_stmt|;
if|if
condition|(
name|isGenericSnapshot
argument_list|(
name|snapshotVersion
argument_list|)
condition|)
block|{
name|m
operator|=
name|GENERIC_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|snapshotVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|snapshotVersion
return|;
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isUniqueSnapshot
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|UNIQUE_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isGenericSnapshot
parameter_list|(
name|String
name|version
parameter_list|)
block|{
return|return
name|version
operator|.
name|endsWith
argument_list|(
name|SNAPSHOT
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getVersionFromGenericSnapshot
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|GENERIC_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
name|version
return|;
block|}
block|}
end_class

end_unit

