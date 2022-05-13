package org.datavaultplatform.broker.controllers.admin;

import org.datavaultplatform.broker.services.RetentionPoliciesService;
import org.datavaultplatform.common.model.RetentionPolicy;
import org.datavaultplatform.common.request.CreateRetentionPolicy;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiHeader;
import org.jsondoc.core.annotation.ApiHeaders;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.pojo.ApiVerb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(name="AdminRetentionPolicies", description = "Administrator RetentionPolicies functions")
public class AdminRetentionPoliciesController {

    private final Logger logger = LoggerFactory.getLogger(AdminRetentionPoliciesController.class);

    private final RetentionPoliciesService retentionPoliciesService;

    public AdminRetentionPoliciesController(RetentionPoliciesService retentionPoliciesService) {
        this.retentionPoliciesService = retentionPoliciesService;
    }

    @ApiMethod(
            path = "/admin/retentionpolicies}",
            verb = ApiVerb.POST,
            description = "Create a new Retention Policy",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID"),
            @ApiHeader(name="X-Client-Key", description="DataVault API Client Key")
    })
    @PostMapping("/admin/retentionpolicies")
    public ResponseEntity<CreateRetentionPolicy> addRetentionPolicy(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                                    @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                                              @RequestBody CreateRetentionPolicy createRetentionPolicy) throws Exception {

        logger.info("Adding new RetentionPolicy with name = " + createRetentionPolicy.getName());

        RetentionPolicy retentionPolicy = retentionPoliciesService.buildRetentionPolicy(createRetentionPolicy);

        try{
            retentionPoliciesService.addRetentionPolicy(retentionPolicy);
        }catch(Exception ex){
            logger.error("Couldn't add retention policy: ", ex);
            return new ResponseEntity<>(createRetentionPolicy, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //TODO - when returning HttpStatus.CREATED we should add Location header with URL of created resource
        return new ResponseEntity<>(createRetentionPolicy, HttpStatus.CREATED);
    }


    @ApiMethod(
            path = "/admin/retentionpolicies/{policyid}",
            verb = ApiVerb.GET,
            description = "Get a Retention Policy",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID"),
            @ApiHeader(name="X-Client-Key", description="DataVault API Client Key")
    })
    @GetMapping("/admin/retentionpolicies/{policyid}")
    public ResponseEntity<CreateRetentionPolicy> getRetentionPolicy(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                                    @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                                                    @PathVariable("policyid") String policyid) {

        logger.info("Getting RetentionPolicy with id = " + policyid) ;

        // Build a transfer object from the real one. We do this to avoid problems with dates, although I am sure there
        // is a better way to do it.

        RetentionPolicy rp = retentionPoliciesService.getPolicy(policyid);
        CreateRetentionPolicy crp = retentionPoliciesService.buildCreateRetentionPolicy(rp);

        return new ResponseEntity<>(crp, HttpStatus.OK);
    }

    @ApiMethod(
            path = "/admin/retentionpolicies}",
            verb = ApiVerb.PUT,
            description = "Edit a Retention Policy",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID"),
            @ApiHeader(name="X-Client-Key", description="DataVault API Client Key")
    })
    @PutMapping("/admin/retentionpolicies")
    public ResponseEntity<CreateRetentionPolicy> editRetentionPolicy(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                                     @RequestHeader(value = "X-Client-Key", required = true) String clientKey,
                                                                    @RequestBody CreateRetentionPolicy crp) {

        RetentionPolicy rp = retentionPoliciesService.buildRetentionPolicy(crp);
        retentionPoliciesService.updateRetentionPolicy(rp);

        return new ResponseEntity<>(crp, HttpStatus.OK);
    }

    @DeleteMapping("/admin/retentionpolicies/delete/{id}")
    public void deleteRetentionPolicy(@RequestHeader(value = "X-UserID", required = true) String userID,
                                      @PathVariable("id") String policyID) {

        retentionPoliciesService.delete(policyID);
    }





}
