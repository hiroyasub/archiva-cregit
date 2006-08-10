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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
comment|/**  * Gradually create a digest for a stream.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|StreamingDigester
block|{
name|String
name|ROLE
init|=
name|StreamingDigester
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
comment|/**      * Reset the hashcode calculation algorithm.      * Only useful when performing incremental hashcodes based on repeated use of {@link #update(InputStream)}      *      * @throws DigesterException if there was a problem with the internal message digest      */
name|void
name|reset
parameter_list|()
throws|throws
name|DigesterException
function_decl|;
comment|/**      * Calculate the current checksum.      *      * @return the current checksum.      * @throws DigesterException if there was a problem computing the hashcode.      */
name|String
name|calc
parameter_list|()
throws|throws
name|DigesterException
function_decl|;
comment|/**      * Update the checksum with the content of the input stream.      *      * @param is the input stream      * @throws DigesterException if there was a problem computing the hashcode.      */
name|void
name|update
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|DigesterException
function_decl|;
block|}
end_interface

end_unit

