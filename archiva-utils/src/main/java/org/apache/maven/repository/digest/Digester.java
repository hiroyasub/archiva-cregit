begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|digest
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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

begin_comment
comment|/**  * Create a digest for a file.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Digester
block|{
name|String
name|ROLE
init|=
name|Digester
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Get the algorithm used for the checksum.      *      * @return the algorithm      */
name|String
name|getAlgorithm
parameter_list|()
function_decl|;
comment|/**      * Calculate a checksum for a file.      *      * @param file the file to calculate the checksum for      * @return the current checksum.      * @throws DigesterException if there was a problem computing the hashcode.      */
name|String
name|calc
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|DigesterException
function_decl|;
comment|/**      * Verify that a checksum is correct.      *      * @param file     the file to compute the checksum for      * @param checksum the checksum to compare to      * @throws DigesterException if there was a problem computing the hashcode.      */
name|void
name|verify
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|checksum
parameter_list|)
throws|throws
name|DigesterException
function_decl|;
block|}
end_interface

end_unit

