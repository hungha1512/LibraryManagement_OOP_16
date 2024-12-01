package com.hunghq.librarymanagement.Global;

import com.hunghq.librarymanagement.Respository.DocumentDAO;

public class GenerateDocumentID {

    private DocumentDAO documentDAO = new DocumentDAO();

    public String generateDocumentID() {
        String prefix = "UET";
        int maxLength = 8;
        String currentMaxID = documentDAO.getMaxDocumentID();

        int newIDNumber = 1;

        if (currentMaxID != null && currentMaxID.startsWith(prefix)) {
            String numberPart = currentMaxID.substring(prefix.length());
            newIDNumber = Integer.parseInt(numberPart) + 1;
        }

        String formattedID = String.format("%0" + maxLength + "d", newIDNumber);

        return prefix + formattedID;
    }



}
