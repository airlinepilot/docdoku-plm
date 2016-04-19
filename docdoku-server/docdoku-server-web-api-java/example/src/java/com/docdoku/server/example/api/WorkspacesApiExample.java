package com.docdoku.server.example.api;


import com.docdoku.server.api.client.ApiException;
import com.docdoku.server.api.models.DocumentRevisionDTO;
import com.docdoku.server.api.models.UserGroupDTO;
import com.docdoku.server.api.models.WorkspaceDTO;
import com.docdoku.server.api.models.WorkspaceDetailsDTO;
import com.docdoku.server.api.services.WorkspacesApi;
import com.docdoku.server.example.utils.ErrorHelper;

import java.util.List;


/**
 * This class calls some WorkspacesApi methods
 * @Author Morgan Guimard
 */
public class WorkspacesApiExample extends DocdokuPLMApiExample {

    private WorkspacesApi workspacesApi;
    private final static String USER_GROUP_ID = "Group1";

    @Override
    public void run() {
        workspacesApi = new WorkspacesApi(plmClient.getClient());
        createWorkspace();
        createUserGroup();
        getWorkspaceDetailsList();
        listDocumentsInWorkspace();
        updateWorkspaceDescription();
    }

    private void createUserGroup() {
        try {
            UserGroupDTO groupToCreate = new UserGroupDTO();
            groupToCreate.setId(USER_GROUP_ID);
            groupToCreate.setWorkspaceId(WORKSPACE);
            workspacesApi.createGroup(WORKSPACE, groupToCreate);
        } catch (ApiException e) {
            ErrorHelper.onError("Error while creating user group", plmClient.getClient());
        }
    }

    private void updateWorkspaceDescription() {
        try {
            WorkspaceDTO workspaceDTO = new WorkspaceDTO();
            workspaceDTO.setId(WORKSPACE);
            workspaceDTO.setDescription("Updated description");
            workspacesApi.updateWorkspace(WORKSPACE, workspaceDTO);
        } catch (ApiException e) {
            ErrorHelper.onError("Error while updating workspace", plmClient.getClient());
        }
    }

    private void listDocumentsInWorkspace() {
        try {
            List<DocumentRevisionDTO> documentsInWorkspace = workspacesApi.getDocumentsInWorkspace(WORKSPACE, 0, null);
            System.out.println(documentsInWorkspace);
        } catch (ApiException e) {
            ErrorHelper.onError("Error while listing documents in workspace", plmClient.getClient());
        }
    }


    private void createWorkspace(){
        try {
            WorkspaceDTO workspaceDTO = new WorkspaceDTO();
            workspaceDTO.setId(WORKSPACE);
            workspacesApi.createWorkspace(workspaceDTO,"user");
        } catch (ApiException e) {
            ErrorHelper.onError("Error while creating workspace", plmClient.getClient());
        }
    }


    private void getWorkspaceDetailsList() {
        try {
            List<WorkspaceDetailsDTO> detailedWorkspacesForConnectedUser = workspacesApi.getDetailedWorkspacesForConnectedUser();
            System.out.println(detailedWorkspacesForConnectedUser);
        } catch (ApiException e) {
            ErrorHelper.onError("Error while getting detailed workspace list", plmClient.getClient());
        }
    }

}
