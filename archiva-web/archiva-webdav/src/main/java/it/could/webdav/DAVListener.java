begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* ========================================================================== *  *         Copyright (C) 2004-2006, Pier Fumagalli<http://could.it/>         *  *                            All rights reserved.                            *  * ========================================================================== *  *                                                                            *  * Licensed under the  Apache License, Version 2.0  (the "License").  You may *  * not use this file except in compliance with the License.  You may obtain a *  * copy of the License at<http://www.apache.org/licenses/LICENSE-2.0>.       *  *                                                                            *  * Unless  required  by applicable  law or  agreed  to  in writing,  software *  * distributed under the License is distributed on an  "AS IS" BASIS, WITHOUT *  * WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied.  See the *  * License for the  specific language  governing permissions  and limitations *  * under the License.                                                         *  *                                                                            *  * ========================================================================== */
end_comment

begin_package
package|package
name|it
operator|.
name|could
operator|.
name|webdav
package|;
end_package

begin_comment
comment|/**  *<p>A simple interface identifying a {@link DAVRepository} event listener.</p>   *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|DAVListener
block|{
comment|/**<p>An event representing the creation of a collection.</p> */
specifier|public
specifier|static
specifier|final
name|int
name|COLLECTION_CREATED
init|=
literal|1
decl_stmt|;
comment|/**<p>An event representing the deletion of a collection.</p> */
specifier|public
specifier|static
specifier|final
name|int
name|COLLECTION_REMOVED
init|=
literal|2
decl_stmt|;
comment|/**<p>An event representing the creation of a resource.</p> */
specifier|public
specifier|static
specifier|final
name|int
name|RESOURCE_CREATED
init|=
literal|3
decl_stmt|;
comment|/**<p>An event representing the deletion of a resource.</p> */
specifier|public
specifier|static
specifier|final
name|int
name|RESOURCE_REMOVED
init|=
literal|4
decl_stmt|;
comment|/**<p>An event representing the modification of a resource.</p> */
specifier|public
specifier|static
specifier|final
name|int
name|RESOURCE_MODIFIED
init|=
literal|5
decl_stmt|;
comment|/**      *<p>Notify this {@link DAVListener} of an action occurred on a      * specified {@link DAVResource}.</p>      *       * @param resource the {@link DAVResource} associated with the notification.      * @param event a number identifying the type of the notification.      */
specifier|public
name|void
name|notify
parameter_list|(
name|DAVResource
name|resource
parameter_list|,
name|int
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

