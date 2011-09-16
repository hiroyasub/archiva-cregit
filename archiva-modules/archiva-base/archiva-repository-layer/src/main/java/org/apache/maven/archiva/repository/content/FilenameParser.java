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
name|repository
operator|.
name|content
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
comment|/**  * Generic Filename Parser for use with layout routines.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FilenameParser
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|extension
decl_stmt|;
specifier|private
name|int
name|offset
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|mavenPluginPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(maven-.*-plugin)|(.*-maven-plugin)"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|extensionPattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(\\.tar\\.gz$)|(\\.tar\\.bz2$)|(\\.[\\-a-z0-9]*$)"
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|SNAPSHOT_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^([0-9]{8}\\.[0-9]{6}-[0-9]+)(.*)$"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|section
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"([^-]*)"
argument_list|)
decl_stmt|;
specifier|private
name|Matcher
name|matcher
decl_stmt|;
specifier|protected
name|FilenameParser
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|filename
expr_stmt|;
name|Matcher
name|mat
init|=
name|extensionPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|mat
operator|.
name|find
argument_list|()
condition|)
block|{
name|extension
operator|=
name|filename
operator|.
name|substring
argument_list|(
name|mat
operator|.
name|start
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
name|extension
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|matcher
operator|=
name|section
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|reset
parameter_list|()
block|{
name|offset
operator|=
literal|0
expr_stmt|;
block|}
specifier|protected
name|String
name|next
parameter_list|()
block|{
comment|// Past the end of the string.
if|if
condition|(
name|offset
operator|>
name|name
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// Return the next section.
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|(
name|offset
argument_list|)
condition|)
block|{
comment|// Return found section.
name|offset
operator|=
name|matcher
operator|.
name|end
argument_list|()
operator|+
literal|1
expr_stmt|;
return|return
name|matcher
operator|.
name|group
argument_list|()
return|;
block|}
comment|// Nothing to return.
return|return
literal|null
return|;
block|}
specifier|protected
name|String
name|expect
parameter_list|(
name|String
name|expected
parameter_list|)
block|{
name|String
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|expected
argument_list|,
name|offset
argument_list|)
condition|)
block|{
name|value
operator|=
name|expected
expr_stmt|;
block|}
if|else if
condition|(
name|VersionUtil
operator|.
name|isGenericSnapshot
argument_list|(
name|expected
argument_list|)
condition|)
block|{
name|String
name|version
init|=
name|name
operator|.
name|substring
argument_list|(
name|offset
argument_list|)
decl_stmt|;
comment|// check it starts with the same version up to the snapshot part
name|int
name|leadingLength
init|=
name|expected
operator|.
name|length
argument_list|()
operator|-
literal|9
decl_stmt|;
if|if
condition|(
name|leadingLength
operator|>
literal|0
operator|&&
name|version
operator|.
name|startsWith
argument_list|(
name|expected
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|leadingLength
argument_list|)
argument_list|)
operator|&&
name|version
operator|.
name|length
argument_list|()
operator|>
name|leadingLength
condition|)
block|{
comment|// If we expect a non-generic snapshot - look for the timestamp
name|Matcher
name|m
init|=
name|SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|version
operator|.
name|substring
argument_list|(
name|leadingLength
operator|+
literal|1
argument_list|)
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
name|value
operator|=
name|version
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|leadingLength
operator|+
literal|1
argument_list|)
operator|+
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
comment|// Potential hit. check for '.' or '-' at end of expected.
name|int
name|seperatorOffset
init|=
name|offset
operator|+
name|value
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// Test for "out of bounds" first.
if|if
condition|(
name|seperatorOffset
operator|>=
name|name
operator|.
name|length
argument_list|()
condition|)
block|{
name|offset
operator|=
name|name
operator|.
name|length
argument_list|()
expr_stmt|;
return|return
name|value
return|;
block|}
comment|// Test for seperator char.
name|char
name|seperatorChar
init|=
name|name
operator|.
name|charAt
argument_list|(
name|seperatorOffset
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|seperatorChar
operator|==
literal|'-'
operator|)
operator|||
operator|(
name|seperatorChar
operator|==
literal|'.'
operator|)
condition|)
block|{
name|offset
operator|=
name|seperatorOffset
operator|+
literal|1
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Get the current seperator character.      *      * @return the seperator character (either '.' or '-'), or 0 if no seperator character available.      */
specifier|protected
name|char
name|seperator
parameter_list|()
block|{
comment|// Past the end of the string?
if|if
condition|(
name|offset
operator|>=
name|name
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// Before the start of the string?
if|if
condition|(
name|offset
operator|<=
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|name
operator|.
name|charAt
argument_list|(
name|offset
operator|-
literal|1
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|protected
name|String
name|getExtension
parameter_list|()
block|{
return|return
name|extension
return|;
block|}
specifier|protected
name|String
name|remaining
parameter_list|()
block|{
if|if
condition|(
name|offset
operator|>=
name|name
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|end
init|=
name|name
operator|.
name|substring
argument_list|(
name|offset
argument_list|)
decl_stmt|;
name|offset
operator|=
name|name
operator|.
name|length
argument_list|()
expr_stmt|;
return|return
name|end
return|;
block|}
specifier|protected
name|String
name|nextNonVersion
parameter_list|()
block|{
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|StringBuffer
name|ver
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
comment|// Any text upto the end of a special case is considered non-version.
name|Matcher
name|specialMat
init|=
name|mavenPluginPattern
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|specialMat
operator|.
name|find
argument_list|()
condition|)
block|{
name|ver
operator|.
name|append
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|offset
argument_list|,
name|specialMat
operator|.
name|end
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|offset
operator|=
name|specialMat
operator|.
name|end
argument_list|()
operator|+
literal|1
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|int
name|initialOffset
init|=
name|offset
decl_stmt|;
name|String
name|section
init|=
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|section
operator|==
literal|null
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|VersionUtil
operator|.
name|isVersion
argument_list|(
name|section
argument_list|)
condition|)
block|{
if|if
condition|(
name|ver
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ver
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
name|ver
operator|.
name|append
argument_list|(
name|section
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|offset
operator|=
name|initialOffset
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|ver
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|String
name|nextVersion
parameter_list|()
block|{
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|ver
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|int
name|initialOffset
init|=
name|offset
decl_stmt|;
name|String
name|section
init|=
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|section
operator|==
literal|null
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
name|VersionUtil
operator|.
name|isVersion
argument_list|(
name|section
argument_list|)
condition|)
block|{
if|if
condition|(
name|ver
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ver
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
block|}
name|ver
operator|.
name|append
argument_list|(
name|section
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|offset
operator|=
name|initialOffset
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|ver
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

