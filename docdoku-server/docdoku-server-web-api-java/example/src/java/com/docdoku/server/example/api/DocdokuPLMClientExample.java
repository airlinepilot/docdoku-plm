package com.docdoku.server.example.api;


import com.docdoku.server.api.DocdokuPLMClient;
import com.docdoku.server.api.client.ApiClient;
import com.docdoku.server.api.client.ApiException;
import com.docdoku.server.api.models.AccountDTO;
import com.docdoku.server.api.models.DocumentRevisionDTO;
import com.docdoku.server.api.models.FolderDTO;
import com.docdoku.server.api.models.WorkspaceDTO;
import com.docdoku.server.api.services.AccountsApi;
import com.docdoku.server.api.services.FoldersApi;
import com.docdoku.server.api.services.WorkspacesApi;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Morgan Guimard
 */
public class DocdokuPLMClientExample {

    private final static String API_URL = "http://localhost:8080/api";
    private final static String USERNAME = "user";
    private final static String PASSWORD = "password";
    private final static String WORKSPACE = "user-workspace";
    private final static String FOLDERA = "FA";
    private final static String FOLDERB = "FB";
    private final static boolean DEBUG = true;

    private static final Logger LOGGER = Logger.getLogger(DocdokuPLMClientExample.class.getName());

    public static void main(String[] args) throws UnsupportedEncodingException {

        DocdokuPLMClient plmClient = new DocdokuPLMClient(API_URL, USERNAME, PASSWORD, DEBUG);
        ApiClient client = plmClient.getClient();

        AccountsApi accountsApi = new AccountsApi(client);
        WorkspacesApi workspacesApi = new WorkspacesApi(client);
        FoldersApi foldersApi = new FoldersApi(client);

        try {
            AccountDTO account = accountsApi.getAccount();
            System.out.println(account);
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode() , e);
        }

        try {
            List<WorkspaceDTO> workspaces = accountsApi.getWorkspaces();
            System.out.println(workspaces);
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode() , e);
        }

        WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(WORKSPACE);

        try {
            workspacesApi.createWorkspace(workspaceDTO,"user");
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode() , e);

        }

        try {
            List<FolderDTO> rootFolders = foldersApi.getRootFolders(WORKSPACE, null);
            System.out.println(rootFolders);
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode(), e);
        }

        FolderDTO folderDTO = new FolderDTO();
        folderDTO.setName(FOLDERB);

        try {
            foldersApi.createSubFolder(WORKSPACE, WORKSPACE+":"+FOLDERA, folderDTO);
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode(), e);
        }

        try {
            List<DocumentRevisionDTO> documentsWithGivenFolderIdAndWorkspaceId = foldersApi.getDocumentsWithGivenFolderIdAndWorkspaceId(WORKSPACE, WORKSPACE + ":" + FOLDERA, null);
            System.out.println(documentsWithGivenFolderIdAndWorkspaceId);
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode(), e);
        }

        try {
            List<DocumentRevisionDTO> documentsInWorkspace = workspacesApi.getDocumentsInWorkspace(WORKSPACE, 0, null);
            System.out.println(documentsInWorkspace);
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, "Error " + client.getStatusCode(), e);
        }

    }



}