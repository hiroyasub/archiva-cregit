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
$(function() {

  ProxyConnector=function(sourceRepoId,targetRepoId,proxyId,blackListPatterns,whiteListPatterns,policies,properties,
                          disabled,order){
    var self=this;
    this.modified=ko.observable(false);

    //private String sourceRepoId;
    this.sourceRepoId=ko.observable(sourceRepoId);
    this.sourceRepoId.subscribe(function(newValue){self.modified(true)});

    //private String targetRepoId;
    this.targetRepoId=ko.observable(targetRepoId);
    this.targetRepoId.subscribe(function(newValue){self.modified(true)});

    //private String proxyId;
    this.proxyId=ko.observable(proxyId);
    this.proxyId.subscribe(function(newValue){self.modified(true)});

    //private List<String> blackListPatterns;
    this.blackListPatterns=ko.observableArray(blackListPatterns);
    this.blackListPatterns.subscribe(function(newValue){self.modified(true)});

    //private List<String> whiteListPatterns;
    this.whiteListPatterns=ko.observableArray(whiteListPatterns);
    this.whiteListPatterns.subscribe(function(newValue){self.modified(true)});

    //private Map<String, String> policies;
    this.policies=ko.observable(policies);
    this.policies.subscribe(function(newValue){self.modified(true)});

    //private Map<String, String> properties;
    this.properties=ko.observable(properties);
    this.properties.subscribe(function(newValue){self.modified(true)});

    //private boolean disabled = false;
    this.disabled=ko.observable(disabled);
    this.disabled.subscribe(function(newValue){self.modified(true)});

    //private int order = 0;
    this.order=ko.observable(order);
    this.order.subscribe(function(newValue){self.modified(true)});

    this.modified=ko.observable(true);
    this.modified.subscribe(function(newValue){$.log("ProxyConnector modified")});
  }

  PolicyInformation=function(options,defaultOption,id,name){

    var self=this;
    this.modified=ko.observable(false);

    //private List<String> options;
    this.options=ko.observableArray(options);
    this.options.subscribe(function(newValue){self.modified(true)});

    //private String defaultOption;
    this.defaultOption=ko.observable(defaultOption);
    this.defaultOption.subscribe(function(newValue){self.modified(true)});

    //private String id;
    this.id=ko.observable(id);
    this.id.subscribe(function(newValue){self.modified(true)});

    //private String name;
    this.name=ko.observable(name);
    this.name.subscribe(function(newValue){self.modified(true)});

  }

  ManagedRepositoryConnectorView=function(source,targetRepos){
    var self=this;
    this.modified=ko.observable(false);


    this.source=ko.observable(source);
    this.targetRepos=ko.observableArray(targetRepos);
  }

  ProxyConnectorViewModel=function(proxyConnector,update,proxyConnectorsViewModel){
    var self=this;
    this.proxyConnector=ko.observable(proxyConnector);
    this.proxyConnectorsViewModel=proxyConnectorsViewModel;
    this.update=update;
    getSelectedPolicyOption=function(id){
      var policies=self.proxyConnector().policies();
      for (i=0;i<policies().length;i++){
        if (id==policies()[i].key()){
          return policies()[i].value();
        }
      }
      return "";
    }
    getPolicyOptions=function(id){
      var policyInformations=self.proxyConnectorsViewModel.policyInformations();
      for(i=0;i<policyInformations.length;i++){
        $.log("getPolicyOptions:"+id+",cur:"+policyInformations[i].id());
        if (policyInformations[i].id()==id){
          return policyInformations[i].options();
        }
      }
    }
  }

  ProxyConnectorsViewModel=function(){
    var self=this;
    this.proxyConnectors=ko.observableArray([]);
    this.managedRepositoryConnectorViews=ko.observableArray([]);
    this.policyInformations=ko.observableArray([]);
    this.managedRepositories=ko.observableArray([]);
    this.remoteRepositories=ko.observableArray([]);
    this.networkProxies=ko.observableArray([]);

    editProxyConnector=function(proxyConnector){

    }

    this.findUniqueManagedRepos=function(){
      var sourcesRepos=[];
      //sourceRepoId
      for(i=0;i<self.proxyConnectors().length;i++){
        var curSrcRepo=self.proxyConnectors()[i].sourceRepoId();
        var curTarget=self.proxyConnectors()[i];
        var sourceRepo = $.grep(sourcesRepos,
                                function(srcRepo,idx){
                                  for (j=0;j<sourcesRepos.length;j++){
                                    if (srcRepo.source()==curSrcRepo){
                                      return true;
                                    }
                                  }
                                  return false;
                                }
        );
        if (sourceRepo.length>0){
          sourceRepo[0].targetRepos.push(curTarget);
        } else {
          sourcesRepos.push(new ManagedRepositoryConnectorView(curSrcRepo,[curTarget]));
        }
      }
      return sourcesRepos;
    }

    getManagedRepository=function(id){
      var managedRepository=$.grep(self.managedRepositories(),
                                      function(repo,idx){
                                        return repo.id()==id;
                                      }
                            );
      return ($.isArray(managedRepository) && managedRepository.length>0) ? managedRepository[0]:new ManagedRepository();
    }

    getRemoteRepository=function(id){
      var remoteRepository=$.grep(self.remoteRepositories(),
                                      function(repo,idx){
                                        return repo.id()==id;
                                      }
                            );
      return ($.isArray(remoteRepository) && remoteRepository.length>0) ? remoteRepository[0]:new RemoteRepository();
    }

    this.getProxyConnector=function(sourceRepoId,targetRepoId){
      var proxyConnector=$.grep(self.proxyConnectors(),
                                      function(proxyConnector,idx){
                                        return proxyConnector.sourceRepoId()==sourceRepoId
                                            && proxyConnector.targetRepoId==targetRepoId;
                                      }
                                  );
      return ($.isArray(proxyConnector) && proxyConnector.length>0) ? proxyConnector[0]:new ProxyConnector();
    }

    showSettings=function(sourceRepoId,targetRepoId){
      var targetContent = $("#proxy-connectors-grid-remoterepo-settings-content-"+sourceRepoId+"-"+targetRepoId);
      targetContent.html("");
      targetContent.append($("#proxy-connectors-remote-settings-popover-tmpl")
                               .tmpl(self.getProxyConnector(sourceRepoId,targetRepoId)));

      var targetImg = $("#proxy-connectors-grid-remoterepo-settings-edit-"+sourceRepoId+"-"+targetRepoId);
      targetImg.attr("data-content",targetContent.html());
      targetImg.popover(
          {
            placement: "left",
            html: true,
            title: "popover-title"
          }
      );

      targetImg.popover('show');

    }

    this.displayGrid=function(){
      self.managedRepositoryConnectorViews(this.findUniqueManagedRepos());
      this.gridViewModel = new ko.simpleGrid.viewModel({
        data: self.managedRepositoryConnectorViews,
        pageSize: 5,
        gridUpdateCallBack: function(){
          $("#main-content #proxyConnectorsTable [title]").tooltip();
        }
      });
      var mainContent = $("#main-content");
      this.gridViewModel.getManagedRepository=getManagedRepository;
      ko.applyBindings(this,mainContent.find("#proxyConnectorsTable").get(0));
      removeSmallSpinnerImg("#main-content");
      this,mainContent.find("#proxy-connectors-view-tabs #proxy-connectors-view-tabs-a-network-proxies-grid").tab('show');

      mainContent.find("#proxy-connectors-view-tabs").on('show', function (e) {

        if ($(e.target).attr("href")=="#proxy-connectors-edit") {
          var proxyConnector=new ProxyConnector();
          for (i=0;i<self.policyInformations.length;i++){
            //proxyConnector[]
          }
          var proxyConnectorViewModel=new ProxyConnectorViewModel(proxyConnector,false,self);
          ko.applyBindings(proxyConnectorViewModel,mainContent.find("#proxy-connectors-edit").get(0));
          //ko.applyBindings(proxyConnectorViewModel,mainContent.find("#proxy-connector-policies-binding").get(0));

        }
        if ($(e.target).attr("href")=="#proxy-connectors-view") {
          $("#proxy-connectors-view-tabs-a-network-proxies-grid").html($.i18n.prop("add"));

        }

      });
    }

  }

  displayProxyConnectors=function(){
    var mainContent = $("#main-content");
    mainContent.html($("#proxyConnectorsMain").tmpl());
    mainContent.append(smallSpinnerImg());

    this.proxyConnectorsViewModel = new ProxyConnectorsViewModel();
    var self=this;

    loadManagedRepositories(function(data) {
      self.proxyConnectorsViewModel.managedRepositories(mapManagedRepositories(data));

      loadRemoteRepositories(function(data) {
        self.proxyConnectorsViewModel.remoteRepositories(mapRemoteRepositories(data));

        loadNetworkProxies(function(data) {
          self.proxyConnectorsViewModel.networkProxies(mapNetworkProxies(data));

          loadAllPolicies( function(data) {
            self.proxyConnectorsViewModel.policyInformations(mapPolicyInformations(data));

            loadAllProxyConnectors( function(data) {
              self.proxyConnectorsViewModel.proxyConnectors(mapProxyConnectors(data));
              self.proxyConnectorsViewModel.displayGrid();
            });

          });

        });

      });

    });

  }

  loadAllPolicies=function(successCallBackFn,errorCallBackFn){
    $.ajax("restServices/archivaServices/proxyConnectorService/allPolicies", {
        type: "GET",
        dataType: 'json',
        success: successCallBackFn,
        error: errorCallBackFn
      }
    );
  }

  loadAllProxyConnectors=function(successCallBackFn,errorCallBackFn){
    $.ajax("restServices/archivaServices/proxyConnectorService/getProxyConnectors", {
      type: "GET",
      dataType: 'json',
      success: successCallBackFn,
      error: errorCallBackFn
     });
  }

  mapProxyConnector=function(data){
    if (data==null){
      return null;
    }
    var policies = data.policies == null ? null: $.each(data.policies,function(item){
      return new Entry(item.key, item.value);
    });
    var properties = data.properties == null ? null: $.each(data.properties,function(item){
          return new Entry(item.key, item.value);
        });
    return new ProxyConnector(data.sourceRepoId,data.targetRepoId,data.proxyId,mapStringArray(data.blackListPatterns),
                              mapStringArray(data.whiteListPatterns),policies,properties,
                              data.disabled,data.order);
  }

  mapProxyConnectors=function(data){
    var mappedProxyConnectors = $.map(data.proxyConnector, function(item) {
      return mapProxyConnector(item);
    });
    return mappedProxyConnectors;
  }

  mapPolicyInformation=function(data){
    if (data==null){
      return null;
    }
    return new PolicyInformation(mapStringArray(data.options),data.defaultOption,data.id,data.name);
  }

  mapPolicyInformations=function(data){
    return $.map(data.policyInformation, function(item) {
              return mapPolicyInformation(item);
           });
  }

});

