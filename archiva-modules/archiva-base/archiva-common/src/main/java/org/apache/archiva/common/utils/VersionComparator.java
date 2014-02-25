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
name|ArrayUtils
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
name|commons
operator|.
name|lang
operator|.
name|math
operator|.
name|NumberUtils
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
name|Comparator
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
comment|/**  * VersionComparator - compare the parts of two version strings.  *<p>  * Technique.  *</p>  *<p>  * * Split the version strings into parts by splitting on<code>"-._"</code> first, then breaking apart words from numbers.  *</p>  *<code>  * "1.0"         = "1", "0"  * "1.0-alpha-1" = "1", "0", "alpha", "1"  * "2.0-rc2"     = "2", "0", "rc", "2"  * "1.3-m2"      = "1", "3", "m", "3"  *</code>  *<p>  * compare each part individually, and when they do not match, perform the following test.  *</p>  *<p>  * Numbers are calculated per normal comparison rules.  * Words that are part of the "special word list" will be treated as their index within that heirarchy.  * Words that cannot be identified as special, are treated using normal case-insensitive comparison rules.  *</p>  *  */
end_comment

begin_class
specifier|public
class|class
name|VersionComparator
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
specifier|private
specifier|static
name|Comparator
argument_list|<
name|String
argument_list|>
name|INSTANCE
init|=
operator|new
name|VersionComparator
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|specialWords
decl_stmt|;
specifier|public
name|VersionComparator
parameter_list|()
block|{
name|specialWords
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|23
argument_list|)
expr_stmt|;
comment|// ids that refer to LATEST
name|specialWords
operator|.
name|add
argument_list|(
literal|"final"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"release"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"current"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"latest"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"g"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"gold"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"fcs"
argument_list|)
expr_stmt|;
comment|// ids that are for a release cycle.
name|specialWords
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"alpha"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"beta"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"pre"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"rc"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"m"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"milestone"
argument_list|)
expr_stmt|;
comment|// ids that are for dev / debug cycles.
name|specialWords
operator|.
name|add
argument_list|(
literal|"dev"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"debug"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"unofficial"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"nightly"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"incubating"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"incubator"
argument_list|)
expr_stmt|;
name|specialWords
operator|.
name|add
argument_list|(
literal|"snapshot"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Comparator
argument_list|<
name|String
argument_list|>
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|o1
parameter_list|,
name|String
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|==
literal|null
operator|&&
name|o2
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|o1
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
if|if
condition|(
name|o2
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|String
index|[]
name|parts1
init|=
name|toParts
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|String
index|[]
name|parts2
init|=
name|toParts
argument_list|(
name|o2
argument_list|)
decl_stmt|;
name|int
name|diff
decl_stmt|;
name|int
name|partLen
init|=
name|Math
operator|.
name|max
argument_list|(
name|parts1
operator|.
name|length
argument_list|,
name|parts2
operator|.
name|length
argument_list|)
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
name|partLen
condition|;
name|i
operator|++
control|)
block|{
name|diff
operator|=
name|comparePart
argument_list|(
name|safePart
argument_list|(
name|parts1
argument_list|,
name|i
argument_list|)
argument_list|,
name|safePart
argument_list|(
name|parts2
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
block|}
name|diff
operator|=
name|parts2
operator|.
name|length
operator|-
name|parts1
operator|.
name|length
expr_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
return|return
name|o1
operator|.
name|compareToIgnoreCase
argument_list|(
name|o2
argument_list|)
return|;
block|}
specifier|private
name|String
name|safePart
parameter_list|(
name|String
index|[]
name|parts
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
if|if
condition|(
name|idx
operator|<
name|parts
operator|.
name|length
condition|)
block|{
return|return
name|parts
index|[
name|idx
index|]
return|;
block|}
return|return
literal|"0"
return|;
block|}
specifier|private
name|int
name|comparePart
parameter_list|(
name|String
name|s1
parameter_list|,
name|String
name|s2
parameter_list|)
block|{
name|boolean
name|is1Num
init|=
name|NumberUtils
operator|.
name|isNumber
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|boolean
name|is2Num
init|=
name|NumberUtils
operator|.
name|isNumber
argument_list|(
name|s2
argument_list|)
decl_stmt|;
comment|// (Special Case) Test for numbers both first.
if|if
condition|(
name|is1Num
operator|&&
name|is2Num
condition|)
block|{
name|int
name|i1
init|=
name|NumberUtils
operator|.
name|toInt
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|int
name|i2
init|=
name|NumberUtils
operator|.
name|toInt
argument_list|(
name|s2
argument_list|)
decl_stmt|;
return|return
name|i1
operator|-
name|i2
return|;
block|}
comment|// Test for text both next.
if|if
condition|(
operator|!
name|is1Num
operator|&&
operator|!
name|is2Num
condition|)
block|{
name|int
name|idx1
init|=
name|specialWords
operator|.
name|indexOf
argument_list|(
name|s1
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|idx2
init|=
name|specialWords
operator|.
name|indexOf
argument_list|(
name|s2
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
comment|// Only operate perform index based operation, if both strings
comment|// are found in the specialWords index.
if|if
condition|(
name|idx1
operator|>=
literal|0
operator|&&
name|idx2
operator|>=
literal|0
condition|)
block|{
return|return
name|idx1
operator|-
name|idx2
return|;
block|}
block|}
comment|// Comparing text to num
if|if
condition|(
operator|!
name|is1Num
operator|&&
name|is2Num
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// Comparing num to text
if|if
condition|(
name|is1Num
operator|&&
operator|!
name|is2Num
condition|)
block|{
return|return
literal|1
return|;
block|}
comment|// Return comparison of strings themselves.
return|return
name|s1
operator|.
name|compareToIgnoreCase
argument_list|(
name|s2
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|toParts
parameter_list|(
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|version
argument_list|)
condition|)
block|{
return|return
name|ArrayUtils
operator|.
name|EMPTY_STRING_ARRAY
return|;
block|}
name|int
name|modeOther
init|=
literal|0
decl_stmt|;
name|int
name|modeDigit
init|=
literal|1
decl_stmt|;
name|int
name|modeText
init|=
literal|2
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|version
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|start
init|=
literal|0
decl_stmt|;
name|int
name|mode
init|=
name|modeOther
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|len
condition|)
block|{
name|char
name|c
init|=
name|version
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|isDigit
argument_list|(
name|c
argument_list|)
condition|)
block|{
if|if
condition|(
name|mode
operator|!=
name|modeDigit
condition|)
block|{
if|if
condition|(
name|mode
operator|!=
name|modeOther
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|version
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mode
operator|=
name|modeDigit
expr_stmt|;
name|start
operator|=
name|i
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|Character
operator|.
name|isLetter
argument_list|(
name|c
argument_list|)
condition|)
block|{
if|if
condition|(
name|mode
operator|!=
name|modeText
condition|)
block|{
if|if
condition|(
name|mode
operator|!=
name|modeOther
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|version
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|mode
operator|=
name|modeText
expr_stmt|;
name|start
operator|=
name|i
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Other.
if|if
condition|(
name|mode
operator|!=
name|modeOther
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|version
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|mode
operator|=
name|modeOther
expr_stmt|;
block|}
block|}
name|i
operator|++
expr_stmt|;
block|}
comment|// Add remainder
if|if
condition|(
name|mode
operator|!=
name|modeOther
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|version
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|parts
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|parts
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

