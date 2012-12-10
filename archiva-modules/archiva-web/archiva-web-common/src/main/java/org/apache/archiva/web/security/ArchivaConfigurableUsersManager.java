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
name|security
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|runtime
operator|.
name|ArchivaRuntimeConfigurationAdmin
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
name|redback
operator|.
name|users
operator|.
name|User
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
name|redback
operator|.
name|users
operator|.
name|UserManager
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
name|redback
operator|.
name|users
operator|.
name|UserManagerException
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
name|redback
operator|.
name|users
operator|.
name|UserManagerListener
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
name|redback
operator|.
name|users
operator|.
name|UserNotFoundException
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
name|redback
operator|.
name|users
operator|.
name|UserQuery
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
name|redback
operator|.
name|users
operator|.
name|configurable
operator|.
name|ConfigurableUserManager
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
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
name|LinkedHashMap
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"userManager#archiva"
argument_list|)
specifier|public
class|class
name|ArchivaConfigurableUsersManager
extends|extends
name|ConfigurableUserManager
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaRuntimeConfigurationAdmin
name|archivaRuntimeConfigurationAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|UserManager
argument_list|>
name|userManagerPerId
decl_stmt|;
specifier|private
name|List
argument_list|<
name|UserManagerListener
argument_list|>
name|listeners
init|=
operator|new
name|ArrayList
argument_list|<
name|UserManagerListener
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|()
block|{
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|userManagerImpls
init|=
name|archivaRuntimeConfigurationAdmin
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getUserManagerImpls
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"use userManagerImpls: '{}'"
argument_list|,
name|userManagerImpls
argument_list|)
expr_stmt|;
name|userManagerPerId
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|UserManager
argument_list|>
argument_list|(
name|userManagerImpls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|id
range|:
name|userManagerImpls
control|)
block|{
name|UserManager
name|userManagerImpl
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"userManager#"
operator|+
name|id
argument_list|,
name|UserManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|setUserManagerImpl
argument_list|(
name|userManagerImpl
argument_list|)
expr_stmt|;
name|userManagerPerId
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|userManagerImpl
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
comment|// revert to a default one ?
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|User
name|addUser
parameter_list|(
name|User
name|user
parameter_list|)
throws|throws
name|UserManagerException
block|{
return|return
name|userManagerPerId
operator|.
name|get
argument_list|(
name|user
operator|.
name|getUserManagerId
argument_list|()
argument_list|)
operator|.
name|addUser
argument_list|(
name|user
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addUserUnchecked
parameter_list|(
name|User
name|user
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|userManagerPerId
operator|.
name|get
argument_list|(
name|user
operator|.
name|getUserManagerId
argument_list|()
argument_list|)
operator|.
name|addUserUnchecked
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|UserManager
name|findFirstWritable
parameter_list|()
block|{
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|userManager
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
return|return
name|userManager
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|User
name|createUser
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|fullName
parameter_list|,
name|String
name|emailAddress
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|UserManager
name|userManager
init|=
name|findFirstWritable
argument_list|()
decl_stmt|;
if|if
condition|(
name|userManager
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"impossible to find a writable userManager"
argument_list|)
throw|;
block|}
return|return
name|userManager
operator|.
name|createUser
argument_list|(
name|username
argument_list|,
name|fullName
argument_list|,
name|emailAddress
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|UserQuery
name|createUserQuery
parameter_list|()
block|{
return|return
name|super
operator|.
name|createUserQuery
argument_list|()
return|;
comment|//To change body of overridden methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteUser
parameter_list|(
name|String
name|username
parameter_list|)
throws|throws
name|UserNotFoundException
throws|,
name|UserManagerException
block|{
name|UserManager
name|userManager
init|=
name|findFirstWritable
argument_list|()
decl_stmt|;
if|if
condition|(
name|userManager
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"impossible to find a writable userManager"
argument_list|)
throw|;
block|}
name|userManager
operator|.
name|deleteUser
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|eraseDatabase
parameter_list|()
block|{
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|userManager
operator|.
name|eraseDatabase
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|User
name|findUser
parameter_list|(
name|String
name|username
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|User
name|user
init|=
literal|null
decl_stmt|;
name|UserManagerException
name|lastException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|user
operator|=
name|userManager
operator|.
name|findUser
argument_list|(
name|username
argument_list|)
expr_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
return|return
name|user
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|e
parameter_list|)
block|{
name|lastException
operator|=
name|e
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UserManagerException
name|e
parameter_list|)
block|{
name|lastException
operator|=
name|e
expr_stmt|;
block|}
block|}
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|lastException
operator|!=
literal|null
condition|)
block|{
throw|throw
name|lastException
throw|;
block|}
block|}
return|return
name|user
return|;
block|}
annotation|@
name|Override
specifier|public
name|User
name|getGuestUser
parameter_list|()
throws|throws
name|UserNotFoundException
throws|,
name|UserManagerException
block|{
name|User
name|user
init|=
literal|null
decl_stmt|;
name|UserNotFoundException
name|lastException
init|=
literal|null
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|user
operator|=
name|userManager
operator|.
name|getGuestUser
argument_list|()
expr_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
return|return
name|user
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|e
parameter_list|)
block|{
name|lastException
operator|=
name|e
expr_stmt|;
block|}
block|}
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
throw|throw
name|lastException
throw|;
block|}
return|return
name|user
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|findUsersByEmailKey
parameter_list|(
name|String
name|emailKey
parameter_list|,
name|boolean
name|orderAscending
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|List
argument_list|<
name|User
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|User
argument_list|>
name|found
init|=
name|userManager
operator|.
name|findUsersByEmailKey
argument_list|(
name|emailKey
argument_list|,
name|orderAscending
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|users
operator|.
name|addAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|users
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|findUsersByFullNameKey
parameter_list|(
name|String
name|fullNameKey
parameter_list|,
name|boolean
name|orderAscending
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|List
argument_list|<
name|User
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|User
argument_list|>
name|found
init|=
name|userManager
operator|.
name|findUsersByFullNameKey
argument_list|(
name|fullNameKey
argument_list|,
name|orderAscending
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|users
operator|.
name|addAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|users
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|findUsersByQuery
parameter_list|(
name|UserQuery
name|query
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|List
argument_list|<
name|User
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|User
argument_list|>
name|found
init|=
name|userManager
operator|.
name|findUsersByQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|users
operator|.
name|addAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|users
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|findUsersByUsernameKey
parameter_list|(
name|String
name|usernameKey
parameter_list|,
name|boolean
name|orderAscending
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|List
argument_list|<
name|User
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|User
argument_list|>
name|found
init|=
name|userManager
operator|.
name|findUsersByUsernameKey
argument_list|(
name|usernameKey
argument_list|,
name|orderAscending
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|users
operator|.
name|addAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|users
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|getUsers
parameter_list|()
throws|throws
name|UserManagerException
block|{
name|List
argument_list|<
name|User
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|User
argument_list|>
name|found
init|=
name|userManager
operator|.
name|getUsers
argument_list|()
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|users
operator|.
name|addAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|users
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|User
argument_list|>
name|getUsers
parameter_list|(
name|boolean
name|orderAscending
parameter_list|)
throws|throws
name|UserManagerException
block|{
name|List
argument_list|<
name|User
argument_list|>
name|users
init|=
operator|new
name|ArrayList
argument_list|<
name|User
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UserManager
name|userManager
range|:
name|userManagerPerId
operator|.
name|values
argument_list|()
control|)
block|{
name|List
argument_list|<
name|User
argument_list|>
name|found
init|=
name|userManager
operator|.
name|getUsers
argument_list|(
name|orderAscending
argument_list|)
decl_stmt|;
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|users
operator|.
name|addAll
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|users
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
block|{
comment|//olamy: must be it depends :-)
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|User
name|updateUser
parameter_list|(
name|User
name|user
parameter_list|)
throws|throws
name|UserNotFoundException
throws|,
name|UserManagerException
block|{
return|return
name|userManagerPerId
operator|.
name|get
argument_list|(
name|user
operator|.
name|getUserManagerId
argument_list|()
argument_list|)
operator|.
name|updateUser
argument_list|(
name|user
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|User
name|updateUser
parameter_list|(
name|User
name|user
parameter_list|,
name|boolean
name|passwordChangeRequired
parameter_list|)
throws|throws
name|UserNotFoundException
throws|,
name|UserManagerException
block|{
return|return
name|userManagerPerId
operator|.
name|get
argument_list|(
name|user
operator|.
name|getUserManagerId
argument_list|()
argument_list|)
operator|.
name|updateUser
argument_list|(
name|user
argument_list|,
name|passwordChangeRequired
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUserManagerImpl
parameter_list|(
name|UserManager
name|userManagerImpl
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"setUserManagerImpl cannot be used in this implementation"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addUserManagerListener
parameter_list|(
name|UserManagerListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeUserManagerListener
parameter_list|(
name|UserManagerListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|fireUserManagerInit
parameter_list|(
name|boolean
name|freshDatabase
parameter_list|)
block|{
for|for
control|(
name|UserManagerListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|userManagerInit
argument_list|(
name|freshDatabase
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|fireUserManagerUserAdded
parameter_list|(
name|User
name|addedUser
parameter_list|)
block|{
for|for
control|(
name|UserManagerListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|userManagerUserAdded
argument_list|(
name|addedUser
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|fireUserManagerUserRemoved
parameter_list|(
name|User
name|removedUser
parameter_list|)
block|{
for|for
control|(
name|UserManagerListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|userManagerUserRemoved
argument_list|(
name|removedUser
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|fireUserManagerUserUpdated
parameter_list|(
name|User
name|updatedUser
parameter_list|)
block|{
for|for
control|(
name|UserManagerListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|userManagerUserUpdated
argument_list|(
name|updatedUser
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|User
name|createGuestUser
parameter_list|()
throws|throws
name|UserManagerException
block|{
return|return
name|findFirstWritable
argument_list|()
operator|.
name|createGuestUser
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFinalImplementation
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getDescriptionKey
parameter_list|()
block|{
return|return
literal|"archiva.redback.usermanager.configurable.archiva"
return|;
block|}
block|}
end_class

end_unit

