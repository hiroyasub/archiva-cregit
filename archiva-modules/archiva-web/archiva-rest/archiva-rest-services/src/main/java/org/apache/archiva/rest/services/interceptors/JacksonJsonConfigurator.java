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
operator|.
name|interceptors
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|DeserializationFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|dataformat
operator|.
name|xml
operator|.
name|XmlMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|datatype
operator|.
name|jsr310
operator|.
name|JavaTimeModule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|module
operator|.
name|jaxb
operator|.
name|JaxbAnnotationIntrospector
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_comment
comment|/**  * class to setup Jackson Json configuration  *  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaJacksonJsonConfigurator"
argument_list|)
specifier|public
class|class
name|JacksonJsonConfigurator
block|{
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
specifier|public
name|JacksonJsonConfigurator
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"redbackJacksonJsonMapper"
argument_list|)
name|ObjectMapper
name|objectMapper
parameter_list|,
annotation|@
name|Named
argument_list|(
literal|"redbackJacksonXMLMapper"
argument_list|)
name|XmlMapper
name|xmlMapper
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"configure jackson ObjectMapper"
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|disable
argument_list|(
name|DeserializationFeature
operator|.
name|FAIL_ON_UNKNOWN_PROPERTIES
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|setAnnotationIntrospector
argument_list|(
operator|new
name|JaxbAnnotationIntrospector
argument_list|(
name|objectMapper
operator|.
name|getTypeFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModule
argument_list|(
operator|new
name|JavaTimeModule
argument_list|( )
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|setDateFormat
argument_list|(
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
argument_list|)
argument_list|)
expr_stmt|;
name|xmlMapper
operator|.
name|disable
argument_list|(
name|DeserializationFeature
operator|.
name|FAIL_ON_UNKNOWN_PROPERTIES
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

