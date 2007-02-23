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
name|web
operator|.
name|tags
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusConstants
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
name|PlexusContainer
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
name|component
operator|.
name|repository
operator|.
name|exception
operator|.
name|ComponentLookupException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|PageContext
import|;
end_import

begin_comment
comment|/**  * PlexusTagUtil   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|PlexusTagUtil
block|{
specifier|public
specifier|static
name|Object
name|lookup
parameter_list|(
name|PageContext
name|pageContext
parameter_list|,
name|String
name|role
parameter_list|)
throws|throws
name|ComponentLookupException
block|{
return|return
name|getContainer
argument_list|(
name|pageContext
argument_list|)
operator|.
name|lookup
argument_list|(
name|role
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Object
name|lookup
parameter_list|(
name|PageContext
name|pageContext
parameter_list|,
name|String
name|role
parameter_list|,
name|String
name|hint
parameter_list|)
throws|throws
name|ComponentLookupException
block|{
return|return
name|getContainer
argument_list|(
name|pageContext
argument_list|)
operator|.
name|lookup
argument_list|(
name|role
argument_list|,
name|hint
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|PlexusContainer
name|getContainer
parameter_list|(
name|PageContext
name|pageContext
parameter_list|)
throws|throws
name|ComponentLookupException
block|{
name|ServletContext
name|servletContext
init|=
name|pageContext
operator|.
name|getServletContext
argument_list|()
decl_stmt|;
name|PlexusContainer
name|xworkContainer
init|=
operator|(
name|PlexusContainer
operator|)
name|servletContext
operator|.
name|getAttribute
argument_list|(
literal|"webwork.plexus.container"
argument_list|)
decl_stmt|;
if|if
condition|(
name|xworkContainer
operator|!=
literal|null
condition|)
block|{
name|servletContext
operator|.
name|setAttribute
argument_list|(
name|PlexusConstants
operator|.
name|PLEXUS_KEY
argument_list|,
name|xworkContainer
argument_list|)
expr_stmt|;
return|return
name|xworkContainer
return|;
block|}
name|PlexusContainer
name|container
init|=
operator|(
name|PlexusContainer
operator|)
name|servletContext
operator|.
name|getAttribute
argument_list|(
name|PlexusConstants
operator|.
name|PLEXUS_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|container
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ComponentLookupException
argument_list|(
literal|"PlexusContainer is null."
argument_list|)
throw|;
block|}
return|return
name|container
return|;
block|}
block|}
end_class

end_unit

