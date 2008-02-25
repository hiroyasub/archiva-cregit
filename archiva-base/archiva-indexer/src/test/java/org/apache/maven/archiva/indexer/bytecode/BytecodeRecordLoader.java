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
name|bytecode
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|ArchivaArtifact
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
name|ArchivaArtifactJavaDetails
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
name|platform
operator|.
name|JavaArtifactHelper
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
name|util
operator|.
name|IOUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|FileReader
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_comment
comment|/**  * BytecodeRecordLoader - Utility method for loading dump files into BytecordRecords.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|BytecodeRecordLoader
block|{
comment|//    private static Map cache = new HashMap();
specifier|public
specifier|static
name|BytecodeRecord
name|loadRecord
parameter_list|(
name|File
name|dumpFile
parameter_list|,
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
name|BytecodeRecord
name|record
decl_stmt|;
comment|//        record = (BytecodeRecord) cache.get( artifact );
comment|//        if ( record != null )
comment|//        {
comment|//            return record;
comment|//        }
name|record
operator|=
operator|new
name|BytecodeRecord
argument_list|()
expr_stmt|;
name|record
operator|.
name|setArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|record
operator|.
name|setClasses
argument_list|(
operator|new
name|ArrayList
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setMethods
argument_list|(
operator|new
name|ArrayList
argument_list|()
argument_list|)
expr_stmt|;
name|record
operator|.
name|setFiles
argument_list|(
operator|new
name|ArrayList
argument_list|()
argument_list|)
expr_stmt|;
name|FileReader
name|freader
init|=
literal|null
decl_stmt|;
name|BufferedReader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|freader
operator|=
operator|new
name|FileReader
argument_list|(
name|dumpFile
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
name|freader
argument_list|)
expr_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"FILENAME|"
argument_list|)
condition|)
block|{
name|String
name|filename
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"FILENAME|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|setFilename
argument_list|(
name|filename
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"SIZE|"
argument_list|)
condition|)
block|{
name|String
name|size
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"SIZE|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|getArtifact
argument_list|()
operator|.
name|getModel
argument_list|()
operator|.
name|setSize
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|size
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"HASH_MD5|"
argument_list|)
condition|)
block|{
name|String
name|md5
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"HASH_MD5|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|getArtifact
argument_list|()
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumMD5
argument_list|(
name|md5
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"HASH_SHA1|"
argument_list|)
condition|)
block|{
name|String
name|sha1
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"HASH_SHA1|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|getArtifact
argument_list|()
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|sha1
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"HASH_BYTECODE|"
argument_list|)
condition|)
block|{
name|String
name|hash
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"HASH_BYTECODE|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|ArchivaArtifactJavaDetails
name|javaDetails
init|=
name|JavaArtifactHelper
operator|.
name|getJavaDetails
argument_list|(
name|record
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|javaDetails
operator|.
name|setChecksumBytecode
argument_list|(
name|hash
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"JDK|"
argument_list|)
condition|)
block|{
name|String
name|jdk
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"JDK|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|ArchivaArtifactJavaDetails
name|javaDetails
init|=
name|JavaArtifactHelper
operator|.
name|getJavaDetails
argument_list|(
name|record
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|javaDetails
operator|.
name|setJdk
argument_list|(
name|jdk
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"CLASS|"
argument_list|)
condition|)
block|{
name|String
name|classname
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"CLASS|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|getClasses
argument_list|()
operator|.
name|add
argument_list|(
name|classname
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"METHOD|"
argument_list|)
condition|)
block|{
name|String
name|methodName
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"METHOD|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|getMethods
argument_list|()
operator|.
name|add
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"FILE|"
argument_list|)
condition|)
block|{
name|String
name|fileentry
init|=
name|line
operator|.
name|substring
argument_list|(
literal|"FILE|"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|record
operator|.
name|getFiles
argument_list|()
operator|.
name|add
argument_list|(
name|fileentry
argument_list|)
expr_stmt|;
block|}
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
literal|"Unable to load record "
operator|+
name|dumpFile
operator|+
literal|" from disk: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|IOUtil
operator|.
name|close
argument_list|(
name|freader
argument_list|)
expr_stmt|;
block|}
comment|//        cache.put( artifact, record );
return|return
name|record
return|;
block|}
block|}
end_class

end_unit

