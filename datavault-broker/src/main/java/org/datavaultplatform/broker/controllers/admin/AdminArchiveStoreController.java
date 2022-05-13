package org.datavaultplatform.broker.controllers.admin;

import org.datavaultplatform.broker.services.ArchiveStoreService;
import org.datavaultplatform.common.model.ArchiveStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminArchiveStoreController {

    private final ArchiveStoreService archiveStoreService;

    private static final Logger logger = LoggerFactory.getLogger(AdminArchiveStoreController.class);

    @Autowired
    public AdminArchiveStoreController(ArchiveStoreService archiveStoreService) {
        this.archiveStoreService = archiveStoreService;
    }

    @GetMapping("/admin/archivestores")
    public ResponseEntity<List<ArchiveStore>> getArchiveStores(@RequestHeader(value = "X-UserID", required = true) String userID) {

        List<ArchiveStore> archiveStores = archiveStoreService.getArchiveStores();
        return new ResponseEntity<>(archiveStores, HttpStatus.OK);
    }

    @GetMapping("/admin/archivestores/{archivestoreid}")
    public ResponseEntity<ArchiveStore> getArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID, @PathVariable("archivestoreid") String archivestoreid) {

        return new ResponseEntity<>(archiveStoreService.getArchiveStore(archivestoreid), HttpStatus.OK);
    }

    @PostMapping("/admin/archivestores")
    public ResponseEntity<ArchiveStore> addArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                        @RequestBody ArchiveStore store) {
        try{
            archiveStoreService.addArchiveStore(store);
        }catch(Exception e){
            System.err.println("Couldn't add archive store: "+ e.getMessage());
            return new ResponseEntity<>(store, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(store, HttpStatus.CREATED);
    }

    @PutMapping("/admin/archivestores")
    public ResponseEntity<ArchiveStore> editArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                         @RequestBody ArchiveStore store) {

        archiveStoreService.updateArchiveStore(store);
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    @DeleteMapping("/admin/archivestores/{archivestoreid}")
    public ResponseEntity<Void>  deleteArchiveStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                                      @PathVariable("archivestoreid") String archivestoreid) {

        archiveStoreService.deleteArchiveStore(archivestoreid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
