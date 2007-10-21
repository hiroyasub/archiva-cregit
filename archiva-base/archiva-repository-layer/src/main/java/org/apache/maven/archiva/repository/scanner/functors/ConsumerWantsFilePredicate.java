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
name|scanner
operator|.
name|functors
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
name|collections
operator|.
name|Predicate
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
name|BaseFile
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
name|consumers
operator|.
name|RepositoryContentConsumer
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
name|util
operator|.
name|SelectorUtils
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
name|util
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
name|List
import|;
end_import

begin_comment
comment|/**  * ConsumerWantsFilePredicate   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ConsumerWantsFilePredicate
implements|implements
name|Predicate
block|{
specifier|private
name|BaseFile
name|basefile
decl_stmt|;
specifier|private
name|boolean
name|isCaseSensitive
init|=
literal|true
decl_stmt|;
specifier|private
name|int
name|wantedFileCount
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|changesSince
init|=
literal|0
decl_stmt|;
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|boolean
name|satisfies
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|RepositoryContentConsumer
condition|)
block|{
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|wantsFile
argument_list|(
name|consumer
argument_list|,
name|StringUtils
operator|.
name|replace
argument_list|(
name|basefile
operator|.
name|getRelativePath
argument_list|()
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
argument_list|)
condition|)
block|{
name|satisfies
operator|=
literal|true
expr_stmt|;
comment|// regardless of the timestamp, we record that it was wanted so it doesn't get counted as invalid
name|wantedFileCount
operator|++
expr_stmt|;
if|if
condition|(
operator|!
name|consumer
operator|.
name|isProcessUnmodified
argument_list|()
condition|)
block|{
comment|// Timestamp finished points to the last successful scan, not this current one.
if|if
condition|(
name|basefile
operator|.
name|lastModified
argument_list|()
operator|<
name|changesSince
condition|)
block|{
comment|// Skip file as no change has occured.
name|satisfies
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|satisfies
return|;
block|}
specifier|public
name|BaseFile
name|getBasefile
parameter_list|()
block|{
return|return
name|basefile
return|;
block|}
specifier|public
name|int
name|getWantedFileCount
parameter_list|()
block|{
return|return
name|wantedFileCount
return|;
block|}
specifier|public
name|boolean
name|isCaseSensitive
parameter_list|()
block|{
return|return
name|isCaseSensitive
return|;
block|}
specifier|public
name|void
name|setBasefile
parameter_list|(
name|BaseFile
name|basefile
parameter_list|)
block|{
name|this
operator|.
name|basefile
operator|=
name|basefile
expr_stmt|;
name|this
operator|.
name|wantedFileCount
operator|=
literal|0
expr_stmt|;
block|}
specifier|public
name|void
name|setCaseSensitive
parameter_list|(
name|boolean
name|isCaseSensitive
parameter_list|)
block|{
name|this
operator|.
name|isCaseSensitive
operator|=
name|isCaseSensitive
expr_stmt|;
block|}
specifier|private
name|boolean
name|wantsFile
parameter_list|(
name|RepositoryContentConsumer
name|consumer
parameter_list|,
name|String
name|relativePath
parameter_list|)
block|{
comment|// Test excludes first.
name|List
argument_list|<
name|String
argument_list|>
name|excludes
init|=
name|consumer
operator|.
name|getExcludes
argument_list|()
decl_stmt|;
if|if
condition|(
name|excludes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|excludes
control|)
block|{
if|if
condition|(
name|SelectorUtils
operator|.
name|matchPath
argument_list|(
name|pattern
argument_list|,
name|relativePath
argument_list|,
name|isCaseSensitive
argument_list|)
condition|)
block|{
comment|// Definately does NOT WANT FILE.
return|return
literal|false
return|;
block|}
block|}
block|}
comment|// Now test includes.
for|for
control|(
name|String
name|pattern
range|:
name|consumer
operator|.
name|getIncludes
argument_list|()
control|)
block|{
if|if
condition|(
name|SelectorUtils
operator|.
name|matchPath
argument_list|(
name|pattern
argument_list|,
name|relativePath
argument_list|,
name|isCaseSensitive
argument_list|)
condition|)
block|{
comment|// Specifically WANTS FILE.
return|return
literal|true
return|;
block|}
block|}
comment|// Not included, and Not excluded?  Default to EXCLUDE.
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setChangesSince
parameter_list|(
name|long
name|changesSince
parameter_list|)
block|{
name|this
operator|.
name|changesSince
operator|=
name|changesSince
expr_stmt|;
block|}
block|}
end_class

end_unit