(function () {


  // Templates used for rendering
  var templateEngine = new ko.jqueryTmplTemplateEngine();
  TemporaryViewModel=function(currentPolicyInformation,selectedOption){
    var self=this;
    this.policyInformation=currentPolicyInformation;
    this.selectedValue=selectedOption;
    isSelectedOption=function(id){
      return (id==self.selectedValue);
    }
  }
  ko.bindingHandlers.proxyConnectorPolicies = {
      update: function(element, valueAccessor, allBindingsAccessor, proxyConnectorViewModel) {
        // This will be called once when the binding is first applied to an element,
        // and again whenever the associated observable changes value.
        // Update the DOM element based on the supplied values here.
        var jqueryElement=$(element);
        $.log("update:"+proxyConnectorViewModel.proxyConnectorsViewModel.managedRepositories().length);
        // Empty the element
        while(element.firstChild) {
            ko.removeNode(element.firstChild);
        }
        var policyInformations = proxyConnectorViewModel.proxyConnectorsViewModel.policyInformations();
        for (i=0;i<policyInformations.length;i++){
          var currentPolicyInformation=policyInformations[i];
          var gridContainer = element.appendChild(document.createElement("DIV"));
          var selectedOption =
              proxyConnectorViewModel.update ?
                  proxyConnectorViewModel.getSelectedOptionForPolicy(currentPolicyInformation.id()):currentPolicyInformation.defaultOption();

          var tmpViewModel = new TemporaryViewModel(currentPolicyInformation,selectedOption);
          ko.renderTemplate("proxy-connector-edit-form-policies-tmpl", tmpViewModel
                            , { templateEngine: templateEngine }, gridContainer, "replaceNode");
          ko.utils.registerEventHandler($("#policy-"+currentPolicyInformation.id()).get(0), "change", function () {
            $.log("change policy:"+$(this).attr('value')+" for policy " + tmpViewModel.currentPolicyInformation.id());
          });
          /*.change(function(event){
            $.log("change policy:"+$(this).attr('value')+" for policy " + tmpViewModel.currentPolicyInformation.id());
          });*/
        }
      }
  };
})();