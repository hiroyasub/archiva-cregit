begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|xml
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
name|dom4j
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|DocumentException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Namespace
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dom4j
operator|.
name|io
operator|.
name|SAXReader
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * XMLReader - a set of common xml utility methods for reading content out of an xml file.  */
end_comment

begin_class
specifier|public
class|class
name|XMLReader
block|{
specifier|private
name|URL
name|xmlUrl
decl_stmt|;
specifier|private
name|String
name|documentType
decl_stmt|;
specifier|private
name|Document
name|document
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|XMLReader
parameter_list|(
name|String
name|type
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|XMLException
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"file does not exist: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"path is not a file: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|canRead
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Cannot read xml file due to permissions: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
try|try
block|{
name|init
argument_list|(
name|type
argument_list|,
name|file
operator|.
name|toURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Unable to translate file "
operator|+
name|file
operator|+
literal|" to URL: "
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
specifier|public
name|XMLReader
parameter_list|(
name|String
name|type
parameter_list|,
name|URL
name|url
parameter_list|)
throws|throws
name|XMLException
block|{
name|init
argument_list|(
name|type
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|String
name|type
parameter_list|,
name|URL
name|url
parameter_list|)
throws|throws
name|XMLException
block|{
name|this
operator|.
name|documentType
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|xmlUrl
operator|=
name|url
expr_stmt|;
name|SAXReader
name|reader
init|=
operator|new
name|SAXReader
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|InputStreamReader
name|inReader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|LatinEntityResolutionReader
name|latinReader
init|=
operator|new
name|LatinEntityResolutionReader
argument_list|(
name|inReader
argument_list|)
decl_stmt|;
name|this
operator|.
name|document
operator|=
name|reader
operator|.
name|read
argument_list|(
name|latinReader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DocumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Unable to parse "
operator|+
name|documentType
operator|+
literal|" xml "
operator|+
name|xmlUrl
operator|+
literal|": "
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Unable to open stream to "
operator|+
name|url
operator|+
literal|": "
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
name|Element
name|root
init|=
name|this
operator|.
name|document
operator|.
name|getRootElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Invalid "
operator|+
name|documentType
operator|+
literal|" xml: root element is null."
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|root
operator|.
name|getName
argument_list|()
argument_list|,
name|documentType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|XMLException
argument_list|(
literal|"Invalid "
operator|+
name|documentType
operator|+
literal|" xml: Unexpected root element<"
operator|+
name|root
operator|.
name|getName
argument_list|()
operator|+
literal|">, expected<"
operator|+
name|documentType
operator|+
literal|">"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getDefaultNamespaceURI
parameter_list|()
block|{
name|Namespace
name|namespace
init|=
name|this
operator|.
name|document
operator|.
name|getRootElement
argument_list|()
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
return|return
name|namespace
operator|.
name|getURI
argument_list|()
return|;
block|}
specifier|public
name|void
name|addNamespaceMapping
parameter_list|(
name|String
name|elementName
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|namespaceMap
operator|.
name|put
argument_list|(
name|elementName
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Element
name|getElement
parameter_list|(
name|String
name|xpathExpr
parameter_list|)
throws|throws
name|XMLException
block|{
name|XPath
name|xpath
init|=
name|createXPath
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
name|Object
name|evaluated
init|=
name|xpath
operator|.
name|selectSingleNode
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|evaluated
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|evaluated
operator|instanceof
name|Element
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|evaluated
return|;
block|}
else|else
block|{
comment|// Unknown evaluated type.
throw|throw
operator|new
name|XMLException
argument_list|(
literal|".getElement( Expr: "
operator|+
name|xpathExpr
operator|+
literal|" ) resulted in non-Element type -> ("
operator|+
name|evaluated
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|") "
operator|+
name|evaluated
argument_list|)
throw|;
block|}
block|}
specifier|private
name|XPath
name|createXPath
parameter_list|(
name|String
name|xpathExpr
parameter_list|)
block|{
name|XPath
name|xpath
init|=
name|document
operator|.
name|createXPath
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|namespaceMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|xpath
operator|.
name|setNamespaceURIs
argument_list|(
name|this
operator|.
name|namespaceMap
argument_list|)
expr_stmt|;
block|}
return|return
name|xpath
return|;
block|}
specifier|public
name|boolean
name|hasElement
parameter_list|(
name|String
name|xpathExpr
parameter_list|)
throws|throws
name|XMLException
block|{
name|XPath
name|xpath
init|=
name|createXPath
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
name|Object
name|evaluated
init|=
name|xpath
operator|.
name|selectSingleNode
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|evaluated
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Remove namespaces from entire document.      */
specifier|public
name|void
name|removeNamespaces
parameter_list|()
block|{
name|removeNamespaces
argument_list|(
name|this
operator|.
name|document
operator|.
name|getRootElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove namespaces from element recursively.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|removeNamespaces
parameter_list|(
name|Element
name|elem
parameter_list|)
block|{
name|elem
operator|.
name|setQName
argument_list|(
name|QName
operator|.
name|get
argument_list|(
name|elem
operator|.
name|getName
argument_list|()
argument_list|,
name|Namespace
operator|.
name|NO_NAMESPACE
argument_list|,
name|elem
operator|.
name|getQualifiedName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|n
decl_stmt|;
name|Iterator
argument_list|<
name|Node
argument_list|>
name|it
init|=
name|elem
operator|.
name|elementIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|n
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|n
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
case|case
name|Node
operator|.
name|ATTRIBUTE_NODE
case|:
operator|(
operator|(
name|Attribute
operator|)
name|n
operator|)
operator|.
name|setNamespace
argument_list|(
name|Namespace
operator|.
name|NO_NAMESPACE
argument_list|)
expr_stmt|;
break|break;
case|case
name|Node
operator|.
name|ELEMENT_NODE
case|:
name|removeNamespaces
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|public
name|String
name|getElementText
parameter_list|(
name|Node
name|context
parameter_list|,
name|String
name|xpathExpr
parameter_list|)
throws|throws
name|XMLException
block|{
name|XPath
name|xpath
init|=
name|createXPath
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
name|Object
name|evaluated
init|=
name|xpath
operator|.
name|selectSingleNode
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|evaluated
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|evaluated
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|evalElem
init|=
operator|(
name|Element
operator|)
name|evaluated
decl_stmt|;
return|return
name|evalElem
operator|.
name|getTextTrim
argument_list|()
return|;
block|}
else|else
block|{
comment|// Unknown evaluated type.
throw|throw
operator|new
name|XMLException
argument_list|(
literal|".getElementText( Node, Expr: "
operator|+
name|xpathExpr
operator|+
literal|" ) resulted in non-Element type -> ("
operator|+
name|evaluated
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|") "
operator|+
name|evaluated
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getElementText
parameter_list|(
name|String
name|xpathExpr
parameter_list|)
throws|throws
name|XMLException
block|{
name|XPath
name|xpath
init|=
name|createXPath
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
name|Object
name|evaluated
init|=
name|xpath
operator|.
name|selectSingleNode
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|evaluated
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|evaluated
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|evalElem
init|=
operator|(
name|Element
operator|)
name|evaluated
decl_stmt|;
return|return
name|evalElem
operator|.
name|getTextTrim
argument_list|()
return|;
block|}
else|else
block|{
comment|// Unknown evaluated type.
throw|throw
operator|new
name|XMLException
argument_list|(
literal|".getElementText( Expr: "
operator|+
name|xpathExpr
operator|+
literal|" ) resulted in non-Element type -> ("
operator|+
name|evaluated
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|") "
operator|+
name|evaluated
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|Element
argument_list|>
name|getElementList
parameter_list|(
name|String
name|xpathExpr
parameter_list|)
throws|throws
name|XMLException
block|{
name|XPath
name|xpath
init|=
name|createXPath
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
name|Object
name|evaluated
init|=
name|xpath
operator|.
name|evaluate
argument_list|(
name|document
argument_list|)
decl_stmt|;
if|if
condition|(
name|evaluated
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|/* The xpath.evaluate(Context) method can return:          *   1) A Collection or List of dom4j Nodes.           *   2) A single dom4j Node.          */
if|if
condition|(
name|evaluated
operator|instanceof
name|List
condition|)
block|{
return|return
operator|(
name|List
argument_list|<
name|Element
argument_list|>
operator|)
name|evaluated
return|;
block|}
if|else if
condition|(
name|evaluated
operator|instanceof
name|Node
condition|)
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
operator|(
name|Element
operator|)
name|evaluated
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
else|else
block|{
comment|// Unknown evaluated type.
throw|throw
operator|new
name|XMLException
argument_list|(
literal|".getElementList( Expr: "
operator|+
name|xpathExpr
operator|+
literal|" ) resulted in non-List type -> ("
operator|+
name|evaluated
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|") "
operator|+
name|evaluated
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getElementListText
parameter_list|(
name|String
name|xpathExpr
parameter_list|)
throws|throws
name|XMLException
block|{
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|getElementList
argument_list|(
name|xpathExpr
argument_list|)
decl_stmt|;
if|if
condition|(
name|elemList
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Element
argument_list|>
name|iter
init|=
name|elemList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Element
name|listelem
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|listelem
operator|.
name|getTextTrim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

