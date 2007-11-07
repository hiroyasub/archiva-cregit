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
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringEscapeUtils
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
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|web
operator|.
name|util
operator|.
name|ContextUtils
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
name|javax
operator|.
name|servlet
operator|.
name|jsp
operator|.
name|JspException
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
name|JspWriter
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * CopyPasteSnippet  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.web.tags.CopyPasteSnippet"  */
end_comment

begin_class
specifier|public
class|class
name|CopyPasteSnippet
extends|extends
name|AbstractLogEnabled
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PRE
init|=
literal|"pre"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOGGLE
init|=
literal|"toggle"
decl_stmt|;
specifier|public
name|void
name|write
parameter_list|(
name|String
name|wrapper
parameter_list|,
name|Object
name|o
parameter_list|,
name|PageContext
name|pageContext
parameter_list|)
throws|throws
name|JspException
block|{
name|StringBuffer
name|prefix
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|StringBuffer
name|suffix
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"Error generating snippet."
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unable to generate snippet for null object."
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|o
operator|instanceof
name|ManagedRepositoryConfiguration
condition|)
block|{
name|ManagedRepositoryConfiguration
name|repo
init|=
operator|(
name|ManagedRepositoryConfiguration
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|TOGGLE
operator|.
name|equals
argument_list|(
name|wrapper
argument_list|)
condition|)
block|{
name|prefix
operator|.
name|append
argument_list|(
literal|"<a href=\"#\" onclick=\"Effect.toggle('repoPom"
argument_list|)
expr_stmt|;
name|prefix
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"','slide'); return false;\">Show POM Snippet</a><br/>"
argument_list|)
expr_stmt|;
name|prefix
operator|.
name|append
argument_list|(
literal|"<pre class=\"pom\" style=\"display: none;\" id=\"repoPom"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|prefix
operator|.
name|append
argument_list|(
literal|"\"><code>"
argument_list|)
expr_stmt|;
name|suffix
operator|.
name|append
argument_list|(
literal|"</code></pre>"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|PRE
operator|.
name|equals
argument_list|(
name|wrapper
argument_list|)
condition|)
block|{
name|prefix
operator|.
name|append
argument_list|(
literal|"<pre>"
argument_list|)
expr_stmt|;
name|suffix
operator|.
name|append
argument_list|(
literal|"</pre>"
argument_list|)
expr_stmt|;
block|}
name|createSnippet
argument_list|(
name|buf
argument_list|,
name|repo
argument_list|,
name|pageContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"Unable to generate snippet for object "
argument_list|)
operator|.
name|append
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|JspWriter
name|out
init|=
name|pageContext
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|prefix
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|StringEscapeUtils
operator|.
name|escapeXml
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|suffix
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
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
name|JspException
argument_list|(
literal|"Unable to write snippet to output: "
operator|+
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
specifier|private
name|void
name|createSnippet
parameter_list|(
name|StringBuffer
name|snippet
parameter_list|,
name|ManagedRepositoryConfiguration
name|repo
parameter_list|,
name|PageContext
name|pageContext
parameter_list|)
block|{
name|snippet
operator|.
name|append
argument_list|(
literal|"<project>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"  ...\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<distributionManagement>\n"
argument_list|)
expr_stmt|;
name|String
name|distRepoName
init|=
literal|"repository"
decl_stmt|;
if|if
condition|(
name|repo
operator|.
name|isSnapshots
argument_list|()
condition|)
block|{
name|distRepoName
operator|=
literal|"snapshotRepository"
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|distRepoName
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<id>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</id>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<url>dav:"
argument_list|)
operator|.
name|append
argument_list|(
name|ContextUtils
operator|.
name|getBaseURL
argument_list|(
name|pageContext
argument_list|,
literal|"repository"
argument_list|)
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
literal|"</url>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
condition|)
block|{
name|snippet
operator|.
name|append
argument_list|(
literal|"<layout>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</layout>"
argument_list|)
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"</"
argument_list|)
operator|.
name|append
argument_list|(
name|distRepoName
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</distributionManagement>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<repositories>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<repository>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<id>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</id>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<name>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</name>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<url>"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
name|ContextUtils
operator|.
name|getBaseURL
argument_list|(
name|pageContext
argument_list|,
literal|"repository"
argument_list|)
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</url>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
condition|)
block|{
name|snippet
operator|.
name|append
argument_list|(
literal|"<layout>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</layout>\n"
argument_list|)
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"<releases>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<enabled>"
argument_list|)
operator|.
name|append
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|repo
operator|.
name|isReleases
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"</enabled>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</releases>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<snapshots>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<enabled>"
argument_list|)
operator|.
name|append
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|repo
operator|.
name|isSnapshots
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"</enabled>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</snapshots>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</repository>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</repositories>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"  ...\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</project>\n"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

