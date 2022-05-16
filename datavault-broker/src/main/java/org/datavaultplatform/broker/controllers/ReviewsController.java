package org.datavaultplatform.broker.controllers;

import org.datavaultplatform.broker.services.*;
import org.datavaultplatform.common.model.*;
import org.datavaultplatform.common.response.ReviewInfo;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiHeader;
import org.jsondoc.core.annotation.ApiHeaders;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Api(name="Reviews", description = "Review functions")
public class ReviewsController {

    private final VaultsService vaultsService;
    private final VaultsReviewService vaultsReviewService;
    private final DepositsReviewService depositsReviewService;
    private final UsersService usersService;
    private final ClientsService clientsService;
    private final EventService eventService;

    @Autowired
    public ReviewsController(VaultsService vaultsService, VaultsReviewService vaultsReviewService,
        DepositsReviewService depositsReviewService, UsersService usersService,
        ClientsService clientsService, EventService eventService) {
        this.vaultsService = vaultsService;
        this.vaultsReviewService = vaultsReviewService;
        this.depositsReviewService = depositsReviewService;
        this.usersService = usersService;
        this.clientsService = clientsService;
        this.eventService = eventService;
    }

    @ApiMethod(
            path = "/vaults/{vaultid}/vaultreviews",
            verb = ApiVerb.GET,
            description = "Gets a list of Reviews for a Vault",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID")
    })
    @GetMapping("/vaults/{vaultid}/vaultreviews")
    public List<ReviewInfo> getVaultReviews(@RequestHeader(value = "X-UserID", required = true) String userID,
                                             @PathVariable("vaultid") String vaultID) throws Exception {

        User user = usersService.getUser(userID);
        Vault vault = vaultsService.getUserVault(user, vaultID);

        List<ReviewInfo> reviewinfos = new ArrayList<ReviewInfo>();

        for (VaultReview vr : vault.getVaultReviews()) {

            VaultReview vaultReview = vr;
            List <DepositReview> depositReviews = vr.getDepositReviews();

            // Create Lists of Deposit and DepositReview ids
            List<String> depositIds = new ArrayList<>();
            List<String> depositReviewIds = new ArrayList<>();

            for (DepositReview depositReview : depositReviews) {
                depositIds.add(depositReview.getDeposit().getID());
                depositReviewIds.add(depositReview.getId());
            }

            ReviewInfo reviewInfo = new ReviewInfo();
            reviewInfo.setVaultReviewId(vaultReview.getId());
            reviewInfo.setDepositIds(depositIds);
            reviewInfo.setDepositReviewIds(depositReviewIds);

            reviewinfos.add(reviewInfo);
        }

        return reviewinfos;
    }


    @ApiMethod(
            path = "/vaults/vaultreviews/{vaultReviewId}",
            verb = ApiVerb.GET,
            description = "Get a Vault Review",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID"),
            @ApiHeader(name="X-Client-Key", description="DataVault API Client Key")
    })
    @GetMapping( "/vaults/vaultreviews/{vaultReviewId}")
    public VaultReview getVaultReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                @PathVariable("vaultReviewId") String vaultReviewId) {

        return vaultsReviewService.getVaultReview(vaultReviewId);
    }



    @ApiMethod(
            path = "/vaultreviews/{vaultReviewId}/depositreviews",
            verb = ApiVerb.GET,
            description = "Gets a list of Deposit Reviews for a Vault Review",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID")
    })
    @GetMapping("/vaultreviews/{vaultReviewId}/depositreviews")
    public List<DepositReview> getDepositReviews(@RequestHeader(value = "X-UserID", required = true) String userID,
                                             @PathVariable("vaultReviewId") String vaultReviewId) {

        VaultReview vaultReview = vaultsReviewService.getVaultReview(vaultReviewId);
        return vaultReview.getDepositReviews();

    }

    @ApiMethod(
            path = "/vaultreviews/depositreviews/{depositReviewId}",
            verb = ApiVerb.GET,
            description = "Gets a particular Deposit Review",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            responsestatuscode = "200 - OK"
    )
    @ApiHeaders(headers={
            @ApiHeader(name="X-UserID", description="DataVault Broker User ID")
    })
    @GetMapping("/vaultreviews/depositreviews/{depositReviewId}")
    public DepositReview getDepositReview(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                 @PathVariable("depositReviewId") String depositReviewId) {

        DepositReview depositReview = depositsReviewService.getDepositReview(depositReviewId);
        return depositReview;

    }


}
