begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|CommonServices
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
name|io
operator|.
name|IOUtils
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
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|scheduler
operator|.
name|CronExpressionValidator
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RedbackServiceException
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|UtilServices
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"commonServices#rest"
argument_list|)
specifier|public
class|class
name|DefaultCommonServices
implements|implements
name|CommonServices
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_NAME
init|=
literal|"org/apache/archiva/i18n/default"
decl_stmt|;
specifier|private
name|Logger
name|log
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
name|Inject
specifier|private
name|UtilServices
name|utilServices
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cachei18n
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|CronExpressionValidator
name|cronExpressionValidator
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
comment|// preload i18n en and fr
name|getAllI18nResources
argument_list|(
literal|"en"
argument_list|)
expr_stmt|;
name|getAllI18nResources
argument_list|(
literal|"fr"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getI18nResources
parameter_list|(
name|String
name|locale
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|StringBuilder
name|resourceName
init|=
operator|new
name|StringBuilder
argument_list|(
name|RESOURCE_NAME
argument_list|)
decl_stmt|;
try|try
block|{
name|loadResource
argument_list|(
name|properties
argument_list|,
name|resourceName
argument_list|,
name|locale
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip error loading properties {}"
argument_list|,
name|resourceName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|fromProperties
argument_list|(
name|properties
argument_list|)
return|;
block|}
specifier|private
name|void
name|loadResource
parameter_list|(
name|Properties
name|properties
parameter_list|,
name|StringBuilder
name|resourceName
parameter_list|,
name|String
name|locale
parameter_list|)
throws|throws
name|IOException
block|{
comment|// load default
name|loadResource
argument_list|(
name|properties
argument_list|,
operator|new
name|StringBuilder
argument_list|(
name|resourceName
argument_list|)
operator|.
name|append
argument_list|(
literal|".properties"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|locale
argument_list|)
expr_stmt|;
comment|// if locale override with locale content
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|locale
argument_list|)
condition|)
block|{
name|loadResource
argument_list|(
name|properties
argument_list|,
operator|new
name|StringBuilder
argument_list|(
name|resourceName
argument_list|)
operator|.
name|append
argument_list|(
literal|"_"
operator|+
name|locale
argument_list|)
operator|.
name|append
argument_list|(
literal|".properties"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|locale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|fromProperties
parameter_list|(
specifier|final
name|Properties
name|properties
parameter_list|)
block|{
name|StringBuilder
name|output
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|output
operator|.
name|append
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
return|return
name|output
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|loadResource
parameter_list|(
specifier|final
name|Properties
name|finalProperties
parameter_list|,
name|String
name|resourceName
parameter_list|,
name|String
name|locale
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
block|{
name|is
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resourceName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|finalProperties
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|locale
argument_list|,
literal|"en"
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"cannot load resource {}"
argument_list|,
name|resourceName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getAllI18nResources
parameter_list|(
name|String
name|locale
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|String
name|cachedi18n
init|=
name|cachei18n
operator|.
name|get
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|locale
argument_list|)
condition|?
literal|"en"
else|:
name|StringUtils
operator|.
name|lowerCase
argument_list|(
name|locale
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedi18n
operator|!=
literal|null
condition|)
block|{
return|return
name|cachedi18n
return|;
block|}
try|try
block|{
name|Properties
name|all
init|=
name|utilServices
operator|.
name|getI18nProperties
argument_list|(
name|locale
argument_list|)
decl_stmt|;
name|StringBuilder
name|resourceName
init|=
operator|new
name|StringBuilder
argument_list|(
name|RESOURCE_NAME
argument_list|)
decl_stmt|;
name|loadResource
argument_list|(
name|all
argument_list|,
name|resourceName
argument_list|,
name|locale
argument_list|)
expr_stmt|;
name|String
name|i18n
init|=
name|fromProperties
argument_list|(
name|all
argument_list|)
decl_stmt|;
name|cachei18n
operator|.
name|put
argument_list|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|locale
argument_list|)
condition|?
literal|"en"
else|:
name|StringUtils
operator|.
name|lowerCase
argument_list|(
name|locale
argument_list|)
argument_list|,
name|i18n
argument_list|)
expr_stmt|;
return|return
name|i18n
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RedbackServiceException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getHttpErrorCode
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|loadFromString
parameter_list|(
name|String
name|propsStr
parameter_list|,
name|Properties
name|properties
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|InputStream
name|inputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|inputStream
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|propsStr
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Boolean
name|validateCronExpression
parameter_list|(
name|String
name|cronExpression
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
return|return
name|cronExpressionValidator
operator|.
name|validate
argument_list|(
name|cronExpression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

