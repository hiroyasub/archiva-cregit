/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

require(["jquery","jquery.tmpl","i18n"], function(jquery,jqueryTmpl,i18n) {

  loadi18n=function(loadCallback){
    $.log("loadi18n");
    var browserLang = usedLang();
    $.log("use browserLang:"+browserLang);

    var options = {
      cache:false,
      mode: 'map',
      encoding:'utf-8',
      callback: loadCallback
    };
    loadAndParseFile("restServices/archivaServices/commonServices/getAllI18nResources?locale="+browserLang,options );
  }

  /**
   * log message in the console
   */
  $.log = (function(message) {
    if ( !window.archivaJavascriptLog ){
      return;
    }
    Sammy.log(message);
    /*return;
    if (typeof window.console != 'undefined' && typeof window.console.log != 'undefined') {
      console.log(message);
    } else {
      // do nothing no console
    }*/
  });

  /**
   * return value of a param in the url
   * @param name
   */
  $.urlParam = function(name){
      var results = new RegExp('[\\?&]' + name + '=([^&#]*)').exec(window.location.href);
      if (results) {
        return results[1] || 0;
      }
      return null;
  }

  usedLang=function(){
    var browserLang = $.i18n.browserLang();
    var requestLang = $.urlParam('request_lang');
    if (requestLang) {
      browserLang=requestLang;
    }
    return browserLang;
  }

  /**
   * display a success message
   * @param text the success text
   * @param idToAppend the id to append the success box
   */
  displaySuccessMessage=function(text,idToAppend){
    var textId = idToAppend ? $("#"+idToAppend) : $("#user-messages");
    $.tmpl($("#alert-message-success").html(), { "message" : text }).appendTo( textId );
    $(textId).focus();
  }

  /**
   * display an error message
   * @param text the success text
   * @param idToAppend the id to append the success box
   */
  displayErrorMessage=function(text,idToAppend){
    var textId = idToAppend ? $("#"+idToAppend) : $("#user-messages");
    $.tmpl($("#alert-message-error").html(), { "message" : text }).appendTo( textId );
    $(textId).focus();
  }

  /**
   * display a warning message
   * @param text the success text
   * @param idToAppend the id to append the success box
   */
  displayWarningMessage=function(text,idToAppend){
    var textId = idToAppend ? $("#"+idToAppend) : $("#user-messages");
    $.tmpl($("#alert-message-warning").html(), { "message" : text }).appendTo( textId );
    $(textId).focus();
  }

  displayInfoMessage=function(text,idToAppend){
    var textId = idToAppend ? $("#"+idToAppend) : $("#user-messages");
    $.tmpl($("#alert-message-info").html(), { "message" : text }).appendTo( textId );
    $(textId).focus();
  }

  getUrlHash=function(){
    var matches = window.location.toString().match(/^[^#]*(#.+)$/);
    return matches ? matches[1] : null;
  }

  /**
   * clear #main-content and call clearUserMessages
    */
  screenChange=function(){
    var mainContent=$("#main-content");
    mainContent.html("");
    mainContent.removeAttr("data-bind");
    $("#body_content" ).find(".popover" ).hide();
    clearUserMessages();
  }

  /**
   * clear content of id if none clear content of #user-messages
    * @param idToAppend
   */
  clearUserMessages=function(idToAppend){
    var textId = idToAppend ? $("#"+idToAppend) : $("#user-messages");
    $(textId).html('');
  }

  /**
   * clear all input text and password found in the the selector
   * @param selectorStr
   */
  clearForm=function(selectorStr){
    $(selectorStr).find("input[type='text']").each(function(ele){
      $(this).val("");
    });
    $(selectorStr).find("input[type='password']").each(function(ele){
      $(this).val("");
    });

  }

  /**
   * open a confirm dialog based on bootstrap modal
   * @param okFn callback function to call on ok confirm
   * @param okMessage
   * @param cancelMessage
   * @param title
   */
  openDialogConfirm=function(okFn, okMessage, cancelMessage, title,bodyText){
    var dialogCancel=$("#dialog-confirm-modal-cancel");
    if (window.modalConfirmDialog==null) {
      window.modalConfirmDialog = $("#dialog-confirm-modal").modal();//{backdrop:'static',show:false}
      window.modalConfirmDialog.bind('hidden', function () {
        $("#dialog-confirm-modal-header-title").html("");
        $("#dialog-confirm-modal-body-text").html("");
      })
      dialogCancel.on("click", function(){
        window.modalConfirmDialog.modal('hide');
      });
    }
    $("#dialog-confirm-modal-header-title").html(title);
    $("#dialog-confirm-modal-body-text").html(bodyText);
    var dialogConfirmModalOk=$("#dialog-confirm-modal-ok");
    if (okMessage){
      dialogConfirmModalOk.html(okMessage);
    }
    if (cancelMessage){
      dialogCancel.html(cancelMessage);
    }
    window.modalConfirmDialog.modal('show');

    // unbind previous events !!

    dialogConfirmModalOk.off( );
    dialogConfirmModalOk.on("click", okFn);

  }

  /**
   * return a small spinner html img element
   */
  smallSpinnerImg=function(){
    return "<img id=\"small-spinner\" src=\"images/small-spinner.gif\"/>";
  };

  removeSmallSpinnerImg=function(){
    $("#small-spinner").remove();
  }

  mediumSpinnerImg=function(){
    return "<img id=\"medium-spinner\" src=\"images/medium-spinner.gif\"/>";
  };

  removeMediumSpinnerImg=function(){
    $("#medium-spinner").remove();
  }

  removeMediumSpinnerImg=function(selector){
    $(selector+" #medium-spinner").remove();
  }

  closeDialogConfirm=function(){
    window.modalConfirmDialog.modal('hide');
  }

  closeDialogConfirmui=function(){
    $("#dialog-confirm" ).dialog("close");
  }

  /**
   * open a confirm dialog with jqueryui
   * @param okFn callback function to call on ok confirm
   * @param okMessage
   * @param cancelMessage
   * @param title
   */
  openDialogConfirmui=function(okFn, okMessage, cancelMessage, title){
    $("#dialog-confirm" ).dialog({
      resizable: false,
      title: title,
      modal: true,
      show: 'slide',
      buttons: [{
        text: okMessage,
        click: okFn},
        {
        text: cancelMessage,
        click:function() {
          $(this).dialog( "close" );
        }
      }]
    });
  }

  mapStringArray=function(data){
    if (data) {
      if ($.isArray(data)){
        return $.map(data,function(item){
          return item;
        });
      } else {
        return new Array(data);
      }
    }
    return null;
  }

  /**
   * display redback error from redback json error response
   * {"redbackRestError":{"errorMessages":{"args":1,"errorKey":"user.password.violation.numeric"}}}
   * @param obj
   * @param idToAppend
   */
  displayRedbackError=function(obj,idToAppend) {
    if ($.isArray(obj.errorMessages)) {
      $.log("displayRedbackError with array");
      for(var i=0; i<obj.errorMessages.length; i++ ) {
        if(obj.errorMessages[i].errorKey) {
          $.log("displayRedbackError with array loop");
          displayErrorMessage($.i18n.prop( obj.errorMessages[i].errorKey, obj.errorMessages[i].args ),idToAppend);
        }
      }
    } else {
      $.log("displayRedbackError no array");
      displayErrorMessage($.i18n.prop( obj.errorMessages.errorKey, obj.errorMessages.args ),idToAppend);
    }
  }

  /*
   * generic function to display error return by rest service
   * if fieldName is here the function will try to find a field with this name and add a span on it
   * if not error is displayed in #user-messages div
   */
  displayRestError=function(data,idToAppend){

    if (data.redbackRestError){
      displayRedbackError(archivaRestError,idToAppend)
    }
    // if we have the fieldName display error on it
    if (data && data.fieldName){
      var mainContent=$("#main-content");

      if (mainContent.find("#"+data.fieldName)){
        var message=null;
        if (data.errorKey) {
          message=$.i18n.prop('data.errorKey');
        } else {
          message=data.errorMessage;
        }
        mainContent.find("div.clearfix" ).removeClass( "error" );
        mainContent.find("span.help-inline" ).remove();
        mainContent.find("#"+data.fieldName).parents( "div.clearfix" ).addClass( "error" );
        mainContent.find("#"+data.fieldName).parent().append( "<span class=\"help-inline\">" + message + "</span>" );
        return;
      }
      // we don't have any id with this fieldName so continue
    }

    if (data.errorKey && data.errorKey.length>0){
      displayErrorMessage($.i18n.prop( data.errorKey ),idToAppend);
    } else {
      $.log("data.errorMessage:"+data.errorMessage);
      displayErrorMessage(data.errorMessage,idToAppend);
    }

  }

  /**
   * used by validation error to customize error display in the ui
   * @param selector
   * @param validator
   * @param errorMap
   * @param errorList
   */
  customShowError=function(selector, validator, errorMap, errorList) {
    removeValidationErrorMessages(selector);
    for ( var i = 0; errorList[i]; i++ ) {
      var error = errorList[i];
      var field = $(selector).find("#"+error.element.id);
      field.parents( "div.control-group" ).addClass( "error" );
      field.parent().append( "<span class=\"help-inline\">" + error.message + "</span>" );
    }
  }

  removeValidationErrorMessages=function(selector){
    $(selector).find("div.control-group" ).removeClass( "error" );
    $(selector).find("span.help-inline").remove();
  }

  appendArchivaVersion=function(){
    return "_archivaVersion="+window.archivaRuntimeInfo.version;
  }

  buildLoadJsUrl=function(srcScript){
    return srcScript+"?"+appendArchivaVersion()+"&_"+jQuery.now();
  }

  timestampNoCache=function(){
    if (!window.archivaDevMode){
      return "";
    }
    return "&_="+jQuery.now();
  }


  /**
   * mapping for a java Map entry
   * @param key
   * @param value
   */
  Entry=function(key,value){
    var self=this;
    this.key=ko.observable(key);
    this.value=ko.observable(value);
  }

  /**
   * map {"strings":["snapshots","internal"]} to an array
   * @param data
   */
  mapStringList=function(data){
    if (data && data.strings){
    return $.isArray(data.strings) ?
        $.map(data.strings,function(item){return item}): [data.strings];
    }
    return [];
  }

  /**
   * return an array with removing duplicate strings
   * @param strArray an array of string
   * @param sorted to sort or not
   */
  unifyArray=function(strArray,sorted){
    var res = [];
    $(strArray).each(function(idx,str){
      if ( $.inArray(str,res)<0){
        res.push(str);
      }
    });
    return sorted?res.sort():res;
  }

  goToAnchor=function(anchor){
    var curHref = window.location.href;
    curHref=curHref.substringBeforeLast("#");
    window.location.href=curHref+"#"+anchor;
  }

  //------------------------------------
  // utils javascript string extensions
  //------------------------------------

  String.prototype.endsWith = function(str) {
    return (this.match(str+"$")==str)
  }

  String.prototype.startsWith = function(str) {
    return (this.match("^"+str)==str)
  }

  String.prototype.substringBeforeLast = function(str) {
    return this.substring(0,this.lastIndexOf(str));
  }

  String.prototype.substringBeforeFirst = function(str) {
    var idx = this.indexOf(str);
    if(idx<0){
      return this;
    }
    return this.substring(0,idx);
  }

  String.prototype.substringAfterLast = function(str) {
    return this.substring(this.lastIndexOf(str)+1);
  }
  /**
   *
   * @param str
   * @return {String} if str not found return empty string
   */
  String.prototype.substringAfterFirst = function(str) {
    var idx = this.indexOf(str);
    if (idx<0){
      return "";
    }
    return this.substring(idx);
  }

  escapeDot=function(str){
    return str.replace(/\./g,"\\\.");
  }

  activatePopoverDoc=function(){
    var mainContent=$("#main-content");
    mainContent.find(".popover-doc" ).popover({html: true, trigger: 'manual'});
    mainContent.find(".popover-doc" ).on("click",function(){
      $(this).popover("show");
    });

    mainContent.find(".popover-doc" ).mouseover(function(){
      $(this).popover("destroy");
    });

    mainContent.find(".tooltip-doc" ).tooltip({html: true, trigger: 'hover'});
  }

  //-----------------------------------------
  // extends jquery tmpl to support var def
  //-----------------------------------------

  $(function() {
    $.extend($.tmpl.tag, {
        "var": {
            open: "var $1;"
        }
    });
  });

});