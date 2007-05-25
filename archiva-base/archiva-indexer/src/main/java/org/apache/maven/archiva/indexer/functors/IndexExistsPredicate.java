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
name|indexer
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
name|indexer
operator|.
name|RepositoryContentIndex
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
name|indexer
operator|.
name|RepositoryIndexException
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

begin_comment
comment|/**  * Test the {@link RepositoryContentIndex} object for the existance of an index.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *      role="org.apache.commons.collections.Predicate"   *      role-hint="index-exists"  */
end_comment

begin_class
specifier|public
class|class
name|IndexExistsPredicate
extends|extends
name|AbstractLogEnabled
implements|implements
name|Predicate
block|{
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
name|RepositoryContentIndex
condition|)
block|{
name|RepositoryContentIndex
name|index
init|=
operator|(
name|RepositoryContentIndex
operator|)
name|object
decl_stmt|;
try|try
block|{
name|satisfies
operator|=
name|index
operator|.
name|exists
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Repository Content Index ["
operator|+
name|index
operator|.
name|getId
argument_list|()
operator|+
literal|"] for repository ["
operator|+
name|index
operator|.
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|"] does not exist yet in ["
operator|+
name|index
operator|.
name|getIndexDirectory
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"]."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|satisfies
return|;
block|}
block|}
end_class

end_unit

