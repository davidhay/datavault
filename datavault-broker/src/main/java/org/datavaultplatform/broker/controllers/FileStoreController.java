package org.datavaultplatform.broker.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.datavaultplatform.broker.services.UserKeyPairService;
import org.datavaultplatform.common.model.User;
import org.datavaultplatform.common.model.FileStore;

import org.jsondoc.core.annotation.ApiHeader;
import org.jsondoc.core.annotation.ApiHeaders;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiVerb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.datavaultplatform.broker.services.FileStoreService;
import org.datavaultplatform.broker.services.UsersService;

@RestController
public class FileStoreController {

    private static final Logger logger = LoggerFactory.getLogger(FileStoreController.class);

    private UsersService usersService;
    private FileStoreService fileStoreService;
    private UserKeyPairService userKeyPairService;
    private String host;
    private String port;
    private String rootPath;
    private String passphrase;
    
    public void setFileStoreService(FileStoreService fileStoreService) {
        this.fileStoreService = fileStoreService;
    }
    
    public void setUsersService(UsersService usersService) {
        this.usersService = usersService;
    }

    public void setUserKeyPairService(UserKeyPairService userKeyPairService) {
        this.userKeyPairService = userKeyPairService;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    @RequestMapping(value = "/filestores", method = RequestMethod.GET)
    public List<FileStore> getFileStores(@RequestHeader(value = "X-UserID", required = true) String userID) {
        User user = usersService.getUser(userID);
        
        List<FileStore> userStores = user.getFileStores();
        for (FileStore store : userStores) {
            // For now - strip out config information
            store.setProperties(null);
        }
        
        return userStores;
    }

    @RequestMapping(value = "/filestores", method = RequestMethod.POST)
    public FileStore addFileStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                  @RequestBody FileStore store) throws Exception {
        
        User user = usersService.getUser(userID);
        store.setUser(user);
        fileStoreService.addFileStore(store);
        return store;
    }

    @RequestMapping(value = "/filestores/{filestoreid}", method = RequestMethod.GET)
    public FileStore getPublicKey(@RequestHeader(value = "X-UserID", required = true) String userID,
                               @PathVariable("filestoreid") String filestoreid) {

        FileStore store = fileStoreService.getFileStore(filestoreid);
        return store;
    }

    @RequestMapping(value = "filestores/{filestoreid}", method = RequestMethod.DELETE)
    public @ResponseBody void deleteFileStore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                               @PathVariable("filestoreid") String filestoreid) {

        fileStoreService.deleteFileStore(filestoreid);

    }


    @RequestMapping(value = "/filestores/local", method = RequestMethod.GET)
    public List<FileStore> getFileStoresLocal(@RequestHeader(value = "X-UserID", required = true) String userID) {
        User user = usersService.getUser(userID);

        List<FileStore> userStores = user.getFileStores();
        List<FileStore> localStores = new ArrayList<>();

        for (FileStore userStore : userStores) {
            if (userStore.getStorageClass().equals("org.datavaultplatform.common.storage.impl.LocalFileSystem")) {
                localStores.add(userStore);
            }
        }

        return localStores;
    }


    @RequestMapping(value = "/filestores/sftp", method = RequestMethod.POST)
    public FileStore addKeyPair(@RequestHeader(value = "X-UserID", required = true) String userID) throws Exception {
        User user = usersService.getUser(userID);

        userKeyPairService.generateNewKeyPair();

        HashMap<String,String> storeProperties = new HashMap<String,String>();
        storeProperties.put("host", host);
        storeProperties.put("port", port);
        storeProperties.put("rootPath", rootPath);
        storeProperties.put("username", user.getID());
        storeProperties.put("password", "");
        storeProperties.put("publicKey", userKeyPairService.getPublicKey());
        storeProperties.put("privateKey", userKeyPairService.getPrivateKey());
        storeProperties.put("passphrase", passphrase);

        FileStore store = new FileStore("org.datavaultplatform.common.storage.impl.SFTPFileSystem", storeProperties, "SFTP filesystem");
        store.setUser(user);
        fileStoreService.addFileStore(store);

        // Remove sensitive information that should only be held server side.
        storeProperties = store.getProperties();
        storeProperties.remove("password");
        storeProperties.remove("privateKey");
        storeProperties.remove("passphrase");
        store.setProperties(storeProperties);

        return store;
    }

    @RequestMapping(value = "/filestores/sftp/{filestoreid}", method = RequestMethod.GET)
    public FileStore getSftpFilestore(@RequestHeader(value = "X-UserID", required = true) String userID,
                                   @PathVariable("filestoreid") String filestoreid) {

        FileStore store = fileStoreService.getFileStore(filestoreid);

        // Remove sensitive information that should only be held server side.
        HashMap<String,String> storeProperties = store.getProperties();
        storeProperties.remove("password");
        storeProperties.remove("privateKey");
        storeProperties.remove("passphrase");
        store.setProperties(storeProperties);

        return store;

    }


}
