package org.cjoakim.cosmosdb.common.io;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;
import lombok.extern.slf4j.Slf4j;
import org.cjoakim.cosmosdb.common.CommonConfig;

import java.util.ArrayList;

/**
 * This class implements common Azure Storage Blob operations.
 *
 * Chris Joakim, Microsoft
 */

@Slf4j
public class BlobUtil {

    // Instance variables:
    private BlobServiceClient blobServiceClient = null;

    public BlobUtil() {

        super();
        String connString = CommonConfig.getStorageConnectionString();
        blobServiceClient = new BlobServiceClientBuilder().connectionString(connString).buildClient();
    }

    public ArrayList<String> listContainers() {

        ArrayList<String> containerNames = new ArrayList<String>();
        try {
            for (BlobContainerItem blobContainer : blobServiceClient.listBlobContainers()) {
                containerNames.add(blobContainer.getName());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return containerNames;
    }

    public BlobContainerClient createContainer(String cname) {

        try {
            return blobServiceClient.createBlobContainer(cname);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BlobContainerClient getContainerClient(String cname) {

        try {
            return blobServiceClient.getBlobContainerClient(cname);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> listBlobs(String rootContainer) {

        ArrayList<String> list = new ArrayList<String>();
        for (BlobItem blobItem : getContainerClient(rootContainer).listBlobs()) {
            list.add(blobItem.getName());
        }
        return list;
    }

    public ArrayList<String> getBlobsMatching(String rootContainer, String prefix, String suffix) {

        ArrayList<String> blobList = listBlobs(rootContainer);
        ArrayList<String> matchList = new  ArrayList<String>();
        for (String blobPath : blobList) {
            if (blobPath.endsWith(suffix)) {
                if (blobPath.startsWith(prefix)) {
                    matchList.add(blobPath);
                }
            }
        }
        return matchList;
    }

    public boolean downloadBlob(String cname, String blobPath, String outfile) {

        try {
            BlobContainerClient cc = getContainerClient(cname);
            BlobClient bc = cc.getBlobClient(blobPath);
            bc.downloadToFile(outfile, true);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String downloadBlobAsString(String cname, String blobPath) {

        try {
            BlobContainerClient cc = getContainerClient(cname);
            BlobClient bc = cc.getBlobClient(blobPath);
            return bc.downloadContent().toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean uploadBlob(String cname, String infile) {

        try {
            BlobContainerClient cc = getContainerClient(cname);
            BlobClient bc = cc.getBlobClient(infile);
            bc.uploadFromFile(infile);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

