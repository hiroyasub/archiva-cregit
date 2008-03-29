begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* ========================================================================== *  *         Copyright (C) 2004-2006, Pier Fumagalli<http://could.it/>         *  *                            All rights reserved.                            *  * ========================================================================== *  *                                                                            *  * Licensed under the  Apache License, Version 2.0  (the "License").  You may *  * not use this file except in compliance with the License.  You may obtain a *  * copy of the License at<http://www.apache.org/licenses/LICENSE-2.0>.       *  *                                                                            *  * Unless  required  by applicable  law or  agreed  to  in writing,  software *  * distributed under the License is distributed on an  "AS IS" BASIS, WITHOUT *  * WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied.  See the *  * License for the  specific language  governing permissions  and limitations *  * under the License.                                                         *  *                                                                            *  * ========================================================================== */
end_comment

begin_package
package|package
name|org
operator|.
name|betaversion
operator|.
name|webdav
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
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
name|ServletException
import|;
end_import

begin_comment
comment|/**  *<p>The {@link DAVServlet} class has been moved to a new package and should  * now be referred as {@link it.could.webdav.DAVServlet}.</p>  *   *<p>This class will be preserved for some time (not so long) to give people  * time to update their servlet deployment descriptors.</p>  *   * @author<a href="http://could.it/">Pier Fumagalli</a>  * @deprecated This class has been moved into the<code>it.could.webdav</code>  *             package. Reconfigure your<code>web.xml</code> deployment  *             descriptor to use {@link it.could.webdav.DAVServlet}.  */
end_comment

begin_class
specifier|public
class|class
name|DAVServlet
extends|extends
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVServlet
block|{
comment|/**      *<p>Create a new {@link DAVServlet} instance.</p>      */
specifier|public
name|DAVServlet
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      *<p>Initialize this {@link DAVServlet} instance reporting to the      * {@link ServletContext} log that this class is deprecated.</p>      */
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
specifier|final
name|ServletContext
name|context
init|=
name|config
operator|.
name|getServletContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|log
argument_list|(
literal|"The class \""
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\" is deprecated"
argument_list|)
expr_stmt|;
name|context
operator|.
name|log
argument_list|(
literal|"Modify the \"web.xml\" deployment descriptor to use \""
operator|+
name|it
operator|.
name|could
operator|.
name|webdav
operator|.
name|DAVServlet
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|super
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

