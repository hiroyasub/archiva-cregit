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
name|database
operator|.
name|jdo
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|Constraint
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
name|database
operator|.
name|DeclarativeConstraint
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
name|database
operator|.
name|ObjectNotFoundException
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
name|database
operator|.
name|SimpleConstraint
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
name|database
operator|.
name|constraints
operator|.
name|AbstractSimpleConstraint
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
name|model
operator|.
name|CompoundKey
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
name|jdo
operator|.
name|JdoFactory
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
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|javax
operator|.
name|jdo
operator|.
name|Extent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOHelper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOObjectNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOUserException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|Transaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|datastore
operator|.
name|DataStoreCache
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|listener
operator|.
name|InstanceLifecycleEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|listener
operator|.
name|InstanceLifecycleListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|listener
operator|.
name|StoreLifecycleListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|spi
operator|.
name|Detachable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|spi
operator|.
name|PersistenceCapable
import|;
end_import

begin_comment
comment|/**  * JdoAccess   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.database.jdo.JdoAccess" role-hint="archiva"  */
end_comment

begin_class
specifier|public
class|class
name|JdoAccess
extends|extends
name|AbstractLogEnabled
implements|implements
name|Initializable
implements|,
name|InstanceLifecycleListener
implements|,
name|StoreLifecycleListener
block|{
comment|/**      * @plexus.requirement role-hint="archiva"      */
specifier|private
name|JdoFactory
name|jdoFactory
decl_stmt|;
specifier|private
name|PersistenceManagerFactory
name|pmf
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|pmf
operator|=
name|jdoFactory
operator|.
name|getPersistenceManagerFactory
argument_list|()
expr_stmt|;
comment|/* Primitive (and failed) attempt at creating the schema on startup.          Just to prevent the multiple stack trace warnings on auto-gen of schema.                    // Create the schema (if needed)          URL jdoFileUrls[] = new URL[] { getClass().getResource( "/org/apache/maven/archiva/model/package.jdo" ) };           File propsFile = null; // intentional          boolean verbose = true;           try          {          String connectionFactoryName = pmf.getConnectionFactoryName();          if ( StringUtils.isNotBlank( connectionFactoryName )&& connectionFactoryName.startsWith( "java:comp" ) )          {          // We have a JNDI datasource!          String jndiDatasource = connectionFactoryName;          System.setProperty( PMFConfiguration.JDO_DATASTORE_URL_PROPERTY, jndiDatasource );          }                    // TODO: figure out how to get the jdbc driver details from JNDI to pass into SchemaTool.           SchemaTool.createSchemaTables( jdoFileUrls, new URL[] {}, propsFile, verbose, null );          }          catch ( Exception e )          {          getLogger().error( "Unable to create schema: " + e.getMessage(), e );          }           pmf.getPersistenceManager();          */
comment|// Add the lifecycle listener.
name|pmf
operator|.
name|addInstanceLifecycleListener
argument_list|(
name|this
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|dumpObjectState
parameter_list|(
name|PrintStream
name|out
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
specifier|final
name|String
name|STATE
init|=
literal|"[STATE] "
decl_stmt|;
specifier|final
name|String
name|INDENT
init|=
literal|"        "
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|STATE
operator|+
literal|"Object is null."
argument_list|)
expr_stmt|;
return|return;
block|}
name|out
operator|.
name|println
argument_list|(
name|STATE
operator|+
literal|"Object "
operator|+
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|PersistenceCapable
operator|)
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is NOT PersistenceCapable (not a jdo object?)"
argument_list|)
expr_stmt|;
return|return;
block|}
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is PersistenceCapable."
argument_list|)
expr_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Detachable
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is Detachable"
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is new : "
operator|+
name|Boolean
operator|.
name|toString
argument_list|(
name|JDOHelper
operator|.
name|isNew
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is transactional : "
operator|+
name|Boolean
operator|.
name|toString
argument_list|(
name|JDOHelper
operator|.
name|isTransactional
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is deleted : "
operator|+
name|Boolean
operator|.
name|toString
argument_list|(
name|JDOHelper
operator|.
name|isDeleted
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is detached : "
operator|+
name|Boolean
operator|.
name|toString
argument_list|(
name|JDOHelper
operator|.
name|isDetached
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is dirty : "
operator|+
name|Boolean
operator|.
name|toString
argument_list|(
name|JDOHelper
operator|.
name|isDirty
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"is persistent : "
operator|+
name|Boolean
operator|.
name|toString
argument_list|(
name|JDOHelper
operator|.
name|isPersistent
argument_list|(
name|o
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|INDENT
operator|+
literal|"object id : "
operator|+
name|JDOHelper
operator|.
name|getObjectId
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PersistenceManager
name|getPersistenceManager
parameter_list|()
block|{
name|PersistenceManager
name|pm
init|=
name|pmf
operator|.
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|pm
operator|.
name|getFetchPlan
argument_list|()
operator|.
name|setMaxFetchDepth
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|pm
return|;
block|}
specifier|public
name|void
name|enableCache
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
name|DataStoreCache
name|cache
init|=
name|pmf
operator|.
name|getDataStoreCache
argument_list|()
decl_stmt|;
name|cache
operator|.
name|pinAll
argument_list|(
name|clazz
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Pin all objects of type clazz from now on
block|}
specifier|public
name|Object
name|saveObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|saveObject
argument_list|(
name|object
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|Object
name|saveObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
index|[]
name|fetchGroups
parameter_list|)
block|{
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|JDOHelper
operator|.
name|getObjectId
argument_list|(
name|object
argument_list|)
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|JDOHelper
operator|.
name|isDetached
argument_list|(
name|object
argument_list|)
condition|)
block|{
comment|// This is a fatal error that means we need to fix our code.
comment|// Leave it as a JDOUserException, it's intentional.
throw|throw
operator|new
name|JDOUserException
argument_list|(
literal|"Existing object is not detached: "
operator|+
name|object
argument_list|,
name|object
argument_list|)
throw|;
block|}
if|if
condition|(
name|fetchGroups
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|>=
name|fetchGroups
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|pm
operator|.
name|getFetchPlan
argument_list|()
operator|.
name|addGroup
argument_list|(
name|fetchGroups
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|pm
operator|.
name|makePersistent
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|object
operator|=
name|pm
operator|.
name|detachCopy
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|object
return|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
name|getAllObjects
parameter_list|(
name|Class
name|clazz
parameter_list|)
block|{
return|return
name|queryObjects
argument_list|(
name|clazz
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|List
name|queryObjects
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Constraint
name|constraint
parameter_list|)
block|{
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
name|List
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|constraint
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|constraint
operator|instanceof
name|DeclarativeConstraint
condition|)
block|{
name|result
operator|=
name|processConstraint
argument_list|(
name|pm
argument_list|,
name|clazz
argument_list|,
operator|(
name|DeclarativeConstraint
operator|)
name|constraint
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|constraint
operator|instanceof
name|AbstractSimpleConstraint
condition|)
block|{
name|result
operator|=
name|processConstraint
argument_list|(
name|pm
argument_list|,
operator|(
name|SimpleConstraint
operator|)
name|constraint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|processUnconstrained
argument_list|(
name|pm
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|processUnconstrained
argument_list|(
name|pm
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
operator|(
name|List
operator|)
name|pm
operator|.
name|detachCopyAll
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
name|queryObjects
parameter_list|(
name|SimpleConstraint
name|constraint
parameter_list|)
block|{
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
name|List
name|result
init|=
name|processConstraint
argument_list|(
name|pm
argument_list|,
name|constraint
argument_list|)
decl_stmt|;
comment|// Only detach if results are known to be persistable.
if|if
condition|(
name|constraint
operator|.
name|isResultsPersistable
argument_list|()
condition|)
block|{
name|result
operator|=
operator|(
name|List
operator|)
name|pm
operator|.
name|detachCopyAll
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
name|copiedResults
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|copiedResults
operator|.
name|addAll
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|copiedResults
expr_stmt|;
block|}
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
name|processUnconstrained
parameter_list|(
name|PersistenceManager
name|pm
parameter_list|,
name|Class
name|clazz
parameter_list|)
block|{
name|Extent
name|extent
init|=
name|pm
operator|.
name|getExtent
argument_list|(
name|clazz
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Query
name|query
init|=
name|pm
operator|.
name|newQuery
argument_list|(
name|extent
argument_list|)
decl_stmt|;
return|return
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|()
return|;
block|}
specifier|private
name|List
name|processConstraint
parameter_list|(
name|PersistenceManager
name|pm
parameter_list|,
name|SimpleConstraint
name|constraint
parameter_list|)
block|{
name|Query
name|query
init|=
name|pm
operator|.
name|newQuery
argument_list|(
name|constraint
operator|.
name|getSelectSql
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraint
operator|.
name|getResultClass
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to use a SimpleConstraint with a null result class."
argument_list|)
throw|;
block|}
name|query
operator|.
name|setResultClass
argument_list|(
name|constraint
operator|.
name|getResultClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|constraint
operator|.
name|getFetchLimits
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pm
operator|.
name|getFetchPlan
argument_list|()
operator|.
name|addGroup
argument_list|(
name|constraint
operator|.
name|getFetchLimits
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processParameterizedQuery
argument_list|(
name|query
argument_list|,
name|constraint
operator|.
name|getParameters
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|()
return|;
block|}
specifier|private
name|List
name|processConstraint
parameter_list|(
name|PersistenceManager
name|pm
parameter_list|,
name|Class
name|clazz
parameter_list|,
name|DeclarativeConstraint
name|constraint
parameter_list|)
block|{
name|Extent
name|extent
init|=
name|pm
operator|.
name|getExtent
argument_list|(
name|clazz
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Query
name|query
init|=
name|pm
operator|.
name|newQuery
argument_list|(
name|extent
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraint
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setFilter
argument_list|(
name|constraint
operator|.
name|getFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getVariables
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|declareVariables
argument_list|(
name|StringUtils
operator|.
name|join
argument_list|(
name|constraint
operator|.
name|getVariables
argument_list|()
argument_list|,
literal|";  "
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getSortColumn
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|ordering
init|=
name|constraint
operator|.
name|getSortColumn
argument_list|()
decl_stmt|;
if|if
condition|(
name|constraint
operator|.
name|getSortDirection
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ordering
operator|+=
literal|" "
operator|+
name|constraint
operator|.
name|getSortDirection
argument_list|()
expr_stmt|;
block|}
name|query
operator|.
name|setOrdering
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getFetchLimits
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pm
operator|.
name|getFetchPlan
argument_list|()
operator|.
name|addGroup
argument_list|(
name|constraint
operator|.
name|getFetchLimits
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getWhereCondition
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setFilter
argument_list|(
name|constraint
operator|.
name|getWhereCondition
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getDeclaredImports
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|declareImports
argument_list|(
name|StringUtils
operator|.
name|join
argument_list|(
name|constraint
operator|.
name|getDeclaredImports
argument_list|()
argument_list|,
literal|", "
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getDeclaredParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|constraint
operator|.
name|getParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JDOException
argument_list|(
literal|"Unable to use query, there are declared parameters, "
operator|+
literal|"but no parameter objects to use."
argument_list|)
throw|;
block|}
if|if
condition|(
name|constraint
operator|.
name|getParameters
argument_list|()
operator|.
name|length
operator|!=
name|constraint
operator|.
name|getDeclaredParameters
argument_list|()
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|JDOException
argument_list|(
literal|"Unable to use query, there are<"
operator|+
name|constraint
operator|.
name|getDeclaredParameters
argument_list|()
operator|.
name|length
operator|+
literal|"> declared parameters, yet there are<"
operator|+
name|constraint
operator|.
name|getParameters
argument_list|()
operator|.
name|length
operator|+
literal|"> parameter objects to use.  This should be equal."
argument_list|)
throw|;
block|}
name|query
operator|.
name|declareParameters
argument_list|(
name|StringUtils
operator|.
name|join
argument_list|(
name|constraint
operator|.
name|getDeclaredParameters
argument_list|()
argument_list|,
literal|", "
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|processParameterizedQuery
argument_list|(
name|query
argument_list|,
name|constraint
operator|.
name|getParameters
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|()
return|;
block|}
block|}
specifier|private
name|List
name|processParameterizedQuery
parameter_list|(
name|Query
name|query
parameter_list|,
name|Object
name|parameters
index|[]
parameter_list|)
block|{
switch|switch
condition|(
name|parameters
operator|.
name|length
condition|)
block|{
case|case
literal|1
case|:
return|return
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|(
name|parameters
index|[
literal|0
index|]
argument_list|)
return|;
case|case
literal|2
case|:
return|return
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|(
name|parameters
index|[
literal|0
index|]
argument_list|,
name|parameters
index|[
literal|1
index|]
argument_list|)
return|;
case|case
literal|3
case|:
return|return
operator|(
name|List
operator|)
name|query
operator|.
name|execute
argument_list|(
name|parameters
index|[
literal|0
index|]
argument_list|,
name|parameters
index|[
literal|1
index|]
argument_list|,
name|parameters
index|[
literal|2
index|]
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|JDOException
argument_list|(
literal|"Unable to use more than 3 parameters."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Object
name|getObjectById
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|Object
name|id
parameter_list|,
name|String
name|fetchGroup
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to get object '"
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|"' from jdo using null id."
argument_list|)
throw|;
block|}
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
if|if
condition|(
name|fetchGroup
operator|!=
literal|null
condition|)
block|{
name|pm
operator|.
name|getFetchPlan
argument_list|()
operator|.
name|addGroup
argument_list|(
name|fetchGroup
argument_list|)
expr_stmt|;
block|}
name|Object
name|objectId
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|id
operator|instanceof
name|CompoundKey
condition|)
block|{
name|objectId
operator|=
name|pm
operator|.
name|newObjectIdInstance
argument_list|(
name|clazz
argument_list|,
name|id
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|objectId
operator|=
name|pm
operator|.
name|newObjectIdInstance
argument_list|(
name|clazz
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
name|Object
name|object
init|=
name|pm
operator|.
name|getObjectById
argument_list|(
name|objectId
argument_list|)
decl_stmt|;
name|object
operator|=
name|pm
operator|.
name|detachCopy
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
return|return
name|object
return|;
block|}
catch|catch
parameter_list|(
name|JDOObjectNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to find Database Object ["
operator|+
name|id
operator|+
literal|"] of type "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" using "
operator|+
operator|(
operator|(
name|fetchGroup
operator|==
literal|null
operator|)
condition|?
literal|"no fetch-group"
else|:
literal|"a fetch-group of ["
operator|+
name|fetchGroup
operator|+
literal|"]"
operator|)
argument_list|,
name|e
argument_list|,
name|id
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|JDOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaDatabaseException
argument_list|(
literal|"Error in JDO during get of Database object id ["
operator|+
name|id
operator|+
literal|"] of type "
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|" using "
operator|+
operator|(
operator|(
name|fetchGroup
operator|==
literal|null
operator|)
condition|?
literal|"no fetch-group"
else|:
literal|"a fetch-group of ["
operator|+
name|fetchGroup
operator|+
literal|"]"
operator|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|getObjectById
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|fetchGroup
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to get object '"
operator|+
name|clazz
operator|.
name|getName
argument_list|()
operator|+
literal|"' from jdo using null/empty id."
argument_list|)
throw|;
block|}
return|return
name|getObjectById
argument_list|(
name|clazz
argument_list|,
operator|(
name|Object
operator|)
name|id
argument_list|,
name|fetchGroup
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|objectExists
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
operator|(
name|JDOHelper
operator|.
name|getObjectId
argument_list|(
name|object
argument_list|)
operator|!=
literal|null
operator|)
return|;
block|}
specifier|public
name|boolean
name|objectExistsById
parameter_list|(
name|Class
name|clazz
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
try|try
block|{
name|Object
name|o
init|=
name|getObjectById
argument_list|(
name|clazz
argument_list|,
name|id
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
operator|(
name|o
operator|!=
literal|null
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|void
name|removeObject
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ArchivaDatabaseException
argument_list|(
literal|"Unable to remove null object '"
operator|+
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
name|o
operator|=
name|pm
operator|.
name|getObjectById
argument_list|(
name|pm
operator|.
name|getObjectId
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
name|pm
operator|.
name|deletePersistent
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|rollbackIfActive
parameter_list|(
name|Transaction
name|tx
parameter_list|)
block|{
name|PersistenceManager
name|pm
init|=
name|tx
operator|.
name|getPersistenceManager
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|tx
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|tx
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|closePersistenceManager
argument_list|(
name|pm
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|closePersistenceManager
parameter_list|(
name|PersistenceManager
name|pm
parameter_list|)
block|{
try|try
block|{
name|pm
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JDOUserException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|public
name|void
name|postDelete
parameter_list|(
name|InstanceLifecycleEvent
name|evt
parameter_list|)
block|{
name|PersistenceCapable
name|obj
init|=
operator|(
operator|(
name|PersistenceCapable
operator|)
name|evt
operator|.
name|getSource
argument_list|()
operator|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
comment|// Do not track null objects.
comment|// These events are typically a product of an internal lifecycle event.
return|return;
block|}
block|}
specifier|public
name|void
name|preDelete
parameter_list|(
name|InstanceLifecycleEvent
name|evt
parameter_list|)
block|{
comment|// ignore
block|}
specifier|public
name|void
name|postStore
parameter_list|(
name|InstanceLifecycleEvent
name|evt
parameter_list|)
block|{
comment|// PersistenceCapable obj = ( (PersistenceCapable) evt.getSource() );
block|}
specifier|public
name|void
name|preStore
parameter_list|(
name|InstanceLifecycleEvent
name|evt
parameter_list|)
block|{
comment|// ignore
block|}
specifier|public
name|void
name|removeAll
parameter_list|(
name|Class
name|aClass
parameter_list|)
block|{
name|PersistenceManager
name|pm
init|=
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|Transaction
name|tx
init|=
name|pm
operator|.
name|currentTransaction
argument_list|()
decl_stmt|;
try|try
block|{
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
name|Query
name|query
init|=
name|pm
operator|.
name|newQuery
argument_list|(
name|aClass
argument_list|)
decl_stmt|;
name|query
operator|.
name|deletePersistentAll
argument_list|()
expr_stmt|;
name|tx
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|rollbackIfActive
argument_list|(
name|tx
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|JdoFactory
name|getJdoFactory
parameter_list|()
block|{
return|return
name|jdoFactory
return|;
block|}
block|}
end_class

end_unit

