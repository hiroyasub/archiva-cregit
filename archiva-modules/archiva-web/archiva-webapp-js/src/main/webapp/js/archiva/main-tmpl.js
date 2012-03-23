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
require(["text!js/archiva/templates/menu.html"+appendTemplateUrl(),
          "text!js/archiva/templates/topbar.html"+appendTemplateUrl(),
          "text!js/archiva/templates/message.html"+appendTemplateUrl(),
          "text!js/archiva/templates/modal.html"+appendTemplateUrl(),
          "text!js/archiva/templates/grids-generics.html"+appendTemplateUrl(),
          "text!js/archiva/templates/repositories.html"+appendTemplateUrl(),
          "text!js/archiva/templates/network-proxies.html"+appendTemplateUrl(),
          "text!js/archiva/templates/proxy-connectors.html"+appendTemplateUrl(),
          "text!js/archiva/templates/repository-groups.html"+appendTemplateUrl(),
          "text!js/archiva/templates/search.html"+appendTemplateUrl(),
          "text!js/archiva/templates/general-admin.html"+appendTemplateUrl()],
  function(menu,topbar,message,modal,grids_generics,repositories,network_proxies,proxies_connectors,
           repository_groups,search,general_admin) {

    var htmlFragment=$("#html-fragments");
    // template loading
    $.tmpl( menu ).appendTo(htmlFragment);
    $.tmpl( topbar ).appendTo(htmlFragment);
    htmlFragment.append(message);
    $.tmpl( modal ).appendTo(htmlFragment);
    htmlFragment.append(grids_generics);
    htmlFragment.append(repositories);
    htmlFragment.append(network_proxies);
    htmlFragment.append(proxies_connectors);
    htmlFragment.append(repository_groups);
    htmlFragment.append(search);
    htmlFragment.append(general_admin);
    $.log("main-tmpl.js loaded");
  }
);